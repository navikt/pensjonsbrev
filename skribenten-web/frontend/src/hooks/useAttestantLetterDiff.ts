import { useQuery } from "@tanstack/react-query";
import { useCallback, useState } from "react";

import { getBrevDiff } from "~/api/brev-queries";
import { pickValueForCurrentHash } from "~/Brevredigering/LetterEditor/diff/diffQueryState";
import { type EditedLetter } from "~/types/brevbakerTypes";

type AttestantLetterDiffStatus = "disabled" | "loading" | "ready" | "empty" | "error";

export function useAttestantLetterDiff({
  brevId,
  savedLetter,
  savedHash,
  isSaved,
}: {
  brevId: number;
  savedLetter: EditedLetter;
  savedHash: string;
  isSaved: boolean;
}) {
  const [enabled, setEnabled] = useState(false);

  // Attestanten kan ikke redigere og se markeringer samtidig: første redigering slår av markeringen.
  const disableDiff = useCallback(() => setEnabled(false), []);

  const diffQuery = useQuery({
    queryKey: getBrevDiff.queryKey(brevId, savedHash),
    queryFn: async () => ({
      value: await getBrevDiff.queryFn(brevId, savedLetter),
      redigertBrevHash: savedHash,
    }),
    // redigertBrev og redigertBrevHash settes sammen fra samme lagringsrespons, så de hører bare sammen når brevet er lagret.
    enabled: enabled && isSaved,
  });

  const activeDiff = pickValueForCurrentHash(diffQuery.isSuccess ? diffQuery.data : undefined, savedHash);

  const diffIsEmpty =
    activeDiff !== undefined &&
    Object.keys(activeDiff.editedBlocks).length === 0 &&
    Object.keys(activeDiff.deletedBlocks).length === 0;

  let status: AttestantLetterDiffStatus = "disabled";
  if (enabled) {
    if (diffQuery.isError) status = "error";
    else if (diffIsEmpty) status = "empty";
    else if (activeDiff) status = "ready";
    else status = "loading";
  }

  const renderMarkers = enabled && activeDiff !== undefined;

  return {
    enabled,
    setEnabled,
    disableDiff,
    status,
    activeDiff: renderMarkers ? activeDiff : undefined,
    diffHash: renderMarkers ? savedHash : undefined,
    error: diffQuery.error,
  };
}
