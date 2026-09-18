import { type HitRefs, NO_HITS, type WorkerRequest, type WorkerResponse } from "~/search/searchProtocol";
import { createSearchWorkerCore } from "~/search/searchWorkerCore";
import { type TemplateText } from "~/search/textSearch";

export type SearchClient = {
  /** Hands the corpus over for indexing. Resolves once it is searchable. */
  setCorpus: (corpus: TemplateText[]) => Promise<void>;
  /** Resolves with the hits, or with `undefined` if a newer query superseded
   *  this one before it was ever run - the caller should then simply keep
   *  waiting for that newer query instead. */
  search: (query: string, exactOnly: boolean) => Promise<HitRefs | undefined>;
  dispose: () => void;
};

/** Runs the search core in-process, on the main thread. Used as the fallback
 *  when `Worker` is unavailable (jsdom, so also every unit test) or when the
 *  worker fails, and injected explicitly by tests so they never depend on the
 *  environment sniffing below. */
export function createLocalSearchClient(): SearchClient {
  const core = createSearchWorkerCore();
  let corpusVersion = 0;
  let currentCorpus: TemplateText[] | undefined;

  return {
    setCorpus(corpus) {
      // Identity, not equality: React Query's structural sharing keeps the same
      // reference for an unchanged corpus, and a StrictMode remount re-runs the
      // posting effect - neither should pay for a full reindex.
      if (corpus !== currentCorpus) {
        currentCorpus = corpus;
        corpusVersion++;
        core.handle({ type: "setCorpus", corpusVersion, corpus });
      }
      return Promise.resolve();
    },
    search(query, exactOnly) {
      const response = core.handle({ type: "search", requestId: 0, query, exactOnly });
      return Promise.resolve(response.type === "results" ? response.hits : NO_HITS);
    },
    dispose() {
      // Nothing to release: the core is plain in-process state, collected with
      // the client itself.
    },
  };
}

type PendingSearch = {
  query: string;
  exactOnly: boolean;
  resolve: (hits: HitRefs | undefined) => void;
};

export function createWorkerSearchClient(): SearchClient {
  const worker = new Worker(new URL("./searchWorker.ts", import.meta.url), { type: "module" });

  let nextRequestId = 1;
  let corpusVersion = 0;
  let currentCorpus: TemplateText[] | undefined;
  let corpusPromise: Promise<void> = Promise.resolve();
  let awaitingCorpus: { version: number; resolve: () => void } | undefined;

  // At most one search is in the worker at a time, with at most one waiting
  // behind it: while a search runs, later queries replace each other instead
  // of queueing up, so a fast typist never makes the worker chew through a
  // backlog of queries nobody is waiting for any more.
  let inFlight: { requestId: number; pending: PendingSearch } | undefined;
  let queued: PendingSearch | undefined;

  /** Set once the worker has failed; from then on everything runs in-process. */
  let fallback: SearchClient | undefined;

  function sendSearch(pending: PendingSearch) {
    const requestId = nextRequestId++;
    inFlight = { requestId, pending };
    const request: WorkerRequest = { type: "search", requestId, query: pending.query, exactOnly: pending.exactOnly };
    worker.postMessage(request);
  }

  /** A worker that fails (most likely failing to load at all) must not leave
   *  search permanently stuck on "indexing": fall back to running the same
   *  core on the main thread, replaying the corpus and the newest outstanding
   *  query so no caller is left with an unresolved promise. */
  function activateFallback() {
    if (fallback) {
      return;
    }
    const local = createLocalSearchClient();
    fallback = local;
    worker.terminate();

    const corpusWaiter = awaitingCorpus;
    awaitingCorpus = undefined;
    corpusPromise = currentCorpus ? local.setCorpus(currentCorpus) : Promise.resolve();
    if (corpusWaiter) {
      void corpusPromise.then(() => corpusWaiter.resolve());
    }

    const outstanding = [inFlight?.pending, queued].filter((pending) => pending !== undefined);
    inFlight = undefined;
    queued = undefined;
    for (const pending of outstanding.slice(0, -1)) {
      pending.resolve(undefined);
    }
    const newest = outstanding.at(-1);
    if (newest) {
      void corpusPromise.then(() => local.search(newest.query, newest.exactOnly)).then((hits) => newest.resolve(hits));
    }
  }

  worker.addEventListener("message", (event) => {
    const response = event.data as WorkerResponse;
    if (response.type === "corpusReady") {
      if (awaitingCorpus?.version === response.corpusVersion) {
        awaitingCorpus.resolve();
        awaitingCorpus = undefined;
      }
      return;
    }
    if (inFlight?.requestId !== response.requestId) {
      return;
    }
    const { pending } = inFlight;
    inFlight = undefined;
    pending.resolve(response.hits);
    if (queued) {
      const next = queued;
      queued = undefined;
      sendSearch(next);
    }
  });
  worker.addEventListener("error", activateFallback);
  worker.addEventListener("messageerror", activateFallback);

  return {
    setCorpus(corpus) {
      if (fallback) {
        currentCorpus = corpus;
        corpusPromise = fallback.setCorpus(corpus);
        return corpusPromise;
      }
      // Identity, not equality: React Query's structural sharing keeps the same
      // reference for an unchanged corpus, and a StrictMode remount re-runs the
      // posting effect - neither should pay for a full reindex.
      if (corpus === currentCorpus) {
        return corpusPromise;
      }
      currentCorpus = corpus;
      corpusVersion++;
      const version = corpusVersion;
      // Anyone waiting on the previous corpus is waiting on a corpus that no
      // longer exists; release them rather than leaving the promise dangling.
      awaitingCorpus?.resolve();
      corpusPromise = new Promise<void>((resolve) => {
        awaitingCorpus = { version, resolve };
      });
      const request: WorkerRequest = { type: "setCorpus", corpusVersion: version, corpus };
      worker.postMessage(request);
      return corpusPromise;
    },
    search(query, exactOnly) {
      if (fallback) {
        return fallback.search(query, exactOnly);
      }
      return new Promise<HitRefs | undefined>((resolve) => {
        const pending: PendingSearch = { query, exactOnly, resolve };
        if (inFlight) {
          queued?.resolve(undefined);
          queued = pending;
        } else {
          sendSearch(pending);
        }
      });
    },
    dispose() {
      awaitingCorpus?.resolve();
      awaitingCorpus = undefined;
      inFlight?.pending.resolve(undefined);
      inFlight = undefined;
      queued?.resolve(undefined);
      queued = undefined;
      fallback?.dispose();
      worker.terminate();
    },
  };
}

export function createSearchClient(): SearchClient {
  if (typeof Worker === "undefined") {
    return createLocalSearchClient();
  }
  try {
    return createWorkerSearchClient();
  } catch {
    return createLocalSearchClient();
  }
}

let sharedClient: SearchClient | undefined;

/** One worker for the whole page, created on first use. Deliberately not tied
 *  to a component's lifetime: React StrictMode double-invokes effects in dev,
 *  so an effect-owned worker would be torn down and forced to reindex the
 *  entire corpus on every mount. */
export function sharedSearchClient(): SearchClient {
  sharedClient ??= createSearchClient();
  return sharedClient;
}
