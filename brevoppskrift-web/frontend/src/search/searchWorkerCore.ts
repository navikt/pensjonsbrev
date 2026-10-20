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
  /** Answers a `search`; `setCorpus` produces no reply. Never throws: a
   *  failure is reported as an `error` reply instead. */
  handle: (request: WorkerRequest) => WorkerResponse | undefined;
};

function messageOf(error: unknown): string {
  return error instanceof Error ? error.message : String(error);
}

/** The whole worker, minus the messaging. Holds the Fuse indexes and answers
 *  requests synchronously, so all of it is unit-testable without a real
 *  `Worker` (which jsdom does not provide). `searchWorker.ts` is only the
 *  `postMessage` wiring around this. */
export function createSearchWorkerCore(): SearchWorkerCore {
  let index: SearchIndex | undefined;
  // Why the latest corpus could not be indexed. Kept so every search against
  // it fails visibly - answering with no hits would tell the user that
  // nothing matched, when in fact nothing was searched.
  let indexError: string | undefined;

  return {
    handle(request) {
      switch (request.type) {
        case "setCorpus": {
          try {
            index = buildIndex(request.corpus);
            indexError = undefined;
          } catch (error) {
            console.error("Kunne ikke bygge søkeindeksen", error);
            index = undefined;
            indexError = messageOf(error);
          }
          return undefined;
        }
        case "search": {
          const { requestId } = request;
          if (indexError !== undefined) {
            return { type: "error", requestId, message: indexError };
          }
          // A query can arrive before the first corpus does; answering with no
          // hits keeps the protocol request/response symmetric so the caller
          // never hangs.
          if (!index) {
            return { type: "results", requestId, hits: NO_HITS };
          }
          try {
            return { type: "results", requestId, hits: toHitRefs(search(index, request.query, request.exactOnly)) };
          } catch (error) {
            console.error("Søket feilet", error);
            return { type: "error", requestId, message: messageOf(error) };
          }
        }
      }
    },
  };
}
