import { describe, expect, test } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { fontTypeOf, text } from "~/Brevredigering/LetterEditor/actions/common";
import {
  type Focus,
  type LetterEditorState,
  type LiteralIndex,
  type SelectionIndex,
  type TableCellIndex,
} from "~/Brevredigering/LetterEditor/model/state";
import { isTextContent } from "~/Brevredigering/LetterEditor/model/utils";
import {
  type Cell,
  type Content,
  ElementTags,
  FontType,
  type Item,
  type ItemList,
  type LiteralValue,
  type ParagraphBlock,
  type Table,
} from "~/types/brevbakerTypes";
import {
  cell,
  item,
  itemList,
  letter,
  literal,
  paragraph,
  row,
  select,
  table,
  variable,
} from "~test/support/letterEditorTestUtils";

type Position = LiteralIndex & { cursorPosition: number };

const at = <T extends LiteralIndex>(index: T, cursorPosition: number): T & { cursorPosition: number } => ({
  ...index,
  cursorPosition,
});
const range = (start: Position, end: Position = start): SelectionIndex => ({ start, end });

const bold = (l: LiteralValue): LiteralValue => ({ ...l, fontType: FontType.BOLD });

const textAndFont = (content: Content[]) => content.filter(isTextContent).map((c) => [text(c), fontTypeOf(c)]);

function switchFontType(state: LetterEditorState, fontType: FontType, selection?: SelectionIndex) {
  return Actions.switchFontType(state, fontType, selection);
}

function expectUnchanged(result: LetterEditorState, state: LetterEditorState) {
  expect(result.redigertBrev).toEqual(state.redigertBrev);
  expect(result.saveStatus).toBe(state.saveStatus);
}

// ─── Tabell ─────────────────────────────────────────────────────────────────

function tableLetter(header: LiteralValue[], body: LiteralValue[][]) {
  return letter(
    paragraph([
      table(
        [cell(...header)],
        body.map((c) => row(cell(...c))),
      ),
      literal(""),
    ]),
  );
}

function cellIndex(rowIndex: number, cellContentIndex = 0): TableCellIndex {
  return { blockIndex: 0, contentIndex: 0, rowIndex, cellIndex: 0, cellContentIndex };
}

function cellAt(state: LetterEditorState, rowIndex: number): Cell {
  const t = select<Table>(state, { blockIndex: 0, contentIndex: 0 });
  return rowIndex === -1 ? t.header.colSpec[0].headerContent : t.rows[rowIndex].cells[0];
}

describe("switchFontType i tabell", () => {
  describe("celle i tabellkropp", () => {
    test("endrer kun markert tekst, og originalen beholdes som første del", () => {
      const state = tableLetter([literal("Overskrift")], [[literal("Første andre tredje")]]);
      const original = cellAt(state, 0).text[0];

      const result = switchFontType(state, FontType.BOLD, range(at(cellIndex(0), 7), at(cellIndex(0), 12)));
      const c = cellAt(result, 0);

      expect(textAndFont(c.text)).toEqual([
        ["Første ", FontType.PLAIN],
        ["andre", FontType.BOLD],
        [" tredje", FontType.PLAIN],
      ]);
      expect(c.text.map((t) => t.id)).toEqual([original.id, null, null]);
      expect(c.text[0].parentId).toBe(original.parentId);
      expect(c.deletedContent).toEqual([]);
      expect(result.focus).toEqual({
        ...at(cellIndex(0, 1), 0),
        pendingSelection: range(at(cellIndex(0, 1), 0), at(cellIndex(0, 1), 5)),
      });
      expect(result.saveStatus).toBe("DIRTY");
    });

    test("markering av hele literalen beholder id, parentId og tags", () => {
      const state = tableLetter([literal("Overskrift")], [[literal({ text: "Alt", tags: [ElementTags.FRITEKST] })]]);
      const original = cellAt(state, 0).text[0] as LiteralValue;

      const result = switchFontType(state, FontType.ITALIC, range(at(cellIndex(0), 0), at(cellIndex(0), 3)));
      const changed = cellAt(result, 0).text[0] as LiteralValue;

      expect(changed).toEqual({ ...original, editedFontType: FontType.ITALIC });
      expect(cellAt(result, 0).deletedContent).toEqual([]);
      expect(result.focus.pendingSelection).toEqual(range(at(cellIndex(0), 0), at(cellIndex(0), 3)));
    });

    test("uten markering endres kun ordet under markøren", () => {
      const state = tableLetter([literal("Overskrift")], [[literal("Første andre tredje")]]);

      const result = switchFontType(state, FontType.BOLD, range(at(cellIndex(0), 9)));

      expect(textAndFont(cellAt(result, 0).text)).toEqual([
        ["Første ", FontType.PLAIN],
        ["andre", FontType.BOLD],
        [" tredje", FontType.PLAIN],
      ]);
      expect(result.focus).toEqual({ ...at(cellIndex(0, 1), 2), pendingSelection: range(at(cellIndex(0, 1), 2)) });
    });

    test("uten selection brukes markørposisjonen i focus", () => {
      const state = tableLetter([literal("Overskrift")], [[literal("Første andre tredje")]]);

      const result = switchFontType({ ...state, focus: at(cellIndex(0), 9) as Focus }, FontType.BOLD);

      expect(textAndFont(cellAt(result, 0).text)[1]).toEqual(["andre", FontType.BOLD]);
    });

    test("markør på mellomrom gjør ingen endring", () => {
      const state = tableLetter([literal("Overskrift")], [[literal("Første andre")]]);

      expectUnchanged(switchFontType(state, FontType.BOLD, range(at(cellIndex(0), 6))), state);
    });

    test("samme fonttype på markert fet tekst gjør den normal igjen", () => {
      const state = tableLetter([literal("Overskrift")], [[literal("Første andre tredje")]]);
      const withBold = switchFontType(state, FontType.BOLD, range(at(cellIndex(0), 7), at(cellIndex(0), 12)));

      const result = switchFontType(withBold, FontType.BOLD, range(at(cellIndex(0, 1), 0), at(cellIndex(0, 1), 5)));

      expect(textAndFont(cellAt(result, 0).text)).toEqual([
        ["Første ", FontType.PLAIN],
        ["andre", FontType.PLAIN],
        [" tredje", FontType.PLAIN],
      ]);
    });

    test("delvis markering i fet maltekst beholder fet skrift rundt markeringen", () => {
      const state = tableLetter([literal("Overskrift")], [[bold(literal("Første andre tredje"))]]);

      const result = switchFontType(state, FontType.ITALIC, range(at(cellIndex(0), 7), at(cellIndex(0), 12)));

      expect(textAndFont(cellAt(result, 0).text)).toEqual([
        ["Første ", FontType.BOLD],
        ["andre", FontType.ITALIC],
        [" tredje", FontType.BOLD],
      ]);
    });

    test("tilbake til malens fonttype fjerner editedFontType", () => {
      const state = tableLetter([literal("Overskrift")], [[bold(literal("Fet"))]]);

      const result = switchFontType(state, FontType.BOLD, range(at(cellIndex(0), 0), at(cellIndex(0), 3)));
      const changed = cellAt(result, 0).text[0] as LiteralValue;

      expect(changed.fontType).toBe(FontType.BOLD);
      expect(changed.editedFontType).toBe(FontType.PLAIN);

      const back = switchFontType(result, FontType.BOLD, range(at(cellIndex(0), 0), at(cellIndex(0), 3)));
      expect((cellAt(back, 0).text[0] as LiteralValue).editedFontType).toBeNull();
    });

    test("markering over flere literals i samme celle", () => {
      const state = tableLetter([literal("Overskrift")], [[literal("Ett to "), literal("tre "), literal("fire fem")]]);

      const result = switchFontType(state, FontType.BOLD, range(at(cellIndex(0, 0), 4), at(cellIndex(0, 2), 4)));

      expect(textAndFont(cellAt(result, 0).text)).toEqual([
        ["Ett ", FontType.PLAIN],
        ["to ", FontType.BOLD],
        ["tre ", FontType.BOLD],
        ["fire", FontType.BOLD],
        [" fem", FontType.PLAIN],
      ]);
      expect(result.focus.pendingSelection).toEqual(range(at(cellIndex(0, 1), 0), at(cellIndex(0, 3), 4)));
    });

    test("markering der alt allerede er fet gjøres normal, ellers fet", () => {
      const state = tableLetter([literal("Overskrift")], [[bold(literal("Fet ")), literal("normal")]]);
      const whole = range(at(cellIndex(0, 0), 0), at(cellIndex(0, 1), 6));

      const allBold = switchFontType(state, FontType.BOLD, whole);
      expect(textAndFont(cellAt(allBold, 0).text)).toEqual([
        ["Fet ", FontType.BOLD],
        ["normal", FontType.BOLD],
      ]);

      const allPlain = switchFontType(allBold, FontType.BOLD, whole);
      expect(textAndFont(cellAt(allPlain, 0).text)).toEqual([
        ["Fet ", FontType.PLAIN],
        ["normal", FontType.PLAIN],
      ]);
    });

    test("markering over flere celler gjør ingen endring", () => {
      const state = tableLetter([literal("Overskrift")], [[literal("En")], [literal("To")]]);

      expectUnchanged(switchFontType(state, FontType.BOLD, range(at(cellIndex(0), 0), at(cellIndex(1), 2))), state);
    });
  });

  describe("tabelloverskrift", () => {
    test("kursiv endrer kun markert tekst", () => {
      const state = tableLetter([literal("Første andre")], [[literal("Kropp")]]);

      const result = switchFontType(state, FontType.ITALIC, range(at(cellIndex(-1), 7), at(cellIndex(-1), 12)));

      expect(textAndFont(cellAt(result, -1).text)).toEqual([
        ["Første ", FontType.PLAIN],
        ["andre", FontType.ITALIC],
      ]);
      expect(result.focus.pendingSelection).toEqual(range(at(cellIndex(-1, 1), 0), at(cellIndex(-1, 1), 5)));
    });

    test("fet gjør ingen endring", () => {
      const state = tableLetter([literal("Første andre")], [[literal("Kropp")]]);

      expectUnchanged(switchFontType(state, FontType.BOLD, range(at(cellIndex(-1), 7), at(cellIndex(-1), 12))), state);
    });
  });
});

// ─── Avsnitt ────────────────────────────────────────────────────────────────

describe("switchFontType i avsnitt", () => {
  const blockIndex = (contentIndex: number) => ({ blockIndex: 0, contentIndex });
  const contentOf = (state: LetterEditorState) => select<ParagraphBlock>(state, { blockIndex: 0 }).content;

  test("delvis markering i kursiv maltekst beholder kursiv rundt markeringen", () => {
    const state = letter(paragraph([{ ...literal("Første andre tredje"), fontType: FontType.ITALIC }]));

    const result = switchFontType(state, FontType.BOLD, range(at(blockIndex(0), 7), at(blockIndex(0), 12)));

    expect(textAndFont(contentOf(result))).toEqual([
      ["Første ", FontType.ITALIC],
      ["andre", FontType.BOLD],
      [" tredje", FontType.ITALIC],
    ]);
  });

  test("ordet under markøren i kursiv maltekst beholder kursiv rundt ordet", () => {
    const state = letter(paragraph([{ ...literal("Første andre tredje"), fontType: FontType.ITALIC }]));

    const result = switchFontType(state, FontType.BOLD, range(at(blockIndex(0), 9)));

    expect(textAndFont(contentOf(result))).toEqual([
      ["Første ", FontType.ITALIC],
      ["andre", FontType.BOLD],
      [" tredje", FontType.ITALIC],
    ]);
  });

  test("ordet under markøren som er hele literalen beholder tags", () => {
    const state = letter(paragraph([literal({ text: "Ord", tags: [ElementTags.FRITEKST] })]));

    const result = switchFontType(state, FontType.BOLD, range(at(blockIndex(0), 1)));

    expect((contentOf(result)[0] as LiteralValue).tags).toEqual([ElementTags.FRITEKST]);
    expect(result.focus).toEqual({ ...at(blockIndex(0), 1), pendingSelection: range(at(blockIndex(0), 1)) });
  });

  test("markering over flere literals hopper over variabler", () => {
    const state = letter(paragraph([literal("Hei "), variable("Navn"), literal(" og velkommen")]));

    const result = switchFontType(state, FontType.BOLD, range(at(blockIndex(0), 0), at(blockIndex(2), 3)));

    expect(textAndFont(contentOf(result))).toEqual([
      ["Hei ", FontType.BOLD],
      ["Navn", FontType.PLAIN],
      [" og", FontType.BOLD],
      [" velkommen", FontType.PLAIN],
    ]);
    expect(result.focus.pendingSelection).toEqual(range(at(blockIndex(0), 0), at(blockIndex(2), 3)));
  });

  test("markering som slutter i en variabel beholdes over variabelen", () => {
    const state = letter(paragraph([literal("Hei "), variable("Navn"), literal(" og velkommen")]));

    const result = switchFontType(state, FontType.BOLD, range(at(blockIndex(0), 0), at(blockIndex(1), 4)));

    expect(textAndFont(contentOf(result))).toEqual([
      ["Hei ", FontType.BOLD],
      ["Navn", FontType.PLAIN],
      [" og velkommen", FontType.PLAIN],
    ]);
    expect(result.focus.pendingSelection).toEqual(range(at(blockIndex(0), 0), at(blockIndex(1), 4)));
  });

  test("markering som starter i en variabel og slutter midt i en literal følger splittingen", () => {
    const state = letter(paragraph([literal("Hei "), variable("Navn"), literal(" og velkommen")]));

    const result = switchFontType(state, FontType.BOLD, range(at(blockIndex(1), 2), at(blockIndex(2), 3)));

    expect(textAndFont(contentOf(result))).toEqual([
      ["Hei ", FontType.PLAIN],
      ["Navn", FontType.PLAIN],
      [" og", FontType.BOLD],
      [" velkommen", FontType.PLAIN],
    ]);
    expect(result.focus.pendingSelection).toEqual(range(at(blockIndex(1), 2), at(blockIndex(2), 3)));
  });

  test("endepunkter etter en splittet literal forskyves", () => {
    const state = letter(paragraph([literal("Hei du "), variable("Navn"), literal(" og velkommen")]));

    const result = switchFontType(state, FontType.BOLD, range(at(blockIndex(0), 4), at(blockIndex(2), 3)));

    expect(textAndFont(contentOf(result))).toEqual([
      ["Hei ", FontType.PLAIN],
      ["du ", FontType.BOLD],
      ["Navn", FontType.PLAIN],
      [" og", FontType.BOLD],
      [" velkommen", FontType.PLAIN],
    ]);
    expect(result.focus.pendingSelection).toEqual(range(at(blockIndex(1), 0), at(blockIndex(3), 3)));
  });

  test("markering over en liste i avsnittet gjør ingen endring", () => {
    const state = letter(paragraph([literal("Før"), itemList({ items: [item(literal("Punkt"))] }), literal("Etter")]));

    expectUnchanged(switchFontType(state, FontType.BOLD, range(at(blockIndex(0), 0), at(blockIndex(2), 5))), state);
  });

  test("markering over flere avsnitt gjør ingen endring", () => {
    const state = letter(paragraph([literal("En")]), paragraph([literal("To")]));

    expectUnchanged(
      switchFontType(
        state,
        FontType.BOLD,
        range(at(blockIndex(0), 0), { blockIndex: 1, contentIndex: 0, cursorPosition: 2 }),
      ),
      state,
    );
  });
});

// ─── Punktliste ─────────────────────────────────────────────────────────────

describe("switchFontType i punktliste", () => {
  const itemIndex = (index: number, itemContentIndex: number) => ({
    blockIndex: 0,
    contentIndex: 0,
    itemIndex: index,
    itemContentIndex,
  });
  const itemAt = (state: LetterEditorState, index: number) =>
    select<ItemList>(state, { blockIndex: 0, contentIndex: 0 }).items[index] as Item;

  test("markering over flere literals i samme punkt", () => {
    const state = letter(paragraph([itemList({ items: [item(literal("Ett to "), bold(literal("tre fire")))] })]));

    const result = switchFontType(state, FontType.ITALIC, range(at(itemIndex(0, 0), 4), at(itemIndex(0, 1), 3)));

    expect(textAndFont(itemAt(result, 0).content)).toEqual([
      ["Ett ", FontType.PLAIN],
      ["to ", FontType.ITALIC],
      ["tre", FontType.ITALIC],
      [" fire", FontType.BOLD],
    ]);
    expect(result.focus.pendingSelection).toEqual(range(at(itemIndex(0, 1), 0), at(itemIndex(0, 2), 3)));
  });

  test("markering over flere punkt gjør ingen endring", () => {
    const state = letter(paragraph([itemList({ items: [item(literal("En")), item(literal("To"))] })]));

    expectUnchanged(switchFontType(state, FontType.BOLD, range(at(itemIndex(0, 0), 0), at(itemIndex(1, 0), 2))), state);
  });
});

describe("toggle list i tabell", () => {
  test.each([
    ["punktliste", Actions.toggleBulletList],
    ["nummerert liste", Actions.toggleNumberList],
  ])("%s gjør ingen endring i tabellceller", (_, toggle) => {
    const state = tableLetter([literal("Overskrift")], [[literal("Kropp")]]);
    for (const index of [cellIndex(-1), cellIndex(0)]) {
      const focus = at(index, 0);
      const result = toggle({ ...state, focus }, focus);
      expect(result.redigertBrev).toEqual(state.redigertBrev);
    }
  });
});
