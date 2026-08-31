import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { act, renderHook, waitFor } from "@testing-library/react";
import { type ReactNode } from "react";
import { beforeEach, describe, expect, it, vi } from "vitest";

import { getBrevDiff } from "~/api/brev-queries";
import { type UnifiedLetterDiff } from "~/Brevredigering/LetterEditor/diff/diffModel";
import { useAttestantLetterDiff } from "~/hooks/useAttestantLetterDiff";
import { type EditedLetter } from "~/types/brevbakerTypes";

const letterFor = (text: string) =>
  ({
    blocks: [{ id: 1, type: "PARAGRAPH", content: [{ id: 2, type: "LITERAL", text }] }],
  }) as unknown as EditedLetter;

const diffWithEdit: UnifiedLetterDiff = {
  editedBlocks: {
    0: {
      contentEdits: { 0: { edit: { inserts: [{ startOffset: 0, endOffset: 3 }], deletes: [] } } },
      deletedContent: {},
    },
  },
  deletedBlocks: {},
};

const wrapper = ({ children }: { children: ReactNode }) => {
  const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });
  return <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>;
};

describe("useAttestantLetterDiff", () => {
  beforeEach(() => {
    vi.restoreAllMocks();
  });

  it("diffs the letter belonging to a hash the feature never observed being saved", async () => {
    const queryFn = vi.spyOn(getBrevDiff, "queryFn").mockResolvedValue(diffWithEdit);
    const initialProps = {
      brevId: 1,
      savedLetter: letterFor("original"),
      savedHash: "hash-1",
      isSaved: true,
    };

    const { result, rerender } = renderHook((props: typeof initialProps) => useAttestantLetterDiff(props), {
      initialProps,
      wrapper,
    });

    act(() => result.current.setEnabled(true));
    await waitFor(() => expect(result.current.status).toBe("ready"));
    expect(queryFn).toHaveBeenLastCalledWith(1, initialProps.savedLetter);

    // Autolagring i ManagedLetterEditor gir en ny hash uten at diff-funksjonen varsles.
    act(() => result.current.disableDiff());
    const autosaved = { ...initialProps, savedLetter: letterFor("autosaved"), savedHash: "hash-2" };
    rerender(autosaved);

    act(() => result.current.setEnabled(true));
    await waitFor(() => expect(result.current.status).toBe("ready"));
    expect(queryFn).toHaveBeenLastCalledWith(1, autosaved.savedLetter);
    expect(result.current.diffHash).toBe("hash-2");
  });

  it("waits instead of diffing while the letter is dirty or being saved", async () => {
    const queryFn = vi.spyOn(getBrevDiff, "queryFn").mockResolvedValue(diffWithEdit);
    const initialProps = {
      brevId: 1,
      savedLetter: letterFor("edited"),
      savedHash: "hash-1",
      isSaved: false,
    };

    const { result, rerender } = renderHook((props: typeof initialProps) => useAttestantLetterDiff(props), {
      initialProps,
      wrapper,
    });

    act(() => result.current.setEnabled(true));
    expect(result.current.status).toBe("loading");
    expect(result.current.activeDiff).toBeUndefined();
    expect(queryFn).not.toHaveBeenCalled();

    rerender({ ...initialProps, savedLetter: letterFor("saved"), savedHash: "hash-2", isSaved: true });

    await waitFor(() => expect(result.current.status).toBe("ready"));
  });
});
