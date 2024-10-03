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
    cy.getDataCy("brevmal-button").should("have.length", 22);

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

    cy.get("select[name=enhetsId]").select("NAV Arbeid og ytelser Innlandet");
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
    cy.get("select[name=enhetsId]").select("NAV Arbeid og ytelser Innlandet");
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

    //tanstack knappen hovrer over ferdigstill knappen - vå i klikker på vestre side av knappen som er synlig. Se om vi kan fikse dette
    cy.getDataCy("order-letter").click("left");
    cy.get("@window-open").should(
      "have.been.calledOnceWithExactly",
      "mbdok://PE2@brevklient/dokument/453864284?token=1711101230605&server=https%3A%2F%2Fwasapp-q2.adeo.no%2Fbrevweb%2F",
    );
    cy.getDataCy("order-letter-success-message");
  });

  describe("Endrer på mottaker", () => {
    beforeEach(() => {
      cy.intercept("POST", "/bff/skribenten-backend/hentSamhandlerAdresse", (request) => {
        expect(request.body).to.deep.equal({ idTSSEkstern: "80000781720" });
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
    });

    it("søk med direkte oppslag", () => {
      cy.intercept("POST", "/bff/skribenten-backend/finnSamhandler", (request) => {
        expect(request.body).to.deep.equal({
          samhandlerType: "ADVO",
          identtype: "ORG",
          id: "direkte-oppslag-id",
          type: "DIREKTE_OPPSLAG",
        });
        request.reply({ fixture: "finnSamhandler.json" });
      }).as("finnSamhandler");

      cy.intercept("POST", "/bff/skribenten-backend/hentSamhandlerAdresse", (request) => {
        expect(request.body).to.deep.equal({ idTSSEkstern: "80000781720" });
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

      //søker opp og velger brevet vi vil ha
      cy.getDataCy("brevmal-search").click().type("brev fra nav");
      cy.getDataCy("brevmal-button").click();

      //åpner endre mottaker modal, og verifiserer at søk button ikke vises
      cy.getDataCy("toggle-endre-mottaker-modal").click();
      cy.get("[data-cy=endre-mottaker-søk-button]").should("not.exist");

      //velger direkte oppslag, og fyller ut resten av form
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
        "http://localhost:5173/saksnummer/123456/brevvelger?templateId=PE_IY_05_300&idTSSEkstern=%2280000781720%22",
      );

      cy.getDataCy("avsenderenhet-select").select("NAV Arbeid og ytelser Innlandet");
      cy.getDataCy("brev-title-textfield").click().type("Vedtak om bla bla");
      cy.getDataCy("språk-velger-select").should("have.value", "NB");
      cy.getDataCy("is-sensitive").contains("Nei").click({ force: true });

      //bestiller brev
      cy.getDataCy("order-letter").click("left");
      cy.get("@window-open").should(
        "have.been.calledOnceWithExactly",
        "mbdok://PE2@brevklient/dokument/453864183?token=1711014877285&server=https%3A%2F%2Fwasapp-q2.adeo.no%2Fbrevweb%2F",
      );
      cy.getDataCy("order-letter-success-message");
    });

    it("søk med organisasjonsnavn", () => {
      cy.intercept("POST", "/bff/skribenten-backend/finnSamhandler", (request) => {
        expect(request.body).to.deep.equal({
          samhandlerType: "ADVO",
          innlandUtland: "ALLE",
          navn: "navnet på samhandler",
          type: "ORGANISASJONSNAVN",
        });
        request.reply({ fixture: "finnSamhandler.json" });
      }).as("finnSamhandler");

      //søker opp og velger brevet vi vil ha
      cy.getDataCy("brevmal-search").click().type("brev fra nav");
      cy.getDataCy("brevmal-button").click();

      //åpner endre mottaker modal, og verifiserer at søk button ikke vises
      cy.getDataCy("toggle-endre-mottaker-modal").click();
      cy.get("[data-cy=endre-mottaker-søk-button]").should("not.exist");

      //velger organisasjonsnavn, og fyller ut resten av form
      cy.getDataCy("endre-mottaker-søketype-select").select("Organisasjonsnavn");
      cy.getDataCy("endre-mottaker-søk-button").should("be.visible");
      cy.getDataCy("endre-mottaker-organisasjonsnavn-innOgUtland").should("have.value", "ALLE");
      //TODO - vil vi trigge et søk for å sjekke at validerings feil vises?
      cy.contains("Samhandlertype").click().type("adv{enter}");
      cy.contains("Navn").click().type("navnet på samhandler");

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
        "http://localhost:5173/saksnummer/123456/brevvelger?templateId=PE_IY_05_300&idTSSEkstern=%2280000781720%22",
      );

      cy.getDataCy("avsenderenhet-select").select("NAV Arbeid og ytelser Innlandet");
      cy.getDataCy("brev-title-textfield").click().type("Vedtak om bla bla");
      cy.getDataCy("språk-velger-select").should("have.value", "NB");
      cy.getDataCy("is-sensitive").contains("Nei").click({ force: true });

      //bestiller brev
      cy.getDataCy("order-letter").click("left");
      cy.get("@window-open").should(
        "have.been.calledOnceWithExactly",
        "mbdok://PE2@brevklient/dokument/453864183?token=1711014877285&server=https%3A%2F%2Fwasapp-q2.adeo.no%2Fbrevweb%2F",
      );
      cy.getDataCy("order-letter-success-message");
    });

    it("søk med personnavn", () => {
      cy.intercept("POST", "/bff/skribenten-backend/finnSamhandler", (request) => {
        expect(request.body).to.deep.equal({
          samhandlerType: "ADVO",
          fornavn: "Fornavnet",
          etternavn: "Etternavnet",
          type: "PERSONNAVN",
        });
        request.reply({ fixture: "finnSamhandler.json" });
      }).as("finnSamhandler");

      //søker opp og velger brevet vi vil ha
      cy.getDataCy("brevmal-search").click().type("brev fra nav");
      cy.getDataCy("brevmal-button").click();

      //åpner endre mottaker modal, og verifiserer at søk button ikke vises
      cy.getDataCy("toggle-endre-mottaker-modal").click();
      cy.get("[data-cy=endre-mottaker-søk-button]").should("not.exist");

      //velger personnavn, og fyller ut resten av form
      cy.getDataCy("endre-mottaker-søketype-select").select("Personnavn");
      cy.getDataCy("endre-mottaker-søk-button").should("be.visible");
      //TODO - vil vi trigge et søk for å sjekke at validerings feil vises?
      cy.contains("Samhandlertype").click().type("adv{enter}");
      cy.contains("Fornavn").click().type("Fornavnet");
      cy.contains("Etternavn").click().type("Etternavnet");

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
        "http://localhost:5173/saksnummer/123456/brevvelger?templateId=PE_IY_05_300&idTSSEkstern=%2280000781720%22",
      );

      cy.getDataCy("avsenderenhet-select").select("NAV Arbeid og ytelser Innlandet");
      cy.getDataCy("brev-title-textfield").click().type("Vedtak om bla bla");
      cy.getDataCy("språk-velger-select").should("have.value", "NB");
      cy.getDataCy("is-sensitive").contains("Nei").click({ force: true });

      //bestiller brev
      cy.getDataCy("order-letter").click("left");
      cy.get("@window-open").should(
        "have.been.calledOnceWithExactly",
        "mbdok://PE2@brevklient/dokument/453864183?token=1711014877285&server=https%3A%2F%2Fwasapp-q2.adeo.no%2Fbrevweb%2F",
      );
      cy.getDataCy("order-letter-success-message");
    });

    it("kan legge inn manuell adresse for brevbaker brev", () => {
      cy.intercept("GET", "/bff/skribenten-backend/land", (request) => {
        request.reply({ fixture: "land.json" });
      }).as("Land fra backend");

      cy.visit("/saksnummer/123456/brevvelger", {
        onBeforeLoad(window) {
          cy.stub(window, "open").as("window-open");
        },
      });

      //søker opp og velger brevet vi vil ha
      cy.getDataCy("brevmal-search").click().type("Informasjon om saksbehandlingstid");
      cy.get('p:contains("Informasjon om saksbehandlingstid")').eq(1).click();
      cy.contains("Endre mottaker").click();
      cy.contains("Legg til manuelt").click();
      cy.contains("Navn").click().type("Fornavn Etternavnsen");
      cy.contains("Adresselinje 1").click().type("Adresselinjen 1");
      cy.contains("Postnummer").click().type("0000");
      cy.contains("Poststed").click().type("Poststedet");
      cy.contains("Land *").click().type("Sver{enter}");
      cy.contains("Gå videre").click();

      cy.contains("Fornavn Etternavnsen").should("be.visible");
      cy.contains("Adresselinjen").should("be.visible");
      cy.contains("0000").should("be.visible");
      cy.contains("Poststedet").should("be.visible");
      cy.get("td:contains('Se')").should("be.visible");
      cy.getDataCy("bekreft-ny-mottaker").click();

      cy.contains("Fornavn Etternavnsen").should("be.visible");
      cy.contains("Adresselinjen").should("be.visible");
      cy.contains("0000").should("be.visible");
      cy.contains("Poststedet").should("be.visible");
      cy.contains("SE").should("be.visible");
      cy.contains("Tilbakestill mottaker").should("be.visible");
    });

    it("kan ikke legge inn manuell adresse for exstream brev", () => {
      cy.getDataCy("brevmal-search").click().type("brev fra nav");
      cy.getDataCy("brevmal-button").click();

      cy.getDataCy("toggle-endre-mottaker-modal").click();
      cy.contains("Legg til manuelt").should("not.exist");
    });

    it("kan avbryte uten bekreftelse dersom det ikke finnes endringer", () => {
      cy.getDataCy("brevmal-search").click().type("brev fra nav");
      cy.getDataCy("brevmal-button").click();
      cy.getDataCy("toggle-endre-mottaker-modal").click();
      cy.get("[data-cy=endre-mottaker-søk-button]").should("not.exist");
      cy.contains("Avbryt").click();
      cy.getDataCy("endre-mottaker-modal").should("not.exist");
    });
    it("må bekrefte avbrytelse dersom det finnes endringer", () => {
      cy.getDataCy("brevmal-search").click().type("brev fra nav");
      cy.getDataCy("brevmal-button").click();
      cy.getDataCy("toggle-endre-mottaker-modal").click();
      cy.get("[data-cy=endre-mottaker-søk-button]").should("not.exist");
      cy.getDataCy("endre-mottaker-søketype-select").select("Direkte oppslag");
      cy.contains("Avbryt").click();

      //får opp bekreftelse
      cy.contains("Vil du avbryte endring av mottaker?").should("be.visible");
      cy.contains("Infoen du har skrevet inn blir ikke lagret. Du kan ikke angre denne handlingen.").should(
        "be.visible",
      );
      //kan navigere seg tilbake til form
      cy.contains("Nei, ikke avbryt").click();
      //velger å bekrefte avbrytelse
      cy.contains("Avbryt").click();
      cy.contains("Ja, avbryt").click();
      cy.getDataCy("endre-mottaker-modal").should("not.exist");
    });
  });

  describe("Kladd", () => {
    beforeEach(() => {
      cy.visit("/saksnummer/123456/brevvelger");
    });

    it("Viser kladder i brevvelgeren", () => {
      cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (req) => {
        req.reply([
          {
            id: 1,
            opprettetAv: { id: "Z990297", navn: "F_Z990297 E_Z990297" },
            opprettet: "2024-09-17T08:36:09.785Z",
            sistredigertAv: { id: "Z990297", navn: "F_Z990297 E_Z990297" },
            sistredigert: "2024-09-17T08:38:48.503Z",
            brevkode: "INFORMASJON_OM_SAKSBEHANDLINGSTID",
            brevtittel: "Informasjon om saksbehandlingstid",
            status: { type: "Kladd" },
            distribusjonstype: "SENTRALPRINT",
            mottaker: null,
            avsenderEnhet: null,
            spraak: "EN",
          },
        ]);
      }).as("getAlleBrevForSak");

      cy.wait("@getAlleBrevForSak");
      cy.contains("Informasjon om saksbehandlingstid").click();
      cy.contains("Mottaker").should("be.visible");
      cy.contains("Avsenderenhet").should("be.visible");
      cy.contains("Språk").should("be.visible");
      cy.contains("Gå til brevbehandler").should("be.visible");
      cy.contains("Åpne brev").should("be.visible");
    });

    it("Kan slette kladd", () => {
      let requestnr = 0;
      cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (req) => {
        if (requestnr !== 2) {
          requestnr++;

          req.reply([
            {
              id: 1,
              opprettetAv: { id: "Z990297", navn: "F_Z990297 E_Z990297" },
              opprettet: "2024-09-17T08:36:09.785Z",
              sistredigertAv: { id: "Z990297", navn: "F_Z990297 E_Z990297" },
              sistredigert: "2024-09-17T08:38:48.503Z",
              brevkode: "INFORMASJON_OM_SAKSBEHANDLINGSTID",
              brevtittel: "Informasjon om saksbehandlingstid",
              status: { type: "Kladd" },
              distribusjonstype: "SENTRALPRINT",
              mottaker: null,
              avsenderEnhet: null,
              spraak: "EN",
            },
          ]);
          //Det skjer visst en ekstra rerendering slik at det er det tredje kallet til endepunktet som er det riktige
        } else if (requestnr === 2) {
          req.reply([]);
        }
      }).as("getAlleBrevForSak");

      cy.intercept("DELETE", "/bff/skribenten-backend/sak/123456/brev/1", {
        statusCode: 204,
        body: null,
      }).as("deleteKladd");

      cy.wait("@getAlleBrevForSak");

      cy.contains("Informasjon om saksbehandlingstid").click();
      cy.contains("Slett kladd").click();
      cy.contains("Vil du slette kladden?").should("be.visible");
      cy.contains("Kladden vil bli slettet, og du kan ikke angre denne handlingen.").should("be.visible");
      cy.contains("Nei, behold kladden").should("be.visible");
      cy.contains("Ja, slett kladden").should("be.visible");
      cy.contains("Ja, slett kladden").click();
      cy.wait("@deleteKladd");
      cy.wait("@getAlleBrevForSak");
      cy.contains("Informasjon om saksbehandlingstid").should("not.be.visible");
    });
  });
});
