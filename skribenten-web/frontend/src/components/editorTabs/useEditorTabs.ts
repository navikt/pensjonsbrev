import { useQuery } from "@tanstack/react-query";
import { useMemo } from "react";

import { getRedigerbareVedlegg } from "~/api/redigerbareVedlegg-endpoints";
import { type BrevResponse } from "~/types/brev";

import { alltidValgbartVedleggTabId, BREV_TAB_ID, type EditorTab, redigerbartVedleggTabId } from "./types";

/**
 * Builds the editor's tab list from the brev plus the lightweight redigerbareVedlegg list query.
 *
 * Tab order and sources:
 *  1. The brev itself (always first), from the brev query.
 *  2. Redigerbare vedlegg, from GET /redigerbareVedlegg (vedleggId + title only — content is
 *     fetched on demand when the tab is opened).
 *  3. AlltidValgbare vedlegg the saksbehandler has selected, from brev.valgteVedlegg (read-only).
 *  4. P1, derived from the brev (brevkode), not from any vedlegg endpoint.
 */
export function useEditorTabs(args: { saksId: string; brev: BrevResponse }): { tabs: EditorTab[] } {
  const { saksId, brev } = args;

  const redigerbareVedleggQuery = useQuery({
    queryKey: getRedigerbareVedlegg.queryKey(brev.info.id),
    queryFn: () => getRedigerbareVedlegg.queryFn(saksId, brev.info.id),
  });

  const tabs = useMemo<EditorTab[]>(() => {
    const brevTab: EditorTab = {
      id: BREV_TAB_ID,
      label: brev.info.brevtittel,
      type: "brev",
      locked: false,
    };

    const redigerbareTabs: EditorTab[] = (redigerbareVedleggQuery.data ?? []).map((vedlegg) => ({
      id: redigerbartVedleggTabId(vedlegg.vedleggId),
      label: vedlegg.tittel,
      type: "redigerbartVedlegg",
      locked: false,
    }));

    const alltidValgbareTabs: EditorTab[] = (brev.valgteVedlegg ?? []).map((vedlegg) => ({
      id: alltidValgbartVedleggTabId(vedlegg.kode),
      label: vedlegg.visningstekst,
      type: "alltidValgbartVedlegg",
      locked: true,
    }));

    // P1 is moved to a separate PR (it has its own form, not the letter editor). The P1 tab is
    // intentionally omitted here so no dead "P1" placeholder appears; it is re-added in the P1 PR.
    return [brevTab, ...redigerbareTabs, ...alltidValgbareTabs];
  }, [brev.info.brevtittel, brev.valgteVedlegg, redigerbareVedleggQuery.data]);

  return { tabs };
}
