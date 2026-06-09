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

export type OpprettBrevRequest = generated.OpprettBrevRequest;
export type OppdaterKlarStatusRequest = generated.OppdaterKlarStatusRequest;
export type DistribusjonstypeRequest = generated.DistribusjonstypeRequest;
export type OppdaterMottakerRequest = generated.OppdaterMottakerRequest;
export type ValgteVedleggRequest = generated.ValgteVedleggRequest;
export type BestillBrevResponse = generated.BestillBrevResponse;
export type BestillBrevError = generated.Error;
export type ReservasjonResponse = generated.ReservasjonResponse;
export type NavAnsatt = generated.NavAnsatt;
export type BrevType = generated.Brevtype;
export type BrevInfo = generated.BrevInfo;
export type BrevStatus = generated.BrevStatus;

export type OppdaterBrevRequest = {
  saksbehandlerValg: SaksbehandlerValg;
  redigertBrev: EditedLetter;
};

export type Distribusjonstype = generated.Distribusjon;
export const Distribusjonstype: Record<Distribusjonstype, Distribusjonstype> = {
  SENTRALPRINT: "SENTRALPRINT",
  LOKALPRINT: "LOKALPRINT",
};

export type Mottaker = generated.OverstyrtMottaker;
export type Samhandler = generated.Samhandler;
export type ManueltAdressertTil = generated.ManueltAdressertTil;
export const ManueltAdressertTil = {
  BRUKER: "BRUKER",
  ANNEN: "ANNEN",
  IKKE_RELEVANT: "IKKE_RELEVANT",
} as const;
export type NorskAdresse = generated.NorskAdresse;
export type UtenlandskAdresse = generated.UtenlandskAdresse;

export type NAVEnhet = generated.NavEnhet;

export const P1_BREVKODE = "P1_SAMLET_MELDING_OM_PENSJONSVEDTAK_V2";

export type AlltidValgbartVedlegg = generated.ValgbartVedlegg;
