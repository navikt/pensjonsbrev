import { SKRIBENTEN_API_BASE_PATH } from "~/api/skribenten-api-endpoints";

type ReleaseReservationKeepaliveArgs = {
  brevId: number;
};

export function releaseReservationKeepalive({ brevId }: ReleaseReservationKeepaliveArgs) {
  // This call is intended for page exit where mutation lifecycle callbacks are not reliable.
  return fetch(`${SKRIBENTEN_API_BASE_PATH}/brev/${brevId}/reservasjon`, {
    method: "DELETE",
    credentials: "same-origin",
    keepalive: true,
  });
}
