import { useEffect, useLayoutEffect, useRef, useState, useSyncExternalStore } from "react";

import { AUTOSAVE_TIMER } from "~/components/ManagedLetterEditor/autosave_timer";

import { createEditorAutosave, type EditorAutosaveOptions } from "./createEditorAutosave";

export function useEditorAutosave<Response>(options: EditorAutosaveOptions<Response>) {
  const callbacks = useRef(options);
  useLayoutEffect(() => {
    callbacks.current = options;
  });

  // Creates and retains one controller instance for that mounted hook, returning the same instance on every subsequent render.
  const [controller] = useState(() =>
    createEditorAutosave({
      initialState: options.initialState,
      save: (state) => callbacks.current.save(state),
      applyResponse: (state, response: Response) => callbacks.current.applyResponse(state, response),
      onSaved: (response) => callbacks.current.onSaved(response),
    }),
  );
  const snapshot = useSyncExternalStore(controller.subscribe, controller.getSnapshot);

  useEffect(() => {
    const timeout = setTimeout(controller.autosave, AUTOSAVE_TIMER);
    return () => clearTimeout(timeout);
  }, [controller, snapshot.revision, snapshot.resetting, snapshot.saveFailed, snapshot.editorState.saveStatus]);

  // Best effort only: browser Back navigates within the app and unmounts the editor, this cleanup attempts to save pending edits. 
  useEffect(
    () => () => {
      if (controller.canAutosave()) void controller.savePendingChanges().catch(() => undefined);
    },
    [controller],
  );

  return {
    editorState: snapshot.editorState,
    setEditorState: controller.update,
    saveFailed: snapshot.saveFailed,
    resetting: snapshot.resetting,
    savePendingChanges: controller.savePendingChanges,
    saveWith: controller.saveWith,
    reset: controller.reset,
  };
}
