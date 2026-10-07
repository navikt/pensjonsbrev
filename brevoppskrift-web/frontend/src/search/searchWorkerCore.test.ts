import { afterEach, describe, expect, it, vi } from "vitest";

import { templateKey } from "~/search/searchProtocol";
import { createSearchWorkerCore } from "~/search/searchWorkerCore";
import { type TemplateText } from "~/search/textSearch";

// The real search, unless a test asks for it to throw - the only way to reach
// the core's error handling for a search against a perfectly good index.
const searchFailure = vi.hoisted(() => ({ error: undefined as Error | undefined }));
vi.mock("~/search/textSearch", async (importOriginal) => {
  const actual = await importOriginal<typeof import("~/search/textSearch")>();
  return {
    ...actual,
    search: (...args: Parameters<typeof actual.search>) => {
      if (searchFailure.error) {
        throw searchFailure.error;
      }
      return actual.search(...args);
    },
  };
});

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

/** A corpus that `buildIndex` cannot index: a template without lines. */
const unindexableCorpus = [{ ...corpus[0], lines: null }] as unknown as TemplateText[];

function coreWithCorpus() {
  const core = createSearchWorkerCore();
  core.handle({ type: "setCorpus", corpus });
  return core;
}

function searchHits(core: ReturnType<typeof createSearchWorkerCore>, query: string, exactOnly = false) {
  const response = core.handle({ type: "search", requestId: 1, query, exactOnly });
  if (response?.type !== "results") {
    throw new Error(`Expected a results response, got "${response?.type}"`);
  }
  return response.hits;
}

describe("searchWorkerCore", () => {
  afterEach(() => {
    searchFailure.error = undefined;
    vi.restoreAllMocks();
  });

  it("does not reply to a corpus: message order already makes later searches run against it", () => {
    const core = createSearchWorkerCore();

    expect(core.handle({ type: "setCorpus", corpus })).toBeUndefined();
  });

  it("answers every search against a corpus it could not index with an error, not with no hits", () => {
    vi.spyOn(console, "error").mockImplementation(() => undefined);
    const core = createSearchWorkerCore();

    core.handle({ type: "setCorpus", corpus: unindexableCorpus });

    for (const requestId of [1, 2]) {
      expect(core.handle({ type: "search", requestId, query: "alderspensjon", exactOnly: false })).toEqual({
        type: "error",
        requestId,
        message: expect.any(String),
      });
    }
  });

  it("recovers once a corpus that can be indexed replaces one that could not", () => {
    vi.spyOn(console, "error").mockImplementation(() => undefined);
    const core = createSearchWorkerCore();
    core.handle({ type: "setCorpus", corpus: unindexableCorpus });

    core.handle({ type: "setCorpus", corpus });

    expect(searchHits(core, "alderspensjon").content).toHaveLength(1);
  });

  it("answers a search that throws with an error carrying the requestId, instead of throwing", () => {
    vi.spyOn(console, "error").mockImplementation(() => undefined);
    const core = coreWithCorpus();
    searchFailure.error = new Error("Fuse gikk i stykker");

    expect(core.handle({ type: "search", requestId: 7, query: "alderspensjon", exactOnly: false })).toEqual({
      type: "error",
      requestId: 7,
      message: "Fuse gikk i stykker",
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

    expect(response?.type === "results" && response.requestId).toBe(99);
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
    core.handle({ type: "setCorpus", corpus: replacement });

    expect(searchHits(core, "uføretrygd")).toEqual({ content: [], brev: [] });
    expect(searchHits(core, "nytt innhold").content).toEqual([
      { key: templateKey(replacement[0]), lineIndex: 0, matchCount: 1, score: expect.any(Number) },
    ]);
  });
});
