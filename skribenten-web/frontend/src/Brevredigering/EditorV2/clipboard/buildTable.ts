import {
  cleansePastedText,
  mergeNeighbouringText,
  type Table,
  type TableCell,
  type Text,
} from "~/Brevredigering/LetterEditor/actions/traversedElement";
import { type ColumnSpecV2, type LiteralV2, type RowV2, type TableV2 } from "~/types/brevV2";

import { newCell, newColumn, newRow, newTable } from "../model/factories";
import { newLiteral } from "../model/text";

export function hasCells(table: Table): boolean {
  return (table.headerCells?.length ?? 0) > 0 || table.rows.length > 0;
}

export function buildTable(table: Table): TableV2 {
  const headerCells = table.headerCells ?? [];
  const columns = Math.max(1, headerCells.length, ...table.rows.map((row) => row.cells.length));
  const promoteFirstRow = headerCells.length === 0;
  const header = promoteFirstRow ? (table.rows[0]?.cells ?? []) : headerCells;
  const body = promoteFirstRow ? table.rows.slice(1) : table.rows;
  const empty = newTable(0, 0);
  return {
    ...empty,
    header: { ...empty.header, colSpec: pad(header, columns).map(buildColumn) },
    rows: body.map((row) => buildRow(pad(row.cells, columns))),
  };
}

function pad(cells: TableCell[], columns: number): TableCell[] {
  return Array.from({ length: columns }, (_, index) => cells[index] ?? { content: [] });
}

function buildColumn(cell: TableCell): ColumnSpecV2 {
  const runs = mergeNeighbouringText(cell.content);
  const text = cleansePastedText(runs.map((run) => run.text).join(" "));
  return newColumn([newLiteral({ text, fontType: runs[0]?.font ?? "PLAIN" })]);
}

function buildRow(cells: TableCell[]): RowV2 {
  return { ...newRow(0), cells: cells.map((cell) => newCell(cell.content.map(literalOf))) };
}

export function literalOf(run: Text): LiteralV2 {
  return newLiteral({ text: run.text, fontType: run.font });
}
