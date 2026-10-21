import { applyPatches } from "immer";
import { describe, expect, test } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { isValidIndex } from "~/Brevredigering/LetterEditor/actions/common";
import { type ItemList } from "~/types/brevbakerTypes";
import { item, itemList, letter, literal, paragraph } from "~test/support/letterEditorTestUtils";
import { MockDataTransfer, projectLetter } from "~test/support/pasteTestUtils";

describe("paste across list and table boundaries", () => {
  test.each([1, 0])("a table after item offset %i preserves the trailing list and text", (offset) => {
    const state = letter(
      paragraph({
        id: 1,
        content: [
          itemList({
            id: 10,
            items: [
              item({ id: 11, content: [literal({ id: 111, text: "a" })] }),
              item({ id: 12, content: [literal({ id: 121, text: "b" })] }),
            ],
          }),
          literal({ id: 20, text: "tail" }),
        ],
      }),
    );

    const result = Actions.paste(
      state,
      { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 },
      offset,
      new MockDataTransfer({ "text/html": "<table><tr><td>x</td></tr></table>" }),
    );

    expect(projectLetter(result)).toEqual(
      offset === 1 ? ["• a", "TABLE", "  th: x", "• b", "P: tail"] : ["TABLE", "  th: x", "• a", "• b", "P: tail"],
    );
    expect(isValidIndex(result.redigertBrev, result.focus)).toBe(true);
    expect(result.saveStatus).toBe("DIRTY");
    expect(result.history.entries).toHaveLength(1);
    const entry = result.history.entries[0];
    if (entry.type !== "PATCH") throw new Error("Expected paste patches");
    expect(applyPatches(result, entry.inversePatches).redigertBrev).toEqual(state.redigertBrev);

    if (offset === 1) {
      const [source, tableBlock, listBlock, tailBlock] = result.redigertBrev.blocks;
      expect(source.id).toBe(1);
      expect(source.deletedContent).toContain(20);
      expect((source.content[0] as ItemList).deletedItems).toEqual([12]);
      expect(tableBlock.id).toBeNull();
      expect(listBlock.id).toBeNull();
      expect(listBlock.content).toHaveLength(1);
      expect((listBlock.content[0] as ItemList).items[0]).toMatchObject({ id: 12, parentId: 10 });
      expect(tailBlock.content[0]).toMatchObject({ id: 20, parentId: 1 });
    }
  });
});
