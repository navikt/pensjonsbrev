import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { renderHook, waitFor } from "@testing-library/react";
import axios from "axios";
import { type ReactNode } from "react";
import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";

import { useSamhandlerMottaker } from "~/hooks/useSamhandlerMottaker";

const samhandler = {
  navn: "Advokat AS",
  idType: "ORG",
  offentligId: "999888777",
  organisasjonsnummer: "999888777",
  samhandlerType: "ADVO",
};
const adresse = { navn: "Advokat AS", linje1: "Postboks 603 Sentrum", postnr: "4003", poststed: "Stavanger" };

function createWrapper() {
  const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });
  return function Wrapper({ children }: { children: ReactNode }) {
    return <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>;
  };
}

describe("useSamhandlerMottaker", () => {
  beforeEach(() => {
    vi.restoreAllMocks();
    vi.spyOn(axios, "post").mockImplementation(async (url) => ({
      data: url.endsWith("/hentSamhandler") ? { success: samhandler } : { adresse },
    }));
  });

  afterEach(() => {
    vi.restoreAllMocks();
  });

  it("fetches the organization number without fetching an address", async () => {
    const { result } = renderHook(() => useSamhandlerMottaker("80000781720"), { wrapper: createWrapper() });

    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    expect(result.current.data).toEqual({ samhandler });
    expect(axios.post).toHaveBeenCalledExactlyOnceWith(expect.stringContaining("/hentSamhandler"), {
      idTSSEkstern: "80000781720",
      hentDetaljert: false,
    });
  });

  it("uses the normalized organization number supplied by the backend", async () => {
    const tssPrefixedSamhandler = {
      ...samhandler,
      offentligId: "00999888777",
      organisasjonsnummer: "999888777",
    };
    vi.mocked(axios.post).mockResolvedValueOnce({ data: { success: tssPrefixedSamhandler } });
    const { result } = renderHook(() => useSamhandlerMottaker("80000781720"), { wrapper: createWrapper() });

    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    expect(result.current.data).toEqual({ samhandler: tssPrefixedSamhandler });
    expect(axios.post).toHaveBeenCalledTimes(1);
  });

  it.each(["FNR", "UTOR", "INST", "UNKNOWN"])("fetches the address for ID type %s", async (idType) => {
    vi.mocked(axios.post).mockResolvedValueOnce({
      data: { success: { ...samhandler, idType, organisasjonsnummer: null } },
    });
    const { result } = renderHook(() => useSamhandlerMottaker("80000781720"), { wrapper: createWrapper() });

    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    expect(result.current.data).toEqual({ samhandler: { ...samhandler, idType, organisasjonsnummer: null }, adresse });
    expect(axios.post).toHaveBeenCalledTimes(2);
    expect(axios.post).toHaveBeenLastCalledWith(expect.stringContaining("/hentSamhandlerAdresse"), {
      idTSSEkstern: "80000781720",
    });
  });

  it.each(["", "   ", "1234567890"])(
    "fetches the address when the backend does not provide a valid organization number for %j",
    async (offentligId) => {
      vi.mocked(axios.post).mockResolvedValueOnce({
        data: { success: { ...samhandler, offentligId, organisasjonsnummer: null } },
      });
      const { result } = renderHook(() => useSamhandlerMottaker("80000781720"), { wrapper: createWrapper() });

      await waitFor(() => expect(result.current.isSuccess).toBe(true));
      expect(result.current.data).toEqual({
        samhandler: { ...samhandler, offentligId, organisasjonsnummer: null },
        adresse,
      });
      expect(axios.post).toHaveBeenCalledTimes(2);
      expect(axios.post).toHaveBeenLastCalledWith(expect.stringContaining("/hentSamhandlerAdresse"), {
        idTSSEkstern: "80000781720",
      });
    },
  );

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

    expect(axios.post).not.toHaveBeenCalled();
  });

  it("does not show the previous recipient while fetching a new recipient", async () => {
    const { result, rerender } = renderHook(({ id }) => useSamhandlerMottaker(id), {
      initialProps: { id: "80000781720" },
      wrapper: createWrapper(),
    });
    await waitFor(() => expect(result.current.data?.samhandler.organisasjonsnummer).toBeDefined());

    vi.mocked(axios.post).mockResolvedValueOnce({
      data: { success: { ...samhandler, idType: "FNR", organisasjonsnummer: null } },
    });
    rerender({ id: "80000781721" });
    expect(result.current.data).toBeUndefined();
    await waitFor(() => expect(result.current.data?.adresse).toBeDefined());
    expect(axios.post).toHaveBeenLastCalledWith(expect.stringContaining("/hentSamhandlerAdresse"), {
      idTSSEkstern: "80000781721",
    });
  });
});
