import { type AxiosAdapter, AxiosError, type AxiosResponse, type InternalAxiosRequestConfig } from "axios";
import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";

import { ApiProblem, resetCsrfTokenForTests, workshopApi, workshopHttp } from "./api";
import { newDraft } from "./draftOps";

type Call = { method: string; url: string; csrf: string | undefined };

function respond(config: InternalAxiosRequestConfig, status: number, data: unknown): AxiosResponse {
  return { data, status, statusText: "", headers: { "content-type": "application/json" }, config };
}

describe("workshop API client CSRF handling", () => {
  const original = workshopHttp.defaults.adapter;
  let calls: Call[];
  let tokenCounter: number;
  let rejectTokens: Set<string>;
  let status409 = false;

  beforeEach(() => {
    calls = [];
    tokenCounter = 0;
    rejectTokens = new Set();
    status409 = false;
    resetCsrfTokenForTests();
    const adapter: AxiosAdapter = async (config) => {
      const method = (config.method ?? "get").toLowerCase();
      const csrf = config.headers.get("X-CSRF-Token") as string | undefined;
      calls.push({ method, url: String(config.url), csrf });
      if (config.url === "/csrf") {
        return respond(config, 200, { token: `t${++tokenCounter}` });
      }
      if (method !== "get" && csrf && rejectTokens.has(csrf)) {
        const response = respond(config, 403, { status: 403, code: "CSRF_REJECTED" });
        throw new AxiosError("rejected", "ERR_BAD_REQUEST", config, null, response);
      }
      if (status409) {
        const response = respond(config, 409, { status: 409, code: "REVISION_CONFLICT", currentRevision: 7 });
        throw new AxiosError("conflict", "ERR_BAD_REQUEST", config, null, response);
      }
      return respond(config, 200, method === "get" ? [] : { files: [], issues: [] });
    };
    workshopHttp.defaults.adapter = adapter;
  });
  afterEach(() => {
    workshopHttp.defaults.adapter = original;
    vi.restoreAllMocks();
    vi.useRealTimers();
  });

  const draft = newDraft({ name: "m", type: "AUTOBREV", targetModule: "UFOERE", languages: ["NB"] });

  it("surfaces a stalled request as unavailable instead of loading indefinitely", async () => {
    vi.useFakeTimers();
    workshopHttp.defaults.adapter = "xhr";
    vi.spyOn(XMLHttpRequest.prototype, "send").mockImplementation(function (this: XMLHttpRequest) {
      setTimeout(() => this.dispatchEvent(new ProgressEvent("timeout")), this.timeout);
    });
    let settled = false;
    const outcome = workshopApi.me().then(
      () => {
        settled = true;
        return undefined;
      },
      (error: unknown) => {
        settled = true;
        return error;
      },
    );
    await vi.advanceTimersByTimeAsync(1_000);
    expect(settled).toBe(false);
    await vi.advanceTimersByTimeAsync(19_000);
    expect(await outcome).toMatchObject({ unavailable: true });
  });

  it("does not fetch a token for reads", async () => {
    await workshopApi.drafts();
    expect(calls.map((c) => c.url)).toEqual(["/utkast"]);
  });

  it("fetches the token lazily once and sends it on writes, including generation", async () => {
    await workshopApi.generate(draft);
    await workshopApi.generate(draft);
    expect(calls.filter((c) => c.url === "/csrf")).toHaveLength(1);
    expect(calls.filter((c) => c.url === "/generer").every((c) => c.csrf === "t1")).toBe(true);
  });

  it("refetches the token and retries once on CSRF_REJECTED", async () => {
    rejectTokens.add("t1");
    await workshopApi.generate(draft);
    const writes = calls.filter((c) => c.url === "/generer");
    expect(writes.map((c) => c.csrf)).toEqual(["t1", "t2"]);
  });

  it("never retries a write on a generic 409 and surfaces the conflict", async () => {
    status409 = true;
    await expect(workshopApi.save("id", 1, draft)).rejects.toMatchObject({ status: 409, currentRevision: 7 });
    expect(calls.filter((c) => c.url === "/utkast/id")).toHaveLength(1);
    expect(calls.filter((c) => c.url === "/csrf")).toHaveLength(1);
    await expect(workshopApi.save("id", 1, draft)).rejects.toBeInstanceOf(ApiProblem);
  });
});
