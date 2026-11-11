import { act, renderHook } from "@testing-library/react";
import { StrictMode } from "react";
import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";

import { AUTOSAVE_TIMER } from "~/components/ManagedLetterEditor/autosave_timer";
import { literal, paragraph, stateV2 } from "~test/support/editorV2TestUtils";

import { caret } from "../model/paths";
import { type LetterStateV2 } from "./editorState";
import { useEditorAutosave } from "./useEditorAutosave";

const initialState = stateV2(paragraph(literal("Avsnitt")));
const edit = (state: LetterStateV2): LetterStateV2 => ({
  ...state,
  redigertBrev: { ...state.redigertBrev },
  saveStatus: "DIRTY",
});

const setup = (save = vi.fn(async (state: LetterStateV2) => state)) => {
  const hook = renderHook(() =>
    useEditorAutosave({
      initialState,
      save,
      applyLatestResponse: (_state, response) => response,
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
    const save = vi.fn(async (state: LetterStateV2) => state).mockRejectedValueOnce(new Error("save failed"));
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
    act(() => result.current.setEditorState((state) => ({ ...state, focus: caret({ kind: "block", block: 1 }) })));
    await act(() => vi.advanceTimersByTimeAsync(1));
    expect(save).toHaveBeenCalledTimes(1);
  });

  it("gives edits made during an active save their own debounce", async () => {
    const pending = Promise.withResolvers<LetterStateV2>();
    const save = vi.fn((state: LetterStateV2) =>
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
    const firstSave = vi.fn(async (state: LetterStateV2) => state);
    const latestSave = vi.fn(async (state: LetterStateV2) => state);
    const otherInitialState = stateV2(paragraph(literal("Avsnitt")));
    const { result, rerender } = renderHook(
      (props: { save: typeof firstSave; initialState: LetterStateV2 }) =>
        useEditorAutosave({ ...props, applyLatestResponse: (state) => state, onSaved: vi.fn() }),
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

    const failed = setup(vi.fn(async () => Promise.reject<LetterStateV2>(new Error("save failed"))));
    act(() => failed.result.current.setEditorState(edit));
    await act(() => vi.advanceTimersByTimeAsync(AUTOSAVE_TIMER));
    expect(failed.result.current.saveFailed).toBe(true);
    failed.unmount();
    await act(() => vi.advanceTimersByTimeAsync(AUTOSAVE_TIMER));
    expect(failed.save).toHaveBeenCalledTimes(1);
  });

  it("sends one request per draft and keeps updating under Strict Mode", async () => {
    const save = vi.fn(async (state: LetterStateV2) => state);
    const { result } = renderHook(
      () =>
        useEditorAutosave({
          initialState,
          save,
          applyLatestResponse: (_state, response) => response,
          onSaved: vi.fn(),
        }),
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
