import { isSameResult, isSameTemplate, type ResultRef } from "~/search/searchContext";

/**
 * Order and position of results for previous/next navigation between
 * templates. Pure, so it is shared by the result list (which page a result is
 * on) and the template page (which result comes next).
 */

/** The ranked results grouped by template, in order of each template's
 *  best-ranked result. Each group holds the template's matching languages in
 *  rank order. The result list itself keeps its ranking; only navigation uses
 *  this grouping. */
export function templatesInOrder(ranked: ResultRef[]): ResultRef[][] {
  const byTemplate = new Map<string, ResultRef[]>();
  for (const ref of ranked) {
    const key = `${ref.malType}/${ref.id}`;
    byTemplate.set(key, [...(byTemplate.get(key) ?? []), ref]);
  }
  return [...byTemplate.values()];
}

export type ResultPosition = {
  /** 1-based position of the current template among the matching templates. */
  position: number;
  /** The number of matching templates, each counted once whatever its languages. */
  total: number;
  previous?: ResultRef;
  next?: ResultRef;
};

/** Where the template of `current` is among the matching templates, and the
 *  neighbouring templates to move to. A neighbour opens in the current
 *  language if it matched there, otherwise in its best-ranked language.
 *  `undefined` if the template is not among the results. */
export function resultPosition(ranked: ResultRef[], current: ResultRef): ResultPosition | undefined {
  const templates = templatesInOrder(ranked);
  const at = templates.findIndex(([first]) => isSameTemplate(first, current));
  if (at === -1) {
    return undefined;
  }
  const open = (languages: ResultRef[] | undefined) =>
    languages && (languages.find((ref) => ref.language === current.language) ?? languages[0]);
  return {
    position: at + 1,
    total: templates.length,
    previous: open(templates[at - 1]),
    next: open(templates[at + 1]),
  };
}

/** Whether a row in the result list stands for `selected`. Content results
 *  are one row per language; the Brev tab groups a template's languages in one
 *  row, so there any language of the template counts. */
export function isSelectedResult(row: ResultRef, selected: ResultRef | undefined, groupsLanguages: boolean): boolean {
  if (!selected) {
    return false;
  }
  return groupsLanguages ? isSameTemplate(row, selected) : isSameResult(row, selected);
}

/** The 1-based page of the result list that shows `selected`, or `undefined`
 *  if it is not among the results. */
export function pageOfResult(
  ranked: ResultRef[],
  selected: ResultRef,
  pageSize: number,
  groupsLanguages: boolean,
): number | undefined {
  const at = ranked.findIndex((ref) => isSelectedResult(ref, selected, groupsLanguages));
  return at === -1 ? undefined : Math.floor(at / pageSize) + 1;
}
