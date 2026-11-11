import {
  type BlockV2,
  type CellV2,
  type ColumnSpecV2,
  type DocumentV2,
  type ItemV2,
  type ListBlockV2,
  type TableV2,
  type TextBlockV2,
  type TextV2,
} from "~/types/brevV2";

export type ContainerPath =
  | { kind: "title" }
  | { kind: "block"; block: number }
  | { kind: "item"; block: number; item: number }
  | { kind: "header"; block: number; column: number }
  | { kind: "cell"; block: number; row: number; cell: number };

export type Caret = { path: ContainerPath; content: number; offset: number };

export type Selection = { anchor: Caret; focus: Caret };

export type TextContainer = { content: TextV2[]; deletedContent: number[]; id?: number | null };

export const TITLE: ContainerPath = { kind: "title" };

export function caret(path: ContainerPath, content = 0, offset = 0): Caret {
  return { path, content, offset };
}

export function isTextBlock(block: BlockV2 | undefined): block is TextBlockV2 {
  switch (block?.type) {
    case "PARAGRAPH":
    case "TITLE2":
    case "TITLE3":
    case "TITLE4":
      return true;
    default:
      return false;
  }
}

export function isListBlock(block: BlockV2 | undefined): block is ListBlockV2 {
  return block?.type === "ITEM_LIST" || block?.type === "NUMBERED_LIST";
}

export function isTable(block: BlockV2 | undefined): block is TableV2 {
  return block?.type === "TABLE";
}

export function resolve(doc: DocumentV2, path: ContainerPath): TextContainer | undefined {
  switch (path.kind) {
    case "title":
      return { content: doc.title.text, deletedContent: doc.title.deletedContent, id: null };
    case "block": {
      const block = doc.blocks[path.block];
      return isTextBlock(block) ? block : undefined;
    }
    case "item":
      return listAt(doc, path.block)?.items[path.item];
    case "header":
      return tableAt(doc, path.block)?.header.colSpec[path.column];
    case "cell":
      return tableAt(doc, path.block)?.rows[path.row]?.cells[path.cell];
  }
}

export function resolveText(doc: DocumentV2, at: Caret): TextV2 | undefined {
  return resolve(doc, at.path)?.content[at.content];
}

export function listAt(doc: DocumentV2, block: number): ListBlockV2 | undefined {
  const found = doc.blocks[block];
  return isListBlock(found) ? found : undefined;
}

export function tableAt(doc: DocumentV2, block: number): TableV2 | undefined {
  const found = doc.blocks[block];
  return isTable(found) ? found : undefined;
}

export function blockIndexOf(path: ContainerPath): number | undefined {
  return path.kind === "title" ? undefined : path.block;
}

export function blockPaths(block: BlockV2, index: number): ContainerPath[] {
  switch (block.type) {
    case "PARAGRAPH":
    case "TITLE2":
    case "TITLE3":
    case "TITLE4":
      return [{ kind: "block", block: index }];
    case "ITEM_LIST":
    case "NUMBERED_LIST":
      return block.items.map((_, item) => ({ kind: "item", block: index, item }));
    case "TABLE":
      return [
        ...block.header.colSpec.map((_, column): ContainerPath => ({ kind: "header", block: index, column })),
        ...block.rows.flatMap((row, rowIndex) =>
          row.cells.map((_, cell): ContainerPath => ({ kind: "cell", block: index, row: rowIndex, cell })),
        ),
      ];
  }
}

export function containers(doc: DocumentV2): ContainerPath[] {
  return doc.blocks.flatMap(blockPaths);
}

function ordinal(path: ContainerPath): number[] {
  switch (path.kind) {
    case "title":
      return [-1];
    case "block":
      return [path.block];
    case "item":
      return [path.block, path.item];
    case "header":
      return [path.block, -1, path.column];
    case "cell":
      return [path.block, path.row, path.cell];
  }
}

function compareNumbers(a: number[], b: number[]): number {
  for (let i = 0; i < Math.max(a.length, b.length); i++) {
    const x = a[i] ?? -Infinity;
    const y = b[i] ?? -Infinity;
    if (x !== y) return x < y ? -1 : 1;
  }
  return 0;
}

function padded(numbers: number[]): number[] {
  return [...numbers, ...Array<number>(3 - numbers.length).fill(-Infinity)];
}

export function comparePaths(a: ContainerPath, b: ContainerPath): number {
  return compareNumbers(ordinal(a), ordinal(b));
}

export function compareCarets(a: Caret, b: Caret): number {
  return compareNumbers(
    [...padded(ordinal(a.path)), a.content, a.offset],
    [...padded(ordinal(b.path)), b.content, b.offset],
  );
}

export function samePath(a: ContainerPath, b: ContainerPath): boolean {
  return comparePaths(a, b) === 0;
}

export function sameCaret(a: Caret, b: Caret): boolean {
  return samePath(a.path, b.path) && a.content === b.content && a.offset === b.offset;
}

export function next(doc: DocumentV2, path: ContainerPath): ContainerPath | undefined {
  if (path.kind === "title") return containers(doc)[0];
  const all = containers(doc);
  return all[all.findIndex((p) => samePath(p, path)) + 1];
}

export function previous(doc: DocumentV2, path: ContainerPath): ContainerPath | undefined {
  const all = containers(doc);
  const index = all.findIndex((p) => samePath(p, path));
  return index > 0 ? all[index - 1] : undefined;
}

export function pathKey(path: ContainerPath): string {
  switch (path.kind) {
    case "title":
      return "title";
    case "block":
      return `b${path.block}`;
    case "item":
      return `b${path.block}.i${path.item}`;
    case "header":
      return `b${path.block}.h${path.column}`;
    case "cell":
      return `b${path.block}.r${path.row}.c${path.cell}`;
  }
}

export function parsePathKey(key: string): ContainerPath | undefined {
  if (key === "title") return TITLE;
  const match = /^b(\d+)(?:\.(i|h)(\d+)|\.r(\d+)\.c(\d+))?$/.exec(key);
  if (!match) return undefined;
  const block = Number(match[1]);
  if (match[2] === "i") return { kind: "item", block, item: Number(match[3]) };
  if (match[2] === "h") return { kind: "header", block, column: Number(match[3]) };
  if (match[4] !== undefined) return { kind: "cell", block, row: Number(match[4]), cell: Number(match[5]) };
  return { kind: "block", block };
}

export type ContainerElement = TextBlockV2 | ItemV2 | ColumnSpecV2 | CellV2;

export function sibling(path: ContainerPath, delta: number): ContainerPath {
  switch (path.kind) {
    case "title":
      return path;
    case "block":
      return { ...path, block: path.block + delta };
    case "item":
      return { ...path, item: path.item + delta };
    case "header":
      return { ...path, column: path.column + delta };
    case "cell":
      return { ...path, cell: path.cell + delta };
  }
}
