import { useQuery } from "@tanstack/react-query";

import { getPreferredLanguage, getSak } from "~/api/skribenten-api-endpoints";

export function usePreferredLanguage(sakId: string) {
  const sak = useQuery({
    queryKey: getSak.queryKey(sakId),
    queryFn: () => getSak.queryFn(sakId),
  }).data;

  return useQuery({
    queryKey: getPreferredLanguage.queryKey(sak?.foedselsnr as string),
    queryFn: () => getPreferredLanguage.queryFn(sak?.sakId.toString() as string, sak?.foedselsnr as string),
    enabled: !!sak,
  }).data?.spraakKode;
}
