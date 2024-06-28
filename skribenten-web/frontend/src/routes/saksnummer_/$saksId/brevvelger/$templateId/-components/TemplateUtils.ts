import { z } from "zod";

import type { OrderDoksysLetterRequest, OrderEblankettRequest, OrderExstreamLetterRequest } from "~/types/apiTypes";
import { BrevSystem, type LetterMetadata, SamhandlerTypeCode, SpraakKode } from "~/types/apiTypes";
import type { Nullable } from "~/types/Nullable";

export const byggDoksysOnSubmitRequest = (argz: {
  template: LetterMetadata;
  vedtaksId: Nullable<string>;
  formValues: {
    enhetsId: string;
    isSensitive?: boolean;
    spraak: Nullable<SpraakKode>;
    brevtittel: Nullable<string>;
  };
}): OrderDoksysLetterRequest => {
  return {
    brevkode: argz.template.id,
    enhetsId: argz.formValues.enhetsId,
    //finnes per nå bare 2 brev i doksys, som begge skal ha språk satt
    spraak: argz.formValues.spraak!,
    vedtaksId: argz.vedtaksId ?? null,
  };
};

export const byggExstreamOnSubmitRequest = (argz: {
  template: LetterMetadata;
  idTSSEkstern: Nullable<string>;
  vedtaksId: Nullable<string>;
  formValues: {
    enhetsId: string;
    isSensitive?: boolean;
    spraak: Nullable<SpraakKode>;
    brevtittel: Nullable<string>;
  };
}): OrderExstreamLetterRequest => {
  return {
    brevkode: argz.template.id,
    enhetsId: argz.formValues.enhetsId,
    //validering skal fange at denne ikke er undefined
    isSensitive: argz.formValues.isSensitive!,
    idTSSEkstern: argz.idTSSEkstern ?? null,
    vedtaksId: argz.vedtaksId ?? null,
    brevtittel: argz.formValues.brevtittel ?? null,
    //dersom den finnes, er språk det som er brukerens foretrukne språk (med mindre saksbehandler overridet) & malen støtter språket
    //ellers vil vi plukke bokmål, om brevet støtter det, eller den første språkkoden i listen i template
    spraak:
      argz.formValues.spraak ??
      (argz.template.spraak.includes(SpraakKode.Bokmaal) ? SpraakKode.Bokmaal : argz.template.spraak[0]),
  };
};

export const byggEBlankettOnSubmitRequest = (argz: {
  template: LetterMetadata;
  vedtaksId?: string;
  formValues: {
    landkode: string;
    mottakerText: string;
    enhetsId: string;
    isSensitive?: boolean;
  };
}): OrderEblankettRequest => {
  return {
    landkode: argz.formValues.landkode,
    mottakerText: argz.formValues.mottakerText,
    isSensitive: argz.formValues.isSensitive!,
    enhetsId: argz.formValues.enhetsId,
    vedtaksId: argz.vedtaksId ?? null,
    brevkode: argz.template.id,
  };
};

/**
 * conditonal validering i zod er ganske knot. Vi bygger opp et base schema med optional verdier, og gjør sjekker
 * i superRefine
 *
 * En nedside ved å valdiere gjennom superRefine er at bare etter det obligatoriske feltet er fylt ut, og man prøver å submitte,
 * vil man få feilmeldingene for de andre feltene
 */
export const createValidationSchema = (template: LetterMetadata) => {
  return z
    .object({
      enhetsId: z.string().min(1, "Obligatorisk"),

      //disse valideres gjennom superRefine
      isSensitive: z.boolean({ required_error: "Obligatorisk" }).optional(),
      spraak: z.nativeEnum(SpraakKode).optional(),
      brevtittel: z.string().optional(),
    })
    .superRefine((data, refinementContext) => {
      //validerer språk for alle templates unntatt 'Notat'
      if (template.id === "PE_IY_03_156") {
        refinementContext;
      } else {
        if (!data.spraak) {
          refinementContext.addIssue({
            code: z.ZodIssueCode.custom,
            message: "Obligatorisk",
            path: ["spraak"],
          });
        }
      }

      //isSensitive skal kun sjekkes for brev som ikke er doksys
      if (template.brevsystem !== BrevSystem.DokSys && data.isSensitive === undefined) {
        refinementContext.addIssue({
          code: z.ZodIssueCode.custom,
          message: "Obligatorisk",
          path: ["isSensitive"],
        });
      }

      //validerer brevtittel for templates som har redigerbar brevtittel
      if (template.redigerbarBrevtittel && !data.brevtittel) {
        refinementContext.addIssue({
          code: z.ZodIssueCode.custom,
          message: "Obligatorisk",
          path: ["brevtittel"],
        });
      }

      return refinementContext;
    });
};

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
fra Karo: Hvis det er enkelt, så er støtte for alle land best foreløpig. Hvis det er en vanskelig sak må det prioriteres opp mot andre ting tror jeg
*/
export enum Land {
  Norge = "Norge",
}

export const LeggTilManuellSamhandlerFormData = z.object({
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
  navn: z.string().min(1, "Obligatorisk"),
  adresselinje1: z.string().min(1, "Obligatorisk"),
  adresselinje2: z.string().min(1, "Obligatorisk"),
  postnummer: z.string().min(1, "Obligatorisk"),
  poststed: z.string().min(1, "Obligatorisk"),
  land: z.nativeEnum(Land),
});

export const FinnSamhandlerFormData = z
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

export const samhandlerFormDataSchema = z.object({
  finnSamhandler: FinnSamhandlerFormData,
  leggTilManuellSamhandler: LeggTilManuellSamhandlerFormData,
});

export const createSamhandlerValidationSchema = (tabToValidate: "samhandler" | "manuellAdresse") => {
  return z.object({
    finnSamhandler: tabToValidate === "samhandler" ? FinnSamhandlerFormData : z.object({}),
    leggTilManuellSamhandler: tabToValidate === "manuellAdresse" ? LeggTilManuellSamhandlerFormData : z.object({}),
  });
};

export type SamhandlerFormDataType = z.infer<typeof samhandlerFormDataSchema>;
