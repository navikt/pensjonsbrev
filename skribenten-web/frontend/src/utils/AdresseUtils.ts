import { type AdresseVisningTag } from "~/components/AdresseVisning";

import { type Adresse, type KontaktAdresseResponse } from "../types/apiTypes";
import { type Mottaker } from "../types/brev";

// Shared tag constants for recipient display
export const MOTTAKER_TAG_BRUKER = { label: "Bruker", color: "info", variant: "outline" } satisfies AdresseVisningTag;
export const MOTTAKER_TAG_VERGE = {
  label: "Verge",
  color: "warning",
  variant: "outline",
} satisfies AdresseVisningTag;
export const MOTTAKER_TAG_SAMHANDLER = {
  label: "Samhandler",
  color: "neutral",
  variant: "outline",
} satisfies AdresseVisningTag;

export const erAdresseKontaktAdresse = (adresse: Adresse | KontaktAdresseResponse): adresse is KontaktAdresseResponse =>
  "adresseString" in adresse && "adresselinjer" in adresse && "type" in adresse;

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
          manueltAdressertTil: v.manueltAdressertTil,
        }
      : {
          type: "UtenlandskAdresse",
          navn: v.navn,
          adresselinje1: v.linje1!,
          adresselinje2: v.linje2,
          adresselinje3: v.linje3,
          landkode: v.land!,
          manueltAdressertTil: v.manueltAdressertTil,
        };
  }
};
