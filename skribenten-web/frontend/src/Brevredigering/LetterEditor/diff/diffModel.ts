export type DiffContentIndex = {
  blockIndex: number;
  contentIndex: number;
};

export type DiffInsert = {
  index: DiffContentIndex;
  startOffset: number;
  endOffset: number;
};

export type DiffDelete = {
  index: DiffContentIndex;
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

export function diffKey(index: DiffContentIndex): string {
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

type BuildDiffSegmentsInput = {
  currentText: string;
  inserts: DiffInsert[];
  deletes: DiffDelete[];
};

export function buildDiffSegments({ currentText, inserts, deletes }: BuildDiffSegmentsInput): DiffSegment[] {
  if (inserts.length === 0 && deletes.length === 0) {
    return [{ type: "unchanged", text: currentText }];
  }

  const sortedInserts = [...inserts].sort((a, b) => a.startOffset - b.startOffset || a.endOffset - b.endOffset);
  const sortedDeletes = [...deletes].sort((a, b) => a.startOffset - b.startOffset || a.endOffset - b.endOffset);

  type Event = { offset: number; kind: "insert" | "delete"; entry: DiffInsert | DiffDelete };
  const events: Event[] = [
    ...sortedInserts.map((e) => ({ offset: e.startOffset, kind: "insert" as const, entry: e })),
    ...sortedDeletes.map((e) => ({ offset: e.startOffset, kind: "delete" as const, entry: e })),
  ];
  events.sort((a, b) => {
    if (a.offset !== b.offset) return a.offset - b.offset;
    if (a.kind === "delete" && b.kind === "insert") return -1;
    if (a.kind === "insert" && b.kind === "delete") return 1;
    return 0;
  });

  const segments: DiffSegment[] = [];
  let cursor = 0;

  for (const event of events) {
    if (event.kind === "delete") {
      const del = event.entry as DiffDelete;
      if (!isValidDeleteRange(del)) continue;

      if (cursor < del.startOffset && cursor < currentText.length) {
        const unchangedEnd = Math.min(del.startOffset, currentText.length);
        segments.push({ type: "unchanged", text: currentText.slice(cursor, unchangedEnd) });
        cursor = unchangedEnd;
      }
      segments.push({ type: "deleted", text: del.text });
    } else {
      const ins = event.entry as DiffInsert;
      if (!isValidInsertRange(ins, currentText.length)) continue;

      if (cursor < ins.startOffset) {
        segments.push({ type: "unchanged", text: currentText.slice(cursor, ins.startOffset) });
        cursor = ins.startOffset;
      }
      segments.push({ type: "inserted", text: currentText.slice(ins.startOffset, ins.endOffset) });
      cursor = ins.endOffset;
    }
  }

  if (cursor < currentText.length) {
    segments.push({ type: "unchanged", text: currentText.slice(cursor) });
  }

  return segments;
}

function isValidInsertRange(ins: DiffInsert, textLength: number): boolean {
  return ins.startOffset >= 0 && ins.endOffset >= ins.startOffset && ins.endOffset <= textLength;
}

function isValidDeleteRange(del: DiffDelete): boolean {
  return del.startOffset >= 0 && del.endOffset >= del.startOffset;
}
