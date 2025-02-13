import { expect } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { text } from "~/Brevredigering/LetterEditor/actions/common";
import type { LiteralValue } from "~/types/brevbakerTypes";

import { item, itemList, letter, literal, paragraph, select } from "../utils";

describe("LetterEditorActions.paste", () => {
  describe("format: text/plain", () => {
    test("start of a literal", () => {
      const state = letter(paragraph(literal({ text: "Her har vi noe" })));
      const clipboard = new MockDataTransfer({ "text/plain": "ikke " });
      const result = Actions.paste(state, { blockIndex: 0, contentIndex: 0 }, 0, clipboard);

      expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("ikke Her har vi noe");
    });
    test("inside of a literal", () => {
      const state = letter(paragraph(literal({ text: "Her har vi noe" })));
      const clipboard = new MockDataTransfer({ "text/plain": " ikke" });
      const result = Actions.paste(state, { blockIndex: 0, contentIndex: 0 }, 10, clipboard);

      expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("Her har vi ikke noe");
    });

    test("end of a literal", () => {
      const state = letter(paragraph(literal({ text: "Her har vi noe" })));
      const clipboard = new MockDataTransfer({ "text/plain": " ikke" });
      const result = Actions.paste(state, { blockIndex: 0, contentIndex: 0 }, 14, clipboard);

      expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("Her har vi noe ikke");
    });

    test("in an item", () => {
      const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
      const state = letter(paragraph(itemList({ items: [item(literal({ text: "Her har vi noe" }))] })));
      const clipboard = new MockDataTransfer({ "text/plain": " ikke" });
      const result = Actions.paste(state, index, 10, clipboard);

      expect(text(select<LiteralValue>(result, index))).toEqual("Her har vi ikke noe");
    });
    test("should update cursorPosition", () => {
      const state = letter(paragraph(literal({ text: "Hei" })));
      const clipboard = new MockDataTransfer({ "text/plain": " Hade" });
      const result = Actions.paste(state, { blockIndex: 0, contentIndex: 0 }, 10, clipboard);

      expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("Hei Hade");
      expect(result.focus.cursorPosition).toEqual(8);
    });
  });

  describe("format: text/html", () => {
    describe("paste into a literal", () => {
      describe("start of a literal", () => {
        test("inserts a single word", () => {
          const index = { blockIndex: 0, contentIndex: 0 };
          const state = letter(paragraph(literal({ text: "Teksten min" })));
          const clipboard = new MockDataTransfer({ "text/html": "<span>1</span>" });
          const result = Actions.paste(state, index, 0, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("1Teksten min");
          expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 1 });
        });
        test("inserts multiple words", () => {
          const index = { blockIndex: 0, contentIndex: 0 };
          const state = letter(paragraph(literal({ text: "Teksten min" })));
          const clipboard = new MockDataTransfer({ "text/html": "<span>1</span><span> 2</span" });
          const result = Actions.paste(state, index, 0, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("1 2Teksten min");
          expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 3 });
        });
        test("inserts single paragraph", () => {
          const index = { blockIndex: 0, contentIndex: 0 };
          const state = letter(paragraph(literal({ text: "Teksten min" })));
          const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
          const result = Actions.paste(state, index, 0, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("1");
          expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual("Teksten min");
          expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 0 });
        });
        test("inserts multiple paragraphs", () => {
          const index = { blockIndex: 0, contentIndex: 0 };
          const state = letter(paragraph(literal({ text: "Teksten min" })));
          const clipboard = new MockDataTransfer({ "text/html": "<p>1</p><p>2</p>" });
          const result = Actions.paste(state, index, 0, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("1");
          expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual("2");
          expect(text(select<LiteralValue>(result, { blockIndex: 2, contentIndex: 0 }))).toEqual("Teksten min");
          expect(result.focus).toEqual({ blockIndex: 2, contentIndex: 0, cursorPosition: 0 });
        });
        test("inserts ul list", () => {
          const index = { blockIndex: 0, contentIndex: 0 };
          const state = letter(paragraph(literal({ text: "Teksten min" })));
          const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
          const result = Actions.paste(state, index, 0, clipboard);

          expect(
            text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0, itemContentIndex: 0, itemIndex: 0 })),
          ).toEqual("1");
          expect(
            text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0, itemContentIndex: 0, itemIndex: 1 })),
          ).toEqual("2");
          expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual("Teksten min");
          expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 0 });
        });

        test("paragraph + ul", () => {
          const clipboard = new MockDataTransfer({
            "text/html":
              "<div><p><span><span>Punktliste</span></span><span> </span></p></div><div><ul><li><p><span><span>Første punkt</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Andre punkt</span></span><span> </span></p></li></ul></div>",
          });
          const state = letter(paragraph(literal({ text: "Hei" })));
          const result = Actions.paste(state, { blockIndex: 0, contentIndex: 0 }, 0, clipboard);

          expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("Punktliste");
          expect(
            text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 1, itemContentIndex: 0, itemIndex: 0 })),
          ).toEqual("Første punkt");
          expect(
            text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 1, itemContentIndex: 0, itemIndex: 1 })),
          ).toEqual("Andre punkt");
          expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual("Hei");
          expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 0 });
        });
      });

      describe("in the middle of a literal", () => {
        test("inserts a single word", () => {
          const index = { blockIndex: 0, contentIndex: 0 };
          const state = letter(paragraph(literal({ text: "Her har vi noe" })));
          const clipboard = new MockDataTransfer({ "text/html": "<span>ikke</span>" });
          const result = Actions.paste(state, index, 10, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("Her har vi ikke noe");
          expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 15 });
        });
        test("inserts multiple words", () => {
          const index = { blockIndex: 0, contentIndex: 0 };
          const state = letter(paragraph(literal({ text: "Her har vi noe" })));
          const clipboard = new MockDataTransfer({ "text/html": "<span> da</span><span> ikke</span>" });
          const result = Actions.paste(state, index, 10, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("Her har vida ikke noe");
          expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 17 });
        });
        test("inserts single paragraph", () => {
          const index = { blockIndex: 0, contentIndex: 0 };
          const state = letter(paragraph(literal({ text: "Her har vi noe" })));
          const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
          const result = Actions.paste(state, index, 10, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("Her har vi1");
          expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual(" noe");
          expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 0 });
        });
        test("inserts multiple paragraphs", () => {
          const index = { blockIndex: 0, contentIndex: 0 };
          const state = letter(paragraph(literal({ text: "Her har vi noe" })));
          const clipboard = new MockDataTransfer({ "text/html": "<p> da</p><p> ikke</p>" });
          const result = Actions.paste(state, index, 10, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("Her har vida");
          expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual("ikke");
          expect(text(select<LiteralValue>(result, { blockIndex: 2, contentIndex: 0 }))).toEqual(" noe");
          expect(result.focus).toEqual({ blockIndex: 2, contentIndex: 0, cursorPosition: 0 });
        });
        test("inserts ul list", () => {
          const index = { blockIndex: 0, contentIndex: 0 };
          const state = letter(paragraph(literal({ text: "Teksten min" })));
          const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
          const result = Actions.paste(state, index, 7, clipboard);

          expect(
            text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0, itemContentIndex: 0, itemIndex: 0 })),
          ).toEqual("Teksten1");
          expect(
            text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0, itemContentIndex: 0, itemIndex: 1 })),
          ).toEqual("2");
          expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual(" min");
          expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 0 });
        });

        test("paragraph + ul", () => {
          const clipboard = new MockDataTransfer({
            "text/html":
              "<div><p><span><span>Punktliste</span></span><span> </span></p></div><div><ul><li><p><span><span>Første punkt</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Andre punkt</span></span><span> </span></p></li></ul></div>",
          });
          const state = letter(paragraph(literal({ text: "Help" })));
          const result = Actions.paste(state, { blockIndex: 0, contentIndex: 0 }, 2, clipboard);

          expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("HePunktliste");
          expect(
            text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 1, itemContentIndex: 0, itemIndex: 0 })),
          ).toEqual("Første punkt");
          expect(
            text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 1, itemContentIndex: 0, itemIndex: 1 })),
          ).toEqual("Andre punkt");
          expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual("lp");
          expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 0 });
        });
      });
      describe("at the end of a literal", () => {
        test("inserts a single word", () => {
          const index = { blockIndex: 0, contentIndex: 0 };
          const state = letter(paragraph(literal({ text: "Teksten min" })));
          const clipboard = new MockDataTransfer({ "text/html": "<span>Single</span>" });
          const result = Actions.paste(state, index, 11, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("Teksten minSingle");
          expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 17 });
        });
        test("inserts multiple words", () => {
          const index = { blockIndex: 0, contentIndex: 0 };
          const state = letter(paragraph(literal({ text: "Teksten min" })));
          const clipboard = new MockDataTransfer({ "text/html": "<span>Single</span><span>Spanner</span>" });
          const result = Actions.paste(state, index, 11, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("Teksten minSingle Spanner");
          expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 25 });
        });
        test("inserts single paragraph", () => {
          const index = { blockIndex: 0, contentIndex: 0 };
          const state = letter(paragraph(literal({ text: "Teksten min" })));
          const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
          const result = Actions.paste(state, index, 11, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("Teksten min1");
          expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 12 });
        });
        test("inserts multiple paragraphs", () => {
          const index = { blockIndex: 0, contentIndex: 0 };
          const state = letter(paragraph(literal({ text: "Teksten min" })));
          const clipboard = new MockDataTransfer({ "text/html": "<p>1</p><p>2</p>" });
          const result = Actions.paste(state, index, 11, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("Teksten min1");
          expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual("2");
          expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 1 });
        });
        test("inserts ul list", () => {
          const index = { blockIndex: 0, contentIndex: 0 };
          const state = letter(paragraph(literal({ text: "Her har vi noe " })));
          const clipboard = new MockDataTransfer({
            "text/html": "<ul><li>annet</li><li>mer</li><li>enda mer</li></ul>",
          });
          const result = Actions.paste(state, index, 15, clipboard);

          expect(
            text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0, itemContentIndex: 0, itemIndex: 0 })),
          ).toEqual("Her har vi noe annet");
          expect(
            text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0, itemContentIndex: 0, itemIndex: 1 })),
          ).toEqual("mer");
          expect(
            text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0, itemContentIndex: 0, itemIndex: 2 })),
          ).toEqual("enda mer");
          expect(result.focus).toEqual({
            blockIndex: 0,
            contentIndex: 0,
            itemContentIndex: 0,
            itemIndex: 2,
            cursorPosition: 8,
          });
        });
        test("paragraph + ul", () => {
          const clipboard = new MockDataTransfer({
            "text/html":
              "<div><p><span><span>Punktliste</span></span><span> </span></p></div><div><ul><li><p><span><span>Første punkt</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Andre punkt</span></span><span> </span></p></li></ul></div>",
          });
          const state = letter(paragraph(literal({ text: "Hei" })));
          const result = Actions.paste(state, { blockIndex: 0, contentIndex: 0 }, 4, clipboard);

          expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("HeiPunktliste");
          expect(
            text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 1, itemContentIndex: 0, itemIndex: 0 })),
          ).toEqual("Første punkt");
          expect(
            text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 1, itemContentIndex: 0, itemIndex: 1 })),
          ).toEqual("Andre punkt");
          expect(result.focus).toEqual({
            blockIndex: 0,
            contentIndex: 1,
            itemContentIndex: 0,
            itemIndex: 1,
            cursorPosition: 11,
          });
        });
      });

      test("flere avsnitt i en kopiert tekst kommer som flere blokker", () => {
        const clipboard = new MockDataTransfer({
          "text/html":
            "<div><p><span><span>The</span><span>morning</span><span></span><span>sun</span><span></span><span>cast</span><span> a </span><span>golden</span><span></span><span>glow</span></span><span> </span></p></div><div><p><span><span>Inside a </span><span>cozy</span><span> café </span><span>on</span><span></span><span>the</span><span> corner</span></span><span> </span></p></div><div><ul><li><p><span><span>Punkt 1</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Punkt 2</span></span><span> </span></p></li></ul></div><div><p><span><span></span></span><span> </span></p></div><div><ul><li><p><span><span>Nytt punkt 1</span></span><span> </span></p></li></ul></div>",
        });
        const state = letter(paragraph(literal({ text: "Hei" })));
        const result = Actions.paste(state, { blockIndex: 0, contentIndex: 0 }, 4, clipboard);

        expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual(
          "HeiThe morning sun cast a golden glow",
        );
        expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual(
          "Inside a cozy café on the corner",
        );
        expect(
          text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 1, itemContentIndex: 0, itemIndex: 0 })),
        ).toEqual("Punkt 1");
        expect(
          text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 1, itemContentIndex: 0, itemIndex: 1 })),
        ).toEqual("Punkt 2");
        expect(text(select<LiteralValue>(result, { blockIndex: 2, contentIndex: 0 }))).toEqual(" ");
        expect(
          text(select<LiteralValue>(result, { blockIndex: 3, contentIndex: 0, itemContentIndex: 0, itemIndex: 0 })),
        ).toEqual("Nytt punkt 1");
        expect(result.focus).toEqual({
          blockIndex: 3,
          contentIndex: 0,
          itemContentIndex: 0,
          itemIndex: 0,
          cursorPosition: 12,
        });
      });
    });

    describe("paste into an item", () => {
      describe("start of literal", () => {
        test("inserts a single word", () => {
          const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
          const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
          const clipboard = new MockDataTransfer({ "text/html": "<span>1</span>" });
          const result = Actions.paste(state, index, 0, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("1Teksten min");
          expect(result.focus).toEqual({ ...index, cursorPosition: 1 });
        });
        test("inserts multiple words", () => {
          const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
          const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
          const clipboard = new MockDataTransfer({ "text/html": "<span>1</span><span> 2</span" });
          const result = Actions.paste(state, index, 0, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("1 2Teksten min");
          expect(result.focus).toEqual({ ...index, cursorPosition: 3 });
        });
        test("inserts single paragraph", () => {
          const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
          const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
          const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
          const result = Actions.paste(state, index, 0, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("1");
          expect(text(select<LiteralValue>(result, { ...index, itemIndex: 1, itemContentIndex: 0 }))).toEqual(
            "Teksten min",
          );
          expect(result.focus).toEqual({ ...index, itemIndex: 1, itemContentIndex: 0, cursorPosition: 0 });
        });
        test("inserts multiple paragraphs", () => {
          const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
          const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
          const clipboard = new MockDataTransfer({ "text/html": "<p>1</p><p>2</p>" });
          const result = Actions.paste(state, index, 0, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("1");
          expect(text(select<LiteralValue>(result, { ...index, itemIndex: 1, itemContentIndex: 0 }))).toEqual("2");
          expect(text(select<LiteralValue>(result, { ...index, itemIndex: 2, itemContentIndex: 0 }))).toEqual(
            "Teksten min",
          );
          expect(result.focus).toEqual({ ...index, itemIndex: 2, itemContentIndex: 0, cursorPosition: 0 });
        });
        test("inserts ul list", () => {
          const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
          const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
          const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
          const result = Actions.paste(state, index, 0, clipboard);

          expect(text(select<LiteralValue>(result, { ...index }))).toEqual("1");
          expect(text(select<LiteralValue>(result, { ...index, itemContentIndex: 0, itemIndex: 1 }))).toEqual("2");
          expect(text(select<LiteralValue>(result, { ...index, itemContentIndex: 0, itemIndex: 2 }))).toEqual(
            "Teksten min",
          );
          expect(result.focus).toEqual({ ...index, itemIndex: 2, itemContentIndex: 0, cursorPosition: 0 });
        });
        test("paragraph + ul", () => {
          const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
          const clipboard = new MockDataTransfer({
            "text/html":
              "<div><p><span><span>Punktliste</span></span><span> </span></p></div><div><ul><li><p><span><span>Første punkt</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Andre punkt</span></span><span> </span></p></li></ul></div>",
          });
          const state = letter(paragraph(itemList({ items: [item(literal({ text: "Hei" }))] })));
          const result = Actions.paste(state, index, 0, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("Punktliste");
          expect(text(select<LiteralValue>(result, { ...index, itemIndex: 1 }))).toEqual("Første punkt");
          expect(text(select<LiteralValue>(result, { ...index, itemIndex: 2 }))).toEqual("Andre punkt");
          expect(text(select<LiteralValue>(result, { ...index, itemIndex: 3 }))).toEqual("Hei");
          expect(result.focus).toEqual({ ...index, itemIndex: 3, cursorPosition: 0 });
        });
      });
      describe("in the middle of a literal", () => {
        test("inserts a single word", () => {
          const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
          const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
          const clipboard = new MockDataTransfer({ "text/html": "<span>1</span>" });
          const result = Actions.paste(state, index, 7, clipboard);

          console.dir(result.redigertBrev.blocks, { depth: null });

          expect(text(select<LiteralValue>(result, index))).toEqual("Teksten 1 min");
          expect(result.focus).toEqual({ ...index, cursorPosition: 9 });
        });
        test("inserts multiple words", () => {
          const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
          const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
          const clipboard = new MockDataTransfer({ "text/html": "<span>1</span><span>2</span" });
          const result = Actions.paste(state, index, 7, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("Teksten1 2 min");
          expect(result.focus).toEqual({ ...index, cursorPosition: 10 });
        });
        test("inserts single paragraph", () => {
          const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
          const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
          const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
          const result = Actions.paste(state, index, 7, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("Teksten1");
          expect(text(select<LiteralValue>(result, { ...index, itemIndex: 1, itemContentIndex: 0 }))).toEqual(" min");
          expect(result.focus).toEqual({ ...index, itemIndex: 1, itemContentIndex: 0, cursorPosition: 0 });
        });
        test("inserts multiple paragraphs", () => {
          const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
          const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
          const clipboard = new MockDataTransfer({ "text/html": "<p>1</p><p>2</p>" });
          const result = Actions.paste(state, index, 7, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("Teksten1");
          expect(text(select<LiteralValue>(result, { ...index, itemIndex: 1, itemContentIndex: 0 }))).toEqual("2");
          expect(text(select<LiteralValue>(result, { ...index, itemIndex: 2, itemContentIndex: 0 }))).toEqual(" min");
          expect(result.focus).toEqual({ ...index, itemIndex: 2, itemContentIndex: 0, cursorPosition: 0 });
        });
        test("inserts ul list", () => {
          const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
          const state = letter(paragraph(itemList({ items: [item(literal({ text: "Her har vi noe" }))] })));
          const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
          const result = Actions.paste(state, index, 10, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("Her har vi1");
          expect(text(select<LiteralValue>(result, { ...index, itemIndex: 1 }))).toEqual("2");
          expect(text(select<LiteralValue>(result, { ...index, itemIndex: 2 }))).toEqual(" noe");
          expect(result.focus).toEqual({ ...index, itemIndex: 2, cursorPosition: 0 });
        });
        test("paragraph + ul", () => {
          const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
          const clipboard = new MockDataTransfer({
            "text/html":
              "<div><p><span><span>Punktliste</span></span><span> </span></p></div><div><ul><li><p><span><span>Første punkt</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Andre punkt</span></span><span> </span></p></li></ul></div>",
          });
          const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
          const result = Actions.paste(state, index, 7, clipboard);

          expect(
            text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 })),
          ).toEqual("TekstenPunktliste");
          expect(text(select<LiteralValue>(result, { ...index, itemIndex: 1 }))).toEqual("Første punkt");
          expect(text(select<LiteralValue>(result, { ...index, itemIndex: 2 }))).toEqual("Andre punkt");
          expect(text(select<LiteralValue>(result, { ...index, itemIndex: 3 }))).toEqual(" min");
          expect(result.focus).toEqual({ ...index, itemIndex: 3, cursorPosition: 0 });
        });
      });
      describe("at the end of a literal", () => {
        test("inserts a single word", () => {
          const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
          const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
          const clipboard = new MockDataTransfer({ "text/html": "<span>Single</span>" });
          const result = Actions.paste(state, index, 11, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("Teksten minSingle");
          expect(result.focus).toEqual({ ...index, cursorPosition: 17 });
        });
        test("inserts multiple words", () => {
          const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
          const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
          const clipboard = new MockDataTransfer({ "text/html": "<span>Single</span><span>Spanner</span>" });
          const result = Actions.paste(state, index, 11, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("Teksten minSingle Spanner");
          expect(result.focus).toEqual({ ...index, cursorPosition: 25 });
        });
        test("inserts single paragraph", () => {
          const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
          const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
          const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
          const result = Actions.paste(state, index, 11, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("Teksten min1");
          expect(result.focus).toEqual({ ...index, itemIndex: 0, itemContentIndex: 0, cursorPosition: 12 });
        });
        test("inserts multiple paragraphs", () => {
          const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
          const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
          const clipboard = new MockDataTransfer({ "text/html": "<p>1</p><p>2</p>" });
          const result = Actions.paste(state, index, 11, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("Teksten min1");
          expect(text(select<LiteralValue>(result, { ...index, itemIndex: 1, itemContentIndex: 0 }))).toEqual("2");
          expect(result.focus).toEqual({ ...index, itemIndex: 1, itemContentIndex: 0, cursorPosition: 1 });
        });
        test("inserts ul list", () => {
          const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
          const state = letter(paragraph(itemList({ items: [item(literal({ text: "Her har vi noe " }))] })));
          const clipboard = new MockDataTransfer({ "text/html": "<ul><li>annet</li><li>og enda mer</li></ul>" });
          const result = Actions.paste(state, index, 15, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("Her har vi noe annet");
          expect(text(select<LiteralValue>(result, { ...index, itemIndex: 1 }))).toEqual("og enda mer");
        });
        test("paragraph + ul", () => {
          const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
          const clipboard = new MockDataTransfer({
            "text/html":
              "<div><p><span><span>Punktliste</span></span><span> </span></p></div><div><ul><li><p><span><span>Første punkt</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Andre punkt</span></span><span> </span></p></li></ul></div>",
          });
          const state = letter(paragraph(itemList({ items: [item(literal({ text: "Hei" }))] })));
          const result = Actions.paste(state, index, 4, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("HeiPunktliste");
          expect(text(select<LiteralValue>(result, { ...index, itemIndex: 1 }))).toEqual("Første punkt");
          expect(text(select<LiteralValue>(result, { ...index, itemIndex: 2 }))).toEqual("Andre punkt");
          expect(result.focus).toEqual({ ...index, itemIndex: 2, cursorPosition: 11 });
        });
      });
    });
  });
});

class MockDataTransfer implements DataTransfer {
  private readonly data: { [format: string]: string } = {};

  get types() {
    return Object.keys(this.data);
  }
  getData(format: string): string {
    return this.data[format];
  }
  setData(format: string, data: string): void {
    this.data[format] = data;
  }

  constructor(data: { [format: string]: string }) {
    this.data = { ...data };
  }

  dropEffect: "none" | "copy" | "link" | "move" = "none";
  effectAllowed: "none" | "copy" | "link" | "move" | "all" | "copyLink" | "copyMove" | "linkMove" | "uninitialized" =
    "uninitialized";
  files = [] as unknown as FileList;
  items = [] as unknown as DataTransferItemList;

  clearData(): void {
    throw new Error("Method not implemented.");
  }
  setDragImage(): void {
    throw new Error("Method not implemented.");
  }
}
