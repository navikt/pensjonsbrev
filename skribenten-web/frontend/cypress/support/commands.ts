// ***********************************************
// This example commands.ts shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************

export {};

declare global {
  // eslint-disable-next-line @typescript-eslint/no-namespace -- auto-added by Cypress, keep as is for now
  namespace Cypress {
    interface Chainable {
      /**
       * Custom command to select DOM element by data-cy attribute.
       * @example cy.dataCy('greeting')
       */
      getDataCy(value: string): Chainable<JQuery<HTMLElement>>;
      setupSakStubs(): Chainable<void>;
    }
  }
}

Cypress.Commands.add("getDataCy", (value: string) => {
  return cy.get(`[data-cy=${value}]`);
});

Cypress.Commands.add("setupSakStubs", () => {
  cy.intercept("GET", "/bff/skribenten-backend/sak/**", { statusCode: 404 }).as("sakNotFound");
  cy.intercept("GET", "/bff/skribenten-backend/sak/123456", { fixture: "sak.json" }).as("sak");
  cy.intercept("GET", "/bff/skribenten-backend/sak/123456/navn", { fixture: "navn.txt" }).as("navn");
  cy.intercept("GET", "/bff/skribenten-backend/sak/123456/adresse", { fixture: "adresse.json" }).as("adresse");
  cy.intercept("GET", "/bff/skribenten-backend/kodeverk/avtaleland", { fixture: "avtaleland.json" }).as("avtaleland");
  cy.intercept("GET", "/bff/skribenten-backend/me/enheter", { fixture: "enheter.json" }).as("enheter");
  cy.intercept("GET", "/bff/skribenten-backend/land", (request) => {
    request.reply([]);
  });
  cy.intercept("GET", "/bff/skribenten-backend/sak/123456/foretrukketSpraak", {
    fixture: "foretrukketSpraak.json",
  }).as("foretrukketSpraak");
});
