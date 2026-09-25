export type RtfToken =
  | { type: "groupStart" }
  | { type: "groupEnd" }
  | { type: "controlWord"; name: string; param?: number }
  | { type: "controlSymbol"; symbol: string }
  | { type: "hexEscape"; byte: number }
  | { type: "text"; text: string };

/**
 * Splits an RTF document into lexical tokens. Destinations, unicode escapes and
 * code pages are interpreted later, in `walkRtfContent`.
 */
export function tokenizeRtf(input: string): RtfToken[] {
  const tokens: RtfToken[] = [];
  let index = 0;
  let textBuffer = "";

  const flushText = () => {
    if (textBuffer.length > 0) {
      tokens.push({ type: "text", text: textBuffer });
      textBuffer = "";
    }
  };

  while (index < input.length) {
    const char = input[index];

    if (char === "{" || char === "}") {
      flushText();
      tokens.push({ type: char === "{" ? "groupStart" : "groupEnd" });
      index++;
    } else if (char === "\\") {
      flushText();
      const next = input[index + 1];
      if (next === undefined) break;

      if (/[a-zA-Z]/.test(next)) {
        index = consumeControlWord(input, index + 1, tokens);
      } else if (next === "'") {
        const byte = Number.parseInt(input.slice(index + 2, index + 4), 16);
        if (!Number.isNaN(byte)) tokens.push({ type: "hexEscape", byte });
        index += 4;
      } else {
        tokens.push({ type: "controlSymbol", symbol: next });
        index += 2;
      }
    } else if (char === "\r" || char === "\n") {
      // Line breaks in the RTF source carry no meaning.
      index++;
    } else {
      textBuffer += char;
      index++;
    }
  }

  flushText();
  return tokens;
}

// Reads a control word starting at its first letter and returns the index after it.
function consumeControlWord(input: string, start: number, tokens: RtfToken[]): number {
  const [, name, rawParam, delimiter] = /^([a-zA-Z]+)(-?\d+)?( ?)/.exec(input.slice(start, start + 64))!;
  const param = rawParam === undefined ? undefined : Number.parseInt(rawParam, 10);
  const end = start + name.length + (rawParam?.length ?? 0);

  if (name === "bin" && param !== undefined && param > 0) {
    // \binN is followed directly by N bytes of binary data, with no delimiter.
    return end + param;
  }

  tokens.push({ type: "controlWord", name, param });
  return end + delimiter.length;
}
