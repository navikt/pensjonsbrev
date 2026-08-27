import { useCallback, useEffect, useRef, useState } from "react";

import { useRedigerbareVedlegg } from "~/components/vedlegg/useRedigerbareVedlegg";

export const useDokumentEditorController = (args: {
  saksId: string;
  brevId: number;
  aktivVedleggId: string | undefined;
  navigateToDocument: (vedleggId: string | undefined) => Promise<void>;
}) => {
  const { saksId, brevId, aktivVedleggId, navigateToDocument } = args;
  const redigerbareVedleggQuery = useRedigerbareVedlegg({ saksId, brevId });
  const lagreAktivtDokumentRef = useRef<(() => Promise<void>) | null>(null);
  const tellUhaandterteAvsnittRef = useRef<(() => number) | null>(null);
  const [lagrerAktivtDokument, setLagrerAktivtDokument] = useState(false);

  const registrerLagring = useCallback((lagreNaa: (() => Promise<void>) | null) => {
    lagreAktivtDokumentRef.current = lagreNaa;
  }, []);

  const registrerAntallUhaandterteAvsnitt = useCallback((tell: (() => number) | null) => {
    tellUhaandterteAvsnittRef.current = tell;
  }, []);

  const getAktivtDokumentWarning = useCallback(() => {
    const count = tellUhaandterteAvsnittRef.current?.() ?? 0;
    return count > 0 ? ({ kind: "avsnittIkkeIMalIVedlegg", count } as const) : null;
  }, []);

  const lagreAktivtDokument = useCallback(async (): Promise<boolean> => {
    setLagrerAktivtDokument(true);
    try {
      await lagreAktivtDokumentRef.current?.();
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
    getAktivtDokumentWarning,
    registrerAntallUhaandterteAvsnitt,
    registrerLagring,
    velgDokument,
  };
};
