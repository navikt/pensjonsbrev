import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { act, renderHook, waitFor } from "@testing-library/react";
import axios from "axios";
import { type ReactNode } from "react";
import { beforeEach, describe, expect, it, vi } from "vitest";

import { type SplitDiffV2, type UnifiedDiffV2 } from "~/types/brevV2";
import { letterV2, literal, paragraph } from "~test/support/editorV2TestUtils";

import { useAttestantLetterDiffV2 } from "./useAttestantLetterDiffV2";

const letterFor = (text: string) => letterV2({ blocks: [paragraph(literal(text))] });

const diffWithEdit: UnifiedDiffV2 = {
  type: "UNIFIED",
  editedBlocks: {
    0: {
      type: "TEXT",
      textEdits: { 0: { inserts: [{ startOffset: 0, endOffset: 3 }], deletes: [] } },
      deletedContent: {},
    },
  },
  deletedBlocks: {},
};

const emptyDiff: UnifiedDiffV2 = { type: "UNIFIED", editedBlocks: {}, deletedBlocks: {} };

const wrapper = ({ children }: { children: ReactNode }) => {
  const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });
  return <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>;
};

type Props = Parameters<typeof useAttestantLetterDiffV2>[0];

const initialProps: Props = {
  saksId: "123",
  brevId: 1,
  savedLetter: letterFor("original"),
  savedHash: "hash-1",
  isSaved: true,
};

const renderDiffHook = (props: Props = initialProps) =>
  renderHook((current: Props) => useAttestantLetterDiffV2(current), { initialProps: props, wrapper });

describe("useAttestantLetterDiffV2", () => {
  beforeEach(() => {
    vi.restoreAllMocks();
  });

  it("diffs the letter belonging to a hash the feature never observed being saved", async () => {
    const post = vi.spyOn(axios, "post").mockResolvedValue({ data: diffWithEdit });
    const { result, rerender } = renderDiffHook();

    act(() => result.current.setToggle("inserted", true));
    await waitFor(() => expect(result.current.status).toBe("ready"));
    expect(post).toHaveBeenLastCalledWith(
      expect.stringMatching(/\/sak\/123\/brev\/v2\/1\/diff\?split=false$/),
      initialProps.savedLetter,
    );

    act(() => result.current.disableDiffMode());
    const autosaved = { ...initialProps, savedLetter: letterFor("autosaved"), savedHash: "hash-2" };
    rerender(autosaved);

    act(() => result.current.setToggle("inserted", true));
    await waitFor(() => expect(result.current.status).toBe("ready"));
    expect(post).toHaveBeenLastCalledWith(expect.any(String), autosaved.savedLetter);
    expect(result.current.diffHash).toBe("hash-2");
  });

  it("waits instead of diffing while the letter is dirty or being saved", async () => {
    const post = vi.spyOn(axios, "post").mockResolvedValue({ data: diffWithEdit });
    const { result, rerender } = renderDiffHook({ ...initialProps, isSaved: false });

    act(() => result.current.setToggle("inserted", true));
    expect(result.current.status).toBe("loading");
    expect(result.current.activeDiff).toBeUndefined();
    expect(post).not.toHaveBeenCalled();

    rerender({ ...initialProps, savedLetter: letterFor("saved"), savedHash: "hash-2" });
    await waitFor(() => expect(result.current.status).toBe("ready"));
  });

  it("reports an empty diff when nothing was changed", async () => {
    vi.spyOn(axios, "post").mockResolvedValue({ data: emptyDiff });
    const { result } = renderDiffHook();

    act(() => result.current.setToggle("deleted", true));
    await waitFor(() => expect(result.current.status).toBe("empty"));
  });

  it("reports an error when the diff cannot be fetched", async () => {
    vi.spyOn(axios, "post").mockRejectedValue(new Error("feil"));
    const { result } = renderDiffHook();

    act(() => result.current.setToggle("deleted", true));
    await waitFor(() => expect(result.current.status).toBe("error"));
    expect(result.current.activeDiff).toBeUndefined();
  });

  it("reports an error when the backend does not answer with a unified diff", async () => {
    const split: SplitDiffV2 = { type: "SPLIT", inserts: [], deletes: [], rendretBrev: letterFor("original") };
    vi.spyOn(axios, "post").mockResolvedValue({ data: split });
    const { result } = renderDiffHook();

    act(() => result.current.setToggle("inserted", true));
    await waitFor(() => expect(result.current.status).toBe("error"));
  });

  it("exposes the diff state that was active when the page first loaded, unaffected by later toggling", () => {
    const { result, rerender } = renderDiffHook();
    expect(result.current.defaultDiffMode).toBe(false);

    act(() => result.current.setToggle("inserted", true));
    rerender({ ...initialProps, savedHash: "hash-2" });
    expect(result.current.defaultDiffMode).toBe(false);

    act(() => result.current.disableDiffMode());
    expect(result.current.defaultDiffMode).toBe(false);
  });

  it("hides the cached markers once the letter goes dirty at the hash they were fetched for", async () => {
    vi.spyOn(axios, "post").mockResolvedValue({ data: diffWithEdit });
    const { result, rerender } = renderDiffHook();

    act(() => result.current.setToggle("inserted", true));
    await waitFor(() => expect(result.current.status).toBe("ready"));
    expect(result.current.activeDiff).toBeDefined();

    rerender({ ...initialProps, isSaved: false });
    expect(result.current.activeDiff).toBeUndefined();
    expect(result.current.diffHash).toBeUndefined();
    expect(result.current.status).toBe("loading");
  });

  it("derives which changes to show from the two independent toggles, and turns both off when disabled", () => {
    const { result } = renderDiffHook();

    act(() => result.current.setToggle("inserted", true));
    expect(result.current).toMatchObject({ isDiffMode: true, showInsertions: true, showDeletions: false });

    act(() => {
      result.current.setToggle("inserted", false);
      result.current.setToggle("deleted", true);
    });
    expect(result.current).toMatchObject({ isDiffMode: true, showInsertions: false, showDeletions: true });

    act(() => result.current.disableDiffMode());
    expect(result.current.toggles).toEqual({ inserted: false, deleted: false });
    expect(result.current).toMatchObject({ isDiffMode: false, status: "disabled" });
  });
});
