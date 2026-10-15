import { afterEach, describe, expect, test } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { type Focus, type LetterEditorState, type TableCellIndex } from "~/Brevredigering/LetterEditor/model/state";
import { type Cell, FontType, type LiteralValue, type Table } from "~/types/brevbakerTypes";
import { cell, letter, literal, paragraph, row, select, table } from "~test/support/letterEditorTestUtils";

function selectText(text: string, start: number, end: number = start) {
  const node = document.createTextNode(text);
  document.body.replaceChildren(node);
  globalThis.getSelection()?.setBaseAndExtent(node, start, node, end);
}

function tableLetter(headerText: string, bodyText: string) {
  return letter(
    paragraph([table([cell(literal({ text: headerText }))], [row(cell(literal({ text: bodyText })))]), literal("")]),
  );
}

type TableFocus = TableCellIndex & Focus;

function cellIndex(rowIndex: number, cellContentIndex = 0): TableFocus {
  return { blockIndex: 0, contentIndex: 0, rowIndex, cellIndex: 0, cellContentIndex, cursorPosition: 0 };
}

function cellAt(state: LetterEditorState, rowIndex: number): Cell {
  const t = select<Table>(state, { blockIndex: 0, contentIndex: 0 });
  return rowIndex === -1 ? t.header.colSpec[0].headerContent : t.rows[rowIndex].cells[0];
}

function switchFontType(state: LetterEditorState, focus: TableFocus, fontType: FontType) {
  return Actions.switchFontType({ ...state, focus }, fontType);
}

const textAndFont = (c: Cell) =>
  (c.text as LiteralValue[]).map((l) => [l.editedText ?? l.text, l.editedFontType ?? l.fontType]);

afterEach(() => {
  globalThis.getSelection()?.removeAllRanges();
  document.body.replaceChildren();
});

describe("switchFontType i tabell", () => {
  describe("celle i tabellkropp", () => {
    test("endrer kun markert tekst", () => {
      const state = tableLetter("Overskrift", "Første andre tredje");
      const original = cellAt(state, 0).text[0];
      selectText("Første andre tredje", 7, 12);

      const result = switchFontType(state, cellIndex(0), FontType.BOLD);
      const c = cellAt(result, 0);

      expect(textAndFont(c)).toEqual([
        ["Første ", FontType.PLAIN],
        ["andre", FontType.BOLD],
        [" tredje", FontType.PLAIN],
      ]);
      expect(c.text.every((t) => t.id === null)).toBe(true);
      expect(c.deletedContent).toEqual([original.id]);
      expect(result.focus).toEqual({ ...cellIndex(0, 1), cursorPosition: 5, selectionStart: 0 });
      expect(result.saveStatus).toBe("DIRTY");
    });

    test("markering av hele literalen beholder id", () => {
      const state = tableLetter("Overskrift", "Alt");
      const original = cellAt(state, 0).text[0];
      selectText("Alt", 0, 3);

      const result = switchFontType(state, cellIndex(0), FontType.ITALIC);
      const c = cellAt(result, 0);

      expect(textAndFont(c)).toEqual([["Alt", FontType.ITALIC]]);
      expect(c.text[0].id).toBe(original.id);
      expect(result.focus).toEqual({ ...cellIndex(0), cursorPosition: 3, selectionStart: 0 });
      expect(c.deletedContent).toEqual([]);
    });

    test("uten markering endres kun ordet under markøren", () => {
      const state = tableLetter("Overskrift", "Første andre tredje");
      selectText("Første andre tredje", 9);

      const result = switchFontType(state, { ...cellIndex(0), cursorPosition: 9 }, FontType.BOLD);

      expect(textAndFont(cellAt(result, 0))).toEqual([
        ["Første ", FontType.PLAIN],
        ["andre", FontType.BOLD],
        [" tredje", FontType.PLAIN],
      ]);
      expect(result.focus).toEqual({ ...cellIndex(0, 1), cursorPosition: 2, selectionStart: 2 });
    });

    test("markør på mellomrom gjør ingen endring", () => {
      const state = tableLetter("Overskrift", "Første andre");
      selectText("Første andre", 6);

      const result = switchFontType(state, { ...cellIndex(0), cursorPosition: 6 }, FontType.BOLD);

      expect(cellAt(result, 0)).toEqual(cellAt(state, 0));
      expect(result.saveStatus).toBe(state.saveStatus);
    });

    test("samme fonttype på markert fet tekst gjør den normal igjen", () => {
      const state = tableLetter("Overskrift", "Første andre tredje");
      selectText("Første andre tredje", 7, 12);
      const bold = switchFontType(state, cellIndex(0), FontType.BOLD);

      selectText("andre", 0, 5);
      const result = switchFontType(bold, cellIndex(0, 1), FontType.BOLD);

      expect(textAndFont(cellAt(result, 0))).toEqual([
        ["Første ", FontType.PLAIN],
        ["andre", FontType.PLAIN],
        [" tredje", FontType.PLAIN],
      ]);
    });
  });

  describe("tabelloverskrift", () => {
    test("kursiv endrer kun markert tekst", () => {
      const state = tableLetter("Første andre", "Kropp");
      selectText("Første andre", 7, 12);

      const result = switchFontType(state, cellIndex(-1), FontType.ITALIC);

      expect(textAndFont(cellAt(result, -1))).toEqual([
        ["Første ", FontType.PLAIN],
        ["andre", FontType.ITALIC],
      ]);
      expect(result.focus).toEqual({ ...cellIndex(-1, 1), cursorPosition: 5, selectionStart: 0 });
    });

    test("fet gjør ingen endring", () => {
      const state = tableLetter("Første andre", "Kropp");
      selectText("Første andre", 7, 12);

      const result = switchFontType(state, cellIndex(-1), FontType.BOLD);

      expect(cellAt(result, -1)).toEqual(cellAt(state, -1));
      expect(result.saveStatus).toBe(state.saveStatus);
    });
  });
});

describe("toggle list i tabell", () => {
  test.each([
    ["punktliste", Actions.toggleBulletList],
    ["nummerert liste", Actions.toggleNumberList],
  ])("%s gjør ingen endring i tabellceller", (_, toggle) => {
    const state = tableLetter("Overskrift", "Kropp");
    for (const focus of [cellIndex(-1), cellIndex(0)]) {
      const result = toggle({ ...state, focus }, focus);
      expect(result.redigertBrev).toEqual(state.redigertBrev);
    }
  });
});
