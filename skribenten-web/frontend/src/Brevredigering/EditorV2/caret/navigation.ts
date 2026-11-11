import { type DocumentV2 } from "~/types/brevV2";

import { blockPaths, type ContainerPath, containers, next, previous, TITLE, tableAt } from "../model/paths";

export type Vertical = "up" | "down";

type TablePath = Extract<ContainerPath, { kind: "header" | "cell" }>;

function isTablePath(path: ContainerPath): path is TablePath {
  return path.kind === "header" || path.kind === "cell";
}

function outsideTable(doc: DocumentV2, block: number, direction: Vertical): ContainerPath | undefined {
  const table = tableAt(doc, block);
  if (!table) return undefined;
  const paths = blockPaths(table, block);
  const edge = direction === "up" ? paths.at(0) : paths.at(-1);
  return edge && neighbour(doc, edge, direction);
}

function neighbour(doc: DocumentV2, path: ContainerPath, direction: Vertical): ContainerPath | undefined {
  if (direction === "down") return path.kind === "title" ? containers(doc).at(0) : next(doc, path);
  if (path.kind === "title") return undefined;
  return previous(doc, path) ?? TITLE;
}

function enterTable(doc: DocumentV2, path: ContainerPath | undefined, direction: Vertical): ContainerPath | undefined {
  if (!path || !isTablePath(path)) return path;
  const table = tableAt(doc, path.block);
  if (!table) return path;
  if (direction === "down") {
    return table.header.colSpec.length > 0
      ? { kind: "header", block: path.block, column: 0 }
      : { kind: "cell", block: path.block, row: 0, cell: 0 };
  }
  return table.rows.length > 0 ? { kind: "cell", block: path.block, row: table.rows.length - 1, cell: 0 } : path;
}

function withinTable(doc: DocumentV2, path: TablePath, direction: Vertical): ContainerPath | undefined {
  const table = tableAt(doc, path.block);
  if (!table) return undefined;
  const row = path.kind === "header" ? -1 : path.row;
  const column = path.kind === "header" ? path.column : path.cell;
  const target = direction === "up" ? row - 1 : row + 1;
  if (target === -1 && table.header.colSpec.length > 0) {
    return { kind: "header", block: path.block, column: Math.min(column, table.header.colSpec.length - 1) };
  }
  const cells = table.rows[target]?.cells.length ?? 0;
  if (target < 0 || cells === 0) return undefined;
  return { kind: "cell", block: path.block, row: target, cell: Math.min(column, cells - 1) };
}

export function verticalTarget(doc: DocumentV2, path: ContainerPath, direction: Vertical): ContainerPath | undefined {
  if (isTablePath(path)) {
    return withinTable(doc, path, direction) ?? enterTable(doc, outsideTable(doc, path.block, direction), direction);
  }
  return enterTable(doc, neighbour(doc, path, direction), direction);
}

export function isLastTableCell(doc: DocumentV2, path: ContainerPath): boolean {
  if (path.kind !== "cell") return false;
  const table = tableAt(doc, path.block);
  return !!table && path.row === table.rows.length - 1 && path.cell === (table.rows[path.row]?.cells.length ?? 0) - 1;
}

export function tabTarget(doc: DocumentV2, path: ContainerPath, backward: boolean): ContainerPath | undefined {
  return backward ? previous(doc, path) : next(doc, path);
}
