import { type Caret, resolve, sibling } from "../model/paths";
import { breakOutOfList, endOf, insertSibling, isEmptyContainer, linearOf, splitContent } from "../model/structure";
import { type Command } from "../state/editorState";
import { focusIn } from "./focus";

export const split: Command<[at: Caret]> = (draft, at) => {
  const doc = draft.redigertBrev;
  const path = at.path;
  if (path.kind !== "block" && path.kind !== "item") return;
  const container = resolve(doc, path);
  if (!container) return;

  if (path.kind === "item" && isEmptyContainer(container)) {
    focusIn(draft, breakOutOfList(doc, path));
    return;
  }
  if (isEmptyContainer(container)) return;

  const linear = linearOf(doc, at);
  if (linear === 0) {
    if (isEmptyContainer(resolve(doc, sibling(path, -1)))) return;
    insertSibling(doc, path, "before");
    focusIn(draft, container);
  } else {
    if (path.kind === "item" && linear === endOf(container) && isEmptyContainer(resolve(doc, sibling(path, 1)))) return;
    focusIn(draft, insertSibling(doc, path, "after", splitContent(container, at)));
  }
};
