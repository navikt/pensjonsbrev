import { getRtfClipboardData, parseRtfClipboard } from "~/Brevredigering/LetterEditor/actions/rtf/parseRtfClipboard";
import { type TraversedElement } from "~/Brevredigering/LetterEditor/actions/traversedElement";

import { htmlToElements } from "./htmlToElements";

export type Accept = (elements: TraversedElement[]) => TraversedElement[] | undefined;

export function readClipboard(clipboard: DataTransfer, accept: Accept): TraversedElement[] | undefined {
  for (const candidate of candidates(clipboard)) {
    const accepted = accept(candidate);
    if (accepted) return accepted;
  }
  return undefined;
}

function* candidates(clipboard: DataTransfer): Generator<TraversedElement[]> {
  const hasHtml = clipboard.types.includes("text/html");
  if (hasHtml) yield htmlToElements(clipboard.getData("text/html"));

  const rtf = getRtfClipboardData(clipboard);
  const fromRtf = rtf === undefined ? undefined : rtfElements(rtf);
  if (fromRtf) yield fromRtf;

  if (clipboard.types.includes("text/plain")) {
    const text = plainText(clipboard.getData("text/plain"));
    if (text) yield text;
  } else if (rtf === undefined && !hasHtml) {
    log(`unsupported clipboard datatype(s) - ${JSON.stringify(clipboard.types)}`);
  }
}

function rtfElements(rtf: string): TraversedElement[] | undefined {
  const parsed = parseRtfClipboard(rtf);
  switch (parsed.mode) {
    case "html":
      return htmlToElements(parsed.html);
    case "elements":
      return parsed.elements;
    case "text":
      return plainText(parsed.text);
    case "empty":
      return undefined;
    case "unsupported":
      log("unable to interpret rtf clipboard content", parsed.error);
      return undefined;
  }
}

function plainText(text: string): TraversedElement[] | undefined {
  return text.length > 0 ? [{ type: "TEXT", font: "PLAIN", text }] : undefined;
}

function log(message: string, ...details: unknown[]) {
  console.info(`Skribenten:pasteHandler: ${message}`, ...details);
}
