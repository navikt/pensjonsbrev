import { useQuery } from "@tanstack/react-query";
import { useCallback, useRef, useState } from "react";

import { getBrevDiffV2 } from "~/api/brevV2-queries";
import { pickValueForCurrentHash } from "~/Brevredigering/LetterEditor/diff/diffQueryState";
import { type AttestantLetterDiffStatus, type DiffToggles } from "~/hooks/useAttestantLetterDiff";
import { type DiffResponseV2, type LetterV2, type UnifiedDiffV2 } from "~/types/brevV2";

import { isEmptyDiff } from "./diffModel";

const ALL_OFF: DiffToggles = { inserted: false, deleted: false };

function unified(response: DiffResponseV2): UnifiedDiffV2 {
  if (response.type === "UNIFIED") return response;
  throw new Error(`Forventet UNIFIED diff, fikk ${response.type}`);
}

export function useAttestantLetterDiffV2({
  saksId,
  brevId,
  savedLetter,
  savedHash,
  isSaved,
}: {
  saksId: string;
  brevId: number;
  savedLetter: LetterV2;
  savedHash: string;
  isSaved: boolean;
}) {
  const [toggles, setToggles] = useState<DiffToggles>(ALL_OFF);
  const showInsertions = toggles.inserted;
  const showDeletions = toggles.deleted;
  const isDiffMode = showInsertions || showDeletions;
  const defaultDiffMode = useRef(isDiffMode).current;

  const setToggle = useCallback(
    (toggle: keyof DiffToggles, checked: boolean) => setToggles((current) => ({ ...current, [toggle]: checked })),
    [],
  );

  const disableDiffMode = useCallback(() => setToggles(ALL_OFF), []);

  const diffRequest = getBrevDiffV2(saksId, brevId, savedHash, savedLetter);
  const diffQuery = useQuery({
    queryKey: diffRequest.queryKey,
    queryFn: async () => ({ value: unified(await diffRequest.queryFn()), redigertBrevHash: savedHash }),
    enabled: isDiffMode && isSaved,
  });

  const activeDiff = pickValueForCurrentHash(diffQuery.isSuccess ? diffQuery.data : undefined, savedHash);

  const status = ((): AttestantLetterDiffStatus => {
    if (!isDiffMode) return "disabled";
    if (!isSaved) return "loading";
    if (diffQuery.isError) return "error";
    if (!activeDiff) return "loading";
    return isEmptyDiff(activeDiff) ? "empty" : "ready";
  })();

  const renderMarkers = isDiffMode && isSaved && activeDiff !== undefined;

  return {
    toggles,
    setToggle,
    isDiffMode,
    showInsertions,
    showDeletions,
    defaultDiffMode,
    disableDiffMode,
    status,
    activeDiff: renderMarkers ? activeDiff : undefined,
    diffHash: renderMarkers ? savedHash : undefined,
    error: diffQuery.error,
  };
}
