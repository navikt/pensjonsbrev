import type { P1RedigerbarForm } from "~/types/p1FormTypes";

const createDefaultRows = <T>(factory: () => T, count: number): T[] => Array.from({ length: count }, () => factory());

export const emptyInnvilgetRow = (): P1RedigerbarForm["innvilgedePensjoner"][number] => ({
  institusjon: {
    institusjonsnavn: "",
    pin: "",
    saksnummer: "",
    vedtaksdato: "",
    land: "",
  },
  pensjonstype: null,
  datoFoersteUtbetaling: "",
  utbetalt: "",
  grunnlagInnvilget: null,
  reduksjonsgrunnlag: null,
  vurderingsperiode: "",
  adresseNyVurdering: "",
});

export const emptyAvslaattRow = (): P1RedigerbarForm["avslaattePensjoner"][number] => ({
  institusjon: {
    institusjonsnavn: "",
    pin: "",
    saksnummer: "",
    vedtaksdato: "",
    land: "",
  },
  pensjonstype: null,
  avslagsbegrunnelse: null,
  vurderingsperiode: "",
  adresseNyVurdering: "",
});

export const emptyP1: P1RedigerbarForm = {
  innehaver: {
    fornavn: "",
    etternavn: "",
    etternavnVedFoedsel: "",
    foedselsdato: "",
    adresselinje: "",
    poststed: "",
    postnummer: "",
    landkode: "",
  },
  forsikrede: {
    fornavn: "",
    etternavn: "",
    etternavnVedFoedsel: "",
    foedselsdato: "",
    adresselinje: "",
    poststed: "",
    postnummer: "",
    landkode: "",
  },
  sakstype: "ALDER",

  innvilgedePensjoner: createDefaultRows(emptyInnvilgetRow, 5),
  avslaattePensjoner: createDefaultRows(emptyAvslaattRow, 5),

  utfyllendeInstitusjon: {
    navn: "",
    adresselinje: "",
    poststed: "",
    postnummer: "",
    landkode: "",
    institusjonsID: "",
    faksnummer: "",
    telefonnummer: "",
    epost: "",
    dato: "",
  },
};
