import type { BrevResponse } from "~/types/brev";

import brev from "../../fixtures/bekreftelsePåFlyktningstatus/brev.json";
import { nyBrevInfo, nyBrevResponse } from "../../utils/brevredigeringTestUtils";

const defaultBrev = nyBrevResponse({});
const bekreftelsePaaFlyktningstatusBrev = brev as unknown as BrevResponse;
const vedtaksBrev = nyBrevResponse({
  ...bekreftelsePaaFlyktningstatusBrev,
  info: { ...bekreftelsePaaFlyktningstatusBrev.info, brevtype: "VEDTAKSBREV" },
});

describe("attestering", () => {
  beforeEach(() => {
    cy.setupSakStubs();
    cy.viewport(1200, 1400);
  });

  it("sender et brev til attestering", () => {
    //brev redigering
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (req) => {
      req.reply(vedtaksBrev);
    }).as("hentBrev");

    cy.intercept("PUT", "/bff/skribenten-backend/brev/1/saksbehandlerValg", (req) => {
      expect(req.body).to.deep.equal({});
      req.reply(vedtaksBrev);
    }).as("saksbehandlerValg");

    cy.intercept("PUT", "/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=false", (req) => {
      req.reply({ ...vedtaksBrev, redigertBrev: req.body });
    }).as("oppdaterBrevtekst");

    cy.intercept("PUT", "/bff/skribenten-backend/sak/123456/brev/1?frigiReservasjon=true", (req) =>
      req.reply({
        ...vedtaksBrev,
        saksbehandlerValg: req.body.saksbehandlerValg,
        redigertBrev: req.body.redigertBrev,
        info: nyBrevInfo({ ...req.body.info, status: { type: "Kladd" } }),
      }),
    ).as("lagreBrev");

    cy.visit("/saksnummer/123456/brev/1");
    cy.wait("@hentBrev", { timeout: 10000 });

    cy.contains("Underskrift").click().type("{selectall}{backspace}Dette er en signatur");

    cy.clock();
    cy.contains("ankomst til Norge.").focus().type("{end}{enter}Dette er en ny tekstblokk");
    cy.tick(5000);
    cy.clock().invoke("restore");
    cy.wait("@oppdaterBrevtekst", { timeout: 10000 });

    cy.get("p:contains('Dette er en signatur')").should("exist");
    cy.contains("Dette er en ny tekstblokk").should("exist");
    cy.get("@lagreBrev.all").should("have.length", 0);

    //brevbehandler

    const brevEtterLaas: BrevResponse = {
      ...vedtaksBrev,
      info: nyBrevInfo({ ...vedtaksBrev.info, status: { type: "Attestering" } }),
    };
    let brevErLaast = false;
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (req) => {
      if (brevErLaast) {
        req.reply([brevEtterLaas.info]);
      } else {
        req.reply([vedtaksBrev.info]);
      }
    }).as("alleBrevPåSak");

    cy.intercept("PATCH", "/bff/skribenten-backend/sak/123456/brev/1", (req) => {
      expect(req.body).to.deep.equal({ laastForRedigering: true });
      brevErLaast = true;
      req.reply(brevEtterLaas);
    }).as("låsBrev");

    cy.contains("Fortsett").click();
    cy.wait("@lagreBrev");
    cy.get("@lagreBrev.all").should("have.length", 1);

    cy.contains("Brevet er klart for attestering").click();
    cy.wait("@låsBrev");
    cy.contains("Klar til Attestering").should("be.visible");
  });

  it("kan attestere, forhåndsvise og sende brev", () => {
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1/attestering?reserver=true", (req) =>
      req.reply(defaultBrev),
    ).as("brev");

    cy.visit("/saksnummer/123456/attester/1/redigering");
    cy.contains("Underskrift").should("exist");
    cy.clock();

    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev/1/pdf/send", (req) => {
      req.reply({
        journalpostId: 9908,
        error: null,
      });
    });

    cy.intercept("PUT", "/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=*", (req) => {
      req.reply({ ...defaultBrev, redigertBrev: req.body });
    }).as("oppdaterBrevtekst");

    cy.intercept("PUT", "/bff/skribenten-backend/sak/123456/brev/1/attestering?frigiReservasjon=true", (req) => {
      expect(req.body.redigertBrev.signatur.attesterendeSaksbehandlerNavn).to.equal(
        "Dette er det nye attestant navnet mitt",
      );

      // I denne requesten så attestereres brevet. Sjekk at forventede endringer er med.
      expect(req.body.redigertBrev.blocks).to.have.length.above(0);
      expect(req.body.redigertBrev.blocks.at(-1).content.at(-1).editedText.trim()).to.include(
        "Dette er en ny tekstblokk",
      );

      req.reply({
        ...defaultBrev,
        redigertBrev: req.body.redigertBrev,
        saksbehandlerValg: req.body.saksbehandlerValg,
        info: { ...defaultBrev.info, status: { type: "Attestering" } },
      });
    }).as("attester");

    //oppdaterer underskrift
    cy.contains("Underskrift")
      .click()
      .type("{selectall}{backspace}{selectall}{backspace}")
      .type("Dette er det nye attestant navnet mitt");
    cy.tick(1000);

    //oppdaterer brevtekst
    cy.get("@oppdaterBrevtekst.all").should("have.length", 0);
    cy.contains("span[contenteditable='true']", "weeks.")
      .focus()
      .type("{end}{enter}Dette er en ny tekstblokk", { delay: 20 });
    cy.tick(6000);
    cy.wait("@oppdaterBrevtekst");

    // underskrift og brevtekst er oppdatert
    cy.get("p:contains('Dette er det nye attestant navnet mitt')").should("exist");
    cy.contains("Dette er en ny tekstblokk").should("exist");

    //attesterer
    cy.get("@attester.all").should("have.length", 0);
    cy.contains("Fortsett").click();
    cy.wait("@attester");
    cy.url().should("contain", "/saksnummer/123456/attester/1/forhandsvisning");

    //------Forhåndsvisning------
    cy.contains("Information about application processing time").should("exist");
    cy.contains("Mottaker").should("exist");
    cy.contains("Tydelig Bakke").should("exist");
    cy.contains("Mauråsveien 29").should("exist");
    cy.contains("4844 Arendal").should("exist");
    cy.contains("Distribusjonstype").should("exist");
    cy.contains("Sentral print").should("exist");
    cy.wait("@pdf").its("response.statusCode").should("eq", 200);
    cy.contains("Send brev").click();
    cy.contains("Vil du sende brevet?").should("exist");
    cy.contains("Du kan ikke angre denne handlingen.").should("exist");
    cy.contains("Ja, send brev").click();

    //------Kvittering------
    cy.url().should("contain", "/saksnummer/123456/attester/1/kvittering");
    cy.contains("Sendt til mottaker").should("exist");
    cy.contains("Informasjon om saksbehandlingstid").should("exist");
    cy.contains("Informasjon om saksbehandlingstid").click();
    cy.contains("Mottaker").should("exist");
    cy.contains("Tydelig Bakke").should("exist");
    cy.contains("Distribueres via").should("exist");
    cy.contains("Sentral print").should("exist");
    cy.contains("Journalpost ID").should("exist");
    cy.contains("9908").should("exist");
  });
});
