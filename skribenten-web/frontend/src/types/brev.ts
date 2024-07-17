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
  saksbehandlerValg: SaksbehandlerValg;
};

export type BrevInfo = {
  id: number;
  opprettetAv: string;
  opprettet: string;
  sistredigertAv: string;
  sistredigert: string;
  brevkode: string;
  status: BrevInfoStatus;
};

export interface BrevInfoStatus {
  type: BrevInfoStatusType;
  /**
   * eksisterer kun dersom type er UNDER_REDIGERING
   */
  redigeresAv?: string;
}

export enum BrevInfoStatusType {
  KLADD = "Kladd",
  KLAR = "Klar",
  UNDER_REDIGERING = "UnderRedigering",
}

export type OppdaterBrevRequest = {
  saksbehandlerValg: SaksbehandlerValg;
  redigertBrev: EditedLetter;
};
