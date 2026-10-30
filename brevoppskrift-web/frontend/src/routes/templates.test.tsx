import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import {
  type AnyRootRoute,
  createRootRouteWithContext,
  createRoute,
  createRouter,
  RouterProvider,
} from "@tanstack/react-router";
import { act, render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";

import { type SearchableContent } from "~/api/brevbaker-api-endpoints";
import { type TemplateDescription } from "~/api/brevbakerTypes";
import { Route as TemplatesRouteImport } from "~/routes/templates";
import { createLocalSearchClient, type SearchClient } from "~/search/searchClient";
import { deferrableClient } from "~test/support/deferrableClient";

const { getAllTemplateDocumentation, getBrevkoderMedMetadata } = vi.hoisted(() => ({
  getAllTemplateDocumentation: {
    queryKey: (malType: string) => ["TEMPLATE_DOCUMENTATION", malType, "BATCH"] as const,
    queryFn: vi.fn(),
  },
  getBrevkoderMedMetadata: {
    queryKey: (malType: string) => ["BREVKODER", malType, "METADATA"] as const,
    queryFn: vi.fn(),
  },
}));

vi.mock("~/api/brevbaker-api-endpoints", async () => {
  const actual = await vi.importActual<typeof import("~/api/brevbaker-api-endpoints")>("~/api/brevbaker-api-endpoints");
  return { ...actual, getAllTemplateDocumentation, getBrevkoderMedMetadata };
});

// The app shares one search client across the page. Each test gets its own
// instead, so one test's corpus never leaks into the next, and a test can swap
// in a client that holds searches open.
const searchClient = vi.hoisted(() => ({ current: undefined as SearchClient | undefined }));
vi.mock("~/search/searchClient", async (importOriginal) => {
  const actual = await importOriginal<typeof import("~/search/searchClient")>();
  return { ...actual, sharedSearchClient: () => searchClient.current ?? actual.createLocalSearchClient() };
});

function installClient<T extends SearchClient>(client: T): T {
  searchClient.current?.dispose();
  searchClient.current = client;
  return client;
}

const autobrevDescriptions: TemplateDescription[] = [
  { name: "A1", letterDataClass: "AutobrevDto", languages: ["BOKMAL"] },
];
const redigerbarDescriptions: TemplateDescription[] = [
  { name: "R1", letterDataClass: "RedigerbarDto", languages: ["BOKMAL"] },
];
const autobrevContent: SearchableContent[] = [
  { brevkode: "A1", language: "BOKMAL", lines: [{ index: 0, segments: [{ type: "text", value: "Hei" }] }] },
];

function mockCorpus() {
  getBrevkoderMedMetadata.queryFn.mockImplementation((malType: string) =>
    Promise.resolve(malType === "autobrev" ? autobrevDescriptions : redigerbarDescriptions),
  );
  getAllTemplateDocumentation.queryFn.mockResolvedValue(autobrevContent);
}

/** Stands in for the template page, so opening a result has somewhere to go. */
function templateRoute(rootRoute: AnyRootRoute) {
  return createRoute({
    getParentRoute: () => rootRoute,
    path: "/template/$malType/$templateId",
    validateSearch: (search: Record<string, unknown>) => search,
    component: () => <p>Malsiden</p>,
  });
}

/** Mounts the real `/templates` route standalone. `createFileRoute` leaves the
 *  exported `Route` unbound (no id/path/parent) — the app only wires those via
 *  the codegen'd `routeTree.gen.ts` (`TemplatesRouteImport.update({ id, path,
 *  getParentRoute })`). We mirror that exact wiring here instead of using
 *  `Route` as-is, otherwise the router throws
 *  "Duplicate routes found with id: __root__". */
async function renderTemplatesRoute(search: Record<string, unknown> = {}) {
  const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });
  const rootRoute = createRootRouteWithContext<{ queryClient: QueryClient }>()({});
  const templatesRoute = TemplatesRouteImport.update({
    id: "/templates",
    path: "/templates",
    getParentRoute: () => rootRoute,
    // biome-ignore lint/suspicious/noExplicitAny: mirrors the `as any` cast routeTree.gen.ts uses for this same `.update(...)` call.
  } as any);
  const router = createRouter({
    routeTree: rootRoute.addChildren([templatesRoute, templateRoute(rootRoute)]),
    context: { queryClient },
    history: undefined,
  });
  await router.navigate({ to: "/templates", search });
  await router.load();

  const result = render(
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
    </QueryClientProvider>,
  );

  await waitFor(() => {
    expect(result.container.innerHTML).not.toBe("<div></div>");
  });

  return { user: userEvent.setup(), router, ...result };
}

describe("<AllTemplates /> (route: /templates)", () => {
  beforeEach(() => {
    installClient(createLocalSearchClient());
  });

  afterEach(() => {
    searchClient.current?.dispose();
    searchClient.current = undefined;
    getAllTemplateDocumentation.queryFn.mockReset();
    getBrevkoderMedMetadata.queryFn.mockReset();
  });

  it("shows a warning alert with a retry action when a template corpus fails to load", async () => {
    getBrevkoderMedMetadata.queryFn.mockImplementation((malType: string) =>
      Promise.resolve(malType === "autobrev" ? autobrevDescriptions : redigerbarDescriptions),
    );
    getAllTemplateDocumentation.queryFn.mockImplementation((malType: string) =>
      malType === "redigerbar" ? Promise.reject(new Error("boom")) : Promise.resolve(autobrevContent),
    );

    await renderTemplatesRoute();

    expect(await screen.findByText(/Kunne ikke hente innhold for redigerbare brev/i)).toBeTruthy();
    expect(screen.getByRole("button", { name: "Prøv igjen" })).toBeTruthy();
  });

  it("does not show the warning alert when every template corpus loads successfully", async () => {
    getBrevkoderMedMetadata.queryFn.mockImplementation((malType: string) =>
      Promise.resolve(malType === "autobrev" ? autobrevDescriptions : redigerbarDescriptions),
    );
    getAllTemplateDocumentation.queryFn.mockResolvedValue(autobrevContent);

    await renderTemplatesRoute();

    expect(screen.queryByText(/Kunne ikke hente innhold for/i)).toBeNull();
  });

  it("clicking 'Prøv igjen' retries only the failing corpus fetch", async () => {
    getBrevkoderMedMetadata.queryFn.mockImplementation((malType: string) =>
      Promise.resolve(malType === "autobrev" ? autobrevDescriptions : redigerbarDescriptions),
    );
    getAllTemplateDocumentation.queryFn.mockImplementation((malType: string) =>
      malType === "redigerbar" ? Promise.reject(new Error("boom")) : Promise.resolve(autobrevContent),
    );

    const { user } = await renderTemplatesRoute();
    await screen.findByRole("button", { name: "Prøv igjen" });

    getAllTemplateDocumentation.queryFn.mockResolvedValue(autobrevContent);
    await user.click(screen.getByRole("button", { name: "Prøv igjen" }));

    await waitFor(() => {
      expect(screen.queryByText(/Kunne ikke hente innhold for/i)).toBeNull();
    });
  });

  it("outlines the search field only while a search runs, then shows the hit summary", async () => {
    mockCorpus();
    const client = installClient(deferrableClient());

    const { user } = await renderTemplatesRoute({ tab: "innhold" });
    await waitFor(() => expect(screen.queryByText(/Klargjør malene for søk/)).toBeNull());
    // An empty box: nothing in flight, nothing to summarise.
    expect(screen.queryByTestId("search-activity")).toBeNull();
    expect(screen.queryByText(/Frasen du søker etter/)).toBeNull();

    await user.type(screen.getByRole("searchbox"), "Hei");

    // First search running: the outline, but no summary and no tab count yet.
    await waitFor(() => expect(screen.getByTestId("search-activity")).toBeTruthy());
    expect(screen.queryByText(/Frasen du søker etter/)).toBeNull();
    expect(screen.getByRole("tab", { name: "Innhold" })).toBeTruthy();

    await act(async () => client.release());

    await waitFor(() => expect(screen.getByText(/Frasen du søker etter er brukt i/)).toBeTruthy());
    expect(screen.queryByTestId("search-activity")).toBeNull();
    expect(screen.getByRole("tab", { name: /^Innhold\s*\d+$/ })).toBeTruthy();
    expect(screen.queryByText(/Søker i innholdet/)).toBeNull();
  });

  it("outlines the search field and counts dots after the status while the corpus is indexing, with no spinner", async () => {
    getBrevkoderMedMetadata.queryFn.mockImplementation((malType: string) =>
      Promise.resolve(malType === "autobrev" ? autobrevDescriptions : redigerbarDescriptions),
    );
    getAllTemplateDocumentation.queryFn.mockReturnValue(new Promise(() => undefined));

    await renderTemplatesRoute();

    const status = await screen.findByText("Klargjør malene for søk");
    expect(status.textContent).toBe("Klargjør malene for søk...");
    // The counting dots are decorative, so screen readers hear just the text.
    expect(status.querySelector('[aria-hidden="true"]')?.textContent).toBe("...");
    expect(screen.getByTestId("search-activity").getAttribute("aria-hidden")).toBe("true");
    expect(screen.queryByTitle(/Søker|Indekserer/)).toBeNull();
  });

  it("keeps the previous summary unchanged while the next search runs", async () => {
    mockCorpus();
    const client = installClient(deferrableClient());

    const { user } = await renderTemplatesRoute({ tab: "innhold" });
    await waitFor(() => expect(screen.queryByText(/Klargjør malene for søk/)).toBeNull());
    await user.type(screen.getByRole("searchbox"), "Hei");
    await act(async () => client.release());
    await waitFor(() => expect(screen.getByText(/Frasen du søker etter er brukt i/)).toBeTruthy());

    await user.type(screen.getByRole("searchbox"), "{Backspace}");

    await waitFor(() => expect(screen.getByTestId("search-activity")).toBeTruthy());
    expect(screen.getByText(/Frasen du søker etter er brukt i/)).toBeTruthy();
    expect(screen.queryByText(/Søker i innholdet/)).toBeNull();
  });

  it("reports zero hits in the status line, leaving the results empty", async () => {
    mockCorpus();

    const { user } = await renderTemplatesRoute();
    await waitFor(() => expect(screen.queryByText(/Klargjør malene for søk/)).toBeNull());
    await user.type(screen.getByRole("searchbox"), "xyzzy");

    await waitFor(() => expect(screen.getByText("Ingen treff i tittel, navn eller brevkode")).toBeTruthy());
    expect(screen.getByRole("tab", { name: /^Brev\s*0$/ })).toBeTruthy();
    expect(screen.queryByRole("heading", { name: "Automatiske brev" })).toBeNull();

    await user.click(screen.getByRole("tab", { name: /^Innhold\s*0$/ }));
    expect(screen.getByText("Ingen treff i innholdet")).toBeTruthy();
  });

  // Replacing the browsable index with "no hits" before the search has
  // answered would claim a result nobody has seen yet.
  it("keeps the full template index on the Brev tab until the first search has results", async () => {
    mockCorpus();
    const client = installClient(deferrableClient());

    const { user } = await renderTemplatesRoute();
    await waitFor(() => expect(screen.queryByText(/Klargjør malene for søk/)).toBeNull());
    expect(screen.getByRole("tab", { name: "Brev" }).getAttribute("aria-selected")).toBe("true");
    expect(screen.getByRole("heading", { name: "Automatiske brev" })).toBeTruthy();

    await user.type(screen.getByRole("searchbox"), "Hei");
    await waitFor(() => expect(screen.getByTestId("search-activity")).toBeTruthy());
    expect(screen.getByRole("heading", { name: "Automatiske brev" })).toBeTruthy();

    await act(async () => client.release());
    await waitFor(() => expect(screen.queryByRole("heading", { name: "Automatiske brev" })).toBeNull());
  });

  it("tells the user when a search fails, instead of spinning forever or reporting no hits", async () => {
    mockCorpus();
    const client = installClient(deferrableClient());

    const { user } = await renderTemplatesRoute();
    await waitFor(() => expect(screen.queryByText(/Klargjør malene for søk/)).toBeNull());
    await user.type(screen.getByRole("searchbox"), "Hei");
    await waitFor(() => expect(screen.getByTestId("search-activity")).toBeTruthy());

    await act(async () => client.fail());

    await waitFor(() => expect(screen.getByText(/Søket kunne ikke gjennomføres/)).toBeTruthy());
    expect(screen.queryByTestId("search-activity")).toBeNull();
    expect(screen.queryByText("Ingen treff i innholdet")).toBeNull();
  });

  // A page belongs to the search it was chosen in: a new search, or another
  // tab, starts over at page 1 rather than landing mid-way into unrelated hits.
  it("returns to page 1 when a new search lands and when switching tabs", async () => {
    const many = Array.from({ length: 12 }, (_, i) => `A${String(i).padStart(2, "0")}`);
    getBrevkoderMedMetadata.queryFn.mockImplementation((malType: string) =>
      Promise.resolve(
        malType === "autobrev"
          ? many.map((name) => ({ name, letterDataClass: "AutobrevDto", languages: ["BOKMAL"] }))
          : [],
      ),
    );
    getAllTemplateDocumentation.queryFn.mockImplementation((malType: string) =>
      Promise.resolve(
        malType === "autobrev"
          ? many.map((brevkode) => ({
              brevkode,
              language: "BOKMAL",
              lines: [{ index: 0, segments: [{ type: "text", value: "Hei" }] }],
            }))
          : [],
      ),
    );
    const isCurrent = (page: string) => screen.getByRole("button", { name: page }).getAttribute("aria-current");

    const { user } = await renderTemplatesRoute({ tab: "innhold" });
    await waitFor(() => expect(screen.queryByText(/Klargjør malene for søk/)).toBeNull());
    await user.type(screen.getByRole("searchbox"), "Hei");
    await waitFor(() => expect(screen.getByRole("button", { name: "2" })).toBeTruthy());

    await user.click(screen.getByRole("button", { name: "2" }));
    expect(isCurrent("2")).toBe("true");
    await user.type(screen.getByRole("searchbox"), "{Backspace}");
    await waitFor(() => expect(isCurrent("1")).toBe("true"));

    await user.click(screen.getByRole("button", { name: "2" }));
    expect(isCurrent("2")).toBe("true");
    await user.click(screen.getByRole("tab", { name: /^Brev/ }));
    await user.click(screen.getByRole("tab", { name: /^Innhold/ }));
    expect(isCurrent("1")).toBe("true");
  });

  it("does not render a min-length hint or a full-page loader", async () => {
    mockCorpus();

    await renderTemplatesRoute();

    expect(screen.queryByText(/Skriv minst/i)).toBeNull();
    expect(screen.queryByTitle("Henter maler")).toBeNull();
  });

  describe("search context in the URL", () => {
    const many = Array.from({ length: 12 }, (_, i) => `A${String(i).padStart(2, "0")}`);
    function mockManyHits() {
      getBrevkoderMedMetadata.queryFn.mockImplementation((malType: string) =>
        Promise.resolve(
          malType === "autobrev"
            ? many.map((name) => ({ name, letterDataClass: "AutobrevDto", languages: ["BOKMAL"] }))
            : [],
        ),
      );
      getAllTemplateDocumentation.queryFn.mockImplementation((malType: string) =>
        Promise.resolve(
          malType === "autobrev"
            ? many.map((brevkode) => ({
                brevkode,
                language: "BOKMAL",
                lines: [{ index: 7, segments: [{ type: "text", value: "Hei" }] }],
              }))
            : [],
        ),
      );
    }
    const isCurrentPage = (page: string) =>
      screen.getByRole("button", { name: page }).getAttribute("aria-current") === "true";

    it("restores the query, match mode and tab from the URL", async () => {
      mockCorpus();

      await renderTemplatesRoute({ q: "A1", exact: true, tab: "innhold" });

      expect(screen.getByRole<HTMLInputElement>("searchbox").value).toBe("A1");
      expect(screen.getByRole<HTMLInputElement>("checkbox", { name: "Vis kun nøyaktige treff" }).checked).toBe(true);
      expect(screen.getByRole("tab", { name: /^Innhold/ }).getAttribute("aria-selected")).toBe("true");
      expect(await screen.findByText("Ingen treff i innholdet")).toBeTruthy();
    });

    it("writes the settled query to the URL without adding history entries", async () => {
      mockCorpus();
      const { user, router } = await renderTemplatesRoute();
      const historyLength = globalThis.history.length;

      await user.type(screen.getByRole("searchbox"), "Hei");
      await user.click(screen.getByRole("checkbox", { name: "Vis kun nøyaktige treff" }));

      await waitFor(() => expect(router.state.location.search).toMatchObject({ q: "Hei", exact: true }));
      expect(globalThis.history.length).toBe(historyLength);
    });

    it("opens at the page showing the selected result, and marks it", async () => {
      mockManyHits();

      await renderTemplatesRoute({ q: "Hei", tab: "innhold", selected: "autobrev/A11/BOKMAL" });

      await waitFor(() => expect(isCurrentPage("2")).toBe(true));
      const link = screen.getByRole("link", { name: "A11" });
      expect(link.getAttribute("aria-current")).toBe("true");
      expect(link.closest("[data-selected-result]")).not.toBeNull();
      expect(document.querySelectorAll("[data-selected-result]")).toHaveLength(1);
    });

    it("starts a new search on page 1 with nothing selected", async () => {
      mockManyHits();
      const { user, router } = await renderTemplatesRoute({
        q: "Hei",
        tab: "innhold",
        selected: "autobrev/A11/BOKMAL",
      });
      await waitFor(() => expect(isCurrentPage("2")).toBe(true));

      await user.type(screen.getByRole("searchbox"), "{Backspace}");

      await waitFor(() => expect(router.state.location.search).toEqual({ q: "He", tab: "innhold" }));
      expect(document.querySelector("[data-selected-result]")).toBeNull();
    });

    it("links each result with the search, and records the opened one so Back returns to it", async () => {
      mockManyHits();
      const { user, router } = await renderTemplatesRoute({ q: "Hei", tab: "innhold" });
      const link = await screen.findByRole("link", { name: "A00" });

      const href = new URL(link.getAttribute("href") ?? "", "http://localhost");
      expect(href.pathname).toBe("/template/autobrev/A00");
      expect(Object.fromEntries(href.searchParams)).toEqual({
        q: "Hei",
        tab: "innhold",
        language: "BOKMAL",
        focus: "7",
      });

      await user.click(link);
      await screen.findByText("Malsiden");

      act(() => router.history.back());
      await waitFor(() => expect(router.state.location.pathname).toBe("/templates"));
      expect(router.state.location.search).toEqual({ q: "Hei", tab: "innhold", selected: "autobrev/A00/BOKMAL" });
      await waitFor(() => expect(screen.getByRole("link", { name: "A00" }).getAttribute("aria-current")).toBe("true"));
    });

    it("opens a Brev result without a block, and Back returns to the Brev tab with it marked", async () => {
      mockManyHits();
      const { user, router } = await renderTemplatesRoute({ q: "A00" });
      await screen.findByText(/Søket traff tittel, navn eller brevkode/);
      const link = screen.getByRole("link", { name: "A00" });

      const href = new URL(link.getAttribute("href") ?? "", "http://localhost");
      expect(Object.fromEntries(href.searchParams)).toEqual({ q: "A00", language: "BOKMAL", showHits: "false" });

      await user.click(link);
      await screen.findByText("Malsiden");

      act(() => router.history.back());
      await waitFor(() => expect(router.state.location.pathname).toBe("/templates"));
      expect(router.state.location.search).toEqual({ q: "A00", selected: "autobrev/A00/BOKMAL" });
      expect(screen.getByRole("tab", { name: /^Brev/ }).getAttribute("aria-selected")).toBe("true");
      await waitFor(() => expect(screen.getByRole("link", { name: "A00" }).getAttribute("aria-current")).toBe("true"));
    });

    // The hits on screen belong to the last completed search, which lags the
    // input while a newer one runs. Back must return to the search the opened
    // result was found in, as "Tilbake til søkeresultatene" does.
    it("records the search an opened result was found in, also while a newer search runs", async () => {
      mockCorpus();
      const client = installClient(deferrableClient());
      const { user, router } = await renderTemplatesRoute({ tab: "innhold" });
      await waitFor(() => expect(screen.queryByText(/Klargjør malene for søk/)).toBeNull());
      await user.type(screen.getByRole("searchbox"), "Hei");
      await act(async () => client.release());
      const [link] = await screen.findAllByRole("link", { name: "A1" });
      const malType = new URL(link.getAttribute("href") ?? "", "http://localhost").pathname.split("/")[2];
      await user.type(screen.getByRole("searchbox"), "xyz");
      await waitFor(() => expect(router.state.location.search).toMatchObject({ q: "Heixyz" }));

      await user.click(link);
      await screen.findByText("Malsiden");
      expect(router.state.location.search).toMatchObject({ q: "Hei", tab: "innhold" });

      act(() => router.history.back());
      await waitFor(() => expect(router.state.location.pathname).toBe("/templates"));
      expect(router.state.location.search).toEqual({ q: "Hei", tab: "innhold", selected: `${malType}/A1/BOKMAL` });
    });
  });
});
