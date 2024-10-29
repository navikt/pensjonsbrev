describe("Brevvelger spec", () => {
  beforeEach(() => {
    cy.setupSakStubs();
    cy.viewport(1200, 1400);
  });

  it("Søk med saksnummer", () => {
    cy.visit("/");
    cy.contains("Saksnummer").click();
    cy.focused().type("123{enter}");
    cy.contains("Finner ikke saksnummer").should("exist");
    cy.focused().type("456{enter}");

    cy.contains("Søk etter brevmal"); // TODO: assert something smarter?
  });
  it("Søk etter brevmal", () => {
    cy.visit("/saksnummer/123456/brevvelger");

    cy.getDataCy("brevmal-search").click();
    cy.focused().type("b");
    cy.getDataCy("category-item").should("have.length", 7).and("have.class", "navds-accordion__item--open");
    cy.getDataCy("brevmal-button").should("have.length", 23);

    cy.focused().type("r");
    cy.getDataCy("category-item").should("have.length", 2).and("have.class", "navds-accordion__item--open");
    cy.getDataCy("brevmal-button").should("have.length", 6);

    cy.focused().type("e");
    cy.getDataCy("category-item").should("have.length", 2).and("have.class", "navds-accordion__item--open");
    cy.getDataCy("brevmal-button").should("have.length", 3);

    cy.focused().type("!");
    cy.getDataCy("category-item").should("have.length", 0);
    cy.getDataCy("brevmal-button").should("have.length", 0);
    cy.getDataCy("ingen-treff-alert");

    cy.focused().type("{esc}");
    cy.getDataCy("category-item").should("have.length", 9).and("not.have.class", "navds-accordion__item--open");
  });

  it("Favoritter", () => {
    cy.intercept("GET", "/bff/skribenten-backend/me/favourites", []).as("ingen favoritter");
    cy.visit("/saksnummer/123456/brevvelger");
    cy.get(".navds-accordion__item").contains("Favoritter").should("not.exist");

    cy.contains("Informasjonsbrev").click();
    cy.get("p:contains('Informasjon om gjenlevenderettigheter')").should("have.length", 1);
    cy.contains("Informasjon om gjenlevenderettigheter").click();

    cy.intercept("POST", "/bff/skribenten-backend/me/favourites", (request) => {
      expect(request.body).to.eq("DOD_INFO_RETT_MAN");
      request.reply({});
    }).as("Legg til favoritt");
    cy.intercept("GET", "/bff/skribenten-backend/me/favourites", ["DOD_INFO_RETT_MAN"]).as("1 favoritt");

    cy.getDataCy("add-favorite-button").click();
    cy.get(".navds-accordion__item").contains("Favoritter").should("exist").and("have.length", 1);
    //skal finnes 2 elementer i DOM'en
    cy.get("p:contains('Informasjon om gjenlevenderettigheter')").should("have.length", 2);
    cy.get("p:contains('Informasjon om gjenlevenderettigheter')").filter(":visible").should("have.length", 1);

    cy.intercept("DELETE", "/bff/skribenten-backend/me/favourites", (request) => {
      expect(request.body).to.eq("DOD_INFO_RETT_MAN");
      request.reply({});
    }).as("Fjern favoritt");
    cy.intercept("GET", "/bff/skribenten-backend/me/favourites", []).as("ingen favoritter");
    cy.getDataCy("remove-favorite-button").click();

    cy.get(".navds-accordion__item").contains("Favoritter").should("not.exist");
  });

  it("Bestill Exstream brev", () => {
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/bestillBrev/exstream", (request) => {
      expect(request.body).contains({
        brevkode: "PE_IY_05_027",
        idTSSEkstern: null,
        spraak: "NN",
        isSensitive: false,
        brevtittel: "",
        enhetsId: "4405",
      });
      request.reply({ fixture: "bestillBrevExstream.json" });
    }).as("bestill exstream");

    cy.visit("/saksnummer/123456/brevvelger", {
      onBeforeLoad(window) {
        cy.stub(window, "open").as("window-open");
      },
    });

    cy.getDataCy("category-item").contains("Feilutbetaling").click();
    cy.getDataCy("brevmal-button").contains("Varsel - tilbakekreving").click();

    cy.get("select[name=spraak]").select("Nynorsk");
    cy.get("select[name=enhetsId]").select("Nav Arbeid og ytelser Innlandet");

    cy.getDataCy("is-sensitive").contains("Nei").click({ force: true });

    //tanstack knappen hovrer over ferdigstill knappen - vå i klikker på vestre side av knappen som er synlig. Se om vi kan fikse dette
    cy.getDataCy("order-letter").click("left");
    cy.get("@window-open").should(
      "have.been.calledOnceWithExactly",
      "mbdok://PE2@brevklient/dokument/453864183?token=1711014877285&server=https%3A%2F%2Fwasapp-q2.adeo.no%2Fbrevweb%2F",
    );
    cy.getDataCy("order-letter-success-message");
  });

  it("Bestill Doksys brev", () => {
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/bestillBrev/doksys", (request) => {
      expect(request.body).contains({ brevkode: "DOD_INFO_RETT_MAN", spraak: "NB", enhetsId: "4405" });
      request.reply({ fixture: "bestillBrevDoksys.json" });
    }).as("bestill doksys");

    cy.visit("/saksnummer/123456/brevvelger", {
      onBeforeLoad(window) {
        cy.stub(window, "open").as("window-open");
      },
    });

    cy.getDataCy("brevmal-search").click().type("gjenlevende");
    cy.getDataCy("brevmal-button").click();

    cy.getDataCy("is-sensitive").should("not.exist");

    cy.get("select[name=enhetsId]").select("Nav Arbeid og ytelser Innlandet");
    //tanstack knappen hovrer over ferdigstill knappen - vå i klikker på vestre side av knappen som er synlig. Se om vi kan fikse dette
    cy.getDataCy("order-letter").click("left");
    cy.get("@window-open").should(
      "have.been.calledOnceWithExactly",
      "mfprocstart9:leaseid=c8cfd547-b80f-442b-8e7f-62f96ff52231",
    );
    cy.getDataCy("order-letter-success-message");
  });

  it("Skriv notat", () => {
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/bestillBrev/exstream", (request) => {
      expect(request.body).contains({
        brevkode: "PE_IY_03_156",
        spraak: "NB",
        isSensitive: true,
        brevtittel: "GGMU",
        enhetsId: "4405",
      });
      request.reply({ fixture: "bestillBrevNotat.json" });
    }).as("bestill notat");

    cy.visit("/saksnummer/123456/brevvelger", {
      onBeforeLoad(window) {
        cy.stub(window, "open").as("window-open");
      },
    });

    cy.getDataCy("brevmal-search").click().type("notat");
    cy.getDataCy("brevmal-button").click();

    cy.getDataCy("språk-velger-select").should("not.exist");

    cy.getDataCy("brev-title-textfield").click().type("GGMU");
    cy.get("select[name=enhetsId]").select("Nav Arbeid og ytelser Innlandet");
    //tanstack knappen hovrer over ferdigstill knappen - vå i klikker på vestre side av knappen som er synlig. Se om vi kan fikse dette
    cy.getDataCy("order-letter").click("left");

    cy.getDataCy("is-sensitive").get(".navds-error-message");
    cy.getDataCy("is-sensitive").contains("Ja").click({ force: true });

    //tanstack knappen hovrer over ferdigstill knappen - vå i klikker på vestre side av knappen som er synlig. Se om vi kan fikse dette
    cy.getDataCy("order-letter").click("left");
    cy.get("@window-open").should(
      "have.been.calledOnceWithExactly",
      "mbdok://PE2@brevklient/dokument/453864212?token=1711023327721&server=https%3A%2F%2Fwasapp-q2.adeo.no%2Fbrevweb%2F",
    );
    cy.getDataCy("order-letter-success-message");
  });

  it("Bestill E-blankett", () => {
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/bestillBrev/exstream/eblankett", (request) => {
      expect(request.body).contains({
        brevkode: "E001",
        landkode: "GBR",
        mottakerText: "Haaland",
        isSensitive: true,
        enhetsId: "4405",
      });
      request.reply({ fixture: "bestillBrevEblankett.json" });
    }).as("bestill e-blankett");

    cy.visit("/saksnummer/123456/brevvelger", {
      onBeforeLoad(window) {
        cy.stub(window, "open").as("window-open");
      },
    });

    cy.getDataCy("brevmal-search").click().type("E 001");
    cy.getDataCy("brevmal-button").click();

    //tanstack knappen hovrer over ferdigstill knappen - vå i klikker på vestre side av knappen som er synlig. Se om vi kan fikse dette
    cy.getDataCy("order-letter").click("left");
    cy.get("select[name=enhetsId]").select("Nav Arbeid og ytelser Innlandet");

    cy.getDataCy("is-sensitive").find(".navds-error-message");
    cy.getDataCy("is-sensitive").contains("Ja").click({ force: true });
    cy.getDataCy("is-sensitive").find(".navds-error-message").should("not.exist");

    cy.get("label").contains("Land").parent().find(".navds-error-message");
    cy.get("select[name=landkode]").select("Storbritannia");
    cy.get("label").contains("Land").parent().find(".navds-error-message").should("not.exist");

    cy.getDataCy("mottaker-text-textfield").parent().find(".navds-error-message");
    cy.getDataCy("mottaker-text-textfield").type("Haaland");
    cy.getDataCy("mottaker-text-textfield").parent().find(".navds-error-message").should("not.exist");

    //tanstack knappen hovrer over ferdigstill knappen - vå i klikker på vestre side av knappen som er synlig. Se om vi kan fikse dette
    cy.getDataCy("order-letter").click("left");
    cy.get("@window-open").should(
      "have.been.calledOnceWithExactly",
      "mbdok://PE2@brevklient/dokument/453864284?token=1711101230605&server=https%3A%2F%2Fwasapp-q2.adeo.no%2Fbrevweb%2F",
    );
    cy.getDataCy("order-letter-success-message");
  });
});
