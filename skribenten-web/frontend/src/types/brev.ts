import type { EditedLetter } from "~/types/brevbakerTypes";

import type { SpraakKode } from "./apiTypes";

export type OpprettBrevRequest = {
  brevkode: string;
  spraak: SpraakKode;
  avsenderEnhet: string;
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
  redigeresAv: string | null;
};

export type OppdaterBrevRequest = {
  saksbehandlerValg: SaksbehandlerValg;
  redigertBrev: EditedLetter;
};
