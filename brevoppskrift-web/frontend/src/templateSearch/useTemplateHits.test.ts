import { describe, expect, it } from "vitest";

import { type ContentHit, type TemplateText } from "~/search/textSearch";
import { type DisplayedSearch } from "~/search/useSearchResults";
import { blockNavigation, hitsInTemplate, type TemplateHits } from "~/templateSearch/useTemplateHits";

function template(id: string, language: string, indexes: number[]): TemplateText {
  return {
    id,
    malType: "autobrev",
    title: id,
    language,
    lines: indexes.map(() => [{ type: "text", value: "tekst" }]),
    indexes,
  };
}

function contentHit(text: TemplateText, matchedLineIndexes: number[]): ContentHit {
  return {
    template: text,
    lineIndex: matchedLineIndexes[0],
    matchCount: matchedLineIndexes.length,
    matchedLineIndexes,
    score: 0,
  };
}

// Two lines can belong to the same block (e.g. a paragraph with a list), so
// lines 1 and 2 of A both sit in block 20. Block indexes follow the template's
// structure, so a block has the same index in every language.
const aBokmal = template("A", "BOKMAL", [10, 20, 20, 30]);
const bBokmal = template("B", "BOKMAL", [5, 6]);
const aNynorsk = template("A", "NYNORSK", [10, 20]);

function displayed(contentHits: ContentHit[]): DisplayedSearch {
  return {
    highlight: { needle: "tekst", exactOnly: false },
    contentHits,
    brevHits: contentHits.map(({ template }) => ({ template })),
    contentTemplateCount: 0,
    contentLineCount: 0,
    brevTemplateCount: 0,
  };
}

const search = displayed([contentHit(aNynorsk, [1]), contentHit(aBokmal, [2, 1, 3]), contentHit(bBokmal, [1])]);

function ready(hits: TemplateHits) {
  if (hits.status !== "ready") {
    throw new Error(`Expected ready hits, got ${hits.status}`);
  }
  return hits;
}

describe("hitsInTemplate", () => {
  it("finds each matching block once, in document order, and opens at the first", () => {
    const hits = ready(
      hitsInTemplate(search, "innhold", { malType: "autobrev", id: "A", language: "BOKMAL" }, undefined, true),
    );

    expect(hits.matchingBlocks).toEqual([20, 30]);
    expect(hits.activeBlock).toBe(20);
  });

  it("opens at the requested block, also when it is not a hit", () => {
    const current = { malType: "autobrev" as const, id: "A", language: "BOKMAL" };

    expect(ready(hitsInTemplate(search, "innhold", current, 30, true)).activeBlock).toBe(30);
    expect(ready(hitsInTemplate(search, "innhold", current, 10, true)).activeBlock).toBe(10);
  });

  it("counts templates and moves to the next template at its best hit", () => {
    const hits = ready(
      hitsInTemplate(search, "innhold", { malType: "autobrev", id: "A", language: "BOKMAL" }, 30, true),
    );
    expect(hits.result).toMatchObject({ position: 1, total: 2 });
    expect(hits.previousResult).toBeUndefined();
    expect(hits.nextResult).toEqual({ ref: { malType: "autobrev", id: "B", language: "BOKMAL" }, block: 6 });
  });

  it("has no blocks in a language without hits, but keeps its place among the templates", () => {
    const withoutNynorsk = displayed([contentHit(aBokmal, [1]), contentHit(bBokmal, [0])]);

    const hits = ready(
      hitsInTemplate(withoutNynorsk, "innhold", { malType: "autobrev", id: "A", language: "NYNORSK" }, undefined, true),
    );

    expect(hits.matchingBlocks).toEqual([]);
    expect(hits.activeBlock).toBeUndefined();
    expect(hits.result).toMatchObject({ position: 1, total: 2 });
    expect(hits.nextResult?.ref.id).toBe("B");
  });

  it("keeps the requested block in a language without hits", () => {
    const withoutNynorsk = displayed([contentHit(aBokmal, [1]), contentHit(bBokmal, [0])]);
    const current = { malType: "autobrev" as const, id: "A", language: "NYNORSK" };

    expect(ready(hitsInTemplate(withoutNynorsk, "innhold", current, 20, true)).activeBlock).toBe(20);
  });

  it("navigates the Brev tab's results without a block to open at", () => {
    const hits = ready(
      hitsInTemplate(search, "brev", { malType: "autobrev", id: "A", language: "NYNORSK" }, 20, false),
    );
    expect(hits.nextResult).toEqual({ ref: { malType: "autobrev", id: "B", language: "BOKMAL" }, block: undefined });
  });

  it("focuses no block unless asked to, but keeps the block hits to step through", () => {
    const current = { malType: "autobrev" as const, id: "A", language: "BOKMAL" };

    const unfocused = ready(hitsInTemplate(search, "brev", current, undefined, false));
    expect(unfocused.matchingBlocks).toEqual([20, 30]);
    expect(unfocused.activeBlock).toBeUndefined();
    expect(blockNavigation(unfocused.matchingBlocks, unfocused.activeBlock).nextBlock).toBe(20);

    expect(ready(hitsInTemplate(search, "brev", current, undefined, true)).activeBlock).toBe(20);
    expect(ready(hitsInTemplate(search, "brev", current, 30, false)).activeBlock).toBe(30);
  });
});

describe("blockNavigation", () => {
  const blocks = [20, 30, 40];

  it("steps to the neighbouring matches of a matching block", () => {
    expect(blockNavigation(blocks, 30)).toEqual({ at: 1, previousBlock: 20, nextBlock: 40 });
    expect(blockNavigation(blocks, 20)).toEqual({ at: 0, previousBlock: undefined, nextBlock: 30 });
  });

  it("steps to the nearest matches around a block that is not a match", () => {
    expect(blockNavigation(blocks, 35)).toEqual({ at: undefined, previousBlock: 30, nextBlock: 40 });
    expect(blockNavigation(blocks, 50)).toEqual({ at: undefined, previousBlock: 40, nextBlock: undefined });
  });

  it("steps to the first match without an active block", () => {
    expect(blockNavigation(blocks, undefined)).toEqual({ at: undefined, previousBlock: undefined, nextBlock: 20 });
  });
});
