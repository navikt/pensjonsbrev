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

const EMPTY: ReadonlySet<number> = new Set();

const InsertedHighlightContext = createContext<ReadonlySet<number>>(EMPTY);

export const InsertedHighlightProvider = ({ ids, children }: { ids: ReadonlySet<number>; children: ReactNode }) => (
  <InsertedHighlightContext.Provider value={ids}>{children}</InsertedHighlightContext.Provider>
);

export const useInsertedHighlight = () => useContext(InsertedHighlightContext);

export const isHighlighted = (ids: ReadonlySet<number>, item: Identifiable): boolean =>
  item.id !== null && ids.has(item.id);

const addId = (acc: Set<number>, item: Identifiable) => {
  if (item.id !== null) acc.add(item.id);
};

const collectTextContentIds = (acc: Set<number>, contents: readonly TextContent[]) => {
  for (const c of contents) addId(acc, c);
};

const collectItemIds = (acc: Set<number>, items: readonly Item[]) => {
  for (const item of items) {
    addId(acc, item);
    collectTextContentIds(acc, item.content);
  }
};

const collectCellIds = (acc: Set<number>, cell: Cell) => {
  addId(acc, cell);
  collectTextContentIds(acc, cell.text);
};

const collectContentIds = (acc: Set<number>, contents: readonly Content[]) => {
  for (const c of contents) {
    addId(acc, c);
    if (c.type === ITEM_LIST) {
      collectItemIds(acc, c.items);
    } else if (c.type === TABLE) {
      addId(acc, c.header);
      for (const cs of c.header.colSpec) {
        addId(acc, cs);
        collectCellIds(acc, cs.headerContent);
      }
      for (const row of c.rows) {
        addId(acc, row);
        for (const cell of row.cells) collectCellIds(acc, cell);
      }
    }
  }
};

const collectBlockIds = (acc: Set<number>, blocks: readonly AnyBlock[]) => {
  for (const block of blocks) {
    addId(acc, block);
    if (block.type === "PARAGRAPH") {
      collectContentIds(acc, block.content);
    } else {
      collectTextContentIds(acc, block.content);
    }
  }
};

export const collectAllIds = (letter: EditedLetter): Set<number> => {
  const acc = new Set<number>();
  collectTextContentIds(acc, letter.title.text);
  collectBlockIds(acc, letter.blocks);
  return acc;
};

export const collectNewIds = (seenIds: ReadonlySet<number>, letter: EditedLetter): Set<number> => {
  const all = collectAllIds(letter);
  const result = new Set<number>();
  for (const id of all) if (!seenIds.has(id)) result.add(id);
  return result;
};

const lengthOfText = (c: TextContent): number => {
  if (c.type === LITERAL) return (c.editedText ?? c.text).length;
  if (c.type === VARIABLE) return c.text.length;
  return 0;
};

const findLastTextFocusInBlock = (
  blockIndex: number,
  contents: readonly TextContent[] | readonly Content[],
  newIds: ReadonlySet<number>,
): Focus | null => {
  for (let ci = contents.length - 1; ci >= 0; ci--) {
    const c = contents[ci];
    if (c.type === LITERAL || c.type === VARIABLE || c.type === NEW_LINE) {
      // Prefer a content that is itself new, otherwise fall back to last text content overall.
      if (isHighlighted(newIds, c)) {
        return { blockIndex, contentIndex: ci, cursorPosition: lengthOfText(c) };
      }
    }
  }
  // Fallback: last text-like content
  for (let ci = contents.length - 1; ci >= 0; ci--) {
    const c = contents[ci];
    if (c.type === LITERAL || c.type === VARIABLE || c.type === NEW_LINE) {
      return { blockIndex, contentIndex: ci, cursorPosition: lengthOfText(c) };
    }
  }
  return null;
};

/**
 * Finn fokus for slutten av sist innsatte tekst etter at tekstvalg ble togglet.
 * Foretrekker sist tilkomne blokk; ellers sist tilkomne tekstinnhold i en eksisterende blokk.
 */
export const findLastInsertedFocus = (letter: EditedLetter, newIds: ReadonlySet<number>): Focus | null => {
  // 1) New block(s) – pick the last one
  for (let bi = letter.blocks.length - 1; bi >= 0; bi--) {
    const block = letter.blocks[bi];
    if (isHighlighted(newIds, block)) {
      const focus = findLastTextFocusInBlock(bi, block.content, newIds);
      if (focus) return focus;
      return { blockIndex: bi, contentIndex: 0, cursorPosition: 0 };
    }
  }
  // 2) New text content inside an existing block – pick the last one
  for (let bi = letter.blocks.length - 1; bi >= 0; bi--) {
    const block = letter.blocks[bi];
    const contents = block.content as readonly Content[];
    for (let ci = contents.length - 1; ci >= 0; ci--) {
      const c = contents[ci];
      if ((c.type === LITERAL || c.type === VARIABLE || c.type === NEW_LINE) && isHighlighted(newIds, c)) {
        return { blockIndex: bi, contentIndex: ci, cursorPosition: lengthOfText(c) };
      }
    }
  }
  return null;
};
