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
  const request = async (index: number) => {
    await vi.waitFor(() => expect(requests.length).toBeGreaterThan(index));
    return requests[index];
  };
  return { controller, edit, save, request, applyResponse, onSaved };
};

describe("createEditorAutosave", () => {
  it("acknowledges only the sent revision and then saves the latest draft", async () => {
    const { controller, edit, save, request, applyResponse } = setup();
    edit();
    const firstDraft = controller.getSnapshot().editorState.redigertBrev;
    const saving = controller.savePendingChanges();
    const firstRequest = await request(0);
    edit();
    edit();
    const latestDraft = controller.getSnapshot().editorState.redigertBrev;
    expect(save).toHaveBeenCalledTimes(1);
    firstRequest.resolve(initialState);
    const secondRequest = await request(1);
    expect(applyResponse).not.toHaveBeenCalled();
    expect(controller.getSnapshot().editorState.redigertBrev).toBe(latestDraft);
    expect(controller.getSnapshot().editorState.saveStatus).not.toBe("SAVED");
    expect(firstDraft).not.toBe(latestDraft);
    secondRequest.resolve({ ...initialState, redigertBrev: latestDraft });
    await saving;
    expect(controller.getSnapshot().editorState.saveStatus).toBe("SAVED");
    expect(applyResponse).toHaveBeenCalledTimes(1);
  });

  it("shares one save process and propagates its failure without retrying", async () => {
    const { controller, edit, save, request } = setup();
    edit();
    const first = controller.savePendingChanges();
    const second = controller.savePendingChanges();
    (await request(0)).reject(new Error("save failed"));
    await Promise.all([expect(first).rejects.toThrow("save failed"), expect(second).rejects.toThrow("save failed")]);
    expect(save).toHaveBeenCalledTimes(1);
    expect(controller.canAutosave()).toBe(false);
    expect(controller.getSnapshot().saveFailed).toBe(true);
    expect(controller.getSnapshot().editorState.saveStatus).toBe("DIRTY");
    const retry = controller.savePendingChanges();
    (await request(1)).resolve(initialState);
    await retry;
    expect(controller.getSnapshot().saveFailed).toBe(false);
  });

  it("can save a newer draft after an older request fails", async () => {
    const { controller, edit, request } = setup();
    edit();
    const saving = controller.savePendingChanges();
    const firstRequest = await request(0);
    edit();
    firstRequest.reject(new Error("old save failed"));
    (await request(1)).resolve(initialState);
    await saving;
    expect(controller.getSnapshot().editorState.saveStatus).toBe("SAVED");
  });

  it("does not save focus-only changes or save responses again", async () => {
    const { controller, save } = setup();
    controller.update((state) => ({ ...state, focus: { blockIndex: 1, contentIndex: 0 } }));
    await controller.savePendingChanges();
    expect(save).not.toHaveBeenCalled();
    expect(controller.getSnapshot().revision).toBe(0);
  });

  it("waits for a running save before resetting and discards pending edits only after reset succeeds", async () => {
    const { controller, edit, request, save } = setup();
    edit();
    const saving = controller.savePendingChanges();
    const joined = controller.savePendingChanges();
    const finished = vi.fn();
    void saving.then(finished);
    void joined.then(finished);
    const firstRequest = await request(0);
    edit();
    const operation = deferred<LetterEditorState>();
    const resetOperation = vi.fn(() => operation.promise);
    const reset = controller.reset(resetOperation);
    const savedDuringReset = controller.savePendingChanges();
    void savedDuringReset.then(finished);
    expect(resetOperation).not.toHaveBeenCalled();
    firstRequest.resolve(initialState);
    await vi.waitFor(() => expect(resetOperation).toHaveBeenCalledTimes(1));
    expect(finished).not.toHaveBeenCalled();
    expect(save).toHaveBeenCalledTimes(1);
    operation.resolve(initialState);
    await reset;
    await savedDuringReset;
    await saving;
    await joined;
    expect(finished).toHaveBeenCalledTimes(3);
    expect(controller.getSnapshot().editorState.saveStatus).toBe("SAVED");
    expect(controller.getSnapshot().resetting).toBe(false);
  });

  it("propagates reset failure to callers that started, joined, or saved during reset", async () => {
    const { controller, edit, request, save } = setup();
    edit();
    const saving = controller.savePendingChanges();
    const joined = controller.savePendingChanges();
    const firstRequest = await request(0);
    edit();
    const draft = controller.getSnapshot().editorState.redigertBrev;
    const operation = deferred<LetterEditorState>();
    const resetOperation = vi.fn(() => operation.promise);
    const reset = controller.reset(resetOperation);
    const savedDuringReset = controller.savePendingChanges();
    const failures = Promise.all(
      [saving, joined, reset, savedDuringReset].map((result) => expect(result).rejects.toThrow("reset failed")),
    );

    firstRequest.resolve(initialState);
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
    const { controller, edit, request } = setup();
    edit();
    const saving = controller.savePendingChanges();
    const firstRequest = await request(0);
    edit();
    firstRequest.reject(new Error("old save failed"));
    (await request(1)).reject(new Error("latest save failed"));
    await expect(saving).rejects.toThrow("latest save failed");
    expect(controller.canAutosave()).toBe(false);
    expect(controller.getSnapshot().editorState.saveStatus).toBe("DIRTY");
  });

  it("recovers when save throws synchronously", async () => {
    const save = vi.fn((): Promise<LetterEditorState> => {
      throw new Error("sync failure");
    });
    const controller = createEditorAutosave({ initialState, save, applyResponse: (state) => state, onSaved: vi.fn() });
    controller.update((state) => ({ ...state, redigertBrev: { ...state.redigertBrev }, saveStatus: "DIRTY" }));
    await expect(controller.savePendingChanges()).rejects.toThrow("sync failure");
    await expect(controller.savePendingChanges()).rejects.toThrow("sync failure");
    expect(save).toHaveBeenCalledTimes(2);
  });

  it("lets a subscriber that saves during save startup wait for the real save", async () => {
    const { controller, edit, request } = setup();
    edit();
    const nestedFinished = vi.fn();
    const unsubscribe = controller.subscribe(() => {
      unsubscribe();
      void controller.savePendingChanges().then(nestedFinished);
    });
    const saving = controller.savePendingChanges();
    const firstRequest = await request(0);
    await new Promise((resolve) => setTimeout(resolve));
    expect(nestedFinished).not.toHaveBeenCalled();
    firstRequest.resolve(initialState);
    await saving;
    await vi.waitFor(() => expect(nestedFinished).toHaveBeenCalledTimes(1));
  });

  it("does not send a save when a subscriber resets during save startup", async () => {
    const { controller, edit, save } = setup();
    edit();
    let nestedReset: Promise<void> | undefined;
    const unsubscribe = controller.subscribe(() => {
      unsubscribe();
      nestedReset = controller.reset(async () => initialState);
    });
    await controller.savePendingChanges();
    await nestedReset;
    expect(save).not.toHaveBeenCalled();
  });

  it("returns the running reset to a subscriber that resets during reset startup", () => {
    const { controller } = setup();
    let nestedReset: Promise<void> | undefined;
    const unsubscribe = controller.subscribe(() => {
      unsubscribe();
      nestedReset = controller.reset(async () => initialState);
    });
    const reset = controller.reset(() => deferred<LetterEditorState>().promise);
    expect(nestedReset).toBe(reset);
  });

  it("keeps draining actual edits without an arbitrary attempt limit", async () => {
    const { controller, edit, request, save } = setup();
    edit();
    const saving = controller.savePendingChanges();
    for (let requestIndex = 0; requestIndex < 7; requestIndex++) {
      const pending = await request(requestIndex);
      edit();
      pending.resolve(initialState);
      await request(requestIndex + 1);
      expect(controller.getSnapshot().editorState.saveStatus).not.toBe("SAVED");
    }
    (await request(7)).resolve(initialState);
    await saving;
    expect(save).toHaveBeenCalledTimes(8);
    expect(controller.getSnapshot().editorState.saveStatus).toBe("SAVED");
  });
});
