import type {
  P1AvslaattPensjon,
  P1InnvilgetPensjon,
  P1Institusjon,
  P1Person,
  P1Redigerbar,
  P1UtfyllendeInstitusjon,
} from "~/types/p1";
import type {
  P1AvslaattPensjonForm,
  P1InnvilgetPensjonForm,
  P1InstitusjonForm,
  P1PersonForm,
  P1RedigerbarForm,
  P1UtfyllendeInstitusjonForm,
} from "~/types/p1FormTypes";

import { emptyAvslaattRow, emptyInnvilgetRow } from "./emptyP1";

const emptyIfNull = (v: string | null | undefined): string => v ?? "";
const nullIfEmpty = (v: string): string | null => (v.trim() === "" ? null : v);

export const createDefaultRows = <T>(factory: () => T, count: number): T[] =>
  Array.from({ length: count }, () => factory());

export const MIN_ROWS = 5;

// Person
const mapPersonDtoToForm = (p: P1Person): P1PersonForm => ({
  fornavn: emptyIfNull(p.fornavn),
  etternavn: emptyIfNull(p.etternavn),
  etternavnVedFoedsel: emptyIfNull(p.etternavnVedFoedsel),
  foedselsdato: emptyIfNull(p.foedselsdato),
  adresselinje: emptyIfNull(p.adresselinje),
  poststed: emptyIfNull(p.poststed),
  postnummer: emptyIfNull(p.postnummer),
  landkode: emptyIfNull(p.landkode),
});

const mapPersonFormToDto = (p: P1PersonForm): P1Person => ({
  fornavn: nullIfEmpty(p.fornavn),
  etternavn: nullIfEmpty(p.etternavn),
  etternavnVedFoedsel: nullIfEmpty(p.etternavnVedFoedsel),
  foedselsdato: nullIfEmpty(p.foedselsdato),
  adresselinje: nullIfEmpty(p.adresselinje),
  poststed: nullIfEmpty(p.poststed),
  postnummer: nullIfEmpty(p.postnummer),
  landkode: nullIfEmpty(p.landkode),
});

// Institusjon
const mapInstitusjonDtoToForm = (i: P1Institusjon | null): P1InstitusjonForm => ({
  institusjonsnavn: emptyIfNull(i?.institusjonsnavn),
  pin: emptyIfNull(i?.pin),
  saksnummer: emptyIfNull(i?.saksnummer),
  vedtaksdato: emptyIfNull(i?.vedtaksdato),
  land: emptyIfNull(i?.land),
});

const mapInstitusjonFormToDto = (i: P1InstitusjonForm): P1Institusjon | null => {
  const hasAny =
    i.institusjonsnavn.trim() || i.pin.trim() || i.saksnummer.trim() || i.vedtaksdato.trim() || i.land.trim();

  if (!hasAny) return null;

  return {
    institusjonsnavn: nullIfEmpty(i.institusjonsnavn),
    pin: nullIfEmpty(i.pin),
    saksnummer: nullIfEmpty(i.saksnummer),
    vedtaksdato: nullIfEmpty(i.vedtaksdato),
    land: nullIfEmpty(i.land),
  };
};

// Innvilget
const mapInnvilgetDtoToForm = (p: P1InnvilgetPensjon): P1InnvilgetPensjonForm => ({
  institusjon: mapInstitusjonDtoToForm(p.institusjon),
  pensjonstype: p.pensjonstype ?? null,
  datoFoersteUtbetaling: emptyIfNull(p.datoFoersteUtbetaling),
  utbetalt: emptyIfNull(p.utbetalt),
  grunnlagInnvilget: p.grunnlagInnvilget ?? null,
  reduksjonsgrunnlag: p.reduksjonsgrunnlag ?? null,
  vurderingsperiode: emptyIfNull(p.vurderingsperiode),
  adresseNyVurdering: emptyIfNull(p.adresseNyVurdering),
});

const mapInnvilgetFormToDto = (p: P1InnvilgetPensjonForm): P1InnvilgetPensjon => ({
  institusjon: mapInstitusjonFormToDto(p.institusjon),
  pensjonstype: p.pensjonstype,
  datoFoersteUtbetaling: nullIfEmpty(p.datoFoersteUtbetaling),
  utbetalt: nullIfEmpty(p.utbetalt),
  grunnlagInnvilget: p.grunnlagInnvilget,
  reduksjonsgrunnlag: p.reduksjonsgrunnlag,
  vurderingsperiode: nullIfEmpty(p.vurderingsperiode),
  adresseNyVurdering: nullIfEmpty(p.adresseNyVurdering),
});

// Avslaatt
const mapAvslaattDtoToForm = (p: P1AvslaattPensjon): P1AvslaattPensjonForm => ({
  institusjon: mapInstitusjonDtoToForm(p.institusjon),
  pensjonstype: p.pensjonstype ?? null,
  avslagsbegrunnelse: p.avslagsbegrunnelse ?? null,
  vurderingsperiode: emptyIfNull(p.vurderingsperiode),
  adresseNyVurdering: emptyIfNull(p.adresseNyVurdering),
});

const mapAvslaattFormToDto = (p: P1AvslaattPensjonForm): P1AvslaattPensjon => ({
  institusjon: mapInstitusjonFormToDto(p.institusjon),
  pensjonstype: p.pensjonstype,
  avslagsbegrunnelse: p.avslagsbegrunnelse,
  vurderingsperiode: nullIfEmpty(p.vurderingsperiode),
  adresseNyVurdering: nullIfEmpty(p.adresseNyVurdering),
});

// Utfyllende institusjon
const mapUtfyllendeDtoToForm = (u: P1UtfyllendeInstitusjon): P1UtfyllendeInstitusjonForm => ({
  navn: u.navn,
  adresselinje: u.adresselinje,
  poststed: u.poststed,
  postnummer: u.postnummer,
  landkode: u.landkode,
  institusjonsID: emptyIfNull(u.institusjonsID),
  faksnummer: emptyIfNull(u.faksnummer),
  telefonnummer: emptyIfNull(u.telefonnummer),
  epost: emptyIfNull(u.epost),
  dato: u.dato,
});

const mapUtfyllendeFormToDto = (u: P1UtfyllendeInstitusjonForm): P1UtfyllendeInstitusjon => ({
  navn: u.navn,
  adresselinje: u.adresselinje,
  poststed: u.poststed,
  postnummer: u.postnummer,
  landkode: u.landkode,
  institusjonsID: nullIfEmpty(u.institusjonsID),
  faksnummer: nullIfEmpty(u.faksnummer),
  telefonnummer: nullIfEmpty(u.telefonnummer),
  epost: nullIfEmpty(u.epost),
  dato: u.dato,
});

// Helpers to check if a row has meaningful data
const isInnvilgetRowFilled = (p: P1InnvilgetPensjonForm): boolean => {
  const i = p.institusjon;
  return !!(
    i.institusjonsnavn.trim() ||
    i.pin.trim() ||
    i.saksnummer.trim() ||
    i.vedtaksdato.trim() ||
    i.land.trim() ||
    p.pensjonstype ||
    p.datoFoersteUtbetaling.trim() ||
    p.utbetalt.trim() ||
    p.grunnlagInnvilget ||
    p.reduksjonsgrunnlag ||
    p.vurderingsperiode.trim() ||
    p.adresseNyVurdering.trim()
  );
};

const isAvslaattRowFilled = (p: P1AvslaattPensjonForm): boolean => {
  const i = p.institusjon;
  return !!(
    i.institusjonsnavn.trim() ||
    i.pin.trim() ||
    i.saksnummer.trim() ||
    i.vedtaksdato.trim() ||
    i.land.trim() ||
    p.pensjonstype ||
    p.avslagsbegrunnelse ||
    p.vurderingsperiode.trim() ||
    p.adresseNyVurdering.trim()
  );
};

// Helper to pad array with empty rows
const padWithEmptyRows = <T>(arr: T[], minLength: number, emptyRowFactory: () => T): T[] => {
  if (arr.length >= minLength) return arr;
  return [...arr, ...createDefaultRows(emptyRowFactory, minLength - arr.length)];
};

// Root
export const mapP1DtoToForm = (p1: P1Redigerbar): P1RedigerbarForm => ({
  innehaver: mapPersonDtoToForm(p1.innehaver),
  forsikrede: mapPersonDtoToForm(p1.forsikrede),
  sakstype: p1.sakstype,
  innvilgedePensjoner: padWithEmptyRows(p1.innvilgedePensjoner.map(mapInnvilgetDtoToForm), MIN_ROWS, emptyInnvilgetRow),
  avslaattePensjoner: padWithEmptyRows(p1.avslaattePensjoner.map(mapAvslaattDtoToForm), MIN_ROWS, emptyAvslaattRow),
  utfyllendeInstitusjon: mapUtfyllendeDtoToForm(p1.utfyllendeInstitusjon),
});

export const mapP1FormToDto = (form: P1RedigerbarForm): P1Redigerbar => ({
  innehaver: mapPersonFormToDto(form.innehaver),
  forsikrede: mapPersonFormToDto(form.forsikrede),
  sakstype: form.sakstype,
  innvilgedePensjoner: form.innvilgedePensjoner.filter(isInnvilgetRowFilled).map(mapInnvilgetFormToDto),
  avslaattePensjoner: form.avslaattePensjoner.filter(isAvslaattRowFilled).map(mapAvslaattFormToDto),
  utfyllendeInstitusjon: mapUtfyllendeFormToDto(form.utfyllendeInstitusjon),
});
