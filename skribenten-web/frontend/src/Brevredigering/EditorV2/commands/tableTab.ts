import { isLastTableCell, tabTarget } from "../caret/navigation";
import { append, collection, insertAt } from "../model/collection";
import { newParagraph, newRow } from "../model/factories";
import { type Caret, caret, tableAt } from "../model/paths";
import { blockCollection } from "../model/structure";
import { type Command } from "../state/editorState";

export const tab: Command<[at: Caret, backward: boolean]> = (draft, at, backward) => {
  const path = at.path;
  if (path.kind !== "header" && path.kind !== "cell") return;
  const doc = draft.redigertBrev;
  const table = tableAt(doc, path.block);
  if (!table) return;

  if (!backward && isLastTableCell(doc, path)) {
    const columns = table.header.colSpec.length || (table.rows[0]?.cells.length ?? 0);
    append(collection(table.rows, table.deletedRows, table), newRow(columns));
    draft.focus = caret({ kind: "cell", block: path.block, row: table.rows.length - 1, cell: 0 });
    return;
  }

  const target = tabTarget(doc, path, backward);
  if (target) {
    draft.focus = caret(target);
  } else if (backward) {
    insertAt(blockCollection(doc), path.block, newParagraph());
    draft.focus = caret({ kind: "block", block: path.block });
  }
};
