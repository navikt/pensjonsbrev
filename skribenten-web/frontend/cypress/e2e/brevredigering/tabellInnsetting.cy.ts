//Hjelpere
const openInsertTableModal = () => {
  cy.get("[data-cy=toolbar-table-btn]").should("be.visible").click();
  return cy.get("[data-cy=insert-table-modal]", { timeout: 5000 }).should("be.visible");
};

const insertTable = (cols: number, rows: number) => {
  openInsertTableModal().within(() => {
    cy.get("[data-cy=input-cols]").clear().type(String(cols));
    cy.get("[data-cy=input-rows]").clear().type(String(rows));
    cy.contains("button", "Sett inn tabell").click();
  });
  cy.get("[data-cy=insert-table-modal]").should("not.be.visible");
};

const rightClickCell = (row: number, col: number) => cy.get(`[data-cy=table-cell-${row}-${col}]`).rightclick();

const waitAfterAutosave = () => {
  cy.tick(5000);
  cy.wait("@autosave");
};

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
    cy.clock();
  });

  it("oppretter en 3x2-tabell", () => {
    insertTable(3, 2);
    waitAfterAutosave();

    cy.get("[data-cy=letter-table]")
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

      cy.get("[data-cy=table-cell-0-2]").should("exist");
      cy.get("[data-cy=letter-table] tbody tr:first td").should("have.length", 4);
    });

    it("legger til rad under via kontekstmenyen", () => {
      rightClickCell(0, 0);
      cy.contains("[role=menuitem]", "Sett inn rad under").click();
      waitAfterAutosave();

      cy.get("[data-cy=table-cell-1-0]").should("exist");
      cy.get("[data-cy=letter-table] tbody tr").should("have.length", 3);
    });

    it("sletter en rad via kontekstmenyen", () => {
      rightClickCell(0, 0);
      cy.contains("[role=menuitem]", "Sett inn rad under").click();
      waitAfterAutosave();

      rightClickCell(1, 0);
      cy.contains("[role=menuitem]", "Slett rad").click();
      waitAfterAutosave();

      cy.get("[data-cy=letter-table] tbody tr").should("have.length", 2);
    });

    it("sletter en kolonne via kontekstmenyen", () => {
      rightClickCell(0, 1);
      cy.contains("[role=menuitem]", "Sett inn kolonne til høyre").click();
      waitAfterAutosave();

      rightClickCell(0, 2);
      cy.contains("[role=menuitem]", "Slett kolonne").click();
      waitAfterAutosave();

      cy.get("[data-cy=letter-table] tbody tr:first td").should("have.length", 3);
    });

    it("sletter hele tabellen via kontekstmenyen", () => {
      rightClickCell(0, 0);
      cy.contains("[role=menuitem]", "Slett tabellen").click();
      waitAfterAutosave();

      cy.get("body").find("[data-cy=letter-table]").should("not.exist");
    });
  });
});
