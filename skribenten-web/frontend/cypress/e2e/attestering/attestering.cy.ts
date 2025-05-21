import type { Interception } from "cypress/types/net-stubbing";

import type { BrevResponse } from "~/types/brev";

import brev from "../../fixtures/bekreftelsePåFlyktningstatus/brev.json";
import {
  nyBrevInfo,
  nyBrevResponse,
  nyLiteral,
  nyParagraphBlock,
  nyRedigertBrev,
  nySignatur,
} from "../../utils/brevredigeringTestUtils";

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
    cy.fixture("helloWorldPdf.txt").then((pdf) => {
      cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1/pdf", pdf).as("pdf");
    });

    cy.intercept("GET", "/bff/internal/userInfo", (req) =>
      req.reply({ id: "Z990297", navn: "F_Z990297 E_Z990297", rolle: "Saksbehandler" }),
    ).as("userInfo");

    const brevEtterLagringAvSignatur = nyBrevResponse({
      ...vedtaksBrev,
      redigertBrev: nyRedigertBrev({
        ...vedtaksBrev.redigertBrev,
        signatur: nySignatur({
          ...vedtaksBrev.redigertBrev.signatur,
          saksbehandlerNavn: "Dette er en signatur",
        }),
      }),
    });

    const brevEtterOppdateringAvTekst: BrevResponse = {
      ...brevEtterLagringAvSignatur,
      redigertBrev: nyRedigertBrev({
        ...brevEtterLagringAvSignatur.redigertBrev,
        signatur: brevEtterLagringAvSignatur.redigertBrev.signatur,
        blocks: [
          ...brevEtterLagringAvSignatur.redigertBrev.blocks,
          nyParagraphBlock({ content: [nyLiteral({ editedText: "Dette er en ny tekstblokk", text: "" })] }),
        ],
      }),
    };

    const brevEtterLaas: BrevResponse = {
      ...brevEtterLagringAvSignatur,
      info: nyBrevInfo({ ...brevEtterLagringAvSignatur.info, status: { type: "Attestering" } }),
    };

    //brevvelger
    cy.intercept(
      "GET",
      "/bff/skribenten-backend/brevmal/PE_BEKREFTELSE_PAA_FLYKTNINGSTATUS/modelSpecification",
      (req) => req.reply({ types: {}, letterModelTypeName: null }),
    );

    let hentAlleSakBrevKallNr = 0;
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (req) => {
      //1 fordi ved klikking av det valgte brevet man skal opprette blir det gjort et nytt kall for å hente alle brev på saken

      if (hentAlleSakBrevKallNr <= 1) {
        hentAlleSakBrevKallNr++;
        req.reply([]);
      } else if (hentAlleSakBrevKallNr === 2) {
        hentAlleSakBrevKallNr++;
        req.reply([vedtaksBrev.info]);
      } else {
        hentAlleSakBrevKallNr++;
        req.reply([brevEtterLaas.info]);
      }
    }).as("alleBrevPåSak");

    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev", (req) => {
      expect(req.body).to.deep.equal({
        brevkode: "PE_BEKREFTELSE_PAA_FLYKTNINGSTATUS",
        spraak: "NB",
        avsenderEnhetsId: "",
        saksbehandlerValg: {},
        mottaker: null,
        vedtaksId: 9876,
      });
      return req.reply(vedtaksBrev);
    }).as("opprettBrev");

    cy.visit('/saksnummer/123456/brevvelger?vedtaksId="9876"');

    cy.intercept("GET", "/bff/skribenten-backend/brev/1/reservasjon", {
      fixture: "brevreservasjon.json",
    }).as("reservasjon");

    cy.contains("Innhente opplysninger").click();
    cy.contains("Bekreftelse på flyktningstatus").click();
    cy.get("@alleBrevPåSak.all").should("have.length", 1);
    cy.contains("Åpne brev").click();

    //brev redigering
    cy.intercept("PUT", "/bff/skribenten-backend/brev/1/saksbehandlerValg", (req) => {
      expect(req.body).to.deep.equal({});
      req.reply(vedtaksBrev);
    }).as("saksbehandlerValg");

    cy.intercept("PUT", "/bff/skribenten-backend/brev/1/signatur", (req) => {
      expect(req.body).to.deep.equal("Dette er en signatur");
      req.reply(brevEtterLagringAvSignatur);
    }).as("signatur");

    cy.intercept("PUT", "/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=*", (req) => {
      req.reply(brevEtterOppdateringAvTekst);
    }).as("oppdaterBrevtekst");

    cy.intercept("PUT", "/bff/skribenten-backend/sak/123456/brev/1?frigiReservasjon=true", (req) =>
      req.reply(brevEtterOppdateringAvTekst),
    ).as("lagreBrev");

    cy.url().should("contain", "/saksnummer/123456/brev/1");
    cy.get("@saksbehandlerValg.all").should("have.length", 0);
    cy.get("@signatur.all").should("have.length", 0);
    cy.wait("@reservasjon");
    cy.contains("Signatur").click().type("{selectall}{backspace}Dette er en signatur");
    cy.wait("@signatur");
    cy.get("@saksbehandlerValg.all").should("have.length", 1);
    cy.get("@signatur.all").should("have.length", 1);
    cy.get("p:contains('Dette er en signatur')").should("exist");

    cy.contains("ankomst til Norge.").focus().type("{end}{enter}Dette er en ny tekstblokk");
    cy.wait("@oppdaterBrevtekst");
    cy.contains("Dette er en ny tekstblokk").should("exist");
    cy.get("@lagreBrev.all").should("have.length", 0);
    cy.contains("Fortsett").click();
    cy.wait("@lagreBrev");
    cy.get("@lagreBrev.all").should("have.length", 1);

    //brevbehandler
    cy.intercept("PATCH", "/bff/skribenten-backend/sak/123456/brev/1", (req) => {
      expect(req.body).to.deep.equal({ saksId: "123456", brevId: 1, laastForRedigering: true });
      req.reply(brevEtterLaas);
    }).as("låsBrev");

    cy.contains("Brevet er klart for attestering").click();
    cy.wait("@låsBrev");
    cy.contains("Klar til Attestering").should("be.visible");
  });

  it("kan attestere, forhåndsvise og sende brev", () => {
    cy.intercept("GET", "/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification", {
      fixture: "modelSpecification.json",
    }).as("modelSpecification");

    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1/attestering?reserver=true", (req) =>
      req.reply(defaultBrev),
    ).as("brev");

    cy.fixture("helloWorldPdf.txt").then((pdf) => {
      cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1/pdf", pdf).as("pdf");
    });
    cy.intercept("GET", "/bff/skribenten-backend/brev/1/reservasjon", {
      fixture: "brevreservasjon.json",
    }).as("reservasjon");

    cy.visit("/saksnummer/123456/attester/1/redigering");
    cy.contains("Underskrift").should("exist");
    cy.clock();

    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev/1/pdf/send", (req) => {
      req.reply({
        journalpostId: 9908,
        error: null,
      });
    });

    const brevEtterOppdateringAvAttestantNavn = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        signatur: nySignatur({
          attesterendeSaksbehandlerNavn: "Dette er det nye attestant navnet mitt",
        }),
      }),
    });

    cy.intercept("PUT", "/bff/skribenten-backend/brev/1/attestant", (req) => {
      expect(req.body).contains("Dette er det nye attestant navnet mitt");
      req.reply(brevEtterOppdateringAvAttestantNavn);
    }).as("attestantNavn");

    const brevEtterOppdateringAvSaksbehandlerValg: BrevResponse = {
      ...brevEtterOppdateringAvAttestantNavn,
      saksbehandlerValg: {
        ...brevEtterOppdateringAvAttestantNavn.saksbehandlerValg,
        land: "Norge",
      },
    };

    cy.intercept("PUT", "/bff/skribenten-backend/brev/1/saksbehandlerValg", (req) => {
      expect(req.body).contains({ land: "Norge" });
      req.reply(brevEtterOppdateringAvSaksbehandlerValg);
    }).as("saksbehandlerValg");

    const brevEtterOppdateringAvTekst: BrevResponse = {
      ...brevEtterOppdateringAvSaksbehandlerValg,
      redigertBrev: nyRedigertBrev({
        ...brevEtterOppdateringAvSaksbehandlerValg.redigertBrev,
        signatur: brevEtterOppdateringAvSaksbehandlerValg.redigertBrev.signatur,
        blocks: [
          ...brevEtterOppdateringAvSaksbehandlerValg.redigertBrev.blocks,
          nyParagraphBlock({
            content: [nyLiteral({ editedText: "Dette er en ny tekstblokk", text: "" })],
          }),
        ],
      }),
    };

    cy.intercept("PUT", "/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=*", (req) => {
      req.reply(brevEtterOppdateringAvTekst);
    }).as("oppdaterBrevtekst");

    cy.intercept("PUT", "/bff/skribenten-backend/sak/123456/brev/1/attestering?frigiReservasjon=true", (req) => {
      expect(req.body).to.deep.include({
        saksbehandlerValg: { land: "Norge" },
        signaturAttestant: "Dette er det nye attestant navnet mitt",
      });

      expect(req.body.redigertBrev.blocks).to.have.length.above(0);

      req.reply(brevEtterOppdateringAvTekst);
    }).as("attester");

    //oppdaterer underskrift
    cy.get("@attestantNavn.all").should("have.length", 0);
    cy.contains("Underskrift")
      .click()
      .type("{selectall}{backspace}{selectall}{backspace}")
      .type("Dette er det nye attestant navnet mitt");
    cy.tick(3000);
    cy.wait("@attestantNavn");
    cy.get("p:contains('Dette er det nye attestant navnet mitt')").should("exist");

    // //oppdaterer saksbehandlerValg
    cy.get("@saksbehandlerValg.all").should("have.length", 0);
    cy.contains("Land").click().type("Norge");
    cy.tick(3000);
    cy.wait("@saksbehandlerValg");
    cy.get("@saksbehandlerValg.all").should("have.length", 1);
    cy.get("@saksbehandlerValg").then((intercept) => {
      //then gir oss ikke riktig type så vi bare caster den
      const casted = intercept as unknown as Interception;
      expect(casted.response?.statusCode).to.equal(200);
    });

    //oppdaterer brevtekst
    cy.get("@oppdaterBrevtekst.all").should("have.length", 0);
    cy.contains("span[contenteditable='true']", "weeks.")
      .focus()
      .type("{end}{enter}Dette er en ny tekstblokk", { delay: 100 });
    cy.tick(6000);
    cy.wait("@oppdaterBrevtekst");
    cy.contains("Dette er en ny tekstblokk").should("exist");

    //attesterer
    cy.get("@attester.all").should("have.length", 0);
    cy.contains("Fortsett").click();
    cy.wait("@attester");
    cy.url().should("contain", "/saksnummer/123456/attester/1/forhandsvisning");

    //------Forhåndsvisning------
    cy.contains("Information about application processing time").should("exist");
    cy.contains("Mottaker").should("exist");
    cy.contains("Tydelig Bakke (Bruker)").should("exist");
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
