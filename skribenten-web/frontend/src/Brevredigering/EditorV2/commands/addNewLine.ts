import { insertAt } from "../model/collection";
import { type Caret, caret, resolve } from "../model/paths";
import {
  contentCollection,
  isEmptyText,
  isLiteral,
  isNewLine,
  lengthOf,
  newLiteral,
  newNewLine,
  splitLiteral,
} from "../model/text";
import { type Command } from "../state/editorState";

export const addNewLine: Command<[at: Caret]> = (draft, at) => {
  const doc = draft.redigertBrev;
  const allowed =
    at.path.kind === "item" || (at.path.kind === "block" && doc.blocks[at.path.block]?.type === "PARAGRAPH");
  const container = allowed ? resolve(doc, at.path) : undefined;
  const literal = container?.content[at.content];
  if (!container || !isLiteral(literal)) return;

  const previous = container.content[at.content - 1];
  const next = container.content[at.content + 1];
  const atStart = at.offset === 0;
  const atEnd = at.offset >= lengthOf(literal);
  if (
    isEmptyText(literal)
      ? isNewLine(previous) || isNewLine(next)
      : (atStart && isNewLine(previous)) || (atEnd && isNewLine(next))
  ) {
    return;
  }

  const items = contentCollection(container);
  let index = atStart || isEmptyText(literal) ? at.content : at.content + 1;
  if (!atStart && !atEnd) insertAt(items, index, splitLiteral(literal, at.offset));
  insertAt(items, index, newNewLine());
  if (!isLiteral(container.content[index - 1])) {
    insertAt(items, index, newLiteral());
    index++;
  }
  if (!isLiteral(container.content[index + 1])) insertAt(items, index + 1, newLiteral());
  draft.focus = caret(at.path, index + 1, 0);
};
