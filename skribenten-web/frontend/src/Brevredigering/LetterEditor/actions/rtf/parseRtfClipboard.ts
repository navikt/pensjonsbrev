import { extractEncapsulatedContent } from "~/Brevredigering/LetterEditor/actions/rtf/extractEncapsulation";
import { interpretNativeRtf } from "~/Brevredigering/LetterEditor/actions/rtf/interpretNativeRtf";
import { createByteDecoder } from "~/Brevredigering/LetterEditor/actions/rtf/rtfDecoding";
import { tokenizeRtf } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";
import { type TraversedElement } from "~/Brevredigering/LetterEditor/actions/traversedElement";

/**
 * Entry point for pasted RTF (`text/rtf`). The pipeline:
 *
 * 1. `tokenizeRtf` splits the clipboard into tokens, once.
 * 2. `createByteDecoder` picks the code page from the header's `\ansicpgN`.
 * 3. If the header marks the RTF as encapsulated by Outlook (`\fromhtml1` / `\fromtext`),
 *    `extractEncapsulatedContent` restores the original HTML or text.
 * 4. Otherwise `interpretNativeRtf` turns Word/WordPad RTF into `TraversedElement`s.
 *
 * Both 3 and 4 read the tokens through `walkRtf`. The result tells `paste.ts` which insertion path to use.
 *
 * Encoding is deliberately limited to Western documents: the header's code page (cp1252 by default,
 * also for unknown code pages), `\uN` with `\ucN` fallback skipping and surrogate pairs (lone surrogates
 * become U+FFFD), the Unicode branch of `\upr`, and Symbol/Wingdings/Webdings/Zapf Dingbats fonts mapped
 * to Unicode. Per-font non-Western charsets and `\mac`/`\pc`/`\pca` are not supported.
 *
 * Malformed input never throws or freezes the editor: input over `MAX_RTF_LENGTH` is `unsupported`,
 * truncated `\'h` escapes become "?", a `\binN` past the end stops parsing, and numeric parameters are
 * capped at ten digits and clamped to 32 bits.
 *
 * Techniques are borrowed, not code, from Apache Tika's `TextExtractor`, RtfPipe, rtf-codec and
 * rtf-stream-parser. The tests use MIT-licensed fixtures from RtfPipe, rtf2xml and rtf.js
 * (see `interpretNativeRtf.reference.test.ts`).
 */

export type ParsedRtfClipboard =
  | { mode: "html"; html: string }
  | { mode: "text"; text: string }
  | { mode: "elements"; elements: TraversedElement[] }
  /** Parsed fine, but there is nothing to insert. The caller falls back to text/plain silently. */
  | { mode: "empty" }
  /** Parsing threw, or the RTF is too large. The caller logs it and falls back to text/plain. */
  | { mode: "unsupported"; error: unknown };

/** RTF from Word with images can be tens of megabytes; parsing that would freeze the editor. */
export const MAX_RTF_LENGTH = 20_000_000;

/**
 * Unwraps HTML or text encapsulated by Outlook (MS-OXRTFEX), or interprets native RTF from Word/WordPad.
 * Tokenizes once. Never throws.
 */
export function parseRtfClipboard(rtf: string): ParsedRtfClipboard {
  if (rtf.length > MAX_RTF_LENGTH) {
    return { mode: "unsupported", error: new Error(`RTF is too large to paste: ${rtf.length} characters`) };
  }
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
