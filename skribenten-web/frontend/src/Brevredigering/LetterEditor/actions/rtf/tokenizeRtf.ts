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
