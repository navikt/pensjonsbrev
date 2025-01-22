import { format, formatISO } from "date-fns";

import type { BrevResponse } from "~/types/brev";

import { nyBrevResponse } from "../utils/brevredigeringTestUtils";

const defaultBrev = nyBrevResponse({});

describe("attestering", () => {
  beforeEach(() => {
    cy.setupSakStubs();

    cy.intercept("GET", "/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification", {
      fixture: "modelSpecification.json",
    }).as("modelSpecification");

    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (req) => req.reply(defaultBrev)).as(
      "brev",
    );
    cy.intercept("put", "/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=*", (req) => {
      req.reply(defaultBrev);
    }).as("hurtiglagreRedigertBrev");

    cy.intercept("GET", "/bff/skribenten-backend/brev/1/reservasjon", {
      fixture: "brevreservasjon.json",
    }).as("reservasjon");

    cy.viewport(1200, 1400);
  });

  it("Autolagrer brev etter redigering", () => {
    const hurtiglagreTidspunkt = formatISO(new Date());
    cy.fixture("brevResponseEtterLagring.json").then((brev: BrevResponse) => {
      brev.info.sistredigert = hurtiglagreTidspunkt;
      cy.intercept("put", "/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=*", brev).as(
        "hurtiglagreRedigertBrev",
      );
    });

    cy.visit("/saksnummer/123456/vedtak/1/redigering");
    cy.contains("Lagret 25.09.2024 ").should("exist");
    cy.contains("weeks.").click();
    cy.focused().type(" hello!");

    cy.wait("@hurtiglagreRedigertBrev");
    cy.contains("Lagret kl " + format(hurtiglagreTidspunkt, "HH:mm")).should("exist");
    cy.contains("hello!").should("exist");
  });

  it("lagrer underskrift", () => {
    cy.intercept("put", "/bff/skribenten-backend/brev/1/attestant", (req) => {
      const newBrev = nyBrevResponse({
        ...defaultBrev,
        redigertBrev: {
          ...defaultBrev.redigertBrev,
          signatur: {
            ...defaultBrev.redigertBrev.signatur,
            attesterendeSaksbehandlerNavn: "Den nye attestanten",
          },
        },
      });
      req.reply(newBrev);
    }).as("lagreUnderskrift");

    cy.visit("/saksnummer/123456/vedtak/1/redigering");
    cy.contains("Underskrift").click().type("{selectall}{backspace}").type("Den nye attestanten");
    cy.wait("@lagreUnderskrift");

    cy.contains("Den nye attestanten").should("exist");
  });

  it("Blokkerer redigering om brev er reservert av noen andre", () => {
    cy.visit("/saksnummer/123456/vedtak/1/redigering");
    cy.intercept("GET", "/bff/skribenten-backend/brev/1/reservasjon", { fixture: "brevreservasjon_opptatt.json" }).as(
      "brevreservasjon_opptatt",
    );
    cy.wait("@brevreservasjon_opptatt");
    cy.contains("Brevet er utilgjengelig for deg fordi Hugo Weaving har brevet åpent.").should("exist");
    cy.contains("Nei, gå til brevbehandler").should("exist");
    cy.contains("Nei, gå til brevbehandler").click();
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/brevbehandler");
  });

  it("Gjenoppta redigering", () => {
    cy.visit("/saksnummer/123456/vedtak/1/redigering");
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
});
