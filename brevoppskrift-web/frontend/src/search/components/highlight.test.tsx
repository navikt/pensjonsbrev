// @vitest-environment node
import { renderToStaticMarkup } from "react-dom/server";
import { describe, expect, it } from "vitest";

import { LineContent } from "~/search/components/highlight";
import { type Line } from "~/search/textSearch";

function textLine(value: string): Line {
  return [{ type: "text", value }];
}

function render(line: Line, needle: string): string {
  return renderToStaticMarkup(<LineContent line={line} needle={needle} />);
}

/** Returns the text content of every `<mark>...</mark>` run, in order. */
function marks(html: string): string[] {
  return [...html.matchAll(/<mark[^>]*>(.*?)<\/mark>/g)].map((m) => m[1]);
}

describe("LineContent highlighting", () => {
  it("highlights all occurrences of an exact term", () => {
    const html = render(textLine("blue sky, blue sea"), "blue");
    expect(marks(html)).toEqual(["blue", "blue"]);
  });

  it("highlights only the single best occurrence of a fuzzy-only term", () => {
    // "blie" has no exact substring in the text, but fuzzy-matches "blue".
    // Fuse's bitap matcher reports only the core matched span (not the full
    // word extent) for fuzzy hits, and only the first/best occurrence.
    const html = render(textLine("the color is blue here, very blue indeed"), "blie");
    expect(marks(html)).toEqual(["bl"]);
  });

  it("combines an exact term (all occurrences) and a fuzzy term (first occurrence) in the same line", () => {
    const html = render(textLine("blue sky, blue sea, very blie today"), "blue blie");
    // "blue" is exact -> both occurrences; "blie" is also an exact substring
    // of the text itself (it appears verbatim), so it too is highlighted via
    // the exact path (all occurrences, here just the one).
    expect(marks(html)).toEqual(["blue", "blue", "blie"]);
  });

  it("does not highlight variable segments", () => {
    const line: Line = [
      { type: "text", value: "Hei " },
      { type: "var", label: "fornavn" },
      { type: "text", value: ", her er blue" },
    ];
    const html = renderToStaticMarkup(<LineContent line={line} needle="fornavn blue" />);
    expect(marks(html)).toEqual(["blue"]);
    expect(html).toContain("fornavn");
  });

  it("renders a term plainly when it matches nothing in this text", () => {
    const html = render(textLine("helt urelatert tekst"), "xyzxyz");
    expect(marks(html)).toEqual([]);
    expect(html).toContain("helt urelatert tekst");
  });
});
