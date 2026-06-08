import { useEffect, useMemo, useRef } from "react";

import { releaseReservationKeepalive } from "~/api/release-reservation";

type UseReleaseReservationOnPageExitArgs = {
  enabled: boolean;
  brevId: number;
  currentUserNavIdent?: string;
  reservationOwnerNavIdent?: string;
};

export function useReleaseReservationOnPageExit({
  enabled,
  brevId,
  currentUserNavIdent,
  reservationOwnerNavIdent,
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
    };
  }, [enabled, ownsReservation, brevId]);
}
