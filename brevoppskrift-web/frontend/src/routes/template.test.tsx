import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { createMemoryHistory, createRootRouteWithContext, createRouter, RouterProvider } from "@tanstack/react-router";
import { act, cleanup, fireEvent, render, screen, waitFor } from "@testing-library/react";
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
  let state: unknown = {
    isLoading: true,
    displayed: undefined,
    searchFailed: false,
    failedMalTypes: [],
    retryFailed: vi.fn(),
  };
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
  const actual = await vi.importActual<typeof import("~/search/useSearchResults")>("~/search/useSearchResults");
  return { ...actual, useSearchResults: () => useSyncExternalStore(searchStore.subscribe, searchStore.get) };
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
  const conditionalPage = {
    ...page,
    outline: [
      {
        controlStructureType: "CONTENT",
        content: {
          elementType: "PARAGRAPH",
          index: 20,
          paragraph: [
            {
              controlStructureType: "CONDITIONAL",
              showIf: [
                {
                  controlStructureType: "CONTENT",
                  content: { elementType: "PARAGRAPH_TEXT_LITERAL", text: "Ingen samsvar i denne grenen" },
                },
              ],
              elseIf: [],
              showElse: [
                {
                  controlStructureType: "CONTENT",
                  content: { elementType: "PARAGRAPH_TEXT_LITERAL", text: "Treff i denne grenen" },
                },
              ],
            },
          ],
        },
      },
    ],
  } as unknown as TemplateDocumentation;

  function text(language: string): TemplateText {
    return { malType: "autobrev", id: "A", title: "A", language, lines: [[], [], []], indexes: [10, 20, 30] };
  }
  /** Hits in blocks 20 and 30 of the Bokmål version only. */
  const loaded = {
    isLoading: false,
    searchFailed: false,
    failedMalTypes: [],
    retryFailed: vi.fn(),
    displayed: {
      highlight: { needle: "treff", exactOnly: false },
      incomplete: false,
      contentHits: [{ template: text("BOKMAL"), lineIndex: 1, matchCount: 2, matchedLineIndexes: [1, 2], score: 0 }],
      brevHits: [{ template: text("BOKMAL") }],
      contentTemplateCount: 1,
      contentLineCount: 2,
      brevTemplateCount: 1,
    } satisfies DisplayedSearch,
  };
  const loading = {
    isLoading: true,
    displayed: undefined,
    searchFailed: false,
    failedMalTypes: [],
    retryFailed: vi.fn(),
  };

  const scrolledTo: (string | null)[] = [];
  const scrolledText: string[] = [];
  const block = (index: number) => document.querySelector(`[data-block-index="${index}"]`) as HTMLElement;
  function openPage(
    search: Record<string, unknown>,
    {
      languages = ["BOKMAL"],
      hits = loaded as unknown,
      documentation = page,
    }: { languages?: string[]; hits?: unknown; documentation?: TemplateDocumentation } = {},
  ) {
    getTemplateDescription.queryFn.mockResolvedValue({ languages });
    getBrevkoderMedMetadata.queryFn.mockResolvedValue([]);
    getTemplateDocumentation.queryFn.mockResolvedValue(documentation);
    searchStore.set(hits);
    scrolledTo.length = 0;
    scrolledText.length = 0;
    Element.prototype.scrollIntoView = function (this: Element) {
      scrolledTo.push(this.getAttribute("data-block-index"));
      scrolledText.push(this.textContent ?? "");
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
    const router = await openPage({ showHits: false });
    const toggle = await screen.findByLabelText<HTMLInputElement>("Vis søketreff");
    expect(toggle.checked).toBe(false);
    expect(scrolledTo).toEqual([]);

    await userEvent.click(toggle);

    await waitFor(() => expect(scrolledTo).toEqual(["20"]));
    expect(screen.getByText("Avsnitt 1 av 2")).toBeTruthy();
    expect(router.state.location.search).not.toHaveProperty("showHits");
  });

  it("scrolls to the conditional branch containing the highlighted text", async () => {
    await openPage({ tab: "innhold" }, { documentation: conditionalPage });

    await waitFor(() => expect(scrolledTo).toEqual(["20"]));
    const blocks = document.querySelectorAll('[data-block-index="20"]');
    expect(blocks).toHaveLength(2);
    expect(blocks[0].querySelector("mark")).toBeNull();
    expect(blocks[1].querySelector("mark")).not.toBeNull();
    expect(scrolledText[0]).toContain("Treff i denne grenen");
  });

  it("scrolls again when a new query's first hit has the same block index", async () => {
    const router = await openPage({ tab: "innhold" });
    await waitFor(() => expect(scrolledTo).toEqual(["20"]));
    const updated = {
      ...loaded,
      displayed: {
        ...loaded.displayed,
        highlight: { needle: "annet", exactOnly: false },
      },
    };

    await act(async () => {
      // biome-ignore lint/suspicious/noExplicitAny: the standalone test router is untyped.
      await router.navigate({ search: (search: Record<string, unknown>) => ({ ...search, q: "annet" }) } as any);
      searchStore.set(updated);
    });

    await waitFor(() => expect(scrolledTo).toEqual(["20", "20"]));
  });

  it("focuses the first hit once loaded, when the hits were turned on while loading", async () => {
    await openPage({ showHits: false }, { hits: loading });
    await userEvent.click(await screen.findByLabelText("Vis søketreff"));

    act(() => searchStore.set(loaded));

    await waitFor(() => expect(scrolledTo).toEqual(["20"]));
    expect(screen.getByText("Avsnitt 1 av 2")).toBeTruthy();
  });

  it("outlines and scrolls to the block of a paragraph link without a search", async () => {
    await openPage({ q: undefined, focus: 21 });

    await waitFor(() => expect(scrolledTo).toEqual(["20"]));
    expect(block(20).getAttribute("data-search-hit")).toBe("active");
    expect(screen.queryByText(/Finner søketreff/)).toBeNull();
  });

  it("does not search for a query too short to search, rather than wait for it", async () => {
    await openPage({ q: "x" }, { hits: loading });

    expect(await screen.findByText("Mal-oversikt")).toBeTruthy();
    expect(screen.queryByText(/Finner søketreff/)).toBeNull();
  });

  it("goes back to the active hit when the hits are turned off and on again", async () => {
    const router = await openPage({ tab: "innhold", focus: 31 });
    await waitFor(() => expect(scrolledTo).toEqual(["30"]));
    const toggle = screen.getByLabelText<HTMLInputElement>("Vis søketreff");

    await userEvent.click(toggle);
    await waitFor(() => expect(router.state.location.search).toMatchObject({ focus: 31, showHits: false }));
    await userEvent.click(toggle);

    await waitFor(() => expect(scrolledTo).toEqual(["30", "30"]));
  });

  it("keeps the focused block when switching to a language where it is no hit", async () => {
    const router = await openPage({ tab: "innhold" }, { languages: ["BOKMAL", "NYNORSK"] });
    await waitFor(() => expect(scrolledTo).toEqual(["20"]));

    await userEvent.click(screen.getByRole("radio", { name: "Nynorsk" }));

    await waitFor(() => expect(router.state.location.search).toMatchObject({ language: "NYNORSK", focus: 21 }));
    await waitFor(() =>
      expect(document.querySelector('[data-block-index="20"]')?.getAttribute("data-search-hit")).toBe("active"),
    );
    expect(screen.getByText("Ingen treff i denne språkversjonen")).toBeTruthy();
  });

  it("focuses a hit the user clicks in, so a copied link opens at it, without scrolling", async () => {
    const router = await openPage({ tab: "innhold" });
    await waitFor(() => expect(scrolledTo).toEqual(["20"]));

    await userEvent.click(block(30));

    await waitFor(() => expect(router.state.location.search).toMatchObject({ focus: 31 }));
    expect(screen.getByText("Avsnitt 2 av 2")).toBeTruthy();
    expect(document.querySelector('[data-block-index="30"]')?.getAttribute("data-search-hit")).toBe("active");
    await act(() => new Promise((resolve) => requestAnimationFrame(resolve)));
    expect(scrolledTo).toEqual(["20"]);
  });

  it("focuses a hit when keyboard focus moves into it", async () => {
    const router = await openPage({ tab: "innhold" });
    await waitFor(() => expect(screen.getByText("Avsnitt 1 av 2")).toBeTruthy());

    fireEvent.focusIn(block(30));

    await waitFor(() => expect(router.state.location.search).toMatchObject({ focus: 31 }));
  });

  it("ignores clicks outside the hits, and all clicks while the hits are hidden", async () => {
    const router = await openPage({ tab: "innhold" });
    await waitFor(() => expect(screen.getByText("Avsnitt 1 av 2")).toBeTruthy());

    await userEvent.click(screen.getByText("Innledning"));
    await userEvent.click(screen.getByLabelText("Vis søketreff"));
    await userEvent.click(block(30));

    await waitFor(() => expect(router.state.location.search).toMatchObject({ showHits: false }));
    expect(router.state.location.search).not.toHaveProperty("focus");
  });

  it("opens a shared link as it was shared, with the hits hidden or shown", async () => {
    await openPage({ showHits: false });
    expect((await screen.findByLabelText<HTMLInputElement>("Vis søketreff")).checked).toBe(false);
    expect(document.querySelector("[data-search-hit]")).toBeNull();
    cleanup();

    // Turned on after arriving from the Brev tab.
    await openPage({ focus: 31 });
    expect((await screen.findByLabelText<HTMLInputElement>("Vis søketreff")).checked).toBe(true);
    await waitFor(() => expect(scrolledTo).toEqual(["30"]));
  });

  describe("the toolbar's search box", () => {
    const searchBox = () => screen.findByLabelText<HTMLInputElement>("Søk etter innhold");

    it("searches the content from a template opened without a search", async () => {
      const router = await openPage({ q: undefined, focus: 21 });
      expect((await searchBox()).value).toBe("");

      await userEvent.type(await searchBox(), "treff");

      await waitFor(() => expect(router.state.location.search).toMatchObject({ q: "treff", tab: "innhold" }));
      expect(router.state.location.search).not.toHaveProperty("focus");
      await waitFor(() => expect(screen.getByText("Avsnitt 1 av 2")).toBeTruthy());
    });

    it("replaces the search, keeping its tab and opening at the first hit, but not for a too short query", async () => {
      const router = await openPage({ q: "første", focus: 31 });
      const input = await searchBox();
      expect(input.value).toBe("første");

      await userEvent.type(input, "t", { initialSelectionStart: 0, initialSelectionEnd: input.value.length });
      await act(() => new Promise((resolve) => setTimeout(resolve, 0)));
      expect(router.state.location.search).toMatchObject({ q: "første" });

      await userEvent.type(input, "reff");
      await waitFor(() => expect(router.state.location.search).toMatchObject({ q: "treff" }));
      expect(router.state.location.search).not.toHaveProperty("tab");
      expect(router.state.location.search).not.toHaveProperty("focus");
    });

    it("drops the search context when cleared", async () => {
      const router = await openPage({ tab: "innhold", exact: true });

      await userEvent.clear(await searchBox());

      await waitFor(() => expect(router.state.location.search).not.toHaveProperty("q"));
      expect(router.state.location.search).not.toHaveProperty("tab");
      expect(router.state.location.search).not.toHaveProperty("exact");
      expect(screen.queryByText(/Mal \d+ av/)).toBeNull();
      // The box stays, so a new search can be typed.
      expect(await searchBox()).toBeTruthy();
    });

    it("is hidden while the hits are hidden", async () => {
      await openPage({ showHits: false });
      await userEvent.click(await screen.findByLabelText("Vis søketreff"));
      expect((await searchBox()).value).toBe("treff");

      await userEvent.click(screen.getByLabelText("Vis søketreff"));

      await waitFor(() => expect(screen.queryByLabelText("Søk etter innhold")).toBeNull());
    });

    it("follows the URL when the query changes elsewhere", async () => {
      const router = await openPage({});
      expect((await searchBox()).value).toBe("treff");

      // biome-ignore lint/suspicious/noExplicitAny: the test router is untyped.
      await act(() => router.navigate({ to: ".", search: (s: any) => ({ ...s, q: "annet" }) } as any));

      await waitFor(async () => expect((await searchBox()).value).toBe("annet"));
    });
  });
});

describe("search hits when the template list cannot be fetched", () => {
  afterEach(() => {
    cleanup();
    vi.resetAllMocks();
  });

  it("offers a retry when template metadata fails, instead of searching forever", async () => {
    getTemplateDescription.queryFn.mockResolvedValue({ languages: ["BOKMAL"] });
    getTemplateDocumentation.queryFn.mockResolvedValue({
      title: [],
      outline: [],
      attachments: [],
      templateModelSpecification: { types: {}, letterModelTypeName: "" },
    });
    let redigerbarCalls = 0;
    getBrevkoderMedMetadata.queryFn.mockImplementation((malType: string) => {
      if (malType === "redigerbar" && redigerbarCalls++ === 0) {
        return Promise.reject(new Error("boom"));
      }
      return Promise.resolve([]);
    });

    await renderTemplateRoute("/template/autobrev/A", { language: "BOKMAL", q: "treff" }, { realPage: true });

    expect(await screen.findByText(/Kan ikke vise treff/)).toBeTruthy();
    await userEvent.click(screen.getByRole("button", { name: "Prøv igjen" }));
    await waitFor(() => expect(screen.queryByText(/Kan ikke vise treff/)).toBeNull());
    expect(redigerbarCalls).toBe(2);
  });
});
