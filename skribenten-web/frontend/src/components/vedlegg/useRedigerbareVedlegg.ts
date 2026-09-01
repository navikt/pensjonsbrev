import { useQuery } from "@tanstack/react-query";

import { getRedigerbareVedlegg } from "~/api/redigerbareVedlegg-endpoints";
import { type Redigeringsflate } from "~/utils/editorTracking";

/**
 * The letter's redigerbare vedlegg (vedleggId + tittel only). Content is fetched per vedlegg when
 * one is opened, so the list stays cheap for the majority of letters that have none.
 */
export function useRedigerbareVedlegg(args: { saksId: string; brevId: number; redigeringsflate: Redigeringsflate }) {
  return useQuery({
    queryKey: getRedigerbareVedlegg.queryKey(args.brevId, args.redigeringsflate),
    queryFn: () => getRedigerbareVedlegg.queryFn(args.saksId, args.brevId, args.redigeringsflate),
  });
}
