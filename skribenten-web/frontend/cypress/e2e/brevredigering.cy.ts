import { format, formatISO } from "date-fns";

import type { BrevResponse } from "~/types/brev";

describe("Brevredigering", () => {
  const hurtiglagreTidspunkt = formatISO(new Date());
  beforeEach(() => {
    cy.setupSakStubs();
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1?reserver=true", { fixture: "brevResponse.json" }).as(
      "brev",
    );
    cy.fixture("brevResponse.json").then((brev: BrevResponse) => {
      brev.info.sistredigert = hurtiglagreTidspunkt;
      cy.intercept("put", "/bff/skribenten-backend/brev/1/redigertBrev", brev).as("hurtiglagreRedigertBrev");
    });
    cy.intercept("GET", "/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification", {
      fixture: "modelSpecification.json",
    }).as("modelSpecification");
    cy.intercept("GET", "/bff/skribenten-backend/brev/1/reservasjon", {
      fixture: "brevreservasjon.json",
    }).as("reservasjon");

    cy.viewport(1200, 1400);
  });

  it("Åpne brevredigering", () => {
    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Saksbehandlingstiden vår er vanligvis 10 uker.").should("exist");
  });

  it("Autolagrer etter redigering", () => {
    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Sist endret 26.07.2024 16:15").should("exist");
    cy.contains("Dersom vi trenger flere opplysninger").click();
    cy.focused().type(" hello!");
    cy.wait("@hurtiglagreRedigertBrev");
    cy.contains("Automatisk lagret kl " + format(hurtiglagreTidspunkt, "HH:mm")).should("exist");
  });

  it("Blokkerer redigering om brev er reservert av noen andre", () => {
    cy.visit("/saksnummer/123456/brev/1");
    cy.intercept("GET", "/bff/skribenten-backend/brev/1/reservasjon", { fixture: "brevreservasjon_opptatt.json" }).as(
      "brevreservasjon_opptatt",
    );
    cy.wait("@brevreservasjon_opptatt");
    cy.contains("Brevet er utilgjengelig for deg fordi Hugo Weaving har brevet åpent.").should("exist");
  });

  it("Gjenoppta redigering", () => {
    cy.visit("/saksnummer/123456/brev/1");
    cy.intercept("GET", "/bff/skribenten-backend/brev/1/reservasjon", { fixture: "brevreservasjon_opptatt.json" }).as(
      "brevreservasjon_opptatt",
    );
    cy.wait("@brevreservasjon_opptatt");
    cy.contains("Brevet er utilgjengelig for deg fordi Hugo Weaving har brevet åpent.").should("exist");

    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1?reserver=true", {
      fixture: "brevResponse_ny_hash.json",
    }).as("brev_ny_hash");
    cy.intercept("GET", "/bff/skribenten-backend/brev/1/reservasjon", { fixture: "brevreservasjon_ny_hash.json" }).as(
      "brevreservasjon_ny_hash",
    );
    cy.contains("Ja, åpne på nytt").click();
    cy.wait("@brev_ny_hash");

    cy.contains("Informasjon om saksbehandlingstiden vår med ny hash").should("exist");
  });

  it("Åpne brev som er reservert av noen andre", () => {
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1?reserver=true", {
      statusCode: 423,
      fixture: "brevreservasjon_opptatt.json",
    }).as("brevreservasjon_opptatt");
    cy.visit("/saksnummer/123456/brev/1");

    cy.contains("Saksbehandlingstiden vår er vanligvis 10 uker.").should("not.exist");
    cy.contains("Brevet redigeres av noen andre").should("exist");
  });
});
