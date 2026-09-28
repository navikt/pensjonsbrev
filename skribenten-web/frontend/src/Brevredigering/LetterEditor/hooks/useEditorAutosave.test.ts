import { act, renderHook } from "@testing-library/react";
import { StrictMode } from "react";
import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { AUTOSAVE_TIMER } from "~/components/ManagedLetterEditor/autosave_timer";
import { brevResponse } from "~test/support/brevFixtures";

import { useEditorAutosave } from "./useEditorAutosave";

const initialState = Actions.create(brevResponse());
const edit = (state: LetterEditorState): LetterEditorState => ({
  ...state,
  redigertBrev: { ...state.redigertBrev },
  saveStatus: "DIRTY",
});

const setup = (save = vi.fn(async (state: LetterEditorState) => state)) => {
  const hook = renderHook(() =>
    useEditorAutosave({
      initialState,
      save,
      applyResponse: (_state, response) => response,
      onSaved: vi.fn(),
    }),
  );
  return { ...hook, save };
};

describe("useEditorAutosave", () => {
  beforeEach(() => {
    vi.useFakeTimers();
  });
  afterEach(() => {
    vi.useRealTimers();
  });

  it("debounces edits and saves the latest draft once", async () => {
    const { result, save } = setup();
    act(() => result.current.setEditorState(edit));
    await act(() => vi.advanceTimersByTimeAsync(AUTOSAVE_TIMER - 1));
    act(() => result.current.setEditorState(edit));
    const latestDraft = result.current.editorState.redigertBrev;
    await act(() => vi.advanceTimersByTimeAsync(AUTOSAVE_TIMER - 1));
    expect(save).not.toHaveBeenCalled();
    await act(() => vi.advanceTimersByTimeAsync(1));
    expect(save).toHaveBeenCalledTimes(1);
    expect(save.mock.calls[0][0].redigertBrev).toBe(latestDraft);
    expect(result.current.editorState.saveStatus).toBe("SAVED");
    await act(() => vi.advanceTimersByTimeAsync(AUTOSAVE_TIMER * 2));
    expect(save).toHaveBeenCalledTimes(1);
  });

  it("saves pending changes immediately without waiting for the debounce", async () => {
    const { result, save } = setup();
    act(() => result.current.setEditorState(edit));
    await act(() => result.current.savePendingChanges());
    expect(save).toHaveBeenCalledTimes(1);
    expect(result.current.editorState.saveStatus).toBe("SAVED");
  });

  it("does not retry a failed revision automatically but saves a new edit", async () => {
    const save = vi.fn(async (state: LetterEditorState) => state).mockRejectedValueOnce(new Error("save failed"));
    const { result } = setup(save);
    act(() => result.current.setEditorState(edit));
    await act(() => vi.advanceTimersByTimeAsync(AUTOSAVE_TIMER));
    expect(result.current.saveFailed).toBe(true);
    await act(() => vi.advanceTimersByTimeAsync(AUTOSAVE_TIMER * 3));
    expect(save).toHaveBeenCalledTimes(1);
    act(() => result.current.setEditorState(edit));
    await act(() => vi.advanceTimersByTimeAsync(AUTOSAVE_TIMER));
    expect(save).toHaveBeenCalledTimes(2);
    expect(result.current.saveFailed).toBe(false);
    expect(result.current.editorState.saveStatus).toBe("SAVED");
  });

  it("preserves the best-effort save on unmount", async () => {
    const { result, save, unmount } = setup();
    act(() => result.current.setEditorState(edit));
    unmount();
    await act(() => Promise.resolve());
    expect(save).toHaveBeenCalledTimes(1);
  });

  it("restarts the debounce on content edits but not on cursor-only changes", async () => {
    const { result, save } = setup();
    act(() => result.current.setEditorState(edit));
    await act(() => vi.advanceTimersByTimeAsync(AUTOSAVE_TIMER - 1));
    act(() => result.current.setEditorState((state) => ({ ...state, focus: { blockIndex: 1, contentIndex: 0 } })));
    await act(() => vi.advanceTimersByTimeAsync(1));
    expect(save).toHaveBeenCalledTimes(1);
  });

  it("gives edits made during an active save their own debounce", async () => {
    const pending = Promise.withResolvers<LetterEditorState>();
    const save = vi.fn((state: LetterEditorState) =>
      save.mock.calls.length === 1 ? pending.promise : Promise.resolve(state),
    );
    const { result } = setup(save);
    act(() => result.current.setEditorState(edit));
    await act(() => vi.advanceTimersByTimeAsync(AUTOSAVE_TIMER));
    expect(save).toHaveBeenCalledTimes(1);

    act(() => result.current.setEditorState(edit));
    await act(() => vi.advanceTimersByTimeAsync(AUTOSAVE_TIMER));
    expect(save).toHaveBeenCalledTimes(1);

    await act(async () => pending.resolve(initialState));
    expect(result.current.editorState.saveStatus).toBe("DIRTY");
    await act(() => vi.advanceTimersByTimeAsync(AUTOSAVE_TIMER - 1));
    expect(save).toHaveBeenCalledTimes(1);
    await act(() => vi.advanceTimersByTimeAsync(1));
    expect(save).toHaveBeenCalledTimes(2);
    expect(result.current.editorState.saveStatus).toBe("SAVED");
  });

  it("keeps the controller and draft across re-renders while using the latest callbacks", async () => {
    const firstSave = vi.fn(async (state: LetterEditorState) => state);
    const latestSave = vi.fn(async (state: LetterEditorState) => state);
    const otherInitialState = Actions.create(brevResponse());
    const { result, rerender } = renderHook(
      (props: { save: typeof firstSave; initialState: LetterEditorState }) =>
        useEditorAutosave({ ...props, applyResponse: (state) => state, onSaved: vi.fn() }),
      { initialProps: { save: firstSave, initialState } },
    );
    act(() => result.current.setEditorState(edit));
    const { setEditorState, savePendingChanges } = result.current;
    const draft = result.current.editorState.redigertBrev;

    rerender({ save: latestSave, initialState: otherInitialState });
    expect(result.current.editorState.redigertBrev).toBe(draft);
    expect(result.current.setEditorState).toBe(setEditorState);
    expect(result.current.savePendingChanges).toBe(savePendingChanges);

    await act(() => vi.advanceTimersByTimeAsync(AUTOSAVE_TIMER));
    expect(firstSave).not.toHaveBeenCalled();
    expect(latestSave).toHaveBeenCalledTimes(1);
  });

  it("clears the debounce on unmount and sends only the best-effort save", async () => {
    const { result, save, unmount } = setup();
    act(() => result.current.setEditorState(edit));
    unmount();
    await act(() => vi.advanceTimersByTimeAsync(AUTOSAVE_TIMER * 2));
    expect(save).toHaveBeenCalledTimes(1);
  });

  it("does not save on unmount when nothing is pending or the revision already failed", async () => {
    const clean = setup();
    clean.unmount();
    await act(() => vi.advanceTimersByTimeAsync(AUTOSAVE_TIMER));
    expect(clean.save).not.toHaveBeenCalled();

    const failed = setup(vi.fn(async () => Promise.reject<LetterEditorState>(new Error("save failed"))));
    act(() => failed.result.current.setEditorState(edit));
    await act(() => vi.advanceTimersByTimeAsync(AUTOSAVE_TIMER));
    expect(failed.result.current.saveFailed).toBe(true);
    failed.unmount();
    await act(() => vi.advanceTimersByTimeAsync(AUTOSAVE_TIMER));
    expect(failed.save).toHaveBeenCalledTimes(1);
  });

  it("sends one request per draft and keeps updating under Strict Mode", async () => {
    const save = vi.fn(async (state: LetterEditorState) => state);
    const { result } = renderHook(
      () => useEditorAutosave({ initialState, save, applyResponse: (_state, response) => response, onSaved: vi.fn() }),
      { wrapper: StrictMode },
    );
    act(() => result.current.setEditorState(edit));
    expect(result.current.editorState.saveStatus).toBe("DIRTY");
    await act(() => vi.advanceTimersByTimeAsync(AUTOSAVE_TIMER * 3));
    expect(save).toHaveBeenCalledTimes(1);
    expect(result.current.editorState.saveStatus).toBe("SAVED");
  });

  it("resumes autosave after a failed reset and does not save after a successful one", async () => {
    const { result, save } = setup();
    act(() => result.current.setEditorState(edit));
    await act(() =>
      expect(result.current.reset(async () => Promise.reject(new Error("reset failed")))).rejects.toThrow(),
    );
    expect(result.current.resetting).toBe(false);
    await act(() => vi.advanceTimersByTimeAsync(AUTOSAVE_TIMER));
    expect(save).toHaveBeenCalledTimes(1);

    act(() => result.current.setEditorState(edit));
    await act(() => result.current.reset(async () => initialState));
    await act(() => vi.advanceTimersByTimeAsync(AUTOSAVE_TIMER * 2));
    expect(save).toHaveBeenCalledTimes(1);
    expect(result.current.editorState.saveStatus).toBe("SAVED");
  });
});
