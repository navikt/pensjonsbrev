import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { act, renderHook, waitFor } from "@testing-library/react";
import { type ReactNode } from "react";
import { afterEach, describe, expect, it, vi } from "vitest";

import { type SearchableContent } from "~/api/brevbaker-api-endpoints";
import { createLocalSearchClient, type SearchClient } from "~/search/searchClient";
import { type TemplateRef, useTemplateSearch } from "~/search/useTemplateSearch";

const { getAllTemplateDocumentation } = vi.hoisted(() => ({
  getAllTemplateDocumentation: {
    queryKey: (malType: string) => ["TEMPLATE_DOCUMENTATION", malType, "BATCH"] as const,
    queryFn: vi.fn(),
  },
}));

vi.mock("~/api/brevbaker-api-endpoints", async () => {
  const actual = await vi.importActual<typeof import("~/api/brevbaker-api-endpoints")>("~/api/brevbaker-api-endpoints");
  return { ...actual, getAllTemplateDocumentation };
});

/** Tracks how many `Fuse` instances have been constructed, so tests can assert
 *  whether toggling `fuzzy` triggers a (re)build of the underlying indexes. */
const fuseConstructions = vi.hoisted(() => ({ count: 0 }));
vi.mock("fuse.js", async (importOriginal) => {
  const actual = await importOriginal<typeof import("fuse.js")>();
  class TrackedFuse<T> extends actual.default<T> {
    constructor(...args: ConstructorParameters<typeof actual.default<T>>) {
      super(...args);
      fuseConstructions.count++;
    }
  }
  return { ...actual, default: TrackedFuse };
});

function wrapper(queryClient: QueryClient) {
  return function Wrapper({ children }: { children: ReactNode }) {
    return <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>;
  };
}

/** jsdom has no `Worker`, so the tests drive the hook with the in-process
 *  client. It runs the exact same `searchWorkerCore` the real worker does, so
 *  the `fuse.js` construction counting above still observes every index build -
 *  it just observes it synchronously. A fresh client per test keeps one test's
 *  corpus from leaking into the next. */
function renderSearch(queryClient: QueryClient, client: SearchClient = createLocalSearchClient()) {
  clients.push(client);
  return renderHook(() => useTemplateSearch(refs, client), { wrapper: wrapper(queryClient) });
}

const clients: SearchClient[] = [];

/** Wraps the in-process client so tests can hold a search open and inspect what
 *  the UI shows while the worker would still be busy. */
function deferrableClient(): SearchClient & { release: () => void } {
  const local = createLocalSearchClient();
  let pending: (() => void)[] = [];
  return {
    setCorpus: (corpus) => local.setCorpus(corpus),
    search: (query, exactOnly) =>
      new Promise((resolve) => {
        pending.push(() => void local.search(query, exactOnly).then(resolve));
      }),
    dispose: () => local.dispose(),
    release() {
      const released = pending;
      pending = [];
      for (const run of released) {
        run();
      }
    },
  };
}

const refs: TemplateRef[] = [
  { malType: "autobrev", brevkode: "A1", title: "Alderspensjon", languages: ["BOKMAL"] },
  { malType: "redigerbar", brevkode: "R1", title: "Uføretrygd", languages: ["BOKMAL"] },
];

const autobrevContent: SearchableContent[] = [
  { brevkode: "A1", language: "BOKMAL", lines: [{ index: 0, segments: [{ type: "text", value: "Hei" }] }] },
];

describe("useTemplateSearch", () => {
  afterEach(() => {
    getAllTemplateDocumentation.queryFn.mockReset();
    fuseConstructions.count = 0;
    for (const client of clients.splice(0)) {
      client.dispose();
    }
  });

  it("reports failedCount/failedMalTypes when one malType's corpus fetch fails, without leaving isLoading stuck", async () => {
    getAllTemplateDocumentation.queryFn.mockImplementation((malType: string) =>
      malType === "redigerbar" ? Promise.reject(new Error("boom")) : Promise.resolve(autobrevContent),
    );
    const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });

    const { result } = renderSearch(queryClient);

    await waitFor(() => {
      expect(result.current.isLoading).toBe(false);
      expect(result.current.failedCount).toBe(1);
    });
    expect(result.current.failedMalTypes).toEqual(["redigerbar"]);
  });

  it("does not report any failures when every malType's corpus fetch succeeds", async () => {
    getAllTemplateDocumentation.queryFn.mockResolvedValue(autobrevContent);
    const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });

    const { result } = renderSearch(queryClient);

    await waitFor(() => {
      expect(result.current.isLoading).toBe(false);
    });
    expect(result.current.failedCount).toBe(0);
    expect(result.current.failedMalTypes).toEqual([]);
  });

  it("retryFailed() re-fetches only the failing malType, clearing failedCount once it succeeds", async () => {
    getAllTemplateDocumentation.queryFn.mockImplementation((malType: string) =>
      malType === "redigerbar" ? Promise.reject(new Error("boom")) : Promise.resolve(autobrevContent),
    );
    const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });

    const { result } = renderSearch(queryClient);
    await waitFor(() => expect(result.current.failedCount).toBe(1));

    const callsBeforeRetry = getAllTemplateDocumentation.queryFn.mock.calls.length;
    getAllTemplateDocumentation.queryFn.mockImplementation(() => Promise.resolve(autobrevContent));
    result.current.retryFailed();

    await waitFor(() => {
      expect(result.current.failedCount).toBe(0);
    });
    expect(getAllTemplateDocumentation.queryFn.mock.calls.length).toBeGreaterThan(callsBeforeRetry);
  });

  it("defaults to fuzzy search enabled (exactOnly off), and setExactOnly(true) disables typo tolerance", async () => {
    const typoTolerantContent: SearchableContent[] = [
      {
        brevkode: "A1",
        language: "BOKMAL",
        lines: [{ index: 0, segments: [{ type: "text", value: "Vi har beregnet din alderspensjon" }] }],
      },
    ];
    getAllTemplateDocumentation.queryFn.mockImplementation((malType: string) =>
      Promise.resolve(malType === "autobrev" ? typoTolerantContent : []),
    );
    const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });

    const { result } = renderSearch(queryClient);
    await waitFor(() => expect(result.current.isLoading).toBe(false));

    expect(result.current.exactOnly).toBe(false);

    act(() => result.current.setQuery("alderspenjson")); // transposed letters
    await waitFor(() => expect(result.current.isSearching).toBe(true));
    await waitFor(() => expect(result.current.contentHits).toHaveLength(1));

    act(() => result.current.setExactOnly(true));
    await waitFor(() => expect(result.current.exactOnly).toBe(true));
    await waitFor(() => expect(result.current.contentHits).toHaveLength(0));
  });

  it("pre-builds one index pair per corpus, and toggling exactOnly never rebuilds it", async () => {
    getAllTemplateDocumentation.queryFn.mockImplementation((malType: string) =>
      Promise.resolve(malType === "autobrev" ? autobrevContent : []),
    );
    const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });

    const { result } = renderSearch(queryClient);
    await waitFor(() => expect(result.current.isLoading).toBe(false));
    const constructionsAfterLoad = fuseConstructions.count;
    // One content and one brev index are built only once the corpus is fully
    // loaded (no wasted builds off a partial corpus along the way).
    expect(constructionsAfterLoad).toBe(2);

    act(() => result.current.setExactOnly(true));
    await waitFor(() => expect(result.current.exactOnly).toBe(true));
    act(() => result.current.setExactOnly(false));
    await waitFor(() => expect(result.current.exactOnly).toBe(false));

    // Toggling back and forth only changes the search query mode.
    expect(fuseConstructions.count).toBe(constructionsAfterLoad);
  });

  it("does not build any index while the corpus is still (partially) loading", async () => {
    let resolveRedigerbar!: (content: SearchableContent[]) => void;
    const redigerbarPending = new Promise<SearchableContent[]>((resolve) => {
      resolveRedigerbar = resolve;
    });
    getAllTemplateDocumentation.queryFn.mockImplementation((malType: string) =>
      malType === "autobrev" ? Promise.resolve(autobrevContent) : redigerbarPending,
    );
    const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });

    const { result } = renderSearch(queryClient);

    // The "autobrev" corpus has resolved but "redigerbar" is still pending,
    // so the overall corpus isn't ready yet: no index should be built.
    await waitFor(() => expect(getAllTemplateDocumentation.queryFn).toHaveBeenCalledTimes(2));
    await act(async () => {
      await Promise.resolve();
    });
    expect(result.current.isLoading).toBe(true);
    expect(fuseConstructions.count).toBe(0);

    await act(async () => {
      resolveRedigerbar([]);
      await Promise.resolve();
    });
    await waitFor(() => expect(result.current.isLoading).toBe(false));
    expect(fuseConstructions.count).toBe(2);
  });

  it("treats a lone special-character query (e.g. §) as searching despite being shorter than MIN_QUERY_LENGTH", async () => {
    const sectionContent: SearchableContent[] = [
      {
        brevkode: "A1",
        language: "BOKMAL",
        lines: [{ index: 0, segments: [{ type: "text", value: "Vedtaket er gjort etter folketrygdloven § 19" }] }],
      },
    ];
    getAllTemplateDocumentation.queryFn.mockImplementation((malType: string) =>
      Promise.resolve(malType === "autobrev" ? sectionContent : []),
    );
    const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });

    const { result } = renderSearch(queryClient);
    await waitFor(() => expect(result.current.isLoading).toBe(false));

    act(() => result.current.setQuery("§"));

    await waitFor(() => expect(result.current.isSearching).toBe(true));
    await waitFor(() => expect(result.current.contentHits).toHaveLength(1));
  });

  it("keeps the previous results visible, flagged as pending, while a newer query is being searched for", async () => {
    const content: SearchableContent[] = [
      {
        brevkode: "A1",
        language: "BOKMAL",
        lines: [
          { index: 0, segments: [{ type: "text", value: "Vi har beregnet din alderspensjon" }] },
          { index: 1, segments: [{ type: "text", value: "Beløpet utbetales den 20. hver måned" }] },
        ],
      },
    ];
    getAllTemplateDocumentation.queryFn.mockImplementation((malType: string) =>
      Promise.resolve(malType === "autobrev" ? content : []),
    );
    const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });
    const client = deferrableClient();

    const { result } = renderSearch(queryClient, client);
    await waitFor(() => expect(result.current.isLoading).toBe(false));

    act(() => result.current.setQuery("beregnet"));
    await waitFor(() => expect(result.current.isPending).toBe(true));
    await act(async () => client.release());
    await waitFor(() => expect(result.current.isPending).toBe(false));
    expect(result.current.contentHits).toHaveLength(1);
    const firstHits = result.current.contentHits;

    // A second query is in flight: the first query's hits must still be the
    // ones on screen, only marked as pending.
    act(() => result.current.setQuery("utbetales"));
    await waitFor(() => expect(result.current.isPending).toBe(true));
    expect(result.current.contentHits).toBe(firstHits);

    await act(async () => client.release());
    await waitFor(() => expect(result.current.isPending).toBe(false));
    expect(result.current.contentHits[0].lineIndex).toBe(1);
  });

  // The retained results belong to the previous query, so the needle and match
  // mode handed to the highlighter must belong to it too. Highlighting the old
  // hits with the query still being typed marks words those hits were never
  // matched on - or, more often, nothing at all, so the emphasis flickers off
  // and back on with every keystroke.
  it("pins the highlight to the query that produced the results on screen", async () => {
    const content: SearchableContent[] = [
      {
        brevkode: "A1",
        language: "BOKMAL",
        lines: [
          { index: 0, segments: [{ type: "text", value: "Vi har beregnet din alderspensjon" }] },
          { index: 1, segments: [{ type: "text", value: "Beløpet utbetales den 20. hver måned" }] },
        ],
      },
    ];
    getAllTemplateDocumentation.queryFn.mockImplementation((malType: string) =>
      Promise.resolve(malType === "autobrev" ? content : []),
    );
    const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });
    const client = deferrableClient();

    const { result } = renderSearch(queryClient, client);
    await waitFor(() => expect(result.current.isLoading).toBe(false));

    act(() => result.current.setQuery("beregnet"));
    await act(async () => client.release());
    await waitFor(() => expect(result.current.isPending).toBe(false));
    expect(result.current.highlight.needle).toBe("beregnet");

    // Second query in flight: the first query's hits are still on screen, so
    // the highlight must still be the first query's too.
    act(() => result.current.setQuery("utbetales"));
    await waitFor(() => expect(result.current.isPending).toBe(true));
    expect(result.current.highlight.needle).toBe("beregnet");

    await act(async () => client.release());
    await waitFor(() => expect(result.current.isPending).toBe(false));
    expect(result.current.highlight.needle).toBe("utbetales");
  });

  // Same argument for the match mode: the checkbox has to respond immediately,
  // but re-highlighting the retained results in the new mode would change which
  // words are emphasised in hits that were matched under the old one.
  it("keeps the checkbox live while the highlight's match mode waits for the re-search", async () => {
    getAllTemplateDocumentation.queryFn.mockImplementation((malType: string) =>
      Promise.resolve(malType === "autobrev" ? autobrevContent : []),
    );
    const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });
    const client = deferrableClient();

    const { result } = renderSearch(queryClient, client);
    await waitFor(() => expect(result.current.isLoading).toBe(false));

    act(() => result.current.setQuery("hei"));
    await act(async () => client.release());
    await waitFor(() => expect(result.current.isPending).toBe(false));
    expect(result.current.highlight.exactOnly).toBe(false);

    act(() => result.current.setExactOnly(true));
    await waitFor(() => expect(result.current.exactOnly).toBe(true));
    expect(result.current.highlight.exactOnly).toBe(false);

    await act(async () => client.release());
    await waitFor(() => expect(result.current.highlight.exactOnly).toBe(true));
  });

  it("clears results and pending state when the query drops below MIN_QUERY_LENGTH", async () => {
    getAllTemplateDocumentation.queryFn.mockImplementation((malType: string) =>
      Promise.resolve(malType === "autobrev" ? autobrevContent : []),
    );
    const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });

    const { result } = renderSearch(queryClient);
    await waitFor(() => expect(result.current.isLoading).toBe(false));

    act(() => result.current.setQuery("hei"));
    await waitFor(() => expect(result.current.contentHits).toHaveLength(1));

    act(() => result.current.setQuery("h"));
    await waitFor(() => expect(result.current.isSearching).toBe(false));
    expect(result.current.contentHits).toEqual([]);
    expect(result.current.isPending).toBe(false);
  });

  it("resolves hits against the template objects the main thread holds, so results can be rendered directly", async () => {
    getAllTemplateDocumentation.queryFn.mockImplementation((malType: string) =>
      Promise.resolve(malType === "autobrev" ? autobrevContent : []),
    );
    const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });

    const { result } = renderSearch(queryClient);
    await waitFor(() => expect(result.current.isLoading).toBe(false));

    act(() => result.current.setQuery("hei"));
    await waitFor(() => expect(result.current.contentHits).toHaveLength(1));

    // The title comes from the TemplateRef metadata, and the lines/indexes from
    // the fetched corpus - none of which the worker sends back.
    const { template } = result.current.contentHits[0];
    expect(template.title).toBe("Alderspensjon");
    expect(template.malType).toBe("autobrev");
    expect(template.lines).toEqual([[{ type: "text", value: "Hei" }]]);
    expect(template.indexes).toEqual([0]);
  });
});
