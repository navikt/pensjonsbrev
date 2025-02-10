import type { Draft } from "immer";
import { produce } from "immer";

import { newItem, newItemList, newLiteral, newParagraph, text } from "~/Brevredigering/LetterEditor/actions/common";
import type { LiteralIndex } from "~/Brevredigering/LetterEditor/actions/model";
import { updateLiteralText } from "~/Brevredigering/LetterEditor/actions/updateContentText";
import type { Action } from "~/Brevredigering/LetterEditor/lib/actions";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import type { LiteralValue, ParagraphBlock } from "~/types/brevbakerTypes";
import { LITERAL } from "~/types/brevbakerTypes";
import { ITEM_LIST } from "~/types/brevbakerTypes";
import { handleSwitchContent } from "~/utils/brevbakerUtils";

import { isLiteral } from "../model/utils";

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

  const thisBlock = draft.redigertBrev.blocks[literalIndex.blockIndex];
  const blockContent = thisBlock.content;
  const current = blockContent[literalIndex.contentIndex];

  // const extracted = extractTextWithDeepestPath(document.body);
  // // console.log("current:", JSON.parse(JSON.stringify(current)));

  handleSwitchContent({
    content: current,
    onLiteral: (literal) => {
      const parsedHtmlToBrevbaker = parseHTMLToStructure(document.body);
      // console.log("------parsedHtmlToBrevbaker--------");
      // console.dir(JSON.parse(JSON.stringify(parsedHtmlToBrevbaker)), { depth: null });
      // console.log("-".repeat(10));

      const appendingToStartOfLiteral = offset === 0;
      const appendingToMiddleOfLiteral = offset > 0 && offset < (literal.editedText?.length ?? literal.text.length);

      const blocksBeforeThisBlock = draft.redigertBrev.blocks.slice(0, literalIndex.blockIndex);
      const blocksAfterThisBlock = draft.redigertBrev.blocks.slice(literalIndex.blockIndex + 1);

      const contentBeforeLiteral = blockContent.slice(0, literalIndex.contentIndex);
      const contentAfterLiteral = blockContent.slice(literalIndex.contentIndex + 1);

      if (appendingToStartOfLiteral) {
        console.log("appending to start of literal");
        if (parsedHtmlToBrevbaker.length === 0) {
          console.log("parsedLength is < 0. Ingenting å legge til");
          return;
        } else if (parsedHtmlToBrevbaker.length === 1) {
          console.log("parsedLength is 1");
        } else {
          console.log("parsedLength is > 1");

          const firstParsedBlock = parsedHtmlToBrevbaker[0];
          const firstParsedBlockContent = firstParsedBlock.content;

          const elementToBePrependedToLiteral = firstParsedBlockContent[0];
          const rest = firstParsedBlockContent.slice(1);

          const prependedLiteral = handleSwitchContent({
            content: elementToBePrependedToLiteral,
            onLiteral: (parsedLiteral) => {
              return [
                newLiteral({
                  ...literal,
                  text: parsedLiteral.text + (literal.editedText ?? literal.text),
                }),
              ];
            },
            onVariable: () => {
              throw new Error("Cannot paste into variable");
            },
            onItemList: () => {
              throw new Error("paste into itemList not yet implemented");
            },
            onNewLine: () => {
              throw new Error("paste into itemList not yet implemented");
            },
          });

          const updatedThisBlockContent = [
            ...contentBeforeLiteral,
            ...prependedLiteral,
            ...rest,
            ...contentAfterLiteral,
          ];

          thisBlock.content = updatedThisBlockContent;
        }
      } else if (appendingToMiddleOfLiteral) {
        console.log("appending to middle of literal");
        const textBeforeOffset = (literal.editedText ?? literal.text).slice(0, offset);
        const textAfterOffset = (literal.editedText ?? literal.text).slice(offset);

        if (parsedHtmlToBrevbaker.length < 0) {
          console.log("parsedLength is < 0. Ingenting å legge til");
          return;
        } else if (parsedHtmlToBrevbaker.length === 1) {
          console.log("parsedLength is 1");

          const newThisLiteral = newLiteral({
            ...literal,
            text: textBeforeOffset + (parsedHtmlToBrevbaker[0].content[0] as LiteralValue).text + textAfterOffset,
          });
          const newContent = [...contentBeforeLiteral, newThisLiteral, ...contentAfterLiteral];
          thisBlock.content = newContent;
        } else {
          console.log("parsedLength is > 1");

          const newThisLiteral = newLiteral({
            ...literal,
            text: textBeforeOffset + (parsedHtmlToBrevbaker[0].content[0] as LiteralValue).text,
          });
          const newContent = [...contentBeforeLiteral, newThisLiteral, ...contentAfterLiteral];

          const newThisBlockContent = newContent;
          const newThisBlock = newParagraph({ ...thisBlock, content: newThisBlockContent });
          const newBlocksAfterThisBlock = parsedHtmlToBrevbaker.slice(1);

          const lastElementInNewBlocks = newBlocksAfterThisBlock.at(-1)?.content.at(-1);

          if (!lastElementInNewBlocks) {
            //TODO
            throw new Error("last element is undefined");
          }

          const appendedLastElementInNewBlocks = handleSwitchContent({
            content: lastElementInNewBlocks,
            onLiteral: (literal) => {
              return newLiteral({
                ...literal,
                text: (literal.editedText ?? literal.text) + textAfterOffset,
              });
            },
            onVariable: () => {
              throw new Error("Cannot paste into variable");
            },
            onItemList: () => {
              throw new Error("paste into itemList not yet implemented");
            },
            onNewLine: () => {
              throw new Error("paste into itemList not yet implemented");
            },
          });

          const newAppendedElementBlocksAfterThisBlock = [
            ...newBlocksAfterThisBlock.slice(0, -1),
            newParagraph({
              ...lastElementInNewBlocks,
              content: [...newBlocksAfterThisBlock.at(-1)!.content.slice(0, -1), appendedLastElementInNewBlocks],
            }),
          ];

          // console.log("------newBlocksAfterThisBlock--------");
          // console.dir(JSON.parse(JSON.stringify(newBlocksAfterThisBlock)), { depth: null });
          // console.log("-----------------");
          const newBlocks = [
            ...blocksBeforeThisBlock,
            newThisBlock,
            ...newAppendedElementBlocksAfterThisBlock,
            ...blocksAfterThisBlock,
          ];

          draft.redigertBrev.blocks = newBlocks;
        }
      } else {
        console.log("appending to end of literal");

        if (parsedHtmlToBrevbaker.length < 0) {
          console.log("parsedLength is < 0");
          return;
        } else if (parsedHtmlToBrevbaker.length === 1) {
          console.log("parsedLength is 1");

          const theCopiedElement = parsedHtmlToBrevbaker[0].content[0];

          handleSwitchContent({
            content: theCopiedElement,
            onLiteral: (literal) => {
              const newThisLiteral = newLiteral({
                ...literal,
                text: literal + (parsedHtmlToBrevbaker[0].content[0] as LiteralValue).text,
              });
              const newContent = [...contentBeforeLiteral, newThisLiteral, ...contentAfterLiteral];
              thisBlock.content = newContent;
            },
            onVariable: () => {
              throw new Error("Cannot paste into variable");
            },
            onItemList: (itemList) => {
              console.log("itemList:", itemList);
              const firstItem = itemList.items[0];
              const rest = itemList.items.slice(1);
              console.log(firstItem);
              const newThisLiteral = newLiteral({
                ...literal,
                text: (literal.editedText ?? literal.text) + (firstItem.content[0] as LiteralValue).text,
              });

              const newContent = [
                ...contentBeforeLiteral,
                newThisLiteral,
                newItemList({ items: rest }),
                ...contentAfterLiteral,
              ];
              thisBlock.content = newContent;
            },
            onNewLine: () => {
              throw new Error("paste into itemList not yet implemented");
            },
          });
        } else {
          console.log("parsedLength is > 1");

          const newThisLiteral = newLiteral({
            ...literal,
            text: (literal.editedText ?? literal.text) + (parsedHtmlToBrevbaker[0].content[0] as LiteralValue).text,
          });
          const newContent = [...contentBeforeLiteral, newThisLiteral, ...contentAfterLiteral];

          const newThisBlockContent = newContent;
          const newThisBlock = newParagraph({ ...thisBlock, content: newThisBlockContent });
          const newBlocksAfterThisBlock = parsedHtmlToBrevbaker.slice(1);
          // console.log("------newBlocksAfterThisBlock--------");
          // console.dir(JSON.parse(JSON.stringify(newBlocksAfterThisBlock)), { depth: null });
          // console.log("-----------------");
          const newBlocks = [
            ...blocksBeforeThisBlock,
            newThisBlock,
            ...newBlocksAfterThisBlock,
            ...blocksAfterThisBlock,
          ];

          draft.redigertBrev.blocks = newBlocks;
        }
      }
    },
    onVariable: () => {
      throw new Error("Cannot paste into variable");
    },
    onItemList: () => {
      throw new Error("paste into itemList not yet implemented");
    },
    onNewLine: () => {
      throw new Error("paste into itemList not yet implemented");
    },
  });
}

interface TraversedElement {
  tag: "P" | "LI" | "SPAN";
  content: (TraversedElement | string)[];
}

const trav = (el: Element): TraversedElement[] | TraversedElement => {
  switch (el.tagName) {
    case "LI": {
      if (el.children.length > 0) {
        const content = [...el.children].flatMap(trav).filter((el) => el.content !== null);

        const mapped = content.flatMap((el) => {
          if (typeof el === "string") {
            return el;
          }

          return el.content;
        });

        return { tag: "LI", content: mapped };
      }

      return { tag: "LI", content: el.textContent?.trim() ? [el.textContent.trim()] : [] };
    }
    case "P": {
      if (el.children.length > 0) {
        const content = [...el.children].flatMap(trav).flatMap((el) => el.content);

        //dersom content er tom, betyr det at den bar inneholdt en tom span, som skal være linjeskift
        return { tag: "P", content: content.length > 0 ? content : [" "] };
      }

      //p elementet skal forsåvidt inneholde andre elementer, men dersom den ikke gjør det og kun inneholder tekst, skal den returneres som en string
      return { tag: "P", content: el.textContent?.trim() ? [el.textContent.trim()] : [] };
    }

    case "SPAN": {
      if (el.children.length > 0) {
        const content = [...el.children].flatMap(trav).flatMap((el) => el.content);
        return { tag: "SPAN", content: content };
      }
      const isEmptyOrNonExisting = el.textContent?.trim() === "" || !el.textContent;
      return { tag: "SPAN", content: isEmptyOrNonExisting ? [] : [el.textContent!.trim()] };
    }

    case "DIV": {
      return [...el.children].flatMap(trav);
    }
    default: {
      return [...el.children].flatMap(trav);
    }
  }
};

function mergeNeighboringTags(blocks: TraversedElement[]): TraversedElement[] {
  return blocks.reduce<TraversedElement[]>((acc, curr) => {
    const last = acc.at(-1);

    if (last && last.tag === curr.tag && (curr.tag === "LI" || curr.tag === "SPAN")) {
      last.content = [...last.content, ...curr.content];
    } else {
      acc.push({ ...curr });
    }

    return acc;
  }, []);
}

const mapCmbIlToBrevbakerTypes = (cmbIL: TraversedElement[]) => {
  return cmbIL.reduce<ParagraphBlock[]>((acc, curr) => {
    const lastParagraph = acc.at(-1) ?? null;
    const isLastParagraphLineBreak =
      lastParagraph?.content.length === 1 &&
      isLiteral(lastParagraph.content[0]) &&
      lastParagraph.content[0].text === " ";

    switch (curr.tag) {
      case "LI": {
        const itemList = newItemList({
          items: curr.content.map((item) => {
            //vi forventer at ItemLists sine items er kun ren tekst strenger
            if (typeof item !== "string") {
              throw new TypeError("item is not a string");
            }
            return newItem({ content: [newLiteral({ text: item })] });
          }),
        });

        if (lastParagraph && !isLastParagraphLineBreak) {
          lastParagraph.content.push(itemList);
        } else {
          acc.push(newParagraph({ content: [itemList] }));
        }
        break;
      }

      case "P": {
        acc.push(newParagraph({ content: [newLiteral({ text: curr.content.join(" ") })] }));
        break;
      }

      case "SPAN": {
        const text = curr.content.length > 1 ? curr.content.join(" ") : ` ${curr.content[0]}`;
        acc.push(newParagraph({ content: [newLiteral({ text })] }));
        break;
      }
    }
    return acc;
  }, []);
};

function parseHTMLToStructure(bodyElement: HTMLElement) {
  const result = [...bodyElement.children].flatMap(trav);
  console.log("-----result--------");
  console.dir(result, { depth: null });
  console.log("-----------------");
  const combined = mergeNeighboringTags(result);
  console.log("-----combined--------");
  console.dir(combined, { depth: null });
  console.log("-----------------");
  // console.log("-----mapped---------");
  const mapped = mapCmbIlToBrevbakerTypes(combined);
  // console.dir(mapped, { depth: null });

  return mapped;
}

function log(message: string) {
  console.log("Skribenten:pasteHandler: " + message);
}
