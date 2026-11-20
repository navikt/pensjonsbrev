import { isAfter, parseISO, startOfToday } from "date-fns";
import { useEffect, useSyncExternalStore } from "react";

/** The announcement is shown to and including `showUntil` (YYYY-MM-DD). */
export const isAnnouncementExpired = (showUntil: string) => isAfter(startOfToday(), parseISO(showUntil));

/** The announcement is shown from and including `showFrom` (YYYY-MM-DD), or right away when it is omitted. */
export const isAnnouncementPublished = (showFrom?: string) =>
  showFrom === undefined || !isAfter(parseISO(showFrom), startOfToday());

const availableCounts = new Map<string, number>();
let replay: { id: string; generation: number } | null = null;
let replayGeneration = 0;

const listeners = new Set<() => void>();
const notify = () => {
  for (const listener of listeners) listener();
};
const subscribe = (listener: () => void) => {
  listeners.add(listener);
  return () => {
    listeners.delete(listener);
  };
};

/** Marks the announcement as possible to show on the current page while `available` is true and the caller is mounted. */
export function useRegisterAnnouncementAvailability(id: string, available: boolean) {
  useEffect(() => {
    if (!available) return;
    availableCounts.set(id, (availableCounts.get(id) ?? 0) + 1);
    notify();
    return () => {
      const count = (availableCounts.get(id) ?? 1) - 1;
      if (count > 0) availableCounts.set(id, count);
      else availableCounts.delete(id);
      notify();
      // React may unmount and mount again right away (StrictMode, a new key), so only end the replay
      // if the announcement is still unavailable once that is done.
      queueMicrotask(() => {
        if (!availableCounts.has(id) && replay?.id === id) {
          replay = null;
          notify();
        }
      });
    };
  }, [id, available]);
}

/** True when the announcement's tour can be started on the current page. */
export const useIsAnnouncementAvailable = (id: string) =>
  useSyncExternalStore(subscribe, () => (availableCounts.get(id) ?? 0) > 0);

/**
 * Asks the mounted announcement to show its tour again, even if the user has already acknowledged it.
 * Ignored when the announcement is not available on the current page. The replay ends when the tour is closed
 * or the announcement is no longer available.
 */
export function requestAnnouncementReplay(id: string) {
  if (!availableCounts.has(id)) return;
  replayGeneration += 1;
  replay = { id, generation: replayGeneration };
  notify();
}

/** Ends the given replay. Ignored if a newer replay has been requested since. */
export function endAnnouncementReplay(generation: number) {
  if (replay?.generation !== generation) return;
  replay = null;
  notify();
}

/** The current replay's generation for this announcement, or 0 when it is not being replayed. */
export const useAnnouncementReplay = (id: string) =>
  useSyncExternalStore(subscribe, () => (replay?.id === id ? replay.generation : 0));
