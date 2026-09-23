import { detectRtfDocumentLanguage } from "~/Brevredigering/LetterEditor/actions/paste-rtf";

export type PasteFormat = "HTML" | "RTF" | "ren tekst";

export interface PasteMetadata {
  innholdsformat: PasteFormat;
  htmlTagger?: string;
  rtfDokumentSpraak?: string;
}

const MAX_HTML_LENGTH_FOR_TAG_EXTRACTION = 100_000;
const MAX_RTF_LENGTH_FOR_LANGUAGE_DETECTION = 100_000;

export function getPasteMetadata(clipboard: Pick<DataTransfer, "getData" | "types">): PasteMetadata {
  const types = Array.from(clipboard.types);
  const hasHtml = types.includes("text/html");
  const hasRtf = types.includes("text/rtf") || types.includes("application/rtf");

  return {
    innholdsformat: hasHtml ? "HTML" : hasRtf ? "RTF" : "ren tekst",
    htmlTagger: hasHtml ? extractHtmlTags(clipboard.getData("text/html")) : undefined,
    rtfDokumentSpraak: hasRtf
      ? getRtfDocumentLanguage(clipboard.getData("text/rtf") || clipboard.getData("application/rtf"))
      : undefined,
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

// Telemetry only (see write-template plan discussion): used to observe the real-world language
// distribution of pasted RTF documents, since we only detect headings for a fixed allowlist of
// English/Norwegian Word style names (see paste-rtf.ts).
function getRtfDocumentLanguage(rtf: string): string | undefined {
  if (rtf.length > MAX_RTF_LENGTH_FOR_LANGUAGE_DETECTION) return undefined;

  try {
    return detectRtfDocumentLanguage(rtf);
  } catch {
    return undefined;
  }
}
