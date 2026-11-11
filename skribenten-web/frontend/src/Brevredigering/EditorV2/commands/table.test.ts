import { describe, expect, test } from "vitest";

import { type TableV2 } from "~/types/brevV2";
import {
  blockAt,
  cell,
  column,
  item,
  list,
  literal,
  newText,
  paragraph,
  row,
  stateV2,
  table,
  tableAtIndex,
  textBlockAt,
  texts,
  variable,
} from "~test/support/editorV2TestUtils";

import { type Caret, caret } from "../model/paths";
import { run } from "../state/editorState";
import {
  demoteHeaderToRow,
  insertTable,
  insertTableColumnLeft,
  insertTableColumnRight,
  insertTableRowAbove,
  insertTableRowBelow,
  moveTableRow,
  promoteRowToHeader,
  removeTable,
  removeTableColumn,
  removeTableRow,
} from "./table";

const header = (block: number, columnIndex: number, content = 0, offset = 0) =>
  caret({ kind: "header", block, column: columnIndex }, content, offset);
const inCell = (block: number, rowIndex: number, cellIndex: number, content = 0, offset = 0) =>
  caret({ kind: "cell", block, row: rowIndex, cell: cellIndex }, content, offset);
const inBlock = (block: number, content = 0, offset = 0) => caret({ kind: "block", block }, content, offset);

function headerTexts(t: TableV2): string[][] {
  return t.header.colSpec.map((spec) => texts(spec.content));
}

function rowTexts(t: TableV2): string[][][] {
  return t.rows.map((r) => r.cells.map((c) => texts(c.content)));
}

function twoByTwo(...before: Parameters<typeof stateV2>) {
  return stateV2(
    ...before,
    table(
      [column(literal("A")), column(literal("B"))],
      [row(cell(literal("a1")), cell(literal("b1"))), row(cell(literal("a2")), cell(literal("b2")))],
    ),
    paragraph(literal("etter")),
  );
}

describe("insertTable", () => {
  test("inserts a table after the paragraph and a paragraph after it when it would be last", () => {
    const state = stateV2(paragraph(literal("hello")));
    const result = run(state, insertTable, inBlock(0, 0, 5), 2, 3);

    expect(result.redigertBrev.blocks.map((block) => block.type)).toEqual(["PARAGRAPH", "TABLE", "PARAGRAPH"]);
    const inserted = tableAtIndex(result, 1);
    expect(inserted.rows).toHaveLength(2);
    expect(headerTexts(inserted)).toEqual([["Kolonne 1"], ["Kolonne 2"], ["Kolonne 3"]]);
    expect(inserted.rows.every((r) => r.cells.length === 3)).toBe(true);
    expect(result.focus).toEqual(header(1, 0));
    expect(result.saveStatus).toBe("DIRTY");
  });

  test("does not add a trailing paragraph when the table is not last", () => {
    const state = stateV2(paragraph(literal("first")), paragraph(literal("second")));
    const result = run(state, insertTable, inBlock(0), 2, 2);

    expect(result.redigertBrev.blocks.map((block) => block.type)).toEqual(["PARAGRAPH", "TABLE", "PARAGRAPH"]);
    expect(texts(textBlockAt(result, 2).content)).toEqual(["second"]);
  });

  test("splits the paragraph after the content at the caret", () => {
    const state = stateV2(paragraph(literal("før"), variable("v"), literal("etter")));
    const original = textBlockAt(state, 0);
    const result = run(state, insertTable, inBlock(0, 1, 0), 1, 1);

    expect(result.redigertBrev.blocks.map((block) => block.type)).toEqual(["PARAGRAPH", "TABLE", "PARAGRAPH"]);
    expect(texts(textBlockAt(result, 0).content)).toEqual(["før", "v", ""]);
    expect(textBlockAt(result, 0).deletedContent).toEqual([original.content[2].id]);
    expect(textBlockAt(result, 2).content).toEqual([original.content[2]]);
  });

  test("inserts the table after a list when the caret is in an item", () => {
    const state = stateV2(list(item(literal("punkt"))), paragraph(literal("etter")));
    const result = run(state, insertTable, caret({ kind: "item", block: 0, item: 0 }), 1, 1);

    expect(result.redigertBrev.blocks.map((block) => block.type)).toEqual(["ITEM_LIST", "TABLE", "PARAGRAPH"]);
  });

  test("does nothing in the title", () => {
    const state = stateV2(paragraph(literal("hello")));

    expect(run(state, insertTable, caret({ kind: "title" }), 1, 1).redigertBrev).toBe(state.redigertBrev);
  });
});

describe("removeTable", () => {
  test("removes the table and focuses the end of the previous block", () => {
    const state = twoByTwo(paragraph(literal("før")));
    const result = run(state, removeTable, inCell(1, 0, 0));

    expect(result.redigertBrev.blocks.map((block) => block.type)).toEqual(["PARAGRAPH", "PARAGRAPH"]);
    expect(result.redigertBrev.deletedBlocks).toEqual([blockAt(state, 1).id]);
    expect(result.focus).toEqual(inBlock(0, 0, 3));
  });

  test("focuses the next block when the table is first", () => {
    const result = run(twoByTwo(), removeTable, header(0, 1));

    expect(result.focus).toEqual(inBlock(0));
  });

  test("joins lists around the removed table", () => {
    const state = stateV2(
      list(item(literal("a"))),
      table([column(literal("A"))], [row(cell(literal("a1")))]),
      list(item(literal("b"))),
    );
    const result = run(state, removeTable, inCell(1, 0, 0));

    expect(result.redigertBrev.blocks).toHaveLength(1);
    expect(result.focus).toEqual(caret({ kind: "item", block: 0, item: 0 }, 0, 1));
  });

  test("adds an empty paragraph when the table was the only block", () => {
    const state = stateV2(table([column(literal("A"))], [row(cell(literal("a1")))]));
    const result = run(state, removeTable, inCell(0, 0, 0));

    expect(textBlockAt(result, 0).type).toBe("PARAGRAPH");
    expect(result.focus).toEqual(inBlock(0));
  });
});

describe("removeTableRow", () => {
  test("deletes a row and keeps the table", () => {
    const state = twoByTwo();
    const result = run(state, removeTableRow, inCell(0, 1, 1));

    const t = tableAtIndex(result, 0);
    expect(rowTexts(t)).toEqual([[["a1"], ["b1"]]]);
    expect(t.deletedRows).toEqual([tableAtIndex(state, 0).rows[1].id]);
    expect(result.focus).toEqual(inCell(0, 0, 0));
  });

  test("replaces the last row with an empty row", () => {
    const once = run(twoByTwo(), removeTableRow, inCell(0, 1, 0));
    const result = run(once, removeTableRow, inCell(0, 0, 0));

    const t = tableAtIndex(result, 0);
    expect(rowTexts(t)).toEqual([[[""], [""]]]);
    expect(t.deletedRows).toHaveLength(2);
    expect(result.focus).toEqual(inCell(0, 0, 0));
  });

  test("clears the header when focus is in the header", () => {
    const state = twoByTwo();
    const result = run(state, removeTableRow, header(0, 1));

    const t = tableAtIndex(result, 0);
    expect(headerTexts(t)).toEqual([[""], [""]]);
    expect(t.header.colSpec[0].deletedContent).toEqual([tableAtIndex(state, 0).header.colSpec[0].content[0].id]);
    expect(t.rows).toHaveLength(2);
    expect(result.focus).toEqual(inCell(0, 0, 0));
  });
});

describe("removeTableColumn", () => {
  test("removes the column from header and all rows", () => {
    const state = twoByTwo();
    const result = run(state, removeTableColumn, inCell(0, 1, 1));

    const t = tableAtIndex(result, 0);
    expect(headerTexts(t)).toEqual([["A"]]);
    expect(rowTexts(t)).toEqual([[["a1"]], [["a2"]]]);
    expect(t.header.deletedColSpecs).toEqual([tableAtIndex(state, 0).header.colSpec[1].id]);
    expect(t.rows[0].deletedCells).toEqual([tableAtIndex(state, 0).rows[0].cells[1].id]);
    expect(result.focus).toEqual(inCell(0, 1, 0));
  });

  test("renumbers default header labels", () => {
    const state = stateV2(
      table(
        [column(literal("Kolonne 1")), column(literal("Kolonne 2")), column(literal("Egen"))],
        [row(cell(), cell(), cell())],
      ),
    );
    const result = run(state, removeTableColumn, header(0, 0));

    expect(headerTexts(tableAtIndex(result, 0))).toEqual([["Kolonne 1"], ["Egen"]]);
    expect(result.focus).toEqual(header(0, 0));
  });

  test("removes the table when removing the only column", () => {
    const state = stateV2(paragraph(literal("før")), table([column(literal("A"))], [row(cell(literal("a1")))]));
    const result = run(state, removeTableColumn, inCell(1, 0, 0));

    expect(result.redigertBrev.blocks.map((block) => block.type)).toEqual(["PARAGRAPH"]);
  });
});

describe("insertTableColumn", () => {
  test("inserts a column to the left and keeps focus in the same cell", () => {
    const result = run(twoByTwo(), insertTableColumnLeft, inCell(0, 1, 1, 0, 1));

    const t = tableAtIndex(result, 0);
    expect(headerTexts(t)).toEqual([["A"], ["Kolonne 2"], ["B"]]);
    expect(rowTexts(t)).toEqual([
      [["a1"], [""], ["b1"]],
      [["a2"], [""], ["b2"]],
    ]);
    expect(result.focus).toEqual(inCell(0, 1, 2, 0, 1));
  });

  test("inserts a column to the right", () => {
    const result = run(twoByTwo(), insertTableColumnRight, header(0, 1));

    const t = tableAtIndex(result, 0);
    expect(headerTexts(t)).toEqual([["A"], ["B"], ["Kolonne 3"]]);
    expect(t.header.colSpec[2].id).toBeNull();
    expect(t.rows.every((r) => r.cells.length === 3)).toBe(true);
    expect(result.focus).toEqual(header(0, 1));
  });
});

describe("insertTableRow", () => {
  test("inserts a row above and focuses it", () => {
    const result = run(twoByTwo(), insertTableRowAbove, inCell(0, 1, 1));

    const t = tableAtIndex(result, 0);
    expect(rowTexts(t)).toEqual([
      [["a1"], ["b1"]],
      [[""], [""]],
      [["a2"], ["b2"]],
    ]);
    expect(t.rows[1].id).toBeNull();
    expect(result.focus).toEqual(inCell(0, 1, 0));
  });

  test("does not insert a row above the header", () => {
    const state = twoByTwo();

    expect(run(state, insertTableRowAbove, header(0, 0)).redigertBrev).toBe(state.redigertBrev);
  });

  test("inserts a row below and focuses it", () => {
    const result = run(twoByTwo(), insertTableRowBelow, inCell(0, 1, 1));

    expect(rowTexts(tableAtIndex(result, 0))[2]).toEqual([[""], [""]]);
    expect(result.focus).toEqual(inCell(0, 2, 0));
  });

  test("inserts the first body row when the header is focused", () => {
    const result = run(twoByTwo(), insertTableRowBelow, header(0, 1));

    expect(rowTexts(tableAtIndex(result, 0))[0]).toEqual([[""], [""]]);
    expect(result.focus).toEqual(inCell(0, 0, 0));
  });
});

describe("moveTableRow", () => {
  test("moves a row down and focus follows", () => {
    const state = twoByTwo();
    const result = run(state, moveTableRow, inCell(0, 0, 1, 0, 1), "down");

    const t = tableAtIndex(result, 0);
    expect(rowTexts(t)).toEqual([
      [["a2"], ["b2"]],
      [["a1"], ["b1"]],
    ]);
    expect(t.deletedRows).toEqual([]);
    expect(result.focus).toEqual(inCell(0, 1, 1, 0, 1));
  });

  test("moves a row up", () => {
    const result = run(twoByTwo(), moveTableRow, inCell(0, 1, 0), "up");

    expect(rowTexts(tableAtIndex(result, 0))[0]).toEqual([["a2"], ["b2"]]);
    expect(result.focus).toEqual(inCell(0, 0, 0));
  });

  test.each<[Caret, "up" | "down"]>([
    [inCell(0, 0, 0), "up"],
    [inCell(0, 1, 0), "down"],
    [header(0, 0), "down"],
  ])("does nothing at the edges or in the header (%o, %s)", (at, direction) => {
    const state = twoByTwo();

    expect(run(state, moveTableRow, at, direction).redigertBrev).toBe(state.redigertBrev);
  });
});

describe("promoteRowToHeader and demoteHeaderToRow", () => {
  function emptyHeader() {
    return stateV2(
      table(
        [column(newText("")), column(newText(""))],
        [row(cell(literal("a1")), cell(literal("b1"))), row(cell(literal("a2")), cell(literal("b2")))],
      ),
    );
  }

  test("promotes a row to an empty header", () => {
    const state = emptyHeader();
    const promoted = tableAtIndex(state, 0).rows[1];
    const result = run(state, promoteRowToHeader, inCell(0, 1, 0));

    const t = tableAtIndex(result, 0);
    expect(headerTexts(t)).toEqual([["a2"], ["b2"]]);
    expect(t.header.colSpec[0].content).toEqual(promoted.cells[0].content);
    expect(rowTexts(t)).toEqual([[["a1"], ["b1"]]]);
    expect(t.deletedRows).toEqual([promoted.id]);
    expect(result.focus).toEqual(header(0, 0));
  });

  test("labels the header when promoting an empty row", () => {
    const state = stateV2(table([column(newText("")), column(newText(""))], [row(cell(), cell())]));
    const result = run(state, promoteRowToHeader, inCell(0, 0, 1));

    expect(headerTexts(tableAtIndex(result, 0))).toEqual([["Kolonne 1"], ["Kolonne 2"]]);
  });

  test("does not promote when the header has content", () => {
    const state = twoByTwo();

    expect(run(state, promoteRowToHeader, inCell(0, 0, 0)).redigertBrev).toBe(state.redigertBrev);
  });

  test("demotes the header to the first row and leaves an empty header", () => {
    const state = twoByTwo();
    const original = tableAtIndex(state, 0);
    const result = run(state, demoteHeaderToRow, header(0, 1));

    const t = tableAtIndex(result, 0);
    expect(headerTexts(t)).toEqual([[""], [""]]);
    expect(t.header.colSpec[0].deletedContent).toEqual([original.header.colSpec[0].content[0].id]);
    expect(rowTexts(t)[0]).toEqual([["A"], ["B"]]);
    expect(t.rows[0].id).toBeNull();
    expect(t.rows[0].cells[0].content).toEqual(original.header.colSpec[0].content);
    expect(result.focus).toEqual(inCell(0, 0, 0));
  });

  test("does not demote an empty header", () => {
    const state = emptyHeader();

    expect(run(state, demoteHeaderToRow, header(0, 0)).redigertBrev).toBe(state.redigertBrev);
  });
});
