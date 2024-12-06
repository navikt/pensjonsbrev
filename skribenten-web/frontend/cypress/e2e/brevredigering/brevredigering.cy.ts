import { format, formatISO } from "date-fns";

import type { BrevResponse } from "~/types/brev";

import { nyBrevResponse } from "../../utils/brevredigeringTestUtils";

describe("Brevredigering", () => {
  const hurtiglagreTidspunkt = formatISO(new Date());
  beforeEach(() => {
    cy.setupSakStubs();
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1?reserver=true", { fixture: "brevResponse.json" }).as(
      "brev",
    );
    cy.fixture("brevResponseEtterLagring.json").then((brev: BrevResponse) => {
      brev.info.sistredigert = hurtiglagreTidspunkt;
      cy.intercept("put", "/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=*", brev).as(
        "hurtiglagreRedigertBrev",
      );
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

  it("lagrer signatur og saksbehandlerValg ved fortsett klikk", () => {
    const brev = nyBrevResponse({});

    cy.intercept("put", "/bff/skribenten-backend/brev/1/saksbehandlerValg", (req) => {
      expect(req.body).deep.equal({
        mottattSoeknad: "2024-07-24",
        ytelse: "alderspensjon",
        land: "Mars",
        svartidUker: "10",
      });
      req.reply(brev);
    }).as("lagreSaksbehandlerValg");

    cy.intercept("put", "/bff/skribenten-backend/brev/1/signatur", (req) => {
      expect(req.body).deep.equal("Det nye saksbehandlernavnet");
      req.reply(brev);
    }).as("lagreSignatur");

    cy.intercept("put", "/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=true", (req) => {
      req.reply(brev);
    }).as("lagreRedigertBrev");

    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Land").click().type("{selectall}{backspace}").type("Mars");
    cy.contains("Signatur").click().type("{selectall}{backspace}").type("Det nye saksbehandlernavnet");
    cy.contains("Fortsett").click();

    cy.wait("@lagreSaksbehandlerValg").should((req) => {
      expect(req.response?.statusCode).to.equal(200);
    });
    cy.wait("@lagreSignatur").should((req) => {
      expect(req.response?.statusCode).to.equal(200);
    });
    cy.wait("@lagreRedigertBrev").should((req) => {
      expect(req.response?.statusCode).to.equal(200);
    });
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/brevbehandler?brevId=1");

    //verifiserer at dem har bare blitt kalt 1 gang
    cy.get("@lagreSaksbehandlerValg.all").should("have.length", 1);
    cy.get("@lagreSignatur.all").should("have.length", 1);
    cy.get("@lagreRedigertBrev.all").should("have.length", 1);
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
    cy.intercept("POST", "/bff/skribenten-backend/brev/1/tilbakestill", {
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

  it("kan ikke redigere et arkivert brev", () => {
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1?reserver=true", {
      statusCode: 409,
    }).as("brev");
    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Brevet er arkivert, og kan derfor ikke redigeres.").should("exist");
    cy.contains("Gå til brevbehandler").click();
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/brevbehandler");
  });

  it("kan toggle punktliste", () => {
    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Du må melde").click();
    cy.getDataCy("editor-bullet-list").click();
    cy.get("ul li span:last").should("contain.text", "Du må melde");
    cy.getDataCy("editor-bullet-list").click();
    cy.get("ul li span:last").should("not.contain.text", "Du må melde");
  });
});
