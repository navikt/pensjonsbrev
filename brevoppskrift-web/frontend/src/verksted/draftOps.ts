import { newId } from "./ids";
import {
  type Block,
  type BlockType,
  type Choice,
  type Draft,
  type DraftMetadata,
  type Field,
  type FieldKind,
  type Inline,
  type Language,
  type Predicate,
  type Translations,
} from "./types";

// ---------- Oversettelser ----------

export function emptyTranslations(languages: readonly string[]): Translations {
  return Object.fromEntries(languages.map((language) => [language, ""]));
}

export function newTextInline(languages: readonly string[]): Inline {
  return { type: "TEXT", text: emptyTranslations(languages), fontType: "PLAIN" };
}

/** Tekst for ett språk, tom streng hvis språket mangler. */
export function translationText(translations: Translations, language: string): string {
  return (translations as Record<string, string | undefined>)[language] ?? "";
}

/** Setter tekst for ett språk og bevarer alle andre språk uendret. */
export function withTranslation(translations: Translations, language: string, value: string): Translations {
  return { ...translations, [language]: value };
}

// ---------- Nye noder ----------

/** Blokktyper brukeren kan lage. LOCKED_SOURCE kommer bare fra kildeimport og kan ikke opprettes. */
export type CreatableBlockType = Exclude<BlockType, "LOCKED_SOURCE">;

export function newBlock(type: CreatableBlockType, languages: readonly string[], fieldId = ""): Block {
  const id = newId();
  switch (type) {
    case "HEADING": {
      return { type, id, level: 2, text: [newTextInline(languages)] };
    }
    case "PARAGRAPH": {
      return { type, id, text: [newTextInline(languages)] };
    }
    case "ITEM_LIST": {
      return { type, id, numbered: false, items: [[newTextInline(languages)]] };
    }
    case "TABLE": {
      return {
        type,
        id,
        header: [emptyTranslations(languages), emptyTranslations(languages)],
        rows: [[[newTextInline(languages)], [newTextInline(languages)]]],
      };
    }
    case "CONDITIONAL": {
      return { type, id, predicate: { type: "IS_NOT_NULL", fieldId: "" }, ifTrue: [], ifFalse: [] };
    }
    case "REPEAT": {
      return { type, id, fieldId, body: [] };
    }
  }
}

export function newInline(type: Inline["type"], languages: readonly string[]): Inline {
  switch (type) {
    case "TEXT": {
      return newTextInline(languages);
    }
    case "FIELD": {
      return { type, fieldId: "", formatter: "DEFAULT" };
    }
    case "NEW_LINE": {
      return { type };
    }
    case "CONDITIONAL": {
      return { type, predicate: { type: "IS_NOT_NULL", fieldId: "" }, ifTrue: [], ifFalse: [] };
    }
    case "FREE_TEXT": {
      return { type, prompt: emptyTranslations(languages) };
    }
  }
}

export function newField(existing: Field[], kind: FieldKind = "STRING"): Field {
  const names = new Set(allFields(existing).map((f) => f.name));
  let n = names.size + 1;
  while (names.has(`felt${n}`)) {
    n += 1;
  }
  return { id: newId(), name: `felt${n}`, kind, nullable: false, children: [], enumValues: [] };
}

export function newChoice(existing: Choice[], languages: readonly string[]): Choice {
  const names = new Set(existing.map((c) => c.name));
  let n = existing.length + 1;
  while (names.has(`valg${n}`)) {
    n += 1;
  }
  return { id: newId(), name: `valg${n}`, prompt: emptyTranslations(languages) };
}

export function newDraft(metadata: DraftMetadata): Draft {
  return {
    formatVersion: 1,
    metadata,
    title: emptyTranslations(metadata.languages),
    fields: [],
    choices: [],
    outline: [newBlock("PARAGRAPH", metadata.languages)],
    scenarios: [],
    comments: [],
    attachments: [],
  };
}

// ---------- Liste-operasjoner (flytting, kopiering, deling) ----------

export function moveItem<T>(list: readonly T[], index: number, delta: -1 | 1): T[] {
  const target = index + delta;
  if (index < 0 || index >= list.length || target < 0 || target >= list.length) {
    return [...list];
  }
  const copy = [...list];
  const [item] = copy.splice(index, 1);
  copy.splice(target, 0, item);
  return copy;
}

export function removeAt<T>(list: readonly T[], index: number): T[] {
  return list.filter((_, i) => i !== index);
}

export function insertAt<T>(list: readonly T[], index: number, item: T): T[] {
  const copy = [...list];
  copy.splice(index, 0, item);
  return copy;
}

export function replaceAt<T>(list: readonly T[], index: number, item: T): T[] {
  return list.map((existing, i) => (i === index ? item : existing));
}

export function duplicateInline(inlines: readonly Inline[], index: number): Inline[] {
  const original = inlines[index];
  return original ? insertAt(inlines, index + 1, structuredClone(original)) : [...inlines];
}

/** Sant når blokken eller noe inni den er en låst kildeblokk. */
export function containsLockedSource(block: Block): boolean {
  switch (block.type) {
    case "LOCKED_SOURCE": {
      return true;
    }
    case "CONDITIONAL": {
      return [...block.ifTrue, ...block.ifFalse].some(containsLockedSource);
    }
    case "REPEAT": {
      return block.body.some(containsLockedSource);
    }
    default: {
      return false;
    }
  }
}

/** Dyp kopi av en blokk der alle blokk-ID-er (også nøstede) er nye. Låste kildeblokker kan aldri kopieres. */
export function cloneBlockWithNewIds(block: Block): Block {
  if (containsLockedSource(block)) {
    throw new Error("Låste kildeblokker kan ikke kopieres");
  }
  const copy = structuredClone(block);
  const renumber = (b: Block) => {
    b.id = newId();
    if (b.type === "CONDITIONAL") {
      b.ifTrue.forEach(renumber);
      b.ifFalse.forEach(renumber);
    } else if (b.type === "REPEAT") {
      b.body.forEach(renumber);
    }
  };
  renumber(copy);
  return copy;
}

export function duplicateBlockAt(blocks: readonly Block[], index: number): Block[] {
  const original = blocks[index];
  return original && !containsLockedSource(original)
    ? insertAt(blocks, index + 1, cloneBlockWithNewIds(original))
    : [...blocks];
}

/** Sletter en blokk, men aldri en som er eller inneholder en låst kildeblokk. */
export function removeBlockAt(blocks: readonly Block[], index: number): Block[] {
  const target = blocks[index];
  return target && containsLockedSource(target) ? [...blocks] : removeAt(blocks, index);
}

/**
 * Deler en blokk ved en grense. Første del beholder ID-en, andre del får ny ID.
 * Avsnitt/overskrifter deles mellom to innholdselementer, lister mellom to punkter.
 * Ingen oversettelse går tapt, siden hvert element bærer alle språk.
 */
export function splitBlock(block: Block, boundary: number): [Block, Block] | undefined {
  switch (block.type) {
    case "PARAGRAPH":
    case "HEADING": {
      if (boundary <= 0 || boundary >= block.text.length) {
        return undefined;
      }
      const first = { ...block, text: block.text.slice(0, boundary) };
      const second: Block = { type: "PARAGRAPH", id: newId(), text: block.text.slice(boundary) };
      return [first, second];
    }
    case "ITEM_LIST": {
      if (boundary <= 0 || boundary >= block.items.length) {
        return undefined;
      }
      return [
        { ...block, items: block.items.slice(0, boundary) },
        { ...block, id: newId(), items: block.items.slice(boundary) },
      ];
    }
    default: {
      return undefined;
    }
  }
}

/** Slår sammen blokk med den neste når det er lossless (avsnitt+avsnitt, liste+liste med lik nummerering). */
export function mergeWithNext(blocks: readonly Block[], index: number): Block[] | undefined {
  const a = blocks[index];
  const b = blocks[index + 1];
  if (!a || !b) {
    return undefined;
  }
  let merged: Block | undefined;
  if (a.type === "PARAGRAPH" && b.type === "PARAGRAPH") {
    merged = { ...a, text: [...a.text, ...b.text] };
  } else if (a.type === "ITEM_LIST" && b.type === "ITEM_LIST" && a.numbered === b.numbered) {
    merged = { ...a, items: [...a.items, ...b.items] };
  }
  if (!merged) {
    return undefined;
  }
  return [...blocks.slice(0, index), merged, ...blocks.slice(index + 2)];
}

export function canMergeWithNext(blocks: readonly Block[], index: number): boolean {
  return mergeWithNext(blocks, index) !== undefined;
}

// ---------- Flytting mellom containere ----------

export type BlockBranch = "root" | "ifTrue" | "ifFalse" | "body";
export type MoveTarget = { containerId: string | null; branch: BlockBranch; index: number };
export type MoveTargetOption = { key: string; label: string; containerId: string | null; branch: BlockBranch };

const childLists = (block: Block): [Exclude<BlockBranch, "root">, Block[]][] => {
  switch (block.type) {
    case "CONDITIONAL": {
      return [
        ["ifTrue", block.ifTrue],
        ["ifFalse", block.ifFalse],
      ];
    }
    case "REPEAT": {
      return [["body", block.body]];
    }
    default: {
      return [];
    }
  }
};

const withChildren = (block: Block, branch: BlockBranch, list: Block[]): Block => {
  if (block.type === "CONDITIONAL" && (branch === "ifTrue" || branch === "ifFalse")) {
    return { ...block, [branch]: list };
  }
  if (block.type === "REPEAT" && branch === "body") {
    return { ...block, body: list };
  }
  return block;
};

export const moveTargetKey = (containerId: string | null, branch: BlockBranch) => `${containerId ?? "root"}:${branch}`;

export function findBlock(blocks: readonly Block[], id: string): Block | undefined {
  return flattenBlocks(blocks).find((f) => f.block.id === id)?.block;
}

/** Alle lister en blokk kan flyttes til, uten blokken selv og dens etterkommere (ellers ville den havnet i seg selv). */
export function blockMoveTargets(outline: readonly Block[], blockId: string): MoveTargetOption[] {
  const options: MoveTargetOption[] = [
    { key: moveTargetKey(null, "root"), label: "Øverst i brevet", containerId: null, branch: "root" },
  ];
  const branchLabel = { ifTrue: "hvis sant", ifFalse: "ellers", body: "gjentakelse" } as const;
  const visit = (blocks: readonly Block[], trail: string) => {
    blocks.forEach((block, i) => {
      if (block.id === blockId) {
        return;
      }
      const name = `${trail}${trail ? " › " : ""}${block.type === "CONDITIONAL" ? "Vilkår" : "Gjentakelse"} ${i + 1}`;
      for (const [branch, list] of childLists(block)) {
        options.push({
          key: moveTargetKey(block.id, branch),
          label: `${name} (${branchLabel[branch]})`,
          containerId: block.id,
          branch,
        });
        visit(list, `${name} (${branchLabel[branch]})`);
      }
    });
  };
  visit(outline, "");
  return options;
}

/** Fjerner en blokk fra der den står og returnerer den sammen med resten. */
function detach(blocks: readonly Block[], id: string): { rest: Block[]; found?: Block } {
  let found: Block | undefined;
  const walk = (list: readonly Block[]): Block[] => {
    const out: Block[] = [];
    for (const block of list) {
      if (block.id === id) {
        found = block;
        continue;
      }
      let next = block;
      for (const [branch, children] of childLists(block)) {
        const changed = walk(children);
        if (changed.length !== children.length || changed.some((c, i) => c !== children[i])) {
          next = withChildren(next, branch, changed);
        }
      }
      out.push(next);
    }
    return out;
  };
  const rest = walk(blocks);
  return { rest, found };
}

function attach(blocks: readonly Block[], target: MoveTarget, block: Block): Block[] | undefined {
  if (target.containerId === null) {
    return insertAt(blocks, Math.min(target.index, blocks.length), block);
  }
  let done = false;
  const walk = (list: readonly Block[]): Block[] =>
    list.map((candidate) => {
      if (candidate.id === target.containerId) {
        const children = childLists(candidate).find(([branch]) => branch === target.branch)?.[1];
        if (children) {
          done = true;
          return withChildren(
            candidate,
            target.branch,
            insertAt(children, Math.min(target.index, children.length), block),
          );
        }
        return candidate;
      }
      let next = candidate;
      for (const [branch, children] of childLists(candidate)) {
        const changed = walk(children);
        if (changed.some((c, i) => c !== children[i])) {
          next = withChildren(next, branch, changed);
        }
      }
      return next;
    });
  const result = walk(blocks);
  return done ? result : undefined;
}

/**
 * Flytter en blokk (med alt innhold, ID-er og oversettelser uendret) til en annen liste.
 * Returnerer undefined når målet ikke finnes, er blokken selv/en etterkommer, eller blokken er en låst kildeblokk.
 */
export function relocateBlock(outline: readonly Block[], blockId: string, target: MoveTarget): Block[] | undefined {
  const moving = findBlock(outline, blockId);
  if (!moving || moving.type === "LOCKED_SOURCE") {
    return undefined;
  }
  if (target.containerId !== null && (target.containerId === blockId || findBlock([moving], target.containerId))) {
    return undefined;
  }
  const { rest, found } = detach(outline, blockId);
  return found ? attach(rest, target, found) : undefined;
}

// ---------- Tabell ----------

export function tableColumnCount(block: Extract<Block, { type: "TABLE" }>): number {
  return block.header.length;
}

export function addTableRow(block: Extract<Block, { type: "TABLE" }>, languages: readonly string[]) {
  return { ...block, rows: [...block.rows, block.header.map(() => [newTextInline(languages)])] };
}

export function addTableColumn(block: Extract<Block, { type: "TABLE" }>, languages: readonly string[]) {
  return {
    ...block,
    header: [...block.header, emptyTranslations(languages)],
    rows: block.rows.map((row) => [...row, [newTextInline(languages)]]),
  };
}

export function removeTableColumn(block: Extract<Block, { type: "TABLE" }>, column: number) {
  if (block.header.length <= 1) {
    return block;
  }
  return {
    ...block,
    header: removeAt(block.header, column),
    rows: block.rows.map((row) => removeAt(row, column)),
  };
}

// ---------- Traversering ----------

export function allFields(fields: readonly Field[]): Field[] {
  return fields.flatMap((field) => [field, ...allFields(field.children)]);
}

export type FlatBlock = { block: Block; parentId: string | null; branch: string; index: number };

export function flattenBlocks(blocks: readonly Block[], parentId: string | null = null, branch = "root"): FlatBlock[] {
  return blocks.flatMap((block, index): FlatBlock[] => {
    const here: FlatBlock = { block, parentId, branch, index };
    switch (block.type) {
      case "CONDITIONAL": {
        return [
          here,
          ...flattenBlocks(block.ifTrue, block.id, "ifTrue"),
          ...flattenBlocks(block.ifFalse, block.id, "ifFalse"),
        ];
      }
      case "REPEAT": {
        return [here, ...flattenBlocks(block.body, block.id, "body")];
      }
      default: {
        return [here];
      }
    }
  });
}

/** Alle `Inline[]`-sekvenser en blokk eier direkte (ikke barneblokker). */
export function inlineSequences(block: Block): Inline[][] {
  switch (block.type) {
    case "HEADING":
    case "PARAGRAPH": {
      return [block.text];
    }
    case "ITEM_LIST": {
      return block.items;
    }
    case "TABLE": {
      return block.rows.flat();
    }
    default: {
      return [];
    }
  }
}

export function walkInlines(inlines: readonly Inline[], visit: (inline: Inline) => void) {
  for (const inline of inlines) {
    visit(inline);
    if (inline.type === "CONDITIONAL") {
      walkInlines(inline.ifTrue, visit);
      walkInlines(inline.ifFalse, visit);
    }
  }
}

export function predicateRefs(predicate: Predicate): { fieldIds: string[]; choiceIds: string[] } {
  switch (predicate.type) {
    case "COMPARE":
    case "IS_NOT_NULL": {
      return { fieldIds: [predicate.fieldId], choiceIds: [] };
    }
    case "CHOICE": {
      return { fieldIds: [], choiceIds: [predicate.choiceId] };
    }
    case "NOT": {
      return predicateRefs(predicate.predicate);
    }
    case "ALL":
    case "ANY": {
      const parts = predicate.predicates.map(predicateRefs);
      return { fieldIds: parts.flatMap((p) => p.fieldIds), choiceIds: parts.flatMap((p) => p.choiceIds) };
    }
  }
}

/** Alle oversettelsesobjekter i et utkast, med en lesbar sti. Brukes til språkvalidering. */
export function collectTranslations(
  draft: Draft,
): { where: string; blockId?: string; choiceId?: string; t: Translations }[] {
  const out: { where: string; blockId?: string; choiceId?: string; t: Translations }[] = [];
  out.push({ where: "Tittel", t: draft.title });
  for (const choice of draft.choices) {
    out.push({ where: `Valg «${choice.name}»`, choiceId: choice.id, t: choice.prompt });
  }
  for (const { block } of flattenBlocks(draft.outline)) {
    const push = (where: string, t: Translations) => out.push({ where, blockId: block.id, t });
    if (block.type === "TABLE") {
      for (const [i, t] of block.header.entries()) {
        push(`Tabelloverskrift ${i + 1}`, t);
      }
    }
    for (const sequence of inlineSequences(block)) {
      walkInlines(sequence, (inline) => {
        if (inline.type === "TEXT") {
          push("Tekst", inline.text);
        } else if (inline.type === "FREE_TEXT") {
          push("Fritekst-veiledning", inline.prompt);
        }
      });
    }
  }
  return out;
}

/** Språk som har ikke-tomt innhold et sted i utkastet. */
export function languagesWithContent(draft: Draft): Set<string> {
  const used = new Set<string>();
  for (const { t } of collectTranslations(draft)) {
    for (const [language, value] of Object.entries(t)) {
      if (value.trim() !== "") {
        used.add(language);
      }
    }
  }
  return used;
}

/** Leser ren tekst for ett språk fra en inline-sekvens. Felt vises som «navn». */
export function inlinesToText(inlines: readonly Inline[], language: string, fieldName: (id: string) => string): string {
  return inlines
    .map((inline) => {
      switch (inline.type) {
        case "TEXT": {
          return translationText(inline.text, language);
        }
        case "FIELD": {
          return `«${fieldName(inline.fieldId)}»`;
        }
        case "NEW_LINE": {
          return "\n";
        }
        case "FREE_TEXT": {
          return `[fritekst: ${translationText(inline.prompt, language)}]`;
        }
        case "CONDITIONAL": {
          return `[hvis: ${inlinesToText(inline.ifTrue, language, fieldName)} | ellers: ${inlinesToText(inline.ifFalse, language, fieldName)}]`;
        }
        default: {
          return "";
        }
      }
    })
    .join("");
}

export function countFieldReferences(draft: Draft, fieldId: string): number {
  let count = 0;
  const inPredicate = (p: Predicate) => {
    count += predicateRefs(p).fieldIds.filter((id) => id === fieldId).length;
  };
  const inInlines = (inlines: readonly Inline[]) =>
    walkInlines(inlines, (inline) => {
      if (inline.type === "FIELD" && inline.fieldId === fieldId) {
        count += 1;
      } else if (inline.type === "CONDITIONAL") {
        inPredicate(inline.predicate);
      }
    });
  for (const { block } of flattenBlocks(draft.outline)) {
    if (block.type === "CONDITIONAL") {
      inPredicate(block.predicate);
    }
    if (block.type === "REPEAT" && block.fieldId === fieldId) {
      count += 1;
    }
    inlineSequences(block).forEach(inInlines);
  }
  return count;
}

export function countChoiceReferences(draft: Draft, choiceId: string): number {
  let count = 0;
  const inPredicate = (p: Predicate) => {
    count += predicateRefs(p).choiceIds.filter((id) => id === choiceId).length;
  };
  for (const { block } of flattenBlocks(draft.outline)) {
    if (block.type === "CONDITIONAL") {
      inPredicate(block.predicate);
    }
    for (const sequence of inlineSequences(block)) {
      walkInlines(sequence, (inline) => {
        if (inline.type === "CONDITIONAL") {
          inPredicate(inline.predicate);
        }
      });
    }
  }
  return count;
}

// ---------- Feltoppslag og scope ----------

export type FieldInfo = { field: Field; path: string; listAncestorId: string | null };

export function indexFields(
  fields: readonly Field[],
  prefix = "",
  listAncestorId: string | null = null,
): Map<string, FieldInfo> {
  const map = new Map<string, FieldInfo>();
  for (const field of fields) {
    const path = prefix ? `${prefix}.${field.name}` : field.name;
    map.set(field.id, { field, path, listAncestorId });
    const childList = field.kind === "LIST" ? field.id : listAncestorId;
    for (const [id, info] of indexFields(field.children, path, childList)) {
      map.set(id, info);
    }
  }
  return map;
}

/** Felt som kan refereres fra et sted: utenfor gjentakelse kun felt utenfor lister; inni gjentakelse også listens barn. */
export function fieldsInScope(index: Map<string, FieldInfo>, repeatListIds: readonly string[]): FieldInfo[] {
  return [...index.values()].filter(
    (info) => info.listAncestorId === null || repeatListIds.includes(info.listAncestorId),
  );
}

export function isScalar(kind: FieldKind): boolean {
  return kind !== "OBJECT" && kind !== "LIST";
}

export function metadataLanguages(draft: Draft): Language[] {
  return draft.metadata.languages;
}
