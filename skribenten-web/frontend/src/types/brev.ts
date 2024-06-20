import type { EditedLetter } from "~/types/brevbakerTypes";

export type OpprettBrevRequest = {
  brevkode: string;
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
  brevkode: string;
  saksbehandlerValg: SaksbehandlerValg;
  redigertBrev: EditedLetter;
};
