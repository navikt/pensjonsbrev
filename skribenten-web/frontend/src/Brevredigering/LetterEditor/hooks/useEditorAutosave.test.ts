import { act, renderHook } from "@testing-library/react";
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

  it("flushes immediately without waiting for the debounce", async () => {
    const { result, save } = setup();
    act(() => result.current.setEditorState(edit));
    await act(() => result.current.flush());
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
});
