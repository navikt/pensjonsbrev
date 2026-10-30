import { type Draft } from "immer";

import { type Cell, type Content, FontType, type LiteralValue, type TextContent } from "~/types/brevbakerTypes";

import { type Action, withPatches } from "../lib/actions";
import { type LetterEditorState, type LiteralIndex, type SelectionIndex } from "../model/state";
import { isItemList, isLiteral, isTableCellIndex, isTextContent } from "../model/utils";
import {
  fontTypeOf,
  isAtSameItem,
  isAtSameTableCell,
  isBlockContentIndex,
  isIndexAfter,
  isItemContentIndex,
  isTable,
  newLiteral,
  TABLE_HEADER_ROW_INDEX,
  text,
} from "./common";
import { updateLiteralText } from "./updateContentText";

export const getCurrentActiveFontTypeAtCursor = (editorState: LetterEditorState): FontType => {
  const block = editorState.redigertBrev.blocks[editorState.focus.blockIndex];
  const focus = editorState.focus;
  const blockContent = block?.content[editorState.focus.contentIndex];

  let textContent: TextContent | undefined;

  if (isTable(blockContent) && isTableCellIndex(focus)) {
    const cell =
      focus.rowIndex === TABLE_HEADER_ROW_INDEX
        ? blockContent.header.colSpec[focus.cellIndex]?.headerContent
        : blockContent.rows[focus.rowIndex]?.cells[focus.cellIndex];

    textContent = cell?.text?.at(focus.cellContentIndex);
  } else if (isItemList(blockContent) && isItemContentIndex(focus)) {
    textContent = blockContent?.items[focus.itemIndex]?.content[focus.itemContentIndex];
  } else if (isTextContent(blockContent)) {
    textContent = blockContent;
  }

  return isTextContent(textContent) ? fontTypeOf(textContent) : FontType.PLAIN;
};

type Position = LiteralIndex & { cursorPosition: number };

/**
 * Endrer fonttype på markert tekst, eller på ordet under markøren når markeringen er tom.
 *
 * Markeringen kan strekke seg over flere literals, men bare innenfor én tekstbeholder (tabellcelle,
 * punkt i liste eller tekst direkte i avsnitt). Krysser markeringen flere beholdere, gjøres ingen endring.
 * Uten `selection` brukes markørposisjonen i `focus`.
 */
export const switchFontType: Action<LetterEditorState, [fontType: FontType, selection?: SelectionIndex]> = withPatches(
  (draft, fontType, selection) => {
    const range = normalizeSelection(draft, selection);
    if (!range) {
      return;
    }
    const { start, end } = range;

    if (draft.redigertBrev.blocks[start.blockIndex]?.type !== "PARAGRAPH") {
      return;
    }

    // Tabelloverskrifter er allerede fete, så fet skrift er ikke tillatt der.
    if (fontType === FontType.BOLD && isTableCellIndex(start) && start.rowIndex === TABLE_HEADER_ROW_INDEX) {
      return;
    }

    const container = textContainerOf(draft, start, end);
    if (!container) {
      return;
    }

    const segments =
      start.cursorPosition === end.cursorPosition && indexInContainer(start) === indexInContainer(end)
        ? currentWordSegment(container, indexInContainer(start), start.cursorPosition)
        : markedSegments(
            container,
            indexInContainer(start),
            start.cursorPosition,
            indexInContainer(end),
            end.cursorPosition,
          );

    if (segments.length === 0) {
      return;
    }

    const targetFontType = segments.every((s) => fontTypeOf(s.literal) === fontType) ? FontType.PLAIN : fontType;

    // Fra siste til første segment, så indeksene til tidligere segmenter ikke forskyves.
    const changed = [...segments]
      .reverse()
      .map((segment) => switchFontTypeOfSegment(container, segment, targetFontType))
      .reverse();

    // Endepunktene flyttes gjennom splittingen, så markeringen fortsatt dekker det samme (også variabler og linjeskift).
    const newStart = withIndexInContainer(
      start,
      ...mapThroughSplits(changed, indexInContainer(start), start.cursorPosition),
    );
    const newEnd = withIndexInContainer(start, ...mapThroughSplits(changed, indexInContainer(end), end.cursorPosition));

    draft.focus = { ...newStart, pendingSelection: { start: newStart, end: newEnd } };
    draft.saveStatus = "DIRTY";
  },
);

type ChangedSegment = ReturnType<typeof switchFontTypeOfSegment>;

/** Gir ny [indeks, offset] for en posisjon i beholderen etter at segmentene er splittet. */
function mapThroughSplits(changed: ChangedSegment[], index: number, offset: number): [number, number] {
  let shift = 0;
  for (const segment of changed) {
    if (segment.index < index) {
      shift += segment.insertedBefore + segment.insertedAfter;
    } else if (segment.index === index) {
      const { from, to, insertedBefore } = segment;
      if (offset < from) return [index + shift, offset];
      if (offset <= to) return [index + shift + insertedBefore, offset - from];
      return [index + shift + insertedBefore + 1, offset - to];
    }
  }
  return [index + shift, offset];
}

function normalizeSelection(
  draft: Draft<LetterEditorState>,
  selection: SelectionIndex | undefined,
): { start: Position; end: Position } | undefined {
  if (selection) {
    return isIndexAfter(selection.end, selection.start)
      ? { start: selection.end, end: selection.start }
      : { start: selection.start, end: selection.end };
  }
  const { focus } = draft;
  if (focus.cursorPosition === undefined || focus.cursorPosition < 0) {
    return undefined;
  }
  const position = withIndexInContainer(focus, indexInContainer(focus), focus.cursorPosition);
  return { start: position, end: position };
}

type TextContainer = { content: Draft<Content[]> };

/**
 * Returnerer innholdslista som både start og slutt peker inn i, eller undefined om de er i ulike
 * beholdere. For tekst direkte i avsnittet regnes en tabell eller liste mellom start og slutt som en grense.
 */
function textContainerOf(draft: Draft<LetterEditorState>, start: Position, end: Position): TextContainer | undefined {
  const block = draft.redigertBrev.blocks[start.blockIndex];
  if (!block || start.blockIndex !== end.blockIndex) {
    return undefined;
  }

  if (isTableCellIndex(start) && isTableCellIndex(end) && isAtSameTableCell(start, end)) {
    const table = block.content[start.contentIndex];
    if (!isTable(table)) return undefined;
    const cell: Draft<Cell> | undefined =
      start.rowIndex === TABLE_HEADER_ROW_INDEX
        ? table.header.colSpec[start.cellIndex]?.headerContent
        : table.rows[start.rowIndex]?.cells[start.cellIndex];
    return cell ? { content: cell.text } : undefined;
  }

  if (isItemContentIndex(start) && isItemContentIndex(end) && isAtSameItem(start, end)) {
    const list = block.content[start.contentIndex];
    const item = isItemList(list) ? list.items[start.itemIndex] : undefined;
    return item ? { content: item.content } : undefined;
  }

  if (isBlockContentIndex(start) && isBlockContentIndex(end)) {
    const between = block.content.slice(start.contentIndex, end.contentIndex + 1);
    return between.every(isTextContent) ? { content: block.content } : undefined;
  }

  return undefined;
}

function indexInContainer(index: LiteralIndex): number {
  if (isTableCellIndex(index)) return index.cellContentIndex;
  if (isItemContentIndex(index)) return index.itemContentIndex;
  return index.contentIndex;
}

function withIndexInContainer(index: LiteralIndex, contentIndex: number, cursorPosition: number): Position {
  if (isTableCellIndex(index)) {
    const { blockIndex, contentIndex: tableIndex, rowIndex, cellIndex } = index;
    return {
      blockIndex,
      contentIndex: tableIndex,
      rowIndex,
      cellIndex,
      cellContentIndex: contentIndex,
      cursorPosition,
    };
  }
  if (isItemContentIndex(index)) {
    const { blockIndex, contentIndex: listIndex, itemIndex } = index;
    return { blockIndex, contentIndex: listIndex, itemIndex, itemContentIndex: contentIndex, cursorPosition };
  }
  return { blockIndex: index.blockIndex, contentIndex, cursorPosition };
}

type Segment = {
  index: number;
  literal: Draft<LiteralValue>;
  from: number;
  to: number;
};

function markedSegments(
  container: TextContainer,
  startIndex: number,
  startOffset: number,
  endIndex: number,
  endOffset: number,
): Segment[] {
  const segments: Segment[] = [];
  for (let index = startIndex; index <= endIndex; index++) {
    const literal = container.content[index];
    // Variabler og linjeskift har (foreløpig) ikke egen fonttype og hoppes over.
    if (!isLiteral(literal)) continue;
    const length = text(literal).length;
    const from = index === startIndex ? Math.min(startOffset, length) : 0;
    const to = index === endIndex ? Math.min(endOffset, length) : length;
    if (from < to) {
      segments.push({ index, literal, from, to });
    }
  }
  return segments;
}

function currentWordSegment(container: TextContainer, index: number, cursorPosition: number): Segment[] {
  const literal = container.content[index];
  if (!isLiteral(literal)) {
    return [];
  }
  const literalText = text(literal);
  if (literalText[cursorPosition] === " ") {
    return [];
  }

  let from = Math.min(cursorPosition, literalText.length);
  while (from > 0 && literalText[from - 1]?.trim() !== "") from--;
  let to = Math.min(cursorPosition, literalText.length);
  while (to < literalText.length && literalText[to]?.trim() !== "") to++;

  return from < to ? [{ index, literal, from, to }] : [];
}

/**
 * Endrer fonttypen til tekst i [from, to) i en literal. Den opprinnelige literalen beholdes (med id, parentId
 * og tags) som første del, slik som ved splitting med Enter; de øvrige delene blir nye literals med samme
 * fonttype som originalen hadde, så resten av teksten ikke mister formateringen sin.
 */
function switchFontTypeOfSegment(container: TextContainer, segment: Segment, fontType: FontType) {
  const { index, literal, from, to } = segment;
  const literalText = text(literal);
  const before = literalText.slice(0, from);
  const marked = literalText.slice(from, to);
  const after = literalText.slice(to);
  const originalFontType = fontTypeOf(literal);

  const newPieces: LiteralValue[] = [];
  if (before.length > 0) {
    updateLiteralText(literal, before);
    newPieces.push(newLiteral({ editedText: marked, fontType }));
  } else {
    if (after.length > 0) updateLiteralText(literal, marked);
    setEditedFontType(literal, fontType);
  }
  if (after.length > 0) {
    newPieces.push(newLiteral({ editedText: after, fontType: originalFontType }));
  }
  // Rene innsettinger av nye literals (id: null) etter originalen; ingen eksisterende id-er flyttes eller slettes.
  container.content.splice(index + 1, 0, ...newPieces);

  return {
    index,
    insertedBefore: before.length > 0 ? 1 : 0,
    insertedAfter: after.length > 0 ? 1 : 0,
    from,
    to,
  };
}

function setEditedFontType(literal: Draft<LiteralValue>, fontType: FontType) {
  literal.editedFontType = fontType === literal.fontType ? null : fontType;
}
