//Hjelpere
const openInsertTableModal = () => {
  cy.get("[data-testid=toolbar-table-btn]").should("be.visible").click();
  return cy.get("[data-testid=insert-table-modal]", { timeout: 5000 }).should("be.visible");
};

const insertTable = (cols: number, rows: number) => {
  openInsertTableModal().within(() => {
    cy.get("[data-testid=input-cols]").clear().type(String(cols));
    cy.get("[data-testid=input-rows]").clear().type(String(rows));
    cy.contains("button", "Sett inn tabell").click();
  });
  cy.get("[data-testid=insert-table-modal]").should("not.be.visible");
};

const rightClickCell = (r: number, c: number) => cy.get(`[data-testid=table-cell-${r}-${c}]`).rightclick();

const waitAfterAutosave = () => cy.wait("@autosave").then(() => cy.wait(5000));

//Tester
describe("Tabell innsetting og redigering via kontekstmeny", () => {
  beforeEach(() => {
    cy.setupSakStubs();
    cy.viewport(1200, 900);

    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1?reserver=true", { fixture: "brevResponse.json" }).as(
      "brev",
    );

    cy.intercept("GET", "/bff/skribenten-backend/brev/1/reservasjon", { fixture: "brevreservasjon.json" }).as(
      "reservasjon",
    );

    cy.intercept("GET", "/bff/skribenten-backend/brevmal/**/modelSpecification", {
      fixture: "modelSpecification.json",
    }).as("modelSpec");

    cy.intercept("PUT", "/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=*", {
      statusCode: 200,
      body: { ok: true },
    }).as("autosave");

    cy.visit("/saksnummer/123456/brev/1");
    cy.wait(["@brev", "@reservasjon", "@modelSpec"]);
  });

  it("oppretter en 3x2-tabell", () => {
    insertTable(3, 2);
    waitAfterAutosave();

    cy.get("[data-testid=letter-table]")
      .should("have.length", 1)
      .within(() => {
        cy.get("tbody tr").should("have.length", 2);
        cy.get("tbody tr:first td").should("have.length", 3);
      });
  });

  context("med en eksisterende 3x2-tabell", () => {
    beforeEach(() => {
      insertTable(3, 2);
      waitAfterAutosave();
    });

    it("legger til kolonne til høyre via kontekstmenyen", () => {
      rightClickCell(0, 1);
      cy.contains("[role=menuitem]", "Sett inn kolonne til høyre").click();
      waitAfterAutosave();

      cy.get("[data-testid=table-cell-0-2]").should("exist");
      cy.get("[data-testid=letter-table] tbody tr:first td").should("have.length", 4);
    });

    it("legger til rad under via kontekstmenyen", () => {
      rightClickCell(0, 0);
      cy.contains("[role=menuitem]", "Sett inn rad under").click();
      waitAfterAutosave();

      cy.get("[data-testid=table-cell-1-0]").should("exist");
      cy.get("[data-testid=letter-table] tbody tr").should("have.length", 3);
    });

    it("sletter en rad via kontekstmenyen", () => {
      rightClickCell(0, 0);
      cy.contains("[role=menuitem]", "Sett inn rad under").click();
      waitAfterAutosave();

      rightClickCell(1, 0);
      cy.contains("[role=menuitem]", "Slett rad").click();
      waitAfterAutosave();

      cy.get("[data-testid=letter-table] tbody tr").should("have.length", 2);
    });

    it("sletter en kolonne via kontekstmenyen", () => {
      rightClickCell(0, 1);
      cy.contains("[role=menuitem]", "Sett inn kolonne til høyre").click();
      waitAfterAutosave();

      rightClickCell(0, 2);
      cy.contains("[role=menuitem]", "Slett kolonne").click();
      waitAfterAutosave();

      cy.get("[data-testid=letter-table] tbody tr:first td").should("have.length", 3);
    });

    it("sletter hele tabellen via kontekstmenyen", () => {
      rightClickCell(0, 0);
      cy.contains("[role=menuitem]", "Slett tabellen").click();
      waitAfterAutosave();

      cy.get("body").find("[data-testid=letter-table]").should("not.exist");
    });
  });
});
