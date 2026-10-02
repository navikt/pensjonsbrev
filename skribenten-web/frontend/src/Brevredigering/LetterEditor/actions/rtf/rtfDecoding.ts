import { type RtfToken } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";

/** Character decoding for RTF: symbol words, `\uN` escapes and `\'hh` bytes in the document's code page. */

/** Control words that stand for a single character. `\line` is left to the consumers, which treat it differently. */
export const RTF_SYMBOL_WORDS: ReadonlyMap<string, string> = new Map([
  ["tab", "\t"],
  ["lquote", "\u2018"],
  ["rquote", "\u2019"],
  ["ldblquote", "\u201C"],
  ["rdblquote", "\u201D"],
  ["endash", "\u2013"],
  ["emdash", "\u2014"],
  ["bullet", "\u2022"],
  ["emspace", " "],
  ["enspace", " "],
  ["qmspace", " "],
]);

/** Decodes `\uN`, whose parameter is a signed 16-bit value. */
export function decodeUnicodeParam(param: number): string {
  return String.fromCharCode(param < 0 ? param + 65_536 : param);
}

/**
 * After `\uN` a reader skips `\ucN` fallback units (a character, `\'hh` or control word each);
 * group boundaries end the skip. Returns what is left of the token, if anything.
 */
export function skipUnicodeFallback(remaining: number, token: RtfToken): { remaining: number; rest?: RtfToken } {
  switch (token.type) {
    case "text": {
      const skipped = Math.min(remaining, token.value.length);
      const value = token.value.slice(skipped);
      return { remaining: remaining - skipped, rest: value.length > 0 ? { type: "text", value } : undefined };
    }
    case "hexByte":
    case "control": {
      return { remaining: remaining - 1 };
    }
    default: {
      return { remaining: 0, rest: token };
    }
  }
}

export type ByteDecoder = (bytes: number[]) => string;

const DEFAULT_ENCODING = "windows-1252";

/** Windows code pages (`\ansicpgN`) mapped to WHATWG Encoding labels understood by `TextDecoder`. */
const ENCODING_BY_CODEPAGE: ReadonlyMap<number, string> = new Map([
  [874, "windows-874"],
  ...[1250, 1251, 1252, 1253, 1254, 1255, 1256, 1257, 1258].map((cp): [number, string] => [cp, `windows-${cp}`]),
  [932, "shift_jis"],
  [936, "gbk"],
  [949, "euc-kr"],
  [950, "big5"],
  [10_000, "macintosh"],
  [20_866, "koi8-r"],
  [21_866, "koi8-u"],
  ...[1, 2, 3, 4, 5, 6, 7, 8, 9, 13, 15].map((n): [number, string] => [28_590 + n, `iso-8859-${n}`]),
  [65_001, "utf-8"],
]);

/** The first `\ansicpgN` in the header, i.e. before any visible text. */
function headerCodepage(tokens: readonly RtfToken[]): number | undefined {
  for (const token of tokens) {
    if (token.type === "control" && token.word === "ansicpg" && token.hasParam) return token.param;
    if (token.type === "hexByte" || (token.type === "text" && token.value.trim().length > 0)) return undefined;
  }
  return undefined;
}

/** Decoder for `\'hh` bytes, using the document's `\ansicpgN` (default Windows-1252). */
export function createByteDecoder(tokens: readonly RtfToken[]): ByteDecoder {
  const codepage = headerCodepage(tokens);
  const label = (codepage !== undefined && ENCODING_BY_CODEPAGE.get(codepage)) || DEFAULT_ENCODING;

  let decoder: TextDecoder;
  try {
    decoder = new TextDecoder(label);
  } catch {
    decoder = new TextDecoder(DEFAULT_ENCODING);
  }
  return (bytes) => decoder.decode(new Uint8Array(bytes));
}
