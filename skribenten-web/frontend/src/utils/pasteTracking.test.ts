import { describe, expect, test, vi } from "vitest";

import { detectRtfDocumentLanguage, getPasteMetadata, getRtfClipboardData } from "~/utils/pasteTracking";

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

  test("leser bare starten av svært stor RTF", () => {
    const metadata = getPasteMetadata(
      clipboard(["text/rtf"], { "text/rtf": `{\\rtf1\\ansi ${"a".repeat(100_000)}\\deflang1033 \\par}` }),
    );

    expect(metadata).toEqual({ innholdsformat: "RTF", htmlTagger: undefined, rtfDokumentSpraak: undefined });
  });

  test("henter RTF-kilde fra \\generator", () => {
    const rtf =
      "{\\rtf1\\ansi\\deff0{\\fonttbl{\\f0 Calibri;}}{\\*\\generator Msftedit 5.41.21.2510;}\\viewkind4 Hei\\par}";

    expect(getPasteMetadata(clipboard(["text/rtf"], { "text/rtf": rtf })).rtfKilde).toBe("Msftedit 5.41.21.2510");
  });

  test("kjenner igjen Outlook-RTF med innkapslet HTML", () => {
    const rtf = "{\\rtf1\\ansi\\ansicpg1252\\fromhtml1 \\deff0{\\fonttbl{\\f0 Arial;}}{\\*\\htmltag19 <html>}}";

    expect(getPasteMetadata(clipboard(["text/rtf"], { "text/rtf": rtf })).rtfKilde).toBe("Outlook (HTML)");
  });

  test("kjenner igjen Outlook-RTF med innkapslet ren tekst", () => {
    const rtf = "{\\rtf1\\ansi\\ansicpg1252\\fromtext \\deff0{\\fonttbl{\\f0 Arial;}} Hei\\par}";

    expect(getPasteMetadata(clipboard(["text/rtf"], { "text/rtf": rtf })).rtfKilde).toBe("Outlook (tekst)");
  });

  test("ser ikke etter \\fromhtml1 etter første gruppe", () => {
    const rtf = "{\\rtf1\\ansi{\\fonttbl{\\f0 Arial;}}\\fromhtml1 Hei\\par}";

    expect(getPasteMetadata(clipboard(["text/rtf"], { "text/rtf": rtf })).rtfKilde).toBeUndefined();
  });

  test("korter ned lange \\generator-navn", () => {
    const rtf = `{\\rtf1\\ansi{\\*\\generator ${"x".repeat(80)};} Hei\\par}`;

    expect(getPasteMetadata(clipboard(["text/rtf"], { "text/rtf": rtf })).rtfKilde).toBe("x".repeat(50));
  });
});

describe("getRtfClipboardData", () => {
  test("foretrekker text/rtf framfor application/rtf", () => {
    const data = { "text/rtf": "{\\rtf1 a}", "application/rtf": "{\\rtf1 b}" };

    expect(getRtfClipboardData(clipboard(["application/rtf", "text/rtf"], data))).toBe("{\\rtf1 a}");
  });

  test("bruker application/rtf når text/rtf er tom", () => {
    const data = { "text/rtf": "", "application/rtf": "{\\rtf1 b}" };

    expect(getRtfClipboardData(clipboard(["text/rtf", "application/rtf"], data))).toBe("{\\rtf1 b}");
  });

  test("returnerer undefined uten RTF, eller når RTF er tom", () => {
    expect(getRtfClipboardData(clipboard(["text/plain"], { "text/plain": "Hei" }))).toBeUndefined();
    expect(getRtfClipboardData(clipboard(["text/rtf"], { "text/rtf": "" }))).toBeUndefined();
  });
});

describe("detectRtfDocumentLanguage", () => {
  test("leser \\deflang og oversetter kjente LCID-er", () => {
    expect(detectRtfDocumentLanguage("{\\rtf1\\ansi\\deflang1033 text\\par}")).toBe("en-US");
    expect(detectRtfDocumentLanguage("{\\rtf1\\ansi\\deflang1044 text}")).toBe("nb-NO");
    expect(detectRtfDocumentLanguage("{\\rtf1\\ansi\\deflang2068 text}")).toBe("nn-NO");
  });

  test("bruker \\lang når \\deflang mangler", () => {
    expect(detectRtfDocumentLanguage("{\\rtf1\\ansi\\lang1033 text}")).toBe("en-US");
  });

  test("merker ukjente LCID-er i stedet for å droppe dem", () => {
    expect(detectRtfDocumentLanguage("{\\rtf1\\ansi\\deflang9999 text}")).toBe("unknown (9999)");
  });

  test("returnerer undefined uten språk", () => {
    expect(detectRtfDocumentLanguage("{\\rtf1\\ansi text}")).toBeUndefined();
  });
});
