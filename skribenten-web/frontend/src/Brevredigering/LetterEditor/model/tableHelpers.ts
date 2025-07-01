import type {
  Cell,
  ColumnAlignment,
  ColumnSpec,
  Header,
  LiteralValue,
  Row,
  Table,
  TextContent,
} from "~/types/brevbakerTypes";
import { FontType, LITERAL } from "~/types/brevbakerTypes";
import type { Nullable } from "~/types/Nullable";

const nullId = (): Nullable<number> => null;

const newLiteral = (): LiteralValue => ({
  id: nullId(),
  parentId: nullId(),
  type: LITERAL,
  text: "",
  editedText: null,
  fontType: FontType.PLAIN,
  editedFontType: null,
  tags: [],
});

const emptyTextArray = (): TextContent[] => [newLiteral()];

export const newCell = (): Cell => ({
  id: nullId(),
  parentId: nullId(),
  text: emptyTextArray(),
});

export const newRow = (cols: number): Row => ({
  id: nullId(),
  parentId: nullId(),
  cells: Array.from({ length: cols }, newCell),
});

const newColSpec = (): ColumnSpec => ({
  id: nullId(),
  parentId: nullId(),
  headerContent: newCell(),
  alignment: "LEFT" satisfies ColumnAlignment,
  span: 1,
});

const newHeader = (cols: number): Header => ({
  id: nullId(),
  parentId: nullId(),
  colSpec: Array.from({ length: cols }, newColSpec),
});

export const newTable = (rows = 2, cols = 2): Table => ({
  id: nullId(),
  parentId: nullId(),
  type: "TABLE",
  header: newHeader(cols),
  rows: Array.from({ length: rows }, () => newRow(cols)),
  deletedRows: [],
});

export const pushRow = (table: Table): void => {
  table.rows.push(newRow(table.header.colSpec.length));
};

export const pushCol = (table: Table): void => {
  table.header.colSpec.push(newColSpec());

  table.rows.forEach((r) => r.cells.push(newCell()));
};
