import type { EditedLetter, PropertyUsage } from "~/types/brevbakerTypes";

import type { SpraakKode } from "./apiTypes";
import type { Nullable } from "./Nullable";

export type OpprettBrevRequest = {
  brevkode: string;
  spraak: SpraakKode;
  avsenderEnhetsId: string;
  saksbehandlerValg: SaksbehandlerValg;
  mottaker: Nullable<Mottaker>;
  vedtaksId: Nullable<number>;
};

export type SaksbehandlerValg = {
  [key: string]: SaksbehandlerValg | number | boolean | string | null;
};

export type BrevResponse = {
  info: BrevInfo;
  redigertBrev: EditedLetter;
  redigertBrevHash: string;
  saksbehandlerValg: SaksbehandlerValg;
  propertyUsage: Nullable<PropertyUsage[]>;
  valgteVedlegg: Nullable<AlltidValgbartVedlegg[]>;
};

export interface OppdaterKlarStatusRequest {
  klar: boolean;
}

export interface DistribusjonstypeRequest {
  distribusjon: Distribusjonstype;
}

export interface OppdaterMottakerRequest {
  mottaker: Mottaker;
}

export interface OppdaterVedleggRequest {
  alltidValgbareVedlegg: AlltidValgbartVedlegg[];
}

export interface BestillBrevResponse {
  journalpostId: Nullable<number>;
  error: Nullable<BestillBrevError>;
}

export interface BestillBrevError {
  brevIkkeStoettet: Nullable<string>;
  tekniskgrunn: Nullable<string>;
  beskrivelse: Nullable<string>;
}

export type ReservasjonResponse = {
  vellykket: boolean;
  reservertAv: NavAnsatt;
  expiresIn: string;
  redigertBrevHash: string;
};

export type NavAnsatt = {
  id: string;
  navn: string | null;
};
export type BrevType = "VEDTAKSBREV" | "INFORMASJONSBREV";

export type BrevInfo = {
  id: number;
  opprettetAv: NavAnsatt;
  opprettet: string;
  sistredigertAv: NavAnsatt;
  sistredigert: string;
  brevkode: string;
  brevtittel: string;
  brevtype: BrevType;
  status: BrevStatus;
  distribusjonstype: Distribusjonstype;
  mottaker: Nullable<Mottaker>;
  avsenderEnhet: NAVEnhet;
  spraak: SpraakKode;
  journalpostId: Nullable<number>;
  vedtaksId: Nullable<number>;
  saksId: string;
};

export type BrevStatus = Kladd | UnderRedigering | Attestering | Klar | Arkivert;
export type Kladd = { type: "Kladd" };
export type UnderRedigering = {
  type: "UnderRedigering";
  redigeresAv: NavAnsatt;
};
export type Attestering = { type: "Attestering" };
export type Klar = { type: "Klar" };
export type Arkivert = { type: "Arkivert" };

export type OppdaterBrevRequest = {
  saksbehandlerValg: SaksbehandlerValg;
  redigertBrev: EditedLetter;
};

export enum Distribusjonstype {
  SENTRALPRINT = "SENTRALPRINT",
  LOKALPRINT = "LOKALPRINT",
}

export type Mottaker = Samhandler | NorskAdresse | UtenlandskAdresse;

export interface Samhandler {
  type: "Samhandler";
  tssId: string;
  navn: Nullable<string>;
}

export enum ManueltAdressertTil {
  BRUKER = "BRUKER",
  ANNEN = "ANNEN",
  IKKE_RELEVANT = "IKKE_RELEVANT",
}

export interface NorskAdresse {
  type: "NorskAdresse";
  navn: string;
  postnummer: string;
  poststed: string;
  adresselinje1: Nullable<string>;
  adresselinje2: Nullable<string>;
  adresselinje3: Nullable<string>;
  manueltAdressertTil: ManueltAdressertTil;
}

export interface UtenlandskAdresse {
  type: "UtenlandskAdresse";
  navn: string;
  adresselinje1: string;
  adresselinje2: Nullable<string>;
  adresselinje3: Nullable<string>;
  landkode: string;
  manueltAdressertTil: ManueltAdressertTil;
}

export interface NAVEnhet {
  enhetNr: string;
  navn: string;
}

export type VedleggKode = string;

export const P1_BREVKODE = "P1_SAMLET_MELDING_OM_PENSJONSVEDTAK_V2";

export interface AlltidValgbartVedlegg {
  kode: string;
  visningstekst: string;
}
