import type { EditedLetter } from "~/types/brevbakerTypes";

import type { SpraakKode } from "./apiTypes";
import type { Nullable } from "./Nullable";

export type OpprettBrevRequest = {
  brevkode: string;
  spraak: SpraakKode;
  avsenderEnhetsId: Nullable<string>;
  saksbehandlerValg: SaksbehandlerValg;
};

export type SaksbehandlerValg = {
  [key: string]: SaksbehandlerValg | number | boolean | string | null;
};

export type BrevResponse = {
  info: BrevInfo;
  redigertBrev: EditedLetter;
  redigertBrevHash: string;
  saksbehandlerValg: SaksbehandlerValg;
};

export interface DelvisOppdaterBrevRequest {
  sakId: string;
  brevId: string | number;
  laastForRedigering?: Nullable<boolean>;
  distribusjonstype?: Nullable<Distribusjonstype>;
}

export interface DelvisOppdaterBrevResponse {
  info: BrevInfo;
  redigertBrev: EditedLetter;
  saksbehandlerValg: SaksbehandlerValg;
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
  timestamp: string;
  expiresIn: string;
  redigertBrevHash: string;
};

export type NavAnsatt = {
  id: string;
  navn: string;
};

export type BrevInfo = {
  id: number;
  opprettetAv: string;
  opprettet: string;
  sistredigertAv: string;
  sistredigert: string;
  brevkode: string;
  brevtittel: string;
  status: BrevStatus;
  distribusjonstype: Distribusjonstype;
};
export type BrevStatus = Kladd | UnderRedigering | Klar;
export type Kladd = { type: "Kladd" };
export type UnderRedigering = { type: "UnderRedigering"; redigeresAv: string };
export type Klar = { type: "Klar" };

export type OppdaterBrevRequest = {
  saksbehandlerValg: SaksbehandlerValg;
  redigertBrev: EditedLetter;
};

export enum Distribusjonstype {
  SENTRALPRINT = "SENTRALPRINT",
  LOKALPRINT = "LOKALPRINT",
}
