import { z } from "zod";

import type { Adresse, KontaktAdresseResponse } from "~/types/apiTypes";
import { SamhandlerTypeCode } from "~/types/apiTypes";

export enum Søketype {
  DIREKTE_OPPSLAG = "DIREKTE_OPPSLAG",
  ORGANISASJONSNAVN = "ORGANISASJONSNAVN",
  PERSONNAVN = "PERSONNAVN",
}

export enum Identtype {
  NORSK_D_NR = "DNR",
  EØS_NR = "EOS",
  FØDSELSDATO = "FDAT",
  FØDSELSNR = "FNR",
  H_NR = "HNR",
  OBJEC_IDENTIFIER = "HPR",
  INSTITUSJONSNR = "INST",
  KOMMUNENR = "KOMM",
  Land = "LAND",
  LOC_NR = "LNR",
  NORSK_ORGNR = "ORG",
  NORSK_PNR = "PNR",
  TRYGDEKONTORNR = "TKNR",
  TP_LEVERANDØR = "TPNR",
  UTENLANDSK_ORGNR = "UTOR",
  UTENLANDSK_PNR = "UTPE",
}

export const identtypeToText = (identtype: Identtype) => {
  switch (identtype) {
    case Identtype.NORSK_D_NR: {
      return "Norsk D-nummer";
    }
    case Identtype.EØS_NR: {
      return "EØS-nummer";
    }
    case Identtype.FØDSELSDATO: {
      return "Fødselsdato";
    }
    case Identtype.FØDSELSNR: {
      return "Fødselsnummer";
    }
    case Identtype.H_NR: {
      return "H-nummer";
    }
    case Identtype.OBJEC_IDENTIFIER: {
      return "Objec identifier";
    }
    case Identtype.INSTITUSJONSNR: {
      return "Institusjonsnr";
    }
    case Identtype.KOMMUNENR: {
      return "Kommunenr";
    }
    case Identtype.Land: {
      return "Land";
    }
    case Identtype.LOC_NR: {
      return "Loc-nr";
    }
    case Identtype.NORSK_ORGNR: {
      return "Norsk orgnr";
    }
    case Identtype.NORSK_PNR: {
      return "Norsk pnr";
    }
    case Identtype.TRYGDEKONTORNR: {
      return "Trygdekontornr";
    }
    case Identtype.TP_LEVERANDØR: {
      return "TP leverandør";
    }
    case Identtype.UTENLANDSK_ORGNR: {
      return "Utenlandsk orgnr";
    }
    case Identtype.UTENLANDSK_PNR: {
      return "Utenlandsk prn";
    }
  }
};

export enum InnOgUtland {
  INNLAND = "INNLAND",
  UTLAND = "UTLAND",
  ALLE = "ALLE",
}

export const leggTilManuellSamhandlerFormDataSchema = z.object({
  /**
   * manuell adresse har 2 krav for utfylling:
   * For norske adresser (der land er "NO") - kreves navn, postnummer, og poststed.
   * For utenlandkse adresser (alle andre land som ikke er "NO") - kreves navn, og adresselinje1
   */
  adresse: z
    .object({
      navn: z.string().min(1, "Obligatorisk"),
      linje1: z.string(),
      linje2: z.string(),
      postnr: z.string(),
      poststed: z.string(),
      land: z.string().min(1, "Obligatorisk"),
    })
    .superRefine((data, refinementContext) => {
      if (data.land === "NO") {
        if (data.postnr.length !== 4) {
          refinementContext.addIssue({
            code: z.ZodIssueCode.custom,
            message: "Postnummer må være 4 tegn",
            path: ["postnr"],
          });
        }
        if (data.poststed === "") {
          refinementContext.addIssue({
            code: z.ZodIssueCode.custom,
            message: "Poststed må fylles ut",
            path: ["poststed"],
          });
        }
      } else {
        if (data.linje1 === "") {
          refinementContext.addIssue({
            code: z.ZodIssueCode.custom,
            message: "Adresselinje 1 må fylles ut",
            path: ["linje1"],
          });
        }
      }
    }),
});

export const finnSamhandlerFormDataSchema = z
  .object({
    søketype: z
      .nativeEnum(Søketype)
      .nullable()
      .superRefine((data, refinementContext) => {
        if (data === null) {
          refinementContext.addIssue({
            code: z.ZodIssueCode.custom,
            message: "Feltet må fylles ut",
          });
        }
      }),
    samhandlerType: z
      .nativeEnum(SamhandlerTypeCode)
      .nullable()
      .superRefine((data, refinementContext) => {
        if (data === null) {
          refinementContext.addIssue({
            code: z.ZodIssueCode.custom,
            message: "Feltet må fylles ut",
          });
        }
      }),
    //valideres i superRefine under siden vi må sjekke søketype
    direkteOppslag: z.object({
      identtype: z.nativeEnum(Identtype).nullable(),
      id: z.string().nullable(),
    }),
    //valideres i superRefine under siden vi må sjekke søketype
    organisasjonsnavn: z.object({
      innOgUtland: z.nativeEnum(InnOgUtland).nullable(),
      navn: z.string().nullable(),
    }),
    //valideres i superRefine under siden vi må sjekke søketype
    personnavn: z.object({
      fornavn: z.string().nullable(),
      etternavn: z.string().nullable(),
    }),
  })
  .superRefine((data, refinementContext) => {
    switch (data.søketype) {
      case Søketype.DIREKTE_OPPSLAG: {
        if (data.direkteOppslag.identtype === null) {
          refinementContext.addIssue({
            code: z.ZodIssueCode.custom,
            message: "Feltet må fylles ut",
            path: ["direkteOppslag.identtype"],
          });
        }
        if (!data.direkteOppslag.id) {
          refinementContext.addIssue({
            code: z.ZodIssueCode.custom,
            message: "Feltet må fylles ut",
            path: ["direkteOppslag.id"],
          });
        }
        break;
      }

      case Søketype.ORGANISASJONSNAVN: {
        if (!data.organisasjonsnavn.navn) {
          refinementContext.addIssue({
            code: z.ZodIssueCode.custom,
            message: "Feltet må fylles ut",
            path: ["organisasjonsnavn.navn"],
          });
        }
        break;
      }

      case Søketype.PERSONNAVN: {
        if (!data.personnavn.fornavn) {
          refinementContext.addIssue({
            code: z.ZodIssueCode.custom,
            message: "Feltet må fylles ut",
            path: ["personnavn.fornavn"],
          });
        }
        if (!data.personnavn.etternavn) {
          refinementContext.addIssue({
            code: z.ZodIssueCode.custom,
            message: "Feltet må fylles ut",
            path: ["personnavn.etternavn"],
          });
        }
        break;
      }

      case null: {
        throw new Error("Teknisk feil - Forventet at verdien til søketype er satt, men var null");
      }
    }

    return refinementContext;
  });

export type ManuellAdresseUtfyllingFormData = z.infer<typeof leggTilManuellSamhandlerFormDataSchema>;
export type FinnSamhandlerFormData = z.infer<typeof finnSamhandlerFormDataSchema>;

export const combinedFormSchema = z.object({
  manuellAdresse: leggTilManuellSamhandlerFormDataSchema,
  finnSamhandler: finnSamhandlerFormDataSchema,
});

export const createSamhandlerValidationSchema = (tabToValidate: "samhandler" | "manuellAdresse" | "oppsummering") => {
  return z.object({
    finnSamhandler: tabToValidate === "samhandler" ? finnSamhandlerFormDataSchema : z.object({}),
    manuellAdresse: tabToValidate === "manuellAdresse" ? leggTilManuellSamhandlerFormDataSchema : z.object({}),
  });
};

export type CombinedFormData = z.infer<typeof combinedFormSchema>;

export const erAdresseEnVanligAdresse = (adresse: Adresse | KontaktAdresseResponse): adresse is Adresse =>
  "linje1" in adresse && "linje2" in adresse && "postnr" in adresse && "poststed" in adresse && "land" in adresse;

export const erAdresseKontaktAdresse = (adresse: Adresse | KontaktAdresseResponse): adresse is KontaktAdresseResponse =>
  "adresseString" in adresse && "adresselinjer" in adresse && "type" in adresse;

export type EndreMottakerModalTabs = "samhandler" | "manuellAdresse" | "oppsummering";
