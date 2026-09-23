import { type FontType, type ListType } from "~/types/brevbakerTypes";

// Shared intermediate representation produced by both the HTML clipboard
// traversal (see `paste.ts`) and the RTF clipboard interpreter (see
// `rtf/parseRtf.ts`) before being inserted into the letter draft. Keeping a
// single shared model means the insertion logic in `paste.ts`
// (`insertTraversedElements`, `insertTable`, `insertItem`, etc.) does not need
// to know which clipboard format produced its input.

export interface Text {
  type: "TEXT";
  font: FontType;
  text: string;
}

export interface ItemElement {
  type: "ITEM";
  content: Text[];
  listType?: ListType;
}

export interface ParagraphElement {
  type: "P";
  content: Text[];
}
export interface Title1Element {
  type: "H1";
  content: Text[];
}

export interface Title2Element {
  type: "H2";
  content: Text[];
}

export interface Title3Element {
  type: "H3";
  content: Text[];
}

export interface TableCell {
  content: Text[];
}

export interface TableRow {
  cells: TableCell[];
}

export interface Table {
  type: "TABLE";
  rows: TableRow[];
  headerCells?: TableCell[];
}

export type TraversedElement =
  | ParagraphElement
  | Text
  | ItemElement
  | Title1Element
  | Title2Element
  | Title3Element
  | Table;
