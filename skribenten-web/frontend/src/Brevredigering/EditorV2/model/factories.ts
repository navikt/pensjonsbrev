import {
  type CellV2,
  type ColumnSpecV2,
  type ItemV2,
  type ListBlockTypeV2,
  type ListBlockV2,
  type ParagraphV2,
  type RowV2,
  type TableV2,
  type TextBlockTypeV2,
  type TextBlockV2,
  type TextV2,
} from "~/types/brevV2";

import { newLiteral } from "./text";

const fresh = { id: null, parentId: null } as const;

const withLiteral = (content?: TextV2[]) => (content && content.length > 0 ? content : [newLiteral()]);

export function newTextBlock(type: TextBlockTypeV2, content?: TextV2[]): TextBlockV2 {
  return { ...fresh, type, content: withLiteral(content), deletedContent: [], missingFromTemplate: false };
}

export function newParagraph(content?: TextV2[]): ParagraphV2 {
  return { ...fresh, type: "PARAGRAPH", content: withLiteral(content), deletedContent: [], missingFromTemplate: false };
}

export function newItem(content?: TextV2[]): ItemV2 {
  return { ...fresh, content: withLiteral(content), deletedContent: [] };
}

export function newList(type: ListBlockTypeV2, items: ItemV2[]): ListBlockV2 {
  return { ...fresh, type, items, deletedItems: [], missingFromTemplate: false };
}

export function newCell(content?: TextV2[]): CellV2 {
  return { ...fresh, content: withLiteral(content), deletedContent: [] };
}

export function newColumn(content?: TextV2[]): ColumnSpecV2 {
  return { ...fresh, alignment: "LEFT", span: 1, content: withLiteral(content), deletedContent: [] };
}

export function newRow(cells: number): RowV2 {
  return { ...fresh, cells: Array.from({ length: cells }, () => newCell()), deletedCells: [] };
}

export function newTable(columns: number, rows: number): TableV2 {
  return {
    ...fresh,
    type: "TABLE",
    header: { ...fresh, colSpec: Array.from({ length: columns }, () => newColumn()), deletedColSpecs: [] },
    rows: Array.from({ length: rows }, () => newRow(columns)),
    deletedRows: [],
    missingFromTemplate: false,
  };
}
