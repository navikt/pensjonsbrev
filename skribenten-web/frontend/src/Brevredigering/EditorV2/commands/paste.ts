import { type Draft } from "immer";

import {
  type ItemElement,
  type Table,
  type Text,
  type TraversedElement,
} from "~/Brevredigering/LetterEditor/actions/traversedElement";
import { type ListType } from "~/types/brevbakerTypes";
import {
  type BlockV2,
  type DocumentV2,
  type FontTypeV2,
  type ListBlockTypeV2,
  type TextBlockTypeV2,
} from "~/types/brevV2";

import { buildTable, literalOf } from "../clipboard/buildTable";
import { fitElements } from "../clipboard/fitElements";
import { readClipboard } from "../clipboard/readClipboard";
import { changeBlockType } from "../model/blockType";
import { append, collection, insertAt, isNew, removeAt, removeFrom, transfer } from "../model/collection";
import { newItem, newList, newParagraph, newTextBlock } from "../model/factories";
import {
  type Caret,
  type ContainerPath,
  caret,
  isListBlock,
  isTextBlock,
  listAt,
  resolve,
  type TextContainer,
} from "../model/paths";
import {
  blockCollection,
  endOf,
  insertSibling,
  isEmptyContainer,
  linearOf,
  normalize,
  splitContent,
} from "../model/structure";
import {
  cleanseText,
  contentCollection,
  fontTypeOf,
  isLiteral,
  newLiteral,
  setText,
  splitLiteral,
  textOf,
} from "../model/text";
import { type Command, type EditorStateV2 } from "../state/editorState";
import { focusEnd, focusIn } from "./focus";
import { split } from "./split";

type EditorDraft = Draft<EditorStateV2>;
type BlockPath = Extract<ContainerPath, { kind: "block" }>;
type ItemPath = Extract<ContainerPath, { kind: "item" }>;

export function clipboardContentAt(
  doc: DocumentV2,
  at: Caret,
  clipboard: DataTransfer,
): TraversedElement[] | undefined {
  return readClipboard(clipboard, (elements) => fitElements(doc, at, elements));
}

export const paste: Command<[at: Caret, clipboard: DataTransfer]> = (draft, at, clipboard) => {
  const elements = clipboardContentAt(draft.redigertBrev, at, clipboard);
  if (elements) insertElements(draft, at, elements);
};

export const insertElements: Command<[at: Caret, elements: TraversedElement[]]> = (draft, at, elements) => {
  draft.focus = at;
  if (elements.every((element) => element.type === "TEXT")) {
    for (const element of elements) insertText(draft, element.text, element.font, false);
  } else {
    insertSequence(draft, elements);
  }
};

function insertSequence(draft: EditorDraft, elements: TraversedElement[]) {
  let trailing: BlockV2 | undefined;
  let pastedListType: ListType | undefined;
  for (const [index, element] of elements.entries()) {
    const previous = elements[index - 1];
    if (previous?.type === "ITEM" && element.type !== "TEXT" && !continuesList(pastedListType, element)) {
      trailing = leavePastedList(draft) ?? trailing;
    } else if (previous?.type === "TABLE") {
      trailing = leavePastedTable(draft) ?? trailing;
    }
    if (element.type === "ITEM" && !element.nested && element.listType !== undefined) {
      pastedListType = element.listType;
    }
    insertElement(draft, element);
  }
  if (trailing) removeCursorParagraph(draft, trailing);
}

function insertElement(draft: EditorDraft, element: TraversedElement) {
  switch (element.type) {
    case "TEXT":
      return insertText(draft, element.text, element.font, true);
    case "P":
      return insertBlock(draft, "PARAGRAPH", element.content);
    case "H1":
      return insertBlock(draft, "TITLE2", element.content);
    case "H2":
      return insertBlock(draft, "TITLE3", element.content);
    case "H3":
      return insertBlock(draft, "TITLE4", element.content);
    case "ITEM":
      return insertItem(draft, element);
    case "TABLE":
      return insertTable(draft, element);
  }
}

function insertText(draft: EditorDraft, text: string, font: FontTypeV2, multipart: boolean) {
  const { path, content: index, offset } = draft.focus;
  const container = resolve(draft.redigertBrev, path);
  const literal = container?.content[index];
  if (!container || !isLiteral(literal)) return;
  const inserted = cleanseText(text);
  const existing = textOf(literal);
  const at = Math.min(Math.max(0, offset), existing.length);

  if ((isNew(literal) || at > 0 || !multipart) && font === fontTypeOf(literal)) {
    setText(literal, existing.slice(0, at) + inserted + existing.slice(at));
    draft.focus = caret(path, index, at + inserted.length);
    return;
  }
  const pasted = newLiteral({ text: inserted, fontType: font });
  if (at > 0 && at < existing.length) {
    insertAt(contentCollection(container), index + 1, pasted, splitLiteral(literal, at));
    draft.focus = caret(path, index + 1, inserted.length);
  } else {
    const position = at > 0 ? index + 1 : index;
    insertAt(contentCollection(container), position, pasted);
    draft.focus = caret(path, position, inserted.length);
  }
}

function insertTextAndSplit(draft: EditorDraft, content: Text[]) {
  for (const run of content) insertText(draft, run.text, run.font, true);
  split(draft, draft.focus);
}

function insertBlock(draft: EditorDraft, type: TextBlockTypeV2, content: Text[]) {
  const doc = draft.redigertBrev;
  const path = draft.focus.path;
  const container = resolve(doc, path);
  const atStart = linearOf(doc, draft.focus) === 0;
  if (!container || !atStart || (path.kind !== "block" && path.kind !== "item")) {
    insertTextAndSplit(draft, content);
  } else if (path.kind === "item") {
    insertSibling(doc, path, "before", content.map(literalOf));
    focusIn(draft, container);
  } else if (isEmptyContainer(container)) {
    retype(doc, path, type);
    insertTextAndSplit(draft, content);
  } else {
    insertAt(blockCollection(doc), path.block, newTextBlock(type, content.map(literalOf)));
    focusIn(draft, container);
  }
}

function retype(doc: DocumentV2, path: BlockPath, type: TextBlockTypeV2) {
  const block = doc.blocks[path.block];
  if (isTextBlock(block)) changeBlockType(block, type);
}

function insertItem(draft: EditorDraft, element: ItemElement) {
  const path = draft.focus.path;
  if (path.kind === "item") {
    insertBlock(draft, "PARAGRAPH", element.content);
  } else if (path.kind === "block" && isTextBlock(draft.redigertBrev.blocks[path.block])) {
    startListAt(draft, path, element.listType);
    insertTextAndSplit(draft, element.content);
  }
}

function startListAt(draft: EditorDraft, path: BlockPath, listType: ListType | undefined) {
  const doc = draft.redigertBrev;
  const block = doc.blocks[path.block];
  if (!isTextBlock(block)) return;
  const item = newItem();
  moveContentBefore(block, draft.focus, item);

  const blocks = blockCollection(doc);
  const previous = doc.blocks[path.block - 1];
  const type = listType ? listBlockType(listType) : isListBlock(previous) ? previous.type : "ITEM_LIST";
  let blockIndex = path.block;
  if (isListBlock(previous) && previous.type === type) {
    append(collection(previous.items, previous.deletedItems, previous), item);
  } else {
    insertAt(blocks, path.block, newList(type, [item]));
    blockIndex++;
  }
  if (isNew(block) && isEmptyContainer(block)) removeAt(blocks, blockIndex);
  focusEnd(draft, item);
}

function moveContentBefore(source: TextContainer, at: Caret, target: TextContainer) {
  const from = contentCollection(source);
  const text = source.content[at.content];
  if (isLiteral(text) && at.offset > 0) insertAt(from, at.content + 1, splitLiteral(text, at.offset));
  transfer(from, 0, at.content + (at.offset > 0 ? 1 : 0), contentCollection(target), 0);
  normalize(source);
  normalize(target);
}

function listBlockType(listType: ListType): ListBlockTypeV2 {
  switch (listType) {
    case "PUNKTLISTE":
      return "ITEM_LIST";
    case "NUMMERERT_LISTE":
      return "NUMBERED_LIST";
  }
}

function continuesList(pastedListType: ListType | undefined, element: TraversedElement): boolean {
  if (element.type !== "ITEM") return false;
  return (
    element.nested === true ||
    element.listType === undefined ||
    pastedListType === undefined ||
    pastedListType === element.listType
  );
}

function leavePastedList(draft: EditorDraft): BlockV2 | undefined {
  const path = draft.focus.path;
  if (path.kind !== "item") return undefined;
  const list = listAt(draft.redigertBrev, path.block);
  if (!list || path.item === 0 || path.item !== list.items.length - 1) return undefined;
  if (!isEmptyContainer(list.items[path.item])) return undefined;
  removeAt(collection(list.items, list.deletedItems, list), path.item);
  return focusBlockAfter(draft, path.block);
}

function leavePastedTable(draft: EditorDraft): BlockV2 | undefined {
  const path = draft.focus.path;
  return path.kind === "header" || path.kind === "cell" ? focusBlockAfter(draft, path.block) : undefined;
}

function focusBlockAfter(draft: EditorDraft, blockIndex: number): BlockV2 | undefined {
  const doc = draft.redigertBrev;
  const next = doc.blocks[blockIndex + 1];
  if (isTextBlock(next)) {
    focusIn(draft, next);
    return undefined;
  }
  const paragraph = newParagraph();
  insertAt(blockCollection(doc), blockIndex + 1, paragraph);
  focusIn(draft, paragraph);
  return next;
}

function removeCursorParagraph(draft: EditorDraft, trailing: BlockV2) {
  const path = draft.focus.path;
  if (path.kind !== "block" || path.block === 0) return;
  const blocks = draft.redigertBrev.blocks;
  const cursor = blocks[path.block];
  const previous = blocks[path.block - 1];
  if (blocks[path.block + 1] !== trailing || cursor?.type !== "PARAGRAPH") return;
  if (!isNew(cursor) || !isEmptyContainer(cursor)) return;
  if (!isTextBlock(previous) || !isLiteral(previous.content.at(-1))) return;
  removeAt(blockCollection(draft.redigertBrev), path.block);
  focusEnd(draft, previous);
}

function insertTable(draft: EditorDraft, element: Table) {
  const doc = draft.redigertBrev;
  const path = draft.focus.path;
  const index =
    path.kind === "item"
      ? tableIndexInList(doc, path, draft.focus)
      : path.kind === "block" && doc.blocks[path.block]?.type === "PARAGRAPH"
        ? tableIndexInParagraph(doc, path, draft.focus)
        : undefined;
  if (index === undefined) return;
  const table = buildTable(element);
  insertAt(blockCollection(doc), index, table);
  draft.focus = caret(
    table.rows.length > 0
      ? { kind: "cell", block: index, row: 0, cell: 0 }
      : { kind: "header", block: index, column: 0 },
  );
}

function tableIndexInParagraph(doc: DocumentV2, path: BlockPath, at: Caret): number | undefined {
  const block = doc.blocks[path.block];
  if (!isTextBlock(block)) return undefined;
  const linear = linearOf(doc, at);
  if (linear === 0) return path.block;
  if (linear < endOf(block)) insertAt(blockCollection(doc), path.block + 1, newParagraph(splitContent(block, at)));
  return path.block + 1;
}

function tableIndexInList(doc: DocumentV2, path: ItemPath, at: Caret): number | undefined {
  const list = listAt(doc, path.block);
  const item = list?.items[path.item];
  if (!list || !item) return undefined;
  const items = collection(list.items, list.deletedItems, list);
  const linear = linearOf(doc, at);
  const following =
    linear === 0
      ? removeFrom(items, path.item)
      : [...(linear < endOf(item) ? [newItem(splitContent(item, at))] : []), ...removeFrom(items, path.item + 1)];
  if (isEmptyContainer(following[0])) following.shift();

  const blocks = blockCollection(doc);
  const index = list.items.length === 0 ? path.block : path.block + 1;
  if (list.items.length === 0) removeAt(blocks, path.block);
  if (following.length > 0) insertAt(blocks, index, newList(list.type, following));
  return index;
}
