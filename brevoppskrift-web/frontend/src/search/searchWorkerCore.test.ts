import { describe, expect, it } from "vitest";

import { templateKey } from "~/search/searchProtocol";
import { createSearchWorkerCore } from "~/search/searchWorkerCore";
import { type TemplateText } from "~/search/textSearch";

function template(id: string, title: string, lines: string[]): TemplateText {
  return {
    id,
    malType: "autobrev",
    title,
    language: "BOKMAL",
    lines: lines.map((value) => [{ type: "text", value }]),
    indexes: lines.map((_, index) => index),
  };
}

const corpus: TemplateText[] = [
  template("A1", "Alderspensjon", ["Vi har beregnet din alderspensjon", "Du får utbetalt beløpet hver måned"]),
  template("U1", "Uføretrygd", ["Vi har innvilget uføretrygd"]),
];

function coreWithCorpus() {
  const core = createSearchWorkerCore();
  core.handle({ type: "setCorpus", corpusVersion: 1, corpus });
  return core;
}

function searchHits(core: ReturnType<typeof createSearchWorkerCore>, query: string, exactOnly = false) {
  const response = core.handle({ type: "search", requestId: 1, query, exactOnly });
  if (response.type !== "results") {
    throw new Error(`Expected a results response, got "${response.type}"`);
  }
  return response.hits;
}

describe("searchWorkerCore", () => {
  it("acknowledges a corpus with the version it was given, so the client can ignore stale acks", () => {
    const core = createSearchWorkerCore();

    expect(core.handle({ type: "setCorpus", corpusVersion: 7, corpus })).toEqual({
      type: "corpusReady",
      corpusVersion: 7,
    });
  });

  it("answers a search that arrives before any corpus with no hits, rather than not answering at all", () => {
    const core = createSearchWorkerCore();

    const response = core.handle({ type: "search", requestId: 42, query: "alderspensjon", exactOnly: false });

    expect(response).toEqual({ type: "results", requestId: 42, hits: { content: [], brev: [] } });
  });

  it("echoes the requestId, so the client can drop responses to superseded queries", () => {
    const core = coreWithCorpus();

    const response = core.handle({ type: "search", requestId: 99, query: "alderspensjon", exactOnly: false });

    expect(response.type === "results" && response.requestId).toBe(99);
  });

  it("returns template keys and line positions rather than whole templates", () => {
    const core = coreWithCorpus();

    const hits = searchHits(core, "utbetalt");

    expect(hits.content).toEqual([
      { key: templateKey(corpus[0]), lineIndex: 1, matchCount: 1, score: expect.any(Number) },
    ]);
    expect(hits.brev).toEqual([]);
  });

  it("matches titles and brevkoder as brev hits", () => {
    const core = coreWithCorpus();

    expect(searchHits(core, "uføretrygd").brev).toEqual([{ key: templateKey(corpus[1]) }]);
  });

  it("returns content hits in ranked order, so the main thread can render them as-is", () => {
    const core = coreWithCorpus();

    // "vi har" matches both templates; the better-scoring one must come first.
    const hits = searchHits(core, "vi har");

    expect(hits.content.length).toBe(2);
    expect(hits.content[0].score).toBeLessThanOrEqual(hits.content[1].score);
  });

  it("respects exactOnly: a typo matches fuzzily but not exactly", () => {
    const core = coreWithCorpus();

    expect(searchHits(core, "alderspenjson", false).content).toHaveLength(1);
    expect(searchHits(core, "alderspenjson", true).content).toHaveLength(0);
  });

  it("searches only the newest corpus once it has been replaced", () => {
    const core = coreWithCorpus();
    expect(searchHits(core, "uføretrygd").content).toHaveLength(1);

    const replacement = [template("N1", "Ny mal", ["Helt nytt innhold"])];
    core.handle({ type: "setCorpus", corpusVersion: 2, corpus: replacement });

    expect(searchHits(core, "uføretrygd")).toEqual({ content: [], brev: [] });
    expect(searchHits(core, "nytt innhold").content).toEqual([
      { key: templateKey(replacement[0]), lineIndex: 0, matchCount: 1, score: expect.any(Number) },
    ]);
  });
});
