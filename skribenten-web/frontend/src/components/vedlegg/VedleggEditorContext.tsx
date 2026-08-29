import { createContext, type ReactNode, useCallback, useContext, useMemo, useRef } from "react";

import { type Redigeringsflate } from "~/utils/editorTracking";

/**
 * Which document the editor surface is currently showing. The brev is the default; a redigerbart
 * vedlegg is identified by its vedleggId.
 */
export type AktivtDokument = { type: "brev" } | { type: "vedlegg"; vedleggId: string };

type VedleggEditorContextValue = {
  aktivtDokument: AktivtDokument;
  redigeringsflate: Redigeringsflate;
  /** Attestanten skal godkjenne brevet, ikke forkaste saksbehandlerens arbeid. */
  kanTilbakestille: boolean;
  velgBrev: () => Promise<boolean>;
  velgVedlegg: (vedleggId: string) => Promise<boolean>;
  tilbakestillAktivtVedlegg: () => void;
  registrerTilbakestilling: (tilbakestill: (() => void) | null) => void;
  /**
   * The active vedlegg editor registers its save function so navigation and submission can wait
   * for unsaved changes before leaving the editing session.
   */
  registrerVedleggslagring: (lagreNaa: (() => Promise<void>) | null) => void;
};

const VedleggEditorContext = createContext<VedleggEditorContextValue | null>(null);

/**
 * Coordinates switching between the brev and its editable vedlegg. The route owns aktivVedleggId,
 * keeping the URL as the source of truth across reload and browser navigation.
 */
export const VedleggEditorProvider = (props: {
  aktivVedleggId: string | undefined;
  redigeringsflate: Redigeringsflate;
  onVelgDokument: (vedleggId: string | undefined) => Promise<boolean>;
  registrerVedleggslagring: (lagreNaa: (() => Promise<void>) | null) => void;
  children: ReactNode;
}) => {
  const { aktivVedleggId, redigeringsflate, onVelgDokument, registrerVedleggslagring } = props;
  const tilbakestillAktivtVedleggRef = useRef<(() => void) | null>(null);

  const velgBrev = useCallback(() => onVelgDokument(undefined), [onVelgDokument]);
  const velgVedlegg = useCallback((vedleggId: string) => onVelgDokument(vedleggId), [onVelgDokument]);
  const tilbakestillAktivtVedlegg = useCallback(() => tilbakestillAktivtVedleggRef.current?.(), []);
  const registrerTilbakestilling = useCallback((tilbakestill: (() => void) | null) => {
    tilbakestillAktivtVedleggRef.current = tilbakestill;
  }, []);

  const value = useMemo<VedleggEditorContextValue>(
    () => ({
      aktivtDokument: aktivVedleggId === undefined ? { type: "brev" } : { type: "vedlegg", vedleggId: aktivVedleggId },
      redigeringsflate: redigeringsflate,
      kanTilbakestille: redigeringsflate === "saksbehandler-redigering",
      velgBrev: velgBrev,
      velgVedlegg: velgVedlegg,
      tilbakestillAktivtVedlegg: tilbakestillAktivtVedlegg,
      registrerTilbakestilling: registrerTilbakestilling,
      registrerVedleggslagring: registrerVedleggslagring,
    }),
    [
      aktivVedleggId,
      redigeringsflate,
      velgBrev,
      velgVedlegg,
      tilbakestillAktivtVedlegg,
      registrerTilbakestilling,
      registrerVedleggslagring,
    ],
  );

  return <VedleggEditorContext.Provider value={value}>{props.children}</VedleggEditorContext.Provider>;
};

export const useVedleggEditor = (): VedleggEditorContextValue => {
  const context = useContext(VedleggEditorContext);
  if (!context) {
    throw new Error("useVedleggEditor must be used within a <VedleggEditorProvider>");
  }
  return context;
};
