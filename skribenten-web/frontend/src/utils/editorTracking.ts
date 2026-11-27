import { type Redigeringsflate } from "~/Brevredigering/LetterEditor/RedigeringsflateContext";
import { type InnblikkEventData, trackEvent } from "~/utils/innblikk";

export type MissingInRenderedEventName = "blokk beholdt" | "blokk slettet";

/**
 * Sporer at saksbehandler beholder eller sletter en blokk som ikke lenger finnes i malen.
 *
 * `redigeringsflate` er en sentral dimensjon for analysen og kan ikke overstyres av `eventData`.
 */
export const trackMissingInRenderedAction = (
  eventName: MissingInRenderedEventName,
  redigeringsflate: Redigeringsflate,
  eventData?: InnblikkEventData,
): void => {
  const { redigeringsflate: _redigeringsflate, ...ekstraData } = eventData ?? {};

  trackEvent(eventName, { ...ekstraData, redigeringsflate });
};
