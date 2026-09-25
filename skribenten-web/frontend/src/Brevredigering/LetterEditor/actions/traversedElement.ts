import { type FontType, type ListType } from "~/types/brevbakerTypes";

// Produced by both the HTML and the RTF clipboard parsers, and inserted into the letter by `paste.ts`.

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
