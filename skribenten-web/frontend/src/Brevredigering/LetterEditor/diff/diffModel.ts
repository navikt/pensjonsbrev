import { type LiteralIndex } from "~/Brevredigering/LetterEditor/model/state";

export type DiffInsert = {
  index: LiteralIndex;
  startOffset: number;
  endOffset: number;
};

export type DiffDelete = {
  index: LiteralIndex;
  startOffset: number;
  endOffset: number;
  text: string;
};

export type LetterDiff = {
  inserts: DiffInsert[];
  deletes: DiffDelete[];
};

export type DiffSegment =
  | { type: "unchanged"; text: string }
  | { type: "inserted"; text: string }
  | { type: "deleted"; text: string };

export type DiffRangesByLiteral = Map<
  string,
  {
    inserts: DiffInsert[];
    deletes: DiffDelete[];
  }
>;

export function diffKey(index: LiteralIndex): string {
  if ("itemIndex" in index) {
    return [index.blockIndex, index.contentIndex, "item", index.itemIndex, index.itemContentIndex].join("-");
  }
  if ("rowIndex" in index) {
    return [index.blockIndex, index.contentIndex, "table", index.rowIndex, index.cellIndex, index.cellContentIndex].join("-");
  }
  return `${index.blockIndex}-${index.contentIndex}`;
}

export function groupDiffByLiteral(diff: LetterDiff): DiffRangesByLiteral {
  const map: DiffRangesByLiteral = new Map();

  for (const insert of diff.inserts) {
    const key = diffKey(insert.index);
    if (!map.has(key)) map.set(key, { inserts: [], deletes: [] });
    map.get(key)!.inserts.push(insert);
  }

  for (const del of diff.deletes) {
    const key = diffKey(del.index);
    if (!map.has(key)) map.set(key, { inserts: [], deletes: [] });
    map.get(key)!.deletes.push(del);
  }

  return map;
}

export type BuildDiffSegmentsResult =
  | { ok: true; segments: DiffSegment[] }
  | { ok: false; reason: string };

type BuildDiffSegmentsInput = {
  currentText: string;
  inserts: DiffInsert[];
  deletes: DiffDelete[];
};

export function buildDiffSegments({ currentText, inserts, deletes }: BuildDiffSegmentsInput): BuildDiffSegmentsResult {
  if (inserts.length === 0 && deletes.length === 0) {
    return { ok: true, segments: [{ type: "unchanged", text: currentText }] };
  }

  const sortedInserts = [...inserts].sort((a, b) => a.startOffset - b.startOffset || a.endOffset - b.endOffset);
  const sortedDeletes = [...deletes].sort((a, b) => a.startOffset - b.startOffset || a.endOffset - b.endOffset);

  for (const ins of sortedInserts) {
    if (ins.startOffset < 0 || ins.endOffset < ins.startOffset || ins.endOffset > currentText.length) {
      return { ok: false, reason: `Invalid insert range [${ins.startOffset}, ${ins.endOffset}] for text length ${currentText.length}` };
    }
  }
  for (const del of sortedDeletes) {
    if (del.startOffset < 0 || del.endOffset < del.startOffset) {
      return { ok: false, reason: `Invalid delete range [${del.startOffset}, ${del.endOffset}]` };
    }
  }

  for (let i = 1; i < sortedInserts.length; i++) {
    if (sortedInserts[i].startOffset < sortedInserts[i - 1].endOffset) {
      return { ok: false, reason: `Overlapping insert ranges at index ${i}` };
    }
  }

  // Inserts are expressed in current-text (new) coordinates, deletes in original-text (old) coordinates.
  // Map both into the shared "unchanged" coordinate space so they can be interleaved without a single
  // cursor drifting out of sync when inserts and deletes change the text length differently.
  type MergedEvent =
    | { kind: "insert"; commonPos: number; order: number; entry: DiffInsert }
    | { kind: "delete"; commonPos: number; order: number; entry: DiffDelete };

  let insertedBefore = 0;
  const insertEvents: MergedEvent[] = sortedInserts.map((ins) => {
    const event: MergedEvent = { kind: "insert", commonPos: ins.startOffset - insertedBefore, order: ins.startOffset, entry: ins };
    insertedBefore += ins.endOffset - ins.startOffset;
    return event;
  });

  let deletedBefore = 0;
  const deleteEvents: MergedEvent[] = sortedDeletes.map((del) => {
    const event: MergedEvent = { kind: "delete", commonPos: del.startOffset - deletedBefore, order: del.startOffset, entry: del };
    deletedBefore += del.endOffset - del.startOffset;
    return event;
  });

  const merged = [...deleteEvents, ...insertEvents].sort((a, b) => {
    if (a.commonPos !== b.commonPos) return a.commonPos - b.commonPos;
    if (a.kind !== b.kind) return a.kind === "insert" ? -1 : 1;
    return a.order - b.order;
  });

  const segments: DiffSegment[] = [];
  let newCursor = 0;
  let commonCursor = 0;

  for (const event of merged) {
    const unchangedLen = event.commonPos - commonCursor;
    if (unchangedLen > 0) {
      const end = Math.min(newCursor + unchangedLen, currentText.length);
      if (end > newCursor) {
        segments.push({ type: "unchanged", text: currentText.slice(newCursor, end) });
      }
      newCursor = end;
      commonCursor = event.commonPos;
    }

    if (event.kind === "delete") {
      segments.push({ type: "deleted", text: event.entry.text });
    } else {
      const ins = event.entry;
      segments.push({ type: "inserted", text: currentText.slice(ins.startOffset, ins.endOffset) });
      newCursor = ins.endOffset;
    }
  }

  if (newCursor < currentText.length) {
    segments.push({ type: "unchanged", text: currentText.slice(newCursor) });
  }

  return { ok: true, segments };
}
