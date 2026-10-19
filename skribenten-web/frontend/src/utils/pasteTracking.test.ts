import { describe, expect, test, vi } from "vitest";

import { detectRtfCodePage, detectRtfDocumentLanguage, getPasteMetadata, hasPasteContent } from "~/utils/pasteTracking";

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

  test.each([
    ["Msftedit 5.41.21.2510", "RichEdit (WordPad e.l.)", "5.41.21.2510"],
    ["Riched20 10.0.22621", "RichEdit (WordPad e.l.)", "10.0.22621"],
    ["Microsoft Word 11.0.6113", "Word", "11.0.6113"],
    ["Riched20", "RichEdit (WordPad e.l.)", undefined],
    ["Riched20 10.0 Ola Nordmann", "RichEdit (WordPad e.l.)", undefined],
    ["Aspose.Words for Java 13.12.0.0", "Annen", undefined],
  ])("henter RTF-kilde fra \\generator %s som fast verdi", (generator, kilde, versjon) => {
    const rtf = `{\\rtf1\\ansi\\deff0{\\fonttbl{\\f0 Calibri;}}{\\*\\generator ${generator};}\\viewkind4 Hei\\par}`;

    const metadata = getPasteMetadata(clipboard(["text/rtf"], { "text/rtf": rtf }));

    expect(metadata.rtfKilde).toBe(kilde);
    expect(metadata.rtfKildeVersjon).toBe(versjon);
  });

  test("kjenner igjen TextEdit på \\cocoartf i RTF-hodet", () => {
    const rtf = "{\\rtf1\\ansi\\ansicpg1252\\cocoartf2580\n{\\fonttbl\\f0\\fswiss Helvetica;}\\f0 Hei\\par}";

    expect(getPasteMetadata(clipboard(["text/rtf"], { "text/rtf": rtf }))).toMatchObject({
      rtfKilde: "TextEdit (macOS)",
      rtfKildeVersjon: undefined,
    });
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

  test("sender aldri teksten fra \\generator videre", () => {
    const rtf = "{\\rtf1\\ansi{\\*\\generator Ola Nordmann 12345678901;} Hei\\par}";

    const metadata = getPasteMetadata(clipboard(["text/rtf"], { "text/rtf": rtf }));

    expect(metadata).toMatchObject({ rtfKilde: "Annen", rtfKildeVersjon: undefined });
    expect(JSON.stringify(metadata)).not.toMatch(/Ola|12345678901/);
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

describe("detectRtfCodePage", () => {
  test("leser \\ansicpg fra RTF-hodet", () => {
    expect(detectRtfCodePage("{\\rtf1\\ansi\\ansicpg1252\\deff0{\\fonttbl{\\f0 Calibri;}} Hei}")).toBe("1252");
    expect(detectRtfCodePage("{\\rtf1\\ansi\\ansicpg1251 Привет}")).toBe("1251");
  });

  test("ser bort fra \\ansicpg etter første gruppe og fra urimelig lange tall", () => {
    expect(detectRtfCodePage("{\\rtf1\\ansi{\\fonttbl{\\f0 Calibri;}}\\ansicpg1252 Hei}")).toBeUndefined();
    expect(detectRtfCodePage("{\\rtf1\\ansi\\ansicpg123456 Hei}")).toBeUndefined();
  });

  test("kommer med i metadataene for RTF", () => {
    const rtf = "{\\rtf1\\ansi\\ansicpg1250 Dobrý den\\par}";

    expect(getPasteMetadata(clipboard(["text/rtf"], { "text/rtf": rtf })).rtfKodetabell).toBe("1250");
  });
});

describe("hasPasteContent", () => {
  test("gjelder også utklippstavler med bare RTF eller bare HTML", () => {
    expect(hasPasteContent(clipboard(["text/rtf"], { "text/rtf": "{\\rtf1 Hei\\par}" }))).toBe(true);
    expect(hasPasteContent(clipboard(["application/rtf"], { "application/rtf": "{\\rtf1 Hei\\par}" }))).toBe(true);
    expect(hasPasteContent(clipboard(["text/html"], { "text/html": "<p>Hei</p>" }))).toBe(true);
    expect(hasPasteContent(clipboard(["text/plain"], { "text/plain": "Hei" }))).toBe(true);
  });

  test("er usann når alt er tomt", () => {
    expect(hasPasteContent(clipboard([]))).toBe(false);
    expect(hasPasteContent(clipboard(["text/plain", "text/rtf"], { "text/plain": "", "text/rtf": "" }))).toBe(false);
    expect(hasPasteContent(clipboard(["Files"]))).toBe(false);
  });
});
