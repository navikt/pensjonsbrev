import { createContext, type ReactNode, useCallback, useContext, useMemo, useRef } from "react";

import { type Redigeringsflate } from "~/utils/editorTracking";

/**
 * Which document the editor surface is currently showing. The letter is the default;
 * an editable attachment is identified by its vedleggId.
 */
export type AktivtDokument = { type: "brev" } | { type: "vedlegg"; vedleggId: string };

type BrevOgVedleggEditorContextValue = {
  aktivtDokument: AktivtDokument;
  redigeringsflate: Redigeringsflate;
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

const BrevOgVedleggEditorContext = createContext<BrevOgVedleggEditorContextValue | null>(null);

/**
 * Coordinates switching between the brev and its editable vedlegg. The route owns aktivVedleggId,
 * keeping the URL as the source of truth across reload and browser navigation.
 */
export const BrevOgVedleggEditorProvider = (props: {
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

  const value = useMemo<BrevOgVedleggEditorContextValue>(
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

  return <BrevOgVedleggEditorContext.Provider value={value}>{props.children}</BrevOgVedleggEditorContext.Provider>;
};

export const useBrevOgVedleggEditor = (): BrevOgVedleggEditorContextValue => {
  const context = useContext(BrevOgVedleggEditorContext);
  if (!context) {
    throw new Error("useBrevOgVedleggEditor must be used within a <BrevOgVedleggEditorProvider>");
  }
  return context;
};
