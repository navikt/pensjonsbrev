import { type Draft } from "immer";

import { type DocumentV2, type TableV2 } from "~/types/brevV2";

import { append, insertAt, move, removeAt, removeFrom, replaceAll } from "../model/collection";
import { newCell, newColumn, newParagraph, newRow, newTable } from "../model/factories";
import {
  type Caret,
  type ContainerPath,
  caret,
  containers,
  resolve,
  type TextContainer,
  tableAt,
} from "../model/paths";
import { blockCollection, linearOf, normalize, removeBlock } from "../model/structure";
import { cellCollection, columnCollection, isEmptyHeader, labelDefaultHeaders, rowCollection } from "../model/table";
import { contentCollection, newLiteral } from "../model/text";
import { type Command, type EditorStateV2 } from "../state/editorState";
import { focusEnd, focusIn } from "./focus";

type TablePath = Extract<ContainerPath, { kind: "header" | "cell" }>;

type FocusedTable = { table: Draft<TableV2>; path: TablePath; column: number };

export type MoveDirection = "up" | "down";

function tableOf(doc: Draft<DocumentV2>, at: Caret): FocusedTable | undefined {
  const path = at.path;
  switch (path.kind) {
    case "header":
    case "cell": {
      const table = tableAt(doc, path.block);
      return table && { table, path, column: path.kind === "header" ? path.column : path.cell };
    }
    case "title":
    case "block":
    case "item":
      return undefined;
  }
}

function cellAt(table: TableV2, row: "header" | number, column: number): TextContainer | undefined {
  return row === "header" ? table.header.colSpec[column] : table.rows[row]?.cells[column];
}

function prepareTableInsertion(doc: Draft<DocumentV2>, at: Caret): number | undefined {
  const path = at.path;
  switch (path.kind) {
    case "block": {
      const paragraph = doc.blocks[path.block];
      if (paragraph?.type !== "PARAGRAPH") return undefined;
      const rest = removeFrom(contentCollection(paragraph), at.content + 1);
      if (rest.length > 0) {
        normalize(paragraph);
        insertAt(blockCollection(doc), path.block + 1, newParagraph(rest));
      }
      return path.block + 1;
    }
    case "item":
      return path.block + 1;
    case "title":
    case "header":
    case "cell":
      return undefined;
  }
}

export const insertTable: Command<[at: Caret, rows: number, columns: number]> = (draft, at, rows, columns) => {
  const doc = draft.redigertBrev;
  const index = prepareTableInsertion(doc, at);
  if (index === undefined) return;
  const table = newTable(columns, rows);
  labelDefaultHeaders(table);
  const blocks = blockCollection(doc);
  insertAt(blocks, index, table);
  if (index === doc.blocks.length - 1) insertAt(blocks, index + 1, newParagraph());
  focusIn(draft, table.header.colSpec[0]);
};

export const removeTable: Command<[at: Caret]> = (draft, at) => {
  const focused = tableOf(draft.redigertBrev, at);
  if (!focused) return;
  const doc = draft.redigertBrev;
  const index = focused.path.block;
  const all = containers(doc);
  const before = all.findLast((path) => path.kind !== "title" && path.block < index);
  const after = all.find((path) => path.kind !== "title" && path.block > index);
  const previous = before && resolve(doc, before);
  const next = after && resolve(doc, after);
  removeBlock(doc, index);
  if (previous) {
    focusEnd(draft, previous);
  } else if (next) {
    focusIn(draft, next);
  } else {
    const paragraph = newParagraph();
    insertAt(blockCollection(doc), 0, paragraph);
    focusIn(draft, paragraph);
  }
};

export const removeTableRow: Command<[at: Caret]> = (draft, at) => {
  const focused = tableOf(draft.redigertBrev, at);
  if (!focused) return;
  const { table, path } = focused;
  if (path.kind === "header") {
    for (const column of table.header.colSpec) replaceAll(contentCollection(column), newLiteral());
    focusIn(draft, cellAt(table, 0, 0) ?? cellAt(table, "header", 0));
    return;
  }
  const columns = table.header.colSpec.length || table.rows[path.row]?.cells.length || 1;
  const rows = rowCollection(table);
  removeAt(rows, path.row);
  if (rows.items.length === 0) insertAt(rows, 0, newRow(columns));
  focusIn(draft, cellAt(table, Math.min(path.row, table.rows.length - 1), 0));
};

export const removeTableColumn: Command<[at: Caret]> = (draft, at) => {
  const focused = tableOf(draft.redigertBrev, at);
  if (!focused) return;
  const { table, path, column } = focused;
  if (table.header.colSpec.length <= 1) {
    removeTable(draft, at);
    return;
  }
  removeAt(columnCollection(table), column);
  for (const row of table.rows) removeAt(cellCollection(row), column);
  labelDefaultHeaders(table);
  const row = path.kind === "header" ? "header" : path.row;
  focusIn(draft, cellAt(table, row, Math.min(column, table.header.colSpec.length - 1)));
};

function insertColumn(draft: Draft<EditorStateV2>, at: Caret, side: "left" | "right") {
  const focused = tableOf(draft.redigertBrev, at);
  if (!focused) return;
  const { table, path, column } = focused;
  const current = resolve(draft.redigertBrev, path);
  const linear = linearOf(draft.redigertBrev, at);
  const index = side === "left" ? column : column + 1;
  insertAt(columnCollection(table), index, newColumn());
  for (const row of table.rows) insertAt(cellCollection(row), index, newCell());
  labelDefaultHeaders(table);
  focusIn(draft, current, linear);
}

export const insertTableColumnLeft: Command<[at: Caret]> = (draft, at) => insertColumn(draft, at, "left");

export const insertTableColumnRight: Command<[at: Caret]> = (draft, at) => insertColumn(draft, at, "right");

function insertRow(draft: Draft<EditorStateV2>, at: Caret, indexOf: (path: TablePath) => number | undefined) {
  const focused = tableOf(draft.redigertBrev, at);
  const index = focused && indexOf(focused.path);
  if (!focused || index === undefined) return;
  insertAt(rowCollection(focused.table), index, newRow(focused.table.header.colSpec.length));
  focusIn(draft, cellAt(focused.table, index, 0));
}

export const insertTableRowAbove: Command<[at: Caret]> = (draft, at) =>
  insertRow(draft, at, (path) => (path.kind === "cell" ? path.row : undefined));

export const insertTableRowBelow: Command<[at: Caret]> = (draft, at) =>
  insertRow(draft, at, (path) => (path.kind === "cell" ? path.row + 1 : 0));

export const moveTableRow: Command<[at: Caret, direction: MoveDirection]> = (draft, at, direction) => {
  const focused = tableOf(draft.redigertBrev, at);
  if (focused?.path.kind !== "cell") return;
  const { table, path } = focused;
  const target = direction === "up" ? path.row - 1 : path.row + 1;
  if (target < 0 || target >= table.rows.length) return;
  move(rowCollection(table), path.row, target);
  draft.focus = caret({ ...path, row: target }, at.content, at.offset);
};

export const promoteRowToHeader: Command<[at: Caret]> = (draft, at) => {
  const focused = tableOf(draft.redigertBrev, at);
  if (focused?.path.kind !== "cell") return;
  const { table, path } = focused;
  const source = table.rows[path.row];
  if (!source || !isEmptyHeader(table) || source.cells.length !== table.header.colSpec.length) return;
  table.header.colSpec.forEach((column, index) => {
    replaceAll(contentCollection(column), ...removeFrom(contentCollection(source.cells[index]), 0));
  });
  if (isEmptyHeader(table)) labelDefaultHeaders(table);
  removeAt(rowCollection(table), path.row);
  focusIn(draft, cellAt(table, "header", 0));
};

export const demoteHeaderToRow: Command<[at: Caret]> = (draft, at) => {
  const table = tableOf(draft.redigertBrev, at)?.table;
  if (!table || isEmptyHeader(table)) return;
  const row = newRow(table.header.colSpec.length);
  table.header.colSpec.forEach((column, index) => {
    const header = contentCollection(column);
    replaceAll(contentCollection(row.cells[index]), ...removeFrom(header, 0));
    append(header, newLiteral());
  });
  insertAt(rowCollection(table), 0, row);
  focusIn(draft, cellAt(table, 0, 0));
};
