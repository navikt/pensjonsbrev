import { expect } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { text } from "~/Brevredigering/LetterEditor/actions/common";
import { ElementTags, type ItemList, type LiteralValue } from "~/types/brevbakerTypes";

import { item, itemList, letter, literal, paragraph, select, variable } from "../utils";

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
        describe("inserts a single word", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Teksten min" })));
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span>" });
            const result = Actions.paste(state, index, 0, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("1Teksten min");
            expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 1 });
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Teksten min" })));
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span>" });
            const firstResult = Actions.paste(state, index, 0, clipboard);

            const secondPasteResult = Actions.paste(
              firstResult,
              firstResult.focus,
              firstResult.focus.cursorPosition!,
              clipboard,
            );

            expect(text(select<LiteralValue>(secondPasteResult, index))).toEqual("1 1Teksten min");
            expect(secondPasteResult.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 3 });
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(
              paragraph(
                literal({ text: "første avsnitt" }),
                variable("variabel"),
                literal({ text: "andre teksten min" }),
                literal({ text: "fritekst", tags: [ElementTags.FRITEKST] }),
              ),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span>" });
            const result = Actions.paste(state, idx, 0, clipboard);

            expect(text(select<LiteralValue>(result, idx))).toEqual("1første avsnitt");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 1 }))).toEqual("variabel");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 2 }))).toEqual("andre teksten min");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 3 }))).toEqual("fritekst");
            expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 1 });
          });
          test("complex letter - literals and itemlists", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(
              paragraph(
                literal({ text: "første avsnitt" }),
                itemList({ items: [item(literal({ text: "item 1" })), item(literal({ text: "item 2" }))] }),
                literal({ text: "andre teksten min" }),
              ),
              paragraph(literal({ text: "Bare en ny setning" })),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span>" });
            const result = Actions.paste(state, idx, 0, clipboard);

            expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("1første avsnitt");
            expect(
              text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 1, itemIndex: 0, itemContentIndex: 0 })),
            ).toEqual("item 1");
            expect(
              text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 1, itemIndex: 1, itemContentIndex: 0 })),
            ).toEqual("item 2");
            expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 2 }))).toEqual("andre teksten min");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual(
              "Bare en ny setning",
            );
            expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 1 });
          });
        });

        describe("inserts multiple words", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Teksten min" })));
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span><span> 2</span" });
            const result = Actions.paste(state, index, 0, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("1 2Teksten min");
            expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 3 });
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Teksten min" })));
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span><span> 2</span" });
            const firstResult = Actions.paste(state, index, 0, clipboard);

            const secondPasteResult = Actions.paste(
              firstResult,
              firstResult.focus,
              firstResult.focus.cursorPosition!,
              clipboard,
            );

            expect(text(select<LiteralValue>(secondPasteResult, index))).toEqual("1 21 2Teksten min");
            expect(secondPasteResult.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 6 });
          });
        });
        describe("inserts single paragraph", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Teksten min" })));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const result = Actions.paste(state, index, 0, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("1");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual("Teksten min");
            expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 0 });
          });

          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Teksten min" })));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const firstResult = Actions.paste(state, index, 0, clipboard);

            const secondPasteResult = Actions.paste(
              firstResult,
              firstResult.focus,
              firstResult.focus.cursorPosition!,
              clipboard,
            );

            expect(text(select<LiteralValue>(secondPasteResult, index))).toEqual("1");
            expect(text(select<LiteralValue>(secondPasteResult, { blockIndex: 1, contentIndex: 0 }))).toEqual("1");
            expect(text(select<LiteralValue>(secondPasteResult, { blockIndex: 2, contentIndex: 0 }))).toEqual(
              "Teksten min",
            );
            expect(secondPasteResult.focus).toEqual({ blockIndex: 2, contentIndex: 0, cursorPosition: 0 });
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(
              paragraph(
                literal({ text: "første avsnitt" }),
                variable("variabel"),
                literal({ text: "andre teksten min" }),
                literal({ text: "fritekst", tags: [ElementTags.FRITEKST] }),
              ),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const result = Actions.paste(state, idx, 0, clipboard);

            expect(text(select<LiteralValue>(result, idx))).toEqual("1");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual("første avsnitt");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 1 }))).toEqual("variabel");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 2 }))).toEqual("andre teksten min");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 3 }))).toEqual("fritekst");
            expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 0 });
          });
          test("complex letter - literals and itemlists", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(
              paragraph(
                literal({ text: "første avsnitt" }),
                itemList({ items: [item(literal({ text: "item 1" })), item(literal({ text: "item 2" }))] }),
                literal({ text: "andre teksten min" }),
              ),
              paragraph(literal({ text: "Bare en ny setning" })),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const result = Actions.paste(state, idx, 0, clipboard);

            expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("1");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual("første avsnitt");
            expect(
              text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 1, itemIndex: 0, itemContentIndex: 0 })),
            ).toEqual("item 1");
            expect(
              text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 1, itemIndex: 1, itemContentIndex: 0 })),
            ).toEqual("item 2");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 2 }))).toEqual("andre teksten min");
            expect(text(select<LiteralValue>(result, { blockIndex: 2, contentIndex: 0 }))).toEqual(
              "Bare en ny setning",
            );
            expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 0 });
          });
        });
        describe("inserts multiple paragraphs", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Teksten min" })));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p><p>2</p>" });
            const result = Actions.paste(state, index, 0, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("1");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual("2");
            expect(text(select<LiteralValue>(result, { blockIndex: 2, contentIndex: 0 }))).toEqual("Teksten min");
            expect(result.focus).toEqual({ blockIndex: 2, contentIndex: 0, cursorPosition: 0 });
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Teksten min" })));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p><p>2</p>" });
            const firstResult = Actions.paste(state, index, 0, clipboard);

            const secondPasteResult = Actions.paste(
              firstResult,
              firstResult.focus,
              firstResult.focus.cursorPosition!,
              clipboard,
            );

            expect(text(select<LiteralValue>(secondPasteResult, index))).toEqual("1");
            expect(text(select<LiteralValue>(secondPasteResult, { blockIndex: 1, contentIndex: 0 }))).toEqual("2");
            expect(text(select<LiteralValue>(secondPasteResult, { blockIndex: 2, contentIndex: 0 }))).toEqual("1");
            expect(text(select<LiteralValue>(secondPasteResult, { blockIndex: 3, contentIndex: 0 }))).toEqual("2");
            expect(text(select<LiteralValue>(secondPasteResult, { blockIndex: 4, contentIndex: 0 }))).toEqual(
              "Teksten min",
            );
            expect(secondPasteResult.focus).toEqual({ blockIndex: 4, contentIndex: 0, cursorPosition: 0 });
          });
        });
        describe("inserts ul list", () => {
          test("single paste", () => {
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
          test("multiple paste", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Teksten min" })));
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
            const firstResult = Actions.paste(state, idx, 0, clipboard);

            const secondResult = Actions.paste(
              firstResult,
              firstResult.focus,
              firstResult.focus.cursorPosition!,
              clipboard,
            );

            expect(
              text(select<LiteralValue>(secondResult, { ...idx, blockIndex: 0, itemContentIndex: 0, itemIndex: 0 })),
            ).toEqual("1");
            expect(
              text(select<LiteralValue>(secondResult, { ...idx, blockIndex: 0, itemContentIndex: 0, itemIndex: 1 })),
            ).toEqual("2");
            expect(
              text(select<LiteralValue>(secondResult, { ...idx, blockIndex: 1, itemContentIndex: 0, itemIndex: 0 })),
            ).toEqual("1");
            expect(
              text(select<LiteralValue>(secondResult, { ...idx, blockIndex: 1, itemContentIndex: 0, itemIndex: 1 })),
            ).toEqual("2");
            expect(text(select<LiteralValue>(secondResult, { blockIndex: 2, contentIndex: 0 }))).toEqual("Teksten min");
            expect(secondResult.focus).toEqual({ blockIndex: 2, contentIndex: 0, cursorPosition: 0 });
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(
              paragraph(
                literal({ text: "første avsnitt" }),
                variable("variabel"),
                literal({ text: "andre teksten min" }),
                literal({ text: "fritekst", tags: [ElementTags.FRITEKST] }),
              ),
              paragraph(literal({ text: "Bare en ny setning" })),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
            const result = Actions.paste(state, idx, 0, clipboard);

            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 0, itemContentIndex: 0 }))).toEqual("1");
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 1, itemContentIndex: 0 }))).toEqual("2");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual("første avsnitt");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 1 }))).toEqual("variabel");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 2 }))).toEqual("andre teksten min");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 3 }))).toEqual("fritekst");
            expect(text(select<LiteralValue>(result, { blockIndex: 2, contentIndex: 0 }))).toEqual(
              "Bare en ny setning",
            );
            expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 0 });
          });
          test("complex letter - literals and itemlists", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(
              paragraph(
                literal({ text: "første avsnitt" }),
                itemList({ items: [item(literal({ text: "item 1" })), item(literal({ text: "item 2" }))] }),
                literal({ text: "andre teksten min" }),
              ),
              paragraph(literal({ text: "Bare en ny setning" })),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
            const result = Actions.paste(state, idx, 0, clipboard);

            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 0, itemContentIndex: 0 }))).toEqual("1");
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 1, itemContentIndex: 0 }))).toEqual("2");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual("første avsnitt");
            expect(
              text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 1, itemIndex: 0, itemContentIndex: 0 })),
            ).toEqual("item 1");
            expect(
              text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 1, itemIndex: 1, itemContentIndex: 0 })),
            ).toEqual("item 2");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 2 }))).toEqual("andre teksten min");
            expect(text(select<LiteralValue>(result, { blockIndex: 2, contentIndex: 0 }))).toEqual(
              "Bare en ny setning",
            );
            expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 0 });
          });
        });

        describe("paragraph + ul", () => {
          test("single paste", () => {
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
          test("multiple paste", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const clipboard = new MockDataTransfer({
              "text/html":
                "<div><p><span><span>Punktliste</span></span><span> </span></p></div><div><ul><li><p><span><span>Første punkt</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Andre punkt</span></span><span> </span></p></li></ul></div>",
            });
            const state = letter(paragraph(literal({ text: "Hei" })));
            const first = Actions.paste(state, idx, 0, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, idx))).toEqual("Punktliste");
            expect(
              text(select<LiteralValue>(second, { ...idx, contentIndex: 1, itemContentIndex: 0, itemIndex: 0 })),
            ).toEqual("Første punkt");
            expect(
              text(select<LiteralValue>(second, { ...idx, contentIndex: 1, itemContentIndex: 0, itemIndex: 1 })),
            ).toEqual("Andre punkt");
            expect(text(select<LiteralValue>(second, { ...idx, blockIndex: 1 }))).toEqual("Punktliste");
            expect(
              text(select<LiteralValue>(second, { blockIndex: 1, contentIndex: 1, itemContentIndex: 0, itemIndex: 0 })),
            ).toEqual("Første punkt");
            expect(
              text(select<LiteralValue>(second, { blockIndex: 1, contentIndex: 1, itemContentIndex: 0, itemIndex: 1 })),
            ).toEqual("Andre punkt");
            expect(text(select<LiteralValue>(second, { ...idx, blockIndex: 2 }))).toEqual("Hei");
            expect(second.focus).toEqual({ blockIndex: 2, contentIndex: 0, cursorPosition: 0 });
          });
        });
      });

      describe("in the middle of a literal", () => {
        describe("inserts a single word", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Her har vi noe" })));
            const clipboard = new MockDataTransfer({ "text/html": "<span>ikke</span>" });
            const result = Actions.paste(state, index, 10, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Her har vi ikke noe");
            expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 15 });
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Her har vi noe" })));
            const clipboard = new MockDataTransfer({ "text/html": "<span>ikke</span>" });
            const first = Actions.paste(state, index, 10, clipboard);

            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Her har vi ikke ikke noe");
            expect(second.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 20 });
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 2 };
            const state = letter(
              paragraph(
                literal({ text: "første avsnitt" }),
                variable("variabel"),
                literal({ text: "andre teksten min" }),
                literal({ text: "fritekst", tags: [ElementTags.FRITEKST] }),
              ),
              paragraph(literal({ text: "Bare en ny setning" })),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span>" });
            const result = Actions.paste(state, idx, 5, clipboard);

            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 0 }))).toEqual("første avsnitt");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 1 }))).toEqual("variabel");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 2 }))).toEqual("andre 1 teksten min");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 3 }))).toEqual("fritekst");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual(
              "Bare en ny setning",
            );
            expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 7 });
          });
          test("complex letter - literals and itemlists", () => {
            const idx = { blockIndex: 0, contentIndex: 1 };
            const state = letter(
              paragraph(
                literal({ text: "første avsnitt" }),
                literal({ text: "andre teksten min" }),
                itemList({ items: [item(literal({ text: "item 1" })), item(literal({ text: "item 2" }))] }),
                literal({ text: "tredje teksten min" }),
              ),
              paragraph(literal({ text: "Bare en ny setning" })),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span>" });
            const result = Actions.paste(state, idx, 5, clipboard);

            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 0 }))).toEqual("første avsnitt");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 1 }))).toEqual("andre 1 teksten min");
            expect(
              text(select<LiteralValue>(result, { ...idx, contentIndex: 2, itemIndex: 0, itemContentIndex: 0 })),
            ).toEqual("item 1");
            expect(
              text(select<LiteralValue>(result, { ...idx, contentIndex: 2, itemIndex: 1, itemContentIndex: 0 })),
            ).toEqual("item 2");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 3 }))).toEqual("tredje teksten min");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual(
              "Bare en ny setning",
            );
            expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 7 });
          });
        });
        describe("inserts multiple words", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Her har vi noe" })));
            const clipboard = new MockDataTransfer({ "text/html": "<span> da</span><span> ikke</span>" });
            const result = Actions.paste(state, index, 10, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Her har vida ikke noe");
            expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 17 });
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Her har vi noe" })));
            const clipboard = new MockDataTransfer({ "text/html": "<span> da</span><span> ikke</span>" });
            const first = Actions.paste(state, index, 10, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Her har vida ikkeda ikke noe");
            expect(second.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 24 });
          });
        });
        describe("inserts single paragraph", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Her har vi noe" })));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const result = Actions.paste(state, index, 10, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Her har vi1");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual(" noe");
            expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 0 });
          });
          test("multiple paste", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Her har vi noe" })));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const first = Actions.paste(state, idx, 10, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, idx))).toEqual("Her har vi1");
            expect(text(select<LiteralValue>(second, { ...idx, blockIndex: 1 }))).toEqual("1");
            expect(text(select<LiteralValue>(second, { blockIndex: 2, contentIndex: 0 }))).toEqual(" noe");
            expect(second.focus).toEqual({ blockIndex: 2, contentIndex: 0, cursorPosition: 0 });
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 2 };
            const state = letter(
              paragraph(
                literal({ text: "første avsnitt" }),
                variable("variabel"),
                literal({ text: "andre teksten min" }),
                literal({ text: "fritekst", tags: [ElementTags.FRITEKST] }),
              ),
              paragraph(literal({ text: "Bare en ny setning" })),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const result = Actions.paste(state, idx, 5, clipboard);

            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 0 }))).toEqual("første avsnitt");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 1 }))).toEqual("variabel");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 2 }))).toEqual("andre1");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual(" teksten min");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 1 }))).toEqual("fritekst");
            expect(text(select<LiteralValue>(result, { blockIndex: 2, contentIndex: 0 }))).toEqual(
              "Bare en ny setning",
            );
            expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 0 });
          });
          test("complex letter - literals and itemlists", () => {
            const idx = { blockIndex: 0, contentIndex: 1 };
            const state = letter(
              paragraph(
                literal({ text: "første avsnitt" }),
                literal({ text: "andre teksten min" }),
                itemList({ items: [item(literal({ text: "item 1" })), item(literal({ text: "item 2" }))] }),
                literal({ text: "tredje teksten min" }),
              ),
              paragraph(literal({ text: "Bare en ny setning" })),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const result = Actions.paste(state, idx, 5, clipboard);

            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 0 }))).toEqual("første avsnitt");
            expect(text(select<LiteralValue>(result, { ...idx }))).toEqual("andre1");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual(" teksten min");
            expect(
              text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 1, itemIndex: 0, itemContentIndex: 0 })),
            ).toEqual("item 1");
            expect(
              text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 1, itemIndex: 1, itemContentIndex: 0 })),
            ).toEqual("item 2");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 2 }))).toEqual(
              "tredje teksten min",
            );
            expect(text(select<LiteralValue>(result, { blockIndex: 2, contentIndex: 0 }))).toEqual(
              "Bare en ny setning",
            );
            expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 0 });
          });
        });
        describe("inserts multiple paragraphs", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Her har vi noe" })));
            const clipboard = new MockDataTransfer({ "text/html": "<p> da</p><p> ikke</p>" });
            const result = Actions.paste(state, index, 10, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Her har vida");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual("ikke");
            expect(text(select<LiteralValue>(result, { blockIndex: 2, contentIndex: 0 }))).toEqual(" noe");
            expect(result.focus).toEqual({ blockIndex: 2, contentIndex: 0, cursorPosition: 0 });
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Her har vi noe" })));
            const clipboard = new MockDataTransfer({ "text/html": "<p> da</p><p> ikke</p>" });
            const first = Actions.paste(state, index, 10, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Her har vida");
            expect(text(select<LiteralValue>(second, { blockIndex: 1, contentIndex: 0 }))).toEqual("ikke");
            expect(text(select<LiteralValue>(second, { blockIndex: 2, contentIndex: 0 }))).toEqual("da");
            expect(text(select<LiteralValue>(second, { blockIndex: 3, contentIndex: 0 }))).toEqual("ikke");
            expect(text(select<LiteralValue>(second, { blockIndex: 4, contentIndex: 0 }))).toEqual(" noe");
            expect(second.focus).toEqual({ blockIndex: 4, contentIndex: 0, cursorPosition: 0 });
          });
        });
        describe("inserts ul list", () => {
          test("single paste", () => {
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
          test("multiple paste", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Teksten min" })));
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
            const first = Actions.paste(state, idx, 7, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(
              text(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 0, itemContentIndex: 0, itemIndex: 0 })),
            ).toEqual("Teksten1");
            expect(
              text(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 0, itemContentIndex: 0, itemIndex: 1 })),
            ).toEqual("2");
            expect(
              text(select<LiteralValue>(second, { blockIndex: 1, contentIndex: 0, itemContentIndex: 0, itemIndex: 0 })),
            ).toEqual("1");
            expect(
              text(select<LiteralValue>(second, { blockIndex: 1, contentIndex: 0, itemContentIndex: 0, itemIndex: 1 })),
            ).toEqual("2");
            expect(text(select<LiteralValue>(second, { blockIndex: 2, contentIndex: 0 }))).toEqual(" min");
            expect(second.focus).toEqual({ blockIndex: 2, contentIndex: 0, cursorPosition: 0 });
          });

          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 2 };
            const state = letter(
              paragraph(
                literal({ text: "første avsnitt" }),
                variable("variabel"),
                literal({ text: "andre teksten min" }),
                literal({ text: "fritekst", tags: [ElementTags.FRITEKST] }),
              ),
              paragraph(literal({ text: "Bare en ny setning" })),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
            const result = Actions.paste(state, idx, 5, clipboard);

            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 0 }))).toEqual("første avsnitt");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 1 }))).toEqual("variabel");
            expect(
              text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 2, itemIndex: 0, itemContentIndex: 0 })),
            ).toEqual("andre1");
            expect(
              text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 2, itemIndex: 1, itemContentIndex: 0 })),
            ).toEqual("2");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual(" teksten min");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 1 }))).toEqual("fritekst");
            expect(text(select<LiteralValue>(result, { blockIndex: 2, contentIndex: 0 }))).toEqual(
              "Bare en ny setning",
            );
            expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 0 });
          });
          test("complex letter - literals and itemlists", () => {
            const idx = { blockIndex: 0, contentIndex: 1 };
            const state = letter(
              paragraph(
                literal({ text: "første avsnitt" }),
                literal({ text: "andre teksten min" }),
                itemList({ items: [item(literal({ text: "item 1" })), item(literal({ text: "item 2" }))] }),
                literal({ text: "tredje teksten min" }),
              ),
              paragraph(literal({ text: "Bare en ny setning" })),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
            const result = Actions.paste(state, idx, 5, clipboard);

            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 0 }))).toEqual("første avsnitt");
            expect(
              text(select<LiteralValue>(result, { ...idx, contentIndex: 1, itemIndex: 0, itemContentIndex: 0 })),
            ).toEqual("andre1");
            expect(
              text(select<LiteralValue>(result, { ...idx, contentIndex: 1, itemIndex: 1, itemContentIndex: 0 })),
            ).toEqual("2");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual(" teksten min");
            expect(
              text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 1, itemIndex: 0, itemContentIndex: 0 })),
            ).toEqual("item 1");
            expect(
              text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 1, itemIndex: 1, itemContentIndex: 0 })),
            ).toEqual("item 2");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 2 }))).toEqual(
              "tredje teksten min",
            );
            expect(text(select<LiteralValue>(result, { blockIndex: 2, contentIndex: 0 }))).toEqual(
              "Bare en ny setning",
            );
            expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 0 });
          });
        });

        describe("paragraph + ul", () => {
          test("single paste", () => {
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
          test("multiple paste", () => {
            const clipboard = new MockDataTransfer({
              "text/html":
                "<div><p><span><span>Punktliste</span></span><span> </span></p></div><div><ul><li><p><span><span>Første punkt</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Andre punkt</span></span><span> </span></p></li></ul></div>",
            });
            const state = letter(paragraph(literal({ text: "Help" })));
            const first = Actions.paste(state, { blockIndex: 0, contentIndex: 0 }, 2, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 0 }))).toEqual("HePunktliste");
            expect(
              text(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 1, itemContentIndex: 0, itemIndex: 0 })),
            ).toEqual("Første punkt");
            expect(
              text(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 1, itemContentIndex: 0, itemIndex: 1 })),
            ).toEqual("Andre punkt");
            expect(text(select<LiteralValue>(second, { blockIndex: 1, contentIndex: 0 }))).toEqual("Punktliste");
            expect(
              text(select<LiteralValue>(second, { blockIndex: 1, contentIndex: 1, itemContentIndex: 0, itemIndex: 0 })),
            ).toEqual("Første punkt");
            expect(
              text(select<LiteralValue>(second, { blockIndex: 1, contentIndex: 1, itemContentIndex: 0, itemIndex: 1 })),
            ).toEqual("Andre punkt");
            expect(text(select<LiteralValue>(second, { blockIndex: 2, contentIndex: 0 }))).toEqual("lp");
            expect(second.focus).toEqual({ blockIndex: 2, contentIndex: 0, cursorPosition: 0 });
          });
        });
      });
      describe("at the end of a literal", () => {
        describe("inserts a single word", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Teksten min" })));
            const clipboard = new MockDataTransfer({ "text/html": "<span>Single</span>" });
            const result = Actions.paste(state, index, 11, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Teksten minSingle");
            expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 17 });
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Teksten min" })));
            const clipboard = new MockDataTransfer({ "text/html": "<span>Single</span>" });
            const first = Actions.paste(state, index, 11, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Teksten minSingleSingle");
            expect(second.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 23 });
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 3 };
            const state = letter(
              paragraph(
                literal({ text: "første avsnitt" }),
                variable("variabel"),
                literal({ text: "fritekst", tags: [ElementTags.FRITEKST] }),
                literal({ text: "andre teksten min" }),
              ),
              paragraph(literal({ text: "Bare en ny setning" })),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span>" });
            const result = Actions.paste(state, idx, 17, clipboard);

            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 0 }))).toEqual("første avsnitt");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 1 }))).toEqual("variabel");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 2 }))).toEqual("fritekst");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 3 }))).toEqual("andre teksten min1");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual(
              "Bare en ny setning",
            );
            expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 18 });
          });
          test("complex letter - literals and itemlists", () => {
            const idx = { blockIndex: 0, contentIndex: 3 };
            const state = letter(
              paragraph(
                literal({ text: "første avsnitt" }),
                literal({ text: "andre teksten min" }),
                itemList({ items: [item(literal({ text: "item 1" })), item(literal({ text: "item 2" }))] }),
                literal({ text: "tredje teksten min" }),
              ),
              paragraph(literal({ text: "Bare en ny setning" })),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span>" });
            const result = Actions.paste(state, idx, 18, clipboard);

            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 0 }))).toEqual("første avsnitt");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 1 }))).toEqual("andre teksten min");
            expect(
              text(select<LiteralValue>(result, { ...idx, contentIndex: 2, itemIndex: 0, itemContentIndex: 0 })),
            ).toEqual("item 1");
            expect(
              text(select<LiteralValue>(result, { ...idx, contentIndex: 2, itemIndex: 1, itemContentIndex: 0 })),
            ).toEqual("item 2");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 3 }))).toEqual("tredje teksten min1");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual(
              "Bare en ny setning",
            );
            expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 19 });
          });
        });
        describe("inserts multiple words", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Teksten min" })));
            const clipboard = new MockDataTransfer({ "text/html": "<span>Single</span><span>Spanner</span>" });
            const result = Actions.paste(state, index, 11, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Teksten minSingle Spanner");
            expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 25 });
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Teksten min" })));
            const clipboard = new MockDataTransfer({ "text/html": "<span>Single</span><span>Spanner</span>" });
            const first = Actions.paste(state, index, 11, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Teksten minSingle SpannerSingle Spanner");
            expect(second.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 39 });
          });
        });
        describe("inserts single paragraph", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Teksten min" })));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const result = Actions.paste(state, index, 11, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Teksten min1");
            expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 12 });
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Teksten min" })));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const first = Actions.paste(state, index, 11, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Teksten min11");
            expect(second.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 13 });
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 3 };
            const state = letter(
              paragraph(
                literal({ text: "første avsnitt" }),
                variable("variabel"),
                literal({ text: "fritekst", tags: [ElementTags.FRITEKST] }),
                literal({ text: "andre teksten min" }),
              ),
              paragraph(literal({ text: "Bare en ny setning" })),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const result = Actions.paste(state, idx, 17, clipboard);

            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 0 }))).toEqual("første avsnitt");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 1 }))).toEqual("variabel");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 2 }))).toEqual("fritekst");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 3 }))).toEqual("andre teksten min1");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual(
              "Bare en ny setning",
            );
            expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 3, cursorPosition: 18 });
          });
          test("complex letter - literals and itemlists", () => {
            const idx = { blockIndex: 0, contentIndex: 3 };
            const state = letter(
              paragraph(
                literal({ text: "første avsnitt" }),
                literal({ text: "andre teksten min" }),
                itemList({ items: [item(literal({ text: "item 1" })), item(literal({ text: "item 2" }))] }),
                literal({ text: "tredje teksten min" }),
              ),
              paragraph(literal({ text: "Bare en ny setning" })),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const result = Actions.paste(state, idx, 18, clipboard);

            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 0 }))).toEqual("første avsnitt");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 1 }))).toEqual("andre teksten min");
            expect(
              text(select<LiteralValue>(result, { ...idx, contentIndex: 2, itemIndex: 0, itemContentIndex: 0 })),
            ).toEqual("item 1");
            expect(
              text(select<LiteralValue>(result, { ...idx, contentIndex: 2, itemIndex: 1, itemContentIndex: 0 })),
            ).toEqual("item 2");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 3 }))).toEqual("tredje teksten min1");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual(
              "Bare en ny setning",
            );
            expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 3, cursorPosition: 19 });
          });
        });
        describe("inserts multiple paragraphs", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Teksten min" })));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p><p>2</p>" });
            const result = Actions.paste(state, index, 11, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Teksten min1");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual("2");
            expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 1 });
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Teksten min" })));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p><p>2</p>" });
            const first = Actions.paste(state, index, 11, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Teksten min1");
            expect(text(select<LiteralValue>(second, { blockIndex: 1, contentIndex: 0 }))).toEqual("21");
            expect(text(select<LiteralValue>(second, { blockIndex: 2, contentIndex: 0 }))).toEqual("2");
            expect(second.focus).toEqual({ blockIndex: 2, contentIndex: 0, cursorPosition: 1 });
          });
        });
        describe("inserts ul list", () => {
          test("single paste", () => {
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
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(paragraph(literal({ text: "Her har vi noe " })));
            const clipboard = new MockDataTransfer({
              "text/html": "<ul><li>annet</li><li>mer</li><li>enda mer</li></ul>",
            });
            const first = Actions.paste(state, index, 15, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(
              text(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 0, itemContentIndex: 0, itemIndex: 0 })),
            ).toEqual("Her har vi noe annet");
            expect(
              text(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 0, itemContentIndex: 0, itemIndex: 1 })),
            ).toEqual("mer");
            expect(
              text(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 0, itemContentIndex: 0, itemIndex: 2 })),
            ).toEqual("enda merannet");
            expect(
              text(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 0, itemContentIndex: 0, itemIndex: 3 })),
            ).toEqual("mer");
            expect(
              text(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 0, itemContentIndex: 0, itemIndex: 4 })),
            ).toEqual("enda mer");
            expect(second.focus).toEqual({
              blockIndex: 0,
              contentIndex: 0,
              itemContentIndex: 0,
              itemIndex: 4,
              cursorPosition: 8,
            });
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 3 };
            const state = letter(
              paragraph(
                literal({ text: "første avsnitt" }),
                variable("variabel"),
                literal({ text: "fritekst", tags: [ElementTags.FRITEKST] }),
                literal({ text: "andre teksten min" }),
              ),
              paragraph(literal({ text: "Bare en ny setning" })),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
            const result = Actions.paste(state, idx, 17, clipboard);

            expect(
              text(select<LiteralValue>(result, { ...idx, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 })),
            ).toEqual("første avsnitt");
            expect(
              text(select<LiteralValue>(result, { ...idx, contentIndex: 0, itemIndex: 0, itemContentIndex: 1 })),
            ).toEqual("variabel");
            expect(
              text(select<LiteralValue>(result, { ...idx, contentIndex: 0, itemIndex: 0, itemContentIndex: 2 })),
            ).toEqual("fritekst");
            expect(
              text(select<LiteralValue>(result, { ...idx, contentIndex: 0, itemIndex: 0, itemContentIndex: 3 })),
            ).toEqual("andre teksten min1");
            expect(
              text(select<LiteralValue>(result, { ...idx, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 })),
            ).toEqual("2");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual(
              "Bare en ny setning",
            );
            expect(result.focus).toEqual({
              blockIndex: 0,
              contentIndex: 0,
              itemIndex: 1,
              itemContentIndex: 0,
              cursorPosition: 1,
            });
          });
          test("complex letter - literals and itemlists", () => {
            const idx = { blockIndex: 0, contentIndex: 3 };
            const state = letter(
              paragraph(
                literal({ text: "første avsnitt" }),
                literal({ text: "andre teksten min" }),
                itemList({ items: [item(literal({ text: "item 1" })), item(literal({ text: "item 2" }))] }),
                literal({ text: "tredje teksten min" }),
              ),
              paragraph(literal({ text: "Bare en ny setning" })),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
            const result = Actions.paste(state, idx, 18, clipboard);

            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 0 }))).toEqual("første avsnitt");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 1 }))).toEqual("andre teksten min");
            expect(
              text(select<LiteralValue>(result, { ...idx, contentIndex: 2, itemIndex: 0, itemContentIndex: 0 })),
            ).toEqual("item 1");
            expect(
              text(select<LiteralValue>(result, { ...idx, contentIndex: 2, itemIndex: 1, itemContentIndex: 0 })),
            ).toEqual("item 2");
            expect(
              text(select<LiteralValue>(result, { ...idx, contentIndex: 2, itemIndex: 2, itemContentIndex: 0 })),
            ).toEqual("tredje teksten min1");
            expect(
              text(select<LiteralValue>(result, { ...idx, contentIndex: 2, itemIndex: 3, itemContentIndex: 0 })),
            ).toEqual("2");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual(
              "Bare en ny setning",
            );
            expect(result.focus).toEqual({
              blockIndex: 0,
              contentIndex: 2,
              itemIndex: 3,
              itemContentIndex: 0,
              cursorPosition: 1,
            });
          });
        });
        describe("paragraph + ul", () => {
          test("single paste", () => {
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
          test("multiple paste", () => {
            const clipboard = new MockDataTransfer({
              "text/html":
                "<div><p><span><span>Punktliste</span></span><span> </span></p></div><div><ul><li><p><span><span>Første punkt</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Andre punkt</span></span><span> </span></p></li></ul></div>",
            });
            const state = letter(paragraph(literal({ text: "Hei" })));
            const first = Actions.paste(state, { blockIndex: 0, contentIndex: 0 }, 4, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 0 }))).toEqual("HeiPunktliste");
            expect(
              text(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 1, itemContentIndex: 0, itemIndex: 0 })),
            ).toEqual("Første punkt");
            expect(
              text(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 1, itemContentIndex: 0, itemIndex: 1 })),
            ).toEqual("Andre punktPunktliste");
            expect(
              text(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 1, itemContentIndex: 0, itemIndex: 2 })),
            ).toEqual("Første punkt");
            expect(
              text(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 1, itemContentIndex: 0, itemIndex: 3 })),
            ).toEqual("Andre punkt");

            expect((second.redigertBrev.blocks[0].content[1] as ItemList).items.length).toEqual(4);

            expect(second.focus).toEqual({
              blockIndex: 0,
              contentIndex: 1,
              itemContentIndex: 0,
              itemIndex: 3,
              cursorPosition: 11,
            });
          });
        });
      });
    });

    describe("paste into an item", () => {
      describe("start of literal", () => {
        describe("inserts a single word", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span>" });
            const result = Actions.paste(state, index, 0, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("1Teksten min");
            expect(result.focus).toEqual({ ...index, cursorPosition: 1 });
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span>" });
            const first = Actions.paste(state, index, 0, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("1 1Teksten min");
            expect(second.focus).toEqual({ ...index, cursorPosition: 3 });
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 1, itemIndex: 1, itemContentIndex: 0 };
            const state = letter(
              paragraph(
                literal({ text: "punktliste" }),
                itemList({
                  items: [
                    item(literal({ text: "c1-i0-ic0" }), variable("c1-i0-ic1"), literal({ text: "c1-i0-ic2" })),
                    item(literal({ text: "c1-i1-ic0" }), variable("c1-i1-ic1"), literal({ text: "c1-i1-ic2" })),
                    item(literal({ text: "c1-i2-ic0" }), variable("c1-i2-ic1"), literal({ text: "c1-i2-ic2" })),
                  ],
                }),
                literal({ text: "Etter punktliste" }),
              ),
              paragraph(literal({ text: "Bare en ny setning" })),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<span>Pasta</span>" });
            const result = Actions.paste(state, idx, 0, clipboard);

            expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("punktliste");
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 0, itemContentIndex: 0 }))).toEqual(
              "c1-i0-ic0",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 0, itemContentIndex: 1 }))).toEqual(
              "c1-i0-ic1",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 0, itemContentIndex: 2 }))).toEqual(
              "c1-i0-ic2",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 1, itemContentIndex: 0 }))).toEqual(
              "Pastac1-i1-ic0",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 1, itemContentIndex: 1 }))).toEqual(
              "c1-i1-ic1",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 1, itemContentIndex: 2 }))).toEqual(
              "c1-i1-ic2",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 2, itemContentIndex: 0 }))).toEqual(
              "c1-i2-ic0",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 2, itemContentIndex: 1 }))).toEqual(
              "c1-i2-ic1",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 2, itemContentIndex: 2 }))).toEqual(
              "c1-i2-ic2",
            );
            expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 2 }))).toEqual("Etter punktliste");

            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual(
              "Bare en ny setning",
            );
            expect(result.focus).toEqual({ ...idx, cursorPosition: 5 });
          });
        });
        describe("inserts multiple words", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span><span> 2</span" });
            const result = Actions.paste(state, index, 0, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("1 2Teksten min");
            expect(result.focus).toEqual({ ...index, cursorPosition: 3 });
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span><span> 2</span" });
            const first = Actions.paste(state, index, 0, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("1 21 2Teksten min");
            expect(second.focus).toEqual({ ...index, cursorPosition: 6 });
          });
        });
        describe("inserts single paragraph", () => {
          test("single paste", () => {
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
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const first = Actions.paste(state, index, 0, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("1");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 1, itemContentIndex: 0 }))).toEqual("1");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 2, itemContentIndex: 0 }))).toEqual(
              "Teksten min",
            );
            expect(second.focus).toEqual({ ...index, itemIndex: 2, itemContentIndex: 0, cursorPosition: 0 });
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 1, itemIndex: 1, itemContentIndex: 0 };
            const state = letter(
              paragraph(
                literal({ text: "punktliste" }),
                itemList({
                  items: [
                    item(literal({ text: "c1-i0-ic0" }), variable("c1-i0-ic1"), literal({ text: "c1-i0-ic2" })),
                    item(literal({ text: "c1-i1-ic0" }), variable("c1-i1-ic1"), literal({ text: "c1-i1-ic2" })),
                    item(literal({ text: "c1-i2-ic0" }), variable("c1-i2-ic1"), literal({ text: "c1-i2-ic2" })),
                  ],
                }),
                literal({ text: "Etter punktliste" }),
              ),
              paragraph(literal({ text: "Bare en ny setning" })),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<p>Pasta</p>" });
            const result = Actions.paste(state, idx, 0, clipboard);

            expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("punktliste");
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 0, itemContentIndex: 0 }))).toEqual(
              "c1-i0-ic0",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 0, itemContentIndex: 1 }))).toEqual(
              "c1-i0-ic1",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 0, itemContentIndex: 2 }))).toEqual(
              "c1-i0-ic2",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 1, itemContentIndex: 0 }))).toEqual("Pasta");
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 2, itemContentIndex: 0 }))).toEqual(
              "c1-i1-ic0",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 2, itemContentIndex: 1 }))).toEqual(
              "c1-i1-ic1",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 2, itemContentIndex: 2 }))).toEqual(
              "c1-i1-ic2",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 3, itemContentIndex: 0 }))).toEqual(
              "c1-i2-ic0",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 3, itemContentIndex: 1 }))).toEqual(
              "c1-i2-ic1",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 3, itemContentIndex: 2 }))).toEqual(
              "c1-i2-ic2",
            );
            expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 2 }))).toEqual("Etter punktliste");

            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual(
              "Bare en ny setning",
            );
            expect(result.focus).toEqual({ ...idx, itemIndex: 2, itemContentIndex: 0, cursorPosition: 0 });
          });
        });
        describe("inserts multiple paragraphs", () => {
          test("single paste", () => {
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
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p><p>2</p>" });
            const first = Actions.paste(state, index, 0, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("1");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 1, itemContentIndex: 0 }))).toEqual("2");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 2, itemContentIndex: 0 }))).toEqual("1");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 3, itemContentIndex: 0 }))).toEqual("2");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 4, itemContentIndex: 0 }))).toEqual(
              "Teksten min",
            );
            expect(second.focus).toEqual({ ...index, itemIndex: 4, itemContentIndex: 0, cursorPosition: 0 });
          });
        });
        describe("inserts ul list", () => {
          test("single paste", () => {
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
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
            const first = Actions.paste(state, index, 0, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("1");
            expect(text(select<LiteralValue>(second, { ...index, itemContentIndex: 0, itemIndex: 1 }))).toEqual("2");
            expect(text(select<LiteralValue>(second, { ...index, itemContentIndex: 0, itemIndex: 2 }))).toEqual("1");
            expect(text(select<LiteralValue>(second, { ...index, itemContentIndex: 0, itemIndex: 3 }))).toEqual("2");
            expect(text(select<LiteralValue>(second, { ...index, itemContentIndex: 0, itemIndex: 4 }))).toEqual(
              "Teksten min",
            );
            expect(second.focus).toEqual({ ...index, itemIndex: 4, itemContentIndex: 0, cursorPosition: 0 });
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 1, itemIndex: 1, itemContentIndex: 0 };
            const state = letter(
              paragraph(
                literal({ text: "punktliste" }),
                itemList({
                  items: [
                    item(literal({ text: "c1-i0-ic0" }), variable("c1-i0-ic1"), literal({ text: "c1-i0-ic2" })),
                    item(literal({ text: "c1-i1-ic0" }), variable("c1-i1-ic1"), literal({ text: "c1-i1-ic2" })),
                    item(literal({ text: "c1-i2-ic0" }), variable("c1-i2-ic1"), literal({ text: "c1-i2-ic2" })),
                  ],
                }),
                literal({ text: "Etter punktliste" }),
              ),
              paragraph(literal({ text: "Bare en ny setning" })),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
            const result = Actions.paste(state, idx, 0, clipboard);

            expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("punktliste");
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 0, itemContentIndex: 0 }))).toEqual(
              "c1-i0-ic0",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 0, itemContentIndex: 1 }))).toEqual(
              "c1-i0-ic1",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 0, itemContentIndex: 2 }))).toEqual(
              "c1-i0-ic2",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 1, itemContentIndex: 0 }))).toEqual("1");
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 2, itemContentIndex: 0 }))).toEqual("2");
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 3, itemContentIndex: 0 }))).toEqual(
              "c1-i1-ic0",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 3, itemContentIndex: 1 }))).toEqual(
              "c1-i1-ic1",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 3, itemContentIndex: 2 }))).toEqual(
              "c1-i1-ic2",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 4, itemContentIndex: 0 }))).toEqual(
              "c1-i2-ic0",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 4, itemContentIndex: 1 }))).toEqual(
              "c1-i2-ic1",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 4, itemContentIndex: 2 }))).toEqual(
              "c1-i2-ic2",
            );
            expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 2 }))).toEqual("Etter punktliste");

            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual(
              "Bare en ny setning",
            );
            expect(result.focus).toEqual({ ...idx, itemIndex: 3, itemContentIndex: 0, cursorPosition: 0 });
          });
        });
        describe("paragraph + ul", () => {
          test("single paste", () => {
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
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const clipboard = new MockDataTransfer({
              "text/html":
                "<div><p><span><span>Punktliste</span></span><span> </span></p></div><div><ul><li><p><span><span>Første punkt</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Andre punkt</span></span><span> </span></p></li></ul></div>",
            });
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Hei" }))] })));
            const first = Actions.paste(state, index, 0, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Punktliste");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 1 }))).toEqual("Første punkt");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 2 }))).toEqual("Andre punkt");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 3 }))).toEqual("Punktliste");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 4 }))).toEqual("Første punkt");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 5 }))).toEqual("Andre punkt");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 6 }))).toEqual("Hei");
            expect(second.focus).toEqual({ ...index, itemIndex: 6, cursorPosition: 0 });
          });
        });
      });
      describe("in the middle of a literal", () => {
        describe("inserts a single word", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span>" });
            const result = Actions.paste(state, index, 7, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Teksten 1 min");
            expect(result.focus).toEqual({ ...index, cursorPosition: 9 });
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span>" });
            const first = Actions.paste(state, index, 7, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Teksten 1 1 min");
            expect(second.focus).toEqual({ ...index, cursorPosition: 11 });
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 1, itemIndex: 1, itemContentIndex: 1 };
            const state = letter(
              paragraph(
                literal({ text: "punktliste" }),
                itemList({
                  items: [
                    item(variable("c1-i0-ic0"), literal({ text: "c1-i0-ic1" }), variable("c1-i0-ic2")),
                    item(variable("c1-i1-ic0"), literal({ text: "c1-i1-ic1" }), variable("c1-i1-ic2")),
                    item(variable("c1-i2-ic0"), literal({ text: "c1-i2-ic1" }), variable("c1-i2-ic2")),
                  ],
                }),
                literal({ text: "Etter punktliste" }),
              ),
              paragraph(literal({ text: "Bare en ny setning" })),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<span>Pasta</span>" });
            const result = Actions.paste(state, idx, 4, clipboard);

            console.dir(result.redigertBrev.blocks, { depth: null });

            expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("punktliste");
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 0, itemContentIndex: 0 }))).toEqual(
              "c1-i0-ic0",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 0, itemContentIndex: 1 }))).toEqual(
              "c1-i0-ic1",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 0, itemContentIndex: 2 }))).toEqual(
              "c1-i0-ic2",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 1, itemContentIndex: 0 }))).toEqual(
              "c1-i1-ic0",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 1, itemContentIndex: 1 }))).toEqual(
              "c1-i Pasta1-ic1",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 1, itemContentIndex: 2 }))).toEqual(
              "c1-i1-ic2",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 2, itemContentIndex: 0 }))).toEqual(
              "c1-i2-ic0",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 2, itemContentIndex: 1 }))).toEqual(
              "c1-i2-ic1",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 2, itemContentIndex: 2 }))).toEqual(
              "c1-i2-ic2",
            );
            expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 2 }))).toEqual("Etter punktliste");

            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual(
              "Bare en ny setning",
            );
            expect(result.focus).toEqual({ ...idx, cursorPosition: 10 });
          });
        });
        describe("inserts multiple words", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span><span>2</span" });
            const result = Actions.paste(state, index, 7, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Teksten1 2 min");
            expect(result.focus).toEqual({ ...index, cursorPosition: 10 });
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span><span>2</span" });
            const first = Actions.paste(state, index, 7, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Teksten1 21 2 min");
            expect(second.focus).toEqual({ ...index, cursorPosition: 13 });
          });
        });
        describe("inserts single paragraph", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const result = Actions.paste(state, index, 7, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Teksten1");
            expect(text(select<LiteralValue>(result, { ...index, itemIndex: 1, itemContentIndex: 0 }))).toEqual(" min");
            expect(result.focus).toEqual({ ...index, itemIndex: 1, itemContentIndex: 0, cursorPosition: 0 });
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const first = Actions.paste(state, index, 7, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Teksten1");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 1 }))).toEqual("1");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 2 }))).toEqual(" min");
            expect(second.focus).toEqual({ ...index, itemIndex: 2, cursorPosition: 0 });
          });
        });
        describe("inserts multiple paragraphs", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p><p>2</p>" });
            const result = Actions.paste(state, index, 7, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Teksten1");
            expect(text(select<LiteralValue>(result, { ...index, itemIndex: 1, itemContentIndex: 0 }))).toEqual("2");
            expect(text(select<LiteralValue>(result, { ...index, itemIndex: 2, itemContentIndex: 0 }))).toEqual(" min");
            expect(result.focus).toEqual({ ...index, itemIndex: 2, itemContentIndex: 0, cursorPosition: 0 });
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p><p>2</p>" });
            const first = Actions.paste(state, index, 7, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Teksten1");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 1, itemContentIndex: 0 }))).toEqual("2");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 2, itemContentIndex: 0 }))).toEqual("1");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 3, itemContentIndex: 0 }))).toEqual("2");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 4, itemContentIndex: 0 }))).toEqual(" min");
            expect(second.focus).toEqual({ ...index, itemIndex: 4, itemContentIndex: 0, cursorPosition: 0 });
          });
        });
        describe("inserts ul list", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Her har vi noe" }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
            const result = Actions.paste(state, index, 10, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Her har vi1");
            expect(text(select<LiteralValue>(result, { ...index, itemIndex: 1 }))).toEqual("2");
            expect(text(select<LiteralValue>(result, { ...index, itemIndex: 2 }))).toEqual(" noe");
            expect(result.focus).toEqual({ ...index, itemIndex: 2, cursorPosition: 0 });
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Her har vi noe" }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
            const first = Actions.paste(state, index, 10, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Her har vi1");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 1 }))).toEqual("2");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 2 }))).toEqual("1");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 3 }))).toEqual("2");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 4 }))).toEqual(" noe");
            expect(second.focus).toEqual({ ...index, itemIndex: 4, cursorPosition: 0 });
          });
        });
        describe("paragraph + ul", () => {
          test("single paste", () => {
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
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const clipboard = new MockDataTransfer({
              "text/html":
                "<div><p><span><span>Punktliste</span></span><span> </span></p></div><div><ul><li><p><span><span>Første punkt</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Andre punkt</span></span><span> </span></p></li></ul></div>",
            });
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
            const first = Actions.paste(state, index, 7, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(
              text(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 })),
            ).toEqual("TekstenPunktliste");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 1 }))).toEqual("Første punkt");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 2 }))).toEqual("Andre punkt");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 3 }))).toEqual("Punktliste");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 4 }))).toEqual("Første punkt");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 5 }))).toEqual("Andre punkt");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 6 }))).toEqual(" min");
            expect(second.focus).toEqual({ ...index, itemIndex: 6, cursorPosition: 0 });
          });
        });
      });
      describe("at the end of a literal", () => {
        describe("inserts a single word", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<span>Single</span>" });
            const result = Actions.paste(state, index, 11, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Teksten minSingle");
            expect(result.focus).toEqual({ ...index, cursorPosition: 17 });
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<span>Single</span>" });
            const first = Actions.paste(state, index, 11, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Teksten minSingleSingle");
            expect(second.focus).toEqual({ ...index, cursorPosition: 23 });
          });
        });
        describe("inserts multiple words", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<span>Single</span><span>Spanner</span>" });
            const result = Actions.paste(state, index, 11, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Teksten minSingle Spanner");
            expect(result.focus).toEqual({ ...index, cursorPosition: 25 });
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<span>Single</span><span>Spanner</span>" });
            const first = Actions.paste(state, index, 11, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Teksten minSingle SpannerSingle Spanner");
            expect(second.focus).toEqual({ ...index, cursorPosition: 39 });
          });
        });
        describe("inserts single paragraph", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const result = Actions.paste(state, index, 11, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Teksten min1");
            expect(result.focus).toEqual({ ...index, itemIndex: 0, itemContentIndex: 0, cursorPosition: 12 });
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const first = Actions.paste(state, index, 11, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Teksten min11");
            expect(second.focus).toEqual({ ...index, itemIndex: 0, itemContentIndex: 0, cursorPosition: 13 });
          });
        });
        describe("inserts multiple paragraphs", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p><p>2</p>" });
            const result = Actions.paste(state, index, 11, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Teksten min1");
            expect(text(select<LiteralValue>(result, { ...index, itemIndex: 1, itemContentIndex: 0 }))).toEqual("2");
            expect(result.focus).toEqual({ ...index, itemIndex: 1, itemContentIndex: 0, cursorPosition: 1 });
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Teksten min" }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p><p>2</p>" });
            const first = Actions.paste(state, index, 11, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Teksten min1");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 1, itemContentIndex: 0 }))).toEqual("21");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 2, itemContentIndex: 0 }))).toEqual("2");
            expect(second.focus).toEqual({ ...index, itemIndex: 2, itemContentIndex: 0, cursorPosition: 1 });
          });
        });
        describe("inserts ul list", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Her har vi noe " }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>annet</li><li>og enda mer</li></ul>" });
            const result = Actions.paste(state, index, 15, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Her har vi noe annet");
            expect(text(select<LiteralValue>(result, { ...index, itemIndex: 1 }))).toEqual("og enda mer");
            expect(result.focus).toEqual({ ...index, itemIndex: 1, cursorPosition: 11 });
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Her har vi noe " }))] })));
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>annet</li><li>og enda mer</li></ul>" });
            const first = Actions.paste(state, index, 15, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Her har vi noe annet");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 1 }))).toEqual("og enda merannet");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 2 }))).toEqual("og enda mer");
            expect(second.focus).toEqual({ ...index, itemIndex: 2, cursorPosition: 11 });
          });
        });
        describe("paragraph + ul", () => {
          test("single paste", () => {
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
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const clipboard = new MockDataTransfer({
              "text/html":
                "<div><p><span><span>Punktliste</span></span><span> </span></p></div><div><ul><li><p><span><span>Første punkt</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Andre punkt</span></span><span> </span></p></li></ul></div>",
            });
            const state = letter(paragraph(itemList({ items: [item(literal({ text: "Hei" }))] })));
            const first = Actions.paste(state, index, 4, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("HeiPunktliste");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 1 }))).toEqual("Første punkt");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 2 }))).toEqual("Andre punktPunktliste");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 3 }))).toEqual("Første punkt");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 4 }))).toEqual("Andre punkt");
            expect(second.focus).toEqual({ ...index, itemIndex: 4, cursorPosition: 11 });
          });
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
