export type AutosaveState = {
  saveStatus: "DIRTY" | "SAVE_PENDING" | "SAVED";
  redigertBrev: unknown;
  saksbehandlerValg: unknown;
};

export type EditorAutosaveOptions<S extends AutosaveState, Response> = {
  initialState: S;
  save: (state: S) => Promise<Response>;
  applyLatestResponse: (state: S, response: Response) => S;
  onSaved: (response: Response) => void;
};

type SaveStatus = AutosaveState["saveStatus"];

type SaveHandlers<S extends AutosaveState, Response> = Pick<
  EditorAutosaveOptions<S, Response>,
  "save" | "applyLatestResponse"
>;

export function createEditorAutosave<S extends AutosaveState, Response>({
  initialState,
  ...defaultSave
}: EditorAutosaveOptions<S, Response>) {
  type EditorStateUpdate = S | ((state: S) => S);

  let latestRevision = 0;

  let lastSavedRevision = initialState.saveStatus === "DIRTY" ? -1 : 0;

  let lastFailedRevision: number | undefined;

  let isSaving = false;
  let isResetting = false;

  let keepSavingUntilLatest = false;

  let activeSave: Promise<Response | undefined> = Promise.resolve(undefined);
  let activeReset: Promise<void> = Promise.resolve();

  let snapshot = {
    editorState: initialState,
    revision: latestRevision,
    saveFailed: false,
    resetting: false,
  };

  const listeners = new Set<() => void>();

  const hasUnsavedChanges = () => latestRevision !== lastSavedRevision;

  const canAutosave = () => hasUnsavedChanges() && lastFailedRevision !== latestRevision && !isResetting;

  const currentSaveStatus = (): SaveStatus => {
    if (isSaving) return "SAVE_PENDING";
    return hasUnsavedChanges() ? "DIRTY" : "SAVED";
  };

  const isContentEdit = (previous: S, next: S) =>
    next.saveStatus === "DIRTY" &&
    (next.redigertBrev !== previous.redigertBrev || next.saksbehandlerValg !== previous.saksbehandlerValg);

  const publish = (editorState = snapshot.editorState) => {
    snapshot = {
      editorState: { ...editorState, saveStatus: currentSaveStatus() },
      revision: latestRevision,
      saveFailed: lastFailedRevision !== undefined,
      resetting: isResetting,
    };
    for (const listener of listeners) listener();
  };

  const setEditorState = (stateOrUpdater: EditorStateUpdate) => {
    if (isResetting) return;
    const previous = snapshot.editorState;
    const next = typeof stateOrUpdater === "function" ? stateOrUpdater(previous) : stateOrUpdater;
    if (next === previous) return;
    if (isContentEdit(previous, next)) latestRevision++;
    publish(next);
  };

  const runSaveLoop = async (customSave?: SaveHandlers<S, Response>) => {
    const handlers = customSave ?? defaultSave;
    let response: Response | undefined;
    try {
      while ((customSave || hasUnsavedChanges()) && !isResetting) {
        const revisionBeingSaved = latestRevision;
        const stateBeingSaved = snapshot.editorState;
        const hasNewerEdits = () => latestRevision !== revisionBeingSaved;
        lastFailedRevision = undefined;
        publish();

        try {
          response = await handlers.save(stateBeingSaved);
        } catch (error) {
          if (!customSave) lastFailedRevision = revisionBeingSaved;
          if (!customSave && keepSavingUntilLatest && hasNewerEdits() && !isResetting) continue;
          throw error;
        }

        lastSavedRevision = revisionBeingSaved;
        defaultSave.onSaved(response);
        publish(hasNewerEdits() ? snapshot.editorState : handlers.applyLatestResponse(snapshot.editorState, response));
        if (customSave || !keepSavingUntilLatest) break;
      }
      return response;
    } finally {
      isSaving = false;
      keepSavingUntilLatest = false;
      publish();
    }
  };

  const startSaveLoop = (customSave?: SaveHandlers<S, Response>) => {
    isSaving = true;
    activeSave = Promise.resolve().then(() => runSaveLoop(customSave));
    publish();
  };

  const autosave = () => {
    if (isSaving || !canAutosave()) return;
    startSaveLoop();
    activeSave.catch(() => undefined);
  };

  const savePendingChanges = async (): Promise<void> => {
    if (isResetting) {
      await activeReset;
      return savePendingChanges();
    }
    if (!isSaving) {
      if (!hasUnsavedChanges()) return;
      startSaveLoop();
    }
    keepSavingUntilLatest = true;
    await activeSave;
    if (isResetting) await activeReset;
    if (hasUnsavedChanges()) return savePendingChanges();
  };

  const saveWith = async (customSave: SaveHandlers<S, Response>): Promise<Response> => {
    if (isResetting) {
      await activeReset;
      return saveWith(customSave);
    }
    if (isSaving) {
      await activeSave.catch(() => undefined);
      return saveWith(customSave);
    }
    startSaveLoop(customSave);
    const response = await activeSave;
    if (isResetting) await activeReset;
    if (response === undefined) throw new Error("Save interrupted by reset");
    return response;
  };

  const runReset = async (loadState: () => Promise<S>) => {
    try {
      await activeSave.catch(() => undefined);
      const state = await loadState();
      latestRevision++;
      lastSavedRevision = latestRevision;
      lastFailedRevision = undefined;
      publish(state);
    } finally {
      isResetting = false;
      publish();
    }
  };

  const reset = (loadState: () => Promise<S>): Promise<void> => {
    if (!isResetting) {
      isResetting = true;
      activeReset = Promise.resolve().then(() => runReset(loadState));
      publish();
    }
    return activeReset;
  };

  return {
    getSnapshot: () => snapshot,
    subscribe: (listener: () => void) => {
      listeners.add(listener);
      return () => {
        listeners.delete(listener);
      };
    },
    setEditorState,
    autosave,
    savePendingChanges,
    saveWith,
    reset,
    canAutosave,
  };
}
