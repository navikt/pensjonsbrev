import { walkRtfContent } from "~/Brevredigering/LetterEditor/actions/rtf/rtfContentWalker";
import {
  classifyListMarkerText,
  extractStylesheetHeadingLevels,
} from "~/Brevredigering/LetterEditor/actions/rtf/rtfStylesheet";
import { type RtfToken } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";
import {
  type Table,
  type TableCell,
  type TableRow,
  type Text,
  type TraversedElement,
} from "~/Brevredigering/LetterEditor/actions/traversedElement";
import { FontType } from "~/types/brevbakerTypes";

// Paragraph/character-level state, inherited by nested groups and reset by
// `\pard` (paragraph properties) or `\plain` (character properties)
// respectively, mirroring the corresponding RTF control words' semantics.
interface RtfFrame {
  bold: boolean;
  italic: boolean;
  styleIndex?: number;
  outlineLevel?: number;
  listId?: number;
}

function childFrame(parent: RtfFrame): RtfFrame {
  return { ...parent };
}

/**
 * Interprets genuine/native RTF (e.g. pasted directly from Word - no
 * `\fromhtml1`/`\fromtext1` encapsulation marker; see
 * `extractEncapsulation.ts` for that case) into the same `TraversedElement[]`
 * model produced by the HTML clipboard traversal in `paste.ts`, so it can be
 * inserted via the existing `insertTraversedElements` machinery unchanged.
 *
 * Supported: paragraphs, bold, italic, headings (via `\outlinelevel` or a
 * heading-named paragraph style), bulleted/numbered lists, and simple
 * tables. Deliberately unsupported (silently ignored rather than crashing):
 * images/embedded objects, revision marks, footnotes, hyperlink field
 * codes, and character formatting beyond bold/italic (colors, underline,
 * strikethrough, fonts).
 */
export function interpretNativeRtf(tokens: RtfToken[]): TraversedElement[] {
  const headingLevelsByStyle = extractStylesheetHeadingLevels(tokens);
  const events = walkRtfContent(tokens);

  const elements: TraversedElement[] = [];

  const stack: RtfFrame[] = [{ bold: false, italic: false }];
  const current = () => stack.at(-1)!;

  let paragraphBuffer: Text[] = [];
  // Paragraph properties captured at the latest text run, so they survive
  // the group that set them being closed before the paragraph is flushed.
  let paragraphFrame: RtfFrame | undefined;
  let pendingListMarkerText: string | undefined;

  let pendingRows: TableRow[] = [];
  let inTableRow = false;
  let rowCells: TableCell[] = [];
  let cellBuffer: Text[] = [];

  const currentFont = (): FontType => {
    const frame = current();
    if (frame.bold) return FontType.BOLD;
    if (frame.italic) return FontType.ITALIC;
    return FontType.PLAIN;
  };

  // Merges consecutive text runs sharing the same font into a single `Text`
  // element rather than emitting one per underlying RTF token/escape (e.g.
  // a `\'hh` hex-escaped letter surrounded by plain text runs) - this keeps
  // output shape aligned with what the HTML traversal path produces.
  const pushText = (value: string) => {
    if (value.length === 0) return;
    const font = currentFont();
    if (!inTableRow) paragraphFrame = { ...current() };
    const buffer = inTableRow ? cellBuffer : paragraphBuffer;
    const last = buffer.at(-1);
    if (last && last.font === font) {
      last.text += value;
    } else {
      buffer.push({ type: "TEXT", font, text: value });
    }
  };

  const flushTable = () => {
    if (pendingRows.length > 0) {
      const table: Table = { type: "TABLE", rows: pendingRows };
      elements.push(table);
    }
    pendingRows = [];
  };

  const headingLevelOf = (frame: RtfFrame): 0 | 1 | 2 | undefined => {
    if (frame.outlineLevel === 0 || frame.outlineLevel === 1 || frame.outlineLevel === 2) return frame.outlineLevel;
    return frame.styleIndex === undefined ? undefined : headingLevelsByStyle.get(frame.styleIndex);
  };

  const flushParagraph = () => {
    if (paragraphBuffer.length === 0) {
      pendingListMarkerText = undefined;
      return;
    }
    const frame = paragraphFrame ?? current();
    const headingLevel = headingLevelOf(frame);

    if (frame.listId !== undefined) {
      const listType = classifyListMarkerText(pendingListMarkerText);
      elements.push({ type: "ITEM", content: paragraphBuffer, listType });
    } else if (headingLevel === 0) {
      elements.push({ type: "H1", content: paragraphBuffer });
    } else if (headingLevel === 1) {
      elements.push({ type: "H2", content: paragraphBuffer });
    } else if (headingLevel === 2) {
      elements.push({ type: "H3", content: paragraphBuffer });
    } else {
      elements.push({ type: "P", content: paragraphBuffer });
    }
    paragraphBuffer = [];
    paragraphFrame = undefined;
    pendingListMarkerText = undefined;
  };

  for (const event of events) {
    if (event.kind === "groupStart") {
      stack.push(childFrame(current()));
      continue;
    }
    if (event.kind === "groupEnd") {
      if (stack.length > 1) stack.pop();
      continue;
    }
    if (event.kind === "listMarkerText") {
      pendingListMarkerText = (pendingListMarkerText ?? "") + event.value;
      continue;
    }
    if (event.kind === "text") {
      pushText(event.value);
      continue;
    }

    // event.kind === "control"
    switch (event.name) {
      case "par": {
        if (!inTableRow) {
          if (pendingRows.length > 0) flushTable();
          flushParagraph();
        }
        break;
      }
      case "pard": {
        current().styleIndex = undefined;
        current().outlineLevel = undefined;
        current().listId = undefined;
        break;
      }
      case "plain": {
        current().bold = false;
        current().italic = false;
        break;
      }
      case "b": {
        current().bold = event.param !== 0;
        break;
      }
      case "i": {
        current().italic = event.param !== 0;
        break;
      }
      case "s": {
        if (event.param !== undefined) current().styleIndex = event.param;
        break;
      }
      case "outlinelevel": {
        if (event.param !== undefined) current().outlineLevel = event.param;
        break;
      }
      case "ls": {
        if (event.param !== undefined) current().listId = event.param;
        break;
      }
      case "tab": {
        pushText("\t");
        break;
      }
      case "trowd": {
        if (!inTableRow) {
          inTableRow = true;
          rowCells = [];
          cellBuffer = [];
        }
        break;
      }
      case "cell": {
        rowCells.push({ content: cellBuffer });
        cellBuffer = [];
        break;
      }
      case "row": {
        pendingRows.push({ cells: rowCells });
        rowCells = [];
        cellBuffer = [];
        inTableRow = false;
        break;
      }
      default: {
        break;
      }
    }
  }

  if (pendingRows.length > 0) flushTable();

  // A document consisting only of text with no terminating `\par` is what
  // Word produces when copying a fragment within a single paragraph. Emit it
  // as loose text (like inline HTML) so it merges into the current literal
  // instead of splitting the target paragraph.
  const frame = paragraphFrame ?? current();
  const isPlainOpenParagraph = frame.listId === undefined && headingLevelOf(frame) === undefined;
  if (elements.length === 0 && isPlainOpenParagraph) {
    return paragraphBuffer;
  }

  flushParagraph();
  return elements;
}
