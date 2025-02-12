import type { Draft } from "immer";
import { produce } from "immer";
import { isEqual } from "lodash";

import { newItem, newItemList, newLiteral, newParagraph, text } from "~/Brevredigering/LetterEditor/actions/common";
import type { LiteralIndex } from "~/Brevredigering/LetterEditor/actions/model";
import { updateLiteralText } from "~/Brevredigering/LetterEditor/actions/updateContentText";
import type { Action } from "~/Brevredigering/LetterEditor/lib/actions";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import type { AnyBlock, Content, ItemList, LiteralValue, ParagraphBlock } from "~/types/brevbakerTypes";
import { LITERAL } from "~/types/brevbakerTypes";
import { ITEM_LIST } from "~/types/brevbakerTypes";
import { handleSwitchContent, isItemContentIndex } from "~/utils/brevbakerUtils";

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

const pasteIntoLiteral = (
  draft: Draft<LetterEditorState>,
  literalIndex: LiteralIndex,
  blocksBeforeThisBlock: AnyBlock[],
  thisBlock: Draft<AnyBlock>,
  blocksAfterThisBlock: AnyBlock[],
  contentBeforeLiteral: Content[],
  contentAfterLiteral: Content[],
  offset: number,
  literalToBePastedInto: LiteralValue,
  parsedAndCombinedHtml: TraversedElement[],
) => {
  const appendingToStartOfLiteral = offset === 0;
  const appendingToMiddleOfLiteral =
    offset > 0 && offset < (literalToBePastedInto.editedText?.length ?? literalToBePastedInto.text.length);

  if (appendingToStartOfLiteral) {
    const { newBlocks, updatedFocus } = insertTextAtStartOfLiteral(
      draft,
      literalIndex,
      blocksBeforeThisBlock,
      thisBlock,
      blocksAfterThisBlock,
      contentBeforeLiteral,
      contentAfterLiteral,
      literalToBePastedInto,
      parsedAndCombinedHtml,
    );

    draft.redigertBrev.blocks = newBlocks;
    draft.focus = updatedFocus;
  } else if (appendingToMiddleOfLiteral) {
    const { newBlocks, newFocus } = insertTextInTheMiddleOfLiteral(
      draft,
      blocksBeforeThisBlock,
      thisBlock,
      blocksAfterThisBlock,
      contentBeforeLiteral,
      contentAfterLiteral,
      literalToBePastedInto,
      parsedAndCombinedHtml,
      offset,
    );

    draft.redigertBrev.blocks = newBlocks;
    draft.focus = newFocus;
  } else {
    const { newBlocks, newFocus } = insertTextAtEndOfLiteral(
      draft,
      blocksBeforeThisBlock,
      thisBlock,
      blocksAfterThisBlock,
      contentBeforeLiteral,
      contentAfterLiteral,
      literalToBePastedInto,
      parsedAndCombinedHtml,
    );

    draft.redigertBrev.blocks = newBlocks;
    draft.focus = newFocus;
  }
};

const insertTextAtStartOfLiteral = (
  draft: Draft<LetterEditorState>,
  literalIndex: LiteralIndex,
  blocksBeforeThisBlock: AnyBlock[],
  thisBlock: Draft<AnyBlock>,
  blocksAfterThisBlock: AnyBlock[],
  contentBeforeLiteral: Content[],
  contentAfterLiteral: Content[],
  literalToBePastedInto: LiteralValue,
  parsedAndCombinedHtml: TraversedElement[],
) => {
  const firstCombinedElement = parsedAndCombinedHtml[0];

  if (firstCombinedElement.tag === "SPAN") {
    const combinedSpanText = firstCombinedElement.content.join(" ");

    const newLiteralToBePastedIn = newLiteral({
      ...literalToBePastedInto,
      text: combinedSpanText + (literalToBePastedInto.editedText ?? literalToBePastedInto.text),
    });

    const newContent = [...contentBeforeLiteral, newLiteralToBePastedIn, ...contentAfterLiteral];
    const newThisBlock = newParagraph({
      content: newContent,
    });

    const newBlocks = [...blocksBeforeThisBlock, newThisBlock, ...blocksAfterThisBlock];
    const updatedFocus = {
      ...draft.focus,
      cursorPosition: combinedSpanText.length,
    };

    return { newBlocks, updatedFocus };
  } else {
    const newBeforeBlock = newParagraph({ content: contentBeforeLiteral });
    const newThisBlock = traversedElementsToBrevbaker(parsedAndCombinedHtml);
    const newNextBlock = newParagraph({ content: [literalToBePastedInto] });
    const newContentAfterLiteralBlock = newParagraph({ content: contentAfterLiteral });

    const newBlocks = [
      ...blocksBeforeThisBlock,
      newBeforeBlock.content.length > 0 ? newBeforeBlock : [],
      ...newThisBlock,
      newNextBlock,
      newContentAfterLiteralBlock.content.length > 0 ? newContentAfterLiteralBlock : [],
      ...blocksAfterThisBlock,
    ].flat();

    const newBlockPosition = newBlocks.findIndex((b) => isEqual(b, newNextBlock));
    const newContentPosition = Math.max(newNextBlock.content.length - 1, 0);

    const lastEl = newNextBlock.content.at(-1);
    const newCursorPosition =
      //TODO - eslint og prettier clasher litt med syntax preferanse. Burde uansett gjøre denne cleanere
      /* eslint-disable prettier/prettier */
        (lastEl && lastEl.type === "ITEM_LIST"
          ? lastEl.items.at(-1)?.content.at(-1)?.text.length
          : (lastEl && "editedText" in lastEl
            ? lastEl.editedText?.length
            : lastEl?.text.length)) ?? 0;
      /* eslint-enable prettier/prettier */

    draft.redigertBrev.blocks = newBlocks;
    const updatedFocus = {
      blockIndex: newBlockPosition,
      contentIndex: newContentPosition,
      cursorPosition: newCursorPosition,
    };

    return { newBlocks, updatedFocus };
  }
};

const insertTextInTheMiddleOfLiteral = (
  draft: Draft<LetterEditorState>,
  blocksBeforeThisBlock: AnyBlock[],
  thisBlock: Draft<AnyBlock>,
  blocksAfterThisBlock: AnyBlock[],
  contentBeforeLiteral: Content[],
  contentAfterLiteral: Content[],
  literalToBePastedInto: LiteralValue,
  parsedAndCombinedHtml: TraversedElement[],
  offset: number,
) => {
  const firstCombinedElement = parsedAndCombinedHtml[0];
  const textBeforeOffset = (literalToBePastedInto.editedText ?? literalToBePastedInto.text).slice(0, offset);
  const textAfterOffset = (literalToBePastedInto.editedText ?? literalToBePastedInto.text).slice(offset);

  if (firstCombinedElement.tag === "SPAN") {
    const combinedSpanText = firstCombinedElement.content.join(" ");
    const newLiteralToBePastedIn = newLiteral({
      ...literalToBePastedInto,
      text:
        textBeforeOffset +
        (firstCombinedElement.content.length > 1 ? combinedSpanText : ` ${firstCombinedElement.content[0]}`) +
        textAfterOffset,
    });

    const newContent = [...contentBeforeLiteral, newLiteralToBePastedIn, ...contentAfterLiteral];
    const newBlock = newParagraph({ content: [contentBeforeLiteral, newContent, contentAfterLiteral].flat() });
    const cursorPosition =
      textBeforeOffset +
      (firstCombinedElement.content.length > 1 ? combinedSpanText : ` ${firstCombinedElement.content[0]}`);
    thisBlock.content = newContent;

    const newFocus = {
      ...draft.focus,
      cursorPosition: cursorPosition.length,
    };

    const newBlocks = [blocksBeforeThisBlock, newBlock, blocksAfterThisBlock].flat();

    return { newBlocks: newBlocks, newFocus };
  } else {
    const newBeforeBlock = newParagraph({ content: contentBeforeLiteral });
    const mappedToBrevbaker = traversedElementsToBrevbaker(parsedAndCombinedHtml);
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
    const restAfterBlock = newParagraph({ content: [newLiteral({ text: textAfterOffset })] });

    const newBlocks = [
      ...blocksBeforeThisBlock,
      newBeforeBlock.content.length > 0 ? newBeforeBlock : [],
      newThisBlock,
      ...newBlocksAfterThisBlock,
      restAfterBlock,
      newContentAfterLiteralBlock.content.length > 0 ? newContentAfterLiteralBlock : [],
      ...blocksAfterThisBlock,
    ].flat();

    const newBlockPosition = newBlocks.findIndex((b) => isEqual(b, restAfterBlock));
    const newContentPosition = Math.max((newBlocksAfterThisBlock.at(-1)?.content?.length ?? 0) - 1, 0);

    const newFocus = {
      blockIndex: newBlockPosition,
      contentIndex: newContentPosition,
      cursorPosition: 0,
    };

    return { newBlocks, newFocus };
  }
};

const insertTextAtEndOfLiteral = (
  draft: Draft<LetterEditorState>,
  blocksBeforeThisBlock: AnyBlock[],
  thisBlock: Draft<AnyBlock>,
  blocksAfterThisBlock: AnyBlock[],
  contentBeforeLiteral: Content[],
  contentAfterLiteral: Content[],
  literalToBePastedInto: LiteralValue,
  parsedAndCombinedHtml: TraversedElement[],
) => {
  const firstCombinedElement = parsedAndCombinedHtml[0];
  if (firstCombinedElement.tag === "SPAN") {
    const combinedSpanText = firstCombinedElement.content.join(" ");
    const newLiteralToBePastedIn = newLiteral({
      ...literalToBePastedInto,
      text: (literalToBePastedInto.editedText ?? literalToBePastedInto.text) + combinedSpanText,
    });
    const newCursorPosition = ((literalToBePastedInto.editedText ?? literalToBePastedInto.text) + combinedSpanText)
      .length;

    const newContent = [...contentBeforeLiteral, newLiteralToBePastedIn, ...contentAfterLiteral];
    const newThisBlock = newParagraph({
      content: [contentBeforeLiteral, newContent, contentAfterLiteral].flat(),
    });

    const newBlocks = [...blocksBeforeThisBlock, newThisBlock, ...blocksAfterThisBlock];

    const newFocus = {
      ...draft.focus,
      cursorPosition: newCursorPosition,
    };

    return { newBlocks, newFocus };
  } else {
    const mappedToBrevbaker = traversedElementsToBrevbaker(parsedAndCombinedHtml);
    const firstMapped = mappedToBrevbaker[0];
    const restMapped = mappedToBrevbaker.slice(1);

    const theNewLiteral =
      firstMapped.content[0].type === "LITERAL"
        ? newLiteral({
            ...literalToBePastedInto,
            text:
              (literalToBePastedInto.editedText ?? literalToBePastedInto.text) +
              (firstMapped.content[0] as LiteralValue).text,
          })
        : newLiteral({
            text:
              (literalToBePastedInto.editedText ?? literalToBePastedInto.text) +
              (firstMapped.content[0] as ItemList).items[0].content.map((c) => c.text).join(" "),
          });

    const newLiteralContent =
      firstMapped.content[0].type === "LITERAL"
        ? [theNewLiteral, ...(firstMapped.content.length > 1 ? firstMapped.content.slice(1) : [])]
        : [
            newItemList({
              items: [
                newItem({
                  content: [theNewLiteral],
                }),
                ...(firstMapped.content[0] as ItemList).items.slice(1),
              ],
            }),
          ];

    const newThisBlock = newParagraph({
      content: [contentBeforeLiteral, newLiteralContent].flat(),
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

    const newBlockPosition = newBlocks.findIndex((b) =>
      isEqual(b, newBlocksAfterThisBlock.length > 0 ? newBlocksAfterThisBlock.at(-1) : newThisBlock),
    );
    const newContentPosition = Math.max(
      (newBlocksAfterThisBlock.length > 0
        ? (newBlocksAfterThisBlock.at(-1)?.content.length ?? 0)
        : (newThisBlock.content.length ?? 0)) - 1,
      0,
    );

    const blockToGetCursorPositionFrom =
      newBlocksAfterThisBlock.length > 0 ? (newBlocksAfterThisBlock.at(-1) ?? newThisBlock) : newThisBlock;
    const lastContent = blockToGetCursorPositionFrom.content.at(-1) ?? newThisBlock.content.at(-1);

    const newCursorPosition =
      lastContent?.type === "LITERAL"
        ? lastContent.text.length
        : ((lastContent && (lastContent as ItemList).items.at(-1)?.content.at(-1)?.text.length) ?? 0);

    const itemContentIndex = lastContent?.type === "ITEM_LIST" ? 0 : undefined;
    const itemIndex = lastContent?.type === "ITEM_LIST" ? lastContent.items.length - 1 : undefined;

    const newFocus = {
      blockIndex: newBlockPosition,
      contentIndex: newContentPosition,
      itemContentIndex: itemContentIndex,
      itemIndex: itemIndex,
      cursorPosition: newCursorPosition,
    };

    return { newBlocks, newFocus };
  }
};

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
      pasteIntoLiteral(
        draft,
        literalIndex,
        blocksBeforeThisBlock,
        thisBlock,
        blocksAfterThisBlock,
        contentBeforeLiteral,
        contentAfterLiteral,
        offset,
        literalToBePastedInto,
        parsedAndCombinedHtml,
      );
    },
    onVariable: () => {
      throw new Error("Cannot paste into variable");
    },
    onItemList: (itemList) => {
      if (!isItemContentIndex(literalIndex)) {
        return;
      }

      const item = itemList.items[literalIndex.itemIndex];
      const itemContent = item.content[literalIndex.itemContentIndex];

      handleSwitchContent({
        content: itemContent,
        onLiteral: (literalToBePastedInto) => {
          pasteIntoLiteral(
            draft,
            literalIndex,
            blocksBeforeThisBlock,
            thisBlock,
            blocksAfterThisBlock,
            contentBeforeLiteral,
            contentAfterLiteral,
            offset,
            literalToBePastedInto,
            parsedAndCombinedHtml,
          );
        },
        onVariable: () => {
          throw new Error("Cannot paste into variable");
        },
        onItemList: () => {
          throw new Error("Cannot paste into itemList");
        },
        onNewLine: () => {
          throw new Error("Cannot paste into newline");
        },
      });
    },
    onNewLine: () => {
      //TODO - newLine er en spesiell type content. Skal vi støtte paste inn i newLine?
      throw new Error("Cannot paste into newline");
    },
  });
}

interface TraversedElement {
  tag: "P" | "LI" | "SPAN";
  content: (TraversedElement | string)[];
}

const traverseElement = (el: Element): TraversedElement[] | TraversedElement => {
  switch (el.tagName) {
    case "LI": {
      if (el.children.length > 0) {
        const content = [...el.children].flatMap(traverseElement).filter((el) => el.content !== null);

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
        const content = [...el.children].flatMap(traverseElement).flatMap((el) => el.content);

        //dersom content er tom, betyr det at den bar inneholdt en tom span, som skal være linjeskift
        return { tag: "P", content: content.length > 0 ? content : [" "] };
      }

      //p elementet skal forsåvidt inneholde andre elementer, men dersom den ikke gjør det og kun inneholder tekst, skal den returneres som en string
      return { tag: "P", content: el.textContent?.trim() ? [el.textContent.trim()] : [] };
    }

    case "SPAN": {
      if (el.children.length > 0) {
        const content = [...el.children].flatMap(traverseElement).flatMap((el) => el.content);
        return { tag: "SPAN", content: content };
      }
      const isEmptyOrNonExisting = el.textContent?.trim() === "" || !el.textContent;
      return { tag: "SPAN", content: isEmptyOrNonExisting ? [] : [el.textContent!.trim()] };
    }

    case "DIV": {
      return [...el.children].flatMap(traverseElement);
    }
    default: {
      return [...el.children].flatMap(traverseElement);
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

const traversedElementToBrevbaker = (t: TraversedElement) => {
  switch (t.tag) {
    case "LI": {
      return newItemList({
        items: t.content.map((item) => {
          //vi forventer at ItemLists sine items er kun rene tekst strenger
          if (typeof item !== "string") {
            throw new TypeError("item is not a string");
          }
          return newItem({ content: [newLiteral({ text: item })] });
        }),
      });
    }
    case "P": {
      return newParagraph({ content: [newLiteral({ text: t.content.join(" ") })] });
    }
    case "SPAN": {
      const text = t.content.length > 1 ? t.content.join(" ") : ` ${t.content[0]}`;
      return newParagraph({ content: [newLiteral({ text })] });
    }
  }
};

const traversedElementsToBrevbaker = (t: TraversedElement[]) =>
  t.reduce<ParagraphBlock[]>((acc, curr) => {
    const lastParagraph = acc.at(-1) ?? null;
    const isLastParagraphLineBreak =
      lastParagraph?.content.length === 1 &&
      isLiteral(lastParagraph.content[0]) &&
      lastParagraph.content[0].text === " ";

    switch (curr.tag) {
      case "LI": {
        const itemList = traversedElementToBrevbaker(curr) as ItemList;

        if (lastParagraph && !isLastParagraphLineBreak) {
          lastParagraph.content.push(itemList);
        } else {
          acc.push(newParagraph({ content: [itemList] }));
        }
        break;
      }

      case "P": {
        acc.push(traversedElementToBrevbaker(curr) as ParagraphBlock);
        break;
      }

      case "SPAN": {
        acc.push(traversedElementToBrevbaker(curr) as ParagraphBlock);
        break;
      }
    }
    return acc;
  }, []);

const parseAndCombineHTML = (bodyElement: HTMLElement) => {
  const result = [...bodyElement.children].flatMap(traverseElement);
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
