import { applyPatches } from "immer";
import { describe, expect, test } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { isValidIndex } from "~/Brevredigering/LetterEditor/actions/common";
import { type ItemList, ListType } from "~/types/brevbakerTypes";
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

  test.each(["<p></p>", "<p>after</p>", "<table><tr><td>next</td></tr></table>"])(
    "an empty-cell table survives before a list when followed by %s",
    (afterTable) => {
      const state = letter(
        paragraph({
          id: 1,
          content: [
            literal({ id: 2, text: "before" }),
            itemList({ id: 10, items: [item(literal("a")), item(literal("b"))] }),
          ],
        }),
      );
      const result = Actions.paste(
        state,
        { blockIndex: 0, contentIndex: 1, itemIndex: 0, itemContentIndex: 0 },
        0,
        new MockDataTransfer({ "text/html": `<table><tr><td></td></tr></table>${afterTable}` }),
      );

      expect(projectLetter(result)).toEqual([
        "P: before",
        "TABLE",
        "  th: ",
        ...(afterTable.includes("after") ? ["P: after"] : afterTable.includes("next") ? ["TABLE", "  th: next"] : []),
        "• a",
        "• b",
      ]);
      expect(result.redigertBrev.blocks[0].id).toBe(1);
      expect(result.redigertBrev.blocks[0].deletedContent).toContain(10);
      expect(isValidIndex(result.redigertBrev, result.focus)).toBe(true);
      expect(result.saveStatus).toBe("DIRTY");
    },
  );
});

describe("pasted list styles follow source boundaries", () => {
  test.each([
    [ListType.PUNKTLISTE, "ol", "•"],
    [ListType.NUMMERERT_LISTE, "ul", "1."],
  ])("adapts the whole source list to the destination %s", (listType, tag, marker) => {
    const state = letter(
      paragraph({
        id: 1,
        content: [itemList({ id: 10, listType, items: [item(literal("a"))] })],
      }),
    );
    const result = Actions.paste(
      state,
      { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 },
      1,
      new MockDataTransfer({ "text/html": `<${tag}><li>A</li><li>B</li></${tag}>` }),
    );

    expect(projectLetter(result)).toEqual([`${marker} aA`, `${marker} B`, `${marker} `]);
    expect(result.redigertBrev.blocks).toHaveLength(1);
    expect(result.redigertBrev.blocks[0].content[0]).toMatchObject({ id: 10, listType });
    expect(isValidIndex(result.redigertBrev, result.focus)).toBe(true);
  });

  test("uses the edited destination list type without changing its template type", () => {
    const state = letter(
      paragraph({
        id: 1,
        content: [
          itemList({
            id: 10,
            listType: ListType.PUNKTLISTE,
            editedListType: ListType.NUMMERERT_LISTE,
            items: [item(literal("a"))],
          }),
        ],
      }),
    );
    const result = Actions.paste(
      state,
      { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 },
      1,
      new MockDataTransfer({ "text/html": "<ul><li>A</li><li>B</li></ul>" }),
    );

    expect(projectLetter(result)).toEqual(["1. aA", "1. B", "1. "]);
    expect(result.redigertBrev.blocks[0].content[0]).toMatchObject({
      listType: ListType.PUNKTLISTE,
      editedListType: ListType.NUMMERERT_LISTE,
    });
  });

  test("nested list styles do not split the surrounding source list", () => {
    const state = letter(paragraph([itemList({ items: [item(literal("a"))] })]));
    const result = Actions.paste(
      state,
      { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 },
      1,
      new MockDataTransfer({
        "text/html": "<ol><li>A<ul><li>nested</li></ul></li><li>B</li></ol>",
      }),
    );

    expect(projectLetter(result)).toEqual(["• aA", "• nested", "• B", "• "]);
    expect(result.redigertBrev.blocks).toHaveLength(1);
  });

  test("a different top-level source list still starts a separate list", () => {
    const state = letter(paragraph([itemList({ items: [item(literal("a"))] })]));
    const result = Actions.paste(
      state,
      { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 },
      1,
      new MockDataTransfer({
        "text/html": "<ol><li>A</li><li>B</li></ol><ul><li>C</li><li>D</li></ul>",
      }),
    );

    expect(projectLetter(result)).toEqual(["• aA", "• B", "• C", "• D", "• "]);
    expect(result.redigertBrev.blocks).toHaveLength(2);
    expect(isValidIndex(result.redigertBrev, result.focus)).toBe(true);
  });
});
