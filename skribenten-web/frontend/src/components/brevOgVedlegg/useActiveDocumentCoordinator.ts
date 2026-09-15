import { useCallback, useEffect, useRef, useState } from "react";

import { type Redigeringsflate } from "~/Brevredigering/LetterEditor/RedigeringsflateContext";
import { useRedigerbareVedlegg } from "~/components/vedlegg/useRedigerbareVedlegg";

/**
 * Handles document switching and ensures the active vedlegg is saved before navigation.
 */
export const useActiveDocumentCoordinator = (args: {
  saksId: string;
  brevId: number;
  activeVedleggId: string | undefined;
  redigeringsflate: Redigeringsflate;
  navigateToDocument: (vedleggId: string | undefined) => Promise<void>;
}) => {
  const { saksId, brevId, activeVedleggId, redigeringsflate, navigateToDocument } = args;
  const redigerbareVedleggQuery = useRedigerbareVedlegg({ saksId, brevId, redigeringsflate });
  const activeVedleggSaveRef = useRef<(() => Promise<void>) | null>(null);
  const [savingActiveDocument, setSavingActiveDocument] = useState(false);

  const registerVedleggSave = useCallback((saveNow: (() => Promise<void>) | null) => {
    activeVedleggSaveRef.current = saveNow;
  }, []);

  const saveActiveDocument = useCallback(async (): Promise<boolean> => {
    setSavingActiveDocument(true);
    try {
      await activeVedleggSaveRef.current?.();
      return true;
    } catch {
      return false;
    } finally {
      setSavingActiveDocument(false);
    }
  }, []);

  const selectDocument = useCallback(
    async (vedleggId: string | undefined): Promise<boolean> => {
      if (activeVedleggId !== undefined && vedleggId !== activeVedleggId && !(await saveActiveDocument())) {
        return false;
      }
      await navigateToDocument(vedleggId);
      return true;
    },
    [activeVedleggId, saveActiveDocument, navigateToDocument],
  );

  const vedleggExists =
    activeVedleggId === undefined ||
    (redigerbareVedleggQuery.data?.some((vedlegg) => vedlegg.vedleggId === activeVedleggId) ?? true);

  useEffect(() => {
    if (!vedleggExists) {
      void selectDocument(undefined);
    }
  }, [vedleggExists, selectDocument]);

  return {
    activeVedleggId: vedleggExists ? activeVedleggId : undefined,
    saveActiveDocument,
    savingActiveDocument,
    registerVedleggSave,
    selectDocument,
  };
};
