import type { Draft } from "immer";
import { produce } from "immer";

import { newItem, newItemList, newLiteral, text } from "~/Brevredigering/LetterEditor/actions/common";
import type { LiteralIndex } from "~/Brevredigering/LetterEditor/actions/model";
import { updateLiteralText } from "~/Brevredigering/LetterEditor/actions/updateContentText";
import type { Action } from "~/Brevredigering/LetterEditor/lib/actions";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import type { Content, Item, ItemList, LiteralValue } from "~/types/brevbakerTypes";
import { LITERAL } from "~/types/brevbakerTypes";
import { ITEM_LIST } from "~/types/brevbakerTypes";
import { handleSwitchContent } from "~/utils/brevbakerUtils";

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

type TextPath = { textContent: string; path: string };

//TODO - test
function helper(el: Element, currentPath: string): TextPath[] {
  const children = [...el.children];
  if (children.length === 0) {
    // Leaf element: return its text (if any)
    const text = el.textContent;
    return text ? [{ textContent: text, path: currentPath }] : [];
  } else if (children.length === 1) {
    // Only one child → flatten the branch (drill down)
    const child = children[0];
    const childPath = `${currentPath}.${child.tagName.toLowerCase()}`;
    return helper(child, childPath);
  } else {
    // More than one child: process each child separately.
    const results: TextPath[] = [];
    for (const child of children) {
      const childPath = `${currentPath}.${child.tagName.toLowerCase()}`;
      const childCandidates = helper(child, childPath);
      if (childCandidates.length > 0) {
        results.push(...childCandidates);
      } else {
        // If no candidate was found in this branch, use the child's own text.
        const text = child.textContent;
        if (text) {
          results.push({ textContent: text, path: childPath });
        }
      }
    }
    return results;
  }
}

function extractTextPathsFromOuter(outer: Element): TextPath[] {
  const results: TextPath[] = [];
  // Process each direct child of the outer element.
  for (const child of outer.children) {
    // Build the initial path as: outerTag.childTag (both lower‑case)
    const initialPath = `${outer.tagName.toLowerCase()}.${child.tagName.toLowerCase()}`;
    // Get the candidate(s) from this branch.
    const candidates = helper(child, initialPath);
    if (candidates.length > 0) {
      results.push(...candidates);
    } else {
      // If no candidate was found, fallback to the child's own text.
      const text = child.textContent;
      if (text) {
        results.push({ textContent: text, path: initialPath });
      }
    }
  }
  return results;
}

//TODO - test
function extractTextWithDeepestPath(documentBody: HTMLElement): TextPath[] {
  return extractTextPathsFromOuter(documentBody);
}

/**
 * eksempel:
 *
 * Si at brevet inneholder, og at vi har kopiert samme innhold:
 *  Punktliste
 *     - Første punkt
 *     - Andre punkt
 *
 * Dersom vi limer inn før 'P'en 'punktliste', skal resultatet bli duplisert. Altså at vi skal ha to punktlister etter hverandre
 *
 * Dersom vi limer inn på midten av 'punktliste', skal resultatet bli:
 *    Punkt Punktliste
 *       - Første punkt
 *       - Andre punkt
 *     liste
 *     - Første punkt
 *     - Andre punkt
 *
 * Dersom vi limer inn på slutten av 'punktliste', skal resultatet bli:
 *    Punktliste Punktliste
 *       - Første punkt
 *       - Andre punkt
 *
 *     - Første punkt
 *     - Andre punkt
 *
 * Merk at mellomrommet kommer fra at når man kopierer fra Word, legger den til et ekstra span element
 *
 * Dersom vi limer inn på starten av et punkt i punktlisten, skal resultatet bli:
 *  Punktliste
 *    - punktliste
 *    - Første punkt
 *    - Andre punkt
 *    - Første punkt
 *    - Andre punkt
 *
 * Dersom vi limer inn på midten av et punkt i en punktliste, skal resultatet bli:
 * Punktliste
 *    - Første punktliste
 *    - første punkt
 *    - andre punkt
 *    - punkt
 *    - andre punkt
 *
 * Dersom vi limer inn på slutten av et punkt i en punktliste, skal resultatet bli:
 * Punktliste
 *   - Første punkt punktliste
 *   - første punkt
 *   - andre punkt
 *   -
 *   - andre punkt
 *
 * * Merk at mellomrommet kommer fra at når man kopierer fra Word, legger den til et ekstra span element
 */
const appendToCurrentLiteral = (
  currentBlockContent: Content[],
  literalIndex: LiteralIndex,
  literal: LiteralValue,
  extracted: TextPath[],
  offset: number,
) => {
  const appendingToStartOfLiteral = offset === 0;
  const appendingToMiddleOfLiteral = offset > 0 && offset < (literal.editedText?.length ?? literal.text.length);

  console.log("extracted:", extracted);
  const mappedToBrevbakerTypes = extracted.map((textPath) => {
    return textPath.path.includes("li")
      ? newItem({ content: [newLiteral({ text: textPath.textContent })] })
      : newLiteral({ text: textPath.textContent });
  });

  console.log("---------mappedToBrevbakerTypes-----------");
  console.dir(JSON.parse(JSON.stringify(mappedToBrevbakerTypes)), { depth: null });
  console.log("----------------------------");

  const combined = combineNeighbouringItemLists(mappedToBrevbakerTypes);

  console.log("---------combined-----------");
  console.dir(JSON.parse(JSON.stringify(combined)), { depth: null });
  console.log("----------------------------");

  const isFirstCombinedElementLiteral = combined[0].type === LITERAL;

  const doesCombinedContainListElements = combined.some((item) => item.type === ITEM_LIST);

  if (appendingToStartOfLiteral) {
    console.log("start of literal");
    const newContent = [...combined, ...currentBlockContent];
    console.log("the new content:", JSON.parse(JSON.stringify(newContent)));
    return newContent;
  } else if (appendingToMiddleOfLiteral) {
    console.log("middle of literal");
    const startOfSplitString = (literal.editedText ?? literal.text).slice(0, offset);
    const endOfSplitString = (literal.editedText ?? literal.text).slice(offset);

    console.log("startOfSplitString:", startOfSplitString);
    console.log("endOfSplitString:", endOfSplitString);

    if (doesCombinedContainListElements) {
      const newContent = [
        ...currentBlockContent.slice(0, literalIndex.contentIndex),
        newLiteral({ text: startOfSplitString + (combined[0] as LiteralValue).text }),
        ...combined.slice(1),
        newLiteral({ text: endOfSplitString }),
        ...currentBlockContent.slice(literalIndex.contentIndex + 1),
      ];

      console.log("newContent:", JSON.parse(JSON.stringify(newContent)));

      return newContent;
    } else {
      console.log("combined contains only literals");

      const firstCombined = combined.at(0) as LiteralValue;
      const lastCombined = combined.length > 1 ? (combined.at(-1) as LiteralValue) : null;
      const betweenCombined = combined.slice(1, -1) as LiteralValue[];

      console.log("firstCombined:", firstCombined);
      console.log("lastCombined:", lastCombined);
      console.log("betweenCombined:", betweenCombined);

      const newContent = [
        ...currentBlockContent.slice(0, literalIndex.contentIndex),
        newLiteral({
          text: startOfSplitString + firstCombined.text + (combined.length > 1 ? "" : endOfSplitString),
        }),
        ...betweenCombined,
        ...(lastCombined ? [newLiteral({ text: lastCombined.text + endOfSplitString })] : []),
        ...currentBlockContent.slice(literalIndex.contentIndex + 1),
      ];

      console.log("newContent:", JSON.parse(JSON.stringify(newContent)));

      return newContent;
    }
  } else {
    console.log("end of literal");

    const theTextWeWant = isFirstCombinedElementLiteral
      ? (combined[0] as LiteralValue).text
      : (combined[0] as ItemList).items[0].content[0].text;

    const builtText = literal.text + theTextWeWant;

    const newContent = [
      ...currentBlockContent.slice(0, literalIndex.contentIndex),
      newLiteral({ text: builtText }),
      ...(isFirstCombinedElementLiteral
        ? combined.slice(1)
        : [newItemList({ items: (combined[0] as ItemList).items.slice(1) })]),
      ...currentBlockContent.slice(literalIndex.contentIndex + 1),
    ];
    // console.log("the new content:", JSON.parse(JSON.stringify(newContent)));
    return newContent;
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

  const thisBlock = draft.redigertBrev.blocks[literalIndex.blockIndex];
  const blockContent = thisBlock.content;
  const current = blockContent[literalIndex.contentIndex];
  const extracted = extractTextWithDeepestPath(document.body);
  // console.log("current:", JSON.parse(JSON.stringify(current)));

  handleSwitchContent({
    content: current,
    onLiteral: (literal) => {
      //literalen som vi er på, er tom
      if ((literal.editedText?.trim()?.length ?? literal.text?.trim()?.length ?? 0) === 0) {
        /**
         * så vil vi appende alt innholdet fra clipboard, akkurat som strukturen i clipboard er
         */
        // console.log("literalen er tom");

        const mappedToBrevbakerTypes = extracted.map((textPath) => {
          return textPath.path.includes("li")
            ? newItem({ content: [newLiteral({ text: textPath.textContent })] })
            : newLiteral({ text: textPath.textContent });
        });

        const combined = combineNeighbouringItemLists(mappedToBrevbakerTypes);
        thisBlock.content = combined;
      } else {
        /**
         * appender det første elementet i clipboard til den nåværende literalen,
         * og resten av elementene i clipboard vil bli lagt til som nye elementer i brevet
         */
        // console.log("literalen er ikke tom");
        const appended = appendToCurrentLiteral(blockContent, literalIndex, literal, extracted, offset);
        // console.log(appended);
        thisBlock.content = appended!;
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

//TODO - test
const combineNeighbouringItemLists = (arr: (LiteralValue | Item)[]): (LiteralValue | ItemList)[] => {
  const temp: (LiteralValue | ItemList)[] = [];
  let bufferItemListContent: Item[] = [];

  for (const current of arr) {
    if ("type" in current && current.type === "LITERAL") {
      if (bufferItemListContent.length > 0) {
        temp.push(newItemList({ items: bufferItemListContent }));
      }
      temp.push(current);
      bufferItemListContent = [];
    } else {
      bufferItemListContent.push(current as Item);
    }
  }

  if (bufferItemListContent.length > 0) {
    temp.push(newItemList({ items: bufferItemListContent }));
  }

  return temp;
};

function log(message: string) {
  console.log("Skribenten:pasteHandler: " + message);
}
