import isEqual from "lodash/isEqual";

import { caret, containers, resolve, TITLE } from "~/Brevredigering/EditorV2/model/paths";
import { isUneditedFritekst } from "~/Brevredigering/EditorV2/model/text";
import { type Focus } from "~/Brevredigering/EditorV2/state/editorState";
import { type DocumentV2 } from "~/types/brevV2";

function normalized(value: unknown): unknown {
  if (Array.isArray(value)) return value.map(normalized);
  if (typeof value !== "object" || value === null) return value;
  return Object.fromEntries(
    Object.entries(value)
      .filter(([, child]) => child !== null && child !== undefined)
      .map(([key, child]) =>
        key.startsWith("deleted") && Array.isArray(child)
          ? [key, child.toSorted((a, b) => a - b)]
          : [key, normalized(child)],
      ),
  );
}

export const sameDocument = (a: DocumentV2, b: DocumentV2): boolean => isEqual(normalized(a), normalized(b));

const textCarets = (doc: DocumentV2) =>
  [TITLE, ...containers(doc)].flatMap(
    (path) => resolve(doc, path)?.content.map((text, index) => ({ text, at: caret(path, index) })) ?? [],
  );

export const countUneditedFritekst = (doc: DocumentV2): number =>
  textCarets(doc).filter(({ text }) => isUneditedFritekst(text)).length;

export const countMissingFromTemplate = (doc: DocumentV2): number =>
  doc.blocks.filter((block) => block.missingFromTemplate).length;

export function firstUneditedFritekst(doc: DocumentV2): Focus | undefined {
  const found = textCarets(doc).find(({ text }) => isUneditedFritekst(text));
  return found && { ...found.at, selectAll: true };
}
