import {
  type ByteDecoder,
  decodeUnicodeParam,
  RTF_SYMBOL_WORDS,
  skipUnicodeFallback,
} from "~/Brevredigering/LetterEditor/actions/rtf/rtfDecoding";
import { type RtfControlToken, type RtfToken } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";

/**
 * The RTF mechanics shared by the native interpreter and the Outlook de-encapsulator: group scoping,
 * destinations (incl. `\*`), `\'hh` and `\uN` decoding with `\ucN` fallback skipping, nested `\rtf`
 * documents and the end of the document. Formatting and paragraph structure are left to the consumer.
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
  /** When set, a word after `\*` only opens its destination if it's in this set; any other is skipped. */
  ignorableDestinations?: ReadonlySet<string>;
}

interface WalkerGroup {
  destination: RtfDestination;
  /** Set by `\*`: the destination word that follows is skipped unless we know it. */
  ignorable: boolean;
  ucSkip: number;
  /** Whether a `groupStart` was emitted, so that `groupEnd` is emitted too. */
  announced: boolean;
}

export function* walkRtf(tokens: readonly RtfToken[], options: WalkRtfOptions): Generator<RtfEvent, void, undefined> {
  const groups: WalkerGroup[] = [{ destination: "body", ignorable: false, ucSkip: 1, announced: false }];
  let pendingBytes: number[] = [];
  /** Fallback units left to skip after `\uN`. */
  let unicodeSkip = 0;

  const current = () => groups.at(-1)!;

  function text(value: string): RtfEvent | undefined {
    const { destination } = current();
    if (value.length === 0 || destination === "skip") return undefined;
    return { kind: "text", value, destination };
  }

  function flushBytes(): RtfEvent | undefined {
    if (pendingBytes.length === 0) return undefined;
    const value = options.decodeBytes(pendingBytes);
    pendingBytes = [];
    return text(value);
  }

  function resolveDestination(group: WalkerGroup, word: string): RtfDestination | undefined {
    const known = options.destinations.get(word);
    if (!group.ignorable) return known;
    group.ignorable = false;
    if (options.ignorableDestinations && !options.ignorableDestinations.has(word)) return "skip";
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
        const event = text(token.value);
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
          const event = text(decodeUnicodeParam(token.param));
          if (event) yield event;
          unicodeSkip = group.ucSkip;
          break;
        }
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
