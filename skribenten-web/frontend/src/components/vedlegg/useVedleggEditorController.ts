import { useCallback, useEffect, useRef, useState } from "react";

import { useRedigerbareVedlegg } from "~/components/vedlegg/useRedigerbareVedlegg";

export const useVedleggEditorController = (args: {
  saksId: string;
  brevId: number;
  aktivVedleggId: string | undefined;
  navigateToDocument: (vedleggId: string | undefined) => Promise<void>;
}) => {
  const { saksId, brevId, aktivVedleggId, navigateToDocument } = args;
  const redigerbareVedleggQuery = useRedigerbareVedlegg({ saksId, brevId });
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
