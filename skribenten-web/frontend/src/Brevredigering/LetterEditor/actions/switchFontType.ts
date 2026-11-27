import { type Draft } from "immer";

import { type Content, type EditedDocument, FontType, type LiteralValue } from "~/types/brevbakerTypes";

import { type Action, withPatches } from "../lib/actions";
import { type Focus, type LetterEditorState, type LiteralIndex, type SelectionIndex } from "../model/state";
import { isItemList, isLiteral, isNewLine, isTableCellIndex, isTextContent } from "../model/utils";
import {
  fontTypeOf,
  isIndexAfter,
  isItemContentIndex,
  isTable,
  mergeLiteralsIfPossible,
  splitLiteralAtOffset,
  text,
  textContentAtIndex,
} from "./common";

/** Whether all, none or only some of the text a formatting button acts on has its style. */
export type FormattingState = "on" | "off" | "mixed";

/** The selected characters of one literal. */
type Segment = { container: Content[]; literal: LiteralValue; from: number; to: number };

/**
 * Bold and italic act on the selected text, or the word around the caret. The decision is made once for the whole
 * range: if all of it already has the style, the style is removed, otherwise all of it gets the style. A literal can
 * be either bold or italic, so applying one replaces the other.
 *
 * Literals are split at the range boundaries, so the selection or caret is kept as character offsets within the text
 * it is in (a paragraph, list item or table cell) and mapped back onto the new literals.
 */
export const switchFontType: Action<LetterEditorState, [style: FontType]> = withPatches((draft, style) => {
  if (style === FontType.PLAIN) return;
  const segments = formattingSegments(draft);
  if (segments.length === 0) return;

  const target = segments.every((segment) => fontTypeOf(segment.literal) === style) ? FontType.PLAIN : style;
  const { selection, focus } = draft;
  const focusAtStart = selection !== undefined && isSamePosition(focus, selection.start);
  const startOffset = offsetInText(draft.redigertBrev, selection?.start ?? focus);
  const endOffset = selection && offsetInText(draft.redigertBrev, selection.end);

  const touched = new Map<Content[], Set<Content>>();
  for (const segment of segments.toReversed()) {
    if (fontTypeOf(segment.literal) === target) continue;
    const changed = applyFontType(segment, target);
    touched.set(segment.container, new Set([...(touched.get(segment.container) ?? []), ...changed]));
  }
  if (touched.size === 0) return;
  for (const [container, literals] of touched) mergeTouchedLiterals(container, literals);
  draft.saveStatus = "DIRTY";

  const mapBack = (index: LiteralIndex, offset: number | undefined, side: "start" | "end") =>
    offset === undefined ? undefined : endpointAt(draft.redigertBrev, index, offset, side);
  if (selection) {
    const start = mapBack(selection.start, startOffset, "start") ?? selection.start;
    const end = mapBack(selection.end, endOffset, "end") ?? selection.end;
    draft.selection = { start, end };
    draft.focus = { ...(focusAtStart ? start : end) };
  } else {
    // A caret after formatted text stays with it, so typing continues in the same formatting.
    draft.focus = mapBack(focus, startOffset, focus.cursorPosition === 0 ? "start" : "end") ?? focus;
  }
});

/** The toolbar state of a style for the selection, or the word around the caret. */
export function formattingStateOf(state: LetterEditorState, style: FontType): FormattingState {
  const segments = formattingSegments(state);
  if (segments.length === 0) {
    // Outside a word, show the formatting of the text the caret is in.
    const content = textContentAtIndex(state.redigertBrev, state.focus);
    return content && fontTypeOf(content) === style ? "on" : "off";
  }
  const withStyle = segments.filter((segment) => fontTypeOf(segment.literal) === style).length;
  return withStyle === segments.length ? "on" : withStyle === 0 ? "off" : "mixed";
}

/** The literal characters formatting acts on: the selection, or the word around a caret. */
function formattingSegments(state: LetterEditorState): Segment[] {
  const { selection, focus, redigertBrev } = state;
  if (selection) {
    const [start, end] = isIndexAfter(selection.end, selection.start)
      ? [selection.end, selection.start]
      : [selection.start, selection.end];
    return segmentsBetween(redigertBrev, start, end);
  }
  return wordAround(redigertBrev, focus);
}

/** Literals in paragraphs, list items and table cells. Headings, the letter title and variables can't be formatted. */
function segmentsBetween(document: EditedDocument, start: SelectionIndex["start"], end: SelectionIndex["end"]) {
  const segments: Segment[] = [];
  for (let blockIndex = Math.max(0, start.blockIndex); blockIndex <= end.blockIndex; blockIndex++) {
    const block = document.blocks[blockIndex];
    if (block?.type !== "PARAGRAPH") continue;
    for (const { container, index } of textContainersOf(block.content, blockIndex)) {
      for (const [position, literal] of container.entries()) {
        if (!isLiteral(literal)) continue;
        const literalIndex = withPositionInText(index, position);
        const fromStart = compareLiterals(literalIndex, start);
        const toEnd = compareLiterals(literalIndex, end);
        if (fromStart < 0 || toEnd > 0) continue;
        const from = fromStart === 0 ? start.cursorPosition : 0;
        const to = toEnd === 0 ? end.cursorPosition : text(literal).length;
        if (from < to) segments.push({ container, literal, from, to });
      }
    }
  }
  return segments;
}

/** A paragraph's own content, and the content of each list item and table cell in it. */
function* textContainersOf(content: Content[], blockIndex: number) {
  yield { container: content, index: { blockIndex, contentIndex: 0 } as LiteralIndex };
  for (const [contentIndex, element] of content.entries()) {
    if (isItemList(element)) {
      for (const [itemIndex, item] of element.items.entries()) {
        yield { container: item.content, index: { blockIndex, contentIndex, itemIndex, itemContentIndex: 0 } };
      }
    } else if (isTable(element)) {
      const cells = [
        ...element.header.colSpec.map((column, cellIndex) => ({ cell: column.headerContent, rowIndex: -1, cellIndex })),
        ...element.rows.flatMap((row, rowIndex) => row.cells.map((cell, cellIndex) => ({ cell, rowIndex, cellIndex }))),
      ];
      for (const { cell, rowIndex, cellIndex } of cells) {
        yield { container: cell.text, index: { blockIndex, contentIndex, rowIndex, cellIndex, cellContentIndex: 0 } };
      }
    }
  }
}

/**
 * The word around the caret, also when it spans literals with different formatting. A word is delimited by whitespace
 * and by anything that isn't a literal. A caret on or right before whitespace is not in a word.
 */
function wordAround(document: EditedDocument, focus: Focus): Segment[] {
  if (document.blocks[focus.blockIndex]?.type !== "PARAGRAPH") return [];
  const container = textOf(document, focus);
  const position = positionInText(focus);
  if (!container || !isLiteral(container[position])) return [];

  let first = position;
  while (isLiteral(container[first - 1])) first--;
  let last = position;
  while (isLiteral(container[last + 1])) last++;
  const run = container.slice(first, last + 1) as LiteralValue[];
  const runText = run.map((literal) => text(literal)).join("");
  const before = run.slice(0, position - first).reduce((sum, literal) => sum + text(literal).length, 0);
  const caret = before + Math.min(Math.max(focus.cursorPosition ?? 0, 0), text(container[position]).length);

  if (/\s/.test(runText[caret] ?? "")) return [];
  let wordStart = caret;
  while (wordStart > 0 && !/\s/.test(runText[wordStart - 1])) wordStart--;
  let wordEnd = caret;
  while (wordEnd < runText.length && !/\s/.test(runText[wordEnd])) wordEnd++;

  const segments: Segment[] = [];
  let start = 0;
  for (const literal of run) {
    const end = start + text(literal).length;
    const from = Math.max(wordStart, start) - start;
    const to = Math.min(wordEnd, end) - start;
    if (from < to) segments.push({ container, literal, from, to });
    start = end;
  }
  return segments;
}

/** Gives the segment's characters the font type, splitting the literal if only part of it is formatted. */
function applyFontType({ container, literal, from, to }: Draft<Segment>, fontType: FontType): Draft<Content>[] {
  const length = text(literal).length;
  const inserted: Draft<LiteralValue>[] = [];
  if (to < length) inserted.unshift(splitLiteralAtOffset(literal, to));
  const formatted = from > 0 ? splitLiteralAtOffset(literal, from) : literal;
  if (formatted !== literal) inserted.unshift(formatted);

  // The literal keeps its id and the split-off parts are new, like when a paragraph is split.
  formatted.editedFontType = fontType === formatted.fontType ? null : fontType;
  container.splice(container.indexOf(literal) + 1, 0, ...inserted);
  return [literal, ...inserted];
}

/** Merges formatted literals with neighbours of the same font type, so repeated formatting doesn't fragment text. */
function mergeTouchedLiterals(container: Draft<Content[]>, touched: Set<Draft<Content>>) {
  for (let index = container.length - 1; index > 0; index--) {
    const first = container[index - 1];
    const second = container[index];
    if (!touched.has(first) && !touched.has(second)) continue;
    const merged = mergeLiteralsIfPossible(first, second);
    if (merged.length === 1) {
      container.splice(index - 1, 2, merged[0]);
      touched.add(merged[0]);
    }
  }
}

/** Orders literals by position, ignoring the cursor: negative if `a` comes first, 0 for the same literal. */
function compareLiterals(a: LiteralIndex, b: LiteralIndex) {
  const atStart = (index: LiteralIndex) => ({ ...index, cursorPosition: 0 });
  if (isIndexAfter(atStart(a), atStart(b))) return -1;
  return isIndexAfter(atStart(b), atStart(a)) ? 1 : 0;
}

function isSamePosition(focus: Focus, endpoint: SelectionIndex["start"]) {
  return compareLiterals(focus, endpoint) === 0 && focus.cursorPosition === endpoint.cursorPosition;
}

type SelectionEndpoint = SelectionIndex["start"];

/** The content a literal index points into: the paragraph itself, a list item or a table cell. */
function textOf(document: EditedDocument, index: LiteralIndex): Content[] | undefined {
  const block = document.blocks[index.blockIndex];
  if (!block) return undefined;
  const content = block.content[index.contentIndex];
  if (isTableCellIndex(index)) {
    if (!isTable(content)) return undefined;
    const cell =
      index.rowIndex === -1
        ? content.header.colSpec[index.cellIndex]?.headerContent
        : content.rows[index.rowIndex]?.cells[index.cellIndex];
    return cell?.text;
  }
  if (isItemContentIndex(index)) {
    return isItemList(content) ? content.items[index.itemIndex]?.content : undefined;
  }
  return block.content;
}

function positionInText(index: LiteralIndex): number {
  if (isTableCellIndex(index)) return index.cellContentIndex;
  if (isItemContentIndex(index)) return index.itemContentIndex;
  return index.contentIndex;
}

function withPositionInText(index: LiteralIndex, position: number): LiteralIndex {
  if (isTableCellIndex(index)) return { ...index, cellContentIndex: position };
  if (isItemContentIndex(index)) return { ...index, itemContentIndex: position };
  return { ...index, contentIndex: position };
}

// Lists and tables inside a paragraph count as one character, so offsets on either side of them stay apart.
const lengthOf = (content: Content) => (isTextContent(content) && !isNewLine(content) ? text(content).length : 1);

function offsetInText(document: EditedDocument, endpoint: Focus): number | undefined {
  const content = textOf(document, endpoint);
  if (!content || endpoint.cursorPosition === undefined) return undefined;
  const preceding = content.slice(0, positionInText(endpoint)).reduce((sum, item) => sum + lengthOf(item), 0);
  return preceding + endpoint.cursorPosition;
}

/** The literal at `offset`: for a start, the literal that begins there; for an end, the literal that ends there. */
function endpointAt(
  document: EditedDocument,
  index: LiteralIndex,
  offset: number,
  side: "start" | "end",
): SelectionEndpoint | undefined {
  const content = textOf(document, index);
  if (!content) return undefined;

  let fallback: SelectionEndpoint | undefined;
  let start = 0;
  for (const [position, item] of content.entries()) {
    const end = start + lengthOf(item);
    if (isLiteral(item) && offset >= start && offset <= end) {
      const endpoint = { ...withPositionInText(index, position), cursorPosition: offset - start };
      const isPreferred = side === "start" ? offset < end : offset > start;
      if (isPreferred) return endpoint;
      fallback ??= endpoint;
    }
    start = end;
  }
  return fallback;
}
