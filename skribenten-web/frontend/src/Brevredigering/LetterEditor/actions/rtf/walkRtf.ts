import {
  type ByteDecoder,
  decodeUnicodeParam,
  RTF_SYMBOL_WORDS,
  skipUnicodeFallback,
} from "~/Brevredigering/LetterEditor/actions/rtf/rtfDecoding";
import {
  mapSymbolBytes,
  mapSymbolText,
  parseSymbolFonts,
} from "~/Brevredigering/LetterEditor/actions/rtf/rtfSymbolFonts";
import { type RtfControlToken, type RtfToken } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";

/**
 * The RTF mechanics shared by the native interpreter and the Outlook de-encapsulator: group scoping,
 * destinations (incl. `\*`), `\'hh` and `\uN` decoding with `\ucN` fallback skipping, nested `\rtf`
 * documents and the end of the document. Formatting, paragraph structure and the meaning of control
 * words such as `\par` and `\line` are left to the consumer, and so is the code page (`decodeBytes`).
 *
 * - `body`: document content
 * - `skip`: nothing inside is emitted
 * - `listMarker`: `\listtext`/`\pntext`, the rendered bullet or number of a list item
 * - `pn`: old-style `\pn` list properties of the current paragraph
 * - `htmltag`: `\*\htmltag`, HTML markup encapsulated by Outlook
 */
export type RtfDestination = "body" | "skip" | "listMarker" | "pn" | "htmltag";

export type RtfEvent =
  /** Only emitted for groups opened in a non-skip destination, so start and end are always balanced. */
  | { kind: "groupStart"; destination: RtfDestination }
  | { kind: "groupEnd"; destination: RtfDestination }
  /** Decoded text: plain text, `\'hh` runs (decoded together), `\uN` and character control words. */
  | { kind: "text"; value: string; destination: RtfDestination }
  /** Any other control word in a non-skip group (`\par`, `\b`, `\intbl`, `\htmlrtf`, `\rtf`, …). */
  | { kind: "control"; token: RtfControlToken; destination: RtfDestination }
  /** The root `{\rtf1 …}` group closed or the input ended. Anything after it is ignored. */
  | { kind: "documentEnd" };

export interface WalkRtfOptions {
  /** Destination words and what they open, e.g. fonttbl → "skip", listtext → "listMarker". */
  destinations: ReadonlyMap<string, RtfDestination>;
  decodeBytes: ByteDecoder;
}

interface WalkerGroup {
  destination: RtfDestination;
  /** Set by `\*`: the destination word that follows is skipped unless we know it. */
  ignorable: boolean;
  ucSkip: number;
  /** `\fN`, which decides how text in symbol fonts is decoded. */
  font?: number;
  /** Whether a `groupStart` was emitted, so that `groupEnd` is emitted too. */
  announced: boolean;
}

export function* walkRtf(tokens: readonly RtfToken[], options: WalkRtfOptions): Generator<RtfEvent, void, undefined> {
  const symbolFonts = parseSymbolFonts(tokens);
  const groups: WalkerGroup[] = [
    { destination: "body", ignorable: false, ucSkip: 1, announced: false, font: symbolFonts.defaultFont },
  ];
  let pendingBytes: number[] = [];
  /** Fallback units left to skip after `\uN`. */
  let unicodeSkip = 0;

  const current = () => groups.at(-1)!;
  /** HTML markup encapsulated by Outlook is never in a symbol font, whatever font is current. */
  const symbolTable = () => {
    const { font, destination } = current();
    return font === undefined || destination === "htmltag" ? undefined : symbolFonts.tables.get(font);
  };

  function text(value: string): RtfEvent | undefined {
    const { destination } = current();
    if (value.length === 0 || destination === "skip") return undefined;
    return { kind: "text", value, destination };
  }

  /** Text and `\uN` in a symbol font. */
  function fontText(value: string): RtfEvent | undefined {
    const table = symbolTable();
    return text(table ? mapSymbolText(table, value, (byte) => options.decodeBytes([byte])) : value);
  }

  // Bytes are flushed before every other token, so before a font change or the end of a group.
  function flushBytes(): RtfEvent | undefined {
    if (pendingBytes.length === 0) return undefined;
    const table = symbolTable();
    const value = table ? mapSymbolBytes(table, pendingBytes, options.decodeBytes) : options.decodeBytes(pendingBytes);
    pendingBytes = [];
    return text(value);
  }

  function resolveDestination(group: WalkerGroup, word: string): RtfDestination | undefined {
    const known = options.destinations.get(word);
    if (!group.ignorable) return known;
    group.ignorable = false;
    return known ?? "skip";
  }

  for (const rawToken of tokens) {
    if (rawToken.type !== "hexByte") {
      const flushed = flushBytes();
      if (flushed) yield flushed;
    }

    let token: RtfToken | undefined = rawToken;
    if (unicodeSkip > 0) {
      const skip = skipUnicodeFallback(unicodeSkip, rawToken);
      unicodeSkip = skip.remaining;
      token = skip.rest;
    }
    if (!token) continue;

    switch (token.type) {
      case "groupStart": {
        const parent = current();
        const announced = parent.destination !== "skip";
        groups.push({ ...parent, ignorable: false, announced });
        if (announced) yield { kind: "groupStart", destination: parent.destination };
        break;
      }
      case "groupEnd": {
        if (groups.length === 2) {
          yield { kind: "documentEnd" };
          return;
        }
        if (groups.length > 1) {
          const closed = groups.pop()!;
          if (closed.announced) yield { kind: "groupEnd", destination: closed.destination };
        }
        break;
      }
      case "hexByte": {
        pendingBytes.push(token.byte);
        break;
      }
      case "text": {
        const group = current();
        if (group.ignorable) group.destination = "skip";
        const event = fontText(token.value);
        if (event) yield event;
        break;
      }
      case "control": {
        const group = current();
        if (group.destination === "skip") break;

        if (token.word === "*") {
          group.ignorable = true;
          break;
        }
        const destination = resolveDestination(group, token.word);
        if (destination) {
          group.destination = destination;
          break;
        }
        if (token.word === "uc") {
          group.ucSkip = Math.max(0, token.param);
          break;
        }
        if (token.word === "u") {
          const event = fontText(decodeUnicodeParam(token.param));
          if (event) yield event;
          unicodeSkip = group.ucSkip;
          break;
        }
        if (token.word === "f" && token.hasParam) group.font = token.param;
        else if (token.word === "plain") group.font = symbolFonts.defaultFont;
        const symbol = RTF_SYMBOL_WORDS.get(token.word);
        if (symbol !== undefined) {
          const event = text(symbol);
          if (event) yield event;
          break;
        }
        // Outlook appends mail signatures as a nested, plain RTF document.
        if (token.word === "rtf" && groups.length > 2) group.destination = "body";
        yield { kind: "control", token, destination: group.destination };
        break;
      }
    }
  }

  const flushed = flushBytes();
  if (flushed) yield flushed;
  yield { kind: "documentEnd" };
}
