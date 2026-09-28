import { useEffect, useLayoutEffect, useRef, useState, useSyncExternalStore } from "react";

import { AUTOSAVE_TIMER } from "~/components/ManagedLetterEditor/autosave_timer";

import { createEditorAutosave, type EditorAutosaveOptions } from "./createEditorAutosave";

export function useEditorAutosave<Response>(options: EditorAutosaveOptions<Response>) {
  const callbacks = useRef(options);
  useLayoutEffect(() => {
    callbacks.current = options;
  });

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

  // Best effort only: nothing waits for this request, and it cannot survive the browser closing the page.
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
