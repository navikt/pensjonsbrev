import { decodeAnsiByte, SKIPPED_RTF_DESTINATIONS } from "~/Brevredigering/LetterEditor/actions/rtf/rtfDestinations";
import { type RtfToken } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";

export type RtfContentEvent =
  | { kind: "groupStart" }
  | { kind: "groupEnd" }
  /** `raw` marks text from a pass-through destination, e.g. literal HTML markup in `\htmltag`. */
  | { kind: "text"; value: string; raw: boolean }
  /** The rendered bullet or number of a list item, taken from `{\listtext ...}`. */
  | { kind: "listMarkerText"; value: string }
  | { kind: "control"; name: string; param?: number };

type GroupMode = "content" | "raw" | "listMarker" | "skip";

interface Group {
  mode: GroupMode;
  unicodeFallbackLength: number;
}

const CONTROL_SYMBOL_TEXT: Record<string, string> = {
  "\\": "\\",
  "{": "{",
  "}": "}",
  "~": "\u00A0",
  _: "-",
};

const CONTROL_WORD_TEXT: Record<string, string> = {
  lquote: "\u2018",
  rquote: "\u2019",
  ldblquote: "\u201C",
  rdblquote: "\u201D",
  bullet: "\u2022",
  endash: "\u2013",
  emdash: "\u2014",
  enspace: " ",
  emspace: " ",
};

/**
 * Resolves the RTF mechanics shared by all interpretations of a document into a flat event stream:
 * skips metadata destinations, decodes hex and unicode escapes, maps special characters to text,
 * and captures list markers separately from the text.
 */
export function walkRtfContent(
  tokens: RtfToken[],
  passThroughDestinations: ReadonlySet<string> = new Set(),
): RtfContentEvent[] {
  const events: RtfContentEvent[] = [];
  const groups: Group[] = [{ mode: "content", unicodeFallbackLength: 1 }];
  const currentGroup = () => groups.at(-1)!;
  const isEmitting = (mode: GroupMode) => mode === "content" || mode === "raw";

  // Number of tokens/characters after a `\uN` that are an ANSI fallback for old readers.
  let fallbackToSkip = 0;
  let listMarkerText = "";

  const emitText = (value: string) => {
    const { mode } = currentGroup();
    if (mode === "listMarker") {
      listMarkerText += value;
    } else if (isEmitting(mode)) {
      events.push({ kind: "text", value, raw: mode === "raw" });
    }
  };

  for (const [index, token] of tokens.entries()) {
    if (token.type === "groupStart") {
      const mode = childGroupMode(currentGroup().mode, tokens, index, passThroughDestinations);
      groups.push({ mode, unicodeFallbackLength: currentGroup().unicodeFallbackLength });
      fallbackToSkip = 0;
      if (isEmitting(mode)) events.push({ kind: "groupStart" });
      continue;
    }

    if (token.type === "groupEnd") {
      if (groups.length === 1) continue; // unbalanced input
      const closed = groups.pop()!;
      fallbackToSkip = 0;
      if (isEmitting(closed.mode)) {
        events.push({ kind: "groupEnd" });
      } else if (closed.mode === "listMarker" && currentGroup().mode !== "listMarker") {
        events.push({ kind: "listMarkerText", value: listMarkerText });
        listMarkerText = "";
      }
      continue;
    }

    if (fallbackToSkip > 0) {
      if (token.type === "text") {
        const skipped = Math.min(fallbackToSkip, token.text.length);
        fallbackToSkip -= skipped;
        if (skipped < token.text.length) emitText(token.text.slice(skipped));
      } else {
        fallbackToSkip--;
      }
      continue;
    }

    switch (token.type) {
      case "text": {
        emitText(token.text);
        break;
      }
      case "hexEscape": {
        emitText(decodeAnsiByte(token.byte));
        break;
      }
      case "controlSymbol": {
        const text = CONTROL_SYMBOL_TEXT[token.symbol];
        if (text !== undefined) emitText(text);
        break;
      }
      case "controlWord": {
        const text = CONTROL_WORD_TEXT[token.name];
        if (text !== undefined) {
          emitText(text);
        } else if (token.name === "uc" && token.param !== undefined) {
          currentGroup().unicodeFallbackLength = token.param;
        } else if (token.name === "u" && token.param !== undefined) {
          // Code points above 32767 are written as negative 16-bit numbers.
          emitText(String.fromCharCode(token.param < 0 ? token.param + 65_536 : token.param));
          fallbackToSkip = currentGroup().unicodeFallbackLength;
        } else if (isEmitting(currentGroup().mode)) {
          events.push({ kind: "control", name: token.name, param: token.param });
        }
        break;
      }
    }
  }

  return events;
}

// A group starting with a control word (optionally preceded by `\*`) is a destination.
function childGroupMode(
  parent: GroupMode,
  tokens: RtfToken[],
  groupStartIndex: number,
  passThroughDestinations: ReadonlySet<string>,
): GroupMode {
  if (parent === "skip") return "skip";

  let next = tokens[groupStartIndex + 1];
  const ignorable = next?.type === "controlSymbol" && next.symbol === "*";
  if (ignorable) next = tokens[groupStartIndex + 2];
  const destination = next?.type === "controlWord" ? next.name : undefined;

  if (destination !== undefined && passThroughDestinations.has(destination)) return "raw";
  if (destination === "listtext") return "listMarker";
  if (ignorable || (destination !== undefined && SKIPPED_RTF_DESTINATIONS.has(destination))) return "skip";
  return parent;
}
