import { useCallback } from "react";

import { caretAtDomSelection } from "../caret/domCaret";
import { moveFocus, refocus } from "../commands/updateText";
import { type Command } from "../state/editorState";
import { useEditorV2 } from "./EditorContextV2";

export function useMenuDispatch() {
  const { dispatch } = useEditorV2();
  return useCallback(
    <Args extends unknown[]>(command: Command<Args>, ...args: Args) => {
      const caret = caretAtDomSelection();
      if (caret) dispatch(moveFocus, caret);
      dispatch(command, ...args);
      dispatch(refocus);
    },
    [dispatch],
  );
}
