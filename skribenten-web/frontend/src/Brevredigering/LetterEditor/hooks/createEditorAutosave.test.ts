import { describe, expect, it, vi } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { brevResponse } from "~test/support/brevFixtures";

import { type LetterEditorState } from "../model/state";
import { createEditorAutosave } from "./createEditorAutosave";

const deferred = <Value>() => {
  let resolve!: (value: Value) => void;
  let reject!: (error: Error) => void;
  const promise = new Promise<Value>((resolvePromise, rejectPromise) => {
    resolve = resolvePromise;
    reject = rejectPromise;
  });
  return { promise, resolve, reject };
};

const initialState = Actions.create(brevResponse());

const setup = () => {
  const requests: ReturnType<typeof deferred<LetterEditorState>>[] = [];
  const save = vi.fn(() => {
    const request = deferred<LetterEditorState>();
    requests.push(request);
    return request.promise;
  });
  const applyResponse = vi.fn((_state: LetterEditorState, response: LetterEditorState) => response);
  const onSaved = vi.fn();
  const controller = createEditorAutosave({ initialState, save, applyResponse, onSaved });
  const edit = () =>
    controller.update((state) => ({
      ...state,
      redigertBrev: { ...state.redigertBrev },
      saveStatus: "DIRTY",
    }));
  return { controller, edit, save, requests, applyResponse, onSaved };
};

describe("createEditorAutosave", () => {
  it("acknowledges only the sent revision and then saves the latest draft", async () => {
    const { controller, edit, save, requests, applyResponse } = setup();
    edit();
    const firstDraft = controller.getSnapshot().editorState.redigertBrev;
    const flushed = controller.flush();
    await Promise.resolve();
    edit();
    edit();
    const latestDraft = controller.getSnapshot().editorState.redigertBrev;
    expect(save).toHaveBeenCalledTimes(1);
    requests[0].resolve(initialState);
    await Promise.resolve();
    expect(applyResponse).not.toHaveBeenCalled();
    expect(controller.getSnapshot().editorState.redigertBrev).toBe(latestDraft);
    expect(controller.getSnapshot().editorState.saveStatus).not.toBe("SAVED");
    expect(save.mock.calls).toHaveLength(2);
    expect(firstDraft).not.toBe(latestDraft);
    requests[1].resolve({ ...initialState, redigertBrev: latestDraft });
    await flushed;
    expect(controller.getSnapshot().editorState.saveStatus).toBe("SAVED");
    expect(applyResponse).toHaveBeenCalledTimes(1);
  });

  it("shares one save process and propagates its failure without retrying", async () => {
    const { controller, edit, save, requests } = setup();
    edit();
    const first = controller.flush();
    const second = controller.flush();
    await Promise.resolve();
    requests[0].reject(new Error("save failed"));
    await Promise.all([expect(first).rejects.toThrow("save failed"), expect(second).rejects.toThrow("save failed")]);
    expect(save).toHaveBeenCalledTimes(1);
    expect(controller.canAutosave()).toBe(false);
    expect(controller.getSnapshot().saveFailed).toBe(true);
    expect(controller.getSnapshot().editorState.saveStatus).toBe("DIRTY");
    const retry = controller.flush();
    await Promise.resolve();
    requests[1].resolve(initialState);
    await retry;
    expect(controller.getSnapshot().saveFailed).toBe(false);
  });

  it("can save a newer draft after an older request fails", async () => {
    const { controller, edit, requests } = setup();
    edit();
    const flushed = controller.flush();
    await Promise.resolve();
    edit();
    requests[0].reject(new Error("old save failed"));
    await Promise.resolve();
    requests[1].resolve(initialState);
    await flushed;
    expect(controller.getSnapshot().editorState.saveStatus).toBe("SAVED");
  });

  it("does not save focus-only changes or save responses again", async () => {
    const { controller, save } = setup();
    controller.update((state) => ({ ...state, focus: { blockIndex: 1, contentIndex: 0 } }));
    await controller.flush();
    expect(save).not.toHaveBeenCalled();
    expect(controller.getSnapshot().revision).toBe(0);
  });

  it("waits for a running save before resetting and discards pending edits only after reset succeeds", async () => {
    const { controller, edit, requests, save } = setup();
    edit();
    const flushed = controller.flush();
    const joined = controller.flush();
    const finished = vi.fn();
    void flushed.then(finished);
    void joined.then(finished);
    await Promise.resolve();
    edit();
    const operation = deferred<LetterEditorState>();
    const resetOperation = vi.fn(() => operation.promise);
    const reset = controller.reset(resetOperation);
    const flushDuringReset = controller.flush();
    void flushDuringReset.then(finished);
    await Promise.resolve();
    expect(resetOperation).not.toHaveBeenCalled();
    requests[0].resolve(initialState);
    await vi.waitFor(() => expect(resetOperation).toHaveBeenCalledTimes(1));
    expect(finished).not.toHaveBeenCalled();
    expect(save).toHaveBeenCalledTimes(1);
    operation.resolve(initialState);
    await reset;
    await flushDuringReset;
    await flushed;
    await joined;
    expect(finished).toHaveBeenCalledTimes(3);
    expect(controller.getSnapshot().editorState.saveStatus).toBe("SAVED");
    expect(controller.getSnapshot().resetting).toBe(false);
  });

  it("propagates reset failure to callers that started, joined, or flushed during reset", async () => {
    const { controller, edit, requests, save } = setup();
    edit();
    const flushed = controller.flush();
    const joined = controller.flush();
    await Promise.resolve();
    edit();
    const draft = controller.getSnapshot().editorState.redigertBrev;
    const operation = deferred<LetterEditorState>();
    const resetOperation = vi.fn(() => operation.promise);
    const reset = controller.reset(resetOperation);
    const flushDuringReset = controller.flush();
    const failures = Promise.all(
      [flushed, joined, reset, flushDuringReset].map((result) => expect(result).rejects.toThrow("reset failed")),
    );

    requests[0].resolve(initialState);
    await vi.waitFor(() => expect(resetOperation).toHaveBeenCalledTimes(1));
    operation.reject(new Error("reset failed"));
    await failures;

    expect(save).toHaveBeenCalledTimes(1);
    expect(controller.getSnapshot().editorState.redigertBrev).toBe(draft);
    expect(controller.getSnapshot().editorState.saveStatus).toBe("DIRTY");
    expect(controller.getSnapshot().resetting).toBe(false);
    expect(controller.canAutosave()).toBe(true);
  });

  it("keeps pending edits when reset fails", async () => {
    const { controller, edit } = setup();
    edit();
    const draft = controller.getSnapshot().editorState.redigertBrev;
    await expect(
      controller.reset(async () => {
        throw new Error("reset failed");
      }),
    ).rejects.toThrow("reset failed");
    expect(controller.getSnapshot().editorState.redigertBrev).toBe(draft);
    expect(controller.canAutosave()).toBe(true);
  });

  it("propagates failure of the latest draft even when an older save also failed", async () => {
    const { controller, edit, requests } = setup();
    edit();
    const flushed = controller.flush();
    await Promise.resolve();
    edit();
    requests[0].reject(new Error("old save failed"));
    await Promise.resolve();
    requests[1].reject(new Error("latest save failed"));
    await expect(flushed).rejects.toThrow("latest save failed");
    expect(controller.canAutosave()).toBe(false);
    expect(controller.getSnapshot().editorState.saveStatus).toBe("DIRTY");
  });

  it("keeps draining actual edits without an arbitrary attempt limit", async () => {
    const { controller, edit, requests, save } = setup();
    edit();
    const flushed = controller.flush();
    await Promise.resolve();
    for (let requestIndex = 0; requestIndex < 7; requestIndex++) {
      edit();
      requests[requestIndex].resolve(initialState);
      await Promise.resolve();
      expect(controller.getSnapshot().editorState.saveStatus).not.toBe("SAVED");
    }
    requests[7].resolve(initialState);
    await flushed;
    expect(save).toHaveBeenCalledTimes(8);
    expect(controller.getSnapshot().editorState.saveStatus).toBe("SAVED");
  });
});
