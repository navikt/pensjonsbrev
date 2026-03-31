describe("Endrer på mottaker", () => {
  beforeEach(() => {
    cy.setupSakStubs();

    cy.intercept("POST", "/bff/skribenten-backend/hentSamhandlerAdresse", {
      fixture: "hentSamhandlerAdresse.json",
    }).as("hentSamhandlerAdresse");

    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/bestillBrev/exstream", (request) => {
      expect(request.body).to.deep.equal({
        brevkode: "PE_IY_05_300",
        idTSSEkstern: "80000781720",
        spraak: "NB",
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

    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/bestillBrev/exstream", (request) => {
      expect(request.body).to.deep.equal({
        brevkode: "PE_IY_05_300",
        idTSSEkstern: "80000781720",
        spraak: "NB",
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
    cy.contains("Samhandlertype").click().type("adv{enter}");
    cy.getDataCy("endre-mottaker-identtype-select").select("Norsk orgnr");
    cy.get('[name="finnSamhandler.direkteOppslag.id"]').type("direkte-oppslag-id");
    cy.getDataCy("endre-mottaker-søk-button").click();

    //velger første samhandler ved å klikke på navnet (selekterer og utvider raden)
    cy.getDataCy("endre-mottaker-modal").contains("Advokat 1 As").first().click();

    //asserter at samhandleradresse vises i utvidet rad
    cy.contains("Postboks 603 Sentrum").should("be.visible");
    cy.contains("4003").should("be.visible");
    cy.contains("Stavanger").should("be.visible");
    cy.contains("Nor").should("be.visible");
    cy.getDataCy("lagre-samhandler").click();

    //asserter at vi har byttet til samhandler
    cy.location("pathname")
      .should("eq", "/saksnummer/123456/brevvelger")
      .location("search")
      .should("eq", "?templateId=PE_IY_05_300&idTSSEkstern=%2280000781720%22");

    cy.getDataCy("avsenderenhet-select").select("Nav Arbeid og ytelser Innlandet");
    cy.getDataCy("brev-title-textfield").click().type("Vedtak om bla bla");
    cy.getDataCy("språk-velger-select").should("have.value", "NB");

    //bestiller brev
    cy.getDataCy("order-letter").click();
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
    cy.contains("Samhandlertype").click().type("adv{enter}");
    cy.get('[name="finnSamhandler.organisasjonsnavn.navn"]').type("navnet på samhandler");

    cy.getDataCy("endre-mottaker-søk-button").click();

    //velger første samhandler ved å klikke på navnet (selekterer og utvider raden)
    cy.getDataCy("endre-mottaker-modal").contains("Advokat 1 As").first().click();

    //asserter at samhandleradresse vises i utvidet rad
    cy.contains("Postboks 603 Sentrum").should("be.visible");
    cy.contains("4003").should("be.visible");
    cy.contains("Stavanger").should("be.visible");
    cy.contains("Nor").should("be.visible");
    cy.getDataCy("lagre-samhandler").click();

    //asserter at vi har byttet til samhandler
    cy.location("pathname")
      .should("eq", "/saksnummer/123456/brevvelger")
      .location("search")
      .should("eq", "?templateId=PE_IY_05_300&idTSSEkstern=%2280000781720%22");

    cy.getDataCy("avsenderenhet-select").select("Nav Arbeid og ytelser Innlandet");
    cy.getDataCy("brev-title-textfield").click().type("Vedtak om bla bla");
    cy.getDataCy("språk-velger-select").should("have.value", "NB");

    //bestiller brev
    cy.getDataCy("order-letter").click();
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
    cy.contains("Samhandlertype").click().type("adv{enter}");
    cy.get('[name="finnSamhandler.personnavn.fornavn"]').click().type("Fornavnet");
    cy.get('[name="finnSamhandler.personnavn.etternavn"]').type("Etternavnet");

    cy.getDataCy("endre-mottaker-søk-button").click();

    //velger første samhandler ved å klikke på navnet (selekterer og utvider raden)
    cy.getDataCy("endre-mottaker-modal").contains("Advokat 1 As").first().click();

    //asserter at samhandleradresse vises i utvidet rad
    cy.contains("Postboks 603 Sentrum").should("be.visible");
    cy.contains("4003").should("be.visible");
    cy.contains("Stavanger").should("be.visible");
    cy.contains("Nor").should("be.visible");
    cy.getDataCy("lagre-samhandler").click();

    //asserter at vi har byttet til samhandler
    cy.location("pathname")
      .should("eq", "/saksnummer/123456/brevvelger")
      .location("search")
      .should("eq", "?templateId=PE_IY_05_300&idTSSEkstern=%2280000781720%22");

    cy.getDataCy("avsenderenhet-select").select("Nav Arbeid og ytelser Innlandet");
    cy.getDataCy("brev-title-textfield").click().type("Vedtak om bla bla");
    cy.getDataCy("språk-velger-select").should("have.value", "NB");

    //bestiller brev
    cy.getDataCy("order-letter").click();
    cy.get("@window-open").should(
      "have.been.calledOnceWithExactly",
      "mbdok://PE2@brevklient/dokument/453864183?token=1711014877285&server=https%3A%2F%2Fwasapp-q2.adeo.no%2Fbrevweb%2F",
    );
    cy.getDataCy("order-letter-success-message");
  });

  it("viser valideringsfeil for direkte oppslag med tomme felter", () => {
    cy.getDataCy("brevmal-search").click().type("brev fra nav");
    cy.getDataCy("brevmal-button").click();
    cy.getDataCy("toggle-endre-mottaker-modal").click();

    cy.getDataCy("endre-mottaker-søketype-select").select("Direkte oppslag");
    cy.getDataCy("endre-mottaker-søk-button").click();

    //samhandlertype, identtype og id er påkrevd
    cy.getDataCy("endre-mottaker-modal").find(".aksel-error-message").should("have.length.at.least", 2);
    cy.getDataCy("endre-mottaker-modal").contains("Feltet må fylles ut").should("be.visible");
  });

  it("viser valideringsfeil for organisasjonsnavn med tomme felter", () => {
    cy.getDataCy("brevmal-search").click().type("brev fra nav");
    cy.getDataCy("brevmal-button").click();
    cy.getDataCy("toggle-endre-mottaker-modal").click();

    cy.getDataCy("endre-mottaker-søketype-select").select("Organisasjonsnavn");
    cy.getDataCy("endre-mottaker-søk-button").click();

    //samhandlertype og navn er påkrevd
    cy.getDataCy("endre-mottaker-modal").find(".aksel-error-message").should("have.length.at.least", 1);
    cy.getDataCy("endre-mottaker-modal").contains("Feltet må fylles ut").should("be.visible");
  });

  it("viser valideringsfeil for personnavn med tomme felter", () => {
    cy.getDataCy("brevmal-search").click().type("brev fra nav");
    cy.getDataCy("brevmal-button").click();
    cy.getDataCy("toggle-endre-mottaker-modal").click();

    cy.getDataCy("endre-mottaker-søketype-select").select("Personnavn");
    cy.getDataCy("endre-mottaker-søk-button").click();

    //samhandlertype, fornavn og etternavn er påkrevd
    cy.getDataCy("endre-mottaker-modal").find(".aksel-error-message").should("have.length.at.least", 2);
    cy.getDataCy("endre-mottaker-modal").contains("Feltet må fylles ut").should("be.visible");
  });

  it("viser valideringsfeil for manuell adresse med norsk adresse", () => {
    cy.intercept("GET", "/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification", {
      fixture: "modelSpecification.json",
    });
    cy.intercept("GET", "/bff/skribenten-backend/land", { fixture: "land.json" });

    cy.visit("/saksnummer/123456/brevvelger", {
      onBeforeLoad(window) {
        cy.stub(window, "open").as("window-open");
      },
    });

    cy.getDataCy("brevmal-search").click().type("Informasjon om saksbehandlingstid");
    cy.contains("Informasjon om saksbehandlingstid").click();
    cy.getDataCy("toggle-endre-mottaker-modal").click();
    cy.contains("Legg til manuelt").click();

    //submitter med tomme felter — navn er obligatorisk
    cy.getDataCy("endre-mottaker-modal").contains("Fortsett").click();
    cy.getDataCy("endre-mottaker-modal").contains("Obligatorisk").should("be.visible");

    //fyller ut navn, setter ugyldig postnummer
    cy.contains("Navn").click().type("Test Testesen");
    cy.contains("Postnummer").click().type("abc");
    cy.contains("Poststed").click().type("Stedet");
    cy.getDataCy("endre-mottaker-modal").contains("Fortsett").click();
    cy.getDataCy("endre-mottaker-modal").contains("Postnummer må være 4 siffer").should("be.visible");
  });

  it("viser valideringsfeil for manuell adresse med utenlandsk adresse", () => {
    cy.intercept("GET", "/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification", {
      fixture: "modelSpecification.json",
    });
    cy.intercept("GET", "/bff/skribenten-backend/land", { fixture: "land.json" });

    cy.visit("/saksnummer/123456/brevvelger", {
      onBeforeLoad(window) {
        cy.stub(window, "open").as("window-open");
      },
    });

    cy.getDataCy("brevmal-search").click().type("Informasjon om saksbehandlingstid");
    cy.contains("Informasjon om saksbehandlingstid").click();
    cy.getDataCy("toggle-endre-mottaker-modal").click();
    cy.contains("Legg til manuelt").click();

    //bytter til utenlandsk adresse — postnr/poststed skal skjules
    cy.getDataCy("land-combobox").click().type("Sver{enter}");
    cy.contains("Postnummer").should("not.exist");
    cy.contains("Poststed").should("not.exist");

    //fyller ut navn men lar adresselinje 1 stå tom
    cy.contains("Navn").click().type("Test Testesen");
    cy.getDataCy("endre-mottaker-modal").contains("Fortsett").click();
    cy.getDataCy("endre-mottaker-modal").contains("Adresselinje 1 må fylles ut").should("be.visible");
  });

  it("kan legge inn manuell adresse for brevbaker brev", () => {
    cy.intercept("GET", "/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification", {
      fixture: "modelSpecification.json",
    });
    cy.intercept("GET", "/bff/skribenten-backend/land", { fixture: "land.json" });

    cy.visit("/saksnummer/123456/brevvelger", {
      onBeforeLoad(window) {
        cy.stub(window, "open").as("window-open");
      },
    });

    //søker opp og velger brevet vi vil ha
    cy.getDataCy("brevmal-search").click().type("Informasjon om saksbehandlingstid");
    cy.contains("Informasjon om saksbehandlingstid").click();
    cy.getDataCy("toggle-endre-mottaker-modal").click();
    cy.contains("Legg til manuelt").click();
    cy.contains("Navn").click().type("Fornavn Etternavnsen");
    cy.contains("Adresselinje 1").click().type("Adresselinjen 1");
    cy.contains("Postnummer").click().type("0000");
    cy.contains("Poststed").click().type("Poststedet");
    cy.getDataCy("land-combobox").click().type("Sver{enter}");
    cy.getDataCy("endre-mottaker-modal").contains("Fortsett").click();
    cy.contains("Adresselinjen").should("be.visible");
    cy.contains("0000").should("not.exist");
    cy.contains("Poststedet").should("not.exist");
    cy.get("td:contains('Sverige')").should("be.visible");
    cy.getDataCy("bekreft-ny-mottaker").click();

    cy.contains("Fornavn Etternavnsen").should("be.visible");
    cy.contains("Adresselinjen").should("be.visible");
    cy.get("body").should("not.contain", "0000");
    cy.get("body").should("not.contain", "Poststedet");
    cy.contains("Sverige").should("be.visible");
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
  it("må bekrefte avbrytelse dersom det finnes endringer for manuellAdresse", () => {
    cy.intercept("GET", "/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification", {
      fixture: "modelSpecification.json",
    }).as("modelSpecification");

    cy.intercept("GET", "/bff/skribenten-backend/land", { fixture: "land.json" });

    cy.getDataCy("brevmal-search").click().type("Informasjon om saksbehandlingstid");
    cy.contains("Informasjon om saksbehandlingstid").click();
    cy.getDataCy("toggle-endre-mottaker-modal").click();
    cy.contains("Legg til manuelt").click();
    cy.contains("Navn").click().type("Fornavn Etternavnsen");
    cy.contains("Avbryt").click();

    //får opp bekreftelse
    cy.contains("Vil du avbryte endring av mottaker?").should("be.visible");
    cy.contains("Infoen du har skrevet inn blir ikke lagret. Du kan ikke angre denne handlingen.").should("be.visible");
    //kan navigere seg tilbake til form
    cy.contains("Nei, ikke avbryt").click();
    //velger å bekrefte avbrytelse
    cy.contains("Avbryt").click();
    cy.contains("Ja, avbryt").click();
    cy.getDataCy("endre-mottaker-modal").should("not.exist");
  });
});
