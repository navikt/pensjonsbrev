import { createContext, useContext } from "react";

import { type Command, type EditorStateV2 } from "../state/editorState";

export type Dispatch = <Args extends unknown[]>(command: Command<Args>, ...args: Args) => void;

export type EditorContextValue = {
  freeze: boolean;
  error: boolean;
  state: EditorStateV2;
  dispatch: Dispatch;
  undo: () => void;
  redo: () => void;
};

export const EditorContextV2 = createContext<EditorContextValue | undefined>(undefined);

export function useEditorV2(): EditorContextValue {
  const value = useContext(EditorContextV2);
  if (!value) throw new Error("useEditorV2 must be used inside EditorV2");
  return value;
}
