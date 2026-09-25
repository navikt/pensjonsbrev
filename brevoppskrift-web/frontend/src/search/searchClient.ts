import { type HitRefs, type WorkerRequest, type WorkerResponse } from "~/search/searchProtocol";
import { createSearchWorkerCore } from "~/search/searchWorkerCore";
import { type TemplateText } from "~/search/textSearch";

export type SearchClient = {
  /** Hands the corpus over for indexing. Every search issued afterwards runs
   *  against it - the worker handles messages in order, so there is nothing to
   *  wait for. */
  setCorpus: (corpus: TemplateText[]) => void;
  /** Resolves with the hits, or with `undefined` if a newer query superseded
   *  this one before it was ever run - the caller should then simply keep
   *  waiting for that newer query instead. Rejects when the search itself
   *  failed, so the failure can be shown rather than read as "no hits". */
  search: (query: string, exactOnly: boolean) => Promise<HitRefs | undefined>;
  dispose: () => void;
};

function settle(response: WorkerResponse | undefined): Promise<HitRefs> {
  if (response?.type === "results") {
    return Promise.resolve(response.hits);
  }
  return Promise.reject(new Error(response?.message ?? "Søket ga ikke noe svar"));
}

/** Runs the search core in-process, on the main thread. Used as the fallback
 *  when `Worker` is unavailable (jsdom, so also every unit test) or when the
 *  worker fails, and injected explicitly by tests so they never depend on the
 *  environment sniffing below. */
export function createLocalSearchClient(): SearchClient {
  const core = createSearchWorkerCore();
  let currentCorpus: TemplateText[] | undefined;

  return {
    setCorpus(corpus) {
      // Identity, not equality: React Query's structural sharing keeps the same
      // reference for an unchanged corpus, and a StrictMode remount re-runs the
      // posting effect - neither should pay for a full reindex.
      if (corpus !== currentCorpus) {
        currentCorpus = corpus;
        core.handle({ type: "setCorpus", corpus });
      }
    },
    search(query, exactOnly) {
      return settle(core.handle({ type: "search", requestId: 0, query, exactOnly }));
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
  reject: (error: Error) => void;
};

export function createWorkerSearchClient(): SearchClient {
  const worker = new Worker(new URL("./searchWorker.ts", import.meta.url), { type: "module" });

  let nextRequestId = 1;
  let currentCorpus: TemplateText[] | undefined;

  // At most one search is in the worker at a time, with at most one waiting
  // behind it: while a search runs, later queries replace each other instead
  // of queueing up, so a fast typist never makes the worker chew through a
  // backlog of queries nobody is waiting for any more.
  let inFlight: { requestId: number; pending: PendingSearch } | undefined;
  let queued: PendingSearch | undefined;

  /** Set once the worker itself has died; from then on everything runs
   *  in-process. */
  let fallback: SearchClient | undefined;

  function sendSearch(pending: PendingSearch) {
    const requestId = nextRequestId++;
    inFlight = { requestId, pending };
    const request: WorkerRequest = { type: "search", requestId, query: pending.query, exactOnly: pending.exactOnly };
    worker.postMessage(request);
  }

  /** The worker reports a failing search as an `error` reply, so this only
   *  fires when the worker itself is gone - most likely because it failed to
   *  load at all. Search must not stay stuck on that: fall back to running the
   *  same core on the main thread, replaying the corpus and the newest
   *  outstanding query. The local core never throws, so this cannot stop
   *  halfway, and every caller's promise is settled one way or the other. */
  function activateFallback() {
    if (fallback) {
      return;
    }
    const local = createLocalSearchClient();
    fallback = local;
    worker.terminate();
    if (currentCorpus) {
      local.setCorpus(currentCorpus);
    }

    const outstanding = [inFlight?.pending, queued].filter((pending) => pending !== undefined);
    inFlight = undefined;
    queued = undefined;
    for (const pending of outstanding.slice(0, -1)) {
      pending.resolve(undefined);
    }
    const newest = outstanding.at(-1);
    if (newest) {
      local.search(newest.query, newest.exactOnly).then(newest.resolve, newest.reject);
    }
  }

  worker.addEventListener("message", (event) => {
    const response = event.data as WorkerResponse;
    if (inFlight?.requestId !== response.requestId) {
      return;
    }
    const { pending } = inFlight;
    inFlight = undefined;
    if (response.type === "results") {
      pending.resolve(response.hits);
    } else {
      pending.reject(new Error(response.message));
    }
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
      // Identity, not equality: React Query's structural sharing keeps the same
      // reference for an unchanged corpus, and a StrictMode remount re-runs the
      // posting effect - neither should pay for a full reindex.
      if (corpus === currentCorpus) {
        return;
      }
      currentCorpus = corpus;
      if (fallback) {
        fallback.setCorpus(corpus);
        return;
      }
      const request: WorkerRequest = { type: "setCorpus", corpus };
      worker.postMessage(request);
    },
    search(query, exactOnly) {
      if (fallback) {
        return fallback.search(query, exactOnly);
      }
      return new Promise<HitRefs | undefined>((resolve, reject) => {
        const pending: PendingSearch = { query, exactOnly, resolve, reject };
        if (inFlight) {
          queued?.resolve(undefined);
          queued = pending;
        } else {
          sendSearch(pending);
        }
      });
    },
    dispose() {
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
 *  entire corpus on every mount.
 *
 *  It serves one `useTemplateSearch` at a time. Only the newest query is ever
 *  kept waiting, so a second, concurrently mounted search would cancel the
 *  first one's queries (they resolve as superseded) and replace its corpus. */
export function sharedSearchClient(): SearchClient {
  sharedClient ??= createSearchClient();
  return sharedClient;
}
