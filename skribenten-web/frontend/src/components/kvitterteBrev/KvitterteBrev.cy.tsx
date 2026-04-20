import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { createRootRoute, createRouter, RouterProvider } from "@tanstack/react-router";

import { type BestillBrevResponse, type BrevInfo, Distribusjonstype } from "~/types/brev";
import { type Nullable } from "~/types/Nullable";

import { nyBrevInfo } from "../../../cypress/utils/brevredigeringTestUtils";
import KvitterteBrev from "./KvitterteBrev";
import { type KvittertBrev } from "./KvitterteBrevUtils";

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
  brevFørHandling: nyBrevInfo({
    distribusjonstype: Distribusjonstype.LOKALPRINT,
  }),
  sendtBrevResponse: { journalpostId: 1, error: null },
});
const sendBrevSuccessSentralprint = nyKvittertBrev({
  apiStatus: "success",
  context: "sendBrev",
  brevFørHandling: nyBrevInfo({
    distribusjonstype: Distribusjonstype.SENTRALPRINT,
  }),
  sendtBrevResponse: { journalpostId: 1, error: null },
});

const KvitterteBrevWithContext = ({ kvitterteBrev, sakId }: { kvitterteBrev: KvittertBrev[]; sakId: string }) => {
  const rootRoute = createRootRoute({
    component: () => <KvitterteBrev kvitterteBrev={kvitterteBrev} sakId={sakId} />,
  });
  const router = createRouter({
    routeTree: rootRoute,
  });

  return (
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
    </QueryClientProvider>
  );
};

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

    cy.mount(<KvitterteBrevWithContext kvitterteBrev={kvitterteBrev} sakId="123456" />);

    cy.get('span:contains("Kunne ikke sende brev")').eq(0).should("be.visible");
    cy.get('span:contains("Kunne ikke sende brev")').eq(0);
    cy.contains("Skribenten klarte ikke å sende brevet.").should("be.visible");
    cy.contains("Brevet ligger lagret i brevbehandler til brevet er sendt.").should("be.visible");
    cy.contains("Prøv å sende igjen").should("be.visible");
    cy.get('span:contains("Kunne ikke sende brev")').eq(0).closest("section").find("button[aria-expanded]").click();

    cy.get('span:contains("Kunne ikke sende brev")');
    //ser ut som at .contains cacher elementet som vi får, så vi henter disse med .get
    cy.get('p:contains("Skribenten klarte ikke å sende brevet.")').should("be.visible");
    cy.get('p:contains("Brevet ligger lagret i brevbehandler til brevet er sendt.")').should("be.visible");
    cy.get('button:contains("Prøv å sende igjen")').should("be.visible");
    cy.get('span:contains("Kunne ikke sende brev")').eq(1).closest("section").find("button[aria-expanded]").click();

    cy.contains("Lokalprint – arkivert").should("be.visible");
    cy.contains("Lokalprint – arkivert");
    cy.contains("Mottaker").should("be.visible");
    cy.contains("Tydelig Bakke").should("be.visible");
    cy.contains("Distribusjon").should("be.visible");
    cy.contains("Lokal print").should("be.visible");
    cy.contains("Journalpost").should("be.visible");
    cy.contains("1").should("be.visible");
    cy.contains("Åpne PDF").should("be.visible");
    cy.contains("Lokalprint – arkivert").closest("section").find("button[aria-expanded]").click();

    cy.contains("Klar for attestering").should("be.visible");
    cy.contains("Klar for attestering").closest("section").find("button[aria-expanded]").click();
    //ser ut som at .contains cacher elementet som vi får, så vi henter disse med .get
    cy.get('p:contains("Mottaker")').eq(1).should("be.visible");
    cy.get('p:contains("Tydelig Bakke")').eq(1).should("be.visible");
    cy.get('p:contains("Distribusjon")').eq(1).should("be.visible");
    cy.get('p:contains("Sentral print")').should("be.visible");
    cy.get('p:contains("Tydelig Bakke")').eq(1).should("be.visible");
    cy.contains("Klar for attestering").closest("section").find("button[aria-expanded]").click();

    cy.contains("Sendt til mottaker").should("be.visible");
    cy.contains("Sendt til mottaker").closest("section").find("button[aria-expanded]").click();
    cy.get('p:contains("Mottaker")').eq(2).should("be.visible");
    cy.get('p:contains("Tydelig Bakke")').eq(2).should("be.visible");
    cy.get('p:contains("Distribusjon")').eq(2).should("be.visible");
    cy.get('p:contains("Sentral print")').eq(1).should("be.visible");
    cy.get('p:contains("Tydelig Bakke")').eq(2).should("be.visible");
    cy.get('p:contains("Journalpost")').eq(1).should("be.visible");
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
    cy.mount(<KvitterteBrevWithContext kvitterteBrev={kvitterteBrev} sakId="123456" />);

    cy.get(".aksel-expansioncard").should("have.length", 6);
    cy.get(".aksel-expansioncard").eq(0).contains("Kunne ikke sende brev");
    cy.get(".aksel-expansioncard").eq(1).contains("Kunne ikke sende brev");
    cy.get(".aksel-expansioncard").eq(2).contains("Kunne ikke sende brev");
    cy.get(".aksel-expansioncard").eq(3).contains("Lokalprint – arkivert");
    cy.get(".aksel-expansioncard").eq(4).contains("Klar for attestering");
    cy.get(".aksel-expansioncard").eq(5).contains("Sendt til mottaker");
  });
});
