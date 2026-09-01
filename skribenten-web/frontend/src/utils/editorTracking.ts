import { type Redigeringsflate } from "~/Brevredigering/LetterEditor/RedigeringsflateContext";
import { trackEvent, type UmamiEventData } from "~/utils/umami";

export type MissingFromTemplateEventName = "blokk beholdt" | "blokk slettet";

/**
 * Sporer at saksbehandler beholder eller sletter en blokk som ikke lenger finnes i malen.
 *
 * `redigeringsflate` er en sentral dimensjon for analysen og kan ikke overstyres av `eventData`.
 */
export const trackMissingFromTemplateAction = (
  eventName: MissingFromTemplateEventName,
  redigeringsflate: Redigeringsflate,
  eventData?: UmamiEventData,
): void => {
  const { redigeringsflate: _redigeringsflate, ...ekstraData } = eventData ?? {};

  trackEvent(eventName, { ...ekstraData, redigeringsflate });
};
