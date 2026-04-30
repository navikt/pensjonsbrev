import { describe, expect, it } from "vitest";

import { isTable, normalizeTableSeparators } from "~/Brevredigering/LetterEditor/actions/common";
import { isLiteral } from "~/Brevredigering/LetterEditor/model/utils";

import { cell, letter, literal, paragraph, row, table } from "../utils";

function makeTable() {
  return table(
    [cell(literal({ text: "h1" })), cell(literal({ text: "h2" }))],
    [row(cell(literal({ text: "" })), cell(literal({ text: "" })))],
  );
}

describe("normalizeTableSeparators", () => {
  it("inserts an empty literal between two adjacent tables in the same block", () => {
    // Add a trailing literal so the trailing-literal rule does not also fire.
    const state = letter(paragraph([makeTable(), makeTable(), literal({ text: "end" })]));

    const normalized = normalizeTableSeparators(state.redigertBrev);

    const block = normalized.blocks[0];
    expect(block.content).toHaveLength(4);
    expect(isTable(block.content[0])).toBe(true);
    expect(isLiteral(block.content[1])).toBe(true);
    if (!isLiteral(block.content[1])) throw new Error("Expected separator literal");
    expect(block.content[1].editedText).toBe("");
    expect(isTable(block.content[2])).toBe(true);
    expect(isLiteral(block.content[3])).toBe(true);
  });

  it("inserts separators between three adjacent tables", () => {
    const state = letter(paragraph([makeTable(), makeTable(), makeTable(), literal({ text: "end" })]));

    const normalized = normalizeTableSeparators(state.redigertBrev);

    const block = normalized.blocks[0];
    expect(block.content.map((c) => c.type)).toEqual(["TABLE", "LITERAL", "TABLE", "LITERAL", "TABLE", "LITERAL"]);
  });

  it("appends a trailing literal when the last block ends with a table", () => {
    const state = letter(paragraph([literal({ text: "intro" }), makeTable()]));

    const normalized = normalizeTableSeparators(state.redigertBrev);

    const lastBlock = normalized.blocks.at(-1)!;
    expect(lastBlock.content).toHaveLength(3);
    expect(isTable(lastBlock.content[1])).toBe(true);
    const trailing = lastBlock.content[2];
    expect(isLiteral(trailing)).toBe(true);
    if (!isLiteral(trailing)) throw new Error("Expected trailing literal");
    expect(trailing.editedText).toBe("");
  });

  it("does not append a trailing literal when the last block does not end with a table", () => {
    const state = letter(paragraph([makeTable(), literal({ text: "after" })]));

    const normalized = normalizeTableSeparators(state.redigertBrev);

    expect(normalized.blocks[0].content).toHaveLength(2);
  });

  it("does not append a trailing literal when a non-last block ends with a table", () => {
    const state = letter(paragraph([makeTable()]), paragraph([literal({ text: "next" })]));

    const normalized = normalizeTableSeparators(state.redigertBrev);

    expect(normalized.blocks[0].content).toHaveLength(1);
    expect(normalized.blocks[1].content).toHaveLength(1);
  });

  it("does not modify a letter that already has separators", () => {
    const state = letter(paragraph([makeTable(), literal({ text: "" }), makeTable(), literal({ text: "" })]));

    const normalized = normalizeTableSeparators(state.redigertBrev);

    expect(normalized.blocks[0].content).toHaveLength(4);
    expect(normalized).toBe(state.redigertBrev);
  });

  it("handles a combination of adjacent tables and trailing table", () => {
    const state = letter(paragraph([makeTable(), makeTable()]));

    const normalized = normalizeTableSeparators(state.redigertBrev);

    const block = normalized.blocks[0];
    expect(block.content.map((c) => c.type)).toEqual(["TABLE", "LITERAL", "TABLE", "LITERAL"]);
  });
});
