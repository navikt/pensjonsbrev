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

  it("defaults to fuzzy search enabled, and setFuzzy(false) disables typo tolerance", async () => {
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

    expect(result.current.fuzzy).toBe(true);

    act(() => result.current.setQuery("alderspenjson")); // transposed letters
    await waitFor(() => expect(result.current.isSearching).toBe(true));
    await waitFor(() => expect(result.current.contentHits).toHaveLength(1));

    act(() => result.current.setFuzzy(false));
    await waitFor(() => expect(result.current.fuzzy).toBe(false));
    await waitFor(() => expect(result.current.contentHits).toHaveLength(0));
  });
});
