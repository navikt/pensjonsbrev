export type RtfToken =
  | { type: "groupStart" }
  | { type: "groupEnd" }
  | { type: "controlWord"; name: string; param?: number }
  | { type: "controlSymbol"; symbol: string }
  | { type: "hexEscape"; byte: number }
  | { type: "text"; text: string };

/**
 * Tokenizes an RTF document into a flat stream of low-level tokens: group
 * boundaries (`{`/`}`), control words (`\word`, optionally with a signed
 * numeric parameter, e.g. `\b1`, `\ilvl2`), control symbols (single
 * non-letter escapes such as `\~`, `\_`, `\-`, `\\`, `\{`, `\}`), `\'hh`
 * hex-escaped bytes, and literal text runs.
 *
 * This only performs lexical tokenization. Grouping/destination semantics,
 * unicode-escape fallback skipping, and codepage decoding of hex-escaped
 * bytes are handled by the higher-level RTF interpreters that consume this
 * token stream (see `parseRtf.ts`).
 */
export function tokenizeRtf(input: string): RtfToken[] {
  const tokens: RtfToken[] = [];
  const { length } = input;
  let index = 0;
  let textBuffer = "";

  const flushText = () => {
    if (textBuffer.length > 0) {
      tokens.push({ type: "text", text: textBuffer });
      textBuffer = "";
    }
  };

  while (index < length) {
    const char = input[index];

    if (char === "{") {
      flushText();
      tokens.push({ type: "groupStart" });
      index++;
    } else if (char === "}") {
      flushText();
      tokens.push({ type: "groupEnd" });
      index++;
    } else if (char === "\\") {
      flushText();
      index++;
      if (index >= length) break;
      const next = input[index];

      if (/[a-zA-Z]/.test(next)) {
        index = consumeControlWord(input, index, tokens);
      } else if (next === "'") {
        // \'hh - a single hex-escaped byte in the current codepage.
        const hex = input.slice(index + 1, index + 3);
        index += 3;
        const byte = Number.parseInt(hex, 16);
        if (!Number.isNaN(byte)) {
          tokens.push({ type: "hexEscape", byte });
        }
      } else {
        // Control symbol: a single non-letter character. No numeric
        // parameter or space delimiter applies to these.
        tokens.push({ type: "controlSymbol", symbol: next });
        index++;
      }
    } else if (char === "\r" || char === "\n") {
      // Raw newlines in the RTF source are insignificant whitespace.
      index++;
    } else {
      textBuffer += char;
      index++;
    }
  }

  flushText();
  return tokens;
}

// Parses a control word starting at `index` (the first letter after the
// backslash), pushes the resulting token (unless it's `\binN`, in which case
// the following N bytes of binary payload are skipped instead), and returns
// the index just past the consumed input.
function consumeControlWord(input: string, index: number, tokens: RtfToken[]): number {
  const { length } = input;
  const nameStart = index;
  while (index < length && /[a-zA-Z]/.test(input[index])) index++;
  const name = input.slice(nameStart, index);

  let param: number | undefined;
  const numStart = index;
  let numIndex = index;
  if (input[numIndex] === "-") numIndex++;
  while (numIndex < length && /[0-9]/.test(input[numIndex])) numIndex++;
  if (numIndex > numStart) {
    param = Number.parseInt(input.slice(numStart, numIndex), 10);
    index = numIndex;
  }

  if (name === "bin" && param !== undefined && param > 0) {
    // \binN is immediately followed by N raw bytes of binary data - no space
    // delimiter is consumed, since the very next byte begins the payload.
    return index + param;
  }

  if (input[index] === " ") {
    index++; // consume a single trailing space delimiter, per the RTF spec
  }

  tokens.push({ type: "controlWord", name, param });
  return index;
}
