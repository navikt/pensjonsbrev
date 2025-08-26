import { z } from "zod";

import type { OrderDoksysLetterRequest, OrderEblankettRequest, OrderExstreamLetterRequest } from "~/types/apiTypes";
import { BrevSystem, type LetterMetadata, SpraakKode } from "~/types/apiTypes";
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
      enhetsId: z.coerce.string().min(1, { error: "Obligatorisk" }),

      //disse valideres gjennom superRefine
      isSensitive: z.boolean({ error: "Obligatorisk" }).optional(),
      spraak: z.enum(SpraakKode).optional(),
      brevtittel: z.string().optional(),
    })
    .superRefine((data, refinementContext) => {
      //validerer språk for alle templates unntatt 'Notat'
      if (template.id !== "PE_IY_03_156" && !data.spraak) {
        refinementContext.addIssue({
          code: "custom",
          message: "Obligatorisk",
          path: ["spraak"],
        });
      }

      //isSensitive skal kun sjekkes for brev som ikke er doksys
      if (template.brevsystem !== BrevSystem.DokSys && data.isSensitive === undefined) {
        refinementContext.addIssue({
          code: "custom",
          message: "Obligatorisk",
          path: ["isSensitive"],
        });
      }

      //validerer brevtittel for templates som har redigerbar brevtittel
      if (template.redigerbarBrevtittel && !data.brevtittel) {
        refinementContext.addIssue({
          code: "custom",
          message: "Obligatorisk",
          path: ["brevtittel"],
        });
      }
    });
};

export const brevmalBrevbakerFormSchema = z.object({
  spraak: z.enum(SpraakKode),
  enhetsId: z.string(),
});

//regel er at hvis brukerens foretrukne språk er tilgjengelig, og malen støtter det, skal den være valgt, ellers skal den første språkkoden i malen være valgt
export const hentDefaultValueForSpråk = (preferredLanguage: Nullable<SpraakKode>, tilgjengeligeSpråk: SpraakKode[]) => {
  if (preferredLanguage && tilgjengeligeSpråk.includes(preferredLanguage)) {
    return preferredLanguage;
  } else {
    return tilgjengeligeSpråk.includes(SpraakKode.Bokmaal) ? SpraakKode.Bokmaal : tilgjengeligeSpråk[0];
  }
};
