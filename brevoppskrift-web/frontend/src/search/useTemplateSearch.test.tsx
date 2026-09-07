import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { act, renderHook, waitFor } from "@testing-library/react";
import { type ReactNode } from "react";
import { afterEach, describe, expect, it, vi } from "vitest";

import { type SearchableContent } from "~/api/brevbaker-api-endpoints";
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
  });

  it("reports failedCount/failedMalTypes when one malType's corpus fetch fails, without leaving isLoading stuck", async () => {
    getAllTemplateDocumentation.queryFn.mockImplementation((malType: string) =>
      malType === "redigerbar" ? Promise.reject(new Error("boom")) : Promise.resolve(autobrevContent),
    );
    const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });

    const { result } = renderHook(() => useTemplateSearch(refs), { wrapper: wrapper(queryClient) });

    await waitFor(() => {
      expect(result.current.isLoading).toBe(false);
      expect(result.current.failedCount).toBe(1);
    });
    expect(result.current.failedMalTypes).toEqual(["redigerbar"]);
  });

  it("does not report any failures when every malType's corpus fetch succeeds", async () => {
    getAllTemplateDocumentation.queryFn.mockResolvedValue(autobrevContent);
    const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });

    const { result } = renderHook(() => useTemplateSearch(refs), { wrapper: wrapper(queryClient) });

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

    const { result } = renderHook(() => useTemplateSearch(refs), { wrapper: wrapper(queryClient) });
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

    const { result } = renderHook(() => useTemplateSearch(refs), { wrapper: wrapper(queryClient) });
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

    const { result } = renderHook(() => useTemplateSearch(refs), { wrapper: wrapper(queryClient) });
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

    const { result } = renderHook(() => useTemplateSearch(refs), { wrapper: wrapper(queryClient) });

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
});
