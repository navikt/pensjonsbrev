import { expect } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { newItem, newParagraph, newVariable, text } from "~/Brevredigering/LetterEditor/actions/common";
import type { Item, ParagraphBlock, VariableValue } from "~/types/brevbakerTypes";
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
            const state = letter(newParagraph({ id: 1, content: [literal({ id: 10, text: "Teksten min" })] }));
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span>" });
            const result = Actions.paste(state, index, 0, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("1Teksten min");
            expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 1 });

            expect(select<LiteralValue>(result, index).id).toEqual(10);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ text: "første avsnitt" }),
                  newVariable({ id: 101, text: "variabel" }),
                  literal({ text: "andre teksten min" }),
                  literal({ text: "fritekst", tags: [ElementTags.FRITEKST] }),
                ],
              }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span>" });
            const result = Actions.paste(state, idx, 0, clipboard);

            expect(text(select<LiteralValue>(result, idx))).toEqual("1første avsnitt");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 1 }))).toEqual("variabel");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 2 }))).toEqual("andre teksten min");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 3 }))).toEqual("fritekst");
            expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 1 });

            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("complex letter - literals and itemlists", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ text: "første avsnitt" }),
                  itemList({ items: [item(literal({ text: "item 1" })), item(literal({ text: "item 2" }))] }),
                  literal({ text: "andre teksten min" }),
                ],
              }),
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

            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
        });

        test("inserts multiple words", () => {
          const index = { blockIndex: 0, contentIndex: 0 };
          const state = letter(newParagraph({ id: 1, content: [literal({ text: "Teksten min" })] }));
          const clipboard = new MockDataTransfer({ "text/html": "<span>1</span><span> 2</span" });
          const result = Actions.paste(state, index, 0, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("1 2Teksten min");
          expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 3 });

          expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
          expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
          expect(result.redigertBrev.deletedBlocks).toEqual([]);
        });

        describe("inserts single paragraph", () => {
          test("single paste", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(newParagraph({ id: 1, content: [literal({ id: 10, text: "Teksten min" })] }));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const result = Actions.paste(state, idx, 0, clipboard);

            expect(text(select<LiteralValue>(result, idx))).toEqual("1");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual("Teksten min");
            expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 0 });

            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(null);
            expect(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(1);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });

          test("multiple paste", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(newParagraph({ id: 1, content: [literal({ id: 10, text: "Teksten min" })] }));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const first = Actions.paste(state, idx, 0, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, idx))).toEqual("1");
            expect(text(select<LiteralValue>(second, { blockIndex: 1, contentIndex: 0 }))).toEqual("1");
            expect(text(select<LiteralValue>(second, { blockIndex: 2, contentIndex: 0 }))).toEqual("Teksten min");
            expect(second.focus).toEqual({ blockIndex: 2, contentIndex: 0, cursorPosition: 0 });

            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(null);
            expect(select<ParagraphBlock>(second, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 1 }).id).toEqual(null);
            expect(select<LiteralValue>(second, { blockIndex: 2, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(second, { blockIndex: 2 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 2 }).id).toEqual(1);
            expect(second.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ text: "første avsnitt" }),
                  newVariable({ id: 101, text: "variabel" }),
                  literal({ text: "andre teksten min" }),
                  literal({ text: "fritekst", tags: [ElementTags.FRITEKST] }),
                ],
              }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const result = Actions.paste(state, idx, 0, clipboard);

            expect(text(select<LiteralValue>(result, idx))).toEqual("1");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual("første avsnitt");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 1 }))).toEqual("variabel");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 2 }))).toEqual("andre teksten min");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 3 }))).toEqual("fritekst");
            expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 0 });

            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(null);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(1);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("complex letter - literals and itemlists", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ text: "første avsnitt" }),
                  itemList({ items: [item(literal({ text: "item 1" })), item(literal({ text: "item 2" }))] }),
                  literal({ text: "andre teksten min" }),
                ],
              }),
              newParagraph({ id: 2, content: [literal({ text: "Bare en ny setning" })] }),
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

            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(null);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(1);
            expect(select<ParagraphBlock>(result, { blockIndex: 2 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 2 }).id).toEqual(2);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
        });
        describe("inserts multiple paragraphs", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(newParagraph({ id: 1, content: [literal({ text: "Teksten min" })] }));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p><p>2</p>" });
            const result = Actions.paste(state, index, 0, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("1");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual("2");
            expect(text(select<LiteralValue>(result, { blockIndex: 2, contentIndex: 0 }))).toEqual("Teksten min");
            expect(result.focus).toEqual({ blockIndex: 2, contentIndex: 0, cursorPosition: 0 });

            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(null);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(null);
            expect(select<ParagraphBlock>(result, { blockIndex: 2 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 2 }).id).toEqual(1);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(newParagraph({ id: 1, content: [literal({ text: "Teksten min" })] }));
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

            expect(select<ParagraphBlock>(secondPasteResult, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(secondPasteResult, { blockIndex: 0 }).id).toEqual(null);
            expect(select<ParagraphBlock>(secondPasteResult, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(secondPasteResult, { blockIndex: 1 }).id).toEqual(null);
            expect(select<ParagraphBlock>(secondPasteResult, { blockIndex: 2 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(secondPasteResult, { blockIndex: 2 }).id).toEqual(null);
            expect(select<ParagraphBlock>(secondPasteResult, { blockIndex: 3 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(secondPasteResult, { blockIndex: 3 }).id).toEqual(null);
            expect(select<ParagraphBlock>(secondPasteResult, { blockIndex: 4 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(secondPasteResult, { blockIndex: 4 }).id).toEqual(1);
            expect(secondPasteResult.redigertBrev.deletedBlocks).toEqual([]);
          });
        });
        describe("inserts ul list", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(newParagraph({ id: 1, content: [literal({ text: "Teksten min" })] }));
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

            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(null);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(1);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("multiple paste", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(newParagraph({ id: 1, content: [literal({ text: "Teksten min" })] }));
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

            expect(select<ParagraphBlock>(secondResult, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(secondResult, { blockIndex: 0 }).id).toEqual(null);
            expect(select<ParagraphBlock>(secondResult, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(secondResult, { blockIndex: 1 }).id).toEqual(null);
            expect(select<ParagraphBlock>(secondResult, { blockIndex: 2 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(secondResult, { blockIndex: 2 }).id).toEqual(1);
            expect(secondResult.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ text: "første avsnitt" }),
                  newVariable({ id: 101, text: "variabel" }),
                  literal({ text: "andre teksten min" }),
                  literal({ text: "fritekst", tags: [ElementTags.FRITEKST] }),
                ],
              }),
              newParagraph({ id: 2, content: [literal({ text: "Bare en ny setning" })] }),
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

            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(null);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(1);
            expect(select<ParagraphBlock>(result, { blockIndex: 2 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 2 }).id).toEqual(2);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("complex letter - literals and itemlists", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ text: "første avsnitt" }),
                  itemList({ items: [item(literal({ text: "item 1" })), item(literal({ text: "item 2" }))] }),
                  literal({ text: "andre teksten min" }),
                ],
              }),
              newParagraph({ id: 2, content: [literal({ text: "Bare en ny setning" })] }),
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

            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(null);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(1);
            expect(select<ParagraphBlock>(result, { blockIndex: 2 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 2 }).id).toEqual(2);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
        });

        describe("paragraph + ul", () => {
          test("single paste", () => {
            const clipboard = new MockDataTransfer({
              "text/html":
                "<div><p><span><span>Punktliste</span></span><span> </span></p></div><div><ul><li><p><span><span>Første punkt</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Andre punkt</span></span><span> </span></p></li></ul></div>",
            });
            const state = letter(newParagraph({ id: 1, content: [literal({ text: "Hei" })] }));
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

            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(null);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(1);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("multiple paste", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const clipboard = new MockDataTransfer({
              "text/html":
                "<div><p><span><span>Punktliste</span></span><span> </span></p></div><div><ul><li><p><span><span>Første punkt</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Andre punkt</span></span><span> </span></p></li></ul></div>",
            });
            const state = letter(newParagraph({ id: 1, content: [literal({ text: "Hei" })] }));
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

            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(null);
            expect(select<ParagraphBlock>(second, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 1 }).id).toEqual(null);
            expect(select<ParagraphBlock>(second, { blockIndex: 2 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 2 }).id).toEqual(1);
            expect(second.redigertBrev.deletedBlocks).toEqual([]);
          });
        });
      });

      describe("in the middle of a literal", () => {
        describe("inserts a single word", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(newParagraph({ id: 1, content: [literal({ id: 10, text: "Teksten min" })] }));
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span>" });
            const result = Actions.paste(state, index, 7, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Teksten1 min");
            expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 8 });

            expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(newParagraph({ id: 1, content: [literal({ id: 10, text: "Teksten min" })] }));
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span>" });
            const first = Actions.paste(state, index, 7, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Teksten11 min");
            expect(second.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 9 });

            expect(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
            expect(second.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 2 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ text: "første avsnitt" }),
                  newVariable({ text: "variabel" }),
                  literal({ id: 12, text: "andre teksten min" }),
                  literal({ text: "fritekst", tags: [ElementTags.FRITEKST] }),
                ],
              }),
              newParagraph({ id: 2, content: [literal({ text: "Bare en ny setning" })] }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span>" });
            const result = Actions.paste(state, idx, 5, clipboard);

            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 0 }))).toEqual("første avsnitt");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 1 }))).toEqual("variabel");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 2 }))).toEqual("andre1 teksten min");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 3 }))).toEqual("fritekst");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual(
              "Bare en ny setning",
            );
            expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 2, cursorPosition: 6 });

            expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 2 }).id).toEqual(12);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(2);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("complex letter - literals and itemlists", () => {
            const idx = { blockIndex: 0, contentIndex: 1 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ text: "første avsnitt" }),
                  literal({ id: 11, text: "andre teksten min" }),
                  itemList({ items: [item(literal({ text: "item 1" })), item(literal({ text: "item 2" }))] }),
                  literal({ text: "tredje teksten min" }),
                ],
              }),
              newParagraph({ id: 2, content: [literal({ text: "Bare en ny setning" })] }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span>" });
            const result = Actions.paste(state, idx, 5, clipboard);

            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 0 }))).toEqual("første avsnitt");
            expect(text(select<LiteralValue>(result, { ...idx, contentIndex: 1 }))).toEqual("andre1 teksten min");
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
            expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 1, cursorPosition: 6 });

            expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 1 }).id).toEqual(11);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(2);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
        });
        describe("inserts multiple words", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(newParagraph({ id: 1, content: [literal({ id: 11, text: "Teksten min" })] }));
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span><span>2</span>" });
            const result = Actions.paste(state, index, 7, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Teksten1 2 min");
            expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 10 });

            expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }).id).toEqual(11);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("multiple paste", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(newParagraph({ id: 1, content: [literal({ id: 11, text: "Teksten min" })] }));
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span><span>2</span>" });
            const first = Actions.paste(state, idx, 7, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, idx))).toEqual("Teksten1 21 2 min");
            expect(second.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 13 });

            expect(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 0 }).id).toEqual(11);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
            expect(second.redigertBrev.deletedBlocks).toEqual([]);
          });
        });
        describe("inserts single paragraph", () => {
          test("single paste", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(newParagraph({ id: 1, content: [literal({ id: 10, text: "Teksten min" })] }));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const result = Actions.paste(state, idx, 7, clipboard);

            expect(text(select<LiteralValue>(result, idx))).toEqual("Teksten1");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual(" min");
            expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 0 });

            expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }).id).toEqual(null);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([10]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }).id).toEqual(null);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(null);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("multiple paste", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(newParagraph({ id: 1, content: [literal({ id: 101, text: "Teksten min" })] }));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const first = Actions.paste(state, idx, 7, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, idx))).toEqual("Teksten1");
            expect(text(select<LiteralValue>(second, { ...idx, blockIndex: 1 }))).toEqual("1");
            expect(text(select<LiteralValue>(second, { blockIndex: 2, contentIndex: 0 }))).toEqual(" min");
            expect(second.focus).toEqual({ blockIndex: 2, contentIndex: 0, cursorPosition: 0 });

            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([101]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(second, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 1 }).id).toEqual(null);
            expect(select<ParagraphBlock>(second, { blockIndex: 2 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 2 }).id).toEqual(null);
            expect(second.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 2 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ id: 100, text: "første avsnitt" }),
                  newVariable({ id: 101, text: "variabel" }),
                  literal({ id: 102, text: "andre teksten min" }),
                  literal({ id: 103, text: "fritekst", tags: [ElementTags.FRITEKST] }),
                ],
              }),
              newParagraph({ id: 2, content: [literal({ text: "Bare en ny setning" })] }),
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

            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([102, 103]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(null);
            expect(select<ParagraphBlock>(result, { blockIndex: 2 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 2 }).id).toEqual(2);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("complex letter - literals and itemlists", () => {
            const idx = { blockIndex: 0, contentIndex: 1 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ id: 100, text: "første avsnitt" }),
                  literal({ id: 101, text: "andre teksten min" }),
                  itemList({ id: 102, items: [item(literal({ text: "item 1" })), item(literal({ text: "item 2" }))] }),
                  literal({ id: 103, text: "tredje teksten min" }),
                ],
              }),
              newParagraph({ id: 2, content: [literal({ text: "Bare en ny setning" })] }),
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

            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([101, 102, 103]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(null);
            expect(select<ParagraphBlock>(result, { blockIndex: 2 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 2 }).id).toEqual(2);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
        });
        describe("inserts multiple paragraphs", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(newParagraph({ id: 1, content: [literal({ id: 101, text: "Teksten min" })] }));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p><p>2</p>" });
            const result = Actions.paste(state, index, 7, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Teksten1");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual("2");
            expect(text(select<LiteralValue>(result, { blockIndex: 2, contentIndex: 0 }))).toEqual(" min");
            expect(result.focus).toEqual({ blockIndex: 2, contentIndex: 0, cursorPosition: 0 });

            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([101]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(null);
            expect(select<ParagraphBlock>(result, { blockIndex: 2 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 2 }).id).toEqual(null);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(newParagraph({ id: 1, content: [literal({ id: 101, text: "Teksten min" })] }));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p><p>2</p>" });
            const first = Actions.paste(state, index, 7, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Teksten1");
            expect(text(select<LiteralValue>(second, { blockIndex: 1, contentIndex: 0 }))).toEqual("2");
            expect(text(select<LiteralValue>(second, { blockIndex: 2, contentIndex: 0 }))).toEqual("1");
            expect(text(select<LiteralValue>(second, { blockIndex: 3, contentIndex: 0 }))).toEqual("2");
            expect(text(select<LiteralValue>(second, { blockIndex: 4, contentIndex: 0 }))).toEqual(" min");
            expect(second.focus).toEqual({ blockIndex: 4, contentIndex: 0, cursorPosition: 0 });

            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([101]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(second, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 1 }).id).toEqual(null);
            expect(select<ParagraphBlock>(second, { blockIndex: 2 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 2 }).id).toEqual(null);
            expect(select<ParagraphBlock>(second, { blockIndex: 3 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 3 }).id).toEqual(null);
            expect(select<ParagraphBlock>(second, { blockIndex: 4 }).deletedContent).toEqual([]);
            expect(second.redigertBrev.deletedBlocks).toEqual([]);
          });
        });
        describe("inserts ul list", () => {
          test("single paste", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(newParagraph({ id: 1, content: [literal({ id: 101, text: "Teksten min" })] }));
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
            const result = Actions.paste(state, idx, 7, clipboard);

            expect(
              text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 })),
            ).toEqual("Teksten1");
            expect(
              text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 })),
            ).toEqual("2");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual(" min");
            expect(result.focus).toEqual({
              blockIndex: 0,
              contentIndex: 0,
              itemContentIndex: 0,
              itemIndex: 1,
              cursorPosition: 1,
            });

            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([101]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(null);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("multiple paste", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(newParagraph({ id: 1, content: [literal({ id: 101, text: "Teksten min" })] }));
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
            const first = Actions.paste(state, idx, 7, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(
              text(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 })),
            ).toEqual("Teksten1");
            expect(
              text(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 })),
            ).toEqual("21");
            expect(
              text(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 2, itemContentIndex: 0 })),
            ).toEqual("2");
            expect(text(select<LiteralValue>(second, { blockIndex: 1, contentIndex: 0 }))).toEqual(" min");
            expect(second.focus).toEqual({
              blockIndex: 0,
              contentIndex: 0,
              itemIndex: 2,
              itemContentIndex: 0,
              cursorPosition: 1,
            });

            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([101]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(second, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 1 }).id).toEqual(null);
            expect(second.redigertBrev.deletedBlocks).toEqual([]);
          });

          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 2 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ id: 100, text: "første avsnitt" }),
                  newVariable({ id: 101, text: "variabel" }),
                  literal({ id: 102, text: "andre teksten min" }),
                  literal({ id: 103, text: "fritekst", tags: [ElementTags.FRITEKST] }),
                ],
              }),
              newParagraph({ id: 2, content: [literal({ text: "Bare en ny setning" })] }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
            const result = Actions.paste(state, idx, 5, clipboard);

            expect(
              text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 })),
            ).toEqual("første avsnitt");
            expect(
              text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 1 })),
            ).toEqual("variabel");
            expect(
              text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 2 })),
            ).toEqual("andre1");
            expect(
              text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 })),
            ).toEqual("2");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual(" teksten min");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 1 }))).toEqual("fritekst");
            expect(text(select<LiteralValue>(result, { blockIndex: 2, contentIndex: 0 }))).toEqual(
              "Bare en ny setning",
            );
            expect(result.focus).toEqual({
              blockIndex: 0,
              contentIndex: 0,
              itemIndex: 1,
              itemContentIndex: 0,
              cursorPosition: 1,
            });

            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([100, 101, 102, 103]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(null);
            expect(select<ParagraphBlock>(result, { blockIndex: 2 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 2 }).id).toEqual(2);

            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("complex letter - literals and itemlists", () => {
            const idx = { blockIndex: 0, contentIndex: 1 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ id: 100, text: "første avsnitt" }),
                  literal({ id: 101, text: "andre teksten min" }),
                  itemList({ id: 102, items: [item(literal({ text: "item 1" })), item(literal({ text: "item 2" }))] }),
                  literal({ id: 103, text: "tredje teksten min" }),
                ],
              }),
              newParagraph({ id: 2, content: [literal({ text: "Bare en ny setning" })] }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
            const res = Actions.paste(state, idx, 5, clipboard);

            expect(
              text(select<LiteralValue>(res, { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 })),
            ).toEqual("første avsnitt");
            expect(
              text(select<LiteralValue>(res, { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 1 })),
            ).toEqual("andre1");
            expect(
              text(select<LiteralValue>(res, { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 })),
            ).toEqual("2");
            expect(text(select<LiteralValue>(res, { blockIndex: 1, contentIndex: 0 }))).toEqual(" teksten min");
            expect(
              text(select<LiteralValue>(res, { blockIndex: 1, contentIndex: 1, itemIndex: 0, itemContentIndex: 0 })),
            ).toEqual("item 1");
            expect(
              text(select<LiteralValue>(res, { blockIndex: 1, contentIndex: 1, itemIndex: 1, itemContentIndex: 0 })),
            ).toEqual("item 2");
            expect(text(select<LiteralValue>(res, { blockIndex: 1, contentIndex: 2 }))).toEqual("tredje teksten min");
            expect(text(select<LiteralValue>(res, { blockIndex: 2, contentIndex: 0 }))).toEqual("Bare en ny setning");
            expect(res.focus).toEqual({
              blockIndex: 0,
              contentIndex: 0,
              itemIndex: 1,
              itemContentIndex: 0,
              cursorPosition: 1,
            });

            expect(select<ParagraphBlock>(res, { blockIndex: 0 }).deletedContent).toEqual([100, 101, 102, 103]);
            expect(select<ParagraphBlock>(res, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(res, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(res, { blockIndex: 1 }).id).toEqual(null);
            expect(select<ParagraphBlock>(res, { blockIndex: 2 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(res, { blockIndex: 2 }).id).toEqual(2);
            expect(res.redigertBrev.deletedBlocks).toEqual([]);
          });
        });

        describe("paragraph + ul", () => {
          test("single paste", () => {
            const clipboard = new MockDataTransfer({
              "text/html":
                "<div><p><span><span>Punktliste</span></span><span> </span></p></div><div><ul><li><p><span><span>Første punkt</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Andre punkt</span></span><span> </span></p></li></ul></div>",
            });
            const state = letter(newParagraph({ id: 1, content: [literal({ id: 101, text: "Help" })] }));
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

            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([101]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(null);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("multiple paste", () => {
            const clipboard = new MockDataTransfer({
              "text/html":
                "<div><p><span><span>Punktliste</span></span><span> </span></p></div><div><ul><li><p><span><span>Første punkt</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Andre punkt</span></span><span> </span></p></li></ul></div>",
            });
            const state = letter(newParagraph({ id: 1, content: [literal({ id: 101, text: "Help" })] }));
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

            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([101]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(second, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 1 }).id).toEqual(null);
            expect(second.redigertBrev.deletedBlocks).toEqual([]);
          });
        });
      });
      describe("at the end of a literal", () => {
        describe("inserts a single word", () => {
          test("single paste", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(newParagraph({ id: 1, content: [literal({ id: 10, text: "Teksten min" })] }));
            const clipboard = new MockDataTransfer({ "text/html": "<span>Single</span>" });
            const result = Actions.paste(state, idx, 11, clipboard);

            expect(text(select<LiteralValue>(result, idx))).toEqual("Teksten minSingle");
            expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 17 });

            expect(select<LiteralValue>(result, idx).id).toEqual(10);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("multiple paste", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(newParagraph({ id: 1, content: [literal({ id: 10, text: "Teksten min" })] }));
            const clipboard = new MockDataTransfer({ "text/html": "<span>Single</span>" });
            const first = Actions.paste(state, idx, 11, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, idx))).toEqual("Teksten minSingleSingle");
            expect(second.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 23 });

            expect(select<LiteralValue>(second, idx).id).toEqual(10);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
            expect(second.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 3 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ id: 100, text: "første avsnitt" }),
                  newVariable({ id: 101, text: "variabel" }),
                  literal({ id: 102, text: "fritekst", tags: [ElementTags.FRITEKST] }),
                  literal({ id: 103, text: "andre teksten min" }),
                ],
              }),
              newParagraph({ id: 2, content: [literal({ text: "Bare en ny setning" })] }),
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
            expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 3, cursorPosition: 18 });

            expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 3 }).id).toEqual(103);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(2);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("complex letter - literals and itemlists", () => {
            const idx = { blockIndex: 0, contentIndex: 3 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ id: 100, text: "første avsnitt" }),
                  literal({ id: 101, text: "andre teksten min" }),
                  itemList({ id: 102, items: [item(literal({ text: "item 1" })), item(literal({ text: "item 2" }))] }),
                  literal({ id: 103, text: "tredje teksten min" }),
                ],
              }),
              newParagraph({ id: 2, content: [literal({ text: "Bare en ny setning" })] }),
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
            expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 3, cursorPosition: 19 });

            expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 3 }).id).toEqual(103);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(2);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
        });
        describe("inserts multiple words", () => {
          test("single paste", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(newParagraph({ id: 1, content: [literal({ id: 10, text: "Teksten min" })] }));
            const clipboard = new MockDataTransfer({ "text/html": "<span>Single</span><span>Spanner</span>" });
            const result = Actions.paste(state, idx, 11, clipboard);

            expect(text(select<LiteralValue>(result, idx))).toEqual("Teksten minSingle Spanner");
            expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 25 });

            expect(select<LiteralValue>(result, idx).id).toEqual(10);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("multiple paste", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(newParagraph({ id: 1, content: [literal({ id: 10, text: "Teksten min" })] }));
            const clipboard = new MockDataTransfer({ "text/html": "<span>Single</span><span>Spanner</span>" });
            const first = Actions.paste(state, idx, 11, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, idx))).toEqual("Teksten minSingle SpannerSingle Spanner");
            expect(second.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 39 });

            expect(select<LiteralValue>(second, idx).id).toEqual(10);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
            expect(second.redigertBrev.deletedBlocks).toEqual([]);
          });
        });
        describe("inserts single paragraph", () => {
          test("single paste", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(newParagraph({ id: 1, content: [literal({ id: 10, text: "Teksten min" })] }));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const result = Actions.paste(state, idx, 11, clipboard);

            expect(text(select<LiteralValue>(result, idx))).toEqual("Teksten min1");
            expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 12 });

            expect(select<LiteralValue>(result, idx).id).toEqual(10);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0 };
            const state = letter(newParagraph({ id: 1, content: [literal({ text: "Teksten min" })] }));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const first = Actions.paste(state, index, 11, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Teksten min11");
            expect(second.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 13 });

            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
            expect(second.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 3 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ id: 100, text: "første avsnitt" }),
                  newVariable({ id: 101, text: "variabel" }),
                  literal({ id: 102, text: "fritekst", tags: [ElementTags.FRITEKST] }),
                  literal({ id: 103, text: "andre teksten min" }),
                ],
              }),
              newParagraph({ id: 2, content: [literal({ text: "Bare en ny setning" })] }),
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

            expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }).id).toEqual(100);
            expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 1 }).id).toEqual(101);
            expect(select<VariableValue>(result, { blockIndex: 0, contentIndex: 2 }).id).toEqual(102);
            expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 3 }).id).toEqual(103);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(2);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });

          test("complex letter - literals and itemlists", () => {
            const idx = { blockIndex: 0, contentIndex: 3 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ id: 100, text: "første avsnitt" }),
                  literal({ id: 101, text: "andre teksten min" }),
                  itemList({ id: 102, items: [item(literal({ text: "item 1" })), item(literal({ text: "item 2" }))] }),
                  literal({ id: 103, text: "tredje teksten min" }),
                ],
              }),
              newParagraph({ id: 2, content: [literal({ text: "Bare en ny setning" })] }),
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

            expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }).id).toEqual(100);
            expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 1 }).id).toEqual(101);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 2 }).id).toEqual(102);
            expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 3 }).id).toEqual(103);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(2);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
        });
        describe("inserts multiple paragraphs", () => {
          test("single paste", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(newParagraph({ id: 1, content: [literal({ id: 10, text: "Teksten min" })] }));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p><p>2</p>" });
            const result = Actions.paste(state, idx, 11, clipboard);

            expect(text(select<LiteralValue>(result, idx))).toEqual("Teksten min1");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual("2");
            expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 1 });

            expect(select<LiteralValue>(result, idx).id).toEqual(10);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("multiple paste", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(newParagraph({ id: 1, content: [literal({ id: 10, text: "Teksten min" })] }));
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p><p>2</p>" });
            const first = Actions.paste(state, idx, 11, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, idx))).toEqual("Teksten min1");
            expect(text(select<LiteralValue>(second, { blockIndex: 1, contentIndex: 0 }))).toEqual("21");
            expect(text(select<LiteralValue>(second, { blockIndex: 2, contentIndex: 0 }))).toEqual("2");
            expect(second.focus).toEqual({ blockIndex: 2, contentIndex: 0, cursorPosition: 1 });

            expect(select<LiteralValue>(second, idx).id).toEqual(10);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(second, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 1 }).id).toEqual(null);
            expect(select<ParagraphBlock>(second, { blockIndex: 2 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 2 }).id).toEqual(null);
            expect(second.redigertBrev.deletedBlocks).toEqual([]);
          });
        });
        describe("inserts ul list", () => {
          test("single paste", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(newParagraph({ id: 1, content: [literal({ id: 101, text: "Teksten min" })] }));
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
            const result = Actions.paste(state, idx, 11, clipboard);

            expect(
              text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 })),
            ).toEqual("Teksten min1");
            expect(
              text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 })),
            ).toEqual("2");
            expect(result.focus).toEqual({
              blockIndex: 0,
              contentIndex: 0,
              itemContentIndex: 0,
              itemIndex: 1,
              cursorPosition: 1,
            });

            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([101]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("multiple paste", () => {
            const idx = { blockIndex: 0, contentIndex: 0 };
            const state = letter(newParagraph({ id: 1, content: [literal({ id: 101, text: "Teksten min" })] }));
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
            const first = Actions.paste(state, idx, 15, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(
              text(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 0, itemContentIndex: 0, itemIndex: 0 })),
            ).toEqual("Teksten min1");
            expect(
              text(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 0, itemContentIndex: 0, itemIndex: 1 })),
            ).toEqual("21");
            expect(
              text(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 0, itemContentIndex: 0, itemIndex: 2 })),
            ).toEqual("2");
            expect(second.focus).toEqual({ ...idx, itemContentIndex: 0, itemIndex: 2, cursorPosition: 1 });

            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([101]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
            expect(second.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 3 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ id: 100, text: "første avsnitt" }),
                  newVariable({ id: 101, text: "variabel" }),
                  literal({ id: 102, text: "fritekst", tags: [ElementTags.FRITEKST] }),
                  literal({ id: 103, text: "andre teksten min" }),
                ],
              }),
              newParagraph({ id: 2, content: [literal({ text: "Bare en ny setning" })] }),
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

            //alle elementene blir konvertert til et item i en item list.
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([100, 101, 102, 103]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(2);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("complex letter - literals and itemlists", () => {
            const idx = { blockIndex: 0, contentIndex: 3 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ id: 100, text: "første avsnitt" }),
                  literal({ id: 101, text: "andre teksten min" }),
                  itemList({ id: 102, items: [item(literal({ text: "item 1" })), item(literal({ text: "item 2" }))] }),
                  literal({ id: 103, text: "tredje teksten min" }),
                ],
              }),
              newParagraph({ id: 2, content: [literal({ text: "Bare en ny setning" })] }),
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

            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([100, 101, 102, 103]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(2);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
        });
        describe("paragraph + ul", () => {
          test("single paste", () => {
            const clipboard = new MockDataTransfer({
              "text/html":
                "<div><p><span><span>Punktliste</span></span><span> </span></p></div><div><ul><li><p><span><span>Første punkt</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Andre punkt</span></span><span> </span></p></li></ul></div>",
            });
            const state = letter(newParagraph({ id: 1, content: [literal({ text: "Hei" })] }));
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

            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(result.redigertBrev.deletedBlocks).toEqual([]);
          });
          test("multiple paste", () => {
            const clipboard = new MockDataTransfer({
              "text/html":
                "<div><p><span><span>Punktliste</span></span><span> </span></p></div><div><ul><li><p><span><span>Første punkt</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Andre punkt</span></span><span> </span></p></li></ul></div>",
            });
            const state = letter(newParagraph({ id: 1, content: [literal({ text: "Hei" })] }));
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

            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
            expect(second.redigertBrev.deletedBlocks).toEqual([]);
          });
        });
      });
    });

    describe("paste into an item", () => {
      describe("start of literal", () => {
        describe("inserts a single word", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [itemList({ id: 10, items: [item(literal({ id: 101, text: "Teksten min" }))] })],
              }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span>" });
            const result = Actions.paste(state, index, 0, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("1Teksten min");
            expect(result.focus).toEqual({ ...index, cursorPosition: 1 });

            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([]);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 1, itemIndex: 1, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ text: "punktliste" }),
                  itemList({
                    id: 11,
                    items: [
                      item(literal({ text: "c1-i0-ic0" }), variable("c1-i0-ic1"), literal({ text: "c1-i0-ic2" })),
                      newItem({
                        id: 111,
                        content: [
                          literal({ text: "c1-i1-ic0" }),
                          variable("c1-i1-ic1"),
                          literal({ text: "c1-i1-ic2" }),
                        ],
                      }),
                      item(literal({ text: "c1-i2-ic0" }), variable("c1-i2-ic1"), literal({ text: "c1-i2-ic2" })),
                    ],
                  }),
                  literal({ text: "Etter punktliste" }),
                ],
              }),
              newParagraph({ id: 2, content: [literal({ text: "Bare en ny setning" })] }),
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

            expect(select<Item>(result, { blockIndex: 0, contentIndex: 1, itemIndex: 1 }).deletedContent).toEqual([]);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 1, itemIndex: 1 }).id).toEqual(111);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 1 }).deletedItems).toEqual([]);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 1 }).id).toEqual(11);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(2);
          });
        });

        test("inserts multiple words", () => {
          const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
          const state = letter(
            newParagraph({
              id: 1,
              content: [
                itemList({
                  id: 10,
                  items: [newItem({ id: 100, content: [literal({ id: 1000, text: "Teksten min" })] })],
                }),
              ],
            }),
          );
          const clipboard = new MockDataTransfer({ "text/html": "<span>1</span><span> 2</span" });
          const result = Actions.paste(state, index, 0, clipboard);

          expect(text(select<LiteralValue>(result, index))).toEqual("1 2Teksten min");
          expect(result.focus).toEqual({ ...index, cursorPosition: 3 });

          expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
          expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id).toEqual(100);
          expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([]);
          expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
          expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
          expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
        });

        describe("inserts single paragraph", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({
                    id: 10,
                    items: [newItem({ id: 100, content: [literal({ id: 1000, text: "Teksten min" })] })],
                  }),
                ],
              }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const result = Actions.paste(state, index, 0, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("1");
            expect(text(select<LiteralValue>(result, { ...index, itemIndex: 1, itemContentIndex: 0 }))).toEqual(
              "Teksten min",
            );
            expect(result.focus).toEqual({ ...index, itemIndex: 1, itemContentIndex: 0, cursorPosition: 0 });

            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id).toEqual(null);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 1 }).deletedContent).toEqual([]);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 1 }).id).toEqual(100);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([]);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
          });

          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 1, itemIndex: 1, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ text: "punktliste" }),
                  itemList({
                    id: 11,
                    items: [
                      item(literal({ text: "c1-i0-ic0" }), variable("c1-i0-ic1"), literal({ text: "c1-i0-ic2" })),
                      newItem({
                        id: 111,
                        content: [
                          literal({ text: "c1-i1-ic0" }),
                          variable("c1-i1-ic1"),
                          literal({ text: "c1-i1-ic2" }),
                        ],
                      }),
                      item(literal({ text: "c1-i2-ic0" }), variable("c1-i2-ic1"), literal({ text: "c1-i2-ic2" })),
                    ],
                  }),
                  literal({ text: "Etter punktliste" }),
                ],
              }),
              newParagraph({ id: 2, content: [literal({ text: "Bare en ny setning" })] }),
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

            expect(select<Item>(result, { blockIndex: 0, contentIndex: 1, itemIndex: 2 }).deletedContent).toEqual([]);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 1, itemIndex: 2 }).id).toEqual(111);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 1 }).deletedItems).toEqual([]);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 1 }).id).toEqual(11);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(2);
          });
        });
        describe("inserts multiple paragraphs", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({
                    id: 10,
                    items: [newItem({ id: 100, content: [literal({ id: 1000, text: "Teksten min" })] })],
                  }),
                ],
              }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p><p>2</p>" });
            const result = Actions.paste(state, index, 0, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("1");
            expect(text(select<LiteralValue>(result, { ...index, itemIndex: 1, itemContentIndex: 0 }))).toEqual("2");
            expect(text(select<LiteralValue>(result, { ...index, itemIndex: 2, itemContentIndex: 0 }))).toEqual(
              "Teksten min",
            );
            expect(result.focus).toEqual({ ...index, itemIndex: 2, itemContentIndex: 0, cursorPosition: 0 });

            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 2 }).deletedContent).toEqual([]);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 2 }).id).toEqual(100);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([]);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({
                    id: 10,
                    items: [newItem({ id: 100, content: [literal({ text: "Teksten min" })] })],
                  }),
                ],
              }),
            );
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

            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 4 }).deletedContent).toEqual([]);
            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 4 }).id).toEqual(100);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([]);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
          });
        });
        describe("inserts ul list", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({
                    id: 10,
                    items: [newItem({ id: 100, content: [literal({ id: 1000, text: "Teksten min" })] })],
                  }),
                ],
              }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
            const result = Actions.paste(state, index, 0, clipboard);

            expect(text(select<LiteralValue>(result, { ...index }))).toEqual("1");
            expect(text(select<LiteralValue>(result, { ...index, itemContentIndex: 0, itemIndex: 1 }))).toEqual("2");
            expect(text(select<LiteralValue>(result, { ...index, itemContentIndex: 0, itemIndex: 2 }))).toEqual(
              "Teksten min",
            );
            expect(result.focus).toEqual({ ...index, itemIndex: 2, itemContentIndex: 0, cursorPosition: 0 });

            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 2 }).deletedContent).toEqual([]);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 2 }).id).toEqual(100);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([]);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({
                    id: 10,
                    items: [newItem({ id: 100, content: [literal({ id: 1000, text: "Teksten min" })] })],
                  }),
                ],
              }),
            );
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

            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 4 }).deletedContent).toEqual([]);
            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 4 }).id).toEqual(100);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([]);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 1, itemIndex: 1, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ text: "punktliste" }),
                  itemList({
                    id: 11,
                    items: [
                      item(literal({ text: "c1-i0-ic0" }), variable("c1-i0-ic1"), literal({ text: "c1-i0-ic2" })),
                      newItem({
                        id: 111,
                        content: [
                          literal({ text: "c1-i1-ic0" }),
                          variable("c1-i1-ic1"),
                          literal({ text: "c1-i1-ic2" }),
                        ],
                      }),
                      item(literal({ text: "c1-i2-ic0" }), variable("c1-i2-ic1"), literal({ text: "c1-i2-ic2" })),
                    ],
                  }),
                  literal({ text: "Etter punktliste" }),
                ],
              }),
              newParagraph({ id: 2, content: [literal({ text: "Bare en ny setning" })] }),
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

            expect(select<Item>(result, { blockIndex: 0, contentIndex: 1, itemIndex: 3 }).deletedContent).toEqual([]);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 1, itemIndex: 3 }).id).toEqual(111);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 1 }).deletedItems).toEqual([]);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 1 }).id).toEqual(11);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(2);
          });
        });
        describe("paragraph + ul", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const clipboard = new MockDataTransfer({
              "text/html":
                "<div><p><span><span>Punktliste</span></span><span> </span></p></div><div><ul><li><p><span><span>Første punkt</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Andre punkt</span></span><span> </span></p></li></ul></div>",
            });
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({
                    id: 10,
                    items: [newItem({ id: 100, content: [literal({ text: "Hei" })] })],
                  }),
                ],
              }),
            );
            const result = Actions.paste(state, index, 0, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Punktliste");
            expect(text(select<LiteralValue>(result, { ...index, itemIndex: 1 }))).toEqual("Første punkt");
            expect(text(select<LiteralValue>(result, { ...index, itemIndex: 2 }))).toEqual("Andre punkt");
            expect(text(select<LiteralValue>(result, { ...index, itemIndex: 3 }))).toEqual("Hei");
            expect(result.focus).toEqual({ ...index, itemIndex: 3, cursorPosition: 0 });

            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 3 }).deletedContent).toEqual([]);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 3 }).id).toEqual(100);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([]);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const clipboard = new MockDataTransfer({
              "text/html":
                "<div><p><span><span>Punktliste</span></span><span> </span></p></div><div><ul><li><p><span><span>Første punkt</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Andre punkt</span></span><span> </span></p></li></ul></div>",
            });
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({
                    id: 10,
                    items: [newItem({ id: 100, content: [literal({ text: "Hei" })] })],
                  }),
                ],
              }),
            );
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

            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 6 }).deletedContent).toEqual([]);
            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 6 }).id).toEqual(100);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([]);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
          });
        });
      });
      describe("in the middle of a literal", () => {
        describe("inserts a single word", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({ id: 10, items: [newItem({ id: 100, content: [literal({ text: "Teksten min" })] })] }),
                ],
              }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span>" });
            const result = Actions.paste(state, index, 7, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Teksten1 min");
            expect(result.focus).toEqual({ ...index, cursorPosition: 8 });

            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id).toEqual(100);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([]);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({ id: 10, items: [newItem({ id: 100, content: [literal({ text: "Teksten min" })] })] }),
                ],
              }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span>" });
            const first = Actions.paste(state, index, 7, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Teksten11 min");
            expect(second.focus).toEqual({ ...index, cursorPosition: 9 });

            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id).toEqual(100);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([]);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 1, itemIndex: 1, itemContentIndex: 1 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ text: "punktliste" }),
                  itemList({
                    id: 11,
                    items: [
                      item(variable("c1-i0-ic0"), literal({ text: "c1-i0-ic1" }), variable("c1-i0-ic2")),
                      newItem({
                        id: 111,
                        content: [variable("c1-i1-ic0"), literal({ text: "c1-i1-ic1" }), variable("c1-i1-ic2")],
                      }),
                      item(variable("c1-i2-ic0"), literal({ text: "c1-i2-ic1" }), variable("c1-i2-ic2")),
                    ],
                  }),
                  literal({ text: "Etter punktliste" }),
                ],
              }),
              newParagraph({ id: 2, content: [literal({ text: "Bare en ny setning" })] }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<span>Pasta</span>" });
            const result = Actions.paste(state, idx, 4, clipboard);

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
              "c1-iPasta1-ic1",
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
            expect(result.focus).toEqual({ ...idx, cursorPosition: 9 });

            expect(select<Item>(result, { blockIndex: 0, contentIndex: 1, itemIndex: 1 }).deletedContent).toEqual([]);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 1, itemIndex: 1 }).id).toEqual(111);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 1 }).deletedItems).toEqual([]);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 1 }).id).toEqual(11);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(2);
          });
        });
        describe("inserts multiple words", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({ id: 10, items: [newItem({ id: 100, content: [literal({ text: "Teksten min" })] })] }),
                ],
              }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span><span>2</span" });
            const result = Actions.paste(state, index, 7, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Teksten1 2 min");
            expect(result.focus).toEqual({ ...index, cursorPosition: 10 });

            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id).toEqual(100);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([]);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({ id: 10, items: [newItem({ id: 100, content: [literal({ text: "Teksten min" })] })] }),
                ],
              }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<span>1</span><span>2</span" });
            const first = Actions.paste(state, index, 7, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Teksten1 21 2 min");
            expect(second.focus).toEqual({ ...index, cursorPosition: 13 });

            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id).toEqual(100);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([]);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
          });
        });
        describe("inserts single paragraph", () => {
          test("single paste", () => {
            const idx = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({ id: 10, items: [newItem({ id: 100, content: [literal({ text: "Teksten min" })] })] }),
                ],
              }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const res = Actions.paste(state, idx, 7, clipboard);

            expect(
              text(select<LiteralValue>(res, { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 })),
            ).toEqual("Teksten1");
            expect(
              text(select<LiteralValue>(res, { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 })),
            ).toEqual(" min");
            expect(res.focus).toEqual({
              blockIndex: 0,
              contentIndex: 0,
              itemIndex: 1,
              itemContentIndex: 0,
              cursorPosition: 0,
            });

            expect(select<Item>(res, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
            expect(select<Item>(res, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id).toEqual(null);
            expect(select<ItemList>(res, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([100]);
            expect(select<ItemList>(res, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(res, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(res, { blockIndex: 0 }).id).toEqual(1);
          });
          test("multiple paste", () => {
            const idx = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({ id: 10, items: [newItem({ id: 100, content: [literal({ text: "Teksten min" })] })] }),
                ],
              }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const first = Actions.paste(state, idx, 7, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(
              text(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 })),
            ).toEqual("Teksten1");
            expect(
              text(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 })),
            ).toEqual("1");
            expect(
              text(select<LiteralValue>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 2, itemContentIndex: 0 })),
            ).toEqual(" min");
            expect(second.focus).toEqual({
              blockIndex: 0,
              contentIndex: 0,
              itemIndex: 2,
              itemContentIndex: 0,
              cursorPosition: 0,
            });

            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id).toEqual(null);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([100]);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 1, itemIndex: 1, itemContentIndex: 1 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ text: "punktliste" }),
                  itemList({
                    id: 11,
                    items: [
                      newItem({
                        id: 110,
                        content: [
                          variable("c1-i0-ic0"),
                          literal({ id: 1101, text: "c1-i0-ic1" }),
                          variable("c1-i0-ic2"),
                        ],
                      }),
                      newItem({
                        id: 111,
                        content: [
                          variable("c1-i1-ic0"),
                          literal({ id: 1111, text: "c1-i1-ic1" }),
                          variable("c1-i1-ic2"),
                        ],
                      }),
                      newItem({
                        id: 112,
                        content: [
                          variable("c1-i2-ic0"),
                          literal({ id: 1121, text: "c1-i2-ic1" }),
                          variable("c1-i2-ic2"),
                        ],
                      }),
                    ],
                  }),
                  literal({ text: "Etter punktliste" }),
                ],
              }),
              newParagraph({ id: 2, content: [literal({ text: "Bare en ny setning" })] }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<p>Pasta</p>" });
            const res = Actions.paste(state, idx, 4, clipboard);

            expect(text(select<LiteralValue>(res, { blockIndex: 0, contentIndex: 0 }))).toEqual("punktliste");
            expect(
              text(select<LiteralValue>(res, { blockIndex: 0, contentIndex: 1, itemIndex: 0, itemContentIndex: 0 })),
            ).toEqual("c1-i0-ic0");
            expect(
              text(select<LiteralValue>(res, { blockIndex: 0, contentIndex: 1, itemIndex: 0, itemContentIndex: 1 })),
            ).toEqual("c1-i0-ic1");
            expect(
              text(select<LiteralValue>(res, { blockIndex: 0, contentIndex: 1, itemIndex: 0, itemContentIndex: 2 })),
            ).toEqual("c1-i0-ic2");
            expect(
              text(select<LiteralValue>(res, { blockIndex: 0, contentIndex: 1, itemIndex: 1, itemContentIndex: 0 })),
            ).toEqual("c1-i1-ic0");
            expect(
              text(select<LiteralValue>(res, { blockIndex: 0, contentIndex: 1, itemIndex: 1, itemContentIndex: 1 })),
            ).toEqual("c1-iPasta");
            expect(
              text(select<LiteralValue>(res, { blockIndex: 0, contentIndex: 1, itemIndex: 2, itemContentIndex: 0 })),
            ).toEqual("1-ic1");
            expect(
              text(select<LiteralValue>(res, { blockIndex: 0, contentIndex: 1, itemIndex: 2, itemContentIndex: 1 })),
            ).toEqual("c1-i1-ic2");
            expect(
              text(select<LiteralValue>(res, { blockIndex: 0, contentIndex: 1, itemIndex: 3, itemContentIndex: 0 })),
            ).toEqual("c1-i2-ic0");
            expect(
              text(select<LiteralValue>(res, { blockIndex: 0, contentIndex: 1, itemIndex: 3, itemContentIndex: 1 })),
            ).toEqual("c1-i2-ic1");
            expect(
              text(select<LiteralValue>(res, { blockIndex: 0, contentIndex: 1, itemIndex: 3, itemContentIndex: 2 })),
            ).toEqual("c1-i2-ic2");
            expect(text(select<LiteralValue>(res, { blockIndex: 0, contentIndex: 2 }))).toEqual("Etter punktliste");

            expect(text(select<LiteralValue>(res, { blockIndex: 1, contentIndex: 0 }))).toEqual("Bare en ny setning");
            expect(res.focus).toEqual({ ...idx, itemIndex: 2, itemContentIndex: 0, cursorPosition: 0 });

            expect(select<Item>(res, { blockIndex: 0, contentIndex: 1, itemIndex: 1 }).deletedContent).toEqual([]);
            expect(select<Item>(res, { blockIndex: 0, contentIndex: 1, itemIndex: 1 }).id).toEqual(null);
            expect(select<Item>(res, { blockIndex: 0, contentIndex: 1, itemIndex: 2 }).deletedContent).toEqual([]);
            expect(select<Item>(res, { blockIndex: 0, contentIndex: 1, itemIndex: 2 }).id).toEqual(null);
            expect(select<ItemList>(res, { blockIndex: 0, contentIndex: 1 }).deletedItems).toEqual([111]);
            expect(select<ItemList>(res, { blockIndex: 0, contentIndex: 1 }).id).toEqual(11);
            expect(select<ParagraphBlock>(res, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(res, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(res, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(res, { blockIndex: 1 }).id).toEqual(2);
          });
        });
        describe("inserts multiple paragraphs", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({ id: 10, items: [newItem({ id: 100, content: [literal({ text: "Teksten min" })] })] }),
                ],
              }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p><p>2</p>" });
            const result = Actions.paste(state, index, 7, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Teksten1");
            expect(text(select<LiteralValue>(result, { ...index, itemIndex: 1, itemContentIndex: 0 }))).toEqual("2");
            expect(text(select<LiteralValue>(result, { ...index, itemIndex: 2, itemContentIndex: 0 }))).toEqual(" min");
            expect(result.focus).toEqual({ ...index, itemIndex: 2, itemContentIndex: 0, cursorPosition: 0 });

            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id).toEqual(null);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([100]);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({ id: 10, items: [newItem({ id: 100, content: [literal({ text: "Teksten min" })] })] }),
                ],
              }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p><p>2</p>" });
            const first = Actions.paste(state, index, 7, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Teksten1");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 1, itemContentIndex: 0 }))).toEqual("2");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 2, itemContentIndex: 0 }))).toEqual("1");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 3, itemContentIndex: 0 }))).toEqual("2");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 4, itemContentIndex: 0 }))).toEqual(" min");
            expect(second.focus).toEqual({ ...index, itemIndex: 4, itemContentIndex: 0, cursorPosition: 0 });

            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id).toEqual(null);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([100]);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
          });
        });
        describe("inserts ul list", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({ id: 10, items: [newItem({ id: 100, content: [literal({ text: "Teksten min" })] })] }),
                ],
              }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
            const result = Actions.paste(state, index, 7, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Teksten1");
            expect(text(select<LiteralValue>(result, { ...index, itemIndex: 1 }))).toEqual("2");
            expect(text(select<LiteralValue>(result, { ...index, itemIndex: 2 }))).toEqual(" min");
            expect(result.focus).toEqual({ ...index, itemIndex: 2, cursorPosition: 0 });

            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id).toEqual(null);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([100]);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({ id: 10, items: [newItem({ id: 100, content: [literal({ text: "Teksten min" })] })] }),
                ],
              }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
            const first = Actions.paste(state, index, 7, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Teksten1");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 1 }))).toEqual("2");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 2 }))).toEqual("1");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 3 }))).toEqual("2");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 4 }))).toEqual(" min");
            expect(second.focus).toEqual({ ...index, itemIndex: 4, cursorPosition: 0 });

            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id).toEqual(null);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([100]);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 1, itemIndex: 1, itemContentIndex: 1 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ text: "punktliste" }),
                  itemList({
                    id: 11,
                    items: [
                      newItem({
                        id: 110,
                        content: [
                          variable("c1-i0-ic0"),
                          literal({ id: 1101, text: "c1-i0-ic1" }),
                          variable("c1-i0-ic2"),
                        ],
                      }),
                      newItem({
                        id: 111,
                        content: [
                          variable("c1-i1-ic0"),
                          literal({ id: 1111, text: "c1-i1-ic1" }),
                          variable("c1-i1-ic2"),
                        ],
                      }),
                      newItem({
                        id: 112,
                        content: [
                          variable("c1-i2-ic0"),
                          literal({ id: 1121, text: "c1-i2-ic1" }),
                          variable("c1-i2-ic2"),
                        ],
                      }),
                    ],
                  }),
                  literal({ text: "Etter punktliste" }),
                ],
              }),
              newParagraph({ id: 2, content: [literal({ text: "Bare en ny setning" })] }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
            const result = Actions.paste(state, idx, 4, clipboard);

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
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 1, itemContentIndex: 1 }))).toEqual("c1-i1");
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 2, itemContentIndex: 0 }))).toEqual("2");
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 3, itemContentIndex: 0 }))).toEqual("1-ic1");
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 3, itemContentIndex: 1 }))).toEqual(
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

            expect(select<Item>(result, { blockIndex: 0, contentIndex: 1, itemIndex: 1 }).deletedContent).toEqual([]);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 1, itemIndex: 1 }).id).toEqual(null);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 1, itemIndex: 3 }).deletedContent).toEqual([]);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 1, itemIndex: 3 }).id).toEqual(null);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 1 }).deletedItems).toEqual([111]);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 1 }).id).toEqual(11);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(2);
          });
        });
        describe("paragraph + ul", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const clipboard = new MockDataTransfer({
              "text/html":
                "<div><p><span><span>Punktliste</span></span><span> </span></p></div><div><ul><li><p><span><span>Første punkt</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Andre punkt</span></span><span> </span></p></li></ul></div>",
            });
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({ id: 10, items: [newItem({ id: 100, content: [literal({ text: "Teksten min" })] })] }),
                ],
              }),
            );
            const result = Actions.paste(state, index, 7, clipboard);

            expect(
              text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 })),
            ).toEqual("TekstenPunktliste");
            expect(text(select<LiteralValue>(result, { ...index, itemIndex: 1 }))).toEqual("Første punkt");
            expect(text(select<LiteralValue>(result, { ...index, itemIndex: 2 }))).toEqual("Andre punkt");
            expect(text(select<LiteralValue>(result, { ...index, itemIndex: 3 }))).toEqual(" min");
            expect(result.focus).toEqual({ ...index, itemIndex: 3, cursorPosition: 0 });

            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id).toEqual(null);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([100]);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const clipboard = new MockDataTransfer({
              "text/html":
                "<div><p><span><span>Punktliste</span></span><span> </span></p></div><div><ul><li><p><span><span>Første punkt</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Andre punkt</span></span><span> </span></p></li></ul></div>",
            });
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({ id: 10, items: [newItem({ id: 100, content: [literal({ text: "Teksten min" })] })] }),
                ],
              }),
            );
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

            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id).toEqual(null);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([100]);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
          });
        });
      });
      describe("at the end of a literal", () => {
        describe("inserts a single word", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({ id: 10, items: [newItem({ id: 100, content: [literal({ text: "Teksten min" })] })] }),
                ],
              }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<span>Single</span>" });
            const result = Actions.paste(state, index, 11, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Teksten minSingle");
            expect(result.focus).toEqual({ ...index, cursorPosition: 17 });

            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id).toEqual(100);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([]);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({ id: 10, items: [newItem({ id: 100, content: [literal({ text: "Teksten min" })] })] }),
                ],
              }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<span>Single</span>" });
            const first = Actions.paste(state, index, 11, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Teksten minSingleSingle");
            expect(second.focus).toEqual({ ...index, cursorPosition: 23 });

            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id).toEqual(100);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([]);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 1, itemIndex: 1, itemContentIndex: 2 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ text: "punktliste" }),
                  itemList({
                    id: 11,
                    items: [
                      item(literal({ text: "c1-i0-ic0" }), variable("c1-i0-ic1"), literal({ text: "c1-i0-ic2" })),
                      newItem({
                        id: 111,
                        content: [
                          literal({ text: "c1-i1-ic0" }),
                          variable("c1-i1-ic1"),
                          literal({ text: "c1-i1-ic2" }),
                        ],
                      }),
                      item(literal({ text: "c1-i2-ic0" }), variable("c1-i2-ic1"), literal({ text: "c1-i2-ic2" })),
                    ],
                  }),
                  literal({ text: "Etter punktliste" }),
                ],
              }),
              newParagraph({ id: 2, content: [literal({ text: "Bare en ny setning" })] }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<span>Pasta</span>" });
            const result = Actions.paste(state, idx, 9, clipboard);

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
              "c1-i1-ic1",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 1, itemContentIndex: 2 }))).toEqual(
              "c1-i1-ic2Pasta",
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
            expect(result.focus).toEqual({ ...idx, cursorPosition: 14 });

            expect(select<Item>(result, { blockIndex: 0, contentIndex: 1, itemIndex: 1 }).deletedContent).toEqual([]);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 1, itemIndex: 1 }).id).toEqual(111);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 1 }).deletedItems).toEqual([]);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 1 }).id).toEqual(11);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(2);
          });
        });
        describe("inserts multiple words", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({ id: 10, items: [newItem({ id: 100, content: [literal({ text: "Teksten min" })] })] }),
                ],
              }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<span>Single</span><span>Spanner</span>" });
            const result = Actions.paste(state, index, 11, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Teksten minSingle Spanner");
            expect(result.focus).toEqual({ ...index, cursorPosition: 25 });

            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id).toEqual(100);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([]);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({ id: 10, items: [newItem({ id: 100, content: [literal({ text: "Teksten min" })] })] }),
                ],
              }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<span>Single</span><span>Spanner</span>" });
            const first = Actions.paste(state, index, 11, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Teksten minSingle SpannerSingle Spanner");
            expect(second.focus).toEqual({ ...index, cursorPosition: 39 });

            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id).toEqual(100);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([]);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
          });
        });
        describe("inserts single paragraph", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({
                    id: 10,
                    items: [newItem({ id: 100, content: [literal({ id: 1000, text: "Teksten min" })] })],
                  }),
                ],
              }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const result = Actions.paste(state, index, 11, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Teksten min1");
            expect(result.focus).toEqual({ ...index, itemIndex: 0, itemContentIndex: 0, cursorPosition: 12 });

            expect(select<LiteralValue>(result, index).id).toEqual(1000);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id).toEqual(100);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([]);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({ id: 10, items: [newItem({ id: 100, content: [literal({ text: "Teksten min" })] })] }),
                ],
              }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p>" });
            const first = Actions.paste(state, index, 11, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Teksten min11");
            expect(second.focus).toEqual({ ...index, itemIndex: 0, itemContentIndex: 0, cursorPosition: 13 });

            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id).toEqual(100);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([]);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 1, itemIndex: 1, itemContentIndex: 2 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ text: "punktliste" }),
                  itemList({
                    id: 11,
                    items: [
                      item(literal({ text: "c1-i0-ic0" }), variable("c1-i0-ic1"), literal({ text: "c1-i0-ic2" })),
                      item(literal({ text: "c1-i1-ic0" }), variable("c1-i1-ic1"), literal({ text: "c1-i1-ic2" })),
                      newItem({
                        id: 112,
                        content: [
                          literal({ text: "c1-i2-ic0" }),
                          variable("c1-i2-ic1"),
                          literal({ text: "c1-i2-ic2" }),
                        ],
                      }),
                    ],
                  }),
                  literal({ text: "Etter punktliste" }),
                ],
              }),
              newParagraph({ id: 2, content: [literal({ text: "Bare en ny setning" })] }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<p>Pasta</p>" });
            const result = Actions.paste(state, idx, 9, clipboard);

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
              "c1-i1-ic1",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 1, itemContentIndex: 2 }))).toEqual(
              "c1-i1-ic2Pasta",
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
            expect(result.focus).toEqual({ ...idx, cursorPosition: 14 });

            expect(select<Item>(result, { blockIndex: 0, contentIndex: 1, itemIndex: 1 }).deletedContent).toEqual([]);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 1, itemIndex: 2 }).id).toEqual(112);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 1 }).deletedItems).toEqual([]);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 1 }).id).toEqual(11);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toEqual(2);
          });
        });
        describe("inserts multiple paragraphs", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({ id: 10, items: [newItem({ id: 100, content: [literal({ text: "Teksten min" })] })] }),
                ],
              }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p><p>2</p>" });
            const result = Actions.paste(state, index, 11, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Teksten min1");
            expect(text(select<LiteralValue>(result, { ...index, itemIndex: 1, itemContentIndex: 0 }))).toEqual("2");
            expect(result.focus).toEqual({ ...index, itemIndex: 1, itemContentIndex: 0, cursorPosition: 1 });

            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id).toEqual(100);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([]);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({ id: 10, items: [newItem({ id: 100, content: [literal({ text: "Teksten min" })] })] }),
                ],
              }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<p>1</p><p>2</p>" });
            const first = Actions.paste(state, index, 11, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Teksten min1");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 1, itemContentIndex: 0 }))).toEqual("21");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 2, itemContentIndex: 0 }))).toEqual("2");
            expect(second.focus).toEqual({ ...index, itemIndex: 2, itemContentIndex: 0, cursorPosition: 1 });

            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id).toEqual(100);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([]);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
          });
        });
        describe("inserts ul list", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({ id: 10, items: [newItem({ id: 100, content: [literal({ text: "Teksten min" })] })] }),
                ],
              }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
            const result = Actions.paste(state, index, 15, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("Teksten min1");
            expect(text(select<LiteralValue>(result, { ...index, itemIndex: 1 }))).toEqual("2");
            expect(result.focus).toEqual({ ...index, itemIndex: 1, cursorPosition: 1 });

            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id).toEqual(100);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([]);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  itemList({ id: 10, items: [newItem({ id: 100, content: [literal({ text: "Teksten min" })] })] }),
                ],
              }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
            const first = Actions.paste(state, index, 11, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("Teksten min1");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 1 }))).toEqual("21");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 2 }))).toEqual("2");
            expect(second.focus).toEqual({ ...index, itemIndex: 2, cursorPosition: 1 });

            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id).toEqual(100);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([]);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
          });
          test("complex letter - literals and variables", () => {
            const idx = { blockIndex: 0, contentIndex: 1, itemIndex: 2, itemContentIndex: 2 };
            const state = letter(
              newParagraph({
                id: 1,
                content: [
                  literal({ text: "punktliste" }),
                  itemList({
                    id: 11,
                    items: [
                      item(literal({ text: "c1-i0-ic0" }), variable("c1-i0-ic1"), literal({ text: "c1-i0-ic2" })),
                      item(literal({ text: "c1-i1-ic0" }), variable("c1-i1-ic1"), literal({ text: "c1-i1-ic2" })),
                      newItem({
                        id: 113,
                        content: [
                          literal({ text: "c1-i2-ic0" }),
                          variable("c1-i2-ic1"),
                          literal({ text: "c1-i2-ic2" }),
                        ],
                      }),
                    ],
                  }),
                  literal({ text: "Etter punktliste" }),
                ],
              }),
              newParagraph({ id: 2, content: [literal({ text: "Bare en ny setning" })] }),
            );
            const clipboard = new MockDataTransfer({ "text/html": "<ul><li>1</li><li>2</li></ul>" });
            const result = Actions.paste(state, idx, 9, clipboard);

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
              "c1-i2-ic21",
            );
            expect(text(select<LiteralValue>(result, { ...idx, itemIndex: 3, itemContentIndex: 0 }))).toEqual("2");
            expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 2 }))).toEqual("Etter punktliste");
            expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual(
              "Bare en ny setning",
            );
            expect(result.focus).toEqual({ ...idx, itemIndex: 3, itemContentIndex: 0, cursorPosition: 1 });

            expect(select<Item>(result, { blockIndex: 0, contentIndex: 1, itemIndex: 1 }).deletedContent).toEqual([]);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 1, itemIndex: 2 }).id).toEqual(113);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 1 }).deletedItems).toEqual([]);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 1 }).id).toEqual(11);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
          });
        });
        describe("paragraph + ul", () => {
          test("single paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const clipboard = new MockDataTransfer({
              "text/html":
                "<div><p><span><span>Punktliste</span></span><span> </span></p></div><div><ul><li><p><span><span>Første punkt</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Andre punkt</span></span><span> </span></p></li></ul></div>",
            });
            const state = letter(
              newParagraph({
                id: 1,
                content: [itemList({ id: 10, items: [newItem({ id: 100, content: [literal({ text: "Hei" })] })] })],
              }),
            );
            const result = Actions.paste(state, index, 4, clipboard);

            expect(text(select<LiteralValue>(result, index))).toEqual("HeiPunktliste");
            expect(text(select<LiteralValue>(result, { ...index, itemIndex: 1 }))).toEqual("Første punkt");
            expect(text(select<LiteralValue>(result, { ...index, itemIndex: 2 }))).toEqual("Andre punkt");
            expect(result.focus).toEqual({ ...index, itemIndex: 2, cursorPosition: 11 });

            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
            expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id).toEqual(100);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([]);
            expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(result, { blockIndex: 0 }).id).toEqual(1);
          });
          test("multiple paste", () => {
            const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
            const clipboard = new MockDataTransfer({
              "text/html":
                "<div><p><span><span>Punktliste</span></span><span> </span></p></div><div><ul><li><p><span><span>Første punkt</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Andre punkt</span></span><span> </span></p></li></ul></div>",
            });
            const state = letter(
              newParagraph({
                id: 1,
                content: [itemList({ id: 10, items: [newItem({ id: 100, content: [literal({ text: "Hei" })] })] })],
              }),
            );
            const first = Actions.paste(state, index, 4, clipboard);
            const second = Actions.paste(first, first.focus, first.focus.cursorPosition!, clipboard);

            expect(text(select<LiteralValue>(second, index))).toEqual("HeiPunktliste");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 1 }))).toEqual("Første punkt");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 2 }))).toEqual("Andre punktPunktliste");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 3 }))).toEqual("Første punkt");
            expect(text(select<LiteralValue>(second, { ...index, itemIndex: 4 }))).toEqual("Andre punkt");
            expect(second.focus).toEqual({ ...index, itemIndex: 4, cursorPosition: 11 });

            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).deletedContent).toEqual([]);
            expect(select<Item>(second, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id).toEqual(100);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).deletedItems).toEqual([]);
            expect(select<ItemList>(second, { blockIndex: 0, contentIndex: 0 }).id).toEqual(10);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).deletedContent).toEqual([]);
            expect(select<ParagraphBlock>(second, { blockIndex: 0 }).id).toEqual(1);
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
