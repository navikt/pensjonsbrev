import { describe, expect, it } from "vitest";

import { buildDiffSegments, type DiffDelete, type DiffInsert } from "~/Brevredigering/LetterEditor/diff/diffModel";
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
      { type: "deleted", text: "søknaden " },
      { type: "inserted", text: "kravet" },
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
      { type: "deleted", text: "mottatt " },
      { type: "inserted", text: "videresendt " },
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
      { type: "deleted", text: "mottatt " },
      { type: "inserted", text: "videresendt " },
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
      { type: "deleted", text: ", " },
      { type: "inserted", text: "; " },
      { type: "unchanged", text: "uføretrygd og barnetillegg." },
    ]);
  });

  it("handles Norwegian characters", () => {
    const currentText = "Søknaden om uføretrygd er ferdigbehandlet.";
    const inserts: DiffInsert[] = [{ index: idx, startOffset: 12, endOffset: 23 }];
    const deletes: DiffDelete[] = [{ index: idx, startOffset: 12, endOffset: 25, text: "alderspensjon" }];
    const segments = expectCurrentTextInvariant(buildDiffSegments({ currentText, inserts, deletes }), currentText);
    expect(segments).toEqual([
      { type: "unchanged", text: "Søknaden om " },
      { type: "deleted", text: "alderspensjon" },
      { type: "inserted", text: "uføretrygd " },
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
        { type: "deleted", text: "søknaden " },
        { type: "inserted", text: "kravet" },
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
