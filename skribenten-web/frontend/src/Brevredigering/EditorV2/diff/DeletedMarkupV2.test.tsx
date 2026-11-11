import { render, screen } from "@testing-library/react";
import { type ReactNode } from "react";
import { describe, expect, it } from "vitest";

import { type UnifiedDiffV2 } from "~/types/brevV2";
import {
  cell,
  column,
  item,
  literal,
  newLine,
  numberedList,
  paragraph,
  row,
  table,
  title2,
} from "~test/support/editorV2TestUtils";

import { AttestantDiffProviderV2 } from "./AttestantDiffContextV2";
import {
  DeletedBlocksAtV2,
  DeletedCellsAtV2,
  DeletedColumnsAtV2,
  DeletedContentAtV2,
  DeletedItemsAtV2,
  DeletedRowsAtV2,
} from "./DeletedMarkupV2";

const diff: UnifiedDiffV2 = {
  type: "UNIFIED",
  deletedBlocks: {
    0: [paragraph(literal("Slettet avsnitt"))],
    2: [title2(literal("Slettet tittel")), numberedList(item(literal("Slettet nummerert punkt")))],
    7: [table([column(literal("Slettet tabellkolonne"))], [row(cell(literal("Slettet tabellcelle")))])],
  },
  editedBlocks: {
    1: {
      type: "TEXT",
      textEdits: {},
      deletedContent: { 0: [literal({ text: "fet", fontType: "BOLD" }), newLine()] },
    },
    3: { type: "LIST", itemEdits: {}, deletedItems: { 1: [item(literal("Slettet punkt"))] } },
    4: {
      type: "TABLE",
      columnSpecEdits: {},
      deletedColumnSpecs: { 0: [column(literal("Slettet kolonne"))] },
      deletedRows: { 2: [row(cell(literal("Slettet rad")))] },
      rowEdits: { 0: { cellEdits: {}, deletedCells: { 1: [cell(literal("Slettet celle"))] } } },
    },
  },
};

const renderWithDiff = (children: ReactNode, activeDiff: UnifiedDiffV2 | null = diff) =>
  render(
    <AttestantDiffProviderV2 diff={activeDiff ?? undefined} diffHash="hash" disableDiffMode={() => {}}>
      {children}
    </AttestantDiffProviderV2>,
  );

describe("DeletedMarkupV2", () => {
  it("renders deleted blocks before the surviving block at their position", () => {
    const { container } = renderWithDiff(<DeletedBlocksAtV2 block={0} />);
    const deleted = container.querySelector("[data-diff-deleted-block]");
    expect(deleted?.className).toBe("PARAGRAPH");
    expect(deleted?.getAttribute("contenteditable")).toBe("false");
    expect(deleted?.textContent).toBe("Slettet avsnitt");
  });

  it("renders every kind of deleted block", () => {
    const { container } = renderWithDiff(<DeletedBlocksAtV2 block={2} trailing />);
    expect(screen.getByText("Slettet tittel").closest("div")?.className).toBe("TITLE2");
    expect(screen.getByRole("listitem").closest("ol")).not.toBeNull();
    expect(screen.getByRole("columnheader", { name: "Slettet tabellkolonne" })).toBeDefined();
    expect(screen.getByRole("cell", { name: "Slettet tabellcelle" })).toBeDefined();
    expect(container.querySelectorAll("[data-diff-deleted-block]")).toHaveLength(3);
  });

  it("renders deleted text content and line breaks", () => {
    const { container } = renderWithDiff(<DeletedContentAtV2 content={0} path={{ kind: "block", block: 1 }} />);
    expect(screen.getByText("fet").hasAttribute("data-diff-deleted")).toBe(true);
    expect(container.querySelector("br")).not.toBeNull();
  });

  it("renders deleted items, columns, rows and cells", () => {
    renderWithDiff(
      <>
        <ul>
          <DeletedItemsAtV2 block={3} item={1} />
        </ul>
        <table>
          <thead>
            <tr>
              <DeletedColumnsAtV2 block={4} column={0} />
            </tr>
          </thead>
          <tbody>
            <DeletedRowsAtV2 block={4} row={2} />
            <tr>
              <DeletedCellsAtV2 block={4} cell={1} row={0} />
            </tr>
          </tbody>
        </table>
      </>,
    );
    expect(screen.getByRole("listitem").textContent).toBe("Slettet punkt");
    expect(
      screen.getByRole("columnheader", { name: "Slettet kolonne" }).getAttribute("data-diff-deleted"),
    ).not.toBeNull();
    expect(screen.getByRole("cell", { name: "Slettet rad" }).closest("tr")?.hasAttribute("data-diff-deleted")).toBe(
      true,
    );
    expect(screen.getByRole("cell", { name: "Slettet celle" }).hasAttribute("data-diff-deleted")).toBe(true);
  });

  it("renders nothing when the diff is off", () => {
    const { container } = renderWithDiff(
      <>
        <DeletedBlocksAtV2 block={0} trailing />
        <DeletedContentAtV2 content={0} path={{ kind: "block", block: 1 }} trailing />
        <DeletedItemsAtV2 block={3} item={0} trailing />
      </>,
      null,
    );
    expect(container.innerHTML).toBe("");
  });
});
