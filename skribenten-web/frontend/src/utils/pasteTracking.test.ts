import { describe, expect, test } from "vitest";

import { getPasteMetadata } from "./pasteTracking";

function clipboard(types: string[], data: Record<string, string> = {}): Pick<DataTransfer, "getData" | "types"> {
  return {
    getData: (type) => data[type] ?? "",
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

    expect(metadata).toEqual({ innholdsformat: "HTML", htmlTagger: "br,p,strong" });
  });

  test("gjenkjenner RTF selv om utklippstavlen også inneholder ren tekst", () => {
    expect(getPasteMetadata(clipboard(["text/plain", "text/rtf"]))).toEqual({
      innholdsformat: "RTF",
      htmlTagger: undefined,
    });
  });

  test("bruker ren tekst som standard", () => {
    expect(getPasteMetadata(clipboard(["text/plain"]))).toEqual({
      innholdsformat: "ren tekst",
      htmlTagger: undefined,
    });
  });
});
