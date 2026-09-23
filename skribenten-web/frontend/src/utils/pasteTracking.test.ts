import { describe, expect, test, vi } from "vitest";

import { getPasteMetadata } from "~/utils/pasteTracking";

function clipboard(types: string[], data: Record<string, string> = {}): Pick<DataTransfer, "getData" | "types"> {
  return {
    getData: (type: string) => data[type] ?? "",
    types: types as readonly string[],
  };
}

describe("getPasteMetadata", () => {
  test("prioriterer HTML og returnerer unike, sorterte tagger", () => {
    const metadata = getPasteMetadata(
      clipboard(["text/plain", "text/html"], {
        "text/html": "<p><strong>Hei</strong><br><strong>igjen</strong></p>",
      }),
    );

    expect(metadata).toEqual({ innholdsformat: "HTML", htmlTagger: "br,p,strong", rtfDokumentSpraak: undefined });
  });

  test("hopper over tagguttrekk for svært stor HTML", () => {
    const metadata = getPasteMetadata(
      clipboard(["text/html"], {
        "text/html": `<p>${"a".repeat(100_000)}</p>`,
      }),
    );

    expect(metadata).toEqual({ innholdsformat: "HTML", htmlTagger: undefined, rtfDokumentSpraak: undefined });
  });

  test("feiler stille dersom DOM-parsingen kaster", () => {
    vi.spyOn(document, "createElement").mockImplementationOnce(() => {
      throw new Error("DOM parsing failed");
    });

    expect(getPasteMetadata(clipboard(["text/html"], { "text/html": "<p>Hei</p>" }))).toEqual({
      innholdsformat: "HTML",
      htmlTagger: undefined,
      rtfDokumentSpraak: undefined,
    });
  });

  test("gjenkjenner RTF selv om utklippstavlen også inneholder ren tekst", () => {
    expect(getPasteMetadata(clipboard(["text/plain", "text/rtf"]))).toEqual({
      innholdsformat: "RTF",
      htmlTagger: undefined,
      rtfDokumentSpraak: undefined,
    });
  });

  test("bruker ren tekst som standard", () => {
    expect(getPasteMetadata(clipboard(["text/plain"]))).toEqual({
      innholdsformat: "ren tekst",
      htmlTagger: undefined,
      rtfDokumentSpraak: undefined,
    });
  });

  test("henter dokumentspråk fra RTF-innholdet (telemetri)", () => {
    const metadata = getPasteMetadata(clipboard(["text/rtf"], { "text/rtf": "{\\rtf1\\ansi\\deflang1044 Hei\\par}" }));

    expect(metadata).toEqual({ innholdsformat: "RTF", htmlTagger: undefined, rtfDokumentSpraak: "nb-NO" });
  });

  test("leser RTF fra application/rtf når text/rtf mangler", () => {
    const metadata = getPasteMetadata(
      clipboard(["application/rtf"], { "application/rtf": "{\\rtf1\\ansi\\deflang1033 Hi\\par}" }),
    );

    expect(metadata).toEqual({ innholdsformat: "RTF", htmlTagger: undefined, rtfDokumentSpraak: "en-US" });
  });

  test("hopper over språkgjenkjenning for svært stor RTF", () => {
    const metadata = getPasteMetadata(
      clipboard(["text/rtf"], { "text/rtf": `{\\rtf1\\ansi\\deflang1033 ${"a".repeat(100_000)}\\par}` }),
    );

    expect(metadata).toEqual({ innholdsformat: "RTF", htmlTagger: undefined, rtfDokumentSpraak: undefined });
  });
});
