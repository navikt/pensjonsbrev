import { useCallback, useEffect, useRef, useState } from "react";

import { useRedigerbareVedlegg } from "~/components/vedlegg/useRedigerbareVedlegg";
import { type Redigeringsflate } from "~/utils/editorTracking";

/**
 * Handles document switching and ensures the active vedlegg is saved before navigation.
 */
export const useAktivtDokumentController = (args: {
  saksId: string;
  brevId: number;
  aktivVedleggId: string | undefined;
  redigeringsflate: Redigeringsflate;
  navigateToDocument: (vedleggId: string | undefined) => Promise<void>;
}) => {
  const { saksId, brevId, aktivVedleggId, redigeringsflate, navigateToDocument } = args;
  const redigerbareVedleggQuery = useRedigerbareVedlegg({ saksId, brevId, redigeringsflate });
  const lagreAktivtVedleggRef = useRef<(() => Promise<void>) | null>(null);
  const [lagrerAktivtDokument, setLagrerAktivtDokument] = useState(false);

  const registrerVedleggslagring = useCallback((lagreNaa: (() => Promise<void>) | null) => {
    lagreAktivtVedleggRef.current = lagreNaa;
  }, []);

  const lagreAktivtDokument = useCallback(async (): Promise<boolean> => {
    setLagrerAktivtDokument(true);
    try {
      await lagreAktivtVedleggRef.current?.();
      return true;
    } catch {
      return false;
    } finally {
      setLagrerAktivtDokument(false);
    }
  }, []);

  const velgDokument = useCallback(
    async (vedleggId: string | undefined): Promise<boolean> => {
      if (aktivVedleggId !== undefined && vedleggId !== aktivVedleggId && !(await lagreAktivtDokument())) {
        return false;
      }
      await navigateToDocument(vedleggId);
      return true;
    },
    [aktivVedleggId, lagreAktivtDokument, navigateToDocument],
  );

  const vedleggFinnes =
    aktivVedleggId === undefined ||
    (redigerbareVedleggQuery.data?.some((vedlegg) => vedlegg.vedleggId === aktivVedleggId) ?? true);

  useEffect(() => {
    if (!vedleggFinnes) {
      void velgDokument(undefined);
    }
  }, [vedleggFinnes, velgDokument]);

  return {
    aktivVedleggId: vedleggFinnes ? aktivVedleggId : undefined,
    lagreAktivtDokument,
    lagrerAktivtDokument,
    registrerVedleggslagring,
    velgDokument,
  };
};
