import { useQuery } from "@tanstack/react-query";

import { getRedigerbareVedlegg } from "~/api/redigerbareVedlegg-endpoints";
import { type Redigeringsflate } from "~/Brevredigering/LetterEditor/RedigeringsflateContext";

/**
 * The letter's redigerbare vedlegg (vedleggId + tittel only). Content is fetched per vedlegg when
 * one is opened, so the list stays cheap for the majority of letters that have none.
 *
 * Unlike the per-vedlegg content query this one is deliberately left unpinned: it decides whether the
 * active vedlegg still exists (`useActiveDocumentCoordinator`), and it never feeds editor state — only
 * the title is patched into it after a save — so refetching it cannot clobber unsaved edits.
 */
export function useRedigerbareVedlegg(args: { saksId: string; brevId: number; redigeringsflate: Redigeringsflate }) {
  return useQuery({
    queryKey: getRedigerbareVedlegg.queryKey(args.brevId, args.redigeringsflate),
    queryFn: () => getRedigerbareVedlegg.queryFn(args.saksId, args.brevId, args.redigeringsflate),
  });
}
