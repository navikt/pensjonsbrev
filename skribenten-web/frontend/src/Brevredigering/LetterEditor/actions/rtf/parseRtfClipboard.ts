import {
  detectEncapsulationMode,
  extractEncapsulatedHtml,
  extractEncapsulatedText,
} from "~/Brevredigering/LetterEditor/actions/rtf/extractEncapsulation";
import { interpretNativeRtf } from "~/Brevredigering/LetterEditor/actions/rtf/interpretNativeRtf";
import { tokenizeRtf } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";
import { type TraversedElement } from "~/Brevredigering/LetterEditor/actions/traversedElement";

export type ParsedRtfClipboard =
  | { mode: "html"; html: string }
  | { mode: "text"; text: string }
  | { mode: "elements"; elements: TraversedElement[] }
  | { mode: "unsupported" };

/**
 * Parses a raw `text/rtf` clipboard payload, routing it to whichever
 * interpretation applies:
 * - `\fromhtml1`/`\fromtext1` encapsulated Outlook/Exchange content is
 *   unwrapped back into a literal HTML string or plain text (see
 *   `extractEncapsulation.ts`).
 * - Otherwise, it's treated as genuine/native RTF (e.g. pasted from Word)
 *   and interpreted directly into our internal `TraversedElement[]` model
 *   (see `interpretNativeRtf.ts`).
 *
 * Never throws: any tokenizing/interpretation failure, or a native-RTF
 * result with zero elements, is reported as `{ mode: "unsupported" }` so
 * callers can fall back to another clipboard format instead of silently
 * losing the paste.
 */
export function parseRtfClipboard(rtf: string): ParsedRtfClipboard {
  try {
    const tokens = tokenizeRtf(rtf);
    const encapsulationMode = detectEncapsulationMode(tokens);

    if (encapsulationMode === "html") {
      return { mode: "html", html: extractEncapsulatedHtml(tokens) };
    }
    if (encapsulationMode === "text") {
      return { mode: "text", text: extractEncapsulatedText(tokens) };
    }

    const elements = interpretNativeRtf(tokens);
    return elements.length > 0 ? { mode: "elements", elements } : { mode: "unsupported" };
  } catch {
    return { mode: "unsupported" };
  }
}

// Last-resort fallback for RTF that we failed to interpret at all (parse
// error, or an unsupported feature set yielding no content) and where no
// `text/plain` clipboard alternative is available either. Strips RTF's own
// syntax (control words/symbols, groups, hex/unicode escapes) down to
// whatever literal text remains, so paste never silently no-ops.
export function stripRtfToPlainText(rtf: string): string {
  return rtf
    .replaceAll(/\\par[d]?\b\s?/g, "\n")
    .replaceAll(/\\tab\b\s?/g, "\t")
    .replaceAll(/\\'[0-9a-fA-F]{2}/g, "")
    .replaceAll(/\\u-?\d+\s?/g, "")
    .replaceAll(/\\[a-zA-Z]+-?\d*\s?/g, "")
    .replaceAll(/[{}]/g, "")
    .replaceAll("\\\\", "\\")
    .trim();
}
