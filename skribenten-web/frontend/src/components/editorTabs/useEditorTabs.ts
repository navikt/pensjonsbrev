import { useQuery } from "@tanstack/react-query";
import { useMemo } from "react";

import { getRedigerbareVedlegg } from "~/api/redigerbareVedlegg-endpoints";
import { type BrevResponse } from "~/types/brev";

import { BREV_TAB_ID, type EditorTab, redigerbartVedleggTabId } from "./types";

/**
 * Builds the editor's tab list: the brev itself (always first), then its redigerbare vedlegg from
 * GET /redigerbareVedlegg (vedleggId + title only — content is fetched on demand when the tab is
 * opened).
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
    };

    const redigerbareTabs: EditorTab[] = (redigerbareVedleggQuery.data ?? []).map((vedlegg) => ({
      id: redigerbartVedleggTabId(vedlegg.vedleggId),
      label: vedlegg.tittel,
      type: "redigerbartVedlegg",
    }));

    return [brevTab, ...redigerbareTabs];
  }, [brev.info.brevtittel, redigerbareVedleggQuery.data]);

  return { tabs };
}
