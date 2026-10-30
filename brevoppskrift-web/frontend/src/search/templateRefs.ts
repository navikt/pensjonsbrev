import { type QueryClient, queryOptions, useQueries } from "@tanstack/react-query";
import { useMemo } from "react";

import {
  getBrevkoderMedMetadata,
  getTemplateDescription,
  MAL_TYPES,
  type MalType,
} from "~/api/brevbaker-api-endpoints";
import { type TemplateDescription } from "~/api/brevbakerTypes";
import { type TemplateRef } from "~/search/useSearchResults";

export function displayTitleOf(description: { name: string; metadata?: { displayTitle?: string } | null }): string {
  const title = description.metadata?.displayTitle;
  return title?.trim() ? title : description.name;
}

export function toRefs(templates: TemplateDescription[], malType: MalType): TemplateRef[] {
  return templates.map((description) => ({
    malType,
    brevkode: description.name,
    title: displayTitleOf(description),
    languages: description.languages,
  }));
}

function assertList(descriptions: unknown, malType: MalType): asserts descriptions is TemplateDescription[] {
  if (!Array.isArray(descriptions)) {
    throw new TypeError(
      `Forventet en liste med brevkoder for "${malType}", men fikk noe annet. Går kallene til brevbaker-APIet (f.eks. via BFF eller Vite-proxy)?`,
    );
  }
}

/** Every template of `malType` with its metadata. Shared by the template list
 *  and the template page, so both cache, validate and fail the same way. */
function descriptionsQuery(malType: MalType) {
  return queryOptions({
    queryKey: getBrevkoderMedMetadata.queryKey(malType),
    queryFn: async () => {
      const descriptions: unknown = await getBrevkoderMedMetadata.queryFn(malType);
      assertList(descriptions, malType);
      return descriptions;
    },
  });
}

export async function fetchAllDescriptions(queryClient: QueryClient, malType: MalType): Promise<TemplateDescription[]> {
  const descriptions = await queryClient.ensureQueryData(descriptionsQuery(malType));
  // Seed the per-template description cache so the detail route loads instantly.
  for (const description of descriptions) {
    queryClient.setQueryData(getTemplateDescription.queryKey(malType, description.name), description);
  }
  return descriptions;
}

export type AllTemplateRefs = { status: "loading" } | { status: "failed" } | { status: "ready"; refs: TemplateRef[] };

const LOADING: AllTemplateRefs = { status: "loading" };
const FAILED: AllTemplateRefs = { status: "failed" };

/** Every template the search covers, for pages that search without having
 *  loaded the template list themselves. Loading while `enabled` is false
 *  (nothing is fetched then). Failed once a fetch has given up, so a page can
 *  say so rather than wait forever. */
export function useAllTemplateRefs(enabled = true): AllTemplateRefs {
  const queries = useQueries({
    queries: MAL_TYPES.map((malType) => ({ ...descriptionsQuery(malType), enabled })),
  });
  // A failed refetch keeps the data it had, which is still good to search.
  const failed = queries.some((query) => query.isError && query.data === undefined);
  const [autobrev, redigerbar] = queries.map((query) => query.data);
  return useMemo(() => {
    if (failed) {
      return FAILED;
    }
    if (!autobrev || !redigerbar) {
      return LOADING;
    }
    return { status: "ready", refs: [...toRefs(autobrev, "autobrev"), ...toRefs(redigerbar, "redigerbar")] };
  }, [failed, autobrev, redigerbar]);
}
