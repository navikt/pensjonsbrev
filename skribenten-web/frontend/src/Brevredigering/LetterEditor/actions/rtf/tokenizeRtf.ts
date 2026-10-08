/** Splits RTF source into tokens. Decoding text and bytes is left to the reader (`walkRtf.ts`). */

export type RtfControlToken = { type: "control"; word: string; hasParam: boolean; param: number };

export type RtfToken =
  | { type: "groupStart" }
  | { type: "groupEnd" }
  | RtfControlToken
  | { type: "text"; value: string }
  | { type: "hexByte"; byte: number };

const isLetter = (char: string | undefined) => char !== undefined && /[a-zA-Z]/.test(char);
const isDigit = (char: string | undefined) => char !== undefined && /[0-9]/.test(char);
const isHexDigit = (char: string | undefined) => char !== undefined && /[0-9a-fA-F]/.test(char);

/** Parameters are signed 32-bit numbers, so more than 10 digits is malformed. */
const MAX_PARAM_DIGITS = 10;
const INT32_MIN = -(2 ** 31);
const INT32_MAX = 2 ** 31 - 1;

/** Parses a control word parameter as a signed 32-bit number, or undefined if it is malformed. */
function parseParam(source: string): number | undefined {
  if (source.replace("-", "").length > MAX_PARAM_DIGITS) return undefined;
  return Math.min(INT32_MAX, Math.max(INT32_MIN, Number.parseInt(source, 10)));
}

function readControlWord(rtf: string, start: number): { token: RtfControlToken; end: number } {
  let wordEnd = start;
  while (isLetter(rtf[wordEnd])) wordEnd++;

  let paramEnd = rtf[wordEnd] === "-" ? wordEnd + 1 : wordEnd;
  const digitsStart = paramEnd;
  while (isDigit(rtf[paramEnd])) paramEnd++;
  if (paramEnd === digitsStart) paramEnd = wordEnd;
  const param = paramEnd > wordEnd ? parseParam(rtf.slice(wordEnd, paramEnd)) : undefined;

  const token: RtfControlToken = {
    type: "control",
    word: rtf.slice(start, wordEnd),
    hasParam: param !== undefined,
    param: param ?? 0,
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
          // Binary data running past the end means the clipboard was truncated; keep what came before it.
          if (token.param > rtf.length - index) break;
          index += Math.max(0, token.param);
        } else {
          tokens.push(token);
        }
      } else if (next === "'") {
        // A malformed escape becomes "?" and consumes only its valid hex digits, so it never yields a control character.
        let digitsEnd = index + 2;
        while (digitsEnd < index + 4 && isHexDigit(rtf[digitsEnd])) digitsEnd++;
        const byte = digitsEnd === index + 4 ? Number.parseInt(rtf.slice(index + 2, digitsEnd), 16) : 0x3f;
        tokens.push({ type: "hexByte", byte });
        index = digitsEnd;
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
