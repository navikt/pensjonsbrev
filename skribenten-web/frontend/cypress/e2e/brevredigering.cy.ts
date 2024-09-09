import { format, formatISO } from "date-fns";

import type { BrevResponse } from "~/types/brev";

describe("Brevredigering", () => {
  const hurtiglagreTidspunkt = formatISO(new Date());
  beforeEach(() => {
    cy.setupSakStubs();
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1?reserver=true", { fixture: "brevResponse.json" }).as(
      "brev",
    );
    cy.fixture("brevResponseEtterLagring.json").then((brev: BrevResponse) => {
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
    cy.contains("Lagret 26.07.2024 ").should("exist");
    cy.contains("Dersom vi trenger flere opplysninger").click();
    cy.focused().type(" hello!");
    cy.wait("@hurtiglagreRedigertBrev");
    cy.contains("Lagret kl " + format(hurtiglagreTidspunkt, "HH:mm")).should("exist");
    cy.contains("hello!").should("exist");
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

  it("kan tilbakestille malen", () => {
    cy.intercept("PUT", "/bff/skribenten-backend/brev/1/redigertBrev", {
      fixture: "brevResponse.json",
    });

    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Lagret 26.07.2024 ").should("exist");
    cy.contains("Dersom vi trenger flere opplysninger").click();
    cy.focused().type(" hello!");

    cy.contains("Tilbakestill malen").click();
    cy.contains("Vil du tilbakestille brevmalen?").should("exist");
    cy.contains("Innholdet du har endret eller lagt til i brevet vil bli slettet.").should("exist");
    cy.contains("Du kan ikke angre denne handlingen.").should("exist");
    cy.contains("Ja, tilbakestill malen").click();
    cy.contains(" hello!").should("not.exist");
  });

  it("beholder brevet etter å ville tilbakestille", () => {
    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Lagret 26.07.2024 ").should("exist");
    cy.contains("Dersom vi trenger flere opplysninger").click();
    cy.focused().type(" hello!");

    cy.contains("Tilbakestill malen").click();
    cy.contains("Nei, behold brevet").click();
    cy.contains(" hello!").should("exist");
  });
});
