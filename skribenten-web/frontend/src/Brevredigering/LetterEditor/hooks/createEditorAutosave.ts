import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";

export type EditorAutosaveOptions<Response> = {
  initialState: LetterEditorState;
  save: (state: LetterEditorState) => Promise<Response>;
  applyResponse: (state: LetterEditorState, response: Response) => LetterEditorState;
  onSaved: (response: Response) => void;
};

type SaveStatus = LetterEditorState["saveStatus"];
type EditorStateUpdate = LetterEditorState | ((state: LetterEditorState) => LetterEditorState);

/**
 * Framework-agnostic store that owns the editor state and keeps it in sync with the backend.
 * Every content edit bumps `latestRevision`; a save acknowledges exactly the revision it sent,
 * so edits made while a request is in flight are never marked as saved by mistake.
 */
export function createEditorAutosave<Response>(options: EditorAutosaveOptions<Response>) {
  let latestRevision = 0;
  // -1 makes an initially dirty letter count as unsaved without inventing an edit.
  let lastSavedRevision = options.initialState.saveStatus === "DIRTY" ? -1 : 0;
  let failedRevision: number | undefined;
  let isSaving = false;
  let isResetting = false;
  // The most recent save/reset; only awaited while the matching flag is set.
  let currentSave: Promise<void> = Promise.resolve();
  let currentReset: Promise<void> = Promise.resolve();
  let snapshot = {
    editorState: options.initialState,
    revision: latestRevision,
    saveFailed: false,
    resetting: false,
  };
  const listeners = new Set<() => void>();

  const hasUnsavedChanges = () => latestRevision !== lastSavedRevision;

  const currentSaveStatus = (): SaveStatus => {
    if (!hasUnsavedChanges()) return "SAVED";
    return isSaving ? "SAVE_PENDING" : "DIRTY";
  };

  const isContentEdit = (previous: LetterEditorState, next: LetterEditorState) =>
    next.saveStatus === "DIRTY" &&
    (next.redigertBrev !== previous.redigertBrev || next.saksbehandlerValg !== previous.saksbehandlerValg);

  // Creates a new immutable snapshot (required by useSyncExternalStore) and notifies subscribers.
  const publish = (editorState = snapshot.editorState) => {
    snapshot = {
      editorState: { ...editorState, saveStatus: currentSaveStatus() },
      revision: latestRevision,
      saveFailed: failedRevision !== undefined,
      resetting: isResetting,
    };
    for (const listener of listeners) listener();
  };

  // Applies a local change. Only content edits create a new revision; focus/cursor changes do not.
  const update = (stateOrUpdater: EditorStateUpdate) => {
    if (isResetting) return;
    const previous = snapshot.editorState;
    const next = typeof stateOrUpdater === "function" ? stateOrUpdater(previous) : stateOrUpdater;
    if (next === previous) return;
    if (isContentEdit(previous, next)) latestRevision++;
    publish(next);
  };

  // Saves one revision after another until the latest edit is stored or a reset takes over.
  const runSaveLoop = async () => {
    try {
      while (hasUnsavedChanges() && !isResetting) {
        const revisionBeingSaved = latestRevision;
        const stateBeingSaved = snapshot.editorState;
        failedRevision = undefined;
        publish();

        let response: Response;
        try {
          response = await options.save(stateBeingSaved);
        } catch (error) {
          failedRevision = revisionBeingSaved;
          // A newer draft makes this failure irrelevant, so try again with that draft.
          const hasNewerEdits = latestRevision !== revisionBeingSaved;
          if (hasNewerEdits && !isResetting) continue;
          throw error;
        }

        lastSavedRevision = revisionBeingSaved;
        options.onSaved(response);
        // Only apply the server response when it does not overwrite edits made during the request.
        const hasNewerEdits = latestRevision !== revisionBeingSaved;
        publish(hasNewerEdits ? snapshot.editorState : options.applyResponse(snapshot.editorState, response));
      }
    } finally {
      isSaving = false;
      publish();
    }
  };

  // Saves pending edits right away. Concurrent callers share the same save, and also wait for a reset started meanwhile.
  const savePendingChanges = async (): Promise<void> => {
    if (isResetting) {
      await currentReset;
      return savePendingChanges();
    }
    if (!isSaving) {
      if (!hasUnsavedChanges()) return;
      isSaving = true;
      // Deferred so subscribers and `save` never see the flag without the matching promise.
      currentSave = Promise.resolve().then(runSaveLoop);
      publish();
    }
    await currentSave;
    if (isResetting) await currentReset;
  };

  const runReset = async (operation: () => Promise<LetterEditorState>) => {
    try {
      await currentSave.catch(() => undefined);
      const state = await operation();
      latestRevision++;
      lastSavedRevision = latestRevision;
      failedRevision = undefined;
      publish(state);
    } finally {
      isResetting = false;
      publish();
    }
  };

  // Replaces the state with the result of `operation` (e.g. reloading the letter) once any running save has settled.
  // Pending edits are discarded only if the operation succeeds.
  const reset = (operation: () => Promise<LetterEditorState>): Promise<void> => {
    if (!isResetting) {
      isResetting = true;
      currentReset = Promise.resolve().then(() => runReset(operation));
      publish();
    }
    return currentReset;
  };

  return {
    getSnapshot: () => snapshot,
    subscribe: (listener: () => void) => {
      listeners.add(listener);
      return () => {
        listeners.delete(listener);
      };
    },
    update,
    savePendingChanges,
    reset,
    // Autosave skips a revision that already failed, so it does not retry the same draft in a loop.
    canAutosave: () => hasUnsavedChanges() && failedRevision !== latestRevision && !isResetting,
  };
}
