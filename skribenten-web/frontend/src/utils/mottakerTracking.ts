import { truncatedSha256Hash } from "~/utils/hashUtils";
import { type InnblikkEventData, trackEvent } from "~/utils/innblikk";

export type MottakerClickEventName = "endre mottaker klikket" | "tilbakestill mottaker klikket";

/**
 * Sporer klikk på endre/tilbakestill mottaker.
 *
 * `saksId` hashes før den sendes til Innblikk, og verken `saksId` eller `kontekst` kan overstyres av
 * `eventData`. Feiler hashingen, sendes hendelsen uten `saksId` — aldri med saksId i klartekst.
 */
export const trackMottakerClick = async (
  eventName: MottakerClickEventName,
  kontekst: string,
  saksId: string,
  eventData?: InnblikkEventData,
): Promise<void> => {
  const { saksId: _saksId, kontekst: _kontekst, ...ekstraData } = eventData ?? {};

  try {
    const hashetSaksId = await truncatedSha256Hash(saksId);
    trackEvent(eventName, { ...ekstraData, kontekst, saksId: hashetSaksId });
  } catch {
    trackEvent(eventName, { ...ekstraData, kontekst });
  }
};
