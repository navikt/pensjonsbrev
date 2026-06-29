import { current, type Draft, produce } from "immer";

import { MergeTarget } from "~/Brevredigering/LetterEditor/actions/merge";
import { updateLiteralText } from "~/Brevredigering/LetterEditor/actions/updateContentText";
import {
  effectiveListType,
  isFritekst,
  isItemList,
  isLiteral,
  isParagraph,
  isTableCellIndex,
  isTextContent,
} from "~/Brevredigering/LetterEditor/model/utils";
import { type BrevResponse } from "~/types/brev";
import {
  type AnyBlock,
  type Cell,
  type ColumnSpec,
  type Content,
  type EditedLetter,
  type ElementTags,
  FontType,
  type Identifiable,
  type Item,
  type ItemList,
  ListType,
  type LiteralValue,
  type NewLine,
  type ParagraphBlock,
  type Row,
  type Table,
  type TextContent,
  type Title1Block,
  type Title2Block,
  type Title3Block,
  type VariableValue,
} from "~/types/brevbakerTypes";
import { type Nullable } from "~/types/Nullable";

import {
  type BlockContentIndex,
  type Focus,
  type ItemContentIndex,
  type LetterEditorState,
  type LiteralIndex,
  type TableCellIndex,
} from "../model/state";

export function cleanseText(text: string): string {
  return text.replaceAll("<br>", "").replaceAll("&nbsp;", " ").replaceAll("\n", " ").replaceAll("\r", "");
}

export function isEditableContent(content: Content | undefined | null): boolean {
  return content != null && (content.type === "VARIABLE" || content.type === "ITEM_LIST");
}

export function isBlockContentIndex(f: Focus | LiteralIndex | undefined): f is BlockContentIndex {
  return (
    f !== undefined &&
    "blockIndex" in f &&
    f.blockIndex !== undefined &&
    "contentIndex" in f &&
    f.contentIndex !== undefined &&
    !isItemContentIndex(f) &&
    !isTableCellIndex(f)
  );
}

export function isTable(content: Content | undefined | null): content is Table {
  return content?.type === "TABLE";
}

export function isItemContentIndex(f: Focus | LiteralIndex | undefined): f is ItemContentIndex {
  return (
    f !== undefined &&
    "itemIndex" in f &&
    f.itemIndex !== undefined &&
    "itemContentIndex" in f &&
    f.itemContentIndex !== undefined
  );
}

export function isIndicesOfSameType<T extends LiteralIndex>(first: T, second: LiteralIndex): second is T {
  return (
    (isItemContentIndex(first) && isItemContentIndex(second)) ||
    (isTableCellIndex(first) && isTableCellIndex(second)) ||
    (isBlockContentIndex(first) && isBlockContentIndex(second))
  );
}

export function isAtStartOfBlock(f: Focus, offset?: number): boolean {
  if (isItemContentIndex(f)) {
    return f.contentIndex === 0 && f.itemIndex === 0 && f.itemContentIndex === 0 && (offset ?? f.cursorPosition) === 0;
  } else if (isTableCellIndex(f)) {
    return (
      f.contentIndex === 0 &&
      f.rowIndex === 0 &&
      f.cellIndex === 0 &&
      f.cellContentIndex === 0 &&
      (offset ?? f.cursorPosition) === 0
    );
  } else if (isBlockContentIndex(f)) {
    return f.contentIndex === 0 && (offset ?? f.cursorPosition) === 0;
  } else {
    return false;
  }
}

export function isAtStartOfItemList(f: Focus): boolean {
  return isItemContentIndex(f) && f.itemIndex === 0 && f.itemContentIndex === 0 && f.cursorPosition === 0;
}

export function isAtEndOfItemList(f: Focus, itemList: ItemList): boolean {
  return (
    isItemContentIndex(f) &&
    f.itemIndex === itemList.items.length - 1 &&
    f.itemContentIndex === itemList.items[f.itemIndex].content.length - 1 &&
    (f.cursorPosition ?? 0) >= text(itemList.items[f.itemIndex].content[f.itemContentIndex]).length
  );
}

export function isAtStartOfTable(f: Focus): boolean {
  return (
    isTableCellIndex(f) && f.rowIndex === -1 && f.cellIndex === 0 && f.cellContentIndex === 0 && f.cursorPosition === 0
  );
}

export function isAtEndOfTable(f: Focus, table: Table): boolean {
  if (isTableCellIndex(f)) {
    if (isTable(table)) {
      const lastRowIndex = table.rows.length - 1;
      const lastRowCells =
        lastRowIndex >= 0
          ? (table.rows[lastRowIndex]?.cells ?? [])
          : table.header.colSpec.map((cs) => cs.headerContent);

      const lastCellIndex = Math.max(0, lastRowCells?.length - 1);
      const lastCellContentIndex = Math.max(0, (lastRowCells[lastCellIndex]?.text.length ?? 0) - 1);
      const lastContent = lastRowCells[lastCellIndex]?.text[lastCellContentIndex];
      return (
        f.rowIndex === lastRowIndex &&
        f.cellIndex === lastCellIndex &&
        f.cellContentIndex === lastCellContentIndex &&
        f.cursorPosition != null &&
        f.cursorPosition >= (lastContent ? text(lastContent).length : 0)
      );
    }
  }
  return false;
}

export function isAtSameBlockContent(first: LiteralIndex, second: LiteralIndex): boolean {
  return first.blockIndex === second.blockIndex && first.contentIndex === second.contentIndex;
}

export function isAtSameItem(first: ItemContentIndex, second: ItemContentIndex): boolean {
  return (
    first.blockIndex === second.blockIndex &&
    first.contentIndex === second.contentIndex &&
    first.itemIndex === second.itemIndex
  );
}

export function isAtSameTableCell(first: TableCellIndex, second: TableCellIndex): boolean {
  return (
    first.blockIndex === second.blockIndex &&
    first.contentIndex === second.contentIndex &&
    first.rowIndex === second.rowIndex &&
    first.cellIndex === second.cellIndex
  );
}

export function isIndexAfter(
  first: LiteralIndex & { cursorPosition: number },
  after: LiteralIndex & { cursorPosition: number },
): boolean {
  if (first.blockIndex === after.blockIndex) {
    if (first.contentIndex === after.contentIndex) {
      if (isItemContentIndex(first) && isItemContentIndex(after)) {
        if (first.itemIndex === after.itemIndex) {
          if (first.itemContentIndex === after.itemContentIndex) {
            return first.cursorPosition < after.cursorPosition;
          } else {
            return first.itemContentIndex < after.itemContentIndex;
          }
        } else {
          return first.itemIndex < after.itemIndex;
        }
      } else if (isTableCellIndex(first) && isTableCellIndex(after)) {
        if (first.rowIndex === after.rowIndex) {
          if (first.cellIndex === after.cellIndex) {
            if (first.cellContentIndex === after.cellContentIndex) {
              return first.cursorPosition < after.cursorPosition;
            } else {
              return first.cellContentIndex < after.cellContentIndex;
            }
          } else {
            return first.cellIndex < after.cellIndex;
          }
        } else {
          return first.rowIndex < after.rowIndex;
        }
      } else {
        return first.cursorPosition < after.cursorPosition;
      }
    } else {
      return first.contentIndex < after.contentIndex;
    }
  } else {
    return first.blockIndex < after.blockIndex;
  }
}

export function isAtStartOfItem(f: Focus, offset?: number): boolean {
  return isItemContentIndex(f) && f.itemContentIndex === 0 && (offset ?? f.cursorPosition) === 0;
}

export function text<T extends TextContent | undefined>(
  content: T,
): string | (undefined extends T ? undefined : never) {
  if (content?.type === "LITERAL") {
    return content.editedText ?? content.text;
  } else if (content?.type === "VARIABLE" || content?.type === "NEW_LINE") {
    return content.text;
  } else {
    return undefined as undefined extends T ? undefined : never;
  }
}

export function fontTypeOf(content: TextContent): FontType {
  switch (content.type) {
    case "NEW_LINE":
      return FontType.PLAIN;
    case "LITERAL":
      return content.editedFontType ?? content.fontType;
    case "VARIABLE":
      return content.fontType;
  }
}

export function isNew(obj: Identifiable): obj is Identifiable & { id: null | undefined } {
  return typeof obj.id !== "number";
}

export function isFromTemplate(obj: Identifiable): obj is Identifiable & { id: number } {
  return typeof obj.id === "number";
}

export function create(brev: BrevResponse): LetterEditorState {
  return {
    info: brev.info,
    redigertBrev: normalizeTableSeparators(brev.redigertBrev),
    redigertBrevHash: brev.redigertBrevHash,
    saksbehandlerValg: brev.saksbehandlerValg,
    saveStatus: "SAVED",
    focus: { blockIndex: 0, contentIndex: 0 },
    history: { entries: [], entryPointer: -1 },
  };
}

/**
 * Normalizes table-separator invariants on a server-loaded letter:
 *   1. Two tables that sit next to each other in the same paragraph get an
 *      empty literal inserted between them.
 *   2. If the last paragraph of the letter ends with a table, an empty literal
 *      is appended after it so the user has somewhere to place the caret and
 *      keep typing.
 *
 * This is intentionally a pure transform: it returns a new letter without
 * touching `saveStatus`. The inserted separators are brand-new empty literals,
 * so they only get persisted to the backend if the user later makes a real
 * edit that triggers a save.
 */
export function normalizeTableSeparators(letter: EditedLetter): EditedLetter {
  return produce(letter, applyTableSeparatorNormalization);
}

/**
 * Same invariants as {@link normalizeTableSeparators}, but operates directly on
 * an existing immer draft.
 *
 * Intended for use inside other recipes (e.g. after a deletion) that already
 * own a draft and need to restore the table-separator invariants without
 * spinning up a nested `produce` call. Callers are responsible for setting
 * `saveStatus` themselves — this function only adjusts the letter structure.
 */
export function applyTableSeparatorNormalization(draft: Draft<EditedLetter>) {
  const blocks = draft.blocks;
  for (let blockIndex = 0; blockIndex < blocks.length; blockIndex++) {
    const block = blocks[blockIndex];
    if (block.type !== "PARAGRAPH") continue;

    // Insert a separator between any two adjacent tables in the same block
    let contentIndex = 0;
    while (contentIndex < block.content.length - 1) {
      if (isTable(block.content[contentIndex]) && isTable(block.content[contentIndex + 1])) {
        block.content.splice(contentIndex + 1, 0, newLiteral({ editedText: "" }));
        contentIndex += 2;
      } else {
        contentIndex++;
      }
    }

    // Append a trailing literal if the last block ends in a table
    const isLastBlock = blockIndex === blocks.length - 1;
    if (isLastBlock && block.content.length > 0 && isTable(block.content.at(-1))) {
      block.content.push(newLiteral({ editedText: "" }));
    }
  }
}

/**
 * Splits a single paragraph block at `blockIndex` if its content mixes
 * ItemList(s) with other content, or contains multiple ItemLists of
 * different types.
 *
 * Algorithm:
 *  1. Merge adjacent same-type ItemLists within the block (reduces needless splits).
 *  2. Partition the resulting content into contiguous "runs" — each run is
 *     either all non-list content (text, tables, …) or a single ItemList.
 *  3. The first run stays in the original block (preserving its id and
 *     deletedContent). Each subsequent run becomes a new PARAGRAPH block
 *     (id: null) inserted immediately after, in order.
 *
 * ID-preservation invariant for re-merge:
 *  - The original block keeps its id at the lowest index.
 *  - Content items moved out are removed via removeElements, which records
 *    their ids in the original block's deletedContent (split-persistence).
 *  - New blocks get id: null, parentId: null, deletedContent: [].
 *  - The merge helpers (mergeListWithAdjacentBlocks, mergeAdjacentListBlocks)
 *    always keep the lower-indexed block as the survivor, so re-merging any
 *    subset of split blocks always produces a block with the original id.
 *
 * Focus update:
 *  If draft.focus.blockIndex === blockIndex and draft.focus has a contentIndex,
 *  this function updates draft.focus.blockIndex and contentIndex to point at
 *  the same content in its new block after the split.
 *
 * Returns the number of blocks that replaced the original (1 = no split needed).
 */
export function splitMixedListBlock(draft: Draft<LetterEditorState>, blockIndex: number): number {
  const block = draft.redigertBrev.blocks[blockIndex];
  if (!isParagraph(block) || block.content.length <= 1) return 1;

  // Step 1: merge adjacent same-type ItemLists within this block.
  mergeSameTypeListsInBlock(block.content, block.deletedContent, block.id ?? null);

  // Step 2: check whether a split is needed at all.
  // Only id:null (user-created) lists must be isolated in their own block.
  // Template lists (id != null) may co-exist with other content — do not split them out.
  if (block.content.length <= 1) return 1;
  const contentSnap = current(block.content) as Content[];
  const isNewList = (c: Content): c is ItemList => isItemList(c) && isNew(c);
  if (!contentSnap.some(isNewList)) return 1;

  // Step 3: partition into runs (maximal sequences of the same category).
  // Each run is [startIndex, endIndex] (inclusive) in contentSnap.
  // Only id:null ItemLists are their own single-element runs; all other content
  // (text, variables, tables, and id != null ItemLists) forms "text" runs.
  const runs: Array<{ start: number; end: number }> = [];
  for (let i = 0; i < contentSnap.length; ) {
    if (isNewList(contentSnap[i])) {
      runs.push({ start: i, end: i });
      i++;
    } else {
      const j = i;
      while (i < contentSnap.length && !isNewList(contentSnap[i])) i++;
      runs.push({ start: j, end: i - 1 });
    }
  }

  if (runs.length <= 1) return 1;

  // Step 4: remove content for runs[1..N] in reverse (avoids index shifting).
  // Capture plain snapshots now for later block construction.
  const laterRunContents: Content[][] = [];
  for (let r = runs.length - 1; r >= 1; r--) {
    const run = runs[r];
    const removed = removeElements(run.start, run.end - run.start + 1, {
      content: block.content,
      deletedContent: block.deletedContent,
      id: block.id,
    }) as unknown as Content[];
    laterRunContents.unshift(removed);
  }

  // Step 5: create and insert new blocks after the original.
  const newBlocks = laterRunContents.map((content) => newParagraph({ content }));
  draft.redigertBrev.blocks.splice(blockIndex + 1, 0, ...newBlocks);

  // Step 6: update focus if it was inside this block.
  if ("contentIndex" in draft.focus && draft.focus.blockIndex === blockIndex) {
    const oldContentIndex = draft.focus.contentIndex;
    let cumulative = 0;
    for (let r = 0; r < runs.length; r++) {
      const runLength = runs[r].end - runs[r].start + 1;
      if (oldContentIndex < cumulative + runLength) {
        draft.focus.blockIndex = blockIndex + r;
        draft.focus.contentIndex = oldContentIndex - cumulative;
        break;
      }
      cumulative += runLength;
    }
  }

  return runs.length;
}

/**
 * Flattens several ItemLists into a single new ItemList, preserving the backend
 * tracking identity. The merged list adopts the id of the first source list that
 * has a non-null id (falling back to the last list), so the backend keeps
 * tracking it across re-renders.
 *
 * INTENTIONAL ID COPY — ID preservation during merge (see letter-editor-actions
 * SKILL.md). Callers must remove the source lists via removeElements (recording
 * their ids in deletedContent) and insert the result via addElements (which
 * un-deletes the preserved id).
 */
export function buildMergedItemList(lists: ItemList[], targetListType: ListType): ItemList {
  const listWithId = lists.find(isFromTemplate) ?? lists[lists.length - 1];
  const allItems = lists.flatMap((list) => [...list.items]);
  const allDeletedItems = lists.flatMap((list) => [...list.deletedItems]);

  return newItemList({
    id: listWithId.id,
    listType: listWithId.listType,
    editedListType: targetListType !== listWithId.listType ? targetListType : listWithId.editedListType,
    items: allItems,
    deletedItems: allDeletedItems,
  });
}

/**
 * Coalesces the run of same-type ItemLists adjoining `anchorContentIndex` within
 * a single block into one list (via buildMergedItemList). Returns the content
 * index of the merged list and the number of items contributed by lists that
 * preceded the anchor in the run (used by callers to offset itemIndex for focus).
 *
 * Does not touch `draft.focus`. If fewer than two same-type lists adjoin the
 * anchor, this is a no-op and returns the anchor unchanged.
 */
export function coalesceAdjacentSameTypeLists(
  draft: Draft<LetterEditorState>,
  blockIndex: number,
  anchorContentIndex: number,
  listType: ListType,
): { newContentIndex: number; itemIndexOffset: number } {
  const block = draft.redigertBrev.blocks[blockIndex];
  if (!isParagraph(block)) return { newContentIndex: anchorContentIndex, itemIndexOffset: 0 };

  const range = findAdjoiningContent(
    anchorContentIndex,
    block.content,
    (content): content is ItemList => isItemList(content) && effectiveListType(content) === listType,
  );

  if (range.count <= 1) return { newContentIndex: anchorContentIndex, itemIndexOffset: 0 };

  const itemLists = removeElements(range.startIndex, range.count, {
    content: block.content,
    deletedContent: block.deletedContent,
    id: block.id,
  }).filter(isItemList);

  // Items contributed by lists preceding the anchor in the run.
  const anchorPosition = anchorContentIndex - range.startIndex;
  const itemIndexOffset = itemLists.slice(0, anchorPosition).reduce((sum, list) => sum + list.items.length, 0);

  const mergedList = buildMergedItemList(itemLists, listType);
  addElements([mergedList], range.startIndex, block.content, block.deletedContent);

  return { newContentIndex: range.startIndex, itemIndexOffset };
}

/**
 * Moves all items (and deletedItems) from the ItemList at
 * `blocks[sourceBlockIndex].content[sourceContentIndex]` into `targetList`, at the
 * front or back. Removes the source list from its block (recording its id in the
 * block's deletedContent for split-persistence), and removes the source block
 * entirely if it becomes empty.
 *
 * Shared mechanical core for cross-block list merging. Callers own the guards
 * (which neighbour to merge, type checks) and focus. This helper does not touch
 * `draft.focus`. Returns how many items were moved and whether the source block
 * was removed (so callers can adjust block indices when the source sat at a lower
 * index than the target).
 */
export function absorbListIntoList(
  draft: Draft<LetterEditorState>,
  targetList: Draft<ItemList>,
  sourceBlockIndex: number,
  sourceContentIndex: number,
  position: "front" | "back",
): { movedItemCount: number; sourceBlockRemoved: boolean } {
  const blocks = draft.redigertBrev.blocks;
  const sourceBlock = blocks[sourceBlockIndex];
  const sourceList = sourceBlock?.content[sourceContentIndex];
  if (!isItemList(sourceList)) return { movedItemCount: 0, sourceBlockRemoved: false };

  // Snapshot before removal so the data survives the source-list splice.
  const movedItems = current(sourceList.items) as Item[];
  const movedDeletedItems = current(sourceList.deletedItems) as number[];
  const movedItemCount = movedItems.length;

  // Remove the list from its block (records id in deletedContent for split-persistence).
  removeElements(sourceContentIndex, 1, {
    content: sourceBlock.content,
    deletedContent: sourceBlock.deletedContent,
    id: sourceBlock.id,
  });

  const insertIndex = position === "front" ? 0 : targetList.items.length;
  addElements(movedItems, insertIndex, targetList.items, targetList.deletedItems);
  // Items moved from another list have parentId pointing at the source list, so they cannot be in
  // targetList.deletedItems — but propagate the source's own deletedItems entries (SKILL.md: never
  // silently drop deletion records; the backend prunes stale ids).
  targetList.deletedItems.push(...movedDeletedItems);

  let sourceBlockRemoved = false;
  if (sourceBlock.content.length === 0) {
    removeElements(sourceBlockIndex, 1, {
      content: blocks,
      deletedContent: draft.redigertBrev.deletedBlocks,
      id: null,
    });
    sourceBlockRemoved = true;
  }

  return { movedItemCount, sourceBlockRemoved };
}

/**
 * Merges adjacent same-type ItemLists within a single block's content array
 * in place. Adjacent lists of the same effective type are combined into the
 * first list; the second list is removed (its id recorded in deletedContent
 * via removeElements for split-persistence).
 *
 * When one list has a non-null id and the other does not, the non-null id is
 * preserved on the surviving list (and un-deleted from deletedContent if it
 * was just added there by removeElements).
 */
function mergeSameTypeListsInBlock(
  content: Draft<Content[]>,
  deletedContent: Draft<number[]>,
  blockId: Nullable<number>,
): void {
  let i = 0;
  while (i < content.length - 1) {
    const first = content[i];
    const second = content[i + 1];
    if (isItemList(first) && isItemList(second) && effectiveListType(first) === effectiveListType(second)) {
      const secondItems = current(second.items) as Item[];
      const secondDeletedItems = current(second.deletedItems) as number[];

      // Remove second from the block; records second.id in deletedContent if applicable.
      removeElements(i + 1, 1, { content, deletedContent, id: blockId });

      // If first has no id but second did, promote second's id onto first (and undo the deletion
      // that removeElements just recorded, since we're reusing the id).
      if (isNew(first) && isFromTemplate(second)) {
        (first as Draft<ItemList>).id = second.id;
        const deletedIndex = deletedContent.indexOf(second.id);
        if (deletedIndex !== -1) deletedContent.splice(deletedIndex, 1);
      }

      // Append second's items and deletedItems to first.
      (first as Draft<ItemList>).items.push(...secondItems);
      (first as Draft<ItemList>).deletedItems.push(...secondDeletedItems);

      // Re-check position i in case the newly extended first is now adjacent to another same-type
      // list.
    } else {
      i++;
    }
  }
}

export function removeElements<T extends Identifiable>(
  startIndex: number,
  count: number,
  from: { content: Draft<T[]>; deletedContent: Draft<number[]>; id?: number | null },
): Draft<T[]> {
  const removedElements = from.content.splice(startIndex, count);
  for (const e of removedElements) deleteElement(e, from);
  return removedElements;
}

function deleteElement(
  toDelete: Identifiable,
  from: { content: Identifiable[]; deletedContent: Draft<number[]>; id?: number | null },
) {
  if (
    isFromTemplate(toDelete) &&
    toDelete.parentId === from.id &&
    !from.deletedContent.includes(toDelete.id) &&
    !from.content.map((c) => c.id).includes(toDelete.id)
  ) {
    from.deletedContent.push(toDelete.id);
  }
}

export function addElements<T extends Identifiable, E extends T>(
  elements: E[],
  toIndex: number,
  to: Draft<T[]>,
  deleted: Draft<number[]>,
): void;
export function addElements<T extends Identifiable, E extends T>(
  elements: Draft<E[]>,
  toIndex: number,
  to: Draft<T[]>,
  deleted: Draft<number[]>,
): void {
  if (toIndex > 0) {
    const elementBeforeStartIndex = to[toIndex - 1];
    const firstElementToAdd = elements[0];

    to.splice(
      toIndex - 1,
      1,
      ...mergeLiteralsIfPossible(elementBeforeStartIndex, firstElementToAdd as Draft<T>),
      ...(elements.slice(1) as unknown as Draft<T>[]),
    );
  } else {
    to.splice(toIndex, 0, ...(elements as unknown as Draft<T>[]));
  }

  const presentIds = to.map((e) => e.id).filter((id) => id !== null) as number[];
  for (const id of presentIds) {
    const index = deleted.indexOf(id);
    if (index !== -1) {
      deleted.splice(index, 1);
    }
  }
}

export function findAdjoiningContent<T extends Content, S extends T>(
  atIndex: number,
  from: T[],
  predicate: (value: T) => value is S,
): { startIndex: number; endIndex: number; count: number } {
  if (from.length === 0) {
    return { startIndex: 0, endIndex: 0, count: 0 };
  }

  const reverseSearchNonMatching = from.slice(0, atIndex).findLastIndex((c) => !predicate(c));
  const forwardSearchNonMatching = from.slice(atIndex + 1).findIndex((c) => !predicate(c));

  const startIndex = reverseSearchNonMatching >= 0 ? reverseSearchNonMatching + 1 : 0;
  const endIndex = forwardSearchNonMatching >= 0 ? atIndex + forwardSearchNonMatching : from.length - 1;

  return {
    startIndex,
    endIndex,
    count: endIndex - startIndex + 1,
  };
}

export function mergeLiteralsIfPossible<T extends Identifiable>(first: Draft<T>, second: Draft<T>): Draft<T[]> {
  if (
    isLiteral(first) &&
    isLiteral(second) &&
    !isFritekst(first) &&
    !isFritekst(second) &&
    fontTypeOf(first) === fontTypeOf(second)
  ) {
    const mergedText = text(first) + text(second);
    if (isFromTemplate(first) && isFromTemplate(second)) {
      return [first, second];
    } else if (isNew(first)) {
      updateLiteralText(second, mergedText);
      return [second];
    } else {
      updateLiteralText(first, mergedText);
      return [first];
    }
  } else {
    return [first, second];
  }
}

/**
 * Splits literal text at given offset by updating 'editedText' of the given literal and returning a new literal.
 *
 * @param literal the literal to update
 * @param offset the offset to split at
 * @returns a new literal
 */
export function splitLiteralAtOffset(literal: Draft<LiteralValue>, offset: number): LiteralValue {
  const origText = text(literal);
  const newText = cleanseText(origText.slice(0, Math.max(0, offset)));
  const nextText = cleanseText(origText.slice(Math.max(0, offset)));

  updateLiteralText(literal, newText);

  return newLiteral({ editedText: nextText, fontType: literal.editedFontType ?? literal.fontType });
}

export function newTitle(args: {
  content: TextContent[];
  type: "TITLE1" | "TITLE2" | "TITLE3";
  deletedContent?: number[];
}): Title1Block | Title2Block | Title3Block {
  return {
    type: args.type,
    id: null,
    editable: true,
    deletedContent: args.deletedContent ?? [],
    content: args.content,
    missingFromTemplate: false,
  };
}

export function newParagraph(args: {
  id?: Nullable<number>;
  parentId?: Nullable<number>;
  content: Content[];
  deletedContent?: number[];
}): ParagraphBlock {
  return {
    type: "PARAGRAPH",
    id: args.id ?? null,
    parentId: args.parentId ?? null,
    editable: true,
    deletedContent: args.deletedContent ?? [],
    missingFromTemplate: false,
    content: args.content,
  };
}

export function newTable(rows: Row[]): Table {
  if (rows.length === 0) {
    throw new Error("newTable: rows must contain at least one row");
  }

  const colCount = rows[0].cells.length;

  if (!rows.every((row) => row.cells.length === colCount)) {
    throw new Error("newTable: all rows must have an identical column count");
  }
  return {
    type: "TABLE",
    id: null,
    parentId: null,
    header: {
      id: null,
      parentId: null,
      colSpec: newColSpec(colCount),
    },
    rows,
    deletedRows: [],
  };
}

export function newLiteral(
  args: {
    id?: Nullable<number>;
    parentId?: Nullable<number>;
    text?: string;
    editedText?: Nullable<string>;
    fontType?: Nullable<FontType>;
    editedFontType?: Nullable<FontType>;
    tags?: ElementTags[];
  } = {},
): LiteralValue {
  return {
    type: "LITERAL",
    id: args?.id ?? null,
    parentId: args?.parentId ?? null,
    text: args?.text ?? "",
    editedText: args?.editedText ?? null,
    editedFontType: args?.editedFontType ?? undefined,
    fontType: args?.fontType ?? FontType.PLAIN,
    tags: args?.tags ?? [],
  };
}

export const newVariable = (args: {
  id?: Nullable<number>;
  text: string;
  parentId?: Nullable<number>;
  fontType?: FontType;
}): VariableValue => {
  return {
    type: "VARIABLE",
    id: args.id ?? null,
    parentId: args.parentId ?? null,
    text: args.text,
    fontType: args.fontType ?? FontType.PLAIN,
    tags: [],
  };
};

export function newItem({
  id,
  content,
  parentId,
}: {
  id?: Nullable<number>;
  content: TextContent[];
  parentId?: Nullable<number>;
}): Item {
  return {
    id: id ?? null,
    parentId: parentId ?? null,
    content,
    deletedContent: [],
  };
}

export function newItemList(args: {
  id?: Nullable<number>;
  listType?: ListType;
  editedListType?: ListType;
  items: Item[];
  deletedItems?: number[];
}): ItemList {
  return {
    id: args.id ?? null,
    parentId: null,
    type: "ITEM_LIST",
    listType: args.listType ?? ListType.PUNKTLISTE,
    editedListType: args.editedListType,
    items: args.items,
    deletedItems: args.deletedItems ?? [],
  };
}

export function createNewLine(): NewLine {
  return {
    id: null,
    parentId: null,
    type: "NEW_LINE",
    text: "",
    fontType: "PLAIN",
  };
}

export function getMergeIds(sourceId: number, target: MergeTarget): [number, number] {
  switch (target) {
    case MergeTarget.PREVIOUS: {
      return [sourceId - 1, sourceId];
    }
    case MergeTarget.NEXT: {
      return [sourceId, sourceId + 1];
    }
  }
}

export function insertEmptyParagraphAfterBlock(draft: Draft<LetterEditorState>, blockIndex: number) {
  const emptyPara = newParagraph({
    content: [newLiteral()],
  });

  addElements([emptyPara], blockIndex + 1, draft.redigertBrev.blocks, draft.redigertBrev.deletedBlocks);

  draft.focus = {
    blockIndex: blockIndex + 1,
    contentIndex: 0,
    cursorPosition: 0,
  };
}

/**
 * Breaks out of an empty list item by removing it and reordering the current block into up to
 * five blocks in order: [content-before-list?] [items-before?] [blank-line] [items-after?] [content-after-list?].
 * Focus lands on the blank-line block.
 *
 * ID semantics (see letter-editor-actions SKILL.md):
 *  - The original block is reused for the first resulting block, keeping its id and deletedContent.
 *    Only the subsequent blocks are new (id: null). Template ids that leave the block are recorded
 *    in block.deletedContent so the backend doesn't re-introduce them.
 *  - The empty item is removed via removeElements, so a template item-id is recorded in
 *    itemList.deletedItems rather than being lost.
 *  - The original list (and its id) is reused for one surviving half; the other half becomes a new
 *    id: null list.
 */
export function breakOutEmptyItem(
  draft: Draft<LetterEditorState>,
  literalIndex: ItemContentIndex,
  itemList: Draft<ItemList>,
  block: Draft<AnyBlock>,
) {
  const blocks = draft.redigertBrev.blocks;
  const blockIndex = literalIndex.blockIndex;
  const itemIndex = literalIndex.itemIndex;
  const contentIndex = literalIndex.contentIndex;

  draft.saveStatus = "DIRTY";

  // Pre-existing deletedItems can't be attributed to a specific half, so we propagate them to
  // both halves (the backend prunes stale ids; dropping them risks re-introducing deleted items).
  const preExistingDeletedItems = [...itemList.deletedItems];
  const listType = itemList.listType;
  const editedListType = itemList.editedListType;

  // Remove the empty item itself. removeElements records a template item-id in itemList.deletedItems
  // (instead of losing it by discarding the whole block).
  removeElements(itemIndex, 1, {
    content: itemList.items,
    deletedContent: itemList.deletedItems,
    id: itemList.id,
  });

  const hasItemsBefore = itemIndex > 0;
  const hasItemsAfter = itemIndex < itemList.items.length;

  // The original list (and its id) is reused for one surviving half: the leading items when they
  // exist, otherwise the trailing items. The other half, when present, becomes a new id: null list.
  let beforeListContent: Content[] | null = null;
  let afterListContent: Content[] | null = null;

  if (hasItemsBefore) {
    if (hasItemsAfter) {
      // Split trailing items into a new list. removeElements records the moved item-ids in
      // itemList.deletedItems (split-persistence) so they aren't re-introduced into the retained list.
      const itemsAfter = removeElements(itemIndex, itemList.items.length - itemIndex, {
        content: itemList.items,
        deletedContent: itemList.deletedItems,
        id: itemList.id,
      }).map((removed) => current(removed)) as unknown as Item[];
      afterListContent = [
        newItemList({ listType, editedListType, items: itemsAfter, deletedItems: [...preExistingDeletedItems] }),
      ];
    }
    // The retained list keeps the leading items (and its id).
    beforeListContent = [current(itemList) as unknown as ItemList];
  } else if (hasItemsAfter) {
    // Empty item was first: the original list now holds only the trailing items; reuse it as-is.
    afterListContent = [current(itemList) as unknown as ItemList];
  }

  // Content surrounding the list within the same block becomes its own block on each side.
  const snapshotContent = current(block.content) as unknown as Content[];
  const contentBeforeList = snapshotContent.slice(0, contentIndex);
  const contentAfterList = snapshotContent.slice(contentIndex + 1);

  // Ordered pieces; each becomes a block.
  const pieces: Content[][] = [];
  if (contentBeforeList.length > 0) pieces.push(contentBeforeList);
  if (beforeListContent) pieces.push(beforeListContent);
  const blankPieceIndex = pieces.length;
  pieces.push([newLiteral()]);
  if (afterListContent) pieces.push(afterListContent);
  if (contentAfterList.length > 0) pieces.push(contentAfterList);

  // Reuse the original block for the first piece (preserving block.id and block.deletedContent).
  // Record any template-id child that leaves the block so the backend doesn't re-introduce it.
  const firstPiece = pieces[0];
  const survivingIds = new Set(firstPiece.map((c) => c.id).filter((id): id is number => id !== null));
  for (const childContent of block.content) {
    if (
      isFromTemplate(childContent) &&
      childContent.parentId === block.id &&
      !survivingIds.has(childContent.id) &&
      !block.deletedContent.includes(childContent.id)
    ) {
      block.deletedContent.push(childContent.id);
    }
  }
  block.content = firstPiece as Draft<Content[]>;

  // Insert the remaining pieces as new id: null blocks after the original.
  const newBlocks = pieces.slice(1).map((content) => newParagraph({ content }));
  blocks.splice(blockIndex + 1, 0, ...newBlocks);

  // Focus the blank line
  draft.focus = {
    blockIndex: blockIndex + blankPieceIndex,
    contentIndex: 0,
    cursorPosition: 0,
  };
}

export function newCell(text?: TextContent[]): Cell {
  return {
    id: null,
    parentId: null,
    text: text ?? [newLiteral({ editedText: "" })],
  };
}

export function newRow(colCount: number): Row {
  return {
    id: null,
    parentId: null,
    cells: Array.from({ length: colCount }, () => newCell()),
  };
}

export function newColSpec(colCount: number, headers?: { text: string; font?: FontType }[]): ColumnSpec[] {
  return Array.from({ length: colCount }, (_, i) => ({
    id: null,
    parentId: null,
    alignment: "LEFT" as const,
    span: 1,
    headerContent: {
      id: null,
      parentId: null,
      text: [
        newLiteral({
          editedText: headers?.[i]?.text ?? `Kolonne ${i + 1}`,
          fontType: headers?.[i]?.font ?? FontType.PLAIN,
        }),
      ],
    },
  }));
}

export function safeIndex(index: number, array: unknown[]) {
  return Math.max(0, Math.min(index, array.length - 1));
}

export function isValidIndex(letter: EditedLetter, index: LiteralIndex) {
  const content = letter.blocks[index.blockIndex]?.content[index.contentIndex];

  if (isItemList(content) && isItemContentIndex(index)) {
    return content.items[index.itemIndex]?.content[index.itemContentIndex] !== undefined;
  } else if (isTable(content) && isTableCellIndex(index)) {
    return index.rowIndex >= 0
      ? content.rows[index.rowIndex]?.cells[index.cellIndex]?.text[index.cellContentIndex] !== undefined
      : content.header.colSpec[index.cellIndex]?.headerContent?.text[index.cellContentIndex] !== undefined;
  } else {
    return isTextContent(content) && isBlockContentIndex(index);
  }
}

/**
 * Normalizes the order of deletedContent and deletedItems arrays by sorting them.
 *
 * This is necessary because the frontend and backend store deletion IDs in different orders:
 * - Frontend tracks deletions in the order they occur
 * - Backend processes and returns them in a different order.
 *
 * Without this normalization, identical content with differently ordered deletion arrays
 * would be considered different, causing undo/redo history to be incorrectly cleared.
 * see ManagedLetterEditorContextProvider.tsx for usage.
 *
 * Example:
 * Frontend: deletedContent: [491536928, 1727594288, -830599486]
 * Backend:  deletedContent: [1727594288, 491536928, -830599486]
 * After normalization, both become: [-830599486, 491536928, 1727594288]
 */
export function normalizeDeletedArrays(obj: unknown): unknown {
  if (Array.isArray(obj)) {
    return obj.map((item) => normalizeDeletedArrays(item));
  }
  if (typeof obj === "object" && obj !== null) {
    const normalized: Record<string, unknown> = {};
    for (const [key, value] of Object.entries(obj)) {
      if ((key === "deletedContent" || key === "deletedItems" || key === "deletedBlocks") && Array.isArray(value)) {
        // Sort to normalize order differences between frontend and backend
        normalized[key] = [...value].sort((a, b) => a - b);
      } else {
        normalized[key] = normalizeDeletedArrays(value);
      }
    }
    return normalized;
  }
  return obj;
}

export function collectAllLiteralValues(letter: EditedLetter): LiteralValue[] {
  const blockLiterals = letter.blocks.flatMap((block) => {
    switch (block.type) {
      case "TITLE1":
      case "TITLE2":
      case "TITLE3":
        return (block.content ?? []).filter((content) => isLiteral(content));
      case "PARAGRAPH":
        return (block.content ?? []).flatMap((content) => {
          if (isLiteral(content)) return [content];
          if (isItemList(content))
            return content?.items.flatMap((item) => (item?.content ?? []).filter((literal) => isLiteral(literal)));
          if (isTable(content)) {
            const header = content.header?.colSpec?.flatMap((spec) =>
              (spec.headerContent?.text ?? []).filter(isLiteral),
            );
            const body = content.rows?.flatMap((row) =>
              row.cells?.flatMap((cell) => (cell.text ?? []).filter(isLiteral)),
            );
            return [...header, ...body];
          }
          return [];
        });
      default:
        return [];
    }
  });
  const topTitleLiterals = (letter.title?.text ?? []).filter(isLiteral);

  return [...topTitleLiterals, ...blockLiterals];
}

export function collectFritekstLiterals(letter: EditedLetter): LiteralValue[] {
  return collectAllLiteralValues(letter).filter((literal) => isFritekst(literal));
}

export const countUnfilledFritekstPlaceholders = (letter: EditedLetter): number => {
  return collectFritekstLiterals(letter).filter((literal) => literal.editedText === null).length;
};

export const base64ToPdfBlob = (b64: string) =>
  new Blob([Uint8Array.from(atob(b64), (c) => c.charCodeAt(0))], { type: "application/pdf" });
