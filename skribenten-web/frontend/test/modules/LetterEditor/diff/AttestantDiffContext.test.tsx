import { render, screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";

import {
  AttestantDiffProvider,
  useDeletedBlocks,
  useDeletedContent,
  useDiffSegmentsForLiteral,
} from "~/Brevredigering/LetterEditor/diff/AttestantDiffContext";
import { diffKey, type UnifiedLetterDiff } from "~/Brevredigering/LetterEditor/diff/diffModel";
import { type AnyBlock } from "~/types/brevbakerTypes";

const literalA = { blockIndex: 0, contentIndex: 0 };
const literalB = { blockIndex: 0, contentIndex: 1 };

const deletedBlock = { type: "PARAGRAPH", id: 7, content: [] } as unknown as AnyBlock;

const diff: UnifiedLetterDiff = {
  editedBlocks: {
    0: {
      contentEdits: {
        0: { edit: { inserts: [{ startOffset: 0, endOffset: 1 }], deletes: [] } },
        1: { edit: { inserts: [{ startOffset: 0, endOffset: 1 }], deletes: [] } },
      },
      deletedContent: {},
    },
  },
  deletedBlocks: { 1: [deletedBlock] },
};

function Probe() {
  const segmentsA = useDiffSegmentsForLiteral(literalA, "AA");
  const segmentsB = useDiffSegmentsForLiteral(literalB, "BB");
  const deletedBlocksAtOne = useDeletedBlocks(1);
  const deletedContentAtZero = useDeletedContent(0, 0);

  return (
    <div>
      <span data-testid="a">{segmentsA ? "visible" : "hidden"}</span>
      <span data-testid="b">{segmentsB ? "visible" : "hidden"}</span>
      <span data-testid="deleted-blocks">{deletedBlocksAtOne.length}</span>
      <span data-testid="deleted-content">{deletedContentAtZero.length}</span>
    </div>
  );
}

describe("AttestantDiffContext", () => {
  it("keeps other literals decorated when one literal is dismissed", () => {
    const hash = "hash-1";
    const dismissedDiffs = new Map<string, string>([[diffKey(literalA), hash]]);

    render(
      <AttestantDiffProvider
        diff={diff}
        diffHash={hash}
        dismissedDiffs={dismissedDiffs}
        dismissLiteral={() => {}}
        invalidateDiff={() => {}}
        invalidatedDiffHashes={new Set()}
      >
        <Probe />
      </AttestantDiffProvider>,
    );

    expect(screen.getByTestId("a").textContent).toBe("hidden");
    expect(screen.getByTestId("b").textContent).toBe("visible");
  });

  it("exposes entirely deleted blocks at their unified position", () => {
    render(
      <AttestantDiffProvider
        diff={diff}
        diffHash="hash-1"
        dismissedDiffs={new Map()}
        dismissLiteral={() => {}}
        invalidateDiff={() => {}}
        invalidatedDiffHashes={new Set()}
      >
        <Probe />
      </AttestantDiffProvider>,
    );

    expect(screen.getByTestId("deleted-blocks").textContent).toBe("1");
    expect(screen.getByTestId("deleted-content").textContent).toBe("0");
  });

  it("hides all decorations, literal and structural, after structural invalidation for current hash", () => {
    const hash = "hash-1";

    render(
      <AttestantDiffProvider
        diff={diff}
        diffHash={hash}
        dismissedDiffs={new Map()}
        dismissLiteral={() => {}}
        invalidateDiff={() => {}}
        invalidatedDiffHashes={new Set([hash])}
      >
        <Probe />
      </AttestantDiffProvider>,
    );

    expect(screen.getByTestId("a").textContent).toBe("hidden");
    expect(screen.getByTestId("b").textContent).toBe("hidden");
    expect(screen.getByTestId("deleted-blocks").textContent).toBe("0");
  });
});
