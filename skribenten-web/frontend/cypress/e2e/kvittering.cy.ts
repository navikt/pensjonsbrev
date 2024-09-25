describe("Kvittering", () => {
  beforeEach(() => {
    cy.setupSakStubs();
    cy.viewport(1200, 1400);
    cy.visit("/saksnummer/123456/kvittering");
  });

  it("åpner en annen sak", () => {
    cy.get("button").contains("Åpne annen sak").click();
    cy.url().should("eq", "http://localhost:5173/saksnummer");
  });
  it("lager nytt brev for bruker", () => {
    cy.get("button").contains("Lage nytt brev på denne saken").click();
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/brevvelger");
  });
});
