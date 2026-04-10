import { type AkselColorRole } from "@navikt/ds-tokens/types";

import { type AdresseVisningTag } from "~/components/AdresseVisning";

import { type Adresse, type KontaktAdresseResponse } from "../types/apiTypes";
import { type Mottaker } from "../types/brev";

// Shared tag constants for recipient display
export const MOTTAKER_TAG_BRUKER = { label: "Bruker", color: "info" as AkselColorRole } satisfies AdresseVisningTag;
export const MOTTAKER_TAG_VERGE = {
  label: "Verge",
  color: "brand-magenta" as AkselColorRole,
} satisfies AdresseVisningTag;
export const MOTTAKER_TAG_SAMHANDLER = {
  label: "Samhandler",
  color: "warning" as AkselColorRole,
} satisfies AdresseVisningTag;

// Type guards for distinguishing address response types
export const erAdresseEnVanligAdresse = (adresse: Adresse | KontaktAdresseResponse): adresse is Adresse =>
  "linje1" in adresse && "linje2" in adresse && "postnr" in adresse && "poststed" in adresse && "land" in adresse;

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
