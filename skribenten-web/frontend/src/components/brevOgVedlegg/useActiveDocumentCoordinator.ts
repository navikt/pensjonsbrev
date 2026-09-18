import { useQueryClient } from "@tanstack/react-query";
import { useCallback, useEffect, useRef, useState } from "react";

import { getRedigerbartVedlegg } from "~/api/redigerbareVedlegg-endpoints";
import { countMissingFromTemplateBlocks } from "~/Brevredigering/LetterEditor/actions/common";
import { type Redigeringsflate } from "~/Brevredigering/LetterEditor/RedigeringsflateContext";
import { useRedigerbareVedlegg } from "~/components/vedlegg/useRedigerbareVedlegg";
import { logError } from "~/utils/logger";

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
  const queryClient = useQueryClient();
  const redigerbareVedleggQuery = useRedigerbareVedlegg({ saksId, brevId, redigeringsflate });
  const activeVedleggSaveRef = useRef<(() => Promise<void>) | null>(null);
  const [savingActiveDocument, setSavingActiveDocument] = useState(false);
  const missingFromTemplateCounts = useRef<Record<string, number>>({});

  const registerVedleggMissingFromTemplate = useCallback((vedleggId: string, count: number) => {
    missingFromTemplateCounts.current[vedleggId] = count;
  }, []);

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

  const getMissingFromTemplateCount = async (): Promise<number> => {
    if (redigeringsflate !== "saksbehandler-redigering") return 0;

    let total = 0;
    for (const vedlegg of redigerbareVedleggQuery.data ?? []) {
      const localCount = missingFromTemplateCounts.current[vedlegg.vedleggId];
      if (localCount !== undefined) {
        // The attachment is (or has been) opened in this session: the local count is freshest.
        total += localCount;
        continue;
      }
      // Attachments never opened must be counted from the server content. The fetch is cached,
      // so it only happens for attachments whose content we do not already have.
      try {
        const content = await queryClient.fetchQuery({
          queryKey: getRedigerbartVedlegg.queryKey(brevId, vedlegg.vedleggId, redigeringsflate),
          queryFn: ({ signal }) =>
            getRedigerbartVedlegg.queryFn(saksId, brevId, vedlegg.vedleggId, redigeringsflate, signal),
          staleTime: Number.POSITIVE_INFINITY,
        });
        total += countMissingFromTemplateBlocks(content);
      } catch (error) {
        // A failed fetch must not block submission — the warning is only a help feature.
        logError(error, undefined).catch(() => console.error("Unable to log error message"));
      }
    }
    return total;
  };

  return {
    activeVedleggId: vedleggExists ? activeVedleggId : undefined,
    saveActiveDocument,
    savingActiveDocument,
    registerVedleggSave,
    registerVedleggMissingFromTemplate,
    getMissingFromTemplateCount,
    selectDocument,
  };
};
