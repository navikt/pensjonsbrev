import { useEffect, useLayoutEffect, useRef, useState, useSyncExternalStore } from "react";

import { AUTOSAVE_TIMER } from "~/components/ManagedLetterEditor/autosave_timer";

import { type AutosaveState, createEditorAutosave, type EditorAutosaveOptions } from "./createEditorAutosave";

export function useEditorAutosave<S extends AutosaveState, Response>(options: EditorAutosaveOptions<S, Response>) {
  const callbacks = useRef(options);
  useLayoutEffect(() => {
    callbacks.current = options;
  });

  const [controller] = useState(() =>
    createEditorAutosave({
      initialState: options.initialState,
      save: (state) => callbacks.current.save(state),
      applyLatestResponse: (state: S, response: Response) => callbacks.current.applyLatestResponse(state, response),
      onSaved: (response) => callbacks.current.onSaved(response),
    }),
  );
  const snapshot = useSyncExternalStore(controller.subscribe, controller.getSnapshot);

  useEffect(() => {
    const timeout = setTimeout(controller.autosave, AUTOSAVE_TIMER);
    return () => clearTimeout(timeout);
  }, [controller, snapshot.revision, snapshot.resetting, snapshot.saveFailed, snapshot.editorState.saveStatus]);

  useEffect(
    () => () => {
      if (controller.canAutosave()) void controller.savePendingChanges().catch(() => undefined);
    },
    [controller],
  );

  return {
    editorState: snapshot.editorState,
    setEditorState: controller.setEditorState,
    saveFailed: snapshot.saveFailed,
    resetting: snapshot.resetting,
    savePendingChanges: controller.savePendingChanges,
    saveWith: controller.saveWith,
    reset: controller.reset,
  };
}
