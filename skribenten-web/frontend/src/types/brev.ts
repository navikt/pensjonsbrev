import { type EditedLetter, type PropertyUsage } from "~/types/brevbakerTypes";

import { type Nullable } from "./Nullable";
import type * as generated from "./skribenten-api";

export type SaksbehandlerValg = {
  [key: string]: SaksbehandlerValg | SaksbehandlerValg[] | number | boolean | string | null;
};

export type BrevResponse = {
  info: BrevInfo;
  redigertBrev: EditedLetter;
  redigertBrevHash: string;
  saksbehandlerValg: SaksbehandlerValg;
  propertyUsage: Nullable<PropertyUsage[]>;
  valgteVedlegg: Nullable<AlltidValgbartVedlegg[]>;
};

export type OpprettBrevRequest = generated.ApiOpprettBrevRequest;
export type OppdaterKlarStatusRequest = generated.ApiOppdaterKlarStatusRequest;
export type DistribusjonstypeRequest = generated.ApiDistribusjonstypeRequest;
export type OppdaterMottakerRequest = generated.ApiOppdaterMottakerRequest;
export type ValgteVedleggRequest = generated.ApiValgteVedleggRequest;
export type BestillBrevResponse = generated.ApiBestillBrevResponse;
export type BestillBrevError = generated.ApiBestillBrevResponseError;
export type ReservasjonResponse = generated.ApiReservasjonResponse;
export type NavAnsatt = generated.ApiNavAnsatt;
export type BrevType = generated.LetterMetadataBrevtype;
export type BrevInfo = generated.ApiBrevInfo;
export type BrevStatus = generated.ApiBrevStatus;

export type OppdaterBrevRequest = {
  saksbehandlerValg: SaksbehandlerValg;
  redigertBrev: EditedLetter;
};

export type Distribusjonstype = generated.Distribusjon;
export const Distribusjonstype: Record<Distribusjonstype, Distribusjonstype> = {
  SENTRALPRINT: "SENTRALPRINT",
  LOKALPRINT: "LOKALPRINT",
};

export type Mottaker = generated.ApiOverstyrtMottaker;
export type Samhandler = generated.ApiOverstyrtMottakerSamhandler;
export type ManueltAdressertTil = generated.DtoMottakerManueltAdressertTil;
export const ManueltAdressertTil = {
  BRUKER: "BRUKER",
  ANNEN: "ANNEN",
  IKKE_RELEVANT: "IKKE_RELEVANT",
} as const;
export type NorskAdresse = generated.ApiOverstyrtMottakerNorskAdresse;
export type UtenlandskAdresse = generated.ApiOverstyrtMottakerUtenlandskAdresse;

export type NAVEnhet = generated.NavEnhet;

export const P1_BREVKODE = "P1_SAMLET_MELDING_OM_PENSJONSVEDTAK_V2";

export type AlltidValgbartVedlegg = generated.ValgbartVedlegg;
