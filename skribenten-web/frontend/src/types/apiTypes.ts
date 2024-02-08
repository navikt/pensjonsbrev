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

export type BrevkategoriCode =
  | "BREV_MED_SKJEMA"
  | "INFORMASJON"
  | "INNHENTE_OPPL"
  | "NOTAT"
  | "OVRIG"
  | "VARSEL"
  | "VEDTAK";

export type LetterMetadata = {
  name: string;
  id: string;
  brevsystem: BrevSystem;
  spraak: SpraakKode[];
  brevkategoriCode?: BrevkategoriCode;
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
  isSensitive: boolean;
  vedtaksId?: string;
};

export type BestillOgRedigerBrevResponse = {
  url?: string;
  failureType?: FailureType;
};

export type FailureType =
  | "DOKSYS_UNDER_REDIGERING"
  | "DOKSYS_IKKE_REDIGERBART"
  | "DOKSYS_VALIDERING_FEILET"
  | "DOKSYS_IKKE_FUNNET"
  | "DOKSYS_IKKE_TILGANG"
  | "DOKSYS_LUKKET"
  | "FERDIGSTILLING_TIMEOUT"
  | "SAF_ERROR"
  | "SKRIBENTEN_TOKEN_UTVEKSLING"
  | "EXTREAM_REDIGERING_GENERELL"
  | "TJENESTEBUSS_INTEGRASJON"
  | "EXTREAM_BESTILLING_ADRESSE_MANGLER"
  | "EXTREAM_BESTILLING_HENTE_BREVDATA"
  | "EXTREAM_BESTILLING_MANGLER_OBLIGATORISK_INPUT"
  | "EXTREAM_BESTILLING_OPPRETTE_JOURNALPOST";

export type KontaktAdresseResponse = {
  adresseString: string;
  adresselinjer: string[];
};

export type Avtaleland = {
  navn: string;
  kode: string;
};
