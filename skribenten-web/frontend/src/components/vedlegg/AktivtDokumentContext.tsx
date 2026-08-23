import { createContext, type ReactNode, useCallback, useContext, useMemo } from "react";

/**
 * Which document the editor surface is currently showing. The brev is the default; a redigerbart
 * vedlegg is identified by its vedleggId.
 */
export type AktivtDokument = { type: "brev" } | { type: "vedlegg"; vedleggId: string };

type AktivtDokumentContextValue = {
  aktivtDokument: AktivtDokument;
  velgBrev: () => void;
  velgVedlegg: (vedleggId: string) => void;
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
  onVelgDokument: (vedleggId: string | undefined) => void;
  registrerLagring: (lagreNaa: (() => Promise<void>) | null) => void;
  children: ReactNode;
}) => {
  const { aktivVedleggId, onVelgDokument, registrerLagring } = props;

  const velgBrev = useCallback(() => onVelgDokument(undefined), [onVelgDokument]);
  const velgVedlegg = useCallback((vedleggId: string) => onVelgDokument(vedleggId), [onVelgDokument]);

  const value = useMemo<AktivtDokumentContextValue>(
    () => ({
      aktivtDokument: aktivVedleggId === undefined ? { type: "brev" } : { type: "vedlegg", vedleggId: aktivVedleggId },
      velgBrev: velgBrev,
      velgVedlegg: velgVedlegg,
      registrerLagring: registrerLagring,
    }),
    [aktivVedleggId, velgBrev, velgVedlegg, registrerLagring],
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
