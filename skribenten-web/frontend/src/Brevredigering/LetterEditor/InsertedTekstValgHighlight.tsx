import { createContext, type ReactNode, useContext } from "react";

import { type Focus } from "~/Brevredigering/LetterEditor/model/state";
import {
  type AnyBlock,
  type Cell,
  type Content,
  type EditedLetter,
  type Identifiable,
  ITEM_LIST,
  type Item,
  LITERAL,
  NEW_LINE,
  TABLE,
  type TextContent,
  VARIABLE,
} from "~/types/brevbakerTypes";

const EMPTY_HIGHLIGHTED_IDS: ReadonlySet<number> = new Set();

const InsertedTekstValgHighlightContext = createContext<ReadonlySet<number>>(EMPTY_HIGHLIGHTED_IDS);

export const InsertedTekstValgHighlightProvider = ({
  ids,
  children,
}: {
  ids: ReadonlySet<number>;
  children: ReactNode;
}) => <InsertedTekstValgHighlightContext.Provider value={ids}>{children}</InsertedTekstValgHighlightContext.Provider>;

export const useInsertedTekstValgHighlight = () => useContext(InsertedTekstValgHighlightContext);

export const isTekstValgHighlighted = (highlightedIds: ReadonlySet<number>, element: Identifiable): boolean =>
  element.id !== null && highlightedIds.has(element.id);

const addId = (ids: Set<number>, element: Identifiable) => {
  if (element.id !== null) ids.add(element.id);
};

const collectTextContentIds = (ids: Set<number>, textContents: readonly TextContent[]) => {
  for (const textContent of textContents) addId(ids, textContent);
};

const collectItemIds = (ids: Set<number>, listItems: readonly Item[]) => {
  for (const listItem of listItems) {
    addId(ids, listItem);
    collectTextContentIds(ids, listItem.content);
  }
};

const collectCellIds = (ids: Set<number>, cell: Cell) => {
  addId(ids, cell);
  collectTextContentIds(ids, cell.text);
};

const collectContentIds = (ids: Set<number>, contents: readonly Content[]) => {
  for (const content of contents) {
    addId(ids, content);
    if (content.type === ITEM_LIST) {
      collectItemIds(ids, content.items);
    } else if (content.type === TABLE) {
      addId(ids, content.header);
      for (const colSpec of content.header.colSpec) {
        addId(ids, colSpec);
        collectCellIds(ids, colSpec.headerContent);
      }
      for (const row of content.rows) {
        addId(ids, row);
        for (const cell of row.cells) collectCellIds(ids, cell);
      }
    }
  }
};

const collectBlockIds = (ids: Set<number>, blocks: readonly AnyBlock[]) => {
  for (const block of blocks) {
    addId(ids, block);
    if (block.type === "PARAGRAPH") {
      collectContentIds(ids, block.content);
    } else {
      collectTextContentIds(ids, block.content);
    }
  }
};

export const collectAllIds = (letter: EditedLetter): Set<number> => {
  const ids = new Set<number>();
  collectTextContentIds(ids, letter.title.text);
  collectBlockIds(ids, letter.blocks);
  return ids;
};

export const collectNewIds = (seenIds: ReadonlySet<number>, letter: EditedLetter): Set<number> => {
  const allIds = collectAllIds(letter);
  const newIds = new Set<number>();
  for (const id of allIds) if (!seenIds.has(id)) newIds.add(id);
  return newIds;
};

const lengthOfText = (textContent: TextContent): number => {
  if (textContent.type === LITERAL) return (textContent.editedText ?? textContent.text).length;
  if (textContent.type === VARIABLE) return textContent.text.length;
  return 0;
};

const findLastTextFocusInBlock = (
  blockIndex: number,
  contents: readonly TextContent[] | readonly Content[],
  highlightedIds: ReadonlySet<number>,
): Focus | null => {
  // Prefer a highlighted text content if present.
  for (let contentIndex = contents.length - 1; contentIndex >= 0; contentIndex--) {
    const content = contents[contentIndex];
    if (content.type === LITERAL || content.type === VARIABLE || content.type === NEW_LINE) {
      if (isTekstValgHighlighted(highlightedIds, content)) {
        return { blockIndex, contentIndex, cursorPosition: lengthOfText(content) };
      }
    }
  }
  // Otherwise fall back to the last text-like content.
  for (let contentIndex = contents.length - 1; contentIndex >= 0; contentIndex--) {
    const content = contents[contentIndex];
    if (content.type === LITERAL || content.type === VARIABLE || content.type === NEW_LINE) {
      return { blockIndex, contentIndex, cursorPosition: lengthOfText(content) };
    }
  }
  return null;
};

// Returns a focus pointing at the end of the most recently inserted text after a tekstvalg toggle.
// Prefers the last newly added block; otherwise the last newly added text content in an existing block.
export const findLastInsertedFocus = (letter: EditedLetter, highlightedIds: ReadonlySet<number>): Focus | null => {
  // 1) Last newly added block.
  for (let blockIndex = letter.blocks.length - 1; blockIndex >= 0; blockIndex--) {
    const block = letter.blocks[blockIndex];
    if (isTekstValgHighlighted(highlightedIds, block)) {
      const focus = findLastTextFocusInBlock(blockIndex, block.content, highlightedIds);
      if (focus) return focus;
      return { blockIndex, contentIndex: 0, cursorPosition: 0 };
    }
  }
  // 2) Last newly added text content inside an existing block.
  for (let blockIndex = letter.blocks.length - 1; blockIndex >= 0; blockIndex--) {
    const block = letter.blocks[blockIndex];
    const contents = block.content as readonly Content[];
    for (let contentIndex = contents.length - 1; contentIndex >= 0; contentIndex--) {
      const content = contents[contentIndex];
      if (
        (content.type === LITERAL || content.type === VARIABLE || content.type === NEW_LINE) &&
        isTekstValgHighlighted(highlightedIds, content)
      ) {
        return { blockIndex, contentIndex, cursorPosition: lengthOfText(content) };
      }
    }
  }
  return null;
};
