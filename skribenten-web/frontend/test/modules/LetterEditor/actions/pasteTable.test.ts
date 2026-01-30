import DOMPurify from "dompurify";
import { describe, expect, it } from "vitest";

import { paste } from "~/Brevredigering/LetterEditor/actions/paste";
import type { LetterEditorState, LiteralIndex } from "~/Brevredigering/LetterEditor/model/state";
import { isLiteral } from "~/Brevredigering/LetterEditor/model/utils";
import type { Table } from "~/types/brevbakerTypes";
import { letter, literal, paragraph, select } from "../utils";

function createClipboardWithHtml(htmlContent: string): DataTransfer {
  const sanitizedHtml = DOMPurify.sanitize(htmlContent, {
    ALLOWED_TAGS: [
      "p",
      "br",
      "strong",
      "b",
      "em",
      "i",
      "ul",
      "ol",
      "li",
      "table",
      "thead",
      "tbody",
      "tr",
      "td",
      "th",
      "span",
    ],
    ALLOWED_ATTR: ["rowspan", "colspan"],
  });
  return {
    types: ["text/html"],
    getData: (type: string) => (type === "text/html" ? sanitizedHtml : ""),
  } as unknown as DataTransfer;
}

function createEmptyLetterState(): LetterEditorState {
  const paragraphBlock = paragraph([literal({ text: "" })]);
  const letterState = letter(paragraphBlock);
  letterState.focus = { blockIndex: 0, contentIndex: 0, cursorPosition: 0 };
  return letterState;
}

// Malformed table: unclosed tr tag and img (should be removed by sanitizer)
const HTML_MALFORMED_WITH_IMG = `
<table>
  <tr><th>Header 1</th></tr
  <tr><td>Row 1</td></tr>
  <img src=x onerror=alert(1)>
</table>
`;

// Table with explicit THEAD/TBODY sections
const HTML_EXPLICIT_THEAD_TBODY = `
<table>
  <thead>
    <tr>
      <th>H</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>A</td>
    </tr>
  </tbody>
</table>
`;

// Table with no th elements; first row should be promoted to header
const HTML_NO_TH_PROMOTE_FIRST_ROW = `
<table>
  <tr>
    <td>A</td>
    <td>B</td>
  </tr>
  <tr>
    <td>C</td>
  </tr>
</table>
`;

// Body row wider than first: expand to widest column count and pad missing header cells
const HTML_BODY_ROW_WITH_EXTRA_CELLS = `
<table>
  <tr>
    <td>H1</td>
    <td>H2</td>
  </tr>
  <tr>
    <td>A</td>
    <td>B</td>
    <td>C</td>
    <td>D</td>
  </tr>
</table>
`;

describe("paste handler inserts TABLE", () => {
  it("creates a Brevbaker TABLE with header and one body row", () => {
    const clipboardData = createClipboardWithHtml(HTML_MALFORMED_WITH_IMG);
    const initialState = createEmptyLetterState();
    const focusPosition: LiteralIndex = { blockIndex: 0, contentIndex: 0 };

    const updatedState = paste(initialState, focusPosition, 0, clipboardData);

    const paragraphBlock = updatedState.redigertBrev.blocks[0];
    expect(paragraphBlock.type).toBe("PARAGRAPH");

    expect(paragraphBlock.content[updatedState.focus.contentIndex].type).toBe("TABLE");

    const tableContent = select<Table>(updatedState, {
      blockIndex: updatedState.focus.blockIndex,
      contentIndex: updatedState.focus.contentIndex,
    });

    expect(tableContent.header.colSpec.length).toBe(1);
    expect(tableContent.rows.length).toBe(1);

    const headerTextLiteral = tableContent.header.colSpec[0].headerContent.text[0];
    expect(isLiteral(headerTextLiteral) && headerTextLiteral.editedText).toBe("Header 1");

    const bodyRowTextLiteral = tableContent.rows[0].cells[0].text[0];
    expect(isLiteral(bodyRowTextLiteral) && bodyRowTextLiteral.editedText).toBe("Row 1");

    const imgTag = tableContent.rows[0].cells[0].text[0];
    expect(isLiteral(imgTag) && imgTag.editedText).not.toContain("<img");
  });

  it("parses <thead> and uses its <th> cells as header", () => {
    const initialState = createEmptyLetterState();
    const updatedState = paste(
      initialState,
      { blockIndex: 0, contentIndex: 0 },
      0,
      createClipboardWithHtml(HTML_EXPLICIT_THEAD_TBODY),
    );
    const tableNode = select<Table>(updatedState, {
      blockIndex: updatedState.focus.blockIndex,
      contentIndex: updatedState.focus.contentIndex,
    });
    expect(tableNode.header.colSpec.length).toBe(1);
    expect(tableNode.rows.length).toBe(1);
    const headerLiteral = tableNode.header.colSpec[0].headerContent.text[0];
    expect(isLiteral(headerLiteral) && headerLiteral.editedText).toBe("H");
    const bodyLiteral = tableNode.rows[0].cells[0].text[0];
    expect(isLiteral(bodyLiteral) && bodyLiteral.editedText).toBe("A");
  });

  it("promotes first body row to header when table has no <th> elements", () => {
    const initialState = createEmptyLetterState();
    const updatedState = paste(
      initialState,
      { blockIndex: 0, contentIndex: 0 },
      0,
      createClipboardWithHtml(HTML_NO_TH_PROMOTE_FIRST_ROW),
    );
    const tbl = select<Table>(updatedState, {
      blockIndex: updatedState.focus.blockIndex,
      contentIndex: updatedState.focus.contentIndex,
    });
    expect(tbl.header.colSpec.length).toBe(2);
    expect(tbl.rows.length).toBe(1);
    expect(tbl.rows[0].cells.length).toBe(2);
    expect(isLiteral(tbl.rows[0].cells[1].text[0]) && tbl.rows[0].cells[1].text[0].editedText).toBe("");
  });

  it("expands to widest body row and pads missing header cells", () => {
    const initialState = createEmptyLetterState();
    const updatedState = paste(
      initialState,
      { blockIndex: 0, contentIndex: 0 },
      0,
      createClipboardWithHtml(HTML_BODY_ROW_WITH_EXTRA_CELLS),
    );
    const tbl = select<Table>(updatedState, {
      blockIndex: updatedState.focus.blockIndex,
      contentIndex: updatedState.focus.contentIndex,
    });
    expect(tbl.header.colSpec.length).toBe(4);
    expect(tbl.rows[0].cells.length).toBe(4);

    expect(
      isLiteral(tbl.header.colSpec[2].headerContent.text[0]) &&
        tbl.header.colSpec[2].headerContent.text[0].editedText === "" &&
        isLiteral(tbl.header.colSpec[3].headerContent.text[0]) &&
        tbl.header.colSpec[3].headerContent.text[0].editedText === "",
    ).toBe(true);
  });
});
