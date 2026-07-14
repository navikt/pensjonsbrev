import { describe, expect, it } from "vitest";

import { buildDiffSegments, diffKey, type DiffDelete, type DiffInsert } from "~/Brevredigering/LetterEditor/diff/diffModel";
import { brevDiffKeys } from "~/api/brev-queries";

function expectOk(result: ReturnType<typeof buildDiffSegments>) {
  if (!result.ok) throw new Error(`Expected ok but got: ${result.reason}`);
  return result.segments;
}

function expectRejected(result: ReturnType<typeof buildDiffSegments>) {
  if (result.ok) throw new Error("Expected rejection but got ok");
  return result.reason;
}

function expectCurrentTextInvariant(result: ReturnType<typeof buildDiffSegments>, currentText: string) {
  const segments = expectOk(result);
  const reconstructed = segments
    .filter((s) => s.type !== "deleted")
    .map((s) => s.text)
    .join("");
  expect(reconstructed).toBe(currentText);
  return segments;
}

const idx = { blockIndex: 0, contentIndex: 0 };

describe("buildDiffSegments", () => {
  it("returns a single unchanged segment when no changes", () => {
    const currentText = "Vi har mottatt søknaden din om alderspensjon.";
    const segments = expectCurrentTextInvariant(buildDiffSegments({ currentText, inserts: [], deletes: [] }), currentText);
    expect(segments).toEqual([{ type: "unchanged", text: currentText }]);
  });

  it("insertion only", () => {
    const currentText = "Vi har mottatt søknaden din om alderspensjon.";
    const inserts: DiffInsert[] = [{ index: idx, startOffset: 15, endOffset: 24 }];
    const segments = expectCurrentTextInvariant(buildDiffSegments({ currentText, inserts, deletes: [] }), currentText);
    expect(segments).toEqual([
      { type: "unchanged", text: "Vi har mottatt " },
      { type: "inserted", text: "søknaden " },
      { type: "unchanged", text: "din om alderspensjon." },
    ]);
  });

  it("deletion only", () => {
    const currentText = "Vi har mottatt din om alderspensjon.";
    const deletes: DiffDelete[] = [{ index: idx, startOffset: 15, endOffset: 24, text: "søknaden " }];
    const segments = expectCurrentTextInvariant(buildDiffSegments({ currentText, inserts: [], deletes }), currentText);
    expect(segments).toEqual([
      { type: "unchanged", text: "Vi har mottatt " },
      { type: "deleted", text: "søknaden " },
      { type: "unchanged", text: "din om alderspensjon." },
    ]);
  });

  it("replacement at the same position", () => {
    const currentText = "Vi har mottatt kravet ditt om uføretrygd.";
    const inserts: DiffInsert[] = [{ index: idx, startOffset: 15, endOffset: 21 }];
    const deletes: DiffDelete[] = [{ index: idx, startOffset: 15, endOffset: 24, text: "søknaden " }];
    const segments = expectCurrentTextInvariant(buildDiffSegments({ currentText, inserts, deletes }), currentText);
    expect(segments).toEqual([
      { type: "unchanged", text: "Vi har mottatt " },
      { type: "inserted", text: "kravet" },
      { type: "deleted", text: "søknaden " },
      { type: "unchanged", text: " ditt om uføretrygd." },
    ]);
  });

  it("replacement where inserted and deleted lengths differ", () => {
    const currentText = "Vi har videresendt søknaden din til behandling.";
    const inserts: DiffInsert[] = [{ index: idx, startOffset: 7, endOffset: 19 }];
    const deletes: DiffDelete[] = [{ index: idx, startOffset: 7, endOffset: 15, text: "mottatt " }];
    const segments = expectCurrentTextInvariant(buildDiffSegments({ currentText, inserts, deletes }), currentText);
    expect(segments).toEqual([
      { type: "unchanged", text: "Vi har " },
      { type: "inserted", text: "videresendt " },
      { type: "deleted", text: "mottatt " },
      { type: "unchanged", text: "søknaden din til behandling." },
    ]);
  });

  it("insertion at the beginning", () => {
    const currentText = "Vedlegg: Vi har mottatt søknaden din.";
    const inserts: DiffInsert[] = [{ index: idx, startOffset: 0, endOffset: 9 }];
    const segments = expectCurrentTextInvariant(buildDiffSegments({ currentText, inserts, deletes: [] }), currentText);
    expect(segments).toEqual([
      { type: "inserted", text: "Vedlegg: " },
      { type: "unchanged", text: "Vi har mottatt søknaden din." },
    ]);
  });

  it("insertion at the end", () => {
    const currentText = "Vi har mottatt søknaden din. Med vennlig hilsen";
    const inserts: DiffInsert[] = [{ index: idx, startOffset: 28, endOffset: 47 }];
    const segments = expectCurrentTextInvariant(buildDiffSegments({ currentText, inserts, deletes: [] }), currentText);
    expect(segments).toEqual([
      { type: "unchanged", text: "Vi har mottatt søknaden din." },
      { type: "inserted", text: " Med vennlig hilsen" },
    ]);
  });

  it("deletion at the beginning", () => {
    const currentText = "Vi har mottatt søknaden din.";
    const deletes: DiffDelete[] = [{ index: idx, startOffset: 0, endOffset: 9, text: "Vedlegg: " }];
    const segments = expectCurrentTextInvariant(buildDiffSegments({ currentText, inserts: [], deletes }), currentText);
    expect(segments).toEqual([
      { type: "deleted", text: "Vedlegg: " },
      { type: "unchanged", text: "Vi har mottatt søknaden din." },
    ]);
  });

  it("deletion at the end", () => {
    const currentText = "Vi har mottatt søknaden din.";
    const deletes: DiffDelete[] = [{ index: idx, startOffset: 28, endOffset: 48, text: " Med vennlig hilsen" }];
    const segments = expectCurrentTextInvariant(buildDiffSegments({ currentText, inserts: [], deletes }), currentText);
    expect(segments).toEqual([
      { type: "unchanged", text: "Vi har mottatt søknaden din." },
      { type: "deleted", text: " Med vennlig hilsen" },
    ]);
  });

  it("multiple changes in one literal", () => {
    const currentText = "Vi har videresendt søknaden din. Med vennlig hilsen";
    const inserts: DiffInsert[] = [
      { index: idx, startOffset: 7, endOffset: 19 },
      { index: idx, startOffset: 32, endOffset: 51 },
    ];
    const deletes: DiffDelete[] = [{ index: idx, startOffset: 7, endOffset: 15, text: "mottatt " }];
    const segments = expectCurrentTextInvariant(buildDiffSegments({ currentText, inserts, deletes }), currentText);
    expect(segments).toEqual([
      { type: "unchanged", text: "Vi har " },
      { type: "inserted", text: "videresendt " },
      { type: "deleted", text: "mottatt " },
      { type: "unchanged", text: "søknaden din." },
      { type: "inserted", text: " Med vennlig hilsen" },
    ]);
  });

  it("preserves spaces and punctuation", () => {
    const currentText = "Pensjon; uføretrygd og barnetillegg.";
    const inserts: DiffInsert[] = [{ index: idx, startOffset: 7, endOffset: 9 }];
    const deletes: DiffDelete[] = [{ index: idx, startOffset: 7, endOffset: 9, text: ", " }];
    const segments = expectCurrentTextInvariant(buildDiffSegments({ currentText, inserts, deletes }), currentText);
    expect(segments).toEqual([
      { type: "unchanged", text: "Pensjon" },
      { type: "inserted", text: "; " },
      { type: "deleted", text: ", " },
      { type: "unchanged", text: "uføretrygd og barnetillegg." },
    ]);
  });

  it("renders API delete text even when an earlier insert shifts the current text length", () => {
    // Deletes are expressed in original-text coordinates, inserts in current-text coordinates.
    // A large insert before the delete used to push the cursor past the delete offset and drop it.
    const currentText = "Nye opplysninger i saken viser at du har rett. Vi søknaden mottatt.";
    const inserts: DiffInsert[] = [{ index: idx, startOffset: 0, endOffset: 47 }];
    const deletes: DiffDelete[] = [{ index: idx, startOffset: 3, endOffset: 12, text: "gamle " }];
    const segments = expectCurrentTextInvariant(buildDiffSegments({ currentText, inserts, deletes }), currentText);
    expect(segments).toEqual([
      { type: "inserted", text: "Nye opplysninger i saken viser at du har rett. " },
      { type: "unchanged", text: "Vi " },
      { type: "deleted", text: "gamle " },
      { type: "unchanged", text: "søknaden mottatt." },
    ]);
  });

  it("handles Norwegian characters", () => {
    const currentText = "Søknaden om uføretrygd er ferdigbehandlet.";
    const inserts: DiffInsert[] = [{ index: idx, startOffset: 12, endOffset: 23 }];
    const deletes: DiffDelete[] = [{ index: idx, startOffset: 12, endOffset: 25, text: "alderspensjon" }];
    const segments = expectCurrentTextInvariant(buildDiffSegments({ currentText, inserts, deletes }), currentText);
    expect(segments).toEqual([
      { type: "unchanged", text: "Søknaden om " },
      { type: "inserted", text: "uføretrygd " },
      { type: "deleted", text: "alderspensjon" },
      { type: "unchanged", text: "er ferdigbehandlet." },
    ]);
  });

  it("inserted text is not rendered twice", () => {
    const currentText = "Vi har mottatt søknaden din om alderspensjon.";
    const inserts: DiffInsert[] = [{ index: idx, startOffset: 0, endOffset: 7 }];
    const segments = expectCurrentTextInvariant(buildDiffSegments({ currentText, inserts, deletes: [] }), currentText);
    expect(segments).toEqual([
      { type: "inserted", text: "Vi har " },
      { type: "unchanged", text: "mottatt søknaden din om alderspensjon." },
    ]);
  });

  it("does not mutate the original API arrays", () => {
    const inserts: DiffInsert[] = [
      { index: idx, startOffset: 15, endOffset: 24 },
      { index: idx, startOffset: 0, endOffset: 7 },
    ];
    const deletes: DiffDelete[] = [];
    const insertsCopy = [...inserts];
    buildDiffSegments({ currentText: "Vi har mottatt søknaden din om alderspensjon.", inserts, deletes });
    expect(inserts).toEqual(insertsCopy);
  });

  describe("currentText invariant — segments without deleted entries reconstruct currentText", () => {
    it("holds for pure insertions", () => {
      const currentText = "Vi har mottatt søknaden din om alderspensjon.";
      const inserts: DiffInsert[] = [
        { index: idx, startOffset: 0, endOffset: 7 },
        { index: idx, startOffset: 31, endOffset: 45 },
      ];
      expectCurrentTextInvariant(buildDiffSegments({ currentText, inserts, deletes: [] }), currentText);
    });

    it("holds for pure deletions", () => {
      const currentText = "Vedtaket er fattet.";
      const deletes: DiffDelete[] = [
        { index: idx, startOffset: 0, endOffset: 8, text: "Tidligere " },
        { index: idx, startOffset: 12, endOffset: 19, text: " av Nav" },
      ];
      expectCurrentTextInvariant(buildDiffSegments({ currentText, inserts: [], deletes }), currentText);
    });

    it("holds for mixed replacements", () => {
      const currentText = "Vi har videresendt kravet ditt.";
      const inserts: DiffInsert[] = [
        { index: idx, startOffset: 7, endOffset: 19 },
        { index: idx, startOffset: 19, endOffset: 25 },
      ];
      const deletes: DiffDelete[] = [
        { index: idx, startOffset: 7, endOffset: 15, text: "mottatt " },
        { index: idx, startOffset: 19, endOffset: 28, text: "søknaden " }
      ];
      expectCurrentTextInvariant(buildDiffSegments({ currentText, inserts, deletes }), currentText);
    });
  });

  describe("validation — rejects entire literal on invalid ranges", () => {
    it("rejects when insert endOffset exceeds text length", () => {
      const inserts: DiffInsert[] = [{ index: idx, startOffset: 40, endOffset: 50 }];
      const reason = expectRejected(buildDiffSegments({ currentText: "Vi har mottatt søknaden din.", inserts, deletes: [] }));
      expect(reason).toContain("Invalid insert range");
    });

    it("rejects when insert startOffset is negative", () => {
      const inserts: DiffInsert[] = [{ index: idx, startOffset: -1, endOffset: 3 }];
      const reason = expectRejected(buildDiffSegments({ currentText: "Vi har mottatt søknaden din.", inserts, deletes: [] }));
      expect(reason).toContain("Invalid insert range");
    });

    it("rejects when insert endOffset < startOffset", () => {
      const inserts: DiffInsert[] = [{ index: idx, startOffset: 15, endOffset: 7 }];
      const reason = expectRejected(buildDiffSegments({ currentText: "Vi har mottatt søknaden din.", inserts, deletes: [] }));
      expect(reason).toContain("Invalid insert range");
    });

    it("rejects when delete startOffset is negative", () => {
      const deletes: DiffDelete[] = [{ index: idx, startOffset: -1, endOffset: 3, text: "Nav" }];
      const reason = expectRejected(buildDiffSegments({ currentText: "Vi har mottatt søknaden din.", inserts: [], deletes }));
      expect(reason).toContain("Invalid delete range");
    });

    it("rejects when delete endOffset < startOffset", () => {
      const deletes: DiffDelete[] = [{ index: idx, startOffset: 10, endOffset: 5, text: "mottatt" }];
      const reason = expectRejected(buildDiffSegments({ currentText: "Vi har mottatt søknaden din.", inserts: [], deletes }));
      expect(reason).toContain("Invalid delete range");
    });

    it("rejects overlapping insert ranges", () => {
      const inserts: DiffInsert[] = [
        { index: idx, startOffset: 0, endOffset: 10 },
        { index: idx, startOffset: 5, endOffset: 15 },
      ];
      const reason = expectRejected(buildDiffSegments({ currentText: "Vi har mottatt søknaden din.", inserts, deletes: [] }));
      expect(reason).toContain("Overlapping insert ranges");
    });

    it("rejects insert behind consumed cursor", () => {
      const inserts: DiffInsert[] = [
        { index: idx, startOffset: 7, endOffset: 15 },
        { index: idx, startOffset: 10, endOffset: 12 },
      ];
      const reason = expectRejected(buildDiffSegments({ currentText: "Vi har mottatt søknaden din.", inserts, deletes: [] }));
      expect(reason).toContain("Overlapping insert ranges");
    });

    it("valid same-position replacement still works", () => {
      const currentText = "Vi har mottatt kravet ditt.";
      const inserts: DiffInsert[] = [{ index: idx, startOffset: 15, endOffset: 21 }];
      const deletes: DiffDelete[] = [{ index: idx, startOffset: 15, endOffset: 24, text: "søknaden " }];
      const segments = expectCurrentTextInvariant(buildDiffSegments({ currentText, inserts, deletes }), currentText);
      expect(segments).toEqual([
        { type: "unchanged", text: "Vi har mottatt " },
        { type: "inserted", text: "kravet" },
        { type: "deleted", text: "søknaden " },
        { type: "unchanged", text: " ditt." },
      ]);
    });

    it("stale ranges from an older, longer text are rejected", () => {
      const inserts: DiffInsert[] = [{ index: idx, startOffset: 50, endOffset: 60 }];
      const reason = expectRejected(buildDiffSegments({ currentText: "Vi har mottatt søknaden din.", inserts, deletes: [] }));
      expect(reason).toContain("Invalid insert range");
    });
  });
});

describe("brevDiffKeys", () => {
  it("produces different keys for different hashes", () => {
    const keyA = brevDiffKeys.id(123, "hash-a");
    const keyB = brevDiffKeys.id(123, "hash-b");
    expect(keyA).not.toEqual(keyB);
  });

  it("produces different keys for different brevIds", () => {
    const key1 = brevDiffKeys.id(1, "same-hash");
    const key2 = brevDiffKeys.id(2, "same-hash");
    expect(key1).not.toEqual(key2);
  });
});

describe("diffKey", () => {
  it("produces correct key for a block content index", () => {
    expect(diffKey({ blockIndex: 1, contentIndex: 0 })).toBe("1-0");
  });

  it("produces correct key for an item content index", () => {
    expect(diffKey({ blockIndex: 12, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 })).toBe("12-0-item-0-0");
  });

  it("produces correct key for a table cell index", () => {
    expect(diffKey({ blockIndex: 8, contentIndex: 1, rowIndex: 1, cellIndex: 0, cellContentIndex: 0 })).toBe("8-1-table-1-0-0");
  });

  describe("collision resistance", () => {
    it("two different list items in the same block produce different keys", () => {
      const a = diffKey({ blockIndex: 3, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 });
      const b = diffKey({ blockIndex: 3, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 });
      expect(a).not.toBe(b);
    });

    it("two different item contents in the same item produce different keys", () => {
      const a = diffKey({ blockIndex: 3, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 });
      const b = diffKey({ blockIndex: 3, contentIndex: 0, itemIndex: 0, itemContentIndex: 1 });
      expect(a).not.toBe(b);
    });

    it("two rows in the same table produce different keys", () => {
      const a = diffKey({ blockIndex: 5, contentIndex: 1, rowIndex: 0, cellIndex: 0, cellContentIndex: 0 });
      const b = diffKey({ blockIndex: 5, contentIndex: 1, rowIndex: 1, cellIndex: 0, cellContentIndex: 0 });
      expect(a).not.toBe(b);
    });

    it("two cells in the same row produce different keys", () => {
      const a = diffKey({ blockIndex: 5, contentIndex: 1, rowIndex: 0, cellIndex: 0, cellContentIndex: 0 });
      const b = diffKey({ blockIndex: 5, contentIndex: 1, rowIndex: 0, cellIndex: 1, cellContentIndex: 0 });
      expect(a).not.toBe(b);
    });

    it("two contents in the same cell produce different keys", () => {
      const a = diffKey({ blockIndex: 5, contentIndex: 1, rowIndex: 0, cellIndex: 0, cellContentIndex: 0 });
      const b = diffKey({ blockIndex: 5, contentIndex: 1, rowIndex: 0, cellIndex: 0, cellContentIndex: 1 });
      expect(a).not.toBe(b);
    });

    it("block index does not collide with item index of same numbers", () => {
      const block = diffKey({ blockIndex: 0, contentIndex: 0 });
      const item = diffKey({ blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 });
      expect(block).not.toBe(item);
    });

    it("item index does not collide with table index of same numbers", () => {
      const item = diffKey({ blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 });
      const table = diffKey({ blockIndex: 0, contentIndex: 0, rowIndex: 0, cellIndex: 0, cellContentIndex: 0 });
      expect(item).not.toBe(table);
    });
  });

  describe("zero-length ranges", () => {
    it("zero-length insert produces an empty inserted segment", () => {
      const inserts: DiffInsert[] = [{ index: idx, startOffset: 5, endOffset: 5 }];
      const segments = expectOk(buildDiffSegments({ currentText: "Vi har mottatt.", inserts, deletes: [] }));
      expect(segments).toEqual([
        { type: "unchanged", text: "Vi ha" },
        { type: "inserted", text: "" },
        { type: "unchanged", text: "r mottatt." },
      ]);
    });

    it("zero-length delete with text still renders the deleted text", () => {
      const deletes: DiffDelete[] = [{ index: idx, startOffset: 6, endOffset: 6, text: "har " }];
      const segments = expectOk(buildDiffSegments({ currentText: "Vi har mottatt.", inserts: [], deletes }));
      expect(segments).toEqual([
        { type: "unchanged", text: "Vi har" },
        { type: "deleted", text: "har " },
        { type: "unchanged", text: " mottatt." },
      ]);
    });
  });
});
