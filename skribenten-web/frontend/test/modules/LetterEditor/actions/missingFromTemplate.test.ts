import { describe, expect, test } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";

import { letter, literal, paragraph, withMissingFromTemplate } from "../utils";

describe("LetterEditorActions.removeMissingFromTemplateBlock", () => {
  test("removes the block and records its id in deletedBlocks", () => {
    const flagged = withMissingFromTemplate(paragraph([literal({ text: "duplikat" })]));
    const other = paragraph([literal({ text: "resten av brevet" })]);
    const state = letter(flagged, other);

    const result = Actions.removeMissingFromTemplateBlock(state, 0);

    expect(result.redigertBrev.blocks).toHaveLength(1);
    expect(result.redigertBrev.blocks[0]).toEqual(other);
    expect(result.redigertBrev.deletedBlocks).toEqual([flagged.id]);
    expect(result.saveStatus).toBe("DIRTY");
  });
});

describe("LetterEditorActions.keepMissingFromTemplateBlock", () => {
  test("clears the id and missingFromTemplate flag, keeping the content", () => {
    const flagged = withMissingFromTemplate(paragraph([literal({ text: "behold meg" })]));
    const state = letter(flagged);

    const result = Actions.keepMissingFromTemplateBlock(state, 0);

    expect(result.redigertBrev.blocks).toHaveLength(1);
    expect(result.redigertBrev.blocks[0].id).toBeNull();
    expect(result.redigertBrev.blocks[0].missingFromTemplate).toBe(false);
    expect(result.redigertBrev.blocks[0].content).toEqual(flagged.content);
    expect(result.saveStatus).toBe("DIRTY");
  });
});
