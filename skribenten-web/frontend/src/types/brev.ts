import type { EditedLetter, PropertyUsage } from "~/types/brevbakerTypes";

import type { SpraakKode } from "./apiTypes";
import type { Nullable } from "./Nullable";

export type OpprettBrevRequest = {
  brevkode: string;
  spraak: SpraakKode;
  avsenderEnhetsId: Nullable<string>;
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
  valgteVedlegg: Nullable<VedleggKode[]>;
};

export interface DelvisOppdaterBrevRequest {
  laastForRedigering?: Nullable<boolean>;
  distribusjonstype?: Nullable<Distribusjonstype>;
  mottaker?: Nullable<Mottaker>;
  alltidValgbareVedlegg?: Nullable<VedleggKode[]>;
}

export interface DelvisOppdaterBrevResponse {
  info: BrevInfo;
  redigertBrev: EditedLetter;
  saksbehandlerValg: SaksbehandlerValg;
  valgteVedlegg: Nullable<VedleggKode[]>;
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
  avsenderEnhet: Nullable<NAVEnhet>;
  spraak: SpraakKode;
  journalpostId: Nullable<number>;
  vedtaksId: Nullable<number>;
  saksId: string;
};

export type BrevStatus = Kladd | UnderRedigering | Attestering | Klar | Arkivert;
export type Kladd = { type: "Kladd" };
export type UnderRedigering = { type: "UnderRedigering"; redigeresAv: NavAnsatt };
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

export enum VedleggKode {
  SKJEMA_FOR_BANKOPPLYSNINGER = "SKJEMA_FOR_BANKOPPLYSNINGER",
  UTTAKSSKJEMA = "UTTAKSSKJEMA",
}

export const VEDLEGG_KODE_TO_TEXT: Record<VedleggKode, string> = {
  [VedleggKode.SKJEMA_FOR_BANKOPPLYSNINGER]: "Skjema for bankopplysninger",
  [VedleggKode.UTTAKSSKJEMA]: "Uttaksskjema",
};
