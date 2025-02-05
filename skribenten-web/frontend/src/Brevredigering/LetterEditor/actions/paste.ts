import type { Draft } from "immer";
import { produce } from "immer";

import { newItem, newItemList, newLiteral, newParagraph, text } from "~/Brevredigering/LetterEditor/actions/common";
import type { LiteralIndex } from "~/Brevredigering/LetterEditor/actions/model";
import { splitRecipe } from "~/Brevredigering/LetterEditor/actions/split";
import { updateLiteralText } from "~/Brevredigering/LetterEditor/actions/updateContentText";
import type { Action } from "~/Brevredigering/LetterEditor/lib/actions";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import type { LiteralValue } from "~/types/brevbakerTypes";
import { PARAGRAPH, TITLE1, TITLE2 } from "~/types/brevbakerTypes";
import { LITERAL } from "~/types/brevbakerTypes";
import { ITEM_LIST } from "~/types/brevbakerTypes";

export const paste: Action<LetterEditorState, [literalIndex: LiteralIndex, offset: number, clipboard: DataTransfer]> =
  produce((draft, literalIndex, offset, clipboard) => {
    if (clipboard.types.includes("text/html")) {
      insertHtmlClipboardInLetter(draft, literalIndex, offset, clipboard);
    } else if (clipboard.types.includes("text/plain")) {
      insertTextInLetter(draft, literalIndex, offset, clipboard.getData("text/plain"));
    } else {
      log("unsupported clipboard datatype(s) - " + JSON.stringify(clipboard.types));
    }
  });

export function logPastedClipboard(clipboardData: DataTransfer) {
  log("available paste types - " + JSON.stringify(clipboardData.types));
  log("pasted html content - " + clipboardData.getData("text/html"));
  log("pasted plain content - " + clipboardData.getData("text/plain"));
}

function insertTextInLetter(draft: Draft<LetterEditorState>, literalIndex: LiteralIndex, offset: number, str: string) {
  const content = draft.redigertBrev.blocks[literalIndex.blockIndex]?.content[literalIndex.contentIndex];
  const cursorPositionUpdater = (l: number) => (draft.focus.cursorPosition = l);
  // const cursorPositionUpdater = (l: number) => void 0;

  if (content?.type === ITEM_LIST && "itemContentIndex" in literalIndex) {
    const itemContent = content.items[literalIndex.itemIndex]?.content[literalIndex?.itemContentIndex];
    if (itemContent?.type === LITERAL) {
      insertText(itemContent, offset, str, cursorPositionUpdater);
    } else {
      log("cannot insert text into variable - " + str);
    }
  } else if (content?.type === LITERAL && !("itemContentIndex" in literalIndex)) {
    insertText(content, offset, str, cursorPositionUpdater);
  } else {
    log("literalIndex is invalid " + JSON.stringify(literalIndex));
    log("text - " + str);
  }
}

function insertText(
  draft: Draft<LiteralValue>,
  offset: number,
  str: string,
  cursorPositionUpdater: (l: number) => void,
) {
  const existingText = text(draft);
  if (offset <= 0) {
    updateLiteralText(draft, str + existingText);
    cursorPositionUpdater(str.length);
  } else if (offset >= existingText.length) {
    updateLiteralText(draft, existingText + str);
    cursorPositionUpdater(existingText.length + str.length);
  } else {
    const text = existingText.slice(0, Math.max(0, offset)) + str + existingText.slice(Math.max(0, offset));
    updateLiteralText(draft, text);
    cursorPositionUpdater(text.length);
  }
}

function insertHtmlClipboardInLetter(
  draft: Draft<LetterEditorState>,
  literalIndex: LiteralIndex,
  offset: number,
  clipboard: DataTransfer,
) {
  const parser = new DOMParser();
  const document = parser.parseFromString(clipboard.getData("text/html"), "text/html");

  insertElement(draft, literalIndex, offset, document.body);
}

function insertElement(draft: Draft<LetterEditorState>, literalIndex: LiteralIndex, offset: number, parent: Element) {
  for (let i = 0; i < parent.children.length; i++) {
    const element = parent.children[i];

    switch (element.tagName) {
      case "DIV": {
        insertElement(draft, literalIndex, offset, element);
        break;
      }
      case "SPAN": {
        const str = element.textContent ?? "";
        insertTextInLetter(draft, literalIndex, offset, str);
        offset += str.length;
        break;
      }
      case "P": {
        const str = element.textContent ?? "";
        if (i === 0) {
          insertTextInLetter(draft, literalIndex, offset, str);
          offset += str.length;
        } else {
          splitRecipe(draft, literalIndex, offset);
          if ("itemContentIndex" in literalIndex) {
            literalIndex = { ...literalIndex, itemIndex: literalIndex.itemIndex + 1 };
            offset = 0;
          } else {
            literalIndex = { ...literalIndex, blockIndex: literalIndex.blockIndex + 1 };
            offset = 0;
          }
          insertTextInLetter(draft, literalIndex, offset, str);
        }
        break;
      }
      case "OL":
      case "UL": {
        interpretULorOL(draft, literalIndex, offset, element);
        break;
      }
      case "BR": {
        break;
      }
      default: {
        log("unsupported element " + element.tagName);
      }
    }
  }
  draft.focus = { ...literalIndex, cursorPosition: offset };
}

function interpretULorOL(draft: Draft<LetterEditorState>, literalIndex: LiteralIndex, offset: number, ul: Element) {
  let isFirstItem = true;

  for (const item of ul.children) {
    if (item.tagName === "LI") {
      const text = item.textContent ?? "";

      if (isFirstItem) {
        insertTextInLetter(draft, literalIndex, offset, text);
        offset += text.length;
        isFirstItem = false;
      } else {
        const currentContent = draft.redigertBrev.blocks[literalIndex.blockIndex]?.content?.[literalIndex.contentIndex];

        if (currentContent?.type === ITEM_LIST && "itemContentIndex" in literalIndex) {
          currentContent.items.push(newItem({ content: [newLiteral({ text })] }));
          literalIndex = { ...literalIndex, itemIndex: literalIndex.itemIndex + 1 };
          offset += text.length;
        } else if (currentContent?.type === LITERAL && !("itemContentIndex" in literalIndex)) {
          const currentBlock = draft.redigertBrev.blocks[literalIndex.blockIndex];

          if (currentBlock?.type === PARAGRAPH) {
            currentBlock.content.push(newItemList({ items: [newItem({ content: [newLiteral({ text })] })] }));
            literalIndex = {
              ...literalIndex,
              contentIndex: literalIndex.contentIndex + 1,
              itemIndex: 0,
              itemContentIndex: 0,
            };
            offset = text.length;
          } else if (currentBlock?.type === TITLE1 || currentBlock?.type === TITLE2) {
            draft.redigertBrev.blocks.splice(
              literalIndex.blockIndex + 1,
              0,
              newParagraph({ content: [newItemList({ items: [newItem({ content: [newLiteral({ text })] })] })] }),
            );
            literalIndex = {
              blockIndex: literalIndex.blockIndex + 1,
              contentIndex: 0,
              itemIndex: 0,
              itemContentIndex: 0,
            };
            offset = text.length;
          } else {
            log("literalIndex is invalid " + JSON.stringify(literalIndex));
            log("block doesn't exist");
          }
        } else {
          log("literalIndex is invalid " + JSON.stringify(literalIndex));
          log(
            "either literalIndex refers to an itemList item and block-content isn't that, or it refers to an itemList without itemContentIndex",
          );
        }
      }
    } else {
      log("pasted ul or ol contains unsupported element - " + item.tagName);
    }
  }
}

function log(message: string) {
  // eslint-disable-next-line no-console
  console.log("Skribenten:pasteHandler: " + message);
}
