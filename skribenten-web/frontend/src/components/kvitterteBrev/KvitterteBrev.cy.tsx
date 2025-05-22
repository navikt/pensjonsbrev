import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

import { type BestillBrevResponse, type BrevInfo, Distribusjonstype } from "~/types/brev";
import type { Nullable } from "~/types/Nullable";

import { nyBrevInfo } from "../../../cypress/utils/brevredigeringTestUtils";
import KvitterteBrev from "./KvitterteBrev";
import type { KvittertBrev } from "./KvitterteBrevUtils";

const nyKvittertBrev = (args: {
  apiStatus?: "error" | "success";
  context?: "sendBrev" | "attestering";
  brevFørHandling?: BrevInfo;
  attesteringResponse?: Nullable<BrevInfo>;
  sendtBrevResponse?: Nullable<BestillBrevResponse>;
}): KvittertBrev => {
  return {
    apiStatus: args.apiStatus ?? "success",
    context: args.context ?? "attestering",
    brevFørHandling: args.brevFørHandling ?? nyBrevInfo({}),
    attesteringResponse: args.attesteringResponse ?? null,
    sendtBrevResponse: args.sendtBrevResponse ?? null,
  };
};

const queryClient = new QueryClient();

const attesteringError = nyKvittertBrev({
  apiStatus: "error",
  context: "attestering",
});
const attesteringSuccess = nyKvittertBrev({
  apiStatus: "success",
  context: "attestering",
  attesteringResponse: nyBrevInfo({}),
});
const sendBrevError = nyKvittertBrev({
  apiStatus: "error",
  context: "sendBrev",
});
const sendBrevSuccessLokalprint = nyKvittertBrev({
  apiStatus: "success",
  context: "sendBrev",
  brevFørHandling: nyBrevInfo({ distribusjonstype: Distribusjonstype.LOKALPRINT }),
  sendtBrevResponse: { journalpostId: 1, error: null },
});
const sendBrevSuccessSentralprint = nyKvittertBrev({
  apiStatus: "success",
  context: "sendBrev",
  brevFørHandling: nyBrevInfo({ distribusjonstype: Distribusjonstype.SENTRALPRINT }),
  sendtBrevResponse: { journalpostId: 1, error: null },
});

describe("<KvitterteBrev />", () => {
  beforeEach(() => {
    cy.setupSakStubs();
    cy.viewport(800, 1400);
  });

  it("Håndterer visning av content fra ulik context og status", () => {
    const kvitterteBrev = [
      attesteringError,
      attesteringSuccess,
      sendBrevError,
      sendBrevSuccessLokalprint,
      sendBrevSuccessSentralprint,
    ];

    cy.mount(
      <QueryClientProvider client={queryClient}>
        <KvitterteBrev kvitterteBrev={kvitterteBrev} sakId={"123456"} />
      </QueryClientProvider>,
    );

    cy.get('span:contains("Kunne ikke sende brev")').eq(0).should("be.visible");
    cy.get('span:contains("Kunne ikke sende brev")').eq(0).click();
    cy.contains("Skribenten klarte ikke å sende brevet.").should("be.visible");
    cy.contains("Brevet ligger lagret i brevbehandler til brevet er sendt.").should("be.visible");
    cy.contains("Prøv å sende igjen").should("be.visible");
    cy.get('span:contains("Kunne ikke sende brev")').eq(0).click();

    cy.get('span:contains("Kunne ikke sende brev")').eq(1).click();
    //ser ut som at .contains cacher elementet som vi får, så vi henter disse med .get
    cy.get('p:contains("Skribenten klarte ikke å sende brevet.")').should("be.visible");
    cy.get('p:contains("Brevet ligger lagret i brevbehandler til brevet er sendt.")').should("be.visible");
    cy.get('button:contains("Prøv å sende igjen")').should("be.visible");
    cy.get('span:contains("Kunne ikke sende brev")').eq(2).click();

    cy.contains("Lokalprint - sendt til joark").should("be.visible");
    cy.contains("Lokalprint - sendt til joark").click();
    cy.contains("Mottaker").should("be.visible");
    cy.contains("Tydelig Bakke").should("be.visible");
    cy.contains("Distribueres via").should("be.visible");
    cy.contains("Lokal print").should("be.visible");
    cy.contains("Journalpost ID").should("be.visible");
    cy.contains("1").should("be.visible");
    cy.contains("Åpne PDF i ny fane").should("be.visible");
    cy.contains("Lokalprint - sendt til joark").click();

    cy.contains("Klar til attestering").should("be.visible");
    cy.contains("Klar til attestering").click();
    //ser ut som at .contains cacher elementet som vi får, så vi henter disse med .get
    cy.get('p:contains("Mottaker")').eq(1).should("be.visible");
    cy.get('p:contains("Tydelig Bakke")').eq(1).should("be.visible");
    cy.get('p:contains("Distribueres via")').eq(1).should("be.visible");
    cy.get('p:contains("Sentral print")').should("be.visible");
    cy.get('p:contains("Tydelig Bakke")').eq(1).should("be.visible");
    cy.contains("Klar til attestering").click();

    cy.contains("Sendt til mottaker").should("be.visible");
    cy.contains("Sendt til mottaker").click();
    cy.get('p:contains("Mottaker")').eq(2).should("be.visible");
    cy.get('p:contains("Tydelig Bakke")').eq(2).should("be.visible");
    cy.get('p:contains("Distribueres via")').eq(2).should("be.visible");
    cy.get('p:contains("Sentral print")').eq(1).should("be.visible");
    cy.get('p:contains("Tydelig Bakke")').eq(2).should("be.visible");
    cy.get('p:contains("Journalpost ID")').eq(1).should("be.visible");
    cy.get('p:contains("1")').eq(1).should("be.visible");
  });

  it("sorterer brevene i prioritert rekkefølge", () => {
    const kvitterteBrev = [
      sendBrevSuccessSentralprint,
      sendBrevError,
      sendBrevSuccessLokalprint,
      attesteringError,
      sendBrevError,
      attesteringSuccess,
    ];
    cy.mount(
      <QueryClientProvider client={queryClient}>
        <KvitterteBrev kvitterteBrev={kvitterteBrev} sakId={"123456"} />
      </QueryClientProvider>,
    );

    cy.get(".navds-accordion__item").should("have.length", 6);
    cy.get(".navds-accordion__item").eq(0).contains("Kunne ikke sende brev");
    cy.get(".navds-accordion__item").eq(1).contains("Kunne ikke sende brev");
    cy.get(".navds-accordion__item").eq(2).contains("Kunne ikke sende brev");
    cy.get(".navds-accordion__item").eq(3).contains("Lokalprint - sendt til joark");
    cy.get(".navds-accordion__item").eq(4).contains("Klar til attestering");
    cy.get(".navds-accordion__item").eq(5).contains("Sendt til mottaker");
  });
});
