import { z } from "zod";

import type { OrderDoksysLetterRequest, OrderExstreamLetterRequest } from "~/types/apiTypes";
import { type LetterMetadata, SpraakKode } from "~/types/apiTypes";

export const byggDoksysOnSubmitRequest = (argz: {
  template: LetterMetadata;
  vedtaksId?: string;
  formValues: {
    enhetsId: string;
    isSensitive: boolean;
    spraak?: SpraakKode | undefined;
    brevtittel?: string | undefined;
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
  idTSSEkstern?: string;
  vedtaksId?: string;
  formValues: {
    enhetsId: string;
    isSensitive: boolean;
    spraak?: SpraakKode | undefined;
    brevtittel?: string | undefined;
  };
}): OrderExstreamLetterRequest => {
  return {
    brevkode: argz.template.id,
    enhetsId: argz.formValues.enhetsId,
    isSensitive: argz.formValues.isSensitive,
    idTSSEkstern: argz.idTSSEkstern ?? null,
    vedtaksId: argz.vedtaksId ?? null,
    brevtittel: argz.formValues.brevtittel ?? null,
    //dersom den finnes, er språk det som er brukerens foretrukne språk (med mindre saksbehandler overridet) & malen støtter språket
    //ellers vil vi plukke bokmål, om brevet støtter det, eller den første språkkoden i listen i template
    spraak:
      argz.formValues.spraak ?? argz.template.spraak.includes(SpraakKode.Bokmaal)
        ? SpraakKode.Bokmaal
        : argz.template.spraak[0],
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
      isSensitive: z.boolean({ required_error: "Obligatorisk" }),
      enhetsId: z.string().min(1, "Obligatorisk"),

      //disse valideres gjennom superRefine
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
