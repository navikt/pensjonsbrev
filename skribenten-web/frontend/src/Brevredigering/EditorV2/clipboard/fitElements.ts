import {
  mergeNeighbouringText,
  type Text,
  type TraversedElement,
} from "~/Brevredigering/LetterEditor/actions/traversedElement";
import { type DocumentV2 } from "~/types/brevV2";

import { type Caret } from "../model/paths";
import { hasCells } from "./buildTable";

export function fitElements(doc: DocumentV2, at: Caret, elements: TraversedElement[]): TraversedElement[] | undefined {
  const kept = elements.filter((element) => element.type !== "TABLE" || hasCells(element));
  if (kept.length === 0) return undefined;
  switch (at.path.kind) {
    case "title":
    case "header":
    case "cell":
      return kept.every((element) => element.type === "TEXT") ? kept : inlineTextOf(kept);
    case "block":
      return kept.every((element) => element.type === "TABLE") && doc.blocks[at.path.block]?.type !== "PARAGRAPH"
        ? undefined
        : kept;
    case "item":
      return kept;
  }
}

function inlineTextOf(elements: TraversedElement[]): Text[] | undefined {
  const runs: Text[] = [];
  let previous: TraversedElement | undefined;
  for (const element of elements) {
    if (element.type !== "TEXT" && element.type !== "P") return undefined;
    const content = element.type === "P" ? element.content : [element];
    const last = runs.at(-1);
    const startsParagraph = element.type === "P" || previous?.type === "P";
    if (startsParagraph && last && content.some(hasText)) {
      runs[runs.length - 1] = { ...last, text: `${last.text} ` };
    }
    runs.push(...content);
    previous = element;
  }
  const merged = mergeNeighbouringText(runs);
  return merged.some(hasText) ? merged : undefined;
}

function hasText(run: Text): boolean {
  return run.text.trim().length > 0;
}
