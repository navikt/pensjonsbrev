export type PasteFormat = "HTML" | "RTF" | "ren tekst";

export interface PasteMetadata {
  innholdsformat: PasteFormat;
  htmlTagger?: string;
}

export function getPasteMetadata(clipboard: Pick<DataTransfer, "getData" | "types">): PasteMetadata {
  const types = Array.from(clipboard.types);
  const hasHtml = types.includes("text/html");
  const hasRtf = types.includes("text/rtf") || types.includes("application/rtf");

  return {
    innholdsformat: hasHtml ? "HTML" : hasRtf ? "RTF" : "ren tekst",
    htmlTagger: hasHtml ? extractHtmlTags(clipboard.getData("text/html")) : undefined,
  };
}

function extractHtmlTags(html: string): string | undefined {
  const template = document.createElement("template");
  template.innerHTML = html;
  const tags = [
    ...new Set(Array.from(template.content.querySelectorAll("*")).map((element) => element.tagName.toLowerCase())),
  ]
    .sort()
    .slice(0, 25);

  return tags.length > 0 ? tags.join(",") : undefined;
}
