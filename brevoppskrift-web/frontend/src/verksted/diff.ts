import { isEqual } from "lodash";

import {
  type FlatBlock,
  flattenBlocks,
  indexFields,
  inlineSequences,
  inlinesToText,
  translationText,
} from "./draftOps";
import { type Block, type Draft } from "./types";

export type WordPart = { type: "same" | "added" | "removed"; text: string };

/** Ordbasert diff (LCS) mellom to tekster. */
export function wordDiff(before: string, after: string): WordPart[] {
  const a = before.split(/(\s+)/).filter((s) => s !== "");
  const b = after.split(/(\s+)/).filter((s) => s !== "");
  const lcs: number[][] = Array.from({ length: a.length + 1 }, () => Array<number>(b.length + 1).fill(0));
  for (let i = a.length - 1; i >= 0; i--) {
    for (let j = b.length - 1; j >= 0; j--) {
      lcs[i][j] = a[i] === b[j] ? lcs[i + 1][j + 1] + 1 : Math.max(lcs[i + 1][j], lcs[i][j + 1]);
    }
  }
  const parts: WordPart[] = [];
  const push = (type: WordPart["type"], text: string) => {
    const last = parts.at(-1);
    if (last?.type === type) {
      last.text += text;
    } else {
      parts.push({ type, text });
    }
  };
  let i = 0;
  let j = 0;
  while (i < a.length && j < b.length) {
    if (a[i] === b[j]) {
      push("same", a[i]);
      i++;
      j++;
    } else if (lcs[i + 1][j] >= lcs[i][j + 1]) {
      push("removed", a[i++]);
    } else {
      push("added", b[j++]);
    }
  }
  while (i < a.length) push("removed", a[i++]);
  while (j < b.length) push("added", b[j++]);
  return parts;
}

export type BlockChange = "added" | "changed" | "moved";

export type DiffItem = {
  key: string;
  area: "Blokk" | "Datafelt" | "Valg" | "Metadata" | "Tittel" | "Vedlegg";
  change: "added" | "removed" | "changed" | "moved";
  label: string;
  blockId?: string;
  words?: { language: string; parts: WordPart[] }[];
};

export type DraftDiff = {
  items: DiffItem[];
  blocks: Map<string, BlockChange>;
};

/** Blokkens eget innhold uten barneblokker (barna diffes som egne blokker). */
function ownContent(block: Block): unknown {
  if (block.type === "CONDITIONAL") {
    return { type: block.type, predicate: block.predicate };
  }
  if (block.type === "REPEAT") {
    return { type: block.type, fieldId: block.fieldId };
  }
  const { id: _id, ...rest } = block;
  return rest;
}

function blockText(block: Block, language: string, fieldName: (id: string) => string): string {
  const seqs = inlineSequences(block).map((s) => inlinesToText(s, language, fieldName));
  const header = block.type === "TABLE" ? block.header.map((h) => translationText(h, language)) : [];
  return [...header, ...seqs].join("\n");
}

function blockLabel(block: Block, language: string, fieldName: (id: string) => string): string {
  const typeLabel: Record<Block["type"], string> = {
    HEADING: "Overskrift",
    PARAGRAPH: "Avsnitt",
    ITEM_LIST: "Liste",
    TABLE: "Tabell",
    CONDITIONAL: "Vilkår",
    REPEAT: "Gjentakelse",
    LOCKED_SOURCE: "Låst kildeblokk",
  };
  const text = blockText(block, language, fieldName).replaceAll("\n", " ").trim();
  return text
    ? `${typeLabel[block.type]}: ${text.length > 60 ? `${text.slice(0, 57)}…` : text}`
    : typeLabel[block.type];
}

/** Diff fra grunnlinjen. Testverdier (scenarier), kommentarer og kildehash er ikke faglige endringer og tas ikke med. */
export function diffDrafts(base: Draft, current: Draft): DraftDiff {
  const items: DiffItem[] = [];
  const blocks = new Map<string, BlockChange>();
  const languages = current.metadata.languages.length > 0 ? current.metadata.languages : base.metadata.languages;
  const primary = languages[0] ?? "NB";
  const nameOf = (draft: Draft) => {
    const index = indexFields(draft.fields);
    return (id: string) => index.get(id)?.path ?? "ukjent felt";
  };
  const baseName = nameOf(base);
  const currentName = nameOf(current);

  // Blokker
  const baseFlat = new Map<string, FlatBlock>(flattenBlocks(base.outline).map((f) => [f.block.id, f]));
  const currentFlat = new Map<string, FlatBlock>(flattenBlocks(current.outline).map((f) => [f.block.id, f]));
  const siblingsOf = (flat: Map<string, FlatBlock>, parentId: string | null, branch: string, shared: Set<string>) =>
    [...flat.values()]
      .filter((f) => f.parentId === parentId && f.branch === branch && shared.has(f.block.id))
      .map((f) => f.block.id);
  const shared = new Set([...currentFlat.keys()].filter((id) => baseFlat.has(id)));

  for (const [id, now] of currentFlat) {
    const before = baseFlat.get(id);
    if (!before) {
      blocks.set(id, "added");
      items.push({
        key: `block:${id}`,
        area: "Blokk",
        change: "added",
        label: blockLabel(now.block, primary, currentName),
        blockId: id,
      });
      continue;
    }
    const baseOrder = siblingsOf(baseFlat, before.parentId, before.branch, shared);
    const currentOrder = siblingsOf(currentFlat, now.parentId, now.branch, shared);
    const moved =
      before.parentId !== now.parentId ||
      before.branch !== now.branch ||
      baseOrder.indexOf(id) !== currentOrder.indexOf(id);
    const baseSame = isEqual(ownContent(before.block), ownContent(now.block));
    const words = languages
      .map((language) => ({
        language,
        before: blockText(before.block, language, baseName),
        after: blockText(now.block, language, currentName),
      }))
      .filter((w) => w.before !== w.after)
      .map((w) => ({ language: w.language, parts: wordDiff(w.before, w.after) }));
    if (!baseSame) {
      blocks.set(id, "changed");
      items.push({
        key: `block:${id}`,
        area: "Blokk",
        change: "changed",
        label: blockLabel(now.block, primary, currentName),
        blockId: id,
        words,
      });
    } else if (moved) {
      blocks.set(id, "moved");
      items.push({
        key: `block:${id}`,
        area: "Blokk",
        change: "moved",
        label: blockLabel(now.block, primary, currentName),
        blockId: id,
      });
    }
  }
  for (const [id, before] of baseFlat) {
    if (!currentFlat.has(id)) {
      items.push({
        key: `block:${id}`,
        area: "Blokk",
        change: "removed",
        label: blockLabel(before.block, primary, baseName),
      });
    }
  }

  // Tittel
  const titleWords = languages
    .filter((l) => (base.title[l] ?? "") !== (current.title[l] ?? ""))
    .map((language) => ({ language, parts: wordDiff(base.title[language] ?? "", current.title[language] ?? "") }));
  if (titleWords.length > 0) {
    items.push({ key: "title", area: "Tittel", change: "changed", label: "Tittel", words: titleWords });
  }

  // Felt
  const baseFields = indexFields(base.fields);
  const currentFields = indexFields(current.fields);
  for (const [id, now] of currentFields) {
    const before = baseFields.get(id);
    if (!before) {
      items.push({ key: `field:${id}`, area: "Datafelt", change: "added", label: now.path });
    } else if (
      !isEqual({ ...before.field, children: [] }, { ...now.field, children: [] }) ||
      before.path !== now.path
    ) {
      items.push({
        key: `field:${id}`,
        area: "Datafelt",
        change: "changed",
        label: before.path === now.path ? now.path : `${before.path} → ${now.path}`,
      });
    }
  }
  for (const [id, before] of baseFields) {
    if (!currentFields.has(id)) {
      items.push({ key: `field:${id}`, area: "Datafelt", change: "removed", label: before.path });
    }
  }

  // Valg
  const baseChoices = new Map(base.choices.map((c) => [c.id, c]));
  for (const choice of current.choices) {
    const before = baseChoices.get(choice.id);
    if (!before) {
      items.push({ key: `choice:${choice.id}`, area: "Valg", change: "added", label: choice.name });
    } else if (!isEqual(before, choice)) {
      items.push({ key: `choice:${choice.id}`, area: "Valg", change: "changed", label: choice.name });
    }
  }
  for (const choice of base.choices) {
    if (!current.choices.some((c) => c.id === choice.id)) {
      items.push({ key: `choice:${choice.id}`, area: "Valg", change: "removed", label: choice.name });
    }
  }

  // Metadata
  if (!isEqual(base.metadata, current.metadata)) {
    const keys = new Set([...Object.keys(base.metadata), ...Object.keys(current.metadata)]);
    for (const key of keys) {
      const a = (base.metadata as Record<string, unknown>)[key];
      const b = (current.metadata as Record<string, unknown>)[key];
      if (!isEqual(a, b)) {
        items.push({
          key: `metadata:${key}`,
          area: "Metadata",
          change: "changed",
          label: `${key}: ${JSON.stringify(a)} → ${JSON.stringify(b)}`,
        });
      }
    }
  }

  // Vedlegg
  const baseAttachments = new Map((base.attachments ?? []).map((a) => [a.id, a]));
  for (const attachment of current.attachments ?? []) {
    const before = baseAttachments.get(attachment.id);
    const label = `${attachment.draft.metadata.name} (revisjon ${attachment.revision})`;
    if (!before) {
      items.push({ key: `attachment:${attachment.id}`, area: "Vedlegg", change: "added", label });
    } else if (before.revision !== attachment.revision || before.fieldId !== attachment.fieldId) {
      items.push({ key: `attachment:${attachment.id}`, area: "Vedlegg", change: "changed", label });
    }
  }
  for (const attachment of base.attachments ?? []) {
    if (!(current.attachments ?? []).some((a) => a.id === attachment.id)) {
      items.push({
        key: `attachment:${attachment.id}`,
        area: "Vedlegg",
        change: "removed",
        label: attachment.draft.metadata.name,
      });
    }
  }

  return { items, blocks };
}
