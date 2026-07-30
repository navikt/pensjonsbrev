import { useQuery } from "@tanstack/react-query";
import { useCallback, useEffect, useMemo, useRef, useState } from "react";

import { getBrevDiff } from "~/api/brev-queries";
import {
  getSnapshotForHash,
  letterStructureSignature,
  pickValueForCurrentHash,
  shouldRenderDiffMarkers,
} from "~/Brevredigering/LetterEditor/diff/diffQueryState";
import { type EditedLetter } from "~/types/brevbakerTypes";

const MAX_HASH_CACHE_SIZE = 20;

export type AttestantLetterDiffStatus =
  | "disabled"
  | "loading"
  | "ready"
  | "empty"
  | "error"
  | "invalidated"
  | "unsupported";

export function useAttestantLetterDiff({
  brevId,
  initialSavedHash,
  initialSavedLetter,
  savedHash,
  currentLetter,
}: {
  brevId: number;
  initialSavedHash: string;
  initialSavedLetter: EditedLetter;
  savedHash: string;
  currentLetter: EditedLetter;
}) {
  const [enabled, setEnabled] = useState(false);
  const savedLettersByHashRef = useRef<Map<string, EditedLetter>>(new Map([[initialSavedHash, initialSavedLetter]]));
  const [invalidatedDiffHashes, setInvalidatedDiffHashes] = useState<Set<string>>(() => new Set());
  const [dismissedDiffs, setDismissedDiffs] = useState<Map<string, string>>(() => new Map());
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

  const invalidateStructuralDiff = useCallback((hash: string) => {
    setInvalidatedDiffHashes((current) => {
      if (current.has(hash)) return current;
      const next = new Set(current);
      next.add(hash);
      while (next.size > MAX_HASH_CACHE_SIZE) {
        const oldest = next.values().next().value;
        if (oldest === undefined) break;
        next.delete(oldest);
      }
      return next;
    });
  }, []);

  const dismissLiteral = useCallback((key: string, hash: string) => {
    setDismissedDiffs((current) => {
      const next = new Map(current);
      next.set(key, hash);
      return next;
    });
  }, []);

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
    setInvalidatedDiffHashes((current) => {
      if (current.size === 0) return current;
      if (current.has(savedHash) && current.size === 1) return current;
      return current.has(savedHash) ? new Set([savedHash]) : new Set();
    });
    setDismissedDiffs((current) => {
      if (current.size === 0) return current;
      const next = new Map([...current].filter(([, hash]) => hash === savedHash));
      return next.size === current.size ? current : next;
    });
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
  const savedStructure = useMemo(
    () => (savedLetter ? letterStructureSignature(savedLetter) : undefined),
    [savedLetter],
  );
  const currentStructure = useMemo(() => letterStructureSignature(currentLetter), [currentLetter]);

  useEffect(() => {
    if (enabled && savedStructure !== undefined && savedStructure !== currentStructure) {
      invalidateStructuralDiff(savedHash);
    }
  }, [currentStructure, enabled, invalidateStructuralDiff, savedHash, savedStructure]);

  useEffect(() => {
    if (!enabled || !diffQuery.isSuccess || diffQuery.data.redigertBrevHash !== savedHash) return;
    if (savedStructure !== currentStructure) return;
    setInvalidatedDiffHashes((current) => {
      if (!current.has(savedHash)) return current;
      const next = new Set(current);
      next.delete(savedHash);
      return next;
    });
  }, [currentStructure, diffQuery.data, diffQuery.isSuccess, enabled, savedHash, savedStructure]);

  const rejectedReasons = [...rejectedDiffs.values()]
    .filter((rejected) => rejected.hash === savedHash)
    .map((rejected) => rejected.reason);
  const currentHashInvalidated = invalidatedDiffHashes.has(savedHash);
  const diffIsEmpty =
    activeDiff !== undefined &&
    Object.keys(activeDiff.editedBlocks).length === 0 &&
    Object.keys(activeDiff.deletedBlocks).length === 0;

  let status: AttestantLetterDiffStatus = "disabled";
  if (enabled) {
    if (savedLetter === undefined || diffQuery.isError) status = "error";
    else if (currentHashInvalidated) status = "invalidated";
    else if (diffQuery.isPending) status = "loading";
    else if (rejectedReasons.length > 0) status = "unsupported";
    else if (diffIsEmpty) status = "empty";
    else if (activeDiff) status = "ready";
    else status = "loading";
  }

  const renderMarkers =
    rejectedReasons.length === 0 &&
    shouldRenderDiffMarkers({
      visDiff: enabled,
      currentSavedHash: savedHash,
      invalidatedDiffHashes,
      diff: activeDiff,
    });

  const retry = () => {
    setRejectedDiffs((current) => {
      const next = new Map([...current].filter(([, rejected]) => rejected.hash !== savedHash));
      return next.size === current.size ? current : next;
    });
    return diffQuery.refetch();
  };

  return {
    enabled,
    setEnabled,
    status,
    activeDiff: renderMarkers ? activeDiff : undefined,
    diffHash: renderMarkers ? savedHash : undefined,
    invalidatedDiffHashes,
    dismissedDiffs,
    dismissLiteral,
    invalidateStructuralDiff,
    reportRejectedLiteral,
    rejectedReasons,
    error: savedLetter === undefined ? new Error("Mangler lagret brevsnapshot") : diffQuery.error,
    retry,
    rememberSavedLetter,
  };
}
