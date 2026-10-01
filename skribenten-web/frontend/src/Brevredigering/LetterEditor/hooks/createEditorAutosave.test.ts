import { describe, expect, it, vi } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { brevResponse } from "~test/support/brevFixtures";
import { letter, literal, paragraph } from "~test/support/letterEditorTestUtils";

import { type LetterEditorState } from "../model/state";
import { createEditorAutosave, type EditorAutosaveOptions } from "./createEditorAutosave";

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

const setup = (overrides: Partial<EditorAutosaveOptions<LetterEditorState>> = {}) => {
  const requests: ReturnType<typeof deferred<LetterEditorState>>[] = [];
  const save = vi.fn(() => {
    const request = deferred<LetterEditorState>();
    requests.push(request);
    return request.promise;
  });
  const applyResponse = vi.fn((_state: LetterEditorState, response: LetterEditorState) => response);
  const onSaved = vi.fn();
  const controller = createEditorAutosave({ initialState, save, applyResponse, onSaved, ...overrides });
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

const settle = () => new Promise((resolve) => setTimeout(resolve));

describe("createEditorAutosave edit detection", () => {
  it("needs no request for an initially saved state", async () => {
    const { controller, save } = setup();
    expect(controller.canAutosave()).toBe(false);
    await controller.savePendingChanges();
    expect(save).not.toHaveBeenCalled();
    expect(controller.getSnapshot().editorState.saveStatus).toBe("SAVED");
  });

  it("saves an initially dirty state", async () => {
    const { controller, request } = setup({ initialState: { ...initialState, saveStatus: "DIRTY" } });
    expect(controller.canAutosave()).toBe(true);
    const saving = controller.savePendingChanges();
    (await request(0)).resolve(initialState);
    await saving;
    expect(controller.getSnapshot().editorState.saveStatus).toBe("SAVED");
  });

  it("counts content edits but not no-op, focus-only or unmarked updates", () => {
    const { controller, edit } = setup();
    controller.update((state) => state);
    controller.update(controller.getSnapshot().editorState);
    controller.update((state) => ({ ...state, focus: { blockIndex: 1, contentIndex: 0 } }));
    controller.update((state) => ({ ...state, redigertBrev: { ...state.redigertBrev } }));
    expect(controller.getSnapshot().revision).toBe(0);
    edit();
    expect(controller.getSnapshot().revision).toBe(1);
  });

  it("detects saksbehandlerValg changes", () => {
    const { controller } = setup();
    controller.update((state) => ({
      ...state,
      saksbehandlerValg: { ...state.saksbehandlerValg },
      saveStatus: "DIRTY",
    }));
    expect(controller.getSnapshot().revision).toBe(1);
  });
});

describe("createEditorAutosave saving", () => {
  it("serializes a custom save after autosave and supplies the latest draft", async () => {
    const { controller, edit, request } = setup();
    edit();
    controller.autosave();
    const pending = await request(0);
    edit();
    const draft = controller.getSnapshot().editorState;
    const customSave = vi.fn(async (state: LetterEditorState) => state);
    const saving = controller.saveWith({ save: customSave, applyResponse: (_state, response) => response });
    expect(customSave).not.toHaveBeenCalled();
    pending.resolve(initialState);
    await saving;
    expect(customSave).toHaveBeenCalledWith(expect.objectContaining({ redigertBrev: draft.redigertBrev }));
    expect(controller.getSnapshot().editorState.saveStatus).toBe("SAVED");
  });

  for (const previousSave of ["autosave", "custom save"] as const) {
    it(`runs a queued custom save with the latest draft after a failing ${previousSave}`, async () => {
      const { controller, edit, request } = setup();
      edit();
      let previousFailure: Promise<unknown> | undefined;
      if (previousSave === "autosave") {
        controller.autosave();
      } else {
        previousFailure = expect(
          controller.saveWith({
            save: () => Promise.reject(new Error("previous save failed")),
            applyResponse: (state) => state,
          }),
        ).rejects.toThrow("previous save failed");
      }
      const pending = previousSave === "autosave" ? await request(0) : undefined;
      edit();
      const draft = controller.getSnapshot().editorState.redigertBrev;
      const customSave = vi.fn(async (state: LetterEditorState) => state);
      const saving = controller.saveWith({ save: customSave, applyResponse: (_state, response) => response });
      expect(customSave).not.toHaveBeenCalled();
      pending?.reject(new Error("previous save failed"));
      await previousFailure;
      await saving;
      expect(customSave).toHaveBeenCalledTimes(1);
      expect(customSave).toHaveBeenCalledWith(expect.objectContaining({ redigertBrev: draft }));
      expect(controller.getSnapshot().editorState.saveStatus).toBe("SAVED");
      expect(controller.getSnapshot().saveFailed).toBe(false);
    });
  }

  for (const outcome of ["succeeds", "fails"] as const) {
    it(`keeps the custom save result independent of a joined flush that ${outcome}`, async () => {
      const { controller, edit, request, onSaved } = setup();
      const customRequest = deferred<LetterEditorState>();
      const customSave = vi.fn(() => customRequest.promise);
      const customApplyResponse = vi.fn((_state: LetterEditorState, response: LetterEditorState) => response);
      const customResponse = letter(paragraph([literal("Custom response")]));
      const saving = controller.saveWith({ save: customSave, applyResponse: customApplyResponse });
      await vi.waitFor(() => expect(customSave).toHaveBeenCalledTimes(1));
      edit();
      const draft = controller.getSnapshot().editorState.redigertBrev;
      const flush = controller.savePendingChanges();
      const failure = outcome === "fails" ? expect(flush).rejects.toThrow("follow-up save failed") : undefined;
      customRequest.resolve(customResponse);
      const followUp = await request(0);
      let customResult: LetterEditorState | undefined;
      void saving.then((response) => {
        customResult = response;
      });
      await vi.waitFor(() => expect(customResult).toBe(customResponse));
      expect(customApplyResponse).not.toHaveBeenCalled();
      expect(controller.getSnapshot().editorState.redigertBrev).toBe(draft);
      expect(onSaved).toHaveBeenCalledWith(customResponse);
      if (outcome === "succeeds") {
        followUp.resolve({ ...initialState, redigertBrev: draft });
        await flush;
        expect(controller.getSnapshot().editorState.saveStatus).toBe("SAVED");
      } else {
        followUp.reject(new Error("follow-up save failed"));
        await failure;
        expect(controller.getSnapshot().editorState.saveStatus).toBe("DIRTY");
        expect(controller.getSnapshot().saveFailed).toBe(true);
      }
      await expect(saving).resolves.toBe(customResponse);
    });
  }

  it("does not invent a failed text revision when a custom save fails", async () => {
    const { controller } = setup();
    await expect(
      controller.saveWith({
        save: async () => {
          throw new Error("form save failed");
        },
        applyResponse: (state) => state,
      }),
    ).rejects.toThrow("form save failed");
    expect(controller.getSnapshot().editorState.saveStatus).toBe("SAVED");
    expect(controller.getSnapshot().saveFailed).toBe(false);
    expect(controller.canAutosave()).toBe(false);
  });

  it("autosaves only the current revision and leaves newer edits to the next autosave", async () => {
    const { controller, edit, save, request, applyResponse } = setup();
    edit();
    controller.autosave();
    const firstRequest = await request(0);
    edit();
    controller.autosave();
    firstRequest.resolve(initialState);
    await vi.waitFor(() => expect(controller.getSnapshot().editorState.saveStatus).toBe("DIRTY"));
    await settle();
    expect(save).toHaveBeenCalledTimes(1);
    expect(applyResponse).not.toHaveBeenCalled();

    controller.autosave();
    (await request(1)).resolve(initialState);
    await vi.waitFor(() => expect(controller.getSnapshot().editorState.saveStatus).toBe("SAVED"));
  });

  for (const outcome of ["succeeds", "fails"] as const) {
    it(`drains newer edits when an explicit save joins a background save that ${outcome}`, async () => {
      const { controller, edit, save, request, applyResponse } = setup();
      edit();
      controller.autosave();
      const firstRequest = await request(0);

      edit();
      const finished = vi.fn();
      const saving = controller.savePendingChanges().then(finished);
      edit();
      const latestDraft = controller.getSnapshot().editorState.redigertBrev;
      await settle();
      expect(save).toHaveBeenCalledTimes(1);
      expect(finished).not.toHaveBeenCalled();

      if (outcome === "succeeds") {
        firstRequest.resolve(initialState);
      } else {
        firstRequest.reject(new Error("background save failed"));
      }

      const latestRequest = await request(1);
      await settle();
      expect(save).toHaveBeenCalledTimes(2);
      expect(save).toHaveBeenNthCalledWith(2, expect.objectContaining({ redigertBrev: latestDraft }));
      expect(controller.getSnapshot().editorState.redigertBrev).toBe(latestDraft);
      expect(controller.getSnapshot().editorState.saveStatus).not.toBe("SAVED");
      expect(applyResponse).not.toHaveBeenCalled();
      expect(finished).not.toHaveBeenCalled();

      latestRequest.resolve({ ...initialState, redigertBrev: latestDraft });
      await saving;
      expect(finished).toHaveBeenCalledTimes(1);
      expect(save).toHaveBeenCalledTimes(2);
      expect(applyResponse).toHaveBeenCalledTimes(1);
      expect(controller.getSnapshot().editorState.saveStatus).toBe("SAVED");
      expect(controller.getSnapshot().saveFailed).toBe(false);
    });
  }

  it("runs onSaved for every successful response, even when newer edits skip applyResponse", async () => {
    const { controller, edit, request, onSaved, applyResponse } = setup();
    edit();
    const saving = controller.savePendingChanges();
    const firstRequest = await request(0);
    edit();
    firstRequest.resolve(initialState);
    (await request(1)).resolve(initialState);
    await saving;
    expect(onSaved).toHaveBeenCalledTimes(2);
    expect(applyResponse).toHaveBeenCalledTimes(1);
  });

  it("keeps document identity and history when the caller treats the response as equivalent", async () => {
    const { controller, edit, request } = setup({ applyResponse: (state) => state });
    edit();
    const { redigertBrev, history } = controller.getSnapshot().editorState;
    const saving = controller.savePendingChanges();
    (await request(0)).resolve(initialState);
    await saving;
    expect(controller.getSnapshot().editorState.redigertBrev).toBe(redigertBrev);
    expect(controller.getSnapshot().editorState.history).toBe(history);
  });

  it("publishes the caller's replacement when the response differs", async () => {
    const serverState = letter(paragraph([literal("Fra serveren")]));
    const { controller, edit, request } = setup({
      applyResponse: (state, response) => ({ ...state, redigertBrev: response.redigertBrev }),
    });
    edit();
    const saving = controller.savePendingChanges();
    (await request(0)).resolve(serverState);
    await saving;
    expect(controller.getSnapshot().editorState.redigertBrev).toBe(serverState.redigertBrev);
    expect(controller.getSnapshot().editorState.saveStatus).toBe("SAVED");
  });

  for (const callback of ["onSaved", "applyResponse"] as const) {
    it(`treats a failing ${callback} as a local error, not a failed server save`, async () => {
      const failing = vi.fn(() => {
        throw new Error(`${callback} failed`);
      });
      const { controller, edit, request } = setup(
        callback === "onSaved" ? { onSaved: failing } : { applyResponse: failing },
      );
      edit();
      const draft = controller.getSnapshot().editorState.redigertBrev;
      const saving = controller.savePendingChanges();
      (await request(0)).resolve(initialState);
      await expect(saving).rejects.toThrow(`${callback} failed`);

      expect(controller.getSnapshot().editorState.redigertBrev).toBe(draft);
      expect(controller.getSnapshot().editorState.saveStatus).toBe("SAVED");
      expect(controller.getSnapshot().saveFailed).toBe(false);
      expect(controller.canAutosave()).toBe(false);

      edit();
      controller.autosave();
      await request(1);
    });
  }

  it("suppresses automatic retries of a failed revision but lets explicit saves and newer edits through", async () => {
    const { controller, edit, save, request } = setup();
    edit();
    controller.autosave();
    (await request(0)).reject(new Error("save failed"));
    await vi.waitFor(() => expect(controller.getSnapshot().saveFailed).toBe(true));
    controller.autosave();
    await settle();
    expect(save).toHaveBeenCalledTimes(1);

    const retry = controller.savePendingChanges();
    (await request(1)).reject(new Error("save failed again"));
    await expect(retry).rejects.toThrow("save failed again");

    edit();
    controller.autosave();
    (await request(2)).resolve(initialState);
    await vi.waitFor(() => expect(controller.getSnapshot().saveFailed).toBe(false));
  });
});

describe("createEditorAutosave reset", () => {
  it("waits for a failing save to settle before running the reset", async () => {
    const { controller, edit, request } = setup();
    edit();
    controller.autosave();
    const firstRequest = await request(0);
    const operation = vi.fn(async () => initialState);
    const reset = controller.reset(operation);
    await settle();
    expect(operation).not.toHaveBeenCalled();

    firstRequest.reject(new Error("save failed"));
    await reset;
    expect(operation).toHaveBeenCalledTimes(1);
    expect(controller.getSnapshot().saveFailed).toBe(false);
    expect(controller.getSnapshot().editorState.saveStatus).toBe("SAVED");
  });

  it("replaces the draft with the reset result and marks it saved", async () => {
    const { controller, edit, save } = setup();
    edit();
    const fresh = letter(paragraph([literal("Fra malen")]));
    await controller.reset(async () => fresh);
    expect(controller.getSnapshot().editorState.redigertBrev).toBe(fresh.redigertBrev);
    expect(controller.getSnapshot().editorState.saveStatus).toBe("SAVED");
    expect(controller.canAutosave()).toBe(false);
    expect(save).not.toHaveBeenCalled();
  });

  it("ignores updates while a reset runs", async () => {
    const { controller, edit } = setup();
    const operation = deferred<LetterEditorState>();
    const reset = controller.reset(() => operation.promise);
    edit();
    expect(controller.getSnapshot().revision).toBe(0);
    const fresh = letter(paragraph([literal("Fra malen")]));
    operation.resolve(fresh);
    await reset;
    expect(controller.getSnapshot().editorState.redigertBrev).toBe(fresh.redigertBrev);
  });

  it("joins a running reset and ignores the later operation", async () => {
    const { controller } = setup();
    const operation = deferred<LetterEditorState>();
    const laterOperation = vi.fn(async () => initialState);
    const first = controller.reset(() => operation.promise);
    expect(controller.reset(laterOperation)).toBe(first);
    operation.resolve(initialState);
    await first;
    expect(laterOperation).not.toHaveBeenCalled();
  });
});
