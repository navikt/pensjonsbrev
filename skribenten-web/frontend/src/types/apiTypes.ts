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

export type DokumentkategoriCode = "B" | "E_BLANKETT" | "IB" | "SED" | "VB";

export type LetterMetadata = {
  name: string;
  id: string;
  brevsystem: BrevSystem;
  spraak: SpraakKode[];
  brevkategoriCode?: BrevkategoriCode;
  dokumentkategoriCode?: DokumentkategoriCode;
};

export enum BrevSystem {
  Exstream = "EXSTREAM",
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
  | "EXSTREAM_REDIGERING_GENERELL"
  | "TJENESTEBUSS_INTEGRASJON"
  | "EXSTREAM_BESTILLING_ADRESSE_MANGLER"
  | "EXSTREAM_BESTILLING_HENTE_BREVDATA"
  | "EXSTREAM_BESTILLING_MANGLER_OBLIGATORISK_INPUT"
  | "EXSTREAM_BESTILLING_OPPRETTE_JOURNALPOST";

export type KontaktAdresseResponse = {
  adresseString: string;
  adresselinjer: string[];
};
