import { useQuery } from "@tanstack/react-query";

import { getPreferredLanguage, getSak } from "~/api/skribenten-api-endpoints";

export function usePreferredLanguage(saksId: string) {
  const sak = useQuery({
    queryKey: getSak.queryKey(saksId),
    queryFn: () => getSak.queryFn(saksId),
  }).data;

  return useQuery({
    queryKey: getPreferredLanguage.queryKey(sak?.foedselsnr as string),
    queryFn: () => getPreferredLanguage.queryFn(sak?.saksId.toString() as string),
    enabled: !!sak,
  }).data?.spraakKode;
}
