import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { createMemoryHistory, createRootRouteWithContext, createRouter, RouterProvider } from "@tanstack/react-router";
import { act, cleanup, render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { AxiosError, AxiosHeaders } from "axios";
import { afterEach, describe, expect, it, vi } from "vitest";

import { type TemplateDocumentation } from "~/api/templateDocumentationTypes";
import { Route as TemplateRouteImport } from "~/routes/template.$malType.$templateId";
import { type TemplateText } from "~/search/textSearch";
import { type DisplayedSearch } from "~/search/useSearchResults";

// The search is replaced by a store the tests set, so the real hit logic runs
// on top of it, and a test can finish loading while the page is open.
const searchStore = vi.hoisted(() => {
  let state: unknown = { isLoading: true, displayed: undefined, searchFailed: false };
  const listeners = new Set<() => void>();
  return {
    get: () => state,
    set(next: unknown) {
      state = next;
      for (const listener of listeners) {
        listener();
      }
    },
    subscribe(listener: () => void) {
      listeners.add(listener);
      return () => listeners.delete(listener);
    },
  };
});
vi.mock("~/search/useSearchResults", async () => {
  const { useSyncExternalStore } = await import("react");
  return { useSearchResults: () => useSyncExternalStore(searchStore.subscribe, searchStore.get) };
});

const { getBrevkoderMedMetadata, getTemplateDescription, getTemplateDocumentation } = vi.hoisted(() => ({
  getBrevkoderMedMetadata: {
    queryKey: (malType: string) => ["BREVKODER", malType, "METADATA"] as const,
    queryFn: vi.fn(),
  },
  getTemplateDescription: {
    queryKey: (malType: string, templateId: string) => ["TEMPLATE_DESCRIPTION", malType, templateId] as const,
    queryFn: vi.fn(),
  },
  getTemplateDocumentation: {
    queryKey: (malType: string, templateId: string, language: string) =>
      ["TEMPLATE_DOCUMENTATION", malType, templateId, language] as const,
    queryFn: vi.fn(),
  },
}));

vi.mock("~/api/brevbaker-api-endpoints", async () => {
  const actual = await vi.importActual<typeof import("~/api/brevbaker-api-endpoints")>("~/api/brevbaker-api-endpoints");
  return { ...actual, getBrevkoderMedMetadata, getTemplateDescription, getTemplateDocumentation };
});

// `Route.update` changes the shared route in place, so the real page is kept
// before any test swaps in the stub.
const realComponent = TemplateRouteImport.options.component;

const documentation = { title: [], outline: [], attachments: [] } as unknown as TemplateDocumentation;

function notFoundError() {
  return new AxiosError("Not Found", "ERR_BAD_REQUEST", undefined, undefined, {
    status: 404,
    statusText: "Not Found",
    data: undefined,
    headers: {},
    config: { headers: new AxiosHeaders() },
  });
}

/** Mounts the real template route's loader and search validation. By default
 *  with a stub page in place of the (heavy) documentation view. */
async function renderTemplateRoute(path: string, search: Record<string, unknown> = {}, { realPage = false } = {}) {
  const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });
  const rootRoute = createRootRouteWithContext<{ queryClient: QueryClient }>()({});
  const templateRoute = TemplateRouteImport.update({
    id: "/template/$malType/$templateId",
    path: "/template/$malType/$templateId",
    getParentRoute: () => rootRoute,
    component: realPage ? realComponent : () => <p>Malsiden</p>,
    // biome-ignore lint/suspicious/noExplicitAny: mirrors the `as any` cast routeTree.gen.ts uses for this same `.update(...)` call.
  } as any);
  const router = createRouter({
    routeTree: rootRoute.addChildren([templateRoute]),
    context: { queryClient },
    history: createMemoryHistory({ initialEntries: ["/"] }),
    defaultNotFoundComponent: () => <p>Fant ikke siden</p>,
  });
  render(
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
    </QueryClientProvider>,
  );
  // biome-ignore lint/suspicious/noExplicitAny: the test router is untyped, like the route it mounts.
  await router.navigate({ to: path, search } as any);
  return router;
}

describe("template route with incorrect URL values", () => {
  afterEach(() => {
    cleanup();
    vi.resetAllMocks();
  });

  it("opens the default language, keeping the search, when the language is unknown", async () => {
    getTemplateDescription.queryFn.mockResolvedValue({ languages: ["NYNORSK", "BOKMAL"] });
    getTemplateDocumentation.queryFn.mockResolvedValue(documentation);

    const router = await renderTemplateRoute("/template/autobrev/A", { language: "SAMISK", q: "pensjon" });

    await screen.findByText("Malsiden");
    expect(router.state.location.search).toMatchObject({ language: "NYNORSK", q: "pensjon" });
    expect(getTemplateDocumentation.queryFn).not.toHaveBeenCalledWith("autobrev", "A", "SAMISK");
  });

  it("shows the not-found page for an unknown template type", async () => {
    await renderTemplateRoute("/template/ukjent/A", { language: "BOKMAL" });

    await screen.findByText("Fant ikke siden");
    expect(getTemplateDescription.queryFn).not.toHaveBeenCalled();
  });

  it("shows the not-found page for a template that does not exist", async () => {
    getTemplateDescription.queryFn.mockRejectedValue(notFoundError());

    await renderTemplateRoute("/template/autobrev/FINNES_IKKE", { language: "BOKMAL" });

    await waitFor(() => expect(screen.getByText("Fant ikke siden")).toBeTruthy());
  });
});

describe("search hit focus", () => {
  function paragraph(index: number, text: string) {
    return {
      controlStructureType: "CONTENT",
      content: {
        elementType: "PARAGRAPH",
        index,
        paragraph: [{ controlStructureType: "CONTENT", content: { elementType: "PARAGRAPH_TEXT_LITERAL", text } }],
      },
    };
  }
  const page = {
    title: [],
    outline: [paragraph(10, "Innledning"), paragraph(20, "Første treff"), paragraph(30, "Andre treff")],
    attachments: [],
    templateModelSpecification: { types: {}, letterModelTypeName: "" },
  } as unknown as TemplateDocumentation;

  function text(language: string): TemplateText {
    return { malType: "autobrev", id: "A", title: "A", language, lines: [[], [], []], indexes: [10, 20, 30] };
  }
  /** Hits in blocks 20 and 30 of the Bokmål version only. */
  const loaded = {
    isLoading: false,
    searchFailed: false,
    displayed: {
      highlight: { needle: "treff", exactOnly: false },
      contentHits: [{ template: text("BOKMAL"), lineIndex: 1, matchCount: 2, matchedLineIndexes: [1, 2], score: 0 }],
      brevHits: [{ template: text("BOKMAL") }],
      contentTemplateCount: 1,
      contentLineCount: 2,
      brevTemplateCount: 1,
    } satisfies DisplayedSearch,
  };
  const loading = { isLoading: true, displayed: undefined, searchFailed: false };

  const scrolledTo: (string | null)[] = [];
  function openPage(search: Record<string, unknown>, { languages = ["BOKMAL"], hits = loaded as unknown } = {}) {
    getTemplateDescription.queryFn.mockResolvedValue({ languages });
    getBrevkoderMedMetadata.queryFn.mockResolvedValue([]);
    getTemplateDocumentation.queryFn.mockResolvedValue(page);
    searchStore.set(hits);
    scrolledTo.length = 0;
    Element.prototype.scrollIntoView = function (this: Element) {
      scrolledTo.push(this.getAttribute("data-block-index"));
    };
    return renderTemplateRoute(
      "/template/autobrev/A",
      { language: "BOKMAL", q: "treff", ...search },
      { realPage: true },
    );
  }

  afterEach(() => {
    cleanup();
    vi.resetAllMocks();
  });

  it("focuses the first hit when the hits are turned on, e.g. opened from the Brev tab", async () => {
    await openPage({});
    const toggle = await screen.findByLabelText<HTMLInputElement>("Vis søketreff");
    expect(toggle.checked).toBe(false);
    expect(scrolledTo).toEqual([]);

    await userEvent.click(toggle);

    await waitFor(() => expect(scrolledTo).toEqual(["20"]));
    expect(screen.getByText("Avsnitt 1 av 2")).toBeTruthy();
  });

  it("focuses the first hit once loaded, when the hits were turned on while loading", async () => {
    await openPage({}, { hits: loading });
    await userEvent.click(await screen.findByLabelText("Vis søketreff"));

    act(() => searchStore.set(loaded));

    await waitFor(() => expect(scrolledTo).toEqual(["20"]));
    expect(screen.getByText("Avsnitt 1 av 2")).toBeTruthy();
  });

  it("goes back to the active hit when the hits are turned off and on again", async () => {
    await openPage({ tab: "innhold", index: 30 });
    await waitFor(() => expect(scrolledTo).toEqual(["30"]));
    const toggle = screen.getByLabelText<HTMLInputElement>("Vis søketreff");

    await userEvent.click(toggle);
    await userEvent.click(toggle);

    await waitFor(() => expect(scrolledTo).toEqual(["30", "30"]));
  });

  it("keeps the focused block when switching to a language where it is no hit", async () => {
    const router = await openPage({ tab: "innhold" }, { languages: ["BOKMAL", "NYNORSK"] });
    await waitFor(() => expect(scrolledTo).toEqual(["20"]));

    await userEvent.click(screen.getByRole("radio", { name: "Nynorsk" }));

    await waitFor(() => expect(router.state.location.search).toMatchObject({ language: "NYNORSK", index: 20 }));
    await waitFor(() =>
      expect(document.querySelector('[data-block-index="20"]')?.getAttribute("data-search-hit")).toBe("active"),
    );
    expect(screen.getByText("Ingen treff i denne språkversjonen")).toBeTruthy();
  });
});

describe("search hits when the template list cannot be fetched", () => {
  afterEach(() => {
    cleanup();
    vi.resetAllMocks();
  });

  it("says the hits could not be found, instead of searching forever", async () => {
    getTemplateDescription.queryFn.mockResolvedValue({ languages: ["BOKMAL"] });
    getTemplateDocumentation.queryFn.mockResolvedValue({
      title: [],
      outline: [],
      attachments: [],
      templateModelSpecification: { types: {}, letterModelTypeName: "" },
    });
    getBrevkoderMedMetadata.queryFn.mockImplementation((malType: string) =>
      malType === "redigerbar" ? Promise.reject(new Error("boom")) : Promise.resolve([]),
    );

    await renderTemplateRoute("/template/autobrev/A", { language: "BOKMAL", q: "treff" }, { realPage: true });

    expect(await screen.findByText(/Kunne ikke finne søketreffene/)).toBeTruthy();
  });
});
