import { type ByteDecoder } from "~/Brevredigering/LetterEditor/actions/rtf/rtfDecoding";
import { NATIVE_DESTINATIONS } from "~/Brevredigering/LetterEditor/actions/rtf/rtfDestinations";
import {
  LEVEL_FORMAT_BULLET,
  LEVEL_FORMAT_NONE,
  type ListLevelFormats,
  parseListLevelFormats,
} from "~/Brevredigering/LetterEditor/actions/rtf/rtfListTable";
import {
  HEADING_BY_OUTLINE_LEVEL,
  type HeadingType,
  parseHeadingStyles,
} from "~/Brevredigering/LetterEditor/actions/rtf/rtfStylesheet";
import { type RtfControlToken, type RtfToken } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";
import { type RtfDestination, walkRtf } from "~/Brevredigering/LetterEditor/actions/rtf/walkRtf";
import {
  cleansePastedText,
  type ItemElement,
  mergeNeighbouringText,
  type TableCell,
  type TableRow,
  type Text,
  type TraversedElement,
} from "~/Brevredigering/LetterEditor/actions/traversedElement";
import { FontType, ListType } from "~/types/brevbakerTypes";

/**
 * Narrow RTF parser for pasting from Word, WordPad and similar. Supports paragraphs, headings,
 * bold/italic, bullet/numbered lists and simple tables, and produces the same `TraversedElement[]`
 * as the HTML paste path. Anything else is ignored.
 */

const NUMBERED_PN_WORDS: ReadonlySet<string> = new Set([
  "pnlvlbody",
  "pndec",
  "pnucltr",
  "pnlcltr",
  "pnucrm",
  "pnlcrm",
]);

/** "1.", "1.2", "a)", "(iv)", "IV." etc. Anything else (·, o, §, –) is a bullet. */
const NUMBERED_MARKER = /^\(?(\d+(\.\d+)*[.)]?|[a-zA-Z][.)]|[ivxlcdm]+[.)]|[IVXLCDM]+[.)])$/;

interface ParagraphProps {
  styleIndex?: number;
  outlineLevel?: number;
  listId: number;
  /** `\ilvl`, 0 for the top level. */
  listLevel: number;
  inTable: boolean;
}

/** RTF formatting is group scoped: `{` saves and `}` restores it. */
interface GroupState {
  bold: boolean;
  italic: boolean;
  hidden: boolean;
  deleted: boolean;
  paragraph: ParagraphProps;
}

interface TableBuilder {
  rows: TableRow[];
  headerCells?: TableCell[];
  cells: TableCell[];
  rowIsHeader: boolean;
  /** Between `\trowd` or a `\cell` and the `\row` that ends the row. */
  rowOpen: boolean;
}

interface ParseContext {
  headingStyles: ReadonlyMap<number, HeadingType>;
  listLevelFormats: ListLevelFormats;
  elements: TraversedElement[];
  groups: GroupState[];
  /** Text of the current paragraph or table cell. */
  text: Text[];
  listMarker?: string;
  pnListType?: ListType;
  /** `\pnlvlN` of a Word 95 list (`\*\pn`), where 1 is the top level. */
  pnLevel?: number;
  table?: TableBuilder;
  /** The last paragraph break, until visible text follows. `\par\sect` and `\page\par` are one break, not two. */
  lastBreak?: "par" | "sectionOrPage";
}

const defaultParagraphProps = (): ParagraphProps => ({ listId: 0, listLevel: 0, inTable: false });

const newTable = (): TableBuilder => ({ rows: [], cells: [], rowIsHeader: false, rowOpen: false });

function initialGroupState(): GroupState {
  return {
    bold: false,
    italic: false,
    hidden: false,
    deleted: false,
    paragraph: defaultParagraphProps(),
  };
}

const currentGroup = (ctx: ParseContext): GroupState => ctx.groups.at(-1)!;

function fontOf(group: GroupState): FontType {
  // Brevbaker text cannot be both bold and italic; bold wins, as in the HTML path.
  if (group.bold) return FontType.BOLD;
  if (group.italic) return FontType.ITALIC;
  return FontType.PLAIN;
}

function listTypeFromMarker(marker: string): ListType {
  return NUMBERED_MARKER.test(marker.replaceAll(/\s/g, "")) ? ListType.NUMMERERT_LISTE : ListType.PUNKTLISTE;
}

/** Collapses whitespace and merges same-font runs; trims the edges unless `trimEdges` is false. Returns [] if there's no visible text. */
function finalizeTextRun(items: Text[], trimEdges = true): Text[] {
  const merged = mergeNeighbouringText(items.map((item) => ({ ...item, text: cleansePastedText(item.text) })));
  if (merged.every((item) => item.text.trim().length === 0)) return [];
  if (!trimEdges) return merged;

  const lastIndex = merged.length - 1;
  return merged
    .map((item, index) => {
      let text = item.text;
      if (index === 0) text = text.trimStart();
      if (index === lastIndex) text = text.trimEnd();
      return { ...item, text };
    })
    .filter((item) => item.text.length > 0);
}

function resetParagraphMarkers(ctx: ParseContext) {
  ctx.text = [];
  ctx.listMarker = undefined;
  ctx.pnListType = undefined;
  ctx.pnLevel = undefined;
}

function headingOf(ctx: ParseContext, props: ParagraphProps): HeadingType | undefined {
  if (props.outlineLevel !== undefined) return HEADING_BY_OUTLINE_LEVEL[props.outlineLevel];
  return props.styleIndex === undefined ? undefined : ctx.headingStyles.get(props.styleIndex);
}

/** The number format of the paragraph's list level in the list table, if the list table defines it. */
function levelFormatOf(ctx: ParseContext, props: ParagraphProps): number | undefined {
  if (props.listId <= 0) return undefined;
  return ctx.listLevelFormats.get(props.listId)?.[props.listLevel];
}

function isListItem(ctx: ParseContext, props: ParagraphProps): boolean {
  const levelFormat = levelFormatOf(ctx, props);
  if (levelFormat !== undefined) return levelFormat !== LEVEL_FORMAT_NONE;
  return ctx.listMarker !== undefined || ctx.pnListType !== undefined || props.listId > 0;
}

/** From the list table, then `\pn`, then the marker text. The marker is a guess, for writers without list tables. */
function listTypeOf(ctx: ParseContext, props: ParagraphProps): ListType {
  const levelFormat = levelFormatOf(ctx, props);
  if (levelFormat !== undefined)
    return levelFormat === LEVEL_FORMAT_BULLET ? ListType.PUNKTLISTE : ListType.NUMMERERT_LISTE;
  if (ctx.pnListType !== undefined) return ctx.pnListType;
  return ctx.listMarker === undefined ? ListType.PUNKTLISTE : listTypeFromMarker(ctx.listMarker);
}

/** Items below the top level are flattened into the outer list, like nested `<ul>`/`<ol>` in the HTML path. */
function listItem(ctx: ParseContext, props: ParagraphProps, content: Text[]): ItemElement {
  const nested = props.listLevel > 0 || (ctx.pnLevel !== undefined && ctx.pnLevel > 1);
  return { type: "ITEM", content, listType: listTypeOf(ctx, props), ...(nested ? { nested } : {}) };
}

function flushTable(ctx: ParseContext) {
  const table = ctx.table;
  if (!table) return;
  if (table.cells.length > 0) table.rows.push({ cells: table.cells });
  if (table.rows.length > 0 || table.headerCells) {
    ctx.elements.push({
      type: "TABLE",
      rows: table.rows,
      ...(table.headerCells ? { headerCells: table.headerCells } : {}),
    });
  }
  ctx.table = undefined;
}

/** Inside a `\intbl` paragraph, or in a row that hasn't ended yet (some writers leave out `\intbl`). */
function isInTable(ctx: ParseContext): boolean {
  return currentGroup(ctx).paragraph.inTable || ctx.table?.rowOpen === true;
}

/** RTF has no "end of table" word: the table ends at the first paragraph without `\intbl` outside an open row. */
function endTableIfLeft(ctx: ParseContext) {
  if (ctx.table && !isInTable(ctx)) flushTable(ctx);
}

function flushParagraph(ctx: ParseContext) {
  endTableIfLeft(ctx);
  const props = currentGroup(ctx).paragraph;
  const content = finalizeTextRun(ctx.text);
  const heading = headingOf(ctx, props);

  if (content.length === 0) {
    // Blank lines are kept as empty paragraphs or list items, like `<p></p>` and `<li></li>` in the HTML path.
    const empty: Text[] = [{ type: "TEXT", font: FontType.PLAIN, text: "" }];
    if (!heading && isListItem(ctx, props)) ctx.elements.push(listItem(ctx, props, empty));
    else ctx.elements.push({ type: "P", content: empty });
  } else if (heading) {
    // Numbered headings ("1 Innledning") keep their number as text.
    const marker = ctx.listMarker?.trim();
    const prefix: Text[] = marker ? [{ type: "TEXT", font: FontType.PLAIN, text: `${marker} ` }] : [];
    ctx.elements.push({ type: heading, content: finalizeTextRun([...prefix, ...content]) });
  } else if (isListItem(ctx, props)) {
    ctx.elements.push(listItem(ctx, props, content));
  } else {
    ctx.elements.push({ type: "P", content });
  }

  resetParagraphMarkers(ctx);
}

function flushCell(ctx: ParseContext) {
  ctx.table ??= newTable();
  ctx.table.rowOpen = true;
  ctx.table.cells.push({ content: finalizeTextRun(ctx.text) });
  resetParagraphMarkers(ctx);
}

function flushRow(ctx: ParseContext) {
  const table = ctx.table;
  if (!table) return;
  table.rowOpen = false;
  if (table.cells.length === 0) return;
  if (table.rowIsHeader && table.rows.length === 0 && !table.headerCells) {
    table.headerCells = table.cells;
  } else {
    table.rows.push({ cells: table.cells });
  }
  table.cells = [];
  table.rowIsHeader = false;
}

function appendText(ctx: ParseContext, value: string, destination: RtfDestination) {
  if (value.length === 0) return;
  const group = currentGroup(ctx);

  if (destination === "listMarker") {
    ctx.listMarker = (ctx.listMarker ?? "") + value;
  } else if (destination === "body" && !group.hidden && !group.deleted) {
    const isWhitespace = value.trim().length === 0;
    if (isWhitespace && ctx.table && !isInTable(ctx)) return;
    endTableIfLeft(ctx);
    if (!isWhitespace) ctx.lastBreak = undefined;
    // Coalesce runs with the same font, so a `\u`-heavy paragraph doesn't become hundreds of runs to merge.
    const font = fontOf(group);
    const last = ctx.text.at(-1);
    if (last?.font === font) last.text += value;
    else ctx.text.push({ type: "TEXT", font, text: value });
  }
}

const isOn = (token: RtfControlToken) => !token.hasParam || token.param !== 0;

/** `\par`, and section and page breaks, which we can't represent, end the paragraph. */
function breakParagraph(ctx: ParseContext, kind: "par" | "sectionOrPage") {
  if (isInTable(ctx)) {
    // Paragraphs inside a cell are joined with a space.
    ctx.table ??= newTable();
    appendText(ctx, " ", "body");
    ctx.listMarker = undefined;
    ctx.pnListType = undefined;
    ctx.pnLevel = undefined;
    return;
  }
  const isEmpty = ctx.listMarker === undefined && ctx.text.every((item) => item.text.trim().length === 0);
  const repeatsBreak = ctx.lastBreak !== undefined && (ctx.lastBreak === "sectionOrPage" || kind === "sectionOrPage");
  if (!(isEmpty && repeatsBreak)) flushParagraph(ctx);
  ctx.lastBreak = kind;
}

function handleControlWord(ctx: ParseContext, token: RtfControlToken, destination: RtfDestination) {
  // Soft line breaks are not supported, so `\line` becomes a space.
  if (token.word === "line") {
    appendText(ctx, " ", destination);
    return;
  }
  if (destination === "pn") {
    if (token.word === "pnlvlblt") ctx.pnListType = ListType.PUNKTLISTE;
    else if (NUMBERED_PN_WORDS.has(token.word)) ctx.pnListType = ListType.NUMMERERT_LISTE;
    else if (token.word === "pnlvl" && token.hasParam) ctx.pnLevel = token.param;
    return;
  }
  if (destination !== "body") return;

  handleBodyControlWord(ctx, currentGroup(ctx), token);
}

function handleBodyControlWord(ctx: ParseContext, group: GroupState, token: RtfControlToken) {
  const props = group.paragraph;

  switch (token.word) {
    case "b": {
      group.bold = isOn(token);
      break;
    }
    case "i": {
      group.italic = isOn(token);
      break;
    }
    case "v": {
      group.hidden = isOn(token);
      break;
    }
    case "deleted": {
      group.deleted = isOn(token);
      break;
    }
    case "plain": {
      group.bold = false;
      group.italic = false;
      group.hidden = false;
      group.deleted = false;
      break;
    }
    case "pard": {
      group.paragraph = defaultParagraphProps();
      break;
    }
    case "s": {
      props.styleIndex = token.param;
      break;
    }
    case "outlinelevel": {
      props.outlineLevel = token.param;
      break;
    }
    case "ls": {
      props.listId = token.param;
      break;
    }
    case "ilvl": {
      props.listLevel = token.param;
      break;
    }
    case "intbl": {
      props.inTable = true;
      break;
    }
    case "par": {
      breakParagraph(ctx, "par");
      break;
    }
    case "sect":
    case "page": {
      breakParagraph(ctx, "sectionOrPage");
      break;
    }
    case "trowd": {
      // Word repeats `\trowd` before every `\row`, so this must not discard the row's cells.
      if (ctx.table) ctx.table.rowIsHeader = false;
      else ctx.table = newTable();
      ctx.table.rowOpen = true;
      break;
    }
    case "trhdr": {
      if (ctx.table) ctx.table.rowIsHeader = true;
      break;
    }
    case "cell": {
      flushCell(ctx);
      break;
    }
    case "nestcell": {
      appendText(ctx, " ", "body");
      break;
    }
    case "row": {
      flushRow(ctx);
      break;
    }
    default: {
      break;
    }
  }
}

/** Ends the document. An unterminated plain paragraph is returned as inline text, like a partial selection. */
function finish(ctx: ParseContext) {
  const props = currentGroup(ctx).paragraph;

  if (ctx.table && isInTable(ctx)) {
    if (ctx.text.some((item) => item.text.trim().length > 0)) flushCell(ctx);
    flushTable(ctx);
  } else {
    endTableIfLeft(ctx);
    const content = finalizeTextRun(ctx.text, false);
    if (content.length > 0) {
      if (headingOf(ctx, props) || isListItem(ctx, props)) flushParagraph(ctx);
      else ctx.elements.push(...content);
    }
  }

  const isBlank = (element: TraversedElement) =>
    (element.type === "P" || element.type === "ITEM") && element.content.every((item) => item.text.length === 0);
  while (ctx.elements.length > 0 && isBlank(ctx.elements[0])) ctx.elements.shift();
  while (ctx.elements.length > 0 && isBlank(ctx.elements.at(-1)!)) ctx.elements.pop();
}

/** Interprets native RTF into the `TraversedElement[]` used by the HTML paste path. Never throws on unknown input. */
export function interpretNativeRtf(tokens: readonly RtfToken[], decodeBytes: ByteDecoder): TraversedElement[] {
  const ctx: ParseContext = {
    headingStyles: parseHeadingStyles(tokens),
    listLevelFormats: parseListLevelFormats(tokens),
    elements: [],
    groups: [initialGroupState()],
    text: [],
  };

  const events = walkRtf(tokens, {
    destinations: NATIVE_DESTINATIONS,
    decodeBytes,
  });
  for (const event of events) {
    switch (event.kind) {
      case "groupStart": {
        const parent = currentGroup(ctx);
        ctx.groups.push({ ...parent, paragraph: { ...parent.paragraph } });
        break;
      }
      case "groupEnd": {
        ctx.groups.pop();
        break;
      }
      case "text": {
        appendText(ctx, event.value, event.destination);
        break;
      }
      case "control": {
        handleControlWord(ctx, event.token, event.destination);
        break;
      }
      case "documentEnd": {
        finish(ctx);
        return ctx.elements;
      }
    }
  }
  return ctx.elements;
}
