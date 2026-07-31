import { useQuery } from "@tanstack/react-query";
import { useCallback, useEffect, useRef, useState } from "react";

import { getBrevDiff } from "~/api/brev-queries";
import { getSnapshotForHash, pickValueForCurrentHash } from "~/Brevredigering/LetterEditor/diff/diffQueryState";
import { type EditedLetter } from "~/types/brevbakerTypes";

const MAX_HASH_CACHE_SIZE = 20;

export type AttestantLetterDiffStatus = "disabled" | "loading" | "ready" | "empty" | "error" | "unsupported";

export function useAttestantLetterDiff({
  brevId,
  initialSavedHash,
  initialSavedLetter,
  savedHash,
}: {
  brevId: number;
  initialSavedHash: string;
  initialSavedLetter: EditedLetter;
  savedHash: string;
}) {
  const [enabled, setEnabled] = useState(false);
  const savedLettersByHashRef = useRef<Map<string, EditedLetter>>(new Map([[initialSavedHash, initialSavedLetter]]));
  const [rejectedDiffs, setRejectedDiffs] = useState<Map<string, { hash: string; reason: string }>>(() => new Map());

  const rememberSavedLetter = useCallback((hash: string, letter: EditedLetter) => {
    const snapshots = savedLettersByHashRef.current;
    snapshots.set(hash, letter);
    while (snapshots.size > MAX_HASH_CACHE_SIZE) {
      const oldest = snapshots.keys().next().value;
      if (!oldest || oldest === hash) break;
      snapshots.delete(oldest);
    }
  }, []);

  useEffect(() => {
    rememberSavedLetter(initialSavedHash, initialSavedLetter);
  }, [initialSavedHash, initialSavedLetter, rememberSavedLetter]);

  // Attestanten kan ikke redigere og se markeringer samtidig: første redigering slår av markeringen.
  const disableDiff = useCallback(() => setEnabled(false), []);

  const reportRejectedLiteral = useCallback((key: string, hash: string, reason: string | null) => {
    setRejectedDiffs((current) => {
      const existing = current.get(key);
      if (reason === null) {
        if (!existing || existing.hash !== hash) return current;
        const next = new Map(current);
        next.delete(key);
        return next;
      }
      if (existing?.hash === hash && existing.reason === reason) return current;
      const next = new Map(current);
      next.set(key, { hash, reason });
      return next;
    });
  }, []);

  useEffect(() => {
    setRejectedDiffs((current) => {
      if (current.size === 0) return current;
      const next = new Map([...current].filter(([, rejected]) => rejected.hash === savedHash));
      return next.size === current.size ? current : next;
    });
  }, [savedHash]);

  const savedLetter = getSnapshotForHash(savedLettersByHashRef.current, savedHash);
  const diffQuery = useQuery({
    queryKey: getBrevDiff.queryKey(brevId, savedHash),
    queryFn: async () => {
      const snapshot = getSnapshotForHash(savedLettersByHashRef.current, savedHash);
      if (!snapshot) throw new Error(`Mangler lagret brevsnapshot for hash ${savedHash}`);
      return { value: await getBrevDiff.queryFn(brevId, snapshot), redigertBrevHash: savedHash };
    },
    enabled: enabled && savedLetter !== undefined,
  });

  const activeDiff = pickValueForCurrentHash(diffQuery.isSuccess ? diffQuery.data : undefined, savedHash);

  const rejectedReasons = [...rejectedDiffs.values()]
    .filter((rejected) => rejected.hash === savedHash)
    .map((rejected) => rejected.reason);
  const diffIsEmpty =
    activeDiff !== undefined &&
    Object.keys(activeDiff.editedBlocks).length === 0 &&
    Object.keys(activeDiff.deletedBlocks).length === 0;

  let status: AttestantLetterDiffStatus = "disabled";
  if (enabled) {
    if (savedLetter === undefined || diffQuery.isError) status = "error";
    else if (diffQuery.isPending) status = "loading";
    else if (rejectedReasons.length > 0) status = "unsupported";
    else if (diffIsEmpty) status = "empty";
    else if (activeDiff) status = "ready";
    else status = "loading";
  }

  const renderMarkers = enabled && activeDiff !== undefined && rejectedReasons.length === 0;

  return {
    enabled,
    setEnabled,
    disableDiff,
    status,
    activeDiff: renderMarkers ? activeDiff : undefined,
    diffHash: renderMarkers ? savedHash : undefined,
    reportRejectedLiteral,
    rejectedReasons,
    error: savedLetter === undefined ? new Error("Mangler lagret brevsnapshot") : diffQuery.error,
    rememberSavedLetter,
  };
}
