describe("Oppretter brevbakerbrev", () => {
  beforeEach(() => {
    cy.setupSakStubs();
    cy.viewport(1200, 1400);
  });

  it("kun obligatoriske felter vises ved opprettelse av brev", () => {
    cy.intercept("GET", "/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification", {
      fixture: "modelSpecification.json",
    });

    cy.visit("saksnummer/123456/brevvelger?templateId=INFORMASJON_OM_SAKSBEHANDLINGSTID");
    cy.contains("Mottatt soeknad").should("exist");
    cy.contains("Ytelse").should("exist");
    cy.contains("Land").should("not.exist");
    cy.contains("inkluder venter svar afp").should("not.exist");
    cy.contains("Svartid uker").should("exist");
  });

  it("boolean felter vises ikke ved opprettelse av brev", () => {
    cy.intercept("GET", "/bff/skribenten-backend/brevmal/UT_ORIENTERING_OM_SAKSBEHANDLINGSTID/modelSpecification", {
      fixture: "modelSpecificationOrienteringOmSaksbehandlingstid.json",
    });
    cy.visit("/saksnummer/123456/brevvelger?templateId=UT_ORIENTERING_OM_SAKSBEHANDLINGSTID&spraak=NB&enhetsId=null");
    cy.contains("Mottatt soknad").should("exist");
    cy.contains("Soknad oversendes til utlandet").should("not.exist");
  });

  it("validering trigges", () => {
    cy.intercept("GET", "/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification", {
      fixture: "modelSpecification.json",
    }).as("getBrevmal");
    cy.visit("/saksnummer/123456/brevvelger?templateId=INFORMASJON_OM_SAKSBEHANDLINGSTID");
    cy.wait("@getBrevmal");
    cy.contains("Åpne brev").click("left");
    cy.get(".navds-error-message").should("have.length", 3);
  });

  it("oppretter brev", () => {
    cy.intercept("GET", "/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification", {
      fixture: "modelSpecification.json",
    });

    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev", (req) => {
      expect(req.body).deep.equal({
        brevkode: "INFORMASJON_OM_SAKSBEHANDLINGSTID",
        spraak: "NB",
        avsenderEnhetsId: "4405",
        mottaker: null,
        saksbehandlerValg: {
          mottattSoeknad: "2024-10-09",
          ytelse: "Alderspensjon",
          svartidUker: "4",
        },
      });

      req.reply({ fixture: "brevResponse.json" });
    }).as("createBrev");

    cy.visit("/saksnummer/123456/brevvelger?templateId=INFORMASJON_OM_SAKSBEHANDLINGSTID");

    cy.get("select[name=enhetsId]").select("Nav Arbeid og ytelser Innlandet");
    cy.get("select[name=spraak]").should("have.value", "NB");

    cy.contains("Mottatt soeknad").click().type("09.10.2024");
    cy.contains("Ytelse").click().type("Alderspensjon");
    cy.contains("Svartid uker").click().type("4");
    cy.contains("Åpne brev").click("left");
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/brev/1");
  });

  it("oppretter brev som har non-nullable boolean felt", () => {
    cy.intercept("GET", "/bff/skribenten-backend/brevmal/UT_ORIENTERING_OM_SAKSBEHANDLINGSTID/modelSpecification", {
      fixture: "modelSpecificationOrienteringOmSaksbehandlingstid.json",
    });

    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev", (req) => {
      expect(req.body).deep.equal({
        brevkode: "UT_ORIENTERING_OM_SAKSBEHANDLINGSTID",
        spraak: "NB",
        avsenderEnhetsId: "4405",
        mottaker: null,
        saksbehandlerValg: {
          mottattSøknad: "2024-10-09",
          søknadOversendesTilUtlandet: false,
        },
      });
      req.reply({ fixture: "brevResponse.json" });
    }).as("createBrev");

    cy.visit("/saksnummer/123456/brevvelger?templateId=UT_ORIENTERING_OM_SAKSBEHANDLINGSTID");

    cy.get("select[name=enhetsId]").select("Nav Arbeid og ytelser Innlandet");
    cy.get("select[name=spraak]").should("have.value", "NB");

    cy.contains("Mottatt soknad").click().type("09.10.2024");
    cy.contains("Åpne brev").click("left");
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/brev/1");
  });
});
