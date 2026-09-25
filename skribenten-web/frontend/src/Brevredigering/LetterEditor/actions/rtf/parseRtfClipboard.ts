import {
  detectEncapsulationMode,
  extractEncapsulatedContent,
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
 * Unwraps HTML or text encapsulated by Outlook, or interprets native RTF from e.g. Word.
 * Returns `unsupported` instead of throwing, so the caller can fall back to `text/plain`.
 */
export function parseRtfClipboard(rtf: string): ParsedRtfClipboard {
  try {
    const tokens = tokenizeRtf(rtf);
    const encapsulationMode = detectEncapsulationMode(tokens);
    if (encapsulationMode === "html") return { mode: "html", html: extractEncapsulatedContent(tokens, "html") };
    if (encapsulationMode === "text") return { mode: "text", text: extractEncapsulatedContent(tokens, "text") };

    const elements = interpretNativeRtf(tokens);
    return elements.length > 0 ? { mode: "elements", elements } : { mode: "unsupported" };
  } catch {
    return { mode: "unsupported" };
  }
}
