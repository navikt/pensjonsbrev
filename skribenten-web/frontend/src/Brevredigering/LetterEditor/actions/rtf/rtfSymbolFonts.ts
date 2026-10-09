import { type RtfToken } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";

/**
 * Symbol fonts (Symbol, Wingdings, Zapf Dingbats) draw their own glyphs for byte values, so `\'b7` in
 * Symbol is a bullet, not "·", and "a" is "α". Text in these fonts is mapped to Unicode by byte value.
 * Bytes we have no mapping for are decoded as usual. Word for Mac writes these characters as `\uN` in
 * the private use area U+F020–U+F0FF, with the byte value in the low byte.
 *
 * The Symbol table and the Wingdings subset are from rtf-stream-parser (MIT, Mazira LLC),
 * https://github.com/mazira/rtf-stream-parser/blob/f112deb2cd93797a2dbc28aa38d8e143b86a79d4/src/decode.ts,
 * with U+20AC at 0xA0 as in the Windows Symbol font. Zapf Dingbats follows Unicode's ZDINGBAT.TXT.
 * Webdings is not mapped: its glyphs are pictures we can't put in a letter anyway.
 */

/** Unicode for a byte in a symbol font, or undefined if we don't know it. */
type SymbolTable = (byte: number) => string | undefined;

const tableFromString =
  (first: number, characters: string): SymbolTable =>
  (byte) => {
    const character = characters[byte - first];
    return character === undefined || character === "\0" ? undefined : character;
  };

const tableFromMap =
  (map: ReadonlyMap<number, number>): SymbolTable =>
  (byte) => {
    const codePoint = map.get(byte);
    return codePoint === undefined ? undefined : String.fromCodePoint(codePoint);
  };

/** 0x20–0x7E and 0xA0–0xFE. 0xF0 is unassigned. */
const SYMBOL_LOW = tableFromString(
  0x20,
  " !\u2200#\u2203%&\u220B()\u2217+,\u2212./0123456789:;<=>?" +
    "\u2245\u0391\u0392\u03A7\u0394\u0395\u03A6\u0393\u0397\u0399\u03D1\u039A\u039B\u039C\u039D\u039F" +
    "\u03A0\u0398\u03A1\u03A3\u03A4\u03A5\u03C2\u03A9\u039E\u03A8\u0396[\u2234]\u22A5_" +
    "\u203E\u03B1\u03B2\u03C7\u03B4\u03B5\u03C6\u03B3\u03B7\u03B9\u03D5\u03BA\u03BB\u03BC\u03BD\u03BF" +
    "\u03C0\u03B8\u03C1\u03C3\u03C4\u03C5\u03D6\u03C9\u03BE\u03C8\u03B6{|}\u223C",
);
const SYMBOL_HIGH = tableFromString(
  0xa0,
  "\u20AC\u03D2\u2032\u2264\u2044\u221E\u0192\u2663\u2666\u2665\u2660\u2194\u2190\u2191\u2192\u2193" +
    "\u00B0\u00B1\u2033\u2265\u00D7\u221D\u2202\u2022\u00F7\u2260\u2261\u2248\u2026\u23D0\u23AF\u21B5" +
    "\u2135\u2111\u211C\u2118\u2297\u2295\u2205\u2229\u222A\u2283\u2287\u2284\u2282\u2286\u2208\u2209" +
    "\u2220\u2207\u00AE\u00A9\u2122\u220F\u221A\u22C5\u00AC\u2227\u2228\u21D4\u21D0\u21D1\u21D2\u21D3" +
    "\u25CA\u2329\u00AE\u00A9\u2122\u2211\u239B\u239C\u239D\u23A1\u23A2\u23A3\u23A7\u23A8\u23A9\u23AA" +
    "\0\u232A\u222B\u2320\u23AE\u2321\u239E\u239F\u23A0\u23A4\u23A5\u23A6\u23AB\u23AC\u23AD",
);
const symbol: SymbolTable = (byte) => SYMBOL_LOW(byte) ?? SYMBOL_HIGH(byte);

/** Bullets, boxes, check marks and arrows that Word uses for list bullets and check boxes. */
const wingdings = tableFromMap(
  new Map([
    [0x6c, 0x25_cf],
    [0x6e, 0x25_a0],
    [0x6f, 0x25_a1],
    [0x71, 0x27_51],
    [0x72, 0x27_52],
    [0x73, 0x2b_27],
    [0x74, 0x29_eb],
    [0x75, 0x25_c6],
    [0x76, 0x27_56],
    [0x77, 0x2b_25],
    [0x9e, 0x00_b7],
    [0x9f, 0x20_22],
    [0xa0, 0x25_aa],
    [0xa1, 0x26_aa],
    [0xa4, 0x25_c9],
    [0xa5, 0x25_ce],
    [0xa7, 0x25_aa],
    [0xa8, 0x25_fb],
    [0xaa, 0x27_26],
    [0xab, 0x26_05],
    [0xd7, 0x2b_98],
    [0xd8, 0x2b_9a],
    [0xd9, 0x2b_99],
    [0xda, 0x2b_9b],
    [0xef, 0x21_e6],
    [0xf0, 0x21_e8],
    [0xfb, 0x1_f5_f6],
    [0xfc, 0x27_14],
    [0xfd, 0x1_f5_f7],
    [0xfe, 0x1_f5_f9],
  ]),
);

const wingdings2 = tableFromMap(
  new Map([
    [0x50, 0x27_13],
    [0x52, 0x26_11],
    [0x53, 0x26_12],
    [0x54, 0x26_12],
    [0x97, 0x26_ab],
    [0x98, 0x2b_24],
    [0x9f, 0x25_fe],
    [0xa0, 0x25_a0],
    [0xa1, 0x25_fc],
    [0xa2, 0x2b_1b],
    [0xa8, 0x25_a3],
  ]),
);

const wingdings3 = tableFromMap(
  new Map([
    [0x71, 0x25_bc],
    [0x72, 0x25_b3],
    [0x73, 0x25_bd],
    [0x74, 0x25_c4],
    [0x75, 0x25_ba],
    [0x76, 0x25_c1],
    [0x77, 0x25_b7],
    [0x81, 0x25_b2],
  ]),
);

/** Glyphs that were in Unicode before the Dingbats block got them, and runs outside the block. */
const ZAPF_DINGBATS_EXCEPTIONS: ReadonlyMap<number, number> = new Map([
  [0x25, 0x26_0e],
  [0x2a, 0x26_1b],
  [0x2b, 0x26_1e],
  [0x48, 0x26_05],
  [0x6c, 0x25_cf],
  [0x6e, 0x25_a0],
  [0x73, 0x25_b2],
  [0x74, 0x25_bc],
  [0x75, 0x25_c6],
  [0x77, 0x25_d7],
  [0xa8, 0x26_63],
  [0xa9, 0x26_66],
  [0xaa, 0x26_65],
  [0xab, 0x26_60],
  [0xd5, 0x21_92],
  [0xd6, 0x21_94],
  [0xd7, 0x21_95],
]);

const zapfDingbats: SymbolTable = (byte) => {
  const exception = ZAPF_DINGBATS_EXCEPTIONS.get(byte);
  if (exception !== undefined) return String.fromCodePoint(exception);
  if (byte >= 0x21 && byte <= 0x7e) return String.fromCodePoint(0x27_00 + byte - 0x20);
  if (byte >= 0x80 && byte <= 0x8d) return String.fromCodePoint(0x27_68 + byte - 0x80);
  if (byte >= 0xac && byte <= 0xb5) return String.fromCodePoint(0x24_60 + byte - 0xac);
  if (byte >= 0xa1 && byte <= 0xfe && byte !== 0xf0) return String.fromCodePoint(0x27_00 + byte - 0x40);
  return undefined;
};

/** By font name, lower case without spaces. */
const SYMBOL_TABLES: ReadonlyMap<string, SymbolTable> = new Map([
  ["symbol", symbol],
  ["wingdings", wingdings],
  ["wingdings2", wingdings2],
  ["wingdings3", wingdings3],
  ["zapfdingbats", zapfDingbats],
  ["itczapfdingbats", zapfDingbats],
]);

export interface SymbolFonts {
  /** `\deffN`: the font of text before any `\fN`, and after `\plain`. */
  defaultFont?: number;
  /** Symbol tables by font number (`\fN`), for fonts we know. */
  tables: ReadonlyMap<number, SymbolTable>;
}

/** The first `\deffN` in the header, i.e. before any visible text. */
function defaultFontOf(tokens: readonly RtfToken[]): number | undefined {
  for (const token of tokens) {
    if (token.type === "control" && token.word === "deff" && token.hasParam) return token.param;
    if (token.type === "hexByte" || (token.type === "text" && token.value.trim().length > 0)) return undefined;
  }
  return undefined;
}

/**
 * Pre-scans `{\fonttbl{\f3\fnil\fcharset2 Symbol;}…}` for symbol fonts. Entries may be groups or follow
 * each other directly; text in nested groups (`{\*\panose …}`, `{\*\falt …}`) is not part of the name.
 */
export function parseSymbolFonts(tokens: readonly RtfToken[]): SymbolFonts {
  const tables = new Map<number, SymbolTable>();
  const start = tokens.findIndex((token) => token.type === "control" && token.word === "fonttbl");
  if (start === -1) return { defaultFont: defaultFontOf(tokens), tables };

  let depth = 1;
  let entry: { font: number; depth: number; name: string } | undefined;
  const endEntry = () => {
    const table = entry && SYMBOL_TABLES.get(entry.name.replaceAll(/\s/g, "").toLowerCase());
    if (entry && table) tables.set(entry.font, table);
    entry = undefined;
  };

  for (let index = start + 1; index < tokens.length && depth > 0; index++) {
    const token = tokens[index];
    if (token.type === "groupStart") {
      depth++;
    } else if (token.type === "groupEnd") {
      if (entry?.depth === depth) endEntry();
      depth--;
    } else if (token.type === "control" && token.word === "f" && token.hasParam && entry?.depth !== depth) {
      entry = { font: token.param, depth, name: "" };
    } else if (token.type === "text" && entry?.depth === depth) {
      const [name, ...rest] = token.value.split(";");
      entry.name += name;
      if (rest.length > 0) endEntry();
    }
  }
  return { defaultFont: defaultFontOf(tokens), tables };
}

/** Maps text in a symbol font by byte value; characters above 0xFF other than U+F020–U+F0FF are kept. */
export function mapSymbolText(table: SymbolTable, text: string, decodeByte: (byte: number) => string): string {
  let result = "";
  for (const character of text) {
    const code = character.codePointAt(0)!;
    const byte = code >= 0xf0_20 && code <= 0xf0_ff ? code - 0xf0_00 : code;
    if (byte > 0xff) result += character;
    else result += table(byte) ?? (byte === code ? character : decodeByte(byte));
  }
  return result;
}

/** Maps `\'hh` bytes in a symbol font. Bytes without a mapping are decoded in the document's code page. */
export function mapSymbolBytes(table: SymbolTable, bytes: number[], decodeBytes: (bytes: number[]) => string): string {
  return bytes.map((byte) => table(byte) ?? decodeBytes([byte])).join("");
}
