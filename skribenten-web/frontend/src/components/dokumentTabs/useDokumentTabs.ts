import { useQuery } from "@tanstack/react-query";
import { useMemo } from "react";

import { getRedigerbareVedlegg } from "~/api/redigerbareVedlegg-endpoints";
import { type BrevResponse, P1_BREVKODE } from "~/types/brev";

import { BREV_TAB_ID, type DokumentTab, P1_TAB_ID } from "./types";

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
export function useDokumentTabs(args: { saksId: string; brev: BrevResponse }): { tabs: DokumentTab[] } {
  const { saksId, brev } = args;

  const redigerbareVedleggQuery = useQuery({
    queryKey: getRedigerbareVedlegg.queryKey(brev.info.id),
    queryFn: () => getRedigerbareVedlegg.queryFn(saksId, brev.info.id),
  });

  const tabs = useMemo<DokumentTab[]>(() => {
    const brevTab: DokumentTab = {
      id: BREV_TAB_ID,
      label: brev.info.brevtittel,
      type: "brev",
      locked: false,
    };

    const redigerbareTabs: DokumentTab[] = (redigerbareVedleggQuery.data ?? []).map((vedlegg) => ({
      id: vedlegg.vedleggId,
      label: vedlegg.tittel,
      type: "redigerbartVedlegg",
      locked: false,
    }));

    const alltidValgbareTabs: DokumentTab[] = (brev.valgteVedlegg ?? []).map((vedlegg) => ({
      id: vedlegg.kode,
      label: vedlegg.visningstekst,
      type: "alltidValgbartVedlegg",
      locked: true,
    }));

    const p1Tab: DokumentTab[] =
      brev.info.brevkode === P1_BREVKODE ? [{ id: P1_TAB_ID, label: "P1", type: "p1", locked: false }] : [];

    return [brevTab, ...redigerbareTabs, ...alltidValgbareTabs, ...p1Tab];
  }, [brev.info.brevtittel, brev.info.brevkode, brev.valgteVedlegg, redigerbareVedleggQuery.data]);

  return { tabs };
}
