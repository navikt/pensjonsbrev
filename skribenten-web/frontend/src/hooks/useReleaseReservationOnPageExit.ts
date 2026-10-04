import { useEffect, useMemo, useRef } from "react";

import { releaseReservationKeepalive } from "~/api/release-reservation";

type UseReleaseReservationOnPageExitArgs = {
  enabled: boolean;
  brevId: number;
  currentUserNavIdent?: string;
  reservationOwnerNavIdent?: string;
  beforeRelease?: () => Promise<unknown>;
};

export function useReleaseReservationOnPageExit({
  enabled,
  brevId,
  currentUserNavIdent,
  reservationOwnerNavIdent,
  beforeRelease,
}: UseReleaseReservationOnPageExitArgs) {
  const releasedRef = useRef(false);

  const ownsReservation = useMemo(
    () =>
      currentUserNavIdent != null &&
      reservationOwnerNavIdent != null &&
      currentUserNavIdent === reservationOwnerNavIdent,
    [currentUserNavIdent, reservationOwnerNavIdent],
  );

  useEffect(() => {
    releasedRef.current = false;
  }, [brevId, reservationOwnerNavIdent]);

  useEffect(() => {
    if (!enabled || !ownsReservation) return;

    const releaseOnce = () => {
      if (releasedRef.current) return;

      releasedRef.current = true;
      void releaseReservationKeepalive({ brevId });
    };

    globalThis.addEventListener("pagehide", releaseOnce);

    return () => {
      globalThis.removeEventListener("pagehide", releaseOnce);
      if (beforeRelease) {
        void beforeRelease().then(releaseOnce, releaseOnce);
      } else {
        releaseOnce();
      }
    };
  }, [enabled, ownsReservation, brevId, beforeRelease]);
}
