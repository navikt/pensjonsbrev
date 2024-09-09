import type { Adresse } from "./apiTypes";
import type { Mottaker } from "./brev";

export const mapEndreMottakerValueTilMottaker = (v: string | Adresse): Mottaker => {
  if (typeof v === "string") {
    return {
      type: "Samhandler",
      tssId: v,
    };
  } else {
    /* 
        TODO - Fiks at Adresse ikke skal ha noen nullables (utenom adresselinjer)
            Kan også forholde oss til backendens adresse typer også siden vi har disse nå
    */
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
