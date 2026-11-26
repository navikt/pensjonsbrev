import { describe, expect, test } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { letter, literal, paragraph, withMissingInRendered } from "~test/support/letterEditorTestUtils";

describe("LetterEditorActions.removeMissingInRenderedBlock", () => {
  test("removes the block and records its id in deletedBlocks", () => {
    const flagged = withMissingInRendered(paragraph([literal({ text: "avsnitt utenfor mal" })]));
    const other = paragraph([literal({ text: "resten av brevet" })]);
    const state = letter(flagged, other);

    const result = Actions.removeMissingInRenderedBlock(state, 0);

    expect(result.redigertBrev.blocks).toHaveLength(1);
    expect(result.redigertBrev.blocks[0]).toEqual(other);
    expect(result.redigertBrev.deletedBlocks).toEqual([flagged.id]);
    expect(result.saveStatus).toBe("DIRTY");
  });

  test("removes a flagged block with id null without recording null in deletedBlocks", () => {
    const flagged = withMissingInRendered(paragraph({ content: [literal({ text: "nytt avsnitt" })] }));
    const other = paragraph([literal({ text: "resten av brevet" })]);
    const state = letter(flagged, other);

    expect(flagged.id).toBeNull();

    const result = Actions.removeMissingInRenderedBlock(state, 0);

    expect(result.redigertBrev.blocks).toHaveLength(1);
    expect(result.redigertBrev.blocks[0]).toEqual(other);
    expect(result.redigertBrev.deletedBlocks).toEqual([]);
    expect(result.saveStatus).toBe("DIRTY");
  });
});

describe("LetterEditorActions.keepMissingInRenderedBlock", () => {
  test("clears the id and accepts the pending decision, keeping the content", () => {
    const flagged = withMissingInRendered(paragraph([literal({ text: "behold meg" })]));
    const state = letter(flagged);

    const result = Actions.keepMissingInRenderedBlock(state, 0);

    expect(result.redigertBrev.blocks).toHaveLength(1);
    expect(result.redigertBrev.blocks[0].id).toBeNull();
    expect(result.redigertBrev.blocks[0].missingFromTemplate).toBe(false);
    expect(result.redigertBrev.blocks[0].decision).toEqual({ type: "MISSING_FROM_RENDERED", status: "ACCEPTED" });
    expect(result.redigertBrev.blocks[0].content).toEqual(flagged.content);
    expect(result.saveStatus).toBe("DIRTY");
  });
});
