import {
  mergeNeighbouringText,
  type TableCell,
  type TableRow,
  type Text,
  type TraversedElement,
} from "~/Brevredigering/LetterEditor/actions/paste-elements";
import { FontType, ListType } from "~/types/brevbakerTypes";

/**
 * Custom, narrow RTF (Rich Text Format) parser.
 *
 * RTF is a plain-text, brace-delimited, control-word based format. We intentionally do not pull in
 * a full-fidelity RTF library — those target complete round-tripping and are heavyweight for our
 * needs. Instead this module implements a small hand-rolled tokenizer + state-machine parser
 * tailored only to the subset of RTF we need to support: paragraphs, headings (via Word style
 * names), bold/italic, bullet/numbered lists, and simple (non-merged) tables.
 *
 * The parser produces the same `TraversedElement[]` shape the HTML clipboard pipeline
 * (`paste.ts`) produces, so all downstream insertion logic (`insertTraversedElements` and
 * friends) is completely format-agnostic and reused unmodified.
 */

// ---------------------------------------------------------------------------
// 1. Tokenizer
// ---------------------------------------------------------------------------

type RtfToken =
  | { type: "groupStart" }
  | { type: "groupEnd" }
  | { type: "control"; word: string; hasParam: boolean; param: number }
  | { type: "text"; value: string }
  | { type: "hexByte"; byte: number };

/**
 * Single-pass scanner producing a flat token stream from raw RTF source.
 */
function tokenizeRtf(rtf: string): RtfToken[] {
  const tokens: RtfToken[] = [];
  let index = 0;
  const length = rtf.length;

  while (index < length) {
    const char = rtf[index];

    if (char === "{") {
      tokens.push({ type: "groupStart" });
      index++;
    } else if (char === "}") {
      tokens.push({ type: "groupEnd" });
      index++;
    } else if (char === "\\") {
      index++;
      const next = rtf[index];

      if (next === "'") {
        // \'hh - hex-escaped byte
        const hex = rtf.slice(index + 1, index + 3);
        const byte = Number.parseInt(hex, 16);
        tokens.push({ type: "hexByte", byte: Number.isNaN(byte) ? 0 : byte });
        index += 3;
      } else if (next === "\\" || next === "{" || next === "}") {
        // escaped literal char
        tokens.push({ type: "text", value: next });
        index++;
      } else if (next === "~") {
        // non-breaking space
        tokens.push({ type: "text", value: "\u00A0" });
        index++;
      } else if (next === "-" || next === "_") {
        // optional hyphen / non-breaking hyphen: represent as plain hyphen so text stays readable
        tokens.push({ type: "text", value: "-" });
        index++;
      } else if (next === "\r" || next === "\n") {
        // \<newline> is a legacy alias for \par in some RTF writers
        tokens.push({ type: "control", word: "par", hasParam: false, param: 0 });
        index++;
      } else if (next !== undefined && /[a-zA-Z]/.test(next)) {
        // control word: letters, optional numeric (possibly negative) parameter,
        // terminated by a single space (consumed) or any other non-alnum char (not consumed).
        let wordEnd = index;
        while (wordEnd < length && /[a-zA-Z]/.test(rtf[wordEnd])) wordEnd++;
        const word = rtf.slice(index, wordEnd);

        let paramEnd = wordEnd;
        let hasParam = false;
        if (rtf[paramEnd] === "-") paramEnd++;
        const paramDigitsStart = paramEnd;
        while (paramEnd < length && /[0-9]/.test(rtf[paramEnd])) paramEnd++;
        if (paramEnd > paramDigitsStart) hasParam = true;
        const param = hasParam ? Number.parseInt(rtf.slice(wordEnd, paramEnd), 10) : 0;

        index = paramEnd;
        // A single trailing space terminates the control word and is consumed (not emitted as text).
        if (rtf[index] === " ") index++;

        tokens.push({ type: "control", word, hasParam, param });
      } else if (next === "*") {
        tokens.push({ type: "control", word: "*", hasParam: false, param: 0 });
        index++;
      } else {
        // Unrecognized escape - skip the backslash and following char defensively.
        index++;
      }
    } else if (char === "\r" || char === "\n" || char === "\t") {
      // Raw whitespace in the RTF *source* is just formatting for readability of the .rtf file
      // itself and does not represent document content - real paragraph/tab breaks are always
      // explicit control words (\par, \tab).
      index++;
    } else {
      // Plain text run: consume until the next backslash/brace/raw-whitespace.
      let end = index;
      while (
        end < length &&
        rtf[end] !== "\\" &&
        rtf[end] !== "{" &&
        rtf[end] !== "}" &&
        rtf[end] !== "\r" &&
        rtf[end] !== "\n"
      ) {
        end++;
      }
      tokens.push({ type: "text", value: rtf.slice(index, end) });
      index = end;
    }
  }

  return tokens;
}

// ---------------------------------------------------------------------------
// 2. Codepage decoding (for \'hh hex-escaped bytes)
// ---------------------------------------------------------------------------

// Windows-1252: bytes 0xA0-0xFF map 1:1 to the same Unicode code point (Latin-1 supplement).
// Bytes 0x80-0x9F have specific overrides; a handful are officially undefined - we fall back to
// the byte's own value (as a C1 control code point) rather than silently dropping data.
const CP1252_OVERRIDES = new Map<number, number>([
  [0x80, 0x20ac],
  [0x82, 0x201a],
  [0x83, 0x0192],
  [0x84, 0x201e],
  [0x85, 0x2026],
  [0x86, 0x2020],
  [0x87, 0x2021],
  [0x88, 0x02c6],
  [0x89, 0x2030],
  [0x8a, 0x0160],
  [0x8b, 0x2039],
  [0x8c, 0x0152],
  [0x8e, 0x017d],
  [0x91, 0x2018],
  [0x92, 0x2019],
  [0x93, 0x201c],
  [0x94, 0x201d],
  [0x95, 0x2022],
  [0x96, 0x2013],
  [0x97, 0x2014],
  [0x98, 0x02dc],
  [0x99, 0x2122],
  [0x9a, 0x0161],
  [0x9b, 0x203a],
  [0x9c, 0x0153],
  [0x9e, 0x017e],
  [0x9f, 0x0178],
]);

// Windows-1257 (Baltic): 0x00-0x7F is ASCII, 0x80-0xFF needs a full explicit table (unlike 1252 it
// is not a simple Latin-1 extension). Source: unicode.org/Public/MAPPINGS/VENDORS/MICSFT/WINDOWS/CP1257.TXT
const CP1257_HIGH = new Map<number, number>([
  [0x80, 0x20ac],
  [0x82, 0x201a],
  [0x84, 0x201e],
  [0x85, 0x2026],
  [0x86, 0x2020],
  [0x87, 0x2021],
  [0x89, 0x2030],
  [0x8b, 0x2039],
  [0x8d, 0x00a8],
  [0x8e, 0x02c7],
  [0x8f, 0x00b8],
  [0x91, 0x2018],
  [0x92, 0x2019],
  [0x93, 0x201c],
  [0x94, 0x201d],
  [0x95, 0x2022],
  [0x96, 0x2013],
  [0x97, 0x2014],
  [0x99, 0x2122],
  [0x9b, 0x203a],
  [0x9d, 0x00af],
  [0x9e, 0x02db],
  [0xa0, 0x00a0],
  [0xa2, 0x00a2],
  [0xa3, 0x00a3],
  [0xa4, 0x00a4],
  [0xa6, 0x00a6],
  [0xa7, 0x00a7],
  [0xa8, 0x00d8],
  [0xa9, 0x00a9],
  [0xaa, 0x0156],
  [0xab, 0x00ab],
  [0xac, 0x00ac],
  [0xad, 0x00ad],
  [0xae, 0x00ae],
  [0xaf, 0x00c6],
  [0xb0, 0x00b0],
  [0xb1, 0x00b1],
  [0xb2, 0x00b2],
  [0xb3, 0x00b3],
  [0xb4, 0x00b4],
  [0xb5, 0x00b5],
  [0xb6, 0x00b6],
  [0xb7, 0x00b7],
  [0xb8, 0x00f8],
  [0xb9, 0x00b9],
  [0xba, 0x0157],
  [0xbb, 0x00bb],
  [0xbc, 0x00bc],
  [0xbd, 0x00bd],
  [0xbe, 0x00be],
  [0xbf, 0x00e6],
  [0xc0, 0x0104],
  [0xc1, 0x012e],
  [0xc2, 0x0100],
  [0xc3, 0x0106],
  [0xc4, 0x00c4],
  [0xc5, 0x00c5],
  [0xc6, 0x0118],
  [0xc7, 0x0112],
  [0xc8, 0x010c],
  [0xc9, 0x00c9],
  [0xca, 0x0179],
  [0xcb, 0x0116],
  [0xcc, 0x0122],
  [0xcd, 0x0136],
  [0xce, 0x012a],
  [0xcf, 0x013b],
  [0xd0, 0x0160],
  [0xd1, 0x0143],
  [0xd2, 0x0145],
  [0xd3, 0x00d3],
  [0xd4, 0x014c],
  [0xd5, 0x00d5],
  [0xd6, 0x00d6],
  [0xd7, 0x00d7],
  [0xd8, 0x0172],
  [0xd9, 0x0141],
  [0xda, 0x015a],
  [0xdb, 0x016a],
  [0xdc, 0x00dc],
  [0xdd, 0x017b],
  [0xde, 0x017d],
  [0xdf, 0x00df],
  [0xe0, 0x0105],
  [0xe1, 0x012f],
  [0xe2, 0x0101],
  [0xe3, 0x0107],
  [0xe4, 0x00e4],
  [0xe5, 0x00e5],
  [0xe6, 0x0119],
  [0xe7, 0x0113],
  [0xe8, 0x010d],
  [0xe9, 0x00e9],
  [0xea, 0x017a],
  [0xeb, 0x0117],
  [0xec, 0x0123],
  [0xed, 0x0137],
  [0xee, 0x012b],
  [0xef, 0x013c],
  [0xf0, 0x0161],
  [0xf1, 0x0144],
  [0xf2, 0x0146],
  [0xf3, 0x00f3],
  [0xf4, 0x014d],
  [0xf5, 0x00f5],
  [0xf6, 0x00f6],
  [0xf7, 0x00f7],
  [0xf8, 0x0173],
  [0xf9, 0x0142],
  [0xfa, 0x015b],
  [0xfb, 0x016b],
  [0xfc, 0x00fc],
  [0xfd, 0x017c],
  [0xfe, 0x017e],
  [0xff, 0x02d9],
]);

type CodepageDecoder = (byte: number) => number;

const decodeCp1252: CodepageDecoder = (byte) => (byte >= 0xa0 ? byte : (CP1252_OVERRIDES.get(byte) ?? byte));
const decodeCp1257: CodepageDecoder = (byte) => (byte < 0x80 ? byte : (CP1257_HIGH.get(byte) ?? byte));

const CODEPAGE_DECODERS = new Map<number, CodepageDecoder>([
  [1252, decodeCp1252],
  [1257, decodeCp1257],
]);

/** Reads `\ansicpgN` from the document header to pick a decode table; defaults to 1252. */
function detectCodepageDecoder(rtf: string): CodepageDecoder {
  const match = /\\ansicpg(\d+)/.exec(rtf);
  const codepage = match ? Number.parseInt(match[1], 10) : 1252;
  return CODEPAGE_DECODERS.get(codepage) ?? decodeCp1252;
}

// ---------------------------------------------------------------------------
// 3. Document language (telemetry only - does not affect parsing behavior)
// ---------------------------------------------------------------------------

const LCID_LABELS = new Map<number, string>([
  [1033, "en-US"],
  [2057, "en-GB"],
  [1044, "nb-NO"],
  [2068, "nn-NO"],
]);

/** Best-effort LCID -> friendly label mapping, used only for pasteTracking.ts telemetry. */
export function detectRtfDocumentLanguage(rtf: string): string | undefined {
  const defLangMatch = /\\deflang(\d+)/.exec(rtf);
  const langMatch = defLangMatch ?? /\\lang(\d+)/.exec(rtf);
  if (!langMatch) return undefined;

  const lcid = Number.parseInt(langMatch[1], 10);
  return LCID_LABELS.get(lcid) ?? `unknown (${lcid})`;
}

// ---------------------------------------------------------------------------
// 4. Stylesheet parsing (for heading detection via Word style names)
// ---------------------------------------------------------------------------

const HEADING_STYLE_NAMES: Record<string, "H1" | "H2" | "H3"> = {
  "heading 1": "H1",
  "overskrift 1": "H1",
  "heading 2": "H2",
  "overskrift 2": "H2",
  "heading 3": "H3",
  "overskrift 3": "H3",
};

function normalizeStyleName(name: string): string {
  return name.trim().toLowerCase();
}

/**
 * Parses the `{\stylesheet {\s1 heading 1;} {\s2 heading 2;} ...}` destination group into a
 * style-index -> heading-level map. Only style entries matching our fixed allowlist (English and
 * Norwegian bokmål/nynorsk) are kept; anything else is treated as a plain paragraph downstream.
 */
function parseStylesheet(tokens: RtfToken[]): Map<number, "H1" | "H2" | "H3"> {
  const headingStyles = new Map<number, "H1" | "H2" | "H3">();

  const stylesheetStart = findStylesheetGroupStart(tokens);
  if (stylesheetStart === undefined) return headingStyles;

  let index = stylesheetStart;
  let depth = 0;
  let currentStyleIndex: number | undefined;
  let currentName = "";

  while (index < tokens.length) {
    const token = tokens[index];

    if (token.type === "groupStart") {
      depth++;
      currentStyleIndex = undefined;
      currentName = "";
    } else if (token.type === "groupEnd") {
      depth--;
      if (depth === 0) break;
    } else if (token.type === "control" && token.word === "s" && token.hasParam) {
      currentStyleIndex = token.param;
    } else if (token.type === "text") {
      currentName += token.value;
      if (currentName.includes(";") && currentStyleIndex !== undefined) {
        const name = normalizeStyleName(currentName.split(";")[0]);
        const level = HEADING_STYLE_NAMES[name];
        if (level) headingStyles.set(currentStyleIndex, level);
        currentStyleIndex = undefined;
      }
    }

    index++;
  }

  return headingStyles;
}

function findStylesheetGroupStart(tokens: RtfToken[]): number | undefined {
  for (let index = 0; index < tokens.length; index++) {
    const token = tokens[index];
    if (token.type === "control" && token.word === "stylesheet") {
      // Walk backwards to the "{" that opens this destination group.
      for (let back = index - 1; back >= 0; back--) {
        if (tokens[back].type === "groupStart") return back;
      }
    }
  }
  return undefined;
}

// ---------------------------------------------------------------------------
// 5. Ignorable destination groups
// ---------------------------------------------------------------------------

// Destinations whose text content must never be emitted into the document. Matches the RTF `\*`
// "ignorable if unknown" convention plus a fixed list of well-known non-content destinations. This
// is the RTF-side equivalent of what DOMPurify does for the HTML clipboard path.
const IGNORABLE_DESTINATIONS = new Set([
  "fonttbl",
  "colortbl",
  "stylesheet",
  "generator",
  "info",
  "pict",
  "object",
  "footer",
  "footerf",
  "footerl",
  "footerr",
  "header",
  "headerf",
  "headerl",
  "headerr",
  "themedata",
  "colorschememapping",
  "latentstyles",
  "rsid",
  "rsidtbl",
  "xmlnstbl",
  "listtable",
  "listoverridetable",
]);

// ---------------------------------------------------------------------------
// 6. State-machine parser
// ---------------------------------------------------------------------------

interface ParserState {
  bold: boolean;
  italic: boolean;
  ucSkip: number;
  /** True if this group (or an ancestor) is a destination whose content must not be emitted. */
  ignorable: boolean;
  /**
   * True if this group (or an ancestor) is a destination whose control words must not be read as
   * list-item signals either - namely the global list/style *definition* tables
   * (`\listtable`/`\listoverridetable`), which legitimately contain `\pndec`/`\pnlvlblt`/`\ls` for
   * every list style defined in the document, not just the paragraph currently being built.
   */
  suppressListSignals: boolean;
  /** \sN style index currently active for the paragraph being built. */
  styleIndex?: number;
}

function initialState(): ParserState {
  return { bold: false, italic: false, ucSkip: 1, ignorable: false, suppressListSignals: false };
}

/**
 * Parses RTF source into the shared `TraversedElement[]` intermediate representation used by the
 * HTML clipboard pipeline. Unsupported/unrecognized RTF constructs are ignored rather than
 * throwing, mirroring the fail-soft behavior of the HTML path (DOMPurify silently strips unknown
 * tags/attributes).
 */
export function parseRtfToTraversedElements(rtf: string): TraversedElement[] {
  const tokens = tokenizeRtf(rtf);
  const decodeByte = detectCodepageDecoder(rtf);
  const headingStyles = parseStylesheet(tokens);

  const elements: TraversedElement[] = [];
  const stateStack: ParserState[] = [initialState()];

  // Paragraph-scoped accumulation
  let paragraphBuffer: Text[] = [];
  let pendingHexBytes: number[] = [];
  let paragraphIsListItem = false;
  let paragraphListType: ListType | undefined;

  // Table-scoped accumulation
  let inTable = false;
  let currentTableRows: TableRow[] = [];
  let currentRowCells: TableCell[] = [];
  let currentCellBuffer: Text[] = [];
  let sawTableRowInDocument = false;

  const currentState = () => stateStack.at(-1)!;
  const currentFont = (): FontType => {
    const state = currentState();
    // Text in brevbaker-brev cannot be both bold and italic simultaneously; bold takes precedence
    // if both are somehow active, mirroring the HTML path's "first change away from plain wins".
    if (state.bold) return FontType.BOLD;
    if (state.italic) return FontType.ITALIC;
    return FontType.PLAIN;
  };

  const flushPendingHexBytes = () => {
    if (pendingHexBytes.length === 0) return;
    const decoded = pendingHexBytes.map((byte) => String.fromCodePoint(decodeByte(byte))).join("");
    pendingHexBytes = [];
    pushText(decoded);
  };

  const pushText = (value: string) => {
    if (value.length === 0) return;
    const font = currentFont();
    if (inTable) {
      currentCellBuffer.push({ type: "TEXT", font, text: value });
    } else {
      paragraphBuffer.push({ type: "TEXT", font, text: value });
    }
  };

  /**
   * Insignificant whitespace between RTF destinations (e.g. a stray space right after a `}` that
   * closed an ignorable destination) commonly ends up as leading/trailing whitespace on the first
   * or last surviving text run of a paragraph/cell. Trim it so pasted content doesn't start or end
   * with a stray space, mirroring how the HTML pipeline never sees such artifacts (DOM whitespace
   * between tags isn't significant either).
   */
  const trimEdgeWhitespace = (items: Text[]): Text[] => {
    if (items.length === 0) return items;
    const trimmed = [...items];
    const first = trimmed[0];
    trimmed[0] = { ...first, text: first.text.replace(/^\s+/, "") };
    const lastIndex = trimmed.length - 1;
    const last = trimmed[lastIndex];
    trimmed[lastIndex] = { ...last, text: last.text.replace(/\s+$/, "") };
    return trimmed;
  };

  /**
   * Prepares an accumulated run of text items for a paragraph or table cell: merges neighbouring
   * same-font runs, trims insignificant leading/trailing whitespace at the edges, and drops the
   * whole thing if it turns out to contain no real (non-whitespace) content. Deliberately does NOT
   * drop whitespace-only items in the *middle* of otherwise-real content - unlike the HTML path,
   * RTF format-toggle control words (`\b`, `\i`, ...) routinely leave a single meaningful space as
   * its own text run, and dropping it would silently glue two words together (e.g. "fet"+"kursiv"
   * instead of "fet kursiv").
   */
  const finalizeTextRun = (items: Text[]): Text[] => {
    const merged = mergeNeighbouringText(items);
    const hasRealContent = merged.some((item) => item.type !== "TEXT" || item.text.trim().length > 0);
    if (!hasRealContent) return [];

    return trimEdgeWhitespace(merged).filter((item) => item.type !== "TEXT" || item.text.length > 0);
  };

  const flushCell = () => {
    flushPendingHexBytes();
    const merged = finalizeTextRun(currentCellBuffer);
    currentRowCells.push({ content: merged });
    currentCellBuffer = [];
  };

  const flushRow = () => {
    if (currentRowCells.length > 0) {
      currentTableRows.push({ cells: currentRowCells });
    }
    currentRowCells = [];
  };

  const flushTable = () => {
    if (currentTableRows.length > 0) {
      elements.push({ type: "TABLE", rows: currentTableRows });
    }
    currentTableRows = [];
    inTable = false;
    sawTableRowInDocument = false;
  };

  const flushParagraph = () => {
    flushPendingHexBytes();
    const merged = finalizeTextRun(paragraphBuffer);

    if (merged.length > 0) {
      const styleIndex = currentState().styleIndex;
      const headingLevel = styleIndex === undefined ? undefined : headingStyles.get(styleIndex);

      if (paragraphIsListItem) {
        elements.push({ type: "ITEM", content: merged, listType: paragraphListType });
      } else if (headingLevel === "H1") {
        elements.push({ type: "H1", content: merged });
      } else if (headingLevel === "H2") {
        elements.push({ type: "H2", content: merged });
      } else if (headingLevel === "H3") {
        elements.push({ type: "H3", content: merged });
      } else {
        elements.push({ type: "P", content: merged });
      }
    }

    paragraphBuffer = [];
    paragraphIsListItem = false;
    paragraphListType = undefined;
    currentState().styleIndex = undefined;
  };

  let tokenIndex = 0;
  while (tokenIndex < tokens.length) {
    const token = tokens[tokenIndex];
    switch (token.type) {
      case "groupStart": {
        stateStack.push({ ...currentState() });
        break;
      }
      case "groupEnd": {
        if (stateStack.length > 1) stateStack.pop();
        break;
      }
      case "hexByte": {
        if (currentState().ignorable) break;
        pendingHexBytes.push(token.byte);
        break;
      }
      case "text": {
        if (currentState().ignorable) break;
        flushPendingHexBytes();
        pushText(token.value);
        break;
      }
      case "control": {
        if (token.word === "u") {
          tokenIndex = handleUnicodeEscape(token, tokens, tokenIndex);
          continue;
        }
        handleControlWord(token);
        break;
      }
    }
    tokenIndex++;
  }

  // Flush any trailing content that wasn't terminated by an explicit \par. Only flush a
  // cell/row here if one was actually left open (i.e. the document ended mid-table without a
  // final \cell/\row) - otherwise this would append a spurious empty trailing row.
  if (inTable) {
    if (currentCellBuffer.length > 0) flushCell();
    if (currentRowCells.length > 0) flushRow();
    flushTable();
  } else {
    flushParagraph();
  }

  return elements;

  /**
   * Handles `\uN` (Unicode code point escape). Per the RTF spec, `\uN` is followed by exactly
   * `\ucM` "fallback" character units (default M=1) intended for readers without Unicode support;
   * since we do support Unicode, we skip over those M following character units (a plain text
   * char or a single `\'hh` hex escape each count as one unit; a nested group/other control word
   * ends the skip early). Returns the token index to resume parsing from.
   */
  function handleUnicodeEscape(
    token: Extract<RtfToken, { type: "control" }>,
    allTokens: RtfToken[],
    index: number,
  ): number {
    const state = currentState();
    if (!state.ignorable) {
      flushPendingHexBytes();
      // RTF's \u takes a signed 16-bit value; negative values represent code points >= 0x8000.
      const codePoint = token.param < 0 ? token.param + 65_536 : token.param;
      pushText(String.fromCodePoint(codePoint));
    }

    let skipRemaining = state.ucSkip;
    let cursor = index + 1;
    while (skipRemaining > 0 && cursor < allTokens.length) {
      const next = allTokens[cursor];
      if (next.type === "text") {
        // A text run may contain multiple fallback characters; only consume as many as needed.
        if (next.value.length <= skipRemaining) {
          skipRemaining -= next.value.length;
          cursor++;
        } else {
          allTokens[cursor] = { type: "text", value: next.value.slice(skipRemaining) };
          skipRemaining = 0;
        }
      } else if (next.type === "hexByte") {
        skipRemaining--;
        cursor++;
      } else {
        // Group boundaries and other control words end the fallback skip early.
        break;
      }
    }

    return cursor;
  }

  /**
   * Looks ahead from a `\pard` at `pardIndex` to determine whether the paragraph it opens is a
   * table cell (an `\intbl` follows, possibly after other paragraph-property control words) or a
   * real, non-table paragraph (real content, a group boundary, or the next `\par`/`\pard` appears
   * first). Used only to decide whether an already-open table has ended.
   */
  function pardStartsTableCell(pardIndex: number): boolean {
    for (let index = pardIndex + 1; index < tokens.length; index++) {
      const next = tokens[index];
      if (next.type === "control") {
        if (next.word === "intbl") return true;
        if (next.word === "par" || next.word === "pard") return false;
        // Other paragraph-property control words (alignment, indentation, tab stops, ...)
        // commonly precede \intbl - keep scanning past those.
        continue;
      }
      if (next.type === "text" && next.value.trim().length === 0) continue;
      // Real text/hex content or a group boundary before any \intbl - not a table cell.
      return false;
    }
    return false;
  }

  function handleControlWord(token: Extract<RtfToken, { type: "control" }>) {
    const state = currentState();

    if (token.word === "*") {
      // Marks the destination opened by this group as "ignorable if unknown" - the RTF spec's
      // generic safety net for destinations we don't explicitly recognize. Note this does NOT
      // suppress list-item signal detection (see below): the legacy `{\*\pn ...}` destination is
      // marked ignorable exactly this way, yet still needs its `\pnlvlblt`/`\pndec` read.
      state.ignorable = true;
      return;
    }

    if (IGNORABLE_DESTINATIONS.has(token.word)) {
      // Well-known non-content destinations (font/color/style tables, headers/footers, list
      // definitions, document metadata, etc). Note \stylesheet's \sN entries reuse the same
      // control word as a paragraph's style index (\sN) - marking this destination ignorable here
      // (separately from the up-front parseStylesheet pass) prevents those definition-time \sN
      // occurrences from being misread as a real paragraph's style below. `\listtable` /
      // `\listoverridetable` additionally suppress list-item *signal* detection (not just text):
      // those destinations define every list style used anywhere in the document, so the
      // `\pndec`/`\pnlvlblt`/`\ls` control words appearing inside them describe list styles in the
      // abstract, not "the paragraph currently being built".
      state.ignorable = true;
      if (token.word === "listtable" || token.word === "listoverridetable") state.suppressListSignals = true;
      return;
    }

    // List-item signal detection: deliberately evaluated before the `ignorable` early-return
    // below, since the (reliable, per-paragraph) old-style `\pntext`/`{\*\pn ...}` markers are
    // themselves inside destinations we mark ignorable for *text* purposes. Gated on
    // `suppressListSignals` rather than `ignorable` so only the global list-definition tables are
    // excluded.
    if (!state.suppressListSignals) {
      switch (token.word) {
        case "pntext":
        case "pnlvlblt": {
          paragraphIsListItem = true;
          if (paragraphListType === undefined) paragraphListType = ListType.PUNKTLISTE;
          break;
        }
        case "pndec":
        case "pnlvlbody": {
          paragraphIsListItem = true;
          // \pndec/\pnlvlbody is a more specific signal than the bullet default above (relevant
          // when both appear on the same old-style `{\pntext\pndec ...}` marker) - let it win.
          paragraphListType = ListType.NUMMERERT_LISTE;
          break;
        }
        case "ls": {
          // Presence of a list-id on a paragraph is a reliable signal it's a list item, even
          // without an explicit \pntext/\pndec (modern Word only emits \ls + \ilvl). We cannot
          // tell bullet vs numbered from \ls alone (that requires resolving \listoverridetable ->
          // \listtable -> \levelnfc, which we intentionally don't implement) - default to bullet,
          // the far more common case for pasted letter content.
          paragraphIsListItem = true;
          if (paragraphListType === undefined) paragraphListType = ListType.PUNKTLISTE;
          break;
        }
        default: {
          break;
        }
      }
    }

    if (token.word === "pntext") {
      // Old-style (pre-\ls) Word list marker: `{\pntext\'B7\tab}` (or `{\pntext\pndec 1.\tab}` for
      // numbered lists) immediately precedes the real item text, in its own (non-\*-prefixed)
      // destination. The list signal itself was already recorded above; mark the destination
      // ignorable now so the literal bullet glyph/number/tab that follows isn't emitted as
      // document text.
      state.ignorable = true;
    }

    if (state.ignorable) {
      // Any other control word encountered while inside an ignorable destination must not affect
      // document state (character formatting, table/heading tracking, etc).
      return;
    }

    switch (token.word) {
      case "b": {
        if (!token.hasParam || token.param !== 0) state.bold = true;
        else state.bold = false;
        break;
      }
      case "i": {
        if (!token.hasParam || token.param !== 0) state.italic = true;
        else state.italic = false;
        break;
      }
      case "uc": {
        state.ucSkip = token.param;
        break;
      }
      case "s": {
        if (token.hasParam) state.styleIndex = token.param;
        break;
      }
      case "pard": {
        // \pard opens a new paragraph's formatting and precedes every paragraph, table cell or
        // not. There is no explicit "table ended" control word in RTF - real writers signal it
        // implicitly by simply not following this \pard with \intbl. Peek ahead to tell those two
        // cases apart, so a trailing real paragraph's text is routed to the paragraph buffer
        // instead of leaking into the (by-then-stale) table-cell buffer.
        if (inTable && currentCellBuffer.length === 0 && !pardStartsTableCell(tokenIndex)) {
          flushTable();
        }
        break;
      }
      case "par": {
        if (inTable) {
          // A hard paragraph break inside a cell; treat the accumulated text as a line-break
          // within the cell rather than ending the cell (RTF doesn't nest tables in cells here).
          currentCellBuffer.push({ type: "TEXT", font: FontType.PLAIN, text: " " });
        } else {
          flushParagraph();
        }
        break;
      }
      case "intbl": {
        inTable = true;
        sawTableRowInDocument = true;
        break;
      }
      case "trowd": {
        inTable = true;
        sawTableRowInDocument = true;
        currentRowCells = [];
        break;
      }
      case "cell": {
        flushCell();
        break;
      }
      case "row": {
        flushRow();
        break;
      }
      case "tab": {
        pushText("\t");
        break;
      }
      default: {
        // Unrecognized control word - ignore. If we're not inside a table and see a run of plain
        // paragraphs after a table ended, flush the accumulated table so it isn't merged with
        // trailing document content.
        if (!inTable && sawTableRowInDocument && currentTableRows.length > 0 && token.word !== "row") {
          flushTable();
        }
        break;
      }
    }
  }
}
