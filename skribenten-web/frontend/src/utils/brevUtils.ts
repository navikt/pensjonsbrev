import type { BrevInfo } from "~/types/brev";

export const erBrevKlar = (brev: BrevInfo) => {
  return brev.status.type === "Klar";
};

export const erBrevKladd = (brev: BrevInfo) => brev.status.type === "Kladd";
