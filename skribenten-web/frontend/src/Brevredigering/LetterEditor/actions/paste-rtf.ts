import {
  cleansePastedText,
  mergeNeighbouringText,
  type TableCell,
  type TableRow,
  type Text,
  type TraversedElement,
} from "~/Brevredigering/LetterEditor/actions/paste-elements";
import {
  type ByteDecoder,
  createByteDecoder,
  decodeUnicodeParam,
  RTF_SYMBOL_WORDS,
  type RtfControlToken,
  type RtfToken,
  skipUnicodeFallback,
  tokenizeRtf,
} from "~/Brevredigering/LetterEditor/actions/paste-rtf-tokenizer";
import { FontType, ListType } from "~/types/brevbakerTypes";

/**
 * Narrow RTF parser for pasting from Word, WordPad and similar. Supports paragraphs, headings,
 * bold/italic, bullet/numbered lists and simple tables, and produces the same `TraversedElement[]`
 * as the HTML paste path. Anything else is ignored.
 */

type HeadingType = "H1" | "H2" | "H3";

const HEADING_BY_OUTLINE_LEVEL: readonly HeadingType[] = ["H1", "H2", "H3"];

/** Fallback for writers that set no `\outlinelevel` on heading styles. */
const HEADING_STYLE_NAMES: ReadonlyMap<string, HeadingType> = new Map([
  ["heading 1", "H1"],
  ["heading 2", "H2"],
  ["heading 3", "H3"],
  ["overskrift 1", "H1"],
  ["overskrift 2", "H2"],
  ["overskrift 3", "H3"],
]);

/** Destinations without visible document text. Unknown `\*` destinations are skipped too. */
const SKIPPED_DESTINATIONS: ReadonlySet<string> = new Set([
  "annotation",
  "atnauthor",
  "atndate",
  "atnid",
  "colortbl",
  "fldinst",
  "fonttbl",
  "footer",
  "footerf",
  "footerl",
  "footerr",
  "footnote",
  "header",
  "headerf",
  "headerl",
  "headerr",
  "info",
  "listoverridetable",
  "listtable",
  "nonesttables",
  "nonshppict",
  "object",
  "pict",
  "pnseclvl",
  "pntxta",
  "pntxtb",
  "shpinst",
  "shprslt",
  "stylesheet",
  "tc",
  "xe",
]);

const LIST_MARKER_DESTINATIONS: ReadonlySet<string> = new Set(["listtext", "pntext"]);

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

/**
 * - `body`: document content
 * - `skip`: no text or formatting is read
 * - `listMarker`: `\listtext`/`\pntext`, the rendered bullet or number of a list item
 * - `pn`: old-style `\pn` list properties of the current paragraph
 */
type Destination = "body" | "skip" | "listMarker" | "pn";

interface ParagraphProps {
  styleIndex?: number;
  outlineLevel?: number;
  listId: number;
  inTable: boolean;
}

/** RTF formatting is group scoped: `{` saves and `}` restores it. */
interface GroupState {
  destination: Destination;
  /** Set by `\*`: the destination word that follows is skipped unless we know it. */
  ignorableIfUnknown: boolean;
  bold: boolean;
  italic: boolean;
  hidden: boolean;
  deleted: boolean;
  ucSkip: number;
  paragraph: ParagraphProps;
}

interface TableBuilder {
  rows: TableRow[];
  headerCells?: TableCell[];
  cells: TableCell[];
  rowIsHeader: boolean;
}

interface ParseContext {
  decodeBytes: ByteDecoder;
  headingStyles: ReadonlyMap<number, HeadingType>;
  elements: TraversedElement[];
  groups: GroupState[];
  /** Text of the current paragraph or table cell. */
  text: Text[];
  pendingBytes: number[];
  /** Fallback units left to skip after `\uN`. */
  unicodeSkip: number;
  listMarker?: string;
  pnListType?: ListType;
  table?: TableBuilder;
}

const defaultParagraphProps = (): ParagraphProps => ({ listId: 0, inTable: false });

const newTable = (): TableBuilder => ({ rows: [], cells: [], rowIsHeader: false });

function initialGroupState(): GroupState {
  return {
    destination: "body",
    ignorableIfUnknown: false,
    bold: false,
    italic: false,
    hidden: false,
    deleted: false,
    ucSkip: 1,
    paragraph: defaultParagraphProps(),
  };
}

const currentGroup = (ctx: ParseContext): GroupState => ctx.groups.at(-1)!;

function headingFromStyleName(name: string): HeadingType | undefined {
  // Style names may carry aliases, e.g. "heading 1,Overskrift 1".
  for (const alias of name.split(",")) {
    const heading = HEADING_STYLE_NAMES.get(alias.trim().toLowerCase());
    if (heading) return heading;
  }
  return undefined;
}

/** Maps paragraph style index (`\sN`) to heading level, from `\outlinelevel` or the style name. */
function parseHeadingStyles(tokens: RtfToken[]): Map<number, HeadingType> {
  const headingStyles = new Map<number, HeadingType>();
  const start = tokens.findIndex((token) => token.type === "control" && token.word === "stylesheet");
  if (start === -1) return headingStyles;

  let depth = 1;
  let entry: { styleIndex?: number; outlineLevel?: number; name: string } | undefined;

  for (let index = start + 1; index < tokens.length && depth > 0; index++) {
    const token = tokens[index];
    if (token.type === "groupStart") {
      depth++;
      if (depth === 2) entry = { name: "" };
    } else if (token.type === "groupEnd") {
      if (depth === 2 && entry?.styleIndex !== undefined) {
        const heading =
          entry.outlineLevel === undefined
            ? headingFromStyleName(entry.name.split(";")[0])
            : HEADING_BY_OUTLINE_LEVEL[entry.outlineLevel];
        if (heading) headingStyles.set(entry.styleIndex, heading);
      }
      depth--;
    } else if (depth === 2 && entry) {
      if (token.type === "text") {
        entry.name += token.value;
      } else if (token.type === "control") {
        // Character (`\*\csN`) and table (`\*\tsN`) styles are not paragraph styles.
        if (token.word === "*") entry = undefined;
        else if (token.word === "s" && token.hasParam) entry.styleIndex = token.param;
        else if (token.word === "outlinelevel" && token.hasParam) entry.outlineLevel = token.param;
      }
    }
  }

  return headingStyles;
}

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
}

function headingOf(ctx: ParseContext, props: ParagraphProps): HeadingType | undefined {
  if (props.outlineLevel !== undefined) return HEADING_BY_OUTLINE_LEVEL[props.outlineLevel];
  return props.styleIndex === undefined ? undefined : ctx.headingStyles.get(props.styleIndex);
}

function isListItem(ctx: ParseContext, props: ParagraphProps): boolean {
  return ctx.listMarker !== undefined || ctx.pnListType !== undefined || props.listId > 0;
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

/** RTF has no "end of table" word: the table ends at the first paragraph without `\intbl`. */
function endTableIfLeft(ctx: ParseContext) {
  if (ctx.table && !currentGroup(ctx).paragraph.inTable) flushTable(ctx);
}

function flushParagraph(ctx: ParseContext) {
  endTableIfLeft(ctx);
  const props = currentGroup(ctx).paragraph;
  const content = finalizeTextRun(ctx.text);
  const heading = headingOf(ctx, props);

  if (content.length === 0) {
    // Blank lines are kept as empty paragraphs, like `<p></p>` in the HTML path.
    ctx.elements.push({ type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "" }] });
  } else if (heading) {
    // Numbered headings ("1 Innledning") keep their number as text.
    const marker = ctx.listMarker?.trim();
    const prefix: Text[] = marker ? [{ type: "TEXT", font: FontType.PLAIN, text: `${marker} ` }] : [];
    ctx.elements.push({ type: heading, content: finalizeTextRun([...prefix, ...content]) });
  } else if (isListItem(ctx, props)) {
    const listType =
      ctx.listMarker === undefined ? (ctx.pnListType ?? ListType.PUNKTLISTE) : listTypeFromMarker(ctx.listMarker);
    ctx.elements.push({ type: "ITEM", content, listType });
  } else {
    ctx.elements.push({ type: "P", content });
  }

  resetParagraphMarkers(ctx);
}

function flushCell(ctx: ParseContext) {
  ctx.table ??= newTable();
  ctx.table.cells.push({ content: finalizeTextRun(ctx.text) });
  resetParagraphMarkers(ctx);
}

function flushRow(ctx: ParseContext) {
  const table = ctx.table;
  if (!table || table.cells.length === 0) return;
  if (table.rowIsHeader && table.rows.length === 0 && !table.headerCells) {
    table.headerCells = table.cells;
  } else {
    table.rows.push({ cells: table.cells });
  }
  table.cells = [];
  table.rowIsHeader = false;
}

function appendText(ctx: ParseContext, value: string) {
  if (value.length === 0) return;
  const group = currentGroup(ctx);

  if (group.destination === "listMarker") {
    ctx.listMarker = (ctx.listMarker ?? "") + value;
  } else if (group.destination === "body" && !group.hidden && !group.deleted) {
    const isWhitespace = value.trim().length === 0;
    if (isWhitespace && ctx.table && !group.paragraph.inTable) return;
    endTableIfLeft(ctx);
    ctx.text.push({ type: "TEXT", font: fontOf(group), text: value });
  }
}

function flushPendingBytes(ctx: ParseContext) {
  if (ctx.pendingBytes.length === 0) return;
  const decoded = ctx.decodeBytes(ctx.pendingBytes);
  ctx.pendingBytes = [];
  appendText(ctx, decoded);
}

/** Handles the word right after `\*`, and destination words that open a new destination. */
function enterDestination(group: GroupState, word: string): boolean {
  if (group.ignorableIfUnknown) {
    group.ignorableIfUnknown = false;
    group.destination = word === "pn" ? "pn" : "skip";
    return true;
  }
  if (SKIPPED_DESTINATIONS.has(word)) {
    group.destination = "skip";
    return true;
  }
  if (LIST_MARKER_DESTINATIONS.has(word)) {
    group.destination = "listMarker";
    return true;
  }
  if (word === "pn") {
    group.destination = "pn";
    return true;
  }
  return false;
}

const isOn = (token: RtfControlToken) => !token.hasParam || token.param !== 0;

function handleControlWord(ctx: ParseContext, token: RtfControlToken) {
  const group = currentGroup(ctx);
  if (group.destination === "skip") return;

  if (token.word === "*") {
    group.ignorableIfUnknown = true;
    return;
  }
  if (enterDestination(group, token.word)) return;

  if (token.word === "u") {
    appendText(ctx, decodeUnicodeParam(token.param));
    ctx.unicodeSkip = group.ucSkip;
    return;
  }
  const symbol = RTF_SYMBOL_WORDS.get(token.word);
  if (symbol !== undefined) {
    appendText(ctx, symbol);
    return;
  }

  if (group.destination === "pn") {
    if (token.word === "pnlvlblt") ctx.pnListType = ListType.PUNKTLISTE;
    else if (NUMBERED_PN_WORDS.has(token.word)) ctx.pnListType = ListType.NUMMERERT_LISTE;
    return;
  }
  if (group.destination !== "body") return;

  handleBodyControlWord(ctx, group, token);
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
    case "uc": {
      group.ucSkip = Math.max(0, token.param);
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
    case "intbl": {
      props.inTable = true;
      break;
    }
    case "par": {
      if (props.inTable) {
        // Paragraphs inside a cell are joined with a space.
        ctx.table ??= newTable();
        appendText(ctx, " ");
        ctx.listMarker = undefined;
        ctx.pnListType = undefined;
      } else {
        flushParagraph(ctx);
      }
      break;
    }
    case "trowd": {
      // Word repeats `\trowd` before every `\row`, so this must not discard the row's cells.
      if (ctx.table) ctx.table.rowIsHeader = false;
      else ctx.table = newTable();
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
      appendText(ctx, " ");
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
  flushPendingBytes(ctx);
  const props = currentGroup(ctx).paragraph;

  if (ctx.table && props.inTable) {
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
    element.type === "P" && element.content.every((item) => item.text.length === 0);
  while (ctx.elements.length > 0 && isBlank(ctx.elements[0])) ctx.elements.shift();
  while (ctx.elements.length > 0 && isBlank(ctx.elements.at(-1)!)) ctx.elements.pop();
}

/** Parses RTF into the `TraversedElement[]` used by the HTML paste path. Never throws on unknown input. */
export function parseRtfToTraversedElements(rtf: string): TraversedElement[] {
  const tokens = tokenizeRtf(rtf);
  const ctx: ParseContext = {
    decodeBytes: createByteDecoder(rtf),
    headingStyles: parseHeadingStyles(tokens),
    elements: [],
    groups: [initialGroupState()],
    text: [],
    pendingBytes: [],
    unicodeSkip: 0,
  };

  for (const rawToken of tokens) {
    if (rawToken.type !== "hexByte") flushPendingBytes(ctx);

    let token: RtfToken | undefined = rawToken;
    if (ctx.unicodeSkip > 0) {
      const skip = skipUnicodeFallback(ctx.unicodeSkip, rawToken);
      ctx.unicodeSkip = skip.remaining;
      token = skip.rest;
    }
    if (!token) continue;

    switch (token.type) {
      case "groupStart": {
        const parent = currentGroup(ctx);
        ctx.groups.push({ ...parent, ignorableIfUnknown: false, paragraph: { ...parent.paragraph } });
        break;
      }
      case "groupEnd": {
        // Closing the `{\rtf1 …}` group ends the document; trailing bytes are ignored.
        if (ctx.groups.length === 2) {
          finish(ctx);
          return ctx.elements;
        }
        if (ctx.groups.length > 1) ctx.groups.pop();
        break;
      }
      case "hexByte": {
        ctx.pendingBytes.push(token.byte);
        break;
      }
      case "text": {
        if (currentGroup(ctx).ignorableIfUnknown) currentGroup(ctx).destination = "skip";
        appendText(ctx, token.value);
        break;
      }
      case "control": {
        handleControlWord(ctx, token);
        break;
      }
    }
  }

  finish(ctx);
  return ctx.elements;
}
