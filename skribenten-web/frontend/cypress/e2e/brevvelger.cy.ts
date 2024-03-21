describe("template spec", () => {
  beforeEach(() => {
    cy.intercept("GET", "/bff/skribenten-backend/sak/**", { statusCode: 404 }).as("sakNotFound");
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456", { fixture: "sak.json" }).as("sak");
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/navn", { fixture: "navn.txt" }).as("navn");
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/adresse", { fixture: "adresse.json" }).as("adresse");
    cy.intercept("GET", "/bff/skribenten-backend/kodeverk/avtaleland", { fixture: "avtaleland.json" }).as("avtaleland");
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/foretrukketSpraak", {
      fixture: "foretrukketSpraak.json",
    }).as("foretrukketSpraak");

    cy.intercept("GET", "/bff/skribenten-backend/lettertemplates/**", { fixture: "letterTemplates.json" }).as(
      "templates",
    );

    cy.viewport(1200, 1400);
  });
  it("Verify saksnummer search", () => {
    cy.visit("/");
    cy.contains("Saksnummer").click();
    cy.focused().type("123{enter}");
    cy.contains("Finner ikke saksnummer").should("exist");
    cy.focused().type("456{enter}");

    cy.contains("Søk etter brevmal"); // TODO: assert something smarter?
  });

  it("Bestill Exstream brev", () => {
    cy.intercept("POST", "/bff/skribenten-backend/hentSamhandlerAdresse", { fixture: "hentSamhandlerAdresse.json" }).as(
      "hentSamhandlerAdresse",
    );
    cy.intercept("POST", "/bff/skribenten-backend/finnSamhandler", { fixture: "finnSamhandler.json" }).as(
      "finnSamhandler",
    );
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/bestillBrev/exstream", (request) => {
      expect(request.body).contains({
        brevkode: "PE_IY_05_027",
        idTSSEkstern: "80000781720",
        spraak: "NN",
        isSensitive: false,
        brevtittel: "",
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

    cy.getDataCy("toggle-samhandler-button").click();
    cy.contains("Samhandlertype").click().type("adv{enter}");
    cy.contains("Navn").click().type("advokat 1{enter}");
    cy.getDataCy("velg-samhandler").first().click();
    cy.getDataCy("bekreft-ny-mottaker").click();
    cy.getDataCy("change-to-user");

    cy.get("select[name=spraak]").select("Nynorsk");

    cy.getDataCy("is-sensitive").contains("Nei").click({ force: true });

    cy.getDataCy("order-letter").click();
    cy.get("@window-open").should(
      "have.been.calledOnceWithExactly",
      "mbdok://PE2@brevklient/dokument/453864183?token=1711014877285&server=https%3A%2F%2Fwasapp-q2.adeo.no%2Fbrevweb%2F",
    );
    cy.getDataCy("order-letter-success-message");
  });

  it("Bestill Docsys brev", () => {
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/bestillBrev/doksys", (request) => {
      expect(request.body).contains({ brevkode: "DOD_INFO_RETT_MAN", spraak: "NB" });
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

    cy.getDataCy("order-letter").click();
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

    cy.getDataCy("brev-title-textfield").click().type("GGMU");
    cy.getDataCy("order-letter").click();

    cy.getDataCy("is-sensitive").get(".navds-error-message");
    cy.getDataCy("is-sensitive").contains("Ja").click({ force: true });

    cy.getDataCy("order-letter").click();
    cy.get("@window-open").should(
      "have.been.calledOnceWithExactly",
      "mbdok://PE2@brevklient/dokument/453864212?token=1711023327721&server=https%3A%2F%2Fwasapp-q2.adeo.no%2Fbrevweb%2F",
    );
    cy.getDataCy("order-letter-success-message");
  });
});
