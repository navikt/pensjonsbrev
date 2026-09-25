import { type RtfToken } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";

/** Character decoding for RTF: symbol words, `\uN` escapes and `\'hh` bytes in the document's code page. */

/** Control words that stand for a single character. `\line` maps to a space since soft line breaks are not supported. */
export const RTF_SYMBOL_WORDS: ReadonlyMap<string, string> = new Map([
  ["line", " "],
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

function textDecoderLabel(codepage: number): string {
  if (codepage === 65_001) return "utf-8";
  if (codepage === 10_000) return "macintosh";
  return `windows-${codepage}`;
}

/** Decoder for `\'hh` bytes, using the document's `\ansicpgN` (default Windows-1252). */
export function createByteDecoder(rtf: string): ByteDecoder {
  const match = /\\ansicpg(\d+)/.exec(rtf);
  const codepage = match ? Number.parseInt(match[1], 10) : 1252;

  let decoder: TextDecoder;
  try {
    decoder = new TextDecoder(textDecoderLabel(codepage));
  } catch {
    decoder = new TextDecoder("windows-1252");
  }
  return (bytes) => decoder.decode(new Uint8Array(bytes));
}
