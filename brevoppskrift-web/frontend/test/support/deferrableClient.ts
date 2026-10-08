import { createLocalSearchClient, type SearchClient } from "~/search/searchClient";

export type DeferrableClient = SearchClient & {
  /** Answers every search held so far. */
  release: () => void;
  /** Fails every search held so far. */
  fail: () => void;
};

/** Wraps the in-process client so tests can hold a search open and inspect what
 *  the UI shows while the worker would still be busy, then either answer it
 *  (`release`) or fail it (`fail`). */
export function deferrableClient(): DeferrableClient {
  const local = createLocalSearchClient();
  type Held = { run: () => void; reject: (error: Error) => void };
  const held: Held[] = [];
  const take = () => held.splice(0);
  return {
    setCorpus: (corpus) => local.setCorpus(corpus),
    search: (query, exactOnly) =>
      new Promise((resolve, reject) => {
        held.push({ run: () => void local.search(query, exactOnly).then(resolve, reject), reject });
      }),
    dispose: () => local.dispose(),
    release() {
      for (const { run } of take()) {
        run();
      }
    },
    fail() {
      for (const { reject } of take()) {
        reject(new Error("Søket feilet"));
      }
    },
  };
}
