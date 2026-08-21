import { useQuery } from "@tanstack/react-query";

import { getRedigerbareVedlegg } from "~/api/redigerbareVedlegg-endpoints";

/**
 * The letter's redigerbare vedlegg (vedleggId + tittel only). Content is fetched per vedlegg when
 * one is opened, so the list stays cheap for the majority of letters that have none.
 */
export function useRedigerbareVedlegg(args: { saksId: string; brevId: number }) {
  return useQuery({
    queryKey: getRedigerbareVedlegg.queryKey(args.brevId),
    queryFn: () => getRedigerbareVedlegg.queryFn(args.saksId, args.brevId),
  });
}
