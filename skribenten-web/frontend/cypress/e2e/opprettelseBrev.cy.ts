describe("Opprettelse av brev", () => {
  beforeEach(() => {
    cy.setupSakStubs();
    cy.viewport(1200, 1400);
  });
  it("kun obligatoriske felter vises ved opprettelse av brev", () => {
    cy.intercept("GET", "/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification", {
      fixture: "modelSpecification.json",
    });

    cy.visit("saksnummer/123456/brev?brevkode=INFORMASJON_OM_SAKSBEHANDLINGSTID&spraak=EN&enhetsId=null");
    cy.contains("Mottatt soeknad").should("exist");
    cy.contains("Ytelse").should("exist");
    cy.contains("Land").should("not.exist");
    cy.contains("inkluder venter svar afp").should("not.exist");
    cy.contains("Svartid uker").should("exist");
    cy.contains("Opprett brev").should("exist");
  });
  it("boolean felter vises ikke ved opprettelse av brev", () => {
    cy.intercept("GET", "/bff/skribenten-backend/brevmal/UT_ORIENTERING_OM_SAKSBEHANDLINGSTID/modelSpecification", {
      fixture: "modelSpecificationOrienteringOmSaksbehandlingstid.json",
    });
    cy.visit("/saksnummer/123456/brev?brevkode=UT_ORIENTERING_OM_SAKSBEHANDLINGSTID&spraak=NB&enhetsId=null");
    cy.contains("Mottatt soknad").should("exist");
    cy.contains("Soknad oversendes til utlandet").should("not.exist");
    cy.contains("Opprett brev").should("exist");
  });
});
