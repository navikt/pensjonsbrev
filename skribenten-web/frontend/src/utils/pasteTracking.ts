export type PasteFormat = "HTML" | "RTF" | "ren tekst";

export interface PasteMetadata {
  innholdsformat: PasteFormat;
  htmlTagger?: string;
  rtfDokumentSpraak?: string;
  rtfKilde?: string;
  /** `\ansicpgN` from the RTF header, to learn whether non-Western code pages reach the RTF paste path. */
  rtfKodetabell?: string;
}

type Clipboard = Pick<DataTransfer, "getData" | "types">;

const MAX_HTML_LENGTH_FOR_TAG_EXTRACTION = 100_000;
/** `\deflang` and `\generator` are in the RTF header, so only the start of large documents is read. */
const MAX_RTF_HEADER_LENGTH = 100_000;
const MAX_RTF_SOURCE_LENGTH = 50;

const RTF_TYPES = ["text/rtf", "application/rtf"] as const;

/** RTF is exposed as "text/rtf" or "application/rtf" depending on OS and browser. Empty RTF counts as none. */
export function getRtfClipboardData(clipboard: Clipboard): string | undefined {
  const types = Array.from(clipboard.types);
  for (const type of RTF_TYPES) {
    const data = types.includes(type) ? clipboard.getData(type) : "";
    if (data) return data;
  }
  return undefined;
}

/** Whether the clipboard has anything to paste; RTF-only clipboards have no text/plain. */
export function hasPasteContent(clipboard: Clipboard): boolean {
  return (
    clipboard.getData("text/plain").length > 0 ||
    clipboard.getData("text/html").length > 0 ||
    getRtfClipboardData(clipboard) !== undefined
  );
}

export function getPasteMetadata(clipboard: Clipboard): PasteMetadata {
  const types = Array.from(clipboard.types);
  const hasHtml = types.includes("text/html");
  const hasRtf = RTF_TYPES.some((type) => types.includes(type));
  const rtfHeader = hasRtf ? getRtfClipboardData(clipboard)?.slice(0, MAX_RTF_HEADER_LENGTH) : undefined;

  return {
    innholdsformat: hasHtml ? "HTML" : hasRtf ? "RTF" : "ren tekst",
    htmlTagger: hasHtml ? extractHtmlTags(clipboard.getData("text/html")) : undefined,
    rtfDokumentSpraak: rtfHeader === undefined ? undefined : detectRtfDocumentLanguage(rtfHeader),
    rtfKilde: rtfHeader === undefined ? undefined : detectRtfSource(rtfHeader),
    rtfKodetabell: rtfHeader === undefined ? undefined : detectRtfCodePage(rtfHeader),
  };
}

function extractHtmlTags(html: string): string | undefined {
  if (html.length > MAX_HTML_LENGTH_FOR_TAG_EXTRACTION) return undefined;

  try {
    const template = document.createElement("template");
    template.innerHTML = html;
    const tags = [
      ...new Set(Array.from(template.content.querySelectorAll("*")).map((element) => element.tagName.toLowerCase())),
    ]
      .sort()
      .slice(0, 25);

    return tags.length > 0 ? tags.join(",") : undefined;
  } catch {
    return undefined;
  }
}

const LCID_LABELS: ReadonlyMap<number, string> = new Map([
  [1033, "en-US"],
  [2057, "en-GB"],
  [1044, "nb-NO"],
  [2068, "nn-NO"],
]);

export function detectRtfDocumentLanguage(rtf: string): string | undefined {
  const match = /\\deflang(\d+)/.exec(rtf) ?? /\\lang(\d+)/.exec(rtf);
  if (!match) return undefined;

  const lcid = Number.parseInt(match[1], 10);
  return LCID_LABELS.get(lcid) ?? `unknown (${lcid})`;
}

/** Which application produced the RTF, to learn which sources actually reach the RTF paste path. */
export function detectRtfSource(rtf: string): string | undefined {
  const header = rtfHeaderGroup(rtf);
  if (/\\fromhtml1/.test(header)) return "Outlook (HTML)";
  if (/\\fromtext/.test(header)) return "Outlook (tekst)";

  const generator = /\{\\\*\\generator ([^;}]*)/.exec(rtf)?.[1]?.trim();
  return generator ? generator.slice(0, MAX_RTF_SOURCE_LENGTH) : undefined;
}

export function detectRtfCodePage(rtf: string): string | undefined {
  return /\\ansicpg(\d{1,5})(?!\d)/.exec(rtfHeaderGroup(rtf))?.[1];
}

/** The document's own control words, before its first group (the font table etc.). */
function rtfHeaderGroup(rtf: string): string {
  return /^\{\\rtf1?[^{}]*/.exec(rtf)?.[0] ?? "";
}
