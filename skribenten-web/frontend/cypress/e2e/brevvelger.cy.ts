describe("template spec", () => {
  before(() => {
    cy.intercept("GET", "/bff/skribenten-backend/sak/**", { statusCode: 404 }).as("sakNotFound");
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456", { fixture: "sak.json" }).as("sak");
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/navn", { fixture: "navn.txt" }).as("navn");
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/adresse", { fixture: "adresse.json" }).as("adresse");
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/foretrukketSpraak", {
      fixture: "foretrukketSpraak.json",
    }).as("foretrukketSpraak");

    cy.intercept("GET", "/bff/skribenten-backend/lettertemplates/**", { fixture: "letterTemplates.json" }).as(
      "templates",
    );

    cy.visit("/");
    cy.viewport(1200, 1400);
  });
  it("Verify saksnummer search", () => {
    cy.contains("Saksnummer").click();
    cy.focused().type("123{enter}");
    cy.contains("Finner ikke saksnummer").should("exist");
    cy.focused().type("456{enter}");

    cy.contains("Søk etter brevmal"); // TODO: assert something smarter?
  });
});
