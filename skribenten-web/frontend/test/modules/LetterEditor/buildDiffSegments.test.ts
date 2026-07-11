import { describe, expect, it } from "vitest";

import { buildDiffSegments, type DiffDelete, type DiffInsert } from "~/Brevredigering/LetterEditor/diff/diffModel";

describe("buildDiffSegments", () => {
  it("returns a single unchanged segment when no changes", () => {
    const result = buildDiffSegments({ currentText: "hello world", inserts: [], deletes: [] });
    expect(result).toEqual([{ type: "unchanged", text: "hello world" }]);
  });

  it("insertion only", () => {
    const inserts: DiffInsert[] = [{ index: { blockIndex: 0, contentIndex: 0 }, startOffset: 5, endOffset: 11 }];
    const result = buildDiffSegments({ currentText: "hello world", inserts, deletes: [] });
    expect(result).toEqual([
      { type: "unchanged", text: "hello" },
      { type: "inserted", text: " world" },
    ]);
  });

  it("deletion only", () => {
    const deletes: DiffDelete[] = [
      { index: { blockIndex: 0, contentIndex: 0 }, startOffset: 5, endOffset: 11, text: " world" },
    ];
    const result = buildDiffSegments({ currentText: "hello", inserts: [], deletes });
    expect(result).toEqual([
      { type: "unchanged", text: "hello" },
      { type: "deleted", text: " world" },
    ]);
  });

  it("replacement at the same position", () => {
    const inserts: DiffInsert[] = [{ index: { blockIndex: 0, contentIndex: 0 }, startOffset: 9, endOffset: 11 }];
    const deletes: DiffDelete[] = [
      { index: { blockIndex: 0, contentIndex: 0 }, startOffset: 9, endOffset: 11, text: "en" },
    ];
    const result = buildDiffSegments({ currentText: "Dette er et brev", inserts, deletes });
    expect(result).toEqual([
      { type: "unchanged", text: "Dette er " },
      { type: "deleted", text: "en" },
      { type: "inserted", text: "et" },
      { type: "unchanged", text: " brev" },
    ]);
  });

  it("replacement where inserted and deleted lengths differ", () => {
    const inserts: DiffInsert[] = [{ index: { blockIndex: 0, contentIndex: 0 }, startOffset: 6, endOffset: 13 }];
    const deletes: DiffDelete[] = [
      { index: { blockIndex: 0, contentIndex: 0 }, startOffset: 6, endOffset: 11, text: "world" },
    ];
    const result = buildDiffSegments({ currentText: "hello goodbye!", inserts, deletes });
    expect(result).toEqual([
      { type: "unchanged", text: "hello " },
      { type: "deleted", text: "world" },
      { type: "inserted", text: "goodbye" },
      { type: "unchanged", text: "!" },
    ]);
  });

  it("insertion at the beginning", () => {
    const inserts: DiffInsert[] = [{ index: { blockIndex: 0, contentIndex: 0 }, startOffset: 0, endOffset: 4 }];
    const result = buildDiffSegments({ currentText: "hey world", inserts, deletes: [] });
    expect(result).toEqual([
      { type: "inserted", text: "hey " },
      { type: "unchanged", text: "world" },
    ]);
  });

  it("insertion at the end", () => {
    const inserts: DiffInsert[] = [{ index: { blockIndex: 0, contentIndex: 0 }, startOffset: 5, endOffset: 11 }];
    const result = buildDiffSegments({ currentText: "hello world", inserts, deletes: [] });
    expect(result).toEqual([
      { type: "unchanged", text: "hello" },
      { type: "inserted", text: " world" },
    ]);
  });

  it("deletion at the beginning", () => {
    const deletes: DiffDelete[] = [
      { index: { blockIndex: 0, contentIndex: 0 }, startOffset: 0, endOffset: 6, text: "Hello " },
    ];
    const result = buildDiffSegments({ currentText: "world", inserts: [], deletes });
    expect(result).toEqual([
      { type: "deleted", text: "Hello " },
      { type: "unchanged", text: "world" },
    ]);
  });

  it("deletion at the end", () => {
    const deletes: DiffDelete[] = [
      { index: { blockIndex: 0, contentIndex: 0 }, startOffset: 5, endOffset: 11, text: " world" },
    ];
    const result = buildDiffSegments({ currentText: "hello", inserts: [], deletes });
    expect(result).toEqual([
      { type: "unchanged", text: "hello" },
      { type: "deleted", text: " world" },
    ]);
  });

  it("multiple changes in one literal", () => {
    const inserts: DiffInsert[] = [
      { index: { blockIndex: 0, contentIndex: 0 }, startOffset: 0, endOffset: 3 },
      { index: { blockIndex: 0, contentIndex: 0 }, startOffset: 8, endOffset: 13 },
    ];
    const deletes: DiffDelete[] = [
      { index: { blockIndex: 0, contentIndex: 0 }, startOffset: 0, endOffset: 2, text: "Hi" },
    ];
    // currentText: "Hey there world"
    const result = buildDiffSegments({ currentText: "Hey there world", inserts, deletes });
    expect(result).toEqual([
      { type: "deleted", text: "Hi" },
      { type: "inserted", text: "Hey" },
      { type: "unchanged", text: " ther" },
      { type: "inserted", text: "e wor" },
      { type: "unchanged", text: "ld" },
    ]);
  });

  it("preserves spaces and punctuation", () => {
    const inserts: DiffInsert[] = [{ index: { blockIndex: 0, contentIndex: 0 }, startOffset: 5, endOffset: 7 }];
    const deletes: DiffDelete[] = [
      { index: { blockIndex: 0, contentIndex: 0 }, startOffset: 5, endOffset: 7, text: ", " },
    ];
    const result = buildDiffSegments({ currentText: "Hello! World", inserts, deletes });
    expect(result).toEqual([
      { type: "unchanged", text: "Hello" },
      { type: "deleted", text: ", " },
      { type: "inserted", text: "! " },
      { type: "unchanged", text: "World" },
    ]);
  });

  it("handles Norwegian characters", () => {
    const inserts: DiffInsert[] = [{ index: { blockIndex: 0, contentIndex: 0 }, startOffset: 4, endOffset: 7 }];
    const deletes: DiffDelete[] = [
      { index: { blockIndex: 0, contentIndex: 0 }, startOffset: 4, endOffset: 7, text: "blå" },
    ];
    const result = buildDiffSegments({ currentText: "Den rød bilen", inserts, deletes });
    expect(result).toEqual([
      { type: "unchanged", text: "Den " },
      { type: "deleted", text: "blå" },
      { type: "inserted", text: "rød" },
      { type: "unchanged", text: " bilen" },
    ]);
  });

  it("invalid insert range is skipped", () => {
    const inserts: DiffInsert[] = [{ index: { blockIndex: 0, contentIndex: 0 }, startOffset: 10, endOffset: 20 }];
    const result = buildDiffSegments({ currentText: "short", inserts, deletes: [] });
    expect(result).toEqual([{ type: "unchanged", text: "short" }]);
  });

  it("inserted text is not rendered twice", () => {
    const inserts: DiffInsert[] = [{ index: { blockIndex: 0, contentIndex: 0 }, startOffset: 0, endOffset: 5 }];
    const result = buildDiffSegments({ currentText: "hello world", inserts, deletes: [] });
    expect(result).toEqual([
      { type: "inserted", text: "hello" },
      { type: "unchanged", text: " world" },
    ]);
    const allText = result.map((s) => s.text).join("");
    expect(allText).toBe("hello world");
  });

  it("does not mutate the original API arrays", () => {
    const inserts: DiffInsert[] = [
      { index: { blockIndex: 0, contentIndex: 0 }, startOffset: 5, endOffset: 8 },
      { index: { blockIndex: 0, contentIndex: 0 }, startOffset: 0, endOffset: 3 },
    ];
    const deletes: DiffDelete[] = [];
    const insertsCopy = [...inserts];
    buildDiffSegments({ currentText: "abcdefgh", inserts, deletes });
    expect(inserts).toEqual(insertsCopy);
  });
});
