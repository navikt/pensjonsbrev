import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";

export type EditorAutosaveOptions<Response> = {
  initialState: LetterEditorState;
  save: (state: LetterEditorState) => Promise<Response>;
  applyResponse: (state: LetterEditorState, response: Response) => LetterEditorState;
  onSaved: (response: Response) => void;
};

export function createEditorAutosave<Response>(options: EditorAutosaveOptions<Response>) {
  let revision = 0;
  let savedRevision = options.initialState.saveStatus === "DIRTY" ? -1 : 0;
  let failedRevision: number | undefined;
  let saving: Promise<void> | undefined;
  let resetting: Promise<void> | undefined;
  let snapshot = {
    editorState: options.initialState,
    revision,
    saveFailed: false,
    resetting: false,
  };
  const listeners = new Set<() => void>();

  const publish = (editorState = snapshot.editorState) => {
    snapshot = {
      editorState: {
        ...editorState,
        saveStatus: revision === savedRevision ? "SAVED" : saving ? "SAVE_PENDING" : "DIRTY",
      },
      revision,
      saveFailed: failedRevision !== undefined,
      resetting: resetting !== undefined,
    };
    for (const listener of listeners) listener();
  };

  const update = (updateState: LetterEditorState | ((state: LetterEditorState) => LetterEditorState)) => {
    if (resetting) return;
    const previous = snapshot.editorState;
    const next = typeof updateState === "function" ? updateState(previous) : updateState;
    if (next === previous) return;
    if (
      next.saveStatus === "DIRTY" &&
      (next.redigertBrev !== previous.redigertBrev || next.saksbehandlerValg !== previous.saksbehandlerValg)
    ) {
      revision++;
    }
    publish(next);
  };

  const flush = (): Promise<void> => {
    if (resetting) return resetting.then(flush);
    if (saving) return saving.then(() => resetting);
    if (revision === savedRevision) return Promise.resolve();

    saving = Promise.resolve().then(async () => {
      try {
        while (revision !== savedRevision && !resetting) {
          const sentRevision = revision;
          const sentState = snapshot.editorState;
          failedRevision = undefined;
          publish();
          let response: Response;
          try {
            response = await options.save(sentState);
          } catch (error) {
            failedRevision = sentRevision;
            if (revision !== sentRevision && !resetting) continue;
            throw error;
          }
          savedRevision = sentRevision;
          options.onSaved(response);
          publish(
            revision === sentRevision ? options.applyResponse(snapshot.editorState, response) : snapshot.editorState,
          );
        }
      } finally {
        saving = undefined;
        publish();
      }
    });
    publish();
    return saving.then(() => resetting);
  };

  const reset = (operation: () => Promise<LetterEditorState>): Promise<void> => {
    if (resetting) return resetting;
    resetting = Promise.resolve().then(async () => {
      try {
        await saving?.catch(() => undefined);
        const state = await operation();
        revision++;
        savedRevision = revision;
        failedRevision = undefined;
        publish(state);
      } finally {
        resetting = undefined;
        publish();
      }
    });
    publish();
    return resetting;
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
    flush,
    reset,
    canAutosave: () => revision !== savedRevision && failedRevision !== revision && !resetting,
  };
}
