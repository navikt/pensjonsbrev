import { type HitRefs, NO_HITS, templateKey, type WorkerRequest, type WorkerResponse } from "~/search/searchProtocol";
import { buildIndex, type SearchIndex, type SearchResults, search } from "~/search/textSearch";

/** Replaces the templates in a result set with their keys, preserving order:
 *  content hits are already ranked by `toContentHits` and brev hits carry
 *  Fuse's ranking, and both orders are what the UI renders. */
function toHitRefs(results: SearchResults): HitRefs {
  return {
    content: results.content.map((hit) => ({
      key: templateKey(hit.template),
      lineIndex: hit.lineIndex,
      matchCount: hit.matchCount,
      score: hit.score,
    })),
    brev: results.brev.map((hit) => ({ key: templateKey(hit.template) })),
  };
}

export type SearchWorkerCore = {
  handle: (request: WorkerRequest) => WorkerResponse;
};

/** The whole worker, minus the messaging. Holds the Fuse indexes and answers
 *  requests synchronously, so all of it is unit-testable without a real
 *  `Worker` (which jsdom does not provide). `searchWorker.ts` is only the
 *  `postMessage` wiring around this. */
export function createSearchWorkerCore(): SearchWorkerCore {
  let index: SearchIndex | undefined;

  return {
    handle(request) {
      switch (request.type) {
        case "setCorpus": {
          index = buildIndex(request.corpus);
          return { type: "corpusReady", corpusVersion: request.corpusVersion };
        }
        case "search": {
          // A query can arrive before the first corpus does (or after a corpus
          // that failed to load entirely); answering with no hits keeps the
          // protocol request/response symmetric so the caller never hangs.
          const hits = index ? toHitRefs(search(index, request.query, request.exactOnly)) : NO_HITS;
          return { type: "results", requestId: request.requestId, hits };
        }
      }
    },
  };
}
