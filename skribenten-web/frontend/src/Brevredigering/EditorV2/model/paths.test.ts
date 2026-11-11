import { describe, expect, test } from "vitest";

import { cell, column, item, list, literal, paragraph, row, stateV2, table } from "~test/support/editorV2TestUtils";

import {
  type ContainerPath,
  caret,
  compareCarets,
  comparePaths,
  containers,
  next,
  parsePathKey,
  pathKey,
  previous,
  resolve,
  resolveText,
  sameCaret,
  sibling,
  TITLE,
} from "./paths";

const p: ContainerPath = { kind: "block", block: 0 };
const i0: ContainerPath = { kind: "item", block: 1, item: 0 };
const i1: ContainerPath = { kind: "item", block: 1, item: 1 };
const h0: ContainerPath = { kind: "header", block: 2, column: 0 };
const c00: ContainerPath = { kind: "cell", block: 2, row: 0, cell: 0 };
const c01: ContainerPath = { kind: "cell", block: 2, row: 0, cell: 1 };
const last: ContainerPath = { kind: "block", block: 3 };

const doc = stateV2(
  paragraph(literal("p")),
  list(item(literal("i0")), item(literal("i1"))),
  table([column(literal("h0"))], [row(cell(literal("c00")), cell(literal("c01")))]),
  paragraph(literal("last")),
).redigertBrev;

const ordered = [p, i0, i1, h0, c00, c01, last];

describe("paths", () => {
  test("containers lists every text container in document order", () => {
    expect(containers(doc)).toEqual(ordered);
  });

  test("resolve finds containers", () => {
    expect(resolveText(doc, caret(TITLE))).toMatchObject({ text: "Tittel" });
    expect(ordered.map((path) => resolveText(doc, caret(path))?.text)).toEqual([
      "p",
      "i0",
      "i1",
      "h0",
      "c00",
      "c01",
      "last",
    ]);
  });

  test("resolve returns undefined for mismatching kinds", () => {
    expect(resolve(doc, { kind: "block", block: 1 })).toBeUndefined();
    expect(resolve(doc, { kind: "item", block: 0, item: 0 })).toBeUndefined();
    expect(resolve(doc, { kind: "cell", block: 2, row: 5, cell: 0 })).toBeUndefined();
  });

  test("comparePaths follows document order", () => {
    const all = [TITLE, ...ordered];
    for (const [index, path] of all.entries()) {
      expect(comparePaths(path, path)).toBe(0);
      if (index > 0) expect(comparePaths(all[index - 1], path)).toBe(-1);
      if (index > 0) expect(comparePaths(path, all[index - 1])).toBe(1);
    }
  });

  test("compareCarets compares content and offset within a path", () => {
    expect(compareCarets(caret(p, 0, 1), caret(p, 0, 2))).toBe(-1);
    expect(compareCarets(caret(p, 1, 0), caret(p, 0, 5))).toBe(1);
    expect(compareCarets(caret(p, 5, 5), caret(i0, 0, 0))).toBe(-1);
    expect(compareCarets(caret(TITLE, 9, 9), caret(p))).toBe(-1);
    expect(sameCaret(caret(c00, 1, 2), caret({ ...c00 }, 1, 2))).toBe(true);
  });

  test("next and previous walk document order", () => {
    expect(next(doc, TITLE)).toEqual(p);
    expect(next(doc, i1)).toEqual(h0);
    expect(next(doc, last)).toBeUndefined();
    expect(previous(doc, h0)).toEqual(i1);
    expect(previous(doc, p)).toBeUndefined();
  });

  test("pathKey and parsePathKey roundtrip", () => {
    for (const path of [TITLE, ...ordered]) {
      expect(parsePathKey(pathKey(path))).toEqual(path);
    }
    expect(parsePathKey("nonsense")).toBeUndefined();
  });

  test("sibling moves within the innermost level", () => {
    expect(sibling(p, 1)).toEqual({ kind: "block", block: 1 });
    expect(sibling(i1, -1)).toEqual(i0);
    expect(sibling(h0, 1)).toEqual({ kind: "header", block: 2, column: 1 });
    expect(sibling(c00, 1)).toEqual(c01);
    expect(sibling(TITLE, 1)).toEqual(TITLE);
  });
});
