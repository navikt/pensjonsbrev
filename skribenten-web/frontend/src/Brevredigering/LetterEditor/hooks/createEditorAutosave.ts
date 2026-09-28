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
 * External Store that owns the editor state and keeps it in sync with the backend.
 * Every content edit bumps `latestRevision`; a save acknowledges exactly the revision it sent,
 * so edits made while a request is in flight are never marked as saved by mistake.
 *
 * Errors: a rejected `save` is a failed server save (`saveFailed`, not retried automatically).
 * If `onSaved` or `applyResponse` throws, the server already has the revision, so it stays saved,
 * the local draft is kept as-is, `saveFailed` is not set, and the error rejects `savePendingChanges`
 * (`autosave` has no caller to report to and ignores it).
 */
export function createEditorAutosave<Response>(options: EditorAutosaveOptions<Response>) {
  /* The version number of the latest edit made to the local editor state. */
  let latestRevision = 0;

  /* Tracks the version number of the last successfully saved revision(saved to the backend). */
  /* -1 makes an initially dirty letter count as unsaved without inventing an edit. */
  let lastSavedRevision = options.initialState.saveStatus === "DIRTY" ? -1 : 0;

  /* Tracks the revision number of the last failed save attempt. */
  /* So we won't try the same failed revision forever. */
  let failedRevision: number | undefined;

  let isSaving = false;
  let isResetting = false;

  /* Set by an explicit save: keep saving newer revisions until the latest one is stored. */
  /* Without it the loop saves one revision and leaves later edits to the next debounced autosave. */
  let keepSavingUntilLatest = false;

  /* The most recent save/reset operation; only awaited while the matching flag is set. */
  let currentSave: Promise<void> = Promise.resolve();
  let currentReset: Promise<void> = Promise.resolve();

  let snapshot = {
    editorState: options.initialState,
    revision: latestRevision,
    saveFailed: false,
    resetting: false,
  };

  /* Callback functions to notify subscribers of state changes. */
  const listeners = new Set<() => void>();

  const hasUnsavedChanges = () => latestRevision !== lastSavedRevision;

  /* Autosave skips a revision that already failed, so it does not retry the same draft in a loop. */
  const canAutosave = () => hasUnsavedChanges() && failedRevision !== latestRevision && !isResetting;

  const currentSaveStatus = (): SaveStatus => {
    if (!hasUnsavedChanges()) return "SAVED";
    return isSaving ? "SAVE_PENDING" : "DIRTY";
  };

  /* Contract: an edit sets saveStatus "DIRTY" */
  const isContentEdit = (previous: LetterEditorState, next: LetterEditorState) =>
    next.saveStatus === "DIRTY" &&
    (next.redigertBrev !== previous.redigertBrev || next.saksbehandlerValg !== previous.saksbehandlerValg);

  /* Creates a new immutable snapshot (required by useSyncExternalStore) and notifies subscribers. */
  const publish = (editorState = snapshot.editorState) => {
    snapshot = {
      editorState: { ...editorState, saveStatus: currentSaveStatus() },
      revision: latestRevision,
      saveFailed: failedRevision !== undefined,
      resetting: isResetting,
    };
    for (const listener of listeners) listener();
  };

  /* Applies a local change. Only content edits create a new revision; focus/cursor changes do not. */
  /* Ignored during a reset, since the reset result replaces the draft anyway (the editor is frozen meanwhile). */
  const update = (stateOrUpdater: EditorStateUpdate) => {
    if (isResetting) return;
    const previous = snapshot.editorState;
    const next = typeof stateOrUpdater === "function" ? stateOrUpdater(previous) : stateOrUpdater;
    if (next === previous) return;
    if (isContentEdit(previous, next)) latestRevision++;
    publish(next);
  };

  /* Saves the current revision; with `keepSavingUntilLatest`, keeps going until the latest edit is stored or a reset takes over. */
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
          // A newer draft (revision) makes this failure irrelevant, so try again with that draft.
          const hasNewerEdits = latestRevision !== revisionBeingSaved;
          if (keepSavingUntilLatest && hasNewerEdits && !isResetting) continue;
          throw error;
        }

        lastSavedRevision = revisionBeingSaved;
        options.onSaved(response);
        // Only apply the server response when it does not overwrite edits made during the request.
        const hasNewerEdits = latestRevision !== revisionBeingSaved;
        publish(hasNewerEdits ? snapshot.editorState : options.applyResponse(snapshot.editorState, response));
        if (!keepSavingUntilLatest) break;
      }
    } finally {
      isSaving = false;
      keepSavingUntilLatest = false;
      publish();
    }
  };

  const startSaveLoop = () => {
    isSaving = true;
    // Deferred so subscribers and `save` never see the flag without the matching promise.
    currentSave = Promise.resolve().then(runSaveLoop);
    publish();
  };

  /* Debounced save of the current revision. Does nothing while a save runs; later edits get their own debounce. */
  const autosave = () => {
    if (isSaving || !canAutosave()) return;
    startSaveLoop();
    currentSave.catch(() => undefined);
  };

  /* Saves pending edits right away, including edits made while it runs, and resolves once the latest revision is stored. */
  /* Concurrent callers share the same save, and also wait for a reset started meanwhile. */
  const savePendingChanges = async (): Promise<void> => {
    if (isResetting) {
      await currentReset;
      return savePendingChanges();
    }
    if (!isSaving) {
      if (!hasUnsavedChanges()) return;
      startSaveLoop();
    }
    keepSavingUntilLatest = true;
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

  /* Replaces the state with the result of `operation` once any running save has settled (even if it failed). */
  /* Pending edits are discarded only if the operation succeeds. A reset requested while one runs joins it; its operation is ignored. */
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
    autosave,
    savePendingChanges,
    reset,
    canAutosave,
  };
}
