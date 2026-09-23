import { walkRtfContent } from "~/Brevredigering/LetterEditor/actions/rtf/rtfContentWalker";
import { type RtfToken } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";

/**
 * RTF-encapsulated HTML/text (per MS-OXRTFEX) is how Outlook/Exchange wrap a
 * literal HTML or plain-text email body in RTF for compatibility with
 * readers that only understand RTF. The wrapped payload is itself already
 * fully-formed HTML/text, not RTF-formatted prose - so unwrapping it is a
 * much narrower problem than interpreting genuine/native RTF (e.g. pasted
 * directly from Word): no font tables, no symbol-font codepoint remapping,
 * no paragraph/list/table semantics of our own to infer.
 */
export type EncapsulationMode = "html" | "text";

const HTML_PASSTHROUGH_DESTINATIONS = new Set(["htmltag"]);

/**
 * Detects whether an RTF document declares itself as encapsulating literal
 * HTML (`\fromhtml1`) or plain text (`\fromtext1`). Returns `undefined` if
 * neither marker is present, meaning the document should be treated as
 * genuine/native RTF instead (see `interpretNativeRtf.ts`).
 */
export function detectEncapsulationMode(tokens: RtfToken[]): EncapsulationMode | undefined {
  for (const token of tokens) {
    if (token.type === "controlWord") {
      // Toggle-style control words default to "on" (param 1) when the
      // numeric parameter is omitted; only an explicit 0 means "off".
      if (token.name === "fromhtml" && token.param !== 0) return "html";
      if (token.name === "fromtext" && token.param !== 0) return "text";
    }
  }
  return undefined;
}

// Per MS-OXRTFEX, visible (non-tag) text in an encapsulated HTML payload is
// generally already HTML-entity-escaped by the encoder, except for stray
// `<`/`>` characters, which the spec requires readers to re-escape so they
// don't get misinterpreted as tag delimiters once the tag markup (recovered
// verbatim from `htmltag` destinations) is spliced back in.
function escapeStrayAngleBrackets(value: string): string {
  return value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
}

/**
 * Recovers the original HTML string from an RTF document that declares
 * `\fromhtml1`. The HTML tag markup itself lives verbatim inside `htmltag`
 * destinations; everything else is visible text content.
 */
export function extractEncapsulatedHtml(tokens: RtfToken[]): string {
  const events = walkRtfContent(tokens, { passThroughDestinations: HTML_PASSTHROUGH_DESTINATIONS });

  let html = "";
  for (const event of events) {
    if (event.kind === "text") {
      html += event.raw ? event.value : escapeStrayAngleBrackets(event.value);
    }
  }
  return html;
}

/**
 * Recovers the original plain-text string from an RTF document that
 * declares `\fromtext1`, converting `\par` paragraph breaks back into
 * newlines.
 */
export function extractEncapsulatedText(tokens: RtfToken[]): string {
  const events = walkRtfContent(tokens);

  let text = "";
  for (const event of events) {
    if (event.kind === "text") {
      text += event.value;
    } else if (event.kind === "control" && event.name === "par") {
      text += "\n";
    }
  }
  return text;
}
