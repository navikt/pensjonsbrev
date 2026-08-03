import { SAMHANDLER_ENUM_TO_TEXT } from "~/types/nameMappings";

import { type ManueltAdressertTil } from "./brev";
import { type Nullable } from "./Nullable";
import type * as generated from "./skribenten-api";

export type SakDto = generated.Fagsak;
export type SakContextDto = generated.ApiSakContext;
export type BrukerStatusDto = generated.ApiBrukerStatus;
export type LetterMetadata = generated.ApiBrevmal;
export type PreferredLanguage = generated.KrrServiceKontaktinfoResponse;

export type BrevSystem = generated.BrevSystem;
export const BrevSystem: Record<string, BrevSystem> = {
  Exstream: "EXSTREAM",
  Brevbaker: "BREVBAKER",
};

export type SpraakKode = generated.SpraakKode;
export const SpraakKode: Record<string, SpraakKode> = {
  Bokmaal: "NB",
  Engelsk: "EN",
  Nynorsk: "NN",
  Fransk: "FR",
  NordSamisk: "SE",
};

export type OrderExstreamLetterRequest = generated.ApiBestillExstreamBrevRequest;
export type OrderEblankettRequest = generated.ApiBestillEblankettRequest;
export type BestillOgRedigerBrevResponse = generated.ApiBestillOgRedigerBrevResponse;
export type FailureType = generated.ApiBestillOgRedigerBrevResponseFailureType;

export type FinnSamhandlerRequestDto = generated.FinnSamhandlerRequestDto;
export type FinnSamhandlerResponseDto = generated.FinnSamhandlerResponseDto;
export type HentSamhandlerRequestDto = generated.HentSamhandlerRequestDto;
export type HentSamhandlerResponseDto = generated.HentSamhandlerResponseDto;
export type HentSamhandlerAdresseRequestDto = generated.HentSamhandlerAdresseRequestDto;
export type HentSamhandlerAdresseResponseDto = generated.HentSamhandlerAdresseResponseDto;
export type SamhandlerPostadresse = generated.HentSamhandlerAdresseResponseDtoSamhandlerPostadresse;

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

export type Samhandler = generated.FinnSamhandlerResponseDtoSamhandler;
export type SamhandlerTypeCode = generated.SamhandlerTypeCode;
export const SamhandlerTypeCodes = Object.keys(SAMHANDLER_ENUM_TO_TEXT) as [
  SamhandlerTypeCode,
  ...SamhandlerTypeCode[],
];

export type KontaktAdresseResponse = generated.KontaktAdresseDto;
export type Avtaleland = generated.PenAvtaleland;
export type NavAnsattEnhet = generated.NavAnsattEnhet;
