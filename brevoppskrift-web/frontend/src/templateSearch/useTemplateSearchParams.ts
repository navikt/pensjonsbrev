import { useLocation, useNavigate, useRouter } from "@tanstack/react-router";
import { useMemo } from "react";

import { type MalType } from "~/api/brevbaker-api-endpoints";
import {
  criteriaOf,
  focusParam,
  type TemplateSearchContextParams,
  validateTemplateSearchContext,
} from "~/search/searchContext";

export type TemplateSearchParams = TemplateSearchContextParams & {
  language?: string;
  highlightedDataClass?: string;
  highlightedDataField?: string;
  highlightedDataFieldOwner?: string;
};

export function validateTemplateSearch(search: Record<string, unknown>): TemplateSearchParams {
  return {
    language: search.language?.toString(),
    highlightedDataClass: search.highlightedDataClass?.toString(),
    highlightedDataField: search.highlightedDataField?.toString(),
    highlightedDataFieldOwner: search.highlightedDataFieldOwner?.toString(),
    ...validateTemplateSearchContext(search),
  };
}

const TEMPLATE_ROUTE = "/template/$malType/$templateId";

/**
 * The template page's search params, and the only way the page changes them.
 *
 * `requested` is the search of the latest navigation on this template. The router
 * knows it as soon as the navigation starts, also when it is redirected or replaced
 * by a later one, while `loaded` (the route's search) follows once it has loaded.
 * While another page is being opened, `requested` is `loaded`, and changes are
 * dropped, so they do not land on that page.
 *
 * A query change opens at the query's first hit, and a language change keeps the
 * block in focus, since block indexes are the same in every language.
 */
export function useTemplateSearchParams(
  { malType, templateId }: { malType: MalType; templateId: string },
  loaded: TemplateSearchParams,
) {
  const router = useRouter();
  const navigate = useNavigate({ from: TEMPLATE_ROUTE });
  const pathname = useMemo(
    () => router.buildLocation({ to: TEMPLATE_ROUTE, params: { malType, templateId } }).pathname,
    [router, malType, templateId],
  );
  const latestSearch = useLocation({
    select: (location) => (location.pathname === pathname ? location.search : undefined),
  });
  const requested = useMemo(
    () => (latestSearch ? validateTemplateSearch(latestSearch as Record<string, unknown>) : loaded),
    [latestSearch, loaded],
  );

  const write = (update: (search: TemplateSearchParams) => TemplateSearchParams) => {
    if (router.latestLocation.pathname !== pathname) {
      return false;
    }
    void navigate({ replace: true, search: (search) => update(validateTemplateSearch(search)) });
    return true;
  };

  return {
    requested,
    changeQuery: (q: string | undefined) =>
      write((search) => {
        if (search.q === q) {
          return search;
        }
        // Without a search to keep the tab of, it searches the content, which is what a template shows.
        return q === undefined
          ? { ...search, q: undefined, exact: undefined, tab: undefined, showHits: undefined, focus: undefined }
          : { ...search, q, tab: criteriaOf(search) ? search.tab : "innhold", focus: undefined };
      }),
    changeMarking: (show: boolean) => write((search) => ({ ...search, showHits: show ? undefined : false })),
    selectBlock: (block: number) => write((search) => ({ ...search, focus: focusParam(block) })),
    changeLanguage: (language: string, block: number | undefined) =>
      write((search) => ({ ...search, language, focus: focusParam(block) })),
  };
}
