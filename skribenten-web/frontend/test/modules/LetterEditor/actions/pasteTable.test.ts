import DOMPurify from "dompurify";
import { describe, expect, it } from "vitest";

import { paste as pasteAction } from "~/Brevredigering/LetterEditor/actions/paste";
import type { LetterEditorState, LiteralIndex } from "~/Brevredigering/LetterEditor/model/state";
import { isLiteral } from "~/Brevredigering/LetterEditor/model/utils";

import type { table } from "../utils";
import { letter, literal, paragraph } from "../utils";

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
// Here we define a simple HTML table with a header and one row.
// Notice that the top tr tag is malformed and the img tag is included to test sanitization.
// After sanitaization, the img tag should be removed, the tr tag should be closed, and the table should be valid.
const htmlTable = `
 <table>
  <tr><th>Header 1</th></tr
  <tr><td>Row 1</td></tr>
  <img src=x onerror=alert(1)>
</table>
`;

describe("paste handler inserts TABLE", () => {
  it("creates a Brevbaker TABLE with header + one row", () => {
    const clipboardData = createClipboardWithHtml(htmlTable);
    const initialState = createEmptyLetterState();
    const focusPosition: LiteralIndex = { blockIndex: 0, contentIndex: 0 };

    const updatedState = pasteAction(initialState, focusPosition, 0, clipboardData);

    const paragraphBlock = updatedState.redigertBrev.blocks[0];
    expect(paragraphBlock.type).toBe("PARAGRAPH");

    expect(paragraphBlock.content[1].type).toBe("TABLE");

    const tableContent = paragraphBlock.content[1] as ReturnType<typeof table>;

    expect(tableContent.header.colSpec.length).toBe(1);
    expect(tableContent.rows.length).toBe(2);

    const headerPlaceholderText = tableContent.header.colSpec[0].headerContent.text[0];
    expect(isLiteral(headerPlaceholderText) && headerPlaceholderText.editedText).toBe("Kolonne 1");

    const headerRowText = tableContent.rows[0].cells[0].text[0];
    expect(isLiteral(headerRowText) && headerRowText.editedText).toBe("Header 1");

    const bodyRowText = tableContent.rows[1].cells[0].text[0];
    expect(isLiteral(bodyRowText) && bodyRowText.editedText).toBe("Row 1");
  });
});
