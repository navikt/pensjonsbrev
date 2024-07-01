import { z } from "zod";

import { SamhandlerTypeCode } from "~/types/apiTypes";

export enum Søketype {
  DIREKTE_OPPSLAG = "DIREKTE_OPPSLAG",
  ORGANISASJONSNAVN = "ORGANISASJONSNAVN",
  PERSONNAVN = "PERSONNAVN",
}

export enum Identtype {
  EØS_NUMMER = "EØS_NUMMER",
  FØDSELSDATO = "FØDSELSDATO",
  FØDSELSNUMMER = "FØDSELSNUMMER",
  H_NUMMER = "H_NUMMER",
  INSTITUSJONSNR = "INSTITUSJONSNR",
  KOMUNNENR = "KOMUNNENR",
  LAND = "LAND",
  LOC_NR = "LOC_NR",
  NORSK_D_NUMMER = "NORSK_D_NUMMER",
  NORSK_ORG_NR = "NORSK_ORG_NR",
  NORSK_PNR = "NORSK_PNR",
  OBJEC_IDENTIFIER = "OBJEC_IDENTIFIER",
  TP_LEVERANDØR = "TP_LEVERANDØR",
  TRYGDEKONTOR = "TRYGDEKONTOR",
  UTENLANDSK_ORGNR = "UTENLANDSK_ORGNR",
  UTENLANDSK_PNR = "UTENLANDSK_PNR",
}

export const identtypeToText = (identtype: Identtype) => {
  switch (identtype) {
    case Identtype.EØS_NUMMER: {
      return "EØS-nummer";
    }
    case Identtype.FØDSELSDATO: {
      return "Fødselsdato";
    }
    case Identtype.FØDSELSNUMMER: {
      return "Fødselsnummer";
    }
    case Identtype.H_NUMMER: {
      return "H-nummer";
    }
    case Identtype.INSTITUSJONSNR: {
      return "Institusjonsnr";
    }
    case Identtype.KOMUNNENR: {
      return "Kommunenr";
    }
    case Identtype.LAND: {
      return "Land";
    }
    case Identtype.LOC_NR: {
      return "Loc-nr";
    }
    case Identtype.NORSK_D_NUMMER: {
      return "Norsk D-nummer";
    }
    case Identtype.NORSK_ORG_NR: {
      return "Norsk orgnr";
    }
    case Identtype.NORSK_PNR: {
      return "Norsk pnr";
    }
    case Identtype.OBJEC_IDENTIFIER: {
      return "Objec identifier";
    }
    case Identtype.TP_LEVERANDØR: {
      return "TP leverandør";
    }
    case Identtype.TRYGDEKONTOR: {
      return "Trygdekontor";
    }
    case Identtype.UTENLANDSK_ORGNR: {
      return "Utenlandsk orgnr";
    }
    case Identtype.UTENLANDSK_PNR: {
      return "Utenlandsk pnr";
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

/*
  TODO - hent land fra backend, fra kodeverk 
  */
export enum Land {
  Norge = "Norge",
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
    land: z.nativeEnum(Land),
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
