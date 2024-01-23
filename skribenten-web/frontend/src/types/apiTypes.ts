export type SakDto = {
  readonly sakId: number;
  readonly foedselsnr: string;
  readonly foedselsdato: [number, number, number];
  readonly sakType: SakType;
};

export type SakType =
  | "AFP"
  | "AFP_PRIVAT"
  | "ALDER"
  | "BARNEP"
  | "FAM_PL"
  | "GAM_YRK"
  | "GENRL"
  | "GJENLEV"
  | "GRBL"
  | "KRIGSP"
  | "OMSORG"
  | "UFOREP";

export type LetterTemplatesResponse = {
  kategorier: LetterCategory[];
  eblanketter: LetterMetadata[];
};

export type LetterCategory = {
  name: string;
  templates: LetterMetadata[];
};

export type LetterMetadata = {
  name: string;
  id: string;
  brevsystem: BrevSystem;
  spraak: SpraakKode[];
  isVedtaksbrev: boolean;
  isEblankett: boolean;
};

export enum BrevSystem {
  Extream = "EXTREAM",
  DokSys = "DOKSYS",
  Brevbaker = "BREVBAKER",
}

export type PreferredLanguage = {
  spraakKode: SpraakKode;
};

export enum SpraakKode {
  Engelsk = "EN",
  Bokmaal = "NB",
  Nynorsk = "NN",
}

export type PidRequest = {
  pid: string;
};

export type OrderLetterRequest = {
  brevkode: string;
  spraak: SpraakKode;
  sakId: number;
  gjelderPid: string;
  landkode?: string;
  mottakerText?: string;
  enhetsId: string;
};
