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

/* Tab 1 & 2: Person validation (Innehaver & Forsikrede) */
const p1PersonFormSchema = z.object({
  fornavn: z.string().min(1, "Fornavn er obligatorisk").max(100, "Fornavn kan ikke være lengre enn 100 tegn"),
  etternavn: z.string().min(1, "Etternavn er obligatorisk").max(100, "Etternavn kan ikke være lengre enn 100 tegn"),
  etternavnVedFoedsel: z.string().max(100, "Etternavn ved fødsel kan ikke være lengre enn 100 tegn"),
  foedselsdato: z
    .string()
    .min(1, "Fødselsdato er obligatorisk")
    .regex(/^\d{4}-\d{2}-\d{2}$/, "Fødselsdato må være i formatet YYYY-MM-DD"),
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
  vedtaksdato: z
    .string()
    .max(10, "Vedtaksdato kan ikke være lengre enn 10 tegn")
    .refine((val) => !val || /^(\d{2})\.(\d{2})\.(\d{4})$/.test(val), "Dato må være i formatet dd.mm.åååå"),
  land: z.string().max(3, "Landskode kan ikke være lengre enn 3 tegn"),
});

/* Helper to check if a row has any filled data */
const isRowFilled = (row: {
  institusjon: { institusjonsnavn: string; pin: string; saksnummer: string; vedtaksdato: string; land: string };
  pensjonstype: string | null;
  datoFoersteUtbetaling?: string;
  utbetalt?: string;
  grunnlagInnvilget?: string | null;
  reduksjonsgrunnlag?: string | null;
  avslagsbegrunnelse?: string | null;
  vurderingsperiode: string;
  adresseNyVurdering: string;
}): boolean => {
  return !!(
    row.institusjon.institusjonsnavn ||
    row.institusjon.pin ||
    row.institusjon.saksnummer ||
    row.institusjon.vedtaksdato ||
    row.institusjon.land ||
    row.pensjonstype ||
    row.vurderingsperiode ||
    row.adresseNyVurdering ||
    row.datoFoersteUtbetaling ||
    row.utbetalt ||
    row.grunnlagInnvilget ||
    row.reduksjonsgrunnlag ||
    row.avslagsbegrunnelse
  );
};

const p1InnvilgetPensjonFormSchema = z
  .object({
    institusjon: p1InstitusjonFormSchema,
    pensjonstype: pensjonstypeEnum.nullable(),
    datoFoersteUtbetaling: z
      .string()
      .max(10, "Dato kan ikke være lengre enn 10 tegn")
      .refine((val) => !val || /^(\d{2})\.(\d{2})\.(\d{4})$/.test(val), "Dato må være i formatet dd.mm.åååå"),
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
          message: "Institusjonsnavn er obligatorisk ",
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
  dato: z
    .string()
    .max(10, "Dato kan ikke være lengre enn 10 tegn")
    .refine((val) => !val || /^(\d{2})\.(\d{2})\.(\d{4})$/.test(val), "Dato må være i formatet dd.mm.åååå"),
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
