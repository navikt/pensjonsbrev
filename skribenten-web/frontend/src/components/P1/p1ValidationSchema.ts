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
const sakstypeEnum = z.enum([
  "AFP",
  "AFP_PRIVAT",
  "ALDER",
  "BARNEP",
  "FAM_PL",
  "GAM_YRK",
  "GENRL",
  "GJENLEV",
  "GRBL",
  "KRIGSP",
  "OMSORG",
  "UFOREP",
]);

/**
 * ISO date string validator.
 * Validates dates in ISO format "yyyy-MM-dd" (e.g., "2022-09-23").
 * Empty strings are valid (optional field).
 * Invalid formats or invalid dates show error message.
 */
const isoDateStringField = () =>
  z.string().refine(
    (val) => {
      if (val === "") return true; // Empty is allowed
      // Check ISO format yyyy-MM-dd
      const isoDateRegex = /^\d{4}-\d{2}-\d{2}$/;
      if (!isoDateRegex.test(val)) return false;
      // Check if it's a valid date
      const date = new Date(val + "T00:00:00");
      return !Number.isNaN(date.getTime());
    },
    { message: "Ugyldig dato" },
  );

/* Tab 1 & 2: Person validation (Innehaver & Forsikrede) - all fields optional for now */
const p1PersonFormSchema = z.object({
  fornavn: z.string().max(100, "Fornavn kan ikke være lengre enn 100 tegn"),
  etternavn: z.string().max(100, "Etternavn kan ikke være lengre enn 100 tegn"),
  etternavnVedFoedsel: z.string().max(100, "Etternavn ved fødsel kan ikke være lengre enn 100 tegn"),
  foedselsdato: isoDateStringField(),
  adresselinje: z.string().max(200, "Adresselinje kan ikke være lengre enn 200 tegn"),
  poststed: z.string().max(100, "Poststed kan ikke være lengre enn 100 tegn"),
  postnummer: z.string().max(20, "Postnummer kan ikke være lengre enn 20 tegn"),
  landkode: z.string().max(20, "Land navn kan ikke være lengre enn 20 tegn"),
});

/* Tab 3: Innvilget Pensjon validation */
const p1InstitusjonFormSchema = z.object({
  land: z.string().max(20, "Land navn kan ikke være lengre enn 20 tegn"),
  institusjonsnavn: z.string().max(200, "Institusjonsnavn kan ikke være lengre enn 200 tegn"),
  pin: z.string().max(50, "PIN kan ikke være lengre enn 50 tegn"),
  saksnummer: z.string().max(50, "Saksnummer kan ikke være lengre enn 50 tegn"),
  vedtaksdato: isoDateStringField(),
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
    datoFoersteUtbetaling: isoDateStringField(),
    utbetalt: z.string().max(500, "Bruttobeløp kan ikke være lengre enn 500 tegn"),
    grunnlagInnvilget: grunnlagInnvilgetEnum.nullable(),
    reduksjonsgrunnlag: reduksjonsgrunnlagEnum.nullable(),
    vurderingsperiode: z.string().max(500, "Vurderingsperiode kan ikke være lengre enn 500 tegn"),
    adresseNyVurdering: z.string().max(500, "Adresse for ny vurdering kan ikke være lengre enn 500 tegn"),
  })
  .superRefine((data, ctx) => {
    if (isRowFilled(data)) {
      if (!data.institusjon.institusjonsnavn) {
        ctx.addIssue({
          code: "custom",
          message: "Institusjonsnavn er obligatorisk",
          path: ["institusjon", "institusjonsnavn"],
        });
      }
      if (!data.pensjonstype) {
        ctx.addIssue({
          code: "custom",
          message: "Pensjonstype må velges",
          path: ["pensjonstype"],
        });
      }
      if (!data.grunnlagInnvilget) {
        ctx.addIssue({
          code: "custom",
          message: "Grunnlag må velges",
          path: ["grunnlagInnvilget"],
        });
      }
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
    if (isRowFilled(data)) {
      if (!data.institusjon.institusjonsnavn) {
        ctx.addIssue({
          code: "custom",
          message: "Institusjonsnavn er obligatorisk",
          path: ["institusjon", "institusjonsnavn"],
        });
      }
      if (!data.pensjonstype) {
        ctx.addIssue({
          code: "custom",
          message: "Pensjonstype må velges",
          path: ["pensjonstype"],
        });
      }
      if (!data.avslagsbegrunnelse) {
        ctx.addIssue({
          code: "custom",
          message: "Begrunnelse for avslag må velges",
          path: ["avslagsbegrunnelse"],
        });
      }
    }
  });

/* Tab 5: Utfyllende Institusjon validation - all fields optional for now */
const p1UtfyllendeInstitusjonFormSchema = z.object({
  navn: z.string().max(200, "Navn kan ikke være lengre enn 200 tegn"),
  adresselinje: z.string().max(200, "Adresselinje kan ikke være lengre enn 200 tegn"),
  poststed: z.string().max(100, "Poststed kan ikke være lengre enn 100 tegn"),
  postnummer: z.string().max(20, "Postnummer kan ikke være lengre enn 20 tegn"),
  landkode: z.string().max(3, "Landskode kan ikke være lengre enn 3 tegn"),
  institusjonsID: z.string().max(50, "Institusjons-ID kan ikke være lengre enn 50 tegn"),
  faksnummer: z.string().max(30, "Faksnummer kan ikke være lengre enn 30 tegn"),
  telefonnummer: z.string().max(30, "Telefonnummer kan ikke være lengre enn 30 tegn"),
  epost: z
    .string()
    .max(100, "E-post kan ikke være lengre enn 100 tegn")
    .refine((val) => !val || /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(val), "Ugyldig e-postformat"),
  dato: isoDateStringField(),
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
