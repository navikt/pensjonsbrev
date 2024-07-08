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
      return "D-nummer";
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
      return "Helsepersonellnummer";
    }
    case Identtype.INSTITUSJONSNR: {
      return "Institusjonsnummer";
    }
    case Identtype.KOMMUNENR: {
      return "Kommunenummer";
    }
    case Identtype.Land: {
      return "Land";
    }
    case Identtype.LOC_NR: {
      return "Lokaliseringsnummer";
    }
    case Identtype.NORSK_ORGNR: {
      return "Organisasjonsnummer";
    }
    case Identtype.NORSK_PNR: {
      return "Personnummer";
    }
    case Identtype.TRYGDEKONTORNR: {
      return "Trygdekontornummer";
    }
    case Identtype.TP_LEVERANDØR: {
      return "Tjenesteyternummer";
    }
    case Identtype.UTENLANDSK_ORGNR: {
      return "Utenlandsk organisasjonsnummer";
    }
    case Identtype.UTENLANDSK_PNR: {
      return "Utenlandsk personnummer";
    }
  }
};

export enum InnOgUtland {
  INNLAND = "INNLAND",
  UTLAND = "UTLAND",
  ALLE = "ALLE",
}

//TODO - skissene har disse som description. er disse valgene?
export enum TypeMottaker {
  PrivatPerson = "PrivatPerson",
  Samhandler = "Samhandler",
  Institusjon = "Institusjon",
  Offentlig = "Offentlig",
}

export const leggTilManuellSamhandlerFormDataSchema = z.object({
  typeMottaker: z
    .nativeEnum(TypeMottaker)
    .nullable()
    .superRefine((data, refinementContext) => {
      if (data === null) {
        refinementContext.addIssue({
          code: z.ZodIssueCode.custom,
          message: "Feltet må fylles ut",
        });
      }
      return refinementContext;
    }),
  adresse: z.object({
    navn: z.string().min(1, "Obligatorisk"),
    linje1: z.string().min(1, "Obligatorisk"),
    linje2: z.string(),
    postnr: z.string().min(1, "Obligatorisk"),
    poststed: z.string().min(1, "Obligatorisk"),
    land: z.string().min(1, "Obligatorisk"),
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
        if (data.organisasjonsnavn.innOgUtland === null) {
          refinementContext.addIssue({
            code: z.ZodIssueCode.custom,
            message: "Feltet må fylles ut",
            path: ["organisasjonsnavn.innOgUtland"],
          });
        }
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
