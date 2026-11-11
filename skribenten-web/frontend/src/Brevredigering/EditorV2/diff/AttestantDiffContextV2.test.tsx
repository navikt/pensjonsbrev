import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";

import { type UnifiedDiffV2 } from "~/types/brevV2";
import { literal, paragraph } from "~test/support/editorV2TestUtils";

import { AttestantDiffProviderV2, useDeletedV2, useDiffSegmentsV2 } from "./AttestantDiffContextV2";
import { deletedBlocksAt, deletedContentAt } from "./diffModel";

const block = { kind: "block", block: 0 } as const;

const diff: UnifiedDiffV2 = {
  type: "UNIFIED",
  editedBlocks: {
    0: {
      type: "TEXT",
      textEdits: {
        0: { inserts: [{ startOffset: 0, endOffset: 1 }], deletes: [{ startOffset: 1, endOffset: 4, text: "old" }] },
        1: { inserts: [{ startOffset: 0, endOffset: 20 }], deletes: [] },
      },
      deletedContent: {},
    },
  },
  deletedBlocks: { 1: [paragraph(literal("slettet"))] },
};

function Probe() {
  const segments = useDiffSegmentsV2(block, 0, "AA");
  const malformed = useDiffSegmentsV2(block, 1, "BB");
  const deletedBlocks = useDeletedV2((active) => deletedBlocksAt(active, 1));
  const deletedContent = useDeletedV2((active) => deletedContentAt(active, block, 0));

  return (
    <div>
      <span data-testid="segments">{segments ? segments.map((segment) => segment.type).join(",") : "none"}</span>
      <span data-testid="malformed">{malformed ? "visible" : "hidden"}</span>
      <span data-testid="deleted-blocks">{deletedBlocks.length}</span>
      <span data-testid="deleted-content">{deletedContent.length}</span>
    </div>
  );
}

const renderWith = ({
  diff,
  diffHash,
  ...filters
}: {
  diff?: UnifiedDiffV2;
  diffHash?: string;
  showInsertions?: boolean;
  showDeletions?: boolean;
}) =>
  render(
    <AttestantDiffProviderV2 diff={diff} diffHash={diffHash} disableDiffMode={() => {}} {...filters}>
      <Probe />
    </AttestantDiffProviderV2>,
  );

describe("AttestantDiffContextV2", () => {
  it("exposes entirely deleted blocks at their unified position", () => {
    renderWith({ diff, diffHash: "hash-1" });
    expect(screen.getByTestId("deleted-blocks").textContent).toBe("1");
    expect(screen.getByTestId("deleted-content").textContent).toBe("0");
  });

  it("hides all decorations when the diff is turned off", () => {
    renderWith({});
    expect(screen.getByTestId("segments").textContent).toBe("none");
    expect(screen.getByTestId("deleted-blocks").textContent).toBe("0");
  });

  it("hides all decorations when the diff no longer belongs to a saved hash", () => {
    renderWith({ diff });
    expect(screen.getByTestId("segments").textContent).toBe("none");
    expect(screen.getByTestId("deleted-blocks").textContent).toBe("0");
  });

  describe("insertion and deletion filters", () => {
    it("shows both insertions and deletions when both are enabled", () => {
      renderWith({ diff, diffHash: "hash-1", showInsertions: true, showDeletions: true });
      expect(screen.getByTestId("segments").textContent).toBe("inserted,unchanged,deleted");
      expect(screen.getByTestId("deleted-blocks").textContent).toBe("1");
    });

    it("shows only insertions when deletions are disabled", () => {
      renderWith({ diff, diffHash: "hash-1", showInsertions: true, showDeletions: false });
      expect(screen.getByTestId("segments").textContent).toBe("inserted,unchanged");
      expect(screen.getByTestId("deleted-blocks").textContent).toBe("0");
    });

    it("shows only deletions, keeping their position, when insertions are disabled", () => {
      renderWith({ diff, diffHash: "hash-1", showInsertions: false, showDeletions: true });
      expect(screen.getByTestId("segments").textContent).toBe("unchanged,unchanged,deleted");
      expect(screen.getByTestId("deleted-blocks").textContent).toBe("1");
    });

    it("leaves the text undecorated when nothing remains to highlight", () => {
      renderWith({ diff, diffHash: "hash-1", showInsertions: false, showDeletions: false });
      expect(screen.getByTestId("segments").textContent).toBe("none");
      expect(screen.getByTestId("deleted-blocks").textContent).toBe("0");
    });
  });

  it("hides text with malformed ranges and warns instead of decorating it", () => {
    const warning = vi.spyOn(console, "warn").mockImplementation(() => {});
    renderWith({ diff, diffHash: "hash-1" });
    expect(screen.getByTestId("malformed").textContent).toBe("hidden");
    expect(warning).toHaveBeenCalledWith(expect.stringContaining("b0:1"));
    warning.mockRestore();
  });
});
