import { z } from "zod";

/* Enums for radio button values */
const pensjonstypeEnum = z.enum(["Alder", "Ufoere", "Etterlatte"]);
const grunnlagInnvilgetEnum = z.enum(["IHenholdTilNasjonalLovgivning", "ProRata", "MindreEnnEttAar"]);
const reduksjonsgrunnlagEnum = z.enum([
  "PaaGrunnAvAndreYtelserEllerAnnenInntekt",
  "PaaGrunnAvOverlappendeGodskrevnePerioder",
]);
const avslagsbegrunnelseEnum = z.enum([
  "IngenOpptjeningsperioder",
  "OpptjeningsperiodePaaMindreEnnEttAar",
  "KravTilKvalifiseringsperiodeEllerAndreKvalifiseringskravErIkkeOppfylt",
  "VilkaarOmUfoerhetErIkkeOppfylt",
  "InntektstakErOverskredet",
  "PensjonsalderErIkkeNaadd",
  "AndreAarsaker",
]);
const sakstypeEnum = z.enum(["ALDER", "UFORE", "ETTERLATTE"]);

/* Shared date validation helper */
const optionalDateField = (fieldName: string = "Dato") =>
  z
    .string()
    .max(10, `${fieldName} kan ikke være lengre enn 10 tegn`)
    .refine((val) => !val || /^(\d{2})\.(\d{2})\.(\d{4})$/.test(val), "Dato må være i formatet dd.mm.åååå")
    .refine((val) => {
      if (!val) return true;
      const [day, month, year] = val.split(".").map(Number);
      const date = new Date(year, month - 1, day);
      return date.getDate() === day && date.getMonth() === month - 1 && date.getFullYear() === year;
    }, "Ugyldig dato");

const requiredDateField = (fieldName: string = "Dato") =>
  z
    .string()
    .min(1, `${fieldName} er obligatorisk`)
    .max(10, `${fieldName} kan ikke være lengre enn 10 tegn`)
    .refine((val) => /^(\d{2})\.(\d{2})\.(\d{4})$/.test(val), "Dato må være i formatet dd.mm.åååå")
    .refine((val) => {
      const [day, month, year] = val.split(".").map(Number);
      const date = new Date(year, month - 1, day);
      return date.getDate() === day && date.getMonth() === month - 1 && date.getFullYear() === year;
    }, "Ugyldig dato");

/* Tab 1 & 2: Person validation (Innehaver & Forsikrede) */
const p1PersonFormSchema = z.object({
  fornavn: z.string().min(1, "Fornavn er obligatorisk").max(100, "Fornavn kan ikke være lengre enn 100 tegn"),
  etternavn: z.string().min(1, "Etternavn er obligatorisk").max(100, "Etternavn kan ikke være lengre enn 100 tegn"),
  etternavnVedFoedsel: z.string().max(100, "Etternavn ved fødsel kan ikke være lengre enn 100 tegn"),
  foedselsdato: requiredDateField("Fødselsdato"),
  adresselinje: z.string().max(200, "Adresselinje kan ikke være lengre enn 200 tegn"),
  poststed: z.string().max(100, "Poststed kan ikke være lengre enn 100 tegn"),
  postnummer: z.string().max(20, "Postnummer kan ikke være lengre enn 20 tegn"),
  landkode: z.string().min(1, "Landskode er obligatorisk").max(3, "Landskode kan ikke være lengre enn 3 tegn"),
});

/* Tab 3: Innvilget Pensjon validation */
const p1InstitusjonFormSchema = z.object({
  institusjonsnavn: z.string().max(200, "Institusjonsnavn kan ikke være lengre enn 200 tegn"),
  pin: z.string().max(50, "PIN kan ikke være lengre enn 50 tegn"),
  saksnummer: z.string().max(50, "Saksnummer kan ikke være lengre enn 50 tegn"),
  vedtaksdato: optionalDateField("Vedtaksdato"),
  land: z.string().max(3, "Landskode kan ikke være lengre enn 3 tegn"),
});

/* Helper to check if a row has any filled data */
const isRowFilled = (row: Record<string, unknown>): boolean => {
  const checkValue = (value: unknown): boolean => {
    if (value === null || value === undefined || value === "") return false;
    if (typeof value === "object") {
      return Object.values(value as Record<string, unknown>).some(checkValue);
    }
    return true;
  };
  return Object.values(row).some(checkValue);
};

const p1InnvilgetPensjonFormSchema = z
  .object({
    institusjon: p1InstitusjonFormSchema,
    pensjonstype: pensjonstypeEnum.nullable(),
    datoFoersteUtbetaling: optionalDateField("Dato for første utbetaling"),
    utbetalt: z.string().max(500, "Bruttobeløp kan ikke være lengre enn 500 tegn"),
    grunnlagInnvilget: grunnlagInnvilgetEnum.nullable(), // Optional - null means "Ikke relevant"
    reduksjonsgrunnlag: reduksjonsgrunnlagEnum.nullable(), // Optional - null means "Ikke relevant"
    vurderingsperiode: z.string().max(500, "Vurderingsperiode kan ikke være lengre enn 500 tegn"),
    adresseNyVurdering: z.string().max(500, "Adresse for ny vurdering kan ikke være lengre enn 500 tegn"),
  })
  .superRefine((data, ctx) => {
    // Only validate required fields if the row has any data
    if (isRowFilled(data)) {
      // Institusjonsnavn is required if row is filled
      if (!data.institusjon.institusjonsnavn) {
        ctx.addIssue({
          code: "custom",
          message: "Institusjonsnavn er obligatorisk",
          path: ["institusjon", "institusjonsnavn"],
        });
      }

      // Pensjonstype is required if row is filled
      if (!data.pensjonstype) {
        ctx.addIssue({
          code: "custom",
          message: "Pensjonstype må velges",
          path: ["pensjonstype"],
        });
      }

      // NOTE: grunnlagInnvilget and reduksjonsgrunnlag are NOT required
      // null means "Ikke relevant" which is a valid choice
    }
  });

/* Tab 4: Avslått Pensjon validation */
const p1AvslaattPensjonFormSchema = z
  .object({
    institusjon: p1InstitusjonFormSchema,
    pensjonstype: pensjonstypeEnum.nullable(),
    avslagsbegrunnelse: avslagsbegrunnelseEnum.nullable(),
    vurderingsperiode: z.string().max(500, "Vurderingsperiode kan ikke være lengre enn 500 tegn"),
    adresseNyVurdering: z.string().max(500, "Adresse for ny vurdering kan ikke være lengre enn 500 tegn"),
  })
  .superRefine((data, ctx) => {
    /* Only validate required fields if the row has any data */
    if (isRowFilled(data)) {
      /* Institusjonsnavn is required if row is filled */
      if (!data.institusjon.institusjonsnavn) {
        ctx.addIssue({
          code: "custom",
          message: "Institusjonsnavn er obligatorisk",
          path: ["institusjon", "institusjonsnavn"],
        });
      }

      /* Pensjonstype is required if row is filled */
      if (!data.pensjonstype) {
        ctx.addIssue({
          code: "custom",
          message: "Pensjonstype må velges",
          path: ["pensjonstype"],
        });
      }

      /* Avslagsbegrunnelse is required if row is filled */
      if (!data.avslagsbegrunnelse) {
        ctx.addIssue({
          code: "custom",
          message: "Begrunnelse for avslag må velges",
          path: ["avslagsbegrunnelse"],
        });
      }
    }
  });

/* Tab 5: Utfyllende Institusjon validation */
const p1UtfyllendeInstitusjonFormSchema = z.object({
  navn: z.string().min(1, "Navn er obligatorisk").max(200, "Navn kan ikke være lengre enn 200 tegn"),
  adresselinje: z.string().max(200, "Adresselinje kan ikke være lengre enn 200 tegn"),
  poststed: z.string().max(100, "Poststed kan ikke være lengre enn 100 tegn"),
  postnummer: z.string().max(20, "Postnummer kan ikke være lengre enn 20 tegn"),
  landkode: z.string().min(1, "Landskode er obligatorisk").max(3, "Landskode kan ikke være lengre enn 3 tegn"),
  institusjonsID: z.string().max(50, "Institusjons-ID kan ikke være lengre enn 50 tegn"),
  faksnummer: z.string().max(30, "Faksnummer kan ikke være lengre enn 30 tegn"),
  telefonnummer: z.string().max(30, "Telefonnummer kan ikke være lengre enn 30 tegn"),
  epost: z
    .string()
    .max(100, "E-post kan ikke være lengre enn 100 tegn")
    .refine((val) => !val || /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(val), "Ugyldig e-postformat"),
  dato: optionalDateField("Dato"),
});

/* P1 Form Schema */
export const p1RedigerbarFormSchema = z
  .object({
    innehaver: p1PersonFormSchema,
    forsikrede: p1PersonFormSchema,
    sakstype: sakstypeEnum,
    innvilgedePensjoner: z.array(p1InnvilgetPensjonFormSchema),
    avslaattePensjoner: z.array(p1AvslaattPensjonFormSchema),
    utfyllendeInstitusjon: p1UtfyllendeInstitusjonFormSchema,
  })
  .superRefine((data, ctx) => {
    /* Validate that at least one innvilget or avslått row is filled */
    const hasFilledInnvilget = data.innvilgedePensjoner.some(isRowFilled);
    const hasFilledAvslaat = data.avslaattePensjoner.some(isRowFilled);

    if (!hasFilledInnvilget && !hasFilledAvslaat) {
      ctx.addIssue({
        code: "custom",
        message: "Minst én innvilget eller avslått pensjon må fylles ut",
        path: ["innvilgedePensjoner"],
      });
    }
  });

/* Per-tab validation schemas (for partial validation) */
export const p1InnehaverTabSchema = z.object({
  innehaver: p1PersonFormSchema,
});

export const p1ForsikredeTabSchema = z.object({
  forsikrede: p1PersonFormSchema,
});

export const p1InnvilgetTabSchema = z.object({
  innvilgedePensjoner: z.array(p1InnvilgetPensjonFormSchema),
});

export const p1AvslagTabSchema = z.object({
  avslaattePensjoner: z.array(p1AvslaattPensjonFormSchema),
});

export const p1InstitusjonTabSchema = z.object({
  utfyllendeInstitusjon: p1UtfyllendeInstitusjonFormSchema,
});

export type P1RedigerbarFormSchema = z.infer<typeof p1RedigerbarFormSchema>;
