import { type CellV2, type ColumnSpecV2, type RowV2, type TableV2 } from "~/types/brevV2";

import { type Collection, collection, replaceAll } from "./collection";
import { contentCollection, isEmptyContent, newLiteral, plainText } from "./text";

export function rowCollection(table: TableV2): Collection<RowV2> {
  return collection(table.rows, table.deletedRows, table);
}

export function columnCollection(table: TableV2): Collection<ColumnSpecV2> {
  return collection(table.header.colSpec, table.header.deletedColSpecs, table.header);
}

export function cellCollection(row: RowV2): Collection<CellV2> {
  return collection(row.cells, row.deletedCells, row);
}

export function isEmptyHeader(table: TableV2): boolean {
  return table.header.colSpec.every((column) => isEmptyContent(column.content));
}

const DEFAULT_LABEL = /^Kolonne\s+\d+$/i;

export function labelDefaultHeaders(table: TableV2): void {
  table.header.colSpec.forEach((column, index) => {
    const current = plainText(column.content).trim();
    const label = `Kolonne ${index + 1}`;
    if ((current === "" || DEFAULT_LABEL.test(current)) && current !== label) {
      replaceAll(contentCollection(column), newLiteral({ text: label }));
    }
  });
}
