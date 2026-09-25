/**
 * Tokenizer and character decoding shared by the RTF parser (`paste-rtf.ts`) and the Outlook
 * HTML de-encapsulator (`paste-rtf-html.ts`).
 */

export type RtfControlToken = { type: "control"; word: string; hasParam: boolean; param: number };

export type RtfToken =
  | { type: "groupStart" }
  | { type: "groupEnd" }
  | RtfControlToken
  | { type: "text"; value: string }
  | { type: "hexByte"; byte: number };

const isLetter = (char: string | undefined) => char !== undefined && /[a-zA-Z]/.test(char);
const isDigit = (char: string | undefined) => char !== undefined && /[0-9]/.test(char);

function readControlWord(rtf: string, start: number): { token: RtfControlToken; end: number } {
  let wordEnd = start;
  while (isLetter(rtf[wordEnd])) wordEnd++;

  let paramEnd = rtf[wordEnd] === "-" ? wordEnd + 1 : wordEnd;
  const digitsStart = paramEnd;
  while (isDigit(rtf[paramEnd])) paramEnd++;
  const hasParam = paramEnd > digitsStart;
  if (!hasParam) paramEnd = wordEnd;

  const token: RtfControlToken = {
    type: "control",
    word: rtf.slice(start, wordEnd),
    hasParam,
    param: hasParam ? Number.parseInt(rtf.slice(wordEnd, paramEnd), 10) : 0,
  };
  // A single space delimits the control word and is part of it.
  return { token, end: rtf[paramEnd] === " " ? paramEnd + 1 : paramEnd };
}

/** Raw CR/LF in RTF source is insignificant; paragraph breaks are always explicit (`\par`). */
export function tokenizeRtf(rtf: string): RtfToken[] {
  const tokens: RtfToken[] = [];
  let index = 0;

  while (index < rtf.length) {
    const char = rtf[index];

    if (char === "{") {
      tokens.push({ type: "groupStart" });
      index++;
    } else if (char === "}") {
      tokens.push({ type: "groupEnd" });
      index++;
    } else if (char === "\\") {
      const next = rtf[index + 1];
      if (isLetter(next)) {
        const { token, end } = readControlWord(rtf, index + 1);
        index = end;
        if (token.word === "bin" && token.hasParam) {
          index += Math.max(0, token.param);
        } else {
          tokens.push(token);
        }
      } else if (next === "'") {
        const byte = Number.parseInt(rtf.slice(index + 2, index + 4), 16);
        tokens.push({ type: "hexByte", byte: Number.isNaN(byte) ? 0x3f : byte });
        index += 4;
      } else {
        const symbol = readControlSymbol(next);
        if (symbol !== undefined) tokens.push(symbol);
        index += 2;
      }
    } else if (char === "\r" || char === "\n") {
      index++;
    } else {
      let end = index;
      while (end < rtf.length && !"\\{}\r\n".includes(rtf[end])) end++;
      tokens.push({ type: "text", value: rtf.slice(index, end) });
      index = end;
    }
  }

  return tokens;
}

function readControlSymbol(symbol: string | undefined): RtfToken | undefined {
  switch (symbol) {
    case "\\":
    case "{":
    case "}": {
      return { type: "text", value: symbol };
    }
    case "~": {
      return { type: "text", value: "\u00A0" };
    }
    case "_": {
      return { type: "text", value: "-" };
    }
    case "*": {
      return { type: "control", word: "*", hasParam: false, param: 0 };
    }
    case "\r":
    case "\n": {
      return { type: "control", word: "par", hasParam: false, param: 0 };
    }
    default: {
      // `\-` (optional hyphen), `\|`, `\:` and unknown symbols have no visible text.
      return undefined;
    }
  }
}

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
