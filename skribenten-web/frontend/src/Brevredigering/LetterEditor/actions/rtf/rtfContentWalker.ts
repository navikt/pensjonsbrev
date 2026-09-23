import { decodeAnsiByte, SKIPPED_RTF_DESTINATIONS } from "~/Brevredigering/LetterEditor/actions/rtf/rtfDestinations";
import { type RtfToken } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";

export type RtfContentEvent =
  | { kind: "groupStart" }
  | { kind: "groupEnd" }
  /** Ordinary visible text. `raw` is true only for text originating inside a
   * pass-through destination (see `passThroughDestinations`), meaning it is
   * already-encoded markup that should not be further escaped. */
  | { kind: "text"; value: string; raw: boolean }
  /** The concatenated literal contents of a `{\listtext ...}` destination -
   * the rendered bullet/number fallback text for a list item. Never part of
   * ordinary paragraph text; used only to classify bullet vs. numbered
   * lists (see `classifyListMarkerText`). */
  | { kind: "listMarkerText"; value: string }
  /** Any control word not already resolved into a `text` event (i.e. not
   * `\u`, `\uc`, or `\bin`, which the tokenizer/walker consume directly). */
  | { kind: "control"; name: string; param?: number };

export interface WalkRtfContentOptions {
  /** Destination names whose content should be walked through normally (not
   * skipped) but marked as `raw` text. Used for the `htmltag` destination
   * when extracting RTF-encapsulated HTML (see `extractEncapsulation.ts`). */
  passThroughDestinations?: ReadonlySet<string>;
}

function mapControlSymbolToText(symbol: string): string | undefined {
  switch (symbol) {
    case "~": {
      return "\u00A0"; // non-breaking space
    }
    case "_": {
      return "-"; // non-breaking hyphen, approximated as a regular hyphen
    }
    case "\\":
    case "{":
    case "}": {
      return symbol;
    }
    default: {
      // e.g. "-" (optional hyphen) or ":" (index subentry marker) - invisible/ignorable.
      return undefined;
    }
  }
}

/**
 * Walks a tokenized RTF document into a flat stream of content events,
 * resolving the mechanics shared by both RTF-encapsulation extraction and
 * native-RTF interpretation:
 *
 * - Destination/group tracking: metadata destinations (font/color/style
 *   tables, document info, etc. - see `SKIPPED_RTF_DESTINATIONS`) and any
 *   unrecognized `{\*...}` ignorable destination are fully swallowed -
 *   their contents never appear in the output, and their own group
 *   boundaries are not emitted either.
 * - `{\listtext ...}` destinations (list item bullet/number fallback text)
 *   are captured into a single dedicated `listMarkerText` event instead of
 *   being swallowed entirely or leaking into ordinary text.
 * - Unicode escapes (`\uN`) are decoded into their character, and the `ucN`
 *   fallback substitute characters that follow are skipped, per the RTF
 *   spec's `\uc` mechanism (simplified: tracked as a single running value
 *   rather than strictly scoped per group, which is sufficient for typical
 *   clipboard-sized content - see plan's documented limitations).
 * - `\'hh` hex escapes are decoded via the Windows-1252 codepage.
 * - Control symbols (`\~`, `\_`, `\\`, `\{`, `\}`, etc.) are mapped to their
 *   literal character where applicable.
 */
export function walkRtfContent(tokens: RtfToken[], options: WalkRtfContentOptions = {}): RtfContentEvent[] {
  const passThrough = options.passThroughDestinations ?? new Set<string>();
  const events: RtfContentEvent[] = [];

  const skipStack: boolean[] = [false];
  const rawStack: boolean[] = [false];
  const ucStack: number[] = [1];
  let pendingUnicodeFallback = 0;

  // When >= 0, we're inside a `{\listtext ...}` destination at this exact
  // skip-stack depth, capturing its literal text instead of discarding it.
  let listMarkerCaptureDepth = -1;
  let listMarkerCapture = "";

  const isSkipping = () => skipStack.at(-1) === true;
  const isRaw = () => rawStack.at(-1) === true;

  let index = 0;
  while (index < tokens.length) {
    const token = tokens[index];

    if (token.type === "groupStart") {
      let lookahead = index + 1;
      let ignorable = false;
      const lookaheadToken = tokens[lookahead];
      if (lookaheadToken?.type === "controlSymbol" && lookaheadToken.symbol === "*") {
        ignorable = true;
        lookahead++;
      }
      const maybeDestination = tokens[lookahead];
      const destinationName = maybeDestination?.type === "controlWord" ? maybeDestination.name : undefined;

      const isListText = destinationName === "listtext";
      const passesThrough = destinationName !== undefined && passThrough.has(destinationName);
      const isKnownSkip = destinationName !== undefined && SKIPPED_RTF_DESTINATIONS.has(destinationName);
      const skipThisGroup = !passesThrough && (isKnownSkip || isListText || ignorable);

      const parentSkip = isSkipping();
      skipStack.push(parentSkip || skipThisGroup);
      rawStack.push(!parentSkip && !skipThisGroup && passesThrough);
      ucStack.push(ucStack.at(-1) ?? 1);
      pendingUnicodeFallback = 0; // \uc's scope (and any pending fallback skip) ends at group boundaries

      if (!parentSkip && isListText && listMarkerCaptureDepth === -1) {
        listMarkerCaptureDepth = skipStack.length - 1;
        listMarkerCapture = "";
      }

      if (!skipStack.at(-1)) {
        events.push({ kind: "groupStart" });
      }
      index++;
      continue;
    }

    if (token.type === "groupEnd") {
      const wasSkipping = isSkipping();
      const closingListTextDepth = skipStack.length - 1 === listMarkerCaptureDepth;
      skipStack.pop();
      rawStack.pop();
      ucStack.pop();
      if (skipStack.length === 0) {
        // Malformed/unbalanced input - keep a root frame so we don't crash.
        skipStack.push(false);
        rawStack.push(false);
        ucStack.push(1);
      }
      pendingUnicodeFallback = 0;

      if (closingListTextDepth) {
        events.push({ kind: "listMarkerText", value: listMarkerCapture });
        listMarkerCaptureDepth = -1;
        listMarkerCapture = "";
      } else if (!wasSkipping) {
        events.push({ kind: "groupEnd" });
      }
      index++;
      continue;
    }

    const capturingListMarker = listMarkerCaptureDepth !== -1 && skipStack.length - 1 >= listMarkerCaptureDepth;

    if (isSkipping() && !capturingListMarker) {
      index++;
      continue;
    }

    if (token.type === "text") {
      let value = token.text;
      if (pendingUnicodeFallback > 0) {
        const consumed = Math.min(pendingUnicodeFallback, value.length);
        value = value.slice(consumed);
        pendingUnicodeFallback -= consumed;
      }
      if (value.length > 0) {
        if (capturingListMarker) {
          listMarkerCapture += value;
        } else {
          events.push({ kind: "text", value, raw: isRaw() });
        }
      }
      index++;
      continue;
    }

    if (token.type === "hexEscape") {
      if (pendingUnicodeFallback > 0) {
        pendingUnicodeFallback -= 1;
      } else {
        const decoded = decodeAnsiByte(token.byte);
        if (capturingListMarker) {
          listMarkerCapture += decoded;
        } else {
          events.push({ kind: "text", value: decoded, raw: isRaw() });
        }
      }
      index++;
      continue;
    }

    if (token.type === "controlSymbol") {
      if (pendingUnicodeFallback > 0) {
        pendingUnicodeFallback -= 1;
        index++;
        continue;
      }
      const mapped = mapControlSymbolToText(token.symbol);
      if (mapped !== undefined && !capturingListMarker) {
        events.push({ kind: "text", value: mapped, raw: isRaw() });
      }
      index++;
      continue;
    }

    // token.type === "controlWord"
    if (pendingUnicodeFallback > 0 && token.name !== "uc" && token.name !== "u") {
      pendingUnicodeFallback -= 1;
      index++;
      continue;
    }

    if (token.name === "uc" && token.param !== undefined) {
      ucStack[ucStack.length - 1] = token.param;
      index++;
      continue;
    }

    if (token.name === "u" && token.param !== undefined) {
      const codepoint = token.param < 0 ? token.param + 65_536 : token.param;
      const char = String.fromCharCode(codepoint);
      if (capturingListMarker) {
        listMarkerCapture += char;
      } else {
        events.push({ kind: "text", value: char, raw: isRaw() });
      }
      pendingUnicodeFallback = ucStack.at(-1) ?? 1;
      index++;
      continue;
    }

    if (!capturingListMarker) {
      events.push({ kind: "control", name: token.name, param: token.param });
    }
    index++;
  }

  return events;
}
