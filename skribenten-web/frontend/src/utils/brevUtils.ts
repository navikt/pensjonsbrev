import type { BrevInfo } from "~/types/brev";

export const erBrevKlar = (brev: BrevInfo) => {
  return brev.status.type === "Klar";
};
