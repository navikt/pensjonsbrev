import type { Draft } from "immer";
import { produce } from "immer";
import { first } from "lodash";

import { newItem, newItemList, newLiteral, newParagraph, text } from "~/Brevredigering/LetterEditor/actions/common";
import type { LiteralIndex } from "~/Brevredigering/LetterEditor/actions/model";
import { updateLiteralText } from "~/Brevredigering/LetterEditor/actions/updateContentText";
import type { Action } from "~/Brevredigering/LetterEditor/lib/actions";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import type { ItemList, LiteralValue, ParagraphBlock } from "~/types/brevbakerTypes";
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
  const parsedAndCombinedHtml = parseAndCombineHTML(document.body);

  if (parsedAndCombinedHtml.length === 0) {
    //trenger ikke å lime inn tomt innhold
    return;
  }

  const blocksBeforeThisBlock = draft.redigertBrev.blocks.slice(0, literalIndex.blockIndex);
  const thisBlock = draft.redigertBrev.blocks[literalIndex.blockIndex];
  const blocksAfterThisBlock = draft.redigertBrev.blocks.slice(literalIndex.blockIndex + 1);

  const contentBeforeLiteral = thisBlock.content.slice(0, literalIndex.contentIndex);
  const thisContent = thisBlock.content[literalIndex.contentIndex];
  const contentAfterLiteral = thisBlock.content.slice(literalIndex.contentIndex + 1);

  handleSwitchContent({
    content: thisContent,
    onLiteral: (literalToBePastedInto) => {
      const appendingToStartOfLiteral = offset === 0;
      const appendingToMiddleOfLiteral =
        offset > 0 && offset < (literalToBePastedInto.editedText?.length ?? literalToBePastedInto.text.length);
      const firstCombinedElement = parsedAndCombinedHtml[0];

      if (appendingToStartOfLiteral) {
        /**
         * hvis firstCombinedElement er span, så pluses dette sammen med literalToBePastedIn
         * hvis firstCombinedElement er p eller li, så lages det en ny paragraph med dette som første element, og literalToBePastedIn som en ny blokk
         */

        if (firstCombinedElement.tag === "SPAN") {
          const newLiteralToBePastedIn = newLiteral({
            ...literalToBePastedInto,
            text:
              firstCombinedElement.content.join(" ") + (literalToBePastedInto.editedText ?? literalToBePastedInto.text),
          });

          const newContent = [...contentBeforeLiteral, newLiteralToBePastedIn, ...contentAfterLiteral];
          thisBlock.content = newContent;
        } else {
          const newBeforeBlock = newParagraph({ content: contentBeforeLiteral });
          const newThisBlock = mapCmbIlToBrevbakerTypes(parsedAndCombinedHtml);
          const newContentAfterLiteralBlock = newParagraph({ content: contentAfterLiteral });
          const newBlocks = [
            ...blocksBeforeThisBlock,
            newBeforeBlock.content.length > 0 ? newBeforeBlock : [],
            ...newThisBlock,
            newParagraph({ content: [literalToBePastedInto] }),
            newContentAfterLiteralBlock.content.length > 0 ? newContentAfterLiteralBlock : [],
            ...blocksAfterThisBlock,
          ].flat();

          draft.redigertBrev.blocks = newBlocks;
        }
      } else if (appendingToMiddleOfLiteral) {
        const textBeforeOffset = (literalToBePastedInto.editedText ?? literalToBePastedInto.text).slice(0, offset);
        const textAfterOffset = (literalToBePastedInto.editedText ?? literalToBePastedInto.text).slice(offset);

        if (firstCombinedElement.tag === "SPAN") {
          const newLiteralToBePastedIn = newLiteral({
            ...literalToBePastedInto,
            text:
              textBeforeOffset +
              (firstCombinedElement.content.length > 1
                ? firstCombinedElement.content.join(" ")
                : ` ${firstCombinedElement.content[0]}`) +
              textAfterOffset,
          });

          const newContent = [...contentBeforeLiteral, newLiteralToBePastedIn, ...contentAfterLiteral];
          thisBlock.content = newContent;
        } else {
          const newBeforeBlock = newParagraph({ content: contentBeforeLiteral });
          const mappedToBrevbaker = mapCmbIlToBrevbakerTypes(parsedAndCombinedHtml);
          const firstMapped = mappedToBrevbaker[0];
          const restMapped = mappedToBrevbaker.slice(1);

          const newThisBlock = newParagraph({
            content: [
              firstMapped.content[0].type === "LITERAL"
                ? [
                    newLiteral({
                      ...literalToBePastedInto,
                      text: textBeforeOffset + (firstMapped.content[0] as LiteralValue).text,
                    }),
                    ...(firstMapped.content.length > 1 ? firstMapped.content.slice(1) : []),
                  ]
                : [
                    newItemList({
                      items: [
                        newItem({
                          content: [
                            newLiteral({
                              text:
                                textBeforeOffset +
                                (firstMapped.content[0] as ItemList).items[0].content.map((c) => c.text).join(" "),
                            }),
                          ],
                        }),
                        ...(firstMapped.content[0] as ItemList).items.slice(1),
                      ],
                    }),
                  ],
            ].flat(),
          });

          const newBlocksAfterThisBlock = restMapped;

          const newContentAfterLiteralBlock = newParagraph({ content: contentAfterLiteral });

          const newBlocks = [
            ...blocksBeforeThisBlock,
            newBeforeBlock.content.length > 0 ? newBeforeBlock : [],
            newThisBlock,
            newBlocksAfterThisBlock,
            newParagraph({ content: [newLiteral({ text: textAfterOffset })] }),
            newContentAfterLiteralBlock.content.length > 0 ? newContentAfterLiteralBlock : [],
            ...blocksAfterThisBlock,
          ].flat();

          draft.redigertBrev.blocks = newBlocks;
        }
      } else {
        if (firstCombinedElement.tag === "SPAN") {
          const newLiteralToBePastedIn = newLiteral({
            ...literalToBePastedInto,
            text:
              (literalToBePastedInto.editedText ?? literalToBePastedInto.text) + firstCombinedElement.content.join(" "),
          });

          const newContent = [...contentBeforeLiteral, newLiteralToBePastedIn, ...contentAfterLiteral];
          thisBlock.content = newContent;
        } else {
          const newBeforeBlock = newParagraph({ content: contentBeforeLiteral });
          const newThisBlock = mapCmbIlToBrevbakerTypes(parsedAndCombinedHtml);
          const newContentAfterLiteralBlock = newParagraph({ content: contentAfterLiteral });
          const newBlocks = [
            ...blocksBeforeThisBlock,
            newBeforeBlock.content.length > 0 ? newBeforeBlock : [],
            ...newThisBlock,
            newParagraph({ content: [literalToBePastedInto] }),
            newContentAfterLiteralBlock.content.length > 0 ? newContentAfterLiteralBlock : [],
            ...blocksAfterThisBlock,
          ].flat();

          draft.redigertBrev.blocks = newBlocks;
        }
        if (firstCombinedElement.tag === "SPAN") {
          const newLiteralToBePastedIn = newLiteral({
            ...literalToBePastedInto,
            text:
              (literalToBePastedInto.editedText ?? literalToBePastedInto.text) + firstCombinedElement.content.join(" "),
          });

          const newContent = [...contentBeforeLiteral, newLiteralToBePastedIn, ...contentAfterLiteral];
          thisBlock.content = newContent;
        } else {
          const mappedToBrevbaker = mapCmbIlToBrevbakerTypes(parsedAndCombinedHtml);
          const firstMapped = mappedToBrevbaker[0];
          const restMapped = mappedToBrevbaker.slice(1);

          const newThisBlock = newParagraph({
            content: [
              contentBeforeLiteral,
              firstMapped.content[0].type === "LITERAL"
                ? [
                    newLiteral({
                      ...literalToBePastedInto,
                      text:
                        (literalToBePastedInto.editedText ?? literalToBePastedInto.text) +
                        (firstMapped.content[0] as LiteralValue).text,
                    }),
                    ...(firstMapped.content.length > 1 ? firstMapped.content.slice(1) : []),
                  ]
                : [
                    newItemList({
                      items: [
                        newItem({
                          content: [
                            newLiteral({
                              text:
                                (literalToBePastedInto.editedText ?? literalToBePastedInto.text) +
                                (firstMapped.content[0] as ItemList).items[0].content.map((c) => c.text).join(" "),
                            }),
                          ],
                        }),
                        ...(firstMapped.content[0] as ItemList).items.slice(1),
                      ],
                    }),
                  ],
            ].flat(),
          });

          const newBlocksAfterThisBlock = restMapped;
          const newContentAfterLiteralBlock = newParagraph({ content: contentAfterLiteral });
          const newBlocks = [
            ...blocksBeforeThisBlock,
            newThisBlock,
            ...newBlocksAfterThisBlock,
            newContentAfterLiteralBlock.content.length > 0 ? newContentAfterLiteralBlock : [],
            ...blocksAfterThisBlock,
          ].flat();

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

const mapCmbIlToBrevbakerTypes = (cmbIL: TraversedElement[]) =>
  cmbIL.reduce<ParagraphBlock[]>((acc, curr) => {
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

const parseAndCombineHTML = (bodyElement: HTMLElement) => {
  const result = [...bodyElement.children].flatMap(trav);
  console.log("-----result--------");
  console.dir(result, { depth: null });
  console.log("-----------------");
  const combined = mergeNeighboringTags(result);
  console.log("-----combined--------");
  console.dir(combined, { depth: null });
  console.log("-----------------");
  return combined;
};

function log(message: string) {
  console.log("Skribenten:pasteHandler: " + message);
}
