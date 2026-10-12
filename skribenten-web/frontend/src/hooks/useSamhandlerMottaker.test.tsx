import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { act, renderHook, waitFor } from "@testing-library/react";
import axios from "axios";
import { type ReactNode } from "react";
import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";

import { useSamhandlerMottaker } from "~/hooks/useSamhandlerMottaker";

const samhandler = { navn: "Advokat AS", idType: "ORG", offentligId: "999888777", samhandlerType: "ADVO" };
const adresse = { navn: "Advokat AS", linje1: "Postboks 603 Sentrum", postnr: "4003", poststed: "Stavanger" };

function createWrapper() {
  const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });
  return function Wrapper({ children }: { children: ReactNode }) {
    return <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>;
  };
}

describe.each([false, true])("useSamhandlerMottaker (DEV=%s)", (isDevelopment) => {
  beforeEach(() => {
    vi.restoreAllMocks();
    vi.stubEnv("DEV", isDevelopment);
    vi.spyOn(axios, "get").mockResolvedValue({ data: { enabled: true } });
    vi.spyOn(axios, "post").mockImplementation(async (url) => ({
      data: url.endsWith("/hentSamhandler") ? { success: samhandler } : { adresse },
    }));
  });

  afterEach(() => {
    vi.unstubAllEnvs();
  });

  it("fetches the organization number without fetching an address", async () => {
    const { result } = renderHook(() => useSamhandlerMottaker("80000781720"), { wrapper: createWrapper() });

    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    expect(result.current.data).toEqual({ type: "organisasjon", samhandler });
    expect(axios.get).toHaveBeenCalledWith(expect.stringContaining("/features/samhandlerOrgnummer"));
    expect(axios.post).toHaveBeenCalledExactlyOnceWith(expect.stringContaining("/hentSamhandler"), {
      idTSSEkstern: "80000781720",
      hentDetaljert: false,
    });
  });

  it.each(["FNR", "UTOR", "INST", "UNKNOWN"])("fetches the address for ID type %s", async (idType) => {
    vi.mocked(axios.post).mockResolvedValueOnce({ data: { success: { ...samhandler, idType } } });
    const { result } = renderHook(() => useSamhandlerMottaker("80000781720"), { wrapper: createWrapper() });

    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    expect(result.current.data).toEqual({ type: "adresse", adresse });
    expect(axios.post).toHaveBeenCalledTimes(2);
    expect(axios.post).toHaveBeenLastCalledWith(expect.stringContaining("/hentSamhandlerAdresse"), {
      idTSSEkstern: "80000781720",
    });
  });

  it.each(["", "   "])("fetches the address for an organization with blank public ID %j", async (offentligId) => {
    vi.mocked(axios.post).mockResolvedValueOnce({ data: { success: { ...samhandler, offentligId } } });
    const { result } = renderHook(() => useSamhandlerMottaker("80000781720"), { wrapper: createWrapper() });

    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    expect(result.current.data).toEqual({ type: "adresse", adresse });
    expect(axios.post).toHaveBeenCalledTimes(2);
    expect(axios.post).toHaveBeenLastCalledWith(expect.stringContaining("/hentSamhandlerAdresse"), {
      idTSSEkstern: "80000781720",
    });
  });

  it.each(["disabled", "unavailable"])("keeps address behavior when the toggle is %s", async (state) => {
    if (state === "disabled") {
      vi.mocked(axios.get).mockResolvedValue({ data: { enabled: false } });
    } else {
      vi.mocked(axios.get).mockRejectedValue(new Error("Toggle unavailable"));
    }
    const { result } = renderHook(() => useSamhandlerMottaker("80000781720"), { wrapper: createWrapper() });

    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    expect(result.current.data).toEqual({ type: "adresse", adresse });
    expect(axios.post).toHaveBeenCalledExactlyOnceWith(expect.stringContaining("/hentSamhandlerAdresse"), {
      idTSSEkstern: "80000781720",
    });
  });

  it("waits for the toggle before starting any recipient lookup", async () => {
    const toggle = Promise.withResolvers<{ data: { enabled: boolean } }>();
    vi.mocked(axios.get).mockReturnValue(toggle.promise);
    const { result } = renderHook(() => useSamhandlerMottaker("80000781720"), { wrapper: createWrapper() });

    expect(result.current.isPending).toBe(true);
    expect(axios.post).not.toHaveBeenCalled();
    await act(async () => toggle.resolve({ data: { enabled: true } }));
    await waitFor(() => expect(result.current.data?.type).toBe("organisasjon"));
    expect(axios.post).toHaveBeenCalledTimes(1);
  });

  it.each([{ failure: "GENERISK" }, { success: null }])(
    "reports a failed lookup without falling back to an address",
    async (data) => {
      vi.mocked(axios.post).mockResolvedValue({ data });
      const { result } = renderHook(() => useSamhandlerMottaker("80000781720"), { wrapper: createWrapper() });

      await waitFor(() => expect(result.current.isError).toBe(true));
      expect(axios.post).toHaveBeenCalledTimes(1);
    },
  );

  it("does not fetch without a samhandler recipient", () => {
    renderHook(() => useSamhandlerMottaker(), { wrapper: createWrapper() });

    expect(axios.get).not.toHaveBeenCalled();
    expect(axios.post).not.toHaveBeenCalled();
  });

  it("does not show the previous recipient while fetching a new recipient", async () => {
    const { result, rerender } = renderHook(({ id }) => useSamhandlerMottaker(id), {
      initialProps: { id: "80000781720" },
      wrapper: createWrapper(),
    });
    await waitFor(() => expect(result.current.data?.type).toBe("organisasjon"));

    vi.mocked(axios.post).mockResolvedValueOnce({ data: { success: { ...samhandler, idType: "FNR" } } });
    rerender({ id: "80000781721" });
    expect(result.current.data).toBeUndefined();
    await waitFor(() => expect(result.current.data?.type).toBe("adresse"));
    expect(axios.post).toHaveBeenLastCalledWith(expect.stringContaining("/hentSamhandlerAdresse"), {
      idTSSEkstern: "80000781721",
    });
  });
});
