import {
  collectTranslations,
  type FieldInfo,
  flattenBlocks,
  indexFields,
  inlineSequences,
  predicateRefs,
  walkInlines,
} from "./draftOps";
import { type Draft, type Inline, type Predicate } from "./types";

export type ValidationIssue = {
  key: string;
  severity: "error" | "warning";
  message: string;
  blockId?: string;
  fieldId?: string;
  choiceId?: string;
  language?: string;
};

/**
 * Rask klientvalidering som viser feil ved kilden. Serveren er autoritativ;
 * dette er ingen tillitsgrense og erstatter ikke generatorens diagnostikk.
 */
export function validateDraft(draft: Draft): ValidationIssue[] {
  const issues: ValidationIssue[] = [];
  const add = (issue: Omit<ValidationIssue, "key">) =>
    issues.push({ ...issue, key: `${issues.length}:${issue.message}` });
  const languages = draft.metadata.languages;
  const index = indexFields(draft.fields);
  const choiceIds = new Set(draft.choices.map((c) => c.id));

  if (draft.metadata.name.trim() === "") {
    add({ severity: "error", message: "Malen mangler navn." });
  }
  if (languages.length === 0) {
    add({ severity: "error", message: "Malen må ha minst ett språk." });
  }

  for (const { where, blockId, choiceId, t } of collectTranslations(draft)) {
    for (const language of languages) {
      if ((t[language] ?? "").trim() === "") {
        add({
          severity: "error",
          message: `${where} mangler tekst på ${language}.`,
          blockId,
          choiceId,
          language,
        });
      }
    }
  }

  // Felt: unike ID-er/navn, enum-verdier
  const seenFieldIds = new Set<string>();
  const seenNames = new Map<string, string>();
  for (const [id, info] of index) {
    if (seenFieldIds.has(id)) {
      add({ severity: "error", message: `Duplisert felt-ID for «${info.path}».`, fieldId: id });
    }
    seenFieldIds.add(id);
    if (!/^[a-zA-Z_][a-zA-Z0-9_]*$/.test(info.field.name)) {
      add({ severity: "error", message: `Feltnavnet «${info.field.name}» er ikke et gyldig navn.`, fieldId: id });
    }
    const parentPath = info.path.slice(0, Math.max(0, info.path.lastIndexOf(".")));
    const siblingKey = `${parentPath}/${info.field.name}`;
    if (seenNames.has(siblingKey)) {
      add({ severity: "error", message: `Duplisert feltnavn «${info.path}».`, fieldId: id });
    }
    seenNames.set(siblingKey, id);
    if (info.field.kind === "ENUM" && info.field.enumValues.filter((v) => v.trim() !== "").length === 0) {
      add({ severity: "error", message: `Enum-feltet «${info.path}» mangler verdier.`, fieldId: id });
    }
    if (info.field.kind === "LIST" && info.field.children.length === 0) {
      add({ severity: "warning", message: `Listefeltet «${info.path}» har ingen delfelt.`, fieldId: id });
    }
  }

  const checkPredicate = (predicate: Predicate, blockId: string | undefined, repeatLists: string[]) => {
    const refs = predicateRefs(predicate);
    for (const fieldId of refs.fieldIds) {
      checkFieldRef(fieldId, blockId, repeatLists, "Vilkår");
    }
    for (const choiceId of refs.choiceIds) {
      if (!choiceIds.has(choiceId)) {
        add({ severity: "error", message: "Vilkåret peker på et valg som ikke finnes.", blockId });
      }
    }
  };

  const checkFieldRef = (fieldId: string, blockId: string | undefined, repeatLists: string[], what: string) => {
    const info: FieldInfo | undefined = index.get(fieldId);
    if (!info) {
      add({ severity: "error", message: `${what} peker på et felt som ikke finnes eller ikke er valgt.`, blockId });
    } else if (info.listAncestorId !== null && !repeatLists.includes(info.listAncestorId)) {
      add({
        severity: "error",
        message: `${what} bruker «${info.path}» utenfor en gjentakelse over listen.`,
        blockId,
        fieldId,
      });
    }
  };

  const checkInlines = (inlines: readonly Inline[], blockId: string, repeatLists: string[]) =>
    walkInlines(inlines, (inline) => {
      if (inline.type === "FIELD") {
        checkFieldRef(inline.fieldId, blockId, repeatLists, "Variabel");
      } else if (inline.type === "CONDITIONAL") {
        checkPredicate(inline.predicate, blockId, repeatLists);
      }
    });

  // Blokker, med gjentakelsesscope
  const walk = (blocks: Draft["outline"], repeatLists: string[]) => {
    for (const block of blocks) {
      for (const sequence of inlineSequences(block)) {
        checkInlines(sequence, block.id, repeatLists);
      }
      if (block.type === "TABLE") {
        if (block.rows.some((row) => row.length !== block.header.length)) {
          add({ severity: "error", message: "Tabellen har rader med feil antall celler.", blockId: block.id });
        }
      }
      if (block.type === "CONDITIONAL") {
        checkPredicate(block.predicate, block.id, repeatLists);
        walk(block.ifTrue, repeatLists);
        walk(block.ifFalse, repeatLists);
      } else if (block.type === "REPEAT") {
        const info = index.get(block.fieldId);
        if (info?.field.kind === "LIST") {
          walk(block.body, [...repeatLists, block.fieldId]);
        } else {
          add({ severity: "error", message: "Gjentakelsen må peke på et listefelt.", blockId: block.id });
          walk(block.body, repeatLists);
        }
      }
    }
  };
  walk(draft.outline, []);

  const seenBlockIds = new Set<string>();
  for (const { block } of flattenBlocks(draft.outline)) {
    if (seenBlockIds.has(block.id)) {
      add({ severity: "error", message: "To blokker har samme ID.", blockId: block.id });
    }
    seenBlockIds.add(block.id);
  }

  return issues;
}

export function issuesForBlock(issues: readonly ValidationIssue[], blockId: string): ValidationIssue[] {
  return issues.filter((issue) => issue.blockId === blockId);
}

export function hasBlockingIssues(issues: readonly ValidationIssue[]): boolean {
  return issues.some((issue) => issue.severity === "error");
}
