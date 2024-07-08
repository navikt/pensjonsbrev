describe("Brevvelger spec", () => {
  beforeEach(() => {
    cy.intercept("GET", "/bff/skribenten-backend/sak/**", { statusCode: 404 }).as("sakNotFound");
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456", { fixture: "sak.json" }).as("sak");
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/navn", { fixture: "navn.txt" }).as("navn");
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/adresse", { fixture: "adresse.json" }).as("adresse");
    cy.intercept("GET", "/bff/skribenten-backend/kodeverk/avtaleland", { fixture: "avtaleland.json" }).as("avtaleland");
    cy.intercept("GET", "/bff/skribenten-backend/me/enheter", { fixture: "enheter.json" }).as("enheter");
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/foretrukketSpraak", {
      fixture: "foretrukketSpraak.json",
    }).as("foretrukketSpraak");

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
    cy.getDataCy("brevmal-button").should("have.length", 27);

    cy.focused().type("r");
    cy.getDataCy("category-item").should("have.length", 2).and("have.class", "navds-accordion__item--open");
    cy.getDataCy("brevmal-button").should("have.length", 10);

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
    cy.getDataCy("brevmal-search").click().type("brev fra nav");
    cy.getDataCy("brevmal-button").click();

    cy.intercept("POST", "/bff/skribenten-backend/me/favourites", (request) => {
      expect(request.body).to.eq("PE_IY_05_300");
      request.reply({});
    }).as("Legg til favoritt");
    cy.intercept("GET", "/bff/skribenten-backend/me/favourites", ["PE_IY_05_300"]).as("1 favoritt");

    cy.getDataCy("add-favorite-button").click();
    cy.get(".navds-accordion__item").contains("Favoritter").should("exist").and("have.length", 1);

    cy.intercept("DELETE", "/bff/skribenten-backend/me/favourites", (request) => {
      expect(request.body).to.eq("PE_IY_05_300");
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
    cy.get("select[name=enhetsId]").select("NAV Arbeid og ytelser Innlandet");

    cy.getDataCy("is-sensitive").contains("Nei").click({ force: true });

    cy.getDataCy("order-letter").click();
    cy.get("@window-open").should(
      "have.been.calledOnceWithExactly",
      "mbdok://PE2@brevklient/dokument/453864183?token=1711014877285&server=https%3A%2F%2Fwasapp-q2.adeo.no%2Fbrevweb%2F",
    );
    cy.getDataCy("order-letter-success-message");
  });

  it("Bestill Doksys brev", () => {
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/bestillBrev/doksys", (request) => {
      expect(request.body).contains({ brevkode: "DOD_INFO_RETT_MAN", spraak: "EN", enhetsId: "4405" });
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

    cy.get("select[name=enhetsId]").select("NAV Arbeid og ytelser Innlandet");
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
    cy.get("select[name=enhetsId]").select("NAV Arbeid og ytelser Innlandet");
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

    cy.getDataCy("order-letter").click();
    cy.get("select[name=enhetsId]").select("NAV Arbeid og ytelser Innlandet");

    cy.getDataCy("is-sensitive").find(".navds-error-message");
    cy.getDataCy("is-sensitive").contains("Ja").click({ force: true });
    cy.getDataCy("is-sensitive").find(".navds-error-message").should("not.exist");

    cy.get("label").contains("Land").parent().find(".navds-error-message");
    cy.get("select[name=landkode]").select("Storbritannia");
    cy.get("label").contains("Land").parent().find(".navds-error-message").should("not.exist");

    cy.getDataCy("mottaker-text-textfield").parent().find(".navds-error-message");
    cy.getDataCy("mottaker-text-textfield").type("Haaland");
    cy.getDataCy("mottaker-text-textfield").parent().find(".navds-error-message").should("not.exist");

    cy.getDataCy("order-letter").click();
    cy.get("@window-open").should(
      "have.been.calledOnceWithExactly",
      "mbdok://PE2@brevklient/dokument/453864284?token=1711101230605&server=https%3A%2F%2Fwasapp-q2.adeo.no%2Fbrevweb%2F",
    );
    cy.getDataCy("order-letter-success-message");
  });
  describe("Endrer på mottaker", () => {
    it("direkte oppslag", () => {
      cy.intercept("POST", "/bff/skribenten-backend/finnSamhandler", (request) => {
        expect(request.body).to.deep.equal({
          samhandlerType: "ADVO",
          direkteOppslag: {
            identtype: "ORG",
            id: "direkte-oppslag-id",
          },
          organisasjonsnavn: null,
          personnavn: null,
        });
        request.reply({ fixture: "finnSamhandler.json" });
      }).as("finnSamhandler");

      cy.intercept("POST", "/bff/skribenten-backend/hentSamhandlerAdresse", (request) => {
        expect(request.body).to.deep.equal({
          idTSSEkstern: "80000781720",
        });
        request.reply({ fixture: "hentSamhandlerAdresse.json" });
      }).as("hentSamhandlerAdresse");

      cy.intercept("POST", "/bff/skribenten-backend/sak/123456/bestillBrev/exstream", (request) => {
        expect(request.body).to.deep.equal({
          brevkode: "PE_IY_05_300",
          idTSSEkstern: "80000781720",
          spraak: "NB",
          isSensitive: false,
          brevtittel: "Vedtak om bla bla",
          enhetsId: "4405",
          vedtaksId: null,
        });
        request.reply({ fixture: "bestillBrevExstream.json" });
      }).as("Brev fra nav - exstream");

      cy.visit("/saksnummer/123456/brevvelger", {
        onBeforeLoad(window) {
          cy.stub(window, "open").as("window-open");
        },
      });

      //søker opp og velger brevet vi vil ha
      cy.getDataCy("brevmal-search").click().type("brev fra nav");
      cy.getDataCy("brevmal-button").click();

      //åpner endre mottaker modal, søker og velger samhandler
      cy.getDataCy("toggle-endre-mottaker-modal").click();
      cy.get("[data-cy=endre-mottaker-søk-button]").should("not.exist");
      cy.getDataCy("endre-mottaker-søketype-select").select("Direkte oppslag");
      cy.getDataCy("endre-mottaker-søk-button").should("be.visible");
      //TODO - vil vi trigge et søk for å sjekke at validerings feil vises?
      cy.contains("Samhandlertype").click().type("adv{enter}");
      cy.getDataCy("endre-mottaker-identtype-select").select("Norsk orgnr");
      cy.contains("ID").click().type("direkte-oppslag-id");
      cy.getDataCy("endre-mottaker-søk-button").click();
      cy.getDataCy("velg-samhandler").first().click();

      //asserter oppsummering viser riktig info
      cy.contains("Advokat 1 As").should("be.visible");
      cy.contains("Postboks 603 Sentrum").should("be.visible");
      cy.contains("4003").should("be.visible");
      cy.contains("Stavanger").should("be.visible");
      cy.contains("Nor").should("be.visible");
      cy.getDataCy("bekreft-ny-mottaker").click();

      //asserter at vi har byttet til samhandler
      cy.url().should(
        "eq",
        "http://localhost:5173/saksnummer/123456/brevvelger/PE_IY_05_300?idTSSEkstern=%2280000781720%22",
      );

      //fyller ut resten av inputtene
      cy.getDataCy("avsenderenhet-select").select("NAV Arbeid og ytelser Innlandet");
      cy.getDataCy("brev-title-textfield").click().type("Vedtak om bla bla");
      cy.getDataCy("språk-velger-select").should("have.value", "NB");
      cy.getDataCy("is-sensitive").contains("Nei").click({ force: true });

      //bestiller brev
      cy.getDataCy("order-letter").click();
      cy.get("@window-open").should(
        "have.been.calledOnceWithExactly",
        "mbdok://PE2@brevklient/dokument/453864183?token=1711014877285&server=https%3A%2F%2Fwasapp-q2.adeo.no%2Fbrevweb%2F",
      );
      cy.getDataCy("order-letter-success-message");
    });

    it("manuell adresse", () => {
      /*
        backend har enda ikke støtte for at vi kan sende inn en manuell adresse. Midlertidig kommentert ut 
        den faktiske testen, og erstattet med en liten verifisering på at dem ikke kan gå inn i manuell adresse
      */

      cy.visit("/saksnummer/123456/brevvelger", {
        onBeforeLoad(window) {
          cy.stub(window, "open").as("window-open");
        },
      });

      //søker opp og velger brevet vi vil ha
      cy.getDataCy("brevmal-search").click().type("brev fra nav");
      cy.getDataCy("brevmal-button").click();

      //åpner endre mottaker modal, søker og velger samhandler
      cy.getDataCy("toggle-endre-mottaker-modal").click();
      cy.contains("Legg til manuelt").should("not.exist");

      /*
      cy.intercept("POST", "/bff/skribenten-backend/sak/123456/bestillBrev/exstream", (request) => {
        expect(request.body).to.deep.equal({
          brevkode: "PE_IY_05_300",
          idTSSEkstern: null,
          spraak: "NB",
          isSensitive: false,
          brevtittel: "Du får innvilget stønad av noe",
          enhetsId: "4405",
          vedtaksId: null,
        });
        request.reply({ fixture: "bestillBrevExstream.json" });
      }).as("Brev fra nav - exstream");

      cy.intercept("GET", "/bff/skribenten-backend/land", (request) => {
        request.reply({ fixture: "land.json" });
      }).as("Land fra backend");

      cy.visit("/saksnummer/123456/brevvelger", {
        onBeforeLoad(window) {
          cy.stub(window, "open").as("window-open");
        },
      });

      //søker opp og velger brevet vi vil ha
      cy.getDataCy("brevmal-search").click().type("brev fra nav");
      cy.getDataCy("brevmal-button").click();

      //åpner endre mottaker modal, søker og velger samhandler
      cy.getDataCy("toggle-endre-mottaker-modal").click();

      //går inn i manuell adresse utfyllingsformen
      cy.contains("Legg til manuelt").click();

      //Fyller ut formen
      cy.getDataCy("endre-mottaker-mottaker-type").select("Privatperson");
      cy.contains("Navn").click().type("Fornavn Etternavnsen");
      cy.contains("Adresselinje 1").click().type("Adresselinjen 1");
      cy.contains("Postnummer").click().type("0000");
      cy.contains("Poststed").click().type("Poststedet");
      cy.contains("Land *").click().type("Sver{enter}");
      cy.contains("Gå videre").click();

      //asserter oppsummering viser riktig info
      cy.contains("Privatperson").should("be.visible");
      cy.contains("Fornavn Etternavnsen").should("be.visible");
      cy.contains("Adresselinjen").should("be.visible");
      cy.contains("0000").should("be.visible");
      cy.contains("Poststedet").should("be.visible");
      cy.contains("Se").should("be.visible");
      cy.getDataCy("bekreft-ny-mottaker").click();

      //fyller ut resten av inputtene
      cy.getDataCy("avsenderenhet-select").select("NAV Arbeid og ytelser Innlandet");
      cy.getDataCy("brev-title-textfield").click().type("Du får innvilget stønad av noe");
      cy.getDataCy("språk-velger-select").should("have.value", "NB");
      cy.getDataCy("is-sensitive").contains("Nei").click({ force: true });

      //bestiller brev
      cy.getDataCy("order-letter").click();
      cy.get("@window-open").should(
        "have.been.calledOnceWithExactly",
        "mbdok://PE2@brevklient/dokument/453864183?token=1711014877285&server=https%3A%2F%2Fwasapp-q2.adeo.no%2Fbrevweb%2F",
      );
      cy.getDataCy("order-letter-success-message");
      */
    });
  });
});
