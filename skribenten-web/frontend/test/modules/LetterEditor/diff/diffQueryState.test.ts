import { describe, expect, it } from "vitest";

import {
  getSnapshotForHash,
  type HashBoundValue,
  letterStructureSignature,
  pickValueForCurrentHash,
  shouldRenderDiffMarkers,
} from "~/Brevredigering/LetterEditor/diff/diffQueryState";
import { type EditedLetter } from "~/types/brevbakerTypes";

const letter = (text: string): EditedLetter =>
  ({
    blocks: [
      {
        type: "PARAGRAPH",
        id: 1,
        parentId: null,
        missingFromTemplate: false,
        content: [
          {
            type: "LITERAL",
            id: 2,
            parentId: 1,
            text,
            editedText: null,
            fontType: "PLAIN",
            editedFontType: null,
            tags: [],
          },
        ],
      },
    ],
  }) as unknown as EditedLetter;

describe("diffQueryState", () => {
  it("ignores stale diff responses from an older hash", () => {
    const response: HashBoundValue<string> = {
      value: "stale-diff",
      redigertBrevHash: "old-hash",
    };

    const active = pickValueForCurrentHash(response, "new-hash");
    expect(active).toBeUndefined();
  });

  it("returns diff only when response hash matches current saved hash", () => {
    const response: HashBoundValue<string> = {
      value: "fresh-diff",
      redigertBrevHash: "hash-1",
    };

    const active = pickValueForCurrentHash(response, "hash-1");
    expect(active).toBe("fresh-diff");
  });

  it("hides markers after structural invalidation for the current hash", () => {
    const visible = shouldRenderDiffMarkers({
      visDiff: true,
      currentSavedHash: "hash-1",
      invalidatedDiffHashes: new Set(["hash-1"]),
      diff: { editedBlocks: {}, deletedBlocks: {} },
    });

    expect(visible).toBe(false);
  });

  it("shows markers again after autosave moves to a new hash with fresh diff", () => {
    const visible = shouldRenderDiffMarkers({
      visDiff: true,
      currentSavedHash: "hash-2",
      invalidatedDiffHashes: new Set(["hash-1"]),
      diff: { editedBlocks: {}, deletedBlocks: {} },
    });

    expect(visible).toBe(true);
  });

  it("returns hash-bound snapshot used by diff request", () => {
    const snapshots = new Map([
      ["hash-a", { text: "saved-a" }],
      ["hash-b", { text: "saved-b" }],
    ]);

    expect(getSnapshotForHash(snapshots, "hash-b")).toEqual({ text: "saved-b" });
    expect(getSnapshotForHash(snapshots, "missing")).toBeUndefined();
  });

  it("keeps the structural signature stable for text-only edits", () => {
    expect(letterStructureSignature(letter("før"))).toBe(letterStructureSignature(letter("etter")));
  });

  it("changes the structural signature when content is inserted", () => {
    const before = letter("tekst");
    const after = structuredClone(before);
    after.blocks[0].content.push({
      type: "NEW_LINE",
      id: null,
      parentId: null,
      text: "\n",
      fontType: "PLAIN",
    });

    expect(letterStructureSignature(after)).not.toBe(letterStructureSignature(before));
  });
});
