import type { Adresse } from "../types/apiTypes";
import type { Mottaker } from "../types/brev";

export const mapEndreMottakerValueTilMottaker = (v: string | Adresse): Mottaker => {
  if (typeof v === "string") {
    return {
      type: "Samhandler",
      tssId: v,
      navn: null,
    };
  } else {
    return v.land === "NO"
      ? {
          type: "NorskAdresse",
          navn: v.navn,
          postnummer: v.postnr!,
          poststed: v.poststed!,
          adresselinje1: v.linje1,
          adresselinje2: v.linje2,
          adresselinje3: v.linje3,
        }
      : {
          type: "UtenlandskAdresse",
          navn: v.navn,
          postnummer: v.postnr,
          poststed: v.poststed,
          adresselinje1: v.linje1!,
          adresselinje2: v.linje2,
          adresselinje3: v.linje3,
          landkode: v.land!,
        };
  }
};
