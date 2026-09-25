import { walkRtfContent } from "~/Brevredigering/LetterEditor/actions/rtf/rtfContentWalker";
import {
  extractStylesheetHeadingLevels,
  type HeadingLevel,
  headingLevelFromOutlineLevel,
} from "~/Brevredigering/LetterEditor/actions/rtf/rtfStylesheet";
import { type RtfToken } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";
import {
  type TableCell,
  type TableRow,
  type Text,
  type TraversedElement,
} from "~/Brevredigering/LetterEditor/actions/traversedElement";
import { FontType, ListType } from "~/types/brevbakerTypes";

// Character properties are reset by `\plain`, paragraph properties by `\pard`.
interface RtfState {
  bold: boolean;
  italic: boolean;
  styleNumber?: number;
  outlineLevel?: number;
  listId?: number;
  inTable: boolean;
}

const HEADING_TYPES = { 1: "H1", 2: "H2", 3: "H3" } as const;

/**
 * Interprets RTF written by a word processor (e.g. Word) into the same elements as the HTML paste
 * path. Supports paragraphs, bold, italic, headings, lists and simple tables; other formatting,
 * images and objects are ignored.
 */
export function interpretNativeRtf(tokens: RtfToken[]): TraversedElement[] {
  const headingLevelsByStyle = extractStylesheetHeadingLevels(tokens);
  const headingLevelOf = (state: RtfState): HeadingLevel | undefined =>
    headingLevelFromOutlineLevel(state.outlineLevel) ??
    (state.styleNumber === undefined ? undefined : headingLevelsByStyle.get(state.styleNumber));

  const elements: TraversedElement[] = [];
  const stack: RtfState[] = [{ bold: false, italic: false, inTable: false }];
  const state = () => stack.at(-1)!;

  let paragraph: Text[] = [];
  // The paragraph's properties when its text was written. The group that set them may be closed
  // before the paragraph ends.
  let paragraphState: RtfState | undefined;
  let listMarker: string | undefined;
  let rowCells: TableCell[] = [];
  let tableRows: TableRow[] = [];

  const appendText = (value: string) => {
    if (value.length === 0) return;
    const { bold, italic } = state();
    const font = bold ? FontType.BOLD : italic ? FontType.ITALIC : FontType.PLAIN;
    const last = paragraph.at(-1);
    if (last?.font === font) {
      last.text += value;
    } else {
      paragraph.push({ type: "TEXT", font, text: value });
    }
    paragraphState = { ...state() };
  };

  const takeParagraph = (): Text[] => {
    const content = paragraph;
    paragraph = [];
    paragraphState = undefined;
    listMarker = undefined;
    return content;
  };

  const endParagraph = () => {
    if (paragraph.length === 0 || paragraphState === undefined) return;
    const { listId } = paragraphState;
    const headingLevel = headingLevelOf(paragraphState);
    const marker = listMarker;
    const content = takeParagraph();

    if (listId !== undefined) {
      elements.push({ type: "ITEM", content, listType: listTypeOfMarker(marker) });
    } else if (headingLevel === undefined) {
      elements.push({ type: "P", content });
    } else {
      elements.push({ type: HEADING_TYPES[headingLevel], content });
    }
  };

  const endTable = () => {
    if (tableRows.length > 0) elements.push({ type: "TABLE", rows: tableRows });
    tableRows = [];
  };

  for (const event of walkRtfContent(tokens)) {
    switch (event.kind) {
      case "groupStart": {
        stack.push({ ...state() });
        break;
      }
      case "groupEnd": {
        if (stack.length > 1) stack.pop();
        break;
      }
      case "listMarkerText": {
        listMarker = event.value;
        break;
      }
      case "text": {
        appendText(event.value);
        break;
      }
      case "control": {
        const current = state();
        switch (event.name) {
          case "par": {
            if (current.inTable) {
              if (paragraph.length > 0) appendText(" ");
            } else {
              endTable();
              endParagraph();
            }
            break;
          }
          case "line":
          case "tab": {
            appendText(" ");
            break;
          }
          case "pard": {
            Object.assign(current, {
              styleNumber: undefined,
              outlineLevel: undefined,
              listId: undefined,
              inTable: false,
            });
            break;
          }
          case "plain": {
            Object.assign(current, { bold: false, italic: false });
            break;
          }
          case "b": {
            current.bold = event.param !== 0;
            break;
          }
          case "i": {
            current.italic = event.param !== 0;
            break;
          }
          case "s": {
            current.styleNumber = event.param;
            break;
          }
          case "outlinelevel": {
            current.outlineLevel = event.param;
            break;
          }
          case "ls": {
            current.listId = event.param;
            break;
          }
          case "intbl": {
            current.inTable = true;
            break;
          }
          case "cell": {
            rowCells.push({ content: takeParagraph() });
            break;
          }
          case "row": {
            tableRows.push({ cells: rowCells });
            rowCells = [];
            break;
          }
        }
        break;
      }
    }
  }

  endTable();

  // Copying part of a single paragraph gives RTF without a closing `\par`. Return it as loose text,
  // like inline HTML, so it is inserted into the current paragraph instead of splitting it.
  const isFragment =
    elements.length === 0 &&
    paragraphState !== undefined &&
    paragraphState.listId === undefined &&
    headingLevelOf(paragraphState) === undefined;
  if (isFragment) return paragraph;

  endParagraph();
  return elements;
}

// Heuristic based on the rendered marker, e.g. "1.", "a)" or "iv." versus "•".
function listTypeOfMarker(marker: string | undefined): ListType {
  const isNumbered = /^(?:\d+[.)]?|[a-z][.)]|[ivxlcdm]+[.)])$/i.test(marker?.trim() ?? "");
  return isNumbered ? ListType.NUMMERERT_LISTE : ListType.PUNKTLISTE;
}
