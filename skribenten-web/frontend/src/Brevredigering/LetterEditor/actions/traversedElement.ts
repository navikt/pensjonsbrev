import { cleanseText } from "~/Brevredigering/LetterEditor/actions/common";
import { type FontType, type ListType } from "~/types/brevbakerTypes";

/**
 * Intermediate representation produced by both the HTML (`paste.ts`) and RTF (`rtf/interpretNativeRtf.ts`)
 * clipboard parsers. Keeping this shape shared means the insertion logic in `paste.ts`
 * (`insertTraversedElements`, `insertBlock`, `insertItem`, `insertTable`, etc.) is completely
 * format-agnostic: it only ever operates on `TraversedElement[]`, regardless of whether the
 * content originated as HTML or RTF.
 */

export interface Text {
  type: "TEXT";
  font: FontType;
  text: string;
}

export interface ItemElement {
  type: "ITEM";
  content: Text[];
  listType?: ListType;
  /** Punkt fra en underliste (HTML, eller `\ilvl` i RTF); flates ut i ytre liste i stedet for å starte en ny liste. */
  nested?: boolean;
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

export function cleansePastedText(str: string): string {
  return cleanseText(str).replaceAll(/\s+/g, " ");
}

/** Merges neighbouring same-font text runs; linear, since RTF can produce tens of thousands of runs. */
export function mergeNeighbouringText<T extends TraversedElement>(elements: T[]): T[] {
  const merged: T[] = [];
  let run: string[] = [];

  const flushRun = () => {
    const last = merged.at(-1);
    if (run.length > 1 && last?.type === "TEXT") {
      merged[merged.length - 1] = { ...last, text: cleansePastedText(run.join("")) };
    }
    run = [];
  };

  for (const curr of elements) {
    const previous = merged.at(-1);
    if (previous?.type === "TEXT" && curr.type === "TEXT" && previous.font === curr.font) {
      run.push(curr.text);
    } else {
      flushRun();
      merged.push(curr);
      if (curr.type === "TEXT") run = [curr.text];
    }
  }
  flushRun();

  return merged;
}
