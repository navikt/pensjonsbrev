import { type ByteDecoder } from "~/Brevredigering/LetterEditor/actions/rtf/rtfDecoding";
import { ENCAPSULATION_DESTINATIONS } from "~/Brevredigering/LetterEditor/actions/rtf/rtfDestinations";
import { type RtfToken } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";
import { type RtfDestination, walkRtf } from "~/Brevredigering/LetterEditor/actions/rtf/walkRtf";

/**
 * Outlook puts mail bodies on the clipboard as RTF that wraps the original HTML (`\fromhtml1`) or
 * plain text (`\fromtext`). This restores the original, following MS-OXRTFEX section 2:
 * https://learn.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxrtfex
 *
 * - `{\*\htmltag …}` holds HTML markup, emitted as is.
 * - Text outside htmltag is document text, emitted HTML-escaped.
 * - `\htmlrtf` … `\htmlrtf0` wraps RTF-only duplicates of the content, which are dropped.
 */

export type EncapsulatedContent = { format: "html"; html: string } | { format: "text"; text: string };

type Format = EncapsulatedContent["format"];

/** The spec requires `\fromhtml1`/`\fromtext` in the header, before any other group or text. */
const HEADER_SCAN_LIMIT = 10;

/** Returns the encapsulated format declared in the RTF header, or undefined for other RTF. */
export function detectEncapsulationFormat(tokens: readonly RtfToken[]): Format | undefined {
  if (tokens[0]?.type !== "groupStart") return undefined;
  for (const token of tokens.slice(1, 1 + HEADER_SCAN_LIMIT)) {
    if (token.type !== "control") return undefined;
    if (token.word === "fromhtml" && token.param === 1) return "html";
    if (token.word === "fromtext") return "text";
  }
  return undefined;
}

const escapeHtml = (text: string) => text.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");

/** Returns the original HTML or text of Outlook's encapsulating RTF, or undefined for other RTF. */
export function extractEncapsulatedContent(
  tokens: readonly RtfToken[],
  decodeBytes: ByteDecoder,
): EncapsulatedContent | undefined {
  const format = detectEncapsulationFormat(tokens);
  if (!format) return undefined;

  const output: string[] = [];
  /** `\htmlrtf` is group scoped. */
  const htmlrtf: boolean[] = [false];

  const emit = (value: string, destination: RtfDestination) => {
    if (htmlrtf.at(-1)) return;
    if (destination === "htmltag") {
      if (format === "html") output.push(value);
    } else if (destination === "body") {
      output.push(format === "html" ? escapeHtml(value) : value);
    }
  };

  const events = walkRtf(tokens, {
    destinations: ENCAPSULATION_DESTINATIONS,
    decodeBytes,
  });
  for (const event of events) {
    switch (event.kind) {
      case "groupStart": {
        htmlrtf.push(htmlrtf.at(-1)!);
        break;
      }
      case "groupEnd": {
        htmlrtf.pop();
        break;
      }
      case "text": {
        emit(event.value, event.destination);
        break;
      }
      case "control": {
        const { word, hasParam, param } = event.token;
        if (word === "htmlrtf") htmlrtf[htmlrtf.length - 1] = !hasParam || param !== 0;
        // A nested document is Outlook's mail signature in plain RTF.
        else if (word === "rtf") htmlrtf[htmlrtf.length - 1] = false;
        else if (word === "par" || word === "line") emit("\r\n", event.destination);
        break;
      }
    }
  }

  const content = output.join("");
  return format === "html" ? { format, html: content } : { format, text: content };
}
