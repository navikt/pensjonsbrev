import { useQueryClient } from "@tanstack/react-query";
import { useCallback, useRef } from "react";

import { getRedigerbartVedlegg } from "~/api/redigerbareVedlegg-endpoints";
import { countMissingFromTemplateBlocks } from "~/Brevredigering/LetterEditor/actions/common";
import { type Redigeringsflate } from "~/Brevredigering/LetterEditor/RedigeringsflateContext";
import { type RedigerbartVedleggInfo } from "~/types/brev";
import { logError } from "~/utils/logger";

export function useVedleggEditorWarnings({
  saksId,
  brevId,
  redigeringsflate,
  vedlegg,
}: {
  saksId: string;
  brevId: number;
  redigeringsflate: Redigeringsflate;
  vedlegg: RedigerbartVedleggInfo[] | undefined;
}) {
  const queryClient = useQueryClient();
  const missingFromTemplateCounts = useRef<Record<string, number>>({});

  const registerVedleggMissingFromTemplate = useCallback((vedleggId: string, count: number) => {
    missingFromTemplateCounts.current[vedleggId] = count;
  }, []);

  const getSavedMissingFromTemplateCount = async (vedleggId: string): Promise<number> => {
    try {
      const content = await queryClient.fetchQuery({
        ...getRedigerbartVedlegg(saksId, brevId, vedleggId, redigeringsflate),
        staleTime: Number.POSITIVE_INFINITY,
      });
      return countMissingFromTemplateBlocks(content);
    } catch (error) {
      logError(error, undefined).catch(() => console.error("Unable to log error message"));
      return 0;
    }
  };

  const getMissingFromTemplateCount = async (): Promise<number> => {
    if (redigeringsflate !== "saksbehandler-redigering") return 0;

    let total = 0;
    for (const { vedleggId } of vedlegg ?? []) {
      total += missingFromTemplateCounts.current[vedleggId] ?? (await getSavedMissingFromTemplateCount(vedleggId));
    }
    return total;
  };

  return { getMissingFromTemplateCount, registerVedleggMissingFromTemplate };
}
