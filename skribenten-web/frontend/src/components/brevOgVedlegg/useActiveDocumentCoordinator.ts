import { useCallback, useEffect, useRef, useState } from "react";

import { type Redigeringsflate } from "~/Brevredigering/LetterEditor/RedigeringsflateContext";
import { useVedleggEditorWarnings } from "~/components/brevOgVedlegg/useVedleggEditorWarnings";
import { useManagedLetterEditorContext } from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";
import { useRedigerbareVedlegg } from "~/components/vedlegg/useRedigerbareVedlegg";

/**
 * Handles document switching and ensures the active vedlegg is saved before navigation.
 */
type DocumentCoordinatorArgs = {
  saksId: string;
  brevId: number;
  activeVedleggId: string | undefined;
  redigeringsflate: Redigeringsflate;
  navigateToDocument: (vedleggId: string | undefined) => Promise<void>;
  fetchSavedMissingFromTemplateCount?: (vedleggId: string) => Promise<number>;
};

export const useActiveDocumentCoordinator = (args: DocumentCoordinatorArgs) => {
  const { savePendingChanges } = useManagedLetterEditorContext();
  return useDocumentCoordinator({ ...args, savePendingChanges });
};

export const useDocumentCoordinator = (args: DocumentCoordinatorArgs & { savePendingChanges: () => Promise<void> }) => {
  const {
    saksId,
    brevId,
    activeVedleggId,
    redigeringsflate,
    navigateToDocument,
    savePendingChanges,
    fetchSavedMissingFromTemplateCount,
  } = args;
  const redigerbareVedleggQuery = useRedigerbareVedlegg({ saksId, brevId, redigeringsflate });
  const activeVedleggSaveRef = useRef<(() => Promise<void>) | null>(null);
  const [savingPendingDocuments, setSavingPendingDocuments] = useState(false);
  const { getMissingFromTemplateCount, registerVedleggMissingFromTemplate } = useVedleggEditorWarnings({
    saksId,
    brevId,
    redigeringsflate,
    vedlegg: redigerbareVedleggQuery.data,
    fetchSavedMissingFromTemplateCount,
  });

  const registerVedleggSave = useCallback((savePendingChanges: (() => Promise<void>) | null) => {
    activeVedleggSaveRef.current = savePendingChanges;
  }, []);

  const savePendingDocuments = useCallback(async (): Promise<boolean> => {
    setSavingPendingDocuments(true);
    try {
      try {
        await activeVedleggSaveRef.current?.();
      } finally {
        await savePendingChanges();
      }
      return true;
    } catch {
      return false;
    } finally {
      setSavingPendingDocuments(false);
    }
  }, [savePendingChanges]);

  const selectDocument = useCallback(
    async (vedleggId: string | undefined): Promise<boolean> => {
      if (vedleggId !== activeVedleggId && !(await savePendingDocuments())) {
        return false;
      }
      await navigateToDocument(vedleggId);
      return true;
    },
    [activeVedleggId, savePendingDocuments, navigateToDocument],
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
    savePendingDocuments,
    savingPendingDocuments,
    registerVedleggSave,
    getMissingFromTemplateCount,
    registerVedleggMissingFromTemplate,
    selectDocument,
  };
};
