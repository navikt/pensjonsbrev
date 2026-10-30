import { isMalType, type MalType } from "~/api/brevbaker-api-endpoints";

/**
 * The search context as it travels in URLs between the result list
 * (`/templates`) and a template (`/template/$malType/$templateId`).
 *
 * Only what is needed to reproduce the context goes in the URL, so a copied
 * address opens the same search: the criteria, the page, and which result was
 * opened. Hit lists and highlight ranges are never stored; both pages derive
 * them by running the search again.
 *
 * Defaults are left out of the URL (`exact=false`, `tab=brev`, `page=1`),
 * which keeps plain links short.
 */

export type ResultTab = "innhold" | "brev";

/** The tab the result list opens in: title hits first, content hits a click away. */
export const DEFAULT_TAB: ResultTab = "brev";

/** The `tab` search param. Only the non-default tab is written. */
type TabParam = "innhold";

/** What was searched for, and in which result tab. */
export type SearchCriteria = {
  q: string;
  exact: boolean;
  tab: ResultTab;
};

/** One result entry: a template in one language. */
export type ResultRef = {
  malType: MalType;
  id: string;
  language: string;
};

/** Search params of `/templates`. */
export type TemplatesSearchParams = {
  q?: string;
  exact?: true;
  tab?: TabParam;
  page?: number;
  /** The result that was opened, as `malType/brevkode/language`. */
  selected?: string;
};

/** The search context part of `/template/$malType/$templateId`'s search params. */
export type TemplateSearchContextParams = {
  q?: string;
  exact?: true;
  tab?: TabParam;
};

/** TanStack Router parses search values as JSON where it can, so a hand-typed
 *  `?q=12` arrives as a number. Values the app writes itself are quoted and
 *  round-trip as strings. */
function stringParam(value: unknown): string | undefined {
  if (typeof value === "string") {
    return value;
  }
  if (typeof value === "number" || typeof value === "boolean") {
    return String(value);
  }
  return undefined;
}

function positiveIntParam(value: unknown): number | undefined {
  const raw = stringParam(value);
  if (raw === undefined || !/^\d+$/.test(raw)) {
    return undefined;
  }
  const number = Number(raw);
  return number >= 1 ? number : undefined;
}

function queryParam(value: unknown): string | undefined {
  const q = stringParam(value)?.trim();
  return q ? q : undefined;
}

function exactParam(value: unknown): true | undefined {
  return value === true || value === "true" ? true : undefined;
}

function tabParam(value: unknown): TabParam | undefined {
  return value === "innhold" ? "innhold" : undefined;
}

export function formatResultRef(ref: ResultRef): string {
  return `${ref.malType}/${ref.id}/${ref.language}`;
}

export function parseResultRef(value: unknown): ResultRef | undefined {
  const parts = stringParam(value)?.split("/");
  if (parts?.length !== 3) {
    return undefined;
  }
  const [malType, id, language] = parts;
  if (!isMalType(malType) || !id || !language) {
    return undefined;
  }
  return { malType, id, language };
}

export function isSameResult(a: ResultRef, b: ResultRef): boolean {
  return a.malType === b.malType && a.id === b.id && a.language === b.language;
}

export function isSameTemplate(a: ResultRef, b: ResultRef): boolean {
  return a.malType === b.malType && a.id === b.id;
}

export function validateTemplatesSearch(search: Record<string, unknown>): TemplatesSearchParams {
  const selected = parseResultRef(search.selected);
  return {
    q: queryParam(search.q),
    exact: exactParam(search.exact),
    tab: tabParam(search.tab),
    page: positiveIntParam(search.page),
    selected: selected ? formatResultRef(selected) : undefined,
  };
}

export function validateTemplateSearchContext(search: Record<string, unknown>): TemplateSearchContextParams {
  return {
    q: queryParam(search.q),
    exact: exactParam(search.exact),
    tab: tabParam(search.tab),
  };
}

/** The criteria in a set of search params, or `undefined` if nothing was searched for. */
export function criteriaOf(params: { q?: string; exact?: true; tab?: TabParam }): SearchCriteria | undefined {
  if (!params.q) {
    return undefined;
  }
  return { q: params.q, exact: params.exact ?? false, tab: params.tab ?? DEFAULT_TAB };
}

function criteriaParams(criteria: SearchCriteria): { q: string; exact?: true; tab?: TabParam } {
  return {
    q: criteria.q,
    exact: criteria.exact ? true : undefined,
    tab: criteria.tab === "innhold" ? "innhold" : undefined,
  };
}

/** Search params for returning to `/templates` with `selected` marked. The
 *  page is left out: the result list derives it from where `selected` is. */
export function toTemplatesSearch(criteria: SearchCriteria, selected?: ResultRef): TemplatesSearchParams {
  return { ...criteriaParams(criteria), selected: selected ? formatResultRef(selected) : undefined };
}

/** Search params for opening `ref` from a search. `block` is the block to
 *  open at; without it, the template opens at its first matching block. */
export function toTemplateSearch(
  criteria: SearchCriteria,
  ref: ResultRef,
  block?: number,
): TemplateSearchContextParams & { language: string; index?: number } {
  return { ...criteriaParams(criteria), language: ref.language, index: block };
}

/** The result a hit stands for: its template in its language. */
export function resultRefOf(template: ResultRef): ResultRef {
  return { malType: template.malType, id: template.id, language: template.language };
}
