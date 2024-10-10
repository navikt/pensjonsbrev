describe("Endrer på mottaker", () => {
  beforeEach(() => {
    cy.setupSakStubs();
    cy.viewport(1200, 1400);

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
    cy.intercept("GET", "/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification", {
      fixture: "modelSpecification.json",
    });
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
    cy.getDataCy("land-combobox").click().type("Sver{enter}");
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
    cy.contains("Infoen du har skrevet inn blir ikke lagret. Du kan ikke angre denne handlingen.").should("be.visible");
    //kan navigere seg tilbake til form
    cy.contains("Nei, ikke avbryt").click();
    //velger å bekrefte avbrytelse
    cy.contains("Avbryt").click();
    cy.contains("Ja, avbryt").click();
    cy.getDataCy("endre-mottaker-modal").should("not.exist");
  });
});
