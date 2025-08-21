import { format, formatISO } from "date-fns";

import { nyBrevResponse } from "../../utils/brevredigeringTestUtils";

const defaultBrev = nyBrevResponse({});
describe("attestant redigering", () => {
  beforeEach(() => {
    cy.setupSakStubs();

    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1/attestering?reserver=true", (req) =>
      req.reply(defaultBrev),
    ).as("loadBrev");
    cy.intercept("put", "/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=*", (req) => {
      req.reply({ ...defaultBrev, redigertBrev: req.body });
    }).as("hurtiglagreRedigertBrev");

    cy.viewport(1200, 1400);
  });

  it("Autolagrer brev etter redigering", () => {
    const hurtiglagreTidspunkt = formatISO(new Date());

    cy.intercept("put", "/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=false", (req) =>
      req.reply({
        ...defaultBrev,
        info: { ...defaultBrev.info, sistredigert: hurtiglagreTidspunkt },
        redigertBrev: req.body,
      }),
    ).as("hurtiglagreRedigertBrev");

    cy.visit("/saksnummer/123456/attester/1/redigering");
    cy.contains("Lagret 25.09.2024 ").should("exist");
    cy.contains("weeks.").click();
    cy.focused().type(" hello!");

    cy.wait("@hurtiglagreRedigertBrev");
    cy.contains("Lagret kl " + format(hurtiglagreTidspunkt, "HH:mm")).should("exist");
    cy.contains("hello!").should("exist");
  });

  it("lagrer underskrift", () => {
    cy.intercept("put", "/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=false", (req) => {
      req.reply({
        ...defaultBrev,
        redigertBrev: req.body,
      });
    }).as("lagreUnderskrift");

    cy.visit("/saksnummer/123456/attester/1/redigering");
    cy.contains("Underskrift").click().type("{selectall}{backspace}").type("Den nye attestanten");
    cy.wait("@lagreUnderskrift");

    cy.contains("Den nye attestanten").should("exist");
  });

  it("Blokkerer redigering om brev er reservert av noen andre", () => {
    cy.visit("/saksnummer/123456/attester/1/redigering");
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
    cy.visit("/saksnummer/123456/attester/1/redigering");
    cy.intercept("GET", "/bff/skribenten-backend/brev/1/reservasjon", { fixture: "brevreservasjon_opptatt.json" }).as(
      "brevreservasjon_opptatt",
    );
    cy.wait("@brevreservasjon_opptatt");
    cy.contains("Brevet er utilgjengelig for deg fordi Hugo Weaving har brevet åpent.").should("exist");

    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1/attestering?reserver=true", {
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
