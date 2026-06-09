import { SAMHANDLER_ENUM_TO_TEXT } from "~/types/nameMappings";

import { type ManueltAdressertTil } from "./brev";
import { type Nullable } from "./Nullable";
import type * as generated from "./skribenten-api";

export type SakDto = generated.Fagsak;
export type SakContextDto = generated.SakContext;
export type BrukerStatusDto = generated.BrukerStatus;
export type LetterMetadata = generated.Brevmal;
export type PreferredLanguage = generated.KontaktinfoResponse;

export type BrevSystem = generated.BrevSystem;
export const BrevSystem: Record<string, BrevSystem> = {
  Exstream: "EXSTREAM",
  Brevbaker: "BREVBAKER",
} as const;

export type SpraakKode = generated.SpraakKode;
export const SpraakKode: Record<string, SpraakKode> = {
  Bokmaal: "NB",
  Engelsk: "EN",
  Nynorsk: "NN",
  Fransk: "FR",
  NordSamisk: "SE",
} as const;

export type OrderExstreamLetterRequest = generated.BestillExstreamBrevRequest;
export type OrderEblankettRequest = generated.BestillEblankettRequest;
export type BestillOgRedigerBrevResponse = generated.BestillOgRedigerBrevResponse;
export type FailureType = generated.FailureType;

export type FinnSamhandlerRequestDto = generated.FinnSamhandlerRequestDto;
export type FinnSamhandlerResponseDto = generated.FinnSamhandlerResponseDto;
export type HentSamhandlerRequestDto = generated.HentSamhandlerRequestDto;
export type HentSamhandlerResponseDto = generated.HentSamhandlerResponseDto;
export type HentSamhandlerAdresseRequestDto = generated.HentSamhandlerAdresseRequestDto;
export type HentSamhandlerAdresseResponseDto = generated.HentSamhandlerAdresseResponseDto;
export type SamhandlerPostadresse = generated.SamhandlerPostadresse;

/*
  vi har '2' typer adresser vi kan få. Denne, og KontaktAdresseResponse. Dette formatet brukes for samhandler / manuell, mens KontaktAdresseResponse brukes ved getKontaktAdresse
  lage en type som omfatter begge - og rename denne? Eventuelt forholde oss til kun 1 adresseformat.
*/
export type Adresse = {
  navn: string;
  linje1: Nullable<string>;
  linje2: Nullable<string>;
  linje3: Nullable<string>;
  postnr: Nullable<string>;
  poststed: Nullable<string>;
  land: Nullable<string>;
  manueltAdressertTil: ManueltAdressertTil;
};

export type Samhandler = generated.NoNavPensjonBrevSkribentenRoutesSamhandlerDtoFinnSamhandlerResponseDtoSamhandler;
export type SamhandlerTypeCode = generated.SamhandlerTypeCode;
export const SamhandlerTypeCodes = Object.keys(SAMHANDLER_ENUM_TO_TEXT) as [
  SamhandlerTypeCode,
  ...SamhandlerTypeCode[],
];

export type KontaktAdresseResponse = {
  adresseString: string;
  adresselinjer: string[];
  type: AdresseType;
};

export type AdresseType =
  | "MATRIKKELADRESSE"
  | "POSTADRESSE_I_FRITT_FORMAT"
  | "POSTBOKSADRESSE"
  | "REGOPPSLAG_ADRESSE"
  | "UKJENT_BOSTED"
  | "UTENLANDSK_ADRESSE"
  | "UTENLANDSK_ADRESSE_I_FRITT_FORMAT"
  | "VEGADRESSE"
  | "VERGE_PERSON_POSTADRESSE"
  | "VERGE_SAMHANDLER_POSTADRESSE";

export type Avtaleland = {
  navn: string;
  kode: string;
};

export type Enhet = {
  id: string;
  navn: string;
};
