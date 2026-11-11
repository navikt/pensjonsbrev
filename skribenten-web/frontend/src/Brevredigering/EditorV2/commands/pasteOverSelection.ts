import { compareCarets, type Selection } from "../model/paths";
import { type Command } from "../state/editorState";
import { deleteSelection } from "./deleteSelection";
import { clipboardContentAt, insertElements } from "./paste";

export const pasteOverSelection: Command<[selection: Selection, clipboard: DataTransfer]> = (
  draft,
  selection,
  clipboard,
) => {
  const start = compareCarets(selection.anchor, selection.focus) <= 0 ? selection.anchor : selection.focus;
  if (!clipboardContentAt(draft.redigertBrev, start, clipboard)) return;
  deleteSelection(draft, selection);
  const at = { ...draft.focus };
  const elements = clipboardContentAt(draft.redigertBrev, at, clipboard);
  if (elements) insertElements(draft, at, elements);
};
