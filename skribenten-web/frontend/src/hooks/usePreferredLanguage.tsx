import { useQuery } from "@tanstack/react-query";

import { getPreferredLanguage, getSakContext } from "~/api/skribenten-api-endpoints";

export function usePreferredLanguage(saksId: string, vedtaksId: string | undefined) {
  const sakContext = useQuery({
    ...getSakContext,
    queryKey: getSakContext.queryKey(saksId, vedtaksId),
    queryFn: () => getSakContext.queryFn(saksId, vedtaksId),
  }).data;

  return useQuery({
    queryKey: getPreferredLanguage.queryKey(sakContext?.sak?.foedselsnr as string),
    queryFn: () => getPreferredLanguage.queryFn(sakContext?.sak?.saksId.toString() as string),
    enabled: !!sakContext?.sak,
  }).data?.spraakKode;
}
