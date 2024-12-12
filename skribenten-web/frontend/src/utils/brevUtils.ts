import type { BrevInfo } from "~/types/brev";

export const erBrevKlar = (brev: BrevInfo) => {
  return brev.status.type === "Klar";
};

export const erBrevKladd = (brev: BrevInfo) => brev.status.type === "Kladd";
export const erBrevUnderRedigering = (brev: BrevInfo) => brev.status.type === "UnderRedigering";
export const erBrevKladdEllerUnderRedigering = (brev: BrevInfo) => erBrevKladd(brev) || erBrevUnderRedigering(brev);

export const erBrevArkivert = (brev: BrevInfo): boolean => brev.status.type === "Arkivert";
