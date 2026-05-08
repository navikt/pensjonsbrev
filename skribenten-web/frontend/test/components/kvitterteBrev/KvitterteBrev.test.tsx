import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { createRootRoute, createRouter, RouterProvider } from "@tanstack/react-router";
import { render, screen, waitFor, within } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, expect, test, vi } from "vitest";

import KvitterteBrev from "~/components/kvitterteBrev/KvitterteBrev";
import { type KvittertBrev } from "~/components/kvitterteBrev/KvitterteBrevUtils";
import { SendtBrevProvider } from "~/routes/saksnummer_/$saksId/kvittering/-components/SendtBrevContext";
import { type BestillBrevResponse, type BrevInfo, Distribusjonstype } from "~/types/brev";
import { type Nullable } from "~/types/Nullable";

import { nyBrevInfo } from "../../utils/brevredigeringTestUtils";

vi.mock("~/hooks/useSakGjelderNavn", () => ({
  useSakGjelderNavnFormatert: () => "Tydelig Bakke",
}));

const nyKvittertBrev = (args: {
  apiStatus?: "error" | "success";
  context?: "sendBrev" | "attestering";
  brevFørHandling?: BrevInfo;
  attesteringResponse?: Nullable<BrevInfo>;
  sendtBrevResponse?: Nullable<BestillBrevResponse>;
}): KvittertBrev => ({
  apiStatus: args.apiStatus ?? "success",
  context: args.context ?? "attestering",
  brevFørHandling: args.brevFørHandling ?? nyBrevInfo({}),
  attesteringResponse: args.attesteringResponse ?? null,
  sendtBrevResponse: args.sendtBrevResponse ?? null,
});

const attesteringError = nyKvittertBrev({ apiStatus: "error", context: "attestering" });
const attesteringSuccess = nyKvittertBrev({
  apiStatus: "success",
  context: "attestering",
  attesteringResponse: nyBrevInfo({}),
});
const sendBrevError = nyKvittertBrev({ apiStatus: "error", context: "sendBrev" });
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

async function renderKvitterteBrev(kvitterteBrev: KvittertBrev[], sakId = "123456") {
  const queryClient = new QueryClient({
    defaultOptions: { queries: { retry: false }, mutations: { retry: false } },
  });

  const rootRoute = createRootRoute({
    component: () => (
      <SendtBrevProvider>
        <KvitterteBrev kvitterteBrev={kvitterteBrev} sakId={sakId} />
      </SendtBrevProvider>
    ),
  });
  const router = createRouter({ routeTree: rootRoute });
  await router.load();

  const result = render(
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
    </QueryClientProvider>,
  );

  // Wait for router to finish rendering
  await waitFor(() => {
    expect(result.container.innerHTML).not.toBe("<div></div>");
  });

  return { user: userEvent.setup(), ...result };
}

function getCardSection(title: string) {
  const section = screen.getByText(title).closest("section");
  if (!section) {
    throw new Error(`Could not find section for card "${title}"`);
  }
  return section;
}

describe("<KvitterteBrev />", () => {
  test("Håndterer visning av content fra ulik context og status", async () => {
    const kvitterteBrev = [
      attesteringError,
      attesteringSuccess,
      sendBrevError,
      sendBrevSuccessLokalprint,
      sendBrevSuccessSentralprint,
    ];

    const { user } = await renderKvitterteBrev(kvitterteBrev);

    // Error cards - "Kunne ikke sende brev" tags (attesteringError + sendBrevError = 2)
    const errorTags = screen.getAllByText("Kunne ikke sende brev");
    expect(errorTags).toHaveLength(2);

    // Error content is visible (error cards are default open)
    expect(screen.queryAllByText("Skribenten klarte ikke å sende brevet.").length).toBe(2);
    expect(screen.queryAllByText("Brevet ligger lagret i brevbehandler til brevet er sendt.").length).toBe(2);
    expect(screen.queryAllByText("Prøv å sende igjen").length).toBe(2);

    // Toggle first error card
    const firstErrorSection = errorTags[0].closest("section")!;
    const firstExpandButton = within(firstErrorSection).getByRole("button", { expanded: true });
    await user.click(firstExpandButton);

    // Verify the button toggles state (jsdom doesn't remove content from DOM, so check button state)
    expect(firstExpandButton.getAttribute("aria-expanded")).toBe("false");

    // Lokalprint card
    const lokalprintSection = getCardSection("Lokalprint – arkivert");
    const lokalprintButton = within(lokalprintSection).getByRole("button", { expanded: true });
    expect(lokalprintButton).not.toBeNull();
    expect(within(lokalprintSection).queryByText("Mottaker")).not.toBeNull();
    expect(within(lokalprintSection).queryByText("Tydelig Bakke")).not.toBeNull();
    expect(within(lokalprintSection).queryByText("Distribusjon")).not.toBeNull();
    expect(within(lokalprintSection).queryByText("Lokal print")).not.toBeNull();
    expect(within(lokalprintSection).queryByText("Journalpost")).not.toBeNull();
    expect(within(lokalprintSection).queryByText("1")).not.toBeNull();
    expect(within(lokalprintSection).queryByRole("button", { name: "Åpne PDF" })).not.toBeNull();

    // Attestering success card
    const attesteringSection = getCardSection("Klar for attestering");
    const attesteringButton = within(attesteringSection).getByRole("button", { expanded: false });
    await user.click(attesteringButton);
    expect(attesteringButton.getAttribute("aria-expanded")).toBe("true");
    expect(within(attesteringSection).queryByText("Mottaker")).not.toBeNull();
    expect(within(attesteringSection).queryByText("Tydelig Bakke")).not.toBeNull();
    expect(within(attesteringSection).queryByText("Distribusjon")).not.toBeNull();
    expect(within(attesteringSection).queryByText("Sentral print")).not.toBeNull();

    // Sentralprint card
    const sentralprintSection = getCardSection("Sendt til mottaker");
    const sentralprintButton = within(sentralprintSection).getByRole("button", { expanded: false });
    await user.click(sentralprintButton);
    expect(sentralprintButton.getAttribute("aria-expanded")).toBe("true");
    expect(within(sentralprintSection).queryByText("Mottaker")).not.toBeNull();
    expect(within(sentralprintSection).queryByText("Tydelig Bakke")).not.toBeNull();
    expect(within(sentralprintSection).queryByText("Distribusjon")).not.toBeNull();
    expect(within(sentralprintSection).queryByText("Sentral print")).not.toBeNull();
    expect(within(sentralprintSection).queryByText("Journalpost")).not.toBeNull();
    expect(within(sentralprintSection).queryByText("1")).not.toBeNull();
  });

  test("sorterer brevene i prioritert rekkefølge", async () => {
    const kvitterteBrev = [
      sendBrevSuccessSentralprint,
      sendBrevError,
      sendBrevSuccessLokalprint,
      attesteringError,
      sendBrevError,
      attesteringSuccess,
    ];

    const { container } = await renderKvitterteBrev(kvitterteBrev);

    const cards = container.querySelectorAll(".aksel-expansioncard");
    expect(cards).toHaveLength(6);

    // Errors first (3 of them: sendBrevError + attesteringError + sendBrevError)
    expect(cards[0].textContent).toContain("Kunne ikke sende brev");
    expect(cards[1].textContent).toContain("Kunne ikke sende brev");
    expect(cards[2].textContent).toContain("Kunne ikke sende brev");

    // Then lokalprint
    expect(cards[3].textContent).toContain("Lokalprint – arkivert");

    // Then attestering
    expect(cards[4].textContent).toContain("Klar for attestering");

    // Then sentralprint
    expect(cards[5].textContent).toContain("Sendt til mottaker");
  });
});
