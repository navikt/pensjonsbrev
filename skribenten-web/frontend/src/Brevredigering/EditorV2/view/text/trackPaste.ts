import { type BrevInfo } from "~/types/brev";
import { getPasteMetadata, hasPasteContent } from "~/utils/pasteTracking";
import { trackEvent } from "~/utils/umami";

export type PasteMethod = "tastatursnarvei" | "kontekstmeny" | "annet";

export function trackPaste(info: BrevInfo, clipboard: DataTransfer, method: PasteMethod, replacing: boolean): void {
  if (!hasPasteContent(clipboard)) return;
  const length = clipboard.getData("text/plain").length;
  trackEvent(replacing ? "tekst erstattet" : "tekst limt inn", {
    brevkode: info.brevkode,
    spraak: info.spraak,
    antallTegn: length,
    merEnn200: length > 200,
    limInnMetode: method,
    ...getPasteMetadata(clipboard),
  });
}
