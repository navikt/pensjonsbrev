import { createContext, type ReactNode, useCallback, useContext, useMemo, useRef } from "react";

import { type Redigeringsflate } from "~/utils/editorTracking";

/**
 * Which document the editor surface is currently showing. The brev is the default; a redigerbart
 * vedlegg is identified by its vedleggId.
 */
export type AktivtDokument = { type: "brev" } | { type: "vedlegg"; vedleggId: string };

type AktivtDokumentContextValue = {
  aktivtDokument: AktivtDokument;
  redigeringsflate: Redigeringsflate;
  /** Attestanten skal godkjenne brevet, ikke forkaste saksbehandlerens arbeid. */
  kanTilbakestille: boolean;
  velgBrev: () => Promise<boolean>;
  velgVedlegg: (vedleggId: string) => Promise<boolean>;
  tilbakestillAktivtVedlegg: () => void;
  registrerTilbakestilling: (tilbakestill: (() => void) | null) => void;
  registrerAntallUhaandterteAvsnitt: (tell: (() => number) | null) => void;
  /**
   * The mounted document editor registers how to persist its unsaved edits, so the page can await
   * that before it submits the brev and releases the reservation. Pass null on unmount.
   */
  registrerLagring: (lagreNaa: (() => Promise<void>) | null) => void;
};

const AktivtDokumentContext = createContext<AktivtDokumentContextValue | null>(null);

/**
 * Owns "which document is being edited" for the brev editor page. It is route-agnostic: the route
 * supplies the current vedleggId (from the `?vedlegg=` search param) and the navigation callback,
 * so the URL stays the single source of truth and the selection survives reload and back/forward.
 */
export const AktivtDokumentProvider = (props: {
  aktivVedleggId: string | undefined;
  redigeringsflate: Redigeringsflate;
  onVelgDokument: (vedleggId: string | undefined) => Promise<boolean>;
  registrerLagring: (lagreNaa: (() => Promise<void>) | null) => void;
  registrerAntallUhaandterteAvsnitt: (tell: (() => number) | null) => void;
  children: ReactNode;
}) => {
  const { aktivVedleggId, redigeringsflate, onVelgDokument, registrerLagring, registrerAntallUhaandterteAvsnitt } =
    props;
  const tilbakestillAktivtVedleggRef = useRef<(() => void) | null>(null);

  const velgBrev = useCallback(() => onVelgDokument(undefined), [onVelgDokument]);
  const velgVedlegg = useCallback((vedleggId: string) => onVelgDokument(vedleggId), [onVelgDokument]);
  const tilbakestillAktivtVedlegg = useCallback(() => tilbakestillAktivtVedleggRef.current?.(), []);
  const registrerTilbakestilling = useCallback((tilbakestill: (() => void) | null) => {
    tilbakestillAktivtVedleggRef.current = tilbakestill;
  }, []);

  const value = useMemo<AktivtDokumentContextValue>(
    () => ({
      aktivtDokument: aktivVedleggId === undefined ? { type: "brev" } : { type: "vedlegg", vedleggId: aktivVedleggId },
      redigeringsflate: redigeringsflate,
      kanTilbakestille: redigeringsflate === "saksbehandler-redigering",
      velgBrev: velgBrev,
      velgVedlegg: velgVedlegg,
      tilbakestillAktivtVedlegg: tilbakestillAktivtVedlegg,
      registrerTilbakestilling: registrerTilbakestilling,
      registrerLagring: registrerLagring,
      registrerAntallUhaandterteAvsnitt: registrerAntallUhaandterteAvsnitt,
    }),
    [
      aktivVedleggId,
      redigeringsflate,
      velgBrev,
      velgVedlegg,
      tilbakestillAktivtVedlegg,
      registrerTilbakestilling,
      registrerLagring,
      registrerAntallUhaandterteAvsnitt,
    ],
  );

  return <AktivtDokumentContext.Provider value={value}>{props.children}</AktivtDokumentContext.Provider>;
};

export const useAktivtDokument = (): AktivtDokumentContextValue => {
  const context = useContext(AktivtDokumentContext);
  if (!context) {
    throw new Error("useAktivtDokument must be used within an <AktivtDokumentProvider>");
  }
  return context;
};
