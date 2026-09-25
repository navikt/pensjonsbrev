import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { createRootRouteWithContext, createRouter, RouterProvider } from "@tanstack/react-router";
import { act, render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";

import { type SearchableContent } from "~/api/brevbaker-api-endpoints";
import { type TemplateDescription } from "~/api/brevbakerTypes";
import { Route as TemplatesRouteImport } from "~/routes/templates";
import { deferrableClient } from "~/search/deferrableClient.testutil";
import { createLocalSearchClient, type SearchClient } from "~/search/searchClient";

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

/** Mounts the real `/templates` route standalone. `createFileRoute` leaves the
 *  exported `Route` unbound (no id/path/parent) — the app only wires those via
 *  the codegen'd `routeTree.gen.ts` (`TemplatesRouteImport.update({ id, path,
 *  getParentRoute })`). We mirror that exact wiring here instead of using
 *  `Route` as-is, otherwise the router throws
 *  "Duplicate routes found with id: __root__". */
async function renderTemplatesRoute() {
  const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });
  const rootRoute = createRootRouteWithContext<{ queryClient: QueryClient }>()({});
  const templatesRoute = TemplatesRouteImport.update({
    id: "/templates",
    path: "/templates",
    getParentRoute: () => rootRoute,
    // biome-ignore lint/suspicious/noExplicitAny: mirrors the `as any` cast routeTree.gen.ts uses for this same `.update(...)` call.
  } as any);
  const router = createRouter({
    routeTree: rootRoute.addChildren([templatesRoute]),
    context: { queryClient },
    history: undefined,
  });
  await router.navigate({ to: "/templates" });
  await router.load();

  const result = render(
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
    </QueryClientProvider>,
  );

  await waitFor(() => {
    expect(result.container.innerHTML).not.toBe("<div></div>");
  });

  return { user: userEvent.setup(), ...result };
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

  it("shows a spinner next to the search field only while a search runs, then the hit summary", async () => {
    mockCorpus();
    const client = installClient(deferrableClient());

    const { user } = await renderTemplatesRoute();
    await waitFor(() => expect(screen.queryByText(/Indekserer innhold/)).toBeNull());
    // An empty box: nothing in flight, nothing to summarise.
    expect(screen.queryByTitle("Søker")).toBeNull();
    expect(screen.queryByText(/Frasen du søker på/)).toBeNull();

    await user.type(screen.getByRole("searchbox"), "Hei");

    // First search running: the spinner, but no summary and no tab count yet.
    await waitFor(() => expect(screen.getByTitle("Søker")).toBeTruthy());
    expect(screen.queryByText(/Frasen du søker på/)).toBeNull();
    expect(screen.getByRole("tab", { name: "Innhold" })).toBeTruthy();

    await act(async () => client.release());

    await waitFor(() => expect(screen.getByText(/Frasen du søker på er brukt i/)).toBeTruthy());
    expect(screen.queryByTitle("Søker")).toBeNull();
    expect(screen.getByRole("tab", { name: /^Innhold\s*\d+$/ })).toBeTruthy();
    expect(screen.queryByText(/Søker i innholdet/)).toBeNull();
  });

  it("keeps the previous summary unchanged while the next search runs", async () => {
    mockCorpus();
    const client = installClient(deferrableClient());

    const { user } = await renderTemplatesRoute();
    await waitFor(() => expect(screen.queryByText(/Indekserer innhold/)).toBeNull());
    await user.type(screen.getByRole("searchbox"), "Hei");
    await act(async () => client.release());
    await waitFor(() => expect(screen.getByText(/Frasen du søker på er brukt i/)).toBeTruthy());

    await user.type(screen.getByRole("searchbox"), "{Backspace}");

    await waitFor(() => expect(screen.getByTitle("Søker")).toBeTruthy());
    expect(screen.getByText(/Frasen du søker på er brukt i/)).toBeTruthy();
    expect(screen.queryByText(/Søker i innholdet/)).toBeNull();
  });

  it("reports zero hits in the status line, leaving the results empty", async () => {
    mockCorpus();

    const { user } = await renderTemplatesRoute();
    await waitFor(() => expect(screen.queryByText(/Indekserer innhold/)).toBeNull());
    await user.type(screen.getByRole("searchbox"), "xyzzy");

    await waitFor(() => expect(screen.getByText("Ingen treff i innholdet")).toBeTruthy());
    expect(screen.getByRole("tab", { name: /^Innhold\s*0$/ })).toBeTruthy();

    await user.click(screen.getByRole("tab", { name: /^Brev\s*0$/ }));
    expect(screen.getByText("Ingen treff i tittel, navn eller brevkode")).toBeTruthy();
    expect(screen.queryByRole("heading", { name: "Automatiske brev" })).toBeNull();
  });

  // Replacing the browsable index with "no hits" before the search has
  // answered would claim a result nobody has seen yet.
  it("keeps the full template index on the Brev tab until the first search has results", async () => {
    mockCorpus();
    const client = installClient(deferrableClient());

    const { user } = await renderTemplatesRoute();
    await waitFor(() => expect(screen.queryByText(/Indekserer innhold/)).toBeNull());
    await user.click(screen.getByRole("tab", { name: "Brev" }));
    expect(screen.getByRole("heading", { name: "Automatiske brev" })).toBeTruthy();

    await user.type(screen.getByRole("searchbox"), "Hei");
    await waitFor(() => expect(screen.getByTitle("Søker")).toBeTruthy());
    expect(screen.getByRole("heading", { name: "Automatiske brev" })).toBeTruthy();

    await act(async () => client.release());
    await waitFor(() => expect(screen.queryByRole("heading", { name: "Automatiske brev" })).toBeNull());
  });

  it("tells the user when a search fails, instead of spinning forever or reporting no hits", async () => {
    mockCorpus();
    const client = installClient(deferrableClient());

    const { user } = await renderTemplatesRoute();
    await waitFor(() => expect(screen.queryByText(/Indekserer innhold/)).toBeNull());
    await user.type(screen.getByRole("searchbox"), "Hei");
    await waitFor(() => expect(screen.getByTitle("Søker")).toBeTruthy());

    await act(async () => client.fail());

    await waitFor(() => expect(screen.getByText(/Søket kunne ikke gjennomføres/)).toBeTruthy());
    expect(screen.queryByTitle("Søker")).toBeNull();
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

    const { user } = await renderTemplatesRoute();
    await waitFor(() => expect(screen.queryByText(/Indekserer innhold/)).toBeNull());
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
});
