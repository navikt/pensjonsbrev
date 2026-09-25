import { walkRtfContent } from "~/Brevredigering/LetterEditor/actions/rtf/rtfContentWalker";
import { type RtfToken } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";

/**
 * Outlook wraps HTML and plain-text mail bodies in RTF (MS-OXRTFEX), marked with `\fromhtml1` or
 * `\fromtext1`. HTML markup is stored verbatim in `\htmltag` destinations, and content between
 * `\htmlrtf` and `\htmlrtf0` exists only for RTF readers and must be dropped.
 */
export type EncapsulationMode = "html" | "text";

const HTML_TAG_DESTINATIONS = new Set(["htmltag"]);

const CONTROL_WORD_TEXT: Record<string, string> = { par: "\n", line: "\n", tab: "\t" };

export function detectEncapsulationMode(tokens: RtfToken[]): EncapsulationMode | undefined {
  for (const token of tokens) {
    if (token.type === "controlWord" && token.param !== 0) {
      if (token.name === "fromhtml") return "html";
      if (token.name === "fromtext") return "text";
    }
  }
  return undefined;
}

export function extractEncapsulatedContent(tokens: RtfToken[], mode: EncapsulationMode): string {
  const events = walkRtfContent(tokens, mode === "html" ? HTML_TAG_DESTINATIONS : undefined);
  // \htmlrtf is scoped to the group it appears in.
  const suppressed: boolean[] = [false];
  let output = "";

  for (const event of events) {
    switch (event.kind) {
      case "groupStart": {
        suppressed.push(suppressed.at(-1)!);
        break;
      }
      case "groupEnd": {
        if (suppressed.length > 1) suppressed.pop();
        break;
      }
      case "control": {
        if (event.name === "htmlrtf") {
          suppressed[suppressed.length - 1] = event.param !== 0;
        } else if (!suppressed.at(-1)) {
          output += CONTROL_WORD_TEXT[event.name] ?? "";
        }
        break;
      }
      case "text": {
        if (!suppressed.at(-1)) output += event.raw || mode === "text" ? event.value : escapeHtml(event.value);
        break;
      }
      case "listMarkerText": {
        break;
      }
    }
  }
  return output;
}

// Text outside \htmltag is document text, so it must not be able to form markup.
function escapeHtml(value: string): string {
  return value.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
}
