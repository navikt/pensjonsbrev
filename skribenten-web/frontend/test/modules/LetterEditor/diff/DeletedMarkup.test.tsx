import { render, screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";

import { DeletedCells } from "~/Brevredigering/LetterEditor/diff/DeletedMarkup";
import { type Cell } from "~/types/brevbakerTypes";

const deletedCell = {
  id: 10,
  parentId: 9,
  deletedContent: [],
  text: [
    {
      type: "LITERAL",
      id: 11,
      parentId: 10,
      text: "Slettet kolonne",
      editedText: null,
      fontType: "PLAIN",
      editedFontType: null,
      tags: [],
    },
  ],
} as Cell;

describe("DeletedMarkup", () => {
  it("renders deleted header cells with table-header semantics", () => {
    render(
      <table>
        <thead>
          <tr>
            <DeletedCells asHeader cells={[deletedCell]} />
          </tr>
        </thead>
      </table>,
    );

    expect(
      screen.getByRole("columnheader", { name: "Slettet kolonne" }).getAttribute("data-diff-deleted"),
    ).not.toBeNull();
  });
});
