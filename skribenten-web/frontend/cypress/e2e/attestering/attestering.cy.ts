import type { Interception } from "cypress/types/net-stubbing";

import type { BrevResponse } from "~/types/brev";

import {
  nyBrevResponse,
  nyLiteral,
  nyParagraphBlock,
  nyRedigertBrev,
  nySignatur,
} from "../../utils/brevredigeringTestUtils";

const defaultBrev = nyBrevResponse({});

describe("attestering", () => {
  beforeEach(() => {
    cy.setupSakStubs();

    cy.intercept("GET", "/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification", {
      fixture: "modelSpecification.json",
    }).as("modelSpecification");

    cy.viewport(1200, 1400);
    cy.visit("/saksnummer/123456/vedtak/1/redigering");
  });

  it("kan attestere, forhåndsvise og sende brev", () => {
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (req) => req.reply(defaultBrev)).as(
      "brev",
    );

    cy.fixture("helloWorldPdf.txt").then((pdf) => {
      cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1/pdf", pdf).as("pdf");
    });

    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev/1/pdf/send", (req) => {
      req.reply({
        journalpostId: 9908,
        error: null,
      });
    });

    cy.intercept("GET", "/bff/skribenten-backend/brev/1/reservasjon", {
      fixture: "brevreservasjon.json",
    }).as("reservasjon");

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

    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev/1/attester", (req) => {
      expect(req.body).to.deep.equal({
        saksbehandlerValg: { land: "Norge" },
        redigertBrev: brevEtterOppdateringAvTekst.redigertBrev,
        signatur: "Dette er det nye attestant navnet mitt",
      });
      req.reply(brevEtterOppdateringAvTekst);
    }).as("attester");

    //oppdaterer underskrift
    cy.get("@attestantNavn.all").should("have.length", 0);
    cy.contains("Underskrift")
      .click()
      .type("{selectall}{backspace}{selectall}{backspace}")
      .type("Dette er det nye attestant navnet mitt");
    cy.wait("@attestantNavn");
    cy.get("p:contains('Dette er det nye attestant navnet mitt')").should("exist");

    // //oppdaterer saksbehandlerValg
    cy.get("@saksbehandlerValg.all").should("have.length", 0);
    cy.contains("Land").click().type("Norge");
    cy.wait("@saksbehandlerValg");
    cy.get("@saksbehandlerValg.all").should("have.length", 1);
    cy.get("@saksbehandlerValg").then((intercept) => {
      //then gir oss ikke riktig type så vi bare caster den
      const casted = intercept as unknown as Interception;
      expect(casted.response?.statusCode).to.equal(200);
    });

    //oppdaterer brevtekst
    cy.get("@oppdaterBrevtekst.all").should("have.length", 0);
    cy.contains("weeks.").focus();
    cy.contains("weeks.").type("{end}{enter}Dette er en ny tekstblokk");
    cy.wait("@oppdaterBrevtekst");
    cy.contains("Dette er en ny tekstblokk").should("exist");

    //attesterer
    cy.get("@attester.all").should("have.length", 0);
    cy.contains("Fortsett").click();
    cy.wait("@attester");
    cy.url().should("contain", "/saksnummer/123456/vedtak/1/forhandsvisning");

    //------Forhåndsvisning------
    cy.contains("Information about application processing time").should("exist");
    cy.contains("Mottaker").should("exist");
    cy.contains("Tydelig Bakke (Bruker)").should("exist");
    cy.contains("Mauråsveien 29").should("exist");
    cy.contains("4844 Arendal").should("exist");
    cy.contains("Distribusjonstype").should("exist");
    cy.contains("Sentral print").should("exist");
    cy.wait("@pdf");
    cy.contains("Hello World").should("exist");
    cy.contains("Send brev").click();
    cy.contains("Vil du sende brevet?").should("exist");
    cy.contains("Du kan ikke angre denne handlingen.").should("exist");
    cy.contains("Ja, send brev").click();

    //------Kvittering------
    cy.url().should("contain", "/saksnummer/123456/vedtak/1/kvittering");
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
