describe("Kvittering", () => {
  beforeEach(() => {
    cy.setupSakStubs();
    cy.visit("/saksnummer/123456/kvittering");
  });

  it("åpner en annen sak", () => {
    cy.get("button").contains("Åpne annen sak").click();
    cy.location("pathname").should("eq", "/saksnummer").location("search").should("eq", "");
  });
  it("lager nytt brev for bruker", () => {
    cy.get("button").contains("Lage nytt brev på denne saken").click();
    cy.location("pathname").should("eq", "/saksnummer/123456/brevvelger").location("search").should("eq", "");
  });
});
