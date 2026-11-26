import { useQueryClient } from "@tanstack/react-query";
import { useCallback, useRef } from "react";

import { getRedigerbartVedlegg } from "~/api/redigerbareVedlegg-endpoints";
import { countMissingInRenderedBlocks } from "~/Brevredigering/LetterEditor/actions/common";
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
  const missingInRenderedCounts = useRef<Record<string, number>>({});

  const registerVedleggMissingInRendered = useCallback((vedleggId: string, count: number | null) => {
    if (count === null) {
      delete missingInRenderedCounts.current[vedleggId];
    } else {
      missingInRenderedCounts.current[vedleggId] = count;
    }
  }, []);

  const getSavedMissingInRenderedCount = async (vedleggId: string): Promise<number> => {
    try {
      const content = await queryClient.fetchQuery({
        ...getRedigerbartVedlegg(saksId, brevId, vedleggId, redigeringsflate),
        staleTime: Number.POSITIVE_INFINITY,
      });
      return countMissingInRenderedBlocks(content);
    } catch (error) {
      logError(error, undefined).catch(() => console.error("Unable to log error message"));
      return 0;
    }
  };

  const getMissingInRenderedCount = async (): Promise<number> => {
    if (redigeringsflate !== "saksbehandler-redigering") return 0;

    let total = 0;
    for (const { vedleggId } of vedlegg ?? []) {
      total += missingInRenderedCounts.current[vedleggId] ?? (await getSavedMissingInRenderedCount(vedleggId));
    }
    return total;
  };

  return { getMissingInRenderedCount, registerVedleggMissingInRendered };
}
