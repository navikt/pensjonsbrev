import { type QueryClient, useQueries } from "@tanstack/react-query";
import { useMemo } from "react";

import { getBrevkoderMedMetadata, getTemplateDescription, type MalType } from "~/api/brevbaker-api-endpoints";
import { type TemplateDescription } from "~/api/brevbakerTypes";
import { type TemplateRef } from "~/search/useSearchResults";

const MAL_TYPES: MalType[] = ["autobrev", "redigerbar"];

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

export async function fetchAllDescriptions(queryClient: QueryClient, malType: MalType): Promise<TemplateDescription[]> {
  const descriptions = await queryClient.ensureQueryData({
    queryKey: getBrevkoderMedMetadata.queryKey(malType),
    queryFn: () => getBrevkoderMedMetadata.queryFn(malType),
  });
  assertList(descriptions, malType);
  // Seed the per-template description cache so the detail route loads instantly.
  for (const description of descriptions) {
    queryClient.setQueryData(getTemplateDescription.queryKey(malType, description.name), description);
  }
  return descriptions;
}

/** Every template the search covers, for pages that search without having
 *  loaded the template list themselves. `undefined` until all are known, and
 *  while `enabled` is false (nothing is fetched then). */
export function useAllTemplateRefs(enabled = true): TemplateRef[] | undefined {
  const queries = useQueries({
    queries: MAL_TYPES.map((malType) => ({
      queryKey: getBrevkoderMedMetadata.queryKey(malType),
      queryFn: () => getBrevkoderMedMetadata.queryFn(malType),
      enabled,
    })),
  });
  const [autobrev, redigerbar] = queries.map((query) => (Array.isArray(query.data) ? query.data : undefined));
  return useMemo(
    () => (autobrev && redigerbar ? [...toRefs(autobrev, "autobrev"), ...toRefs(redigerbar, "redigerbar")] : undefined),
    [autobrev, redigerbar],
  );
}
