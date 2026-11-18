import type * as G from "~/api/brevoppskrift-api.generated";

import {
  type Attachment,
  type Block,
  type Choice,
  type Draft,
  type DraftComment,
  type Field,
  type Inline,
  type Predicate,
  type Scenario,
} from "./types";

/** Fyller inn standardverdier generatoren markerer som valgfrie, slik at resten av klienten kan regne dem som satt. */

function predicate(p: G.Predicate): Predicate {
  switch (p.type) {
    case "ALL":
    case "ANY": {
      return { ...p, predicates: p.predicates.map(predicate) };
    }
    case "NOT": {
      return { ...p, predicate: predicate(p.predicate) };
    }
    default: {
      return p;
    }
  }
}

function inline(i: G.Inline): Inline {
  switch (i.type) {
    case "TEXT": {
      return { ...i, fontType: i.fontType ?? "PLAIN" };
    }
    case "FIELD": {
      return { ...i, formatter: i.formatter ?? "DEFAULT" };
    }
    case "CONDITIONAL": {
      return { ...i, predicate: predicate(i.predicate), ifTrue: i.ifTrue.map(inline), ifFalse: i.ifFalse.map(inline) };
    }
    default: {
      return i;
    }
  }
}

function block(b: G.Block): Block {
  switch (b.type) {
    case "HEADING":
    case "PARAGRAPH": {
      return { ...b, text: b.text.map(inline) };
    }
    case "ITEM_LIST": {
      return { ...b, items: b.items.map((item) => item.map(inline)) };
    }
    case "TABLE": {
      return { ...b, rows: b.rows.map((row) => row.map((cell) => cell.map(inline))) };
    }
    case "CONDITIONAL": {
      return {
        ...b,
        predicate: predicate(b.predicate),
        ifTrue: b.ifTrue.map(block),
        ifFalse: (b.ifFalse ?? []).map(block),
      };
    }
    case "REPEAT": {
      return { ...b, body: b.body.map(block) };
    }
    default: {
      return b;
    }
  }
}

function field(f: G.DataField): Field {
  return {
    ...f,
    nullable: f.nullable ?? false,
    children: (f.children ?? []).map(field),
    enumValues: f.enumValues ?? [],
  };
}

function comment(c: G.DraftComment): DraftComment {
  return { ...c, replies: c.replies ?? [], states: c.states ?? [] };
}

function scenario(s: G.TestScenario): Scenario {
  return { ...s, choices: s.choices ?? {} };
}

export function normalizeDraft(d: G.Draft): Draft {
  return {
    ...d,
    fields: (d.fields ?? []).map(field),
    choices: (d.choices ?? []) as Choice[],
    outline: (d.outline ?? []).map(block),
    scenarios: (d.scenarios ?? []).map(scenario),
    comments: (d.comments ?? []).map(comment),
    baseline: d.baseline ? { ...d.baseline, draft: normalizeDraft(d.baseline.draft) } : d.baseline,
    attachments: (d.attachments ?? []).map((a): Attachment => ({ ...a, draft: normalizeDraft(a.draft) })),
  };
}
