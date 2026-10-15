import { useQuery } from "@tanstack/react-query";
import { useCallback, useRef, useState } from "react";

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
  const [isDiffMode, setDiffMode] = useState(false);
  // Captures the diff state as it was when the page first loaded, for tracking purposes.
  const defaultDiffMode = useRef(isDiffMode).current;

  // Diff decorations and editing are mutually exclusive, so editing disables diff mode.
  const disableDiffMode = useCallback(() => setDiffMode(false), []);

  const diffQuery = useQuery({
    queryKey: getBrevDiff.queryKey(brevId, savedHash),
    queryFn: async () => ({
      value: await getBrevDiff.queryFn(brevId, savedLetter),
      redigertBrevHash: savedHash,
    }),
    // `redigertBrev` and `redigertBrevHash` are updated together from the same save response,
    // so they only represent the same letter version while the editor is in a saved state.
    enabled: isDiffMode && isSaved,
  });

  const activeDiff = pickValueForCurrentHash(diffQuery.isSuccess ? diffQuery.data : undefined, savedHash);

  const diffIsEmpty =
    activeDiff !== undefined &&
    Object.keys(activeDiff.editedBlocks).length === 0 &&
    Object.keys(activeDiff.deletedBlocks).length === 0;

  let status: AttestantLetterDiffStatus = "disabled";
  if (isDiffMode) {
    // An unsaved letter has no backend version to diff against yet, so the caller is expected to be
    // saving; report progress rather than a stale error or an already-superseded diff.
    if (!isSaved) status = "loading";
    else if (diffQuery.isError) status = "error";
    else if (diffIsEmpty) status = "empty";
    else if (activeDiff) status = "ready";
    else status = "loading";
  }

  // The cached diff belongs to `savedHash`, which does not change until a save responds. Rendering it
  // while the letter is dirty would decorate text the attestant has already edited past.
  const renderMarkers = isDiffMode && isSaved && activeDiff !== undefined;

  return {
    isDiffMode,
    setDiffMode,
    defaultDiffMode,
    disableDiffMode,
    status,
    activeDiff: renderMarkers ? activeDiff : undefined,
    diffHash: renderMarkers ? savedHash : undefined,
    error: diffQuery.error,
  };
}
