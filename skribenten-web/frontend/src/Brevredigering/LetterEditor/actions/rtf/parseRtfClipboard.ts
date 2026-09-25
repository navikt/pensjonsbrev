import { extractEncapsulatedContent } from "~/Brevredigering/LetterEditor/actions/rtf/extractEncapsulation";
import { interpretNativeRtf } from "~/Brevredigering/LetterEditor/actions/rtf/interpretNativeRtf";
import { createByteDecoder } from "~/Brevredigering/LetterEditor/actions/rtf/rtfDecoding";
import { tokenizeRtf } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";
import { type TraversedElement } from "~/Brevredigering/LetterEditor/actions/traversedElement";

export type ParsedRtfClipboard =
  | { mode: "html"; html: string }
  | { mode: "text"; text: string }
  | { mode: "elements"; elements: TraversedElement[] }
  /** Parsed fine, but there is nothing to insert. The caller falls back to text/plain silently. */
  | { mode: "empty" }
  /** Parsing threw. The caller logs it and falls back to text/plain. */
  | { mode: "unsupported"; error: unknown };

/**
 * Unwraps HTML or text encapsulated by Outlook (MS-OXRTFEX), or interprets native RTF from Word/WordPad.
 * Tokenizes once. Never throws.
 */
export function parseRtfClipboard(rtf: string): ParsedRtfClipboard {
  try {
    const tokens = tokenizeRtf(rtf);
    const decodeBytes = createByteDecoder(tokens);
    const encapsulated = extractEncapsulatedContent(tokens, decodeBytes);
    if (encapsulated?.format === "html") return { mode: "html", html: encapsulated.html };
    if (encapsulated?.format === "text") {
      return encapsulated.text.trim().length > 0 ? { mode: "text", text: encapsulated.text } : { mode: "empty" };
    }
    const elements = interpretNativeRtf(tokens, decodeBytes);
    return elements.length > 0 ? { mode: "elements", elements } : { mode: "empty" };
  } catch (error) {
    return { mode: "unsupported", error };
  }
}
