import { render, screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";

import {
  AttestantDiffProvider,
  useDiffSegmentsForLiteral,
} from "~/Brevredigering/LetterEditor/diff/AttestantDiffContext";
import { diffKey, type LetterDiff } from "~/Brevredigering/LetterEditor/diff/diffModel";

const literalA = { blockIndex: 0, contentIndex: 0 };
const literalB = { blockIndex: 0, contentIndex: 1 };

const diff: LetterDiff = {
  inserts: [
    { index: literalA, startOffset: 0, endOffset: 1 },
    { index: literalB, startOffset: 0, endOffset: 1 },
  ],
  deletes: [],
};

function Probe() {
  const segmentsA = useDiffSegmentsForLiteral(literalA, "AA");
  const segmentsB = useDiffSegmentsForLiteral(literalB, "BB");

  return (
    <div>
      <span data-testid="a">{segmentsA ? "visible" : "hidden"}</span>
      <span data-testid="b">{segmentsB ? "visible" : "hidden"}</span>
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
        invalidatedDiffHashes={new Set()}
        dismissedDiffs={dismissedDiffs}
        dismissLiteral={() => {}}
        invalidateDiff={() => {}}
      >
        <Probe />
      </AttestantDiffProvider>,
    );

    expect(screen.getByTestId("a").textContent).toBe("hidden");
    expect(screen.getByTestId("b").textContent).toBe("visible");
  });

  it("hides all literal decorations after structural invalidation for current hash", () => {
    const hash = "hash-1";

    render(
      <AttestantDiffProvider
        diff={diff}
        diffHash={hash}
        invalidatedDiffHashes={new Set([hash])}
        dismissedDiffs={new Map()}
        dismissLiteral={() => {}}
        invalidateDiff={() => {}}
      >
        <Probe />
      </AttestantDiffProvider>,
    );

    expect(screen.getByTestId("a").textContent).toBe("hidden");
    expect(screen.getByTestId("b").textContent).toBe("hidden");
  });
});
