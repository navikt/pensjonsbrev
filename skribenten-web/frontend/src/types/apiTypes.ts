export type SakDto = {
  readonly sakId: number;
  readonly foedselsnr: string;
  readonly foedselsdato: string;
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
export enum SpraakKode {
  Engelsk = "EN",
  Frank = "FR",
  Bokmaal = "NB",
  Nynorsk = "NN",
  NordSamisk = "SE",
}
