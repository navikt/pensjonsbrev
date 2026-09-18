import { afterEach, describe, expect, it, vi } from "vitest";

import { createWorkerSearchClient } from "~/search/searchClient";
import { type WorkerRequest, type WorkerResponse } from "~/search/searchProtocol";
import { createSearchWorkerCore } from "~/search/searchWorkerCore";
import { type TemplateText } from "~/search/textSearch";

const corpus: TemplateText[] = [
  {
    id: "A1",
    malType: "autobrev",
    title: "Alderspensjon",
    language: "BOKMAL",
    lines: [[{ type: "text", value: "Vi har beregnet din alderspensjon" }]],
    indexes: [0],
  },
];

/** A `Worker` stand-in that only answers when the test tells it to, so the
 *  client's queueing can be inspected at a point where a real worker would
 *  still be busy. jsdom provides no `Worker` at all, hence the global stub. */
class ControllableWorker {
  static instances: ControllableWorker[] = [];
  private readonly core = createSearchWorkerCore();
  private readonly listeners = new Map<string, ((event: unknown) => void)[]>();
  readonly received: WorkerRequest[] = [];
  private readonly replies: WorkerResponse[] = [];
  terminated = false;

  constructor() {
    ControllableWorker.instances.push(this);
  }

  addEventListener(type: string, listener: (event: unknown) => void) {
    this.listeners.set(type, [...(this.listeners.get(type) ?? []), listener]);
  }

  postMessage(request: WorkerRequest) {
    this.received.push(request);
    this.replies.push(this.core.handle(request));
  }

  terminate() {
    this.terminated = true;
  }

  /** Delivers every reply the worker has produced but not yet sent. */
  flush() {
    for (const reply of this.replies.splice(0)) {
      for (const listener of this.listeners.get("message") ?? []) {
        listener({ data: reply });
      }
    }
  }

  fail() {
    for (const listener of this.listeners.get("error") ?? []) {
      listener(new Event("error"));
    }
  }
}

function installWorkerStub() {
  ControllableWorker.instances = [];
  vi.stubGlobal("Worker", ControllableWorker);
  return ControllableWorker;
}

function searchRequests(worker: ControllableWorker) {
  return worker.received.filter((request) => request.type === "search");
}

describe("createWorkerSearchClient", () => {
  afterEach(() => {
    vi.unstubAllGlobals();
  });

  it("reindexes only when the corpus reference actually changes", async () => {
    installWorkerStub();
    const client = createWorkerSearchClient();
    const worker = ControllableWorker.instances[0];

    const first = client.setCorpus(corpus);
    worker.flush();
    await first;
    const again = client.setCorpus(corpus);
    worker.flush();
    await again;

    expect(worker.received.filter((request) => request.type === "setCorpus")).toHaveLength(1);
    client.dispose();
  });

  it("keeps only the newest query while one is in flight, resolving the skipped one as superseded", async () => {
    installWorkerStub();
    const client = createWorkerSearchClient();
    const worker = ControllableWorker.instances[0];
    void client.setCorpus(corpus);
    worker.flush();

    const inFlight = client.search("alderspensjon", false);
    const skipped = client.search("bereg", false);
    const newest = client.search("beregnet", false);

    // Only the first query has reached the worker; the two later ones are
    // still queued, with the middle one already discarded.
    expect(searchRequests(worker)).toHaveLength(1);
    await expect(skipped).resolves.toBeUndefined();

    worker.flush();
    await expect(inFlight).resolves.toEqual({ content: expect.any(Array), brev: expect.any(Array) });

    // Only now is the newest query sent - the discarded one never is.
    expect(searchRequests(worker).map((request) => request.type === "search" && request.query)).toEqual([
      "alderspensjon",
      "beregnet",
    ]);
    worker.flush();
    expect((await newest)?.content).toHaveLength(1);
    client.dispose();
  });

  it("falls back to searching on the main thread when the worker errors, instead of hanging", async () => {
    installWorkerStub();
    const client = createWorkerSearchClient();
    const worker = ControllableWorker.instances[0];
    void client.setCorpus(corpus);
    worker.flush();

    const pending = client.search("alderspensjon", false);
    worker.fail();

    expect((await pending)?.content).toHaveLength(1);
    expect(worker.terminated).toBe(true);
    // Later queries are answered locally too, without reaching the worker.
    const requestsBefore = searchRequests(worker).length;
    expect((await client.search("beregnet", false))?.content).toHaveLength(1);
    expect(searchRequests(worker)).toHaveLength(requestsBefore);
    client.dispose();
  });

  it("resolves outstanding queries as superseded on dispose, so no caller is left waiting", async () => {
    installWorkerStub();
    const client = createWorkerSearchClient();
    const worker = ControllableWorker.instances[0];
    void client.setCorpus(corpus);
    worker.flush();

    const pending = client.search("alderspensjon", false);
    client.dispose();

    await expect(pending).resolves.toBeUndefined();
    expect(worker.terminated).toBe(true);
  });
});
