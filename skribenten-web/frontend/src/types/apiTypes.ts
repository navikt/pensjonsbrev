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

export type FinnSamhandlerRequestDto = {
  navn: string;
  samhandlerType: SamhandlerTypeCode;
};

export type HentSamhandlerRequestDto = {
  idTSSEkstern: string;
  hentDetaljert: boolean;
};

export type HentSamhandlerAdresseResponseDto = {
  adresse: SamhandlerPostadresse;
  failureType?: string;
};
export type SamhandlerPostadresse = {
  navn: string;
  linje1?: string;
  linje2?: string;
  linje3?: string;
  postnr?: string;
  poststed?: string;
  land?: string;
};

export type HentSamhandlerAdresseRequestDto = {
  idTSSEkstern: string;
};

export type HentsamhandlerResponseDto = {
  success: Samhandler;
  failure: null | string;
};

export type Samhandler = {
  navn: string;
  samhandlerType: string;
  offentligId: string;
  idType: string;
  idTSSEkstern: string;
};

export type FinnSamhandlerResponseDto = {
  samhandlere: Samhandler[];
};

export enum SamhandlerTypeCode {
  AA = "AA",
  ADVO = "ADVO",
  AFPO = "AFPO",
  AK = "AK",
  AMI = "AMI",
  AN = "AN",
  AP = "AP",
  APOP = "APOP",
  APOS = "APOS",
  ART = "ART",
  ASEN = "ASEN",
  AT = "AT",
  ATU = "ATU",
  AU = "AU",
  BAND = "BAND",
  BB = "BB",
  BBE = "BBE",
  BEH = "BEH",
  BF = "BF",
  BH = "BH",
  BHT = "BHT",
  BI = "BI",
  BU = "BU",
  EOS = "EOS",
  ET = "ET",
  FA = "FA",
  FB = "FB",
  FFU = "FFU",
  FKL = "FKL",
  FL = "FL",
  FO = "FO",
  FS = "FS",
  FT = "FT",
  FTK = "FTK",
  FULL = "FULL",
  GVS = "GVS",
  HE = "HE",
  HF = "HF",
  HFO = "HFO",
  HJS = "HJS",
  HMS = "HMS",
  HP = "HP",
  HPRD = "HPRD",
  HS = "HS",
  HSTA = "HSTA",
  HSV = "HSV",
  HTF = "HTF",
  HU = "HU",
  INST = "INST",
  IS = "IS",
  JO = "JO",
  KA = "KA",
  KE = "KE",
  KEMN = "KEMN",
  KI = "KI",
  KOMM = "KOMM",
  KON = "KON",
  KRED = "KRED",
  KRU = "KRU",
  KYND = "KYND",
  LABO = "LABO",
  LABP = "LABP",
  LARO = "LARO",
  LBS = "LBS",
  LE = "LE",
  LEBE = "LEBE",
  LEFA = "LEFA",
  LEKO = "LEKO",
  LERA = "LERA",
  LFK = "LFK",
  LHK = "LHK",
  LK = "LK",
  LM = "LM",
  LOGO = "LOGO",
  LRB = "LRB",
  LS = "LS",
  MT = "MT",
  NB = "NB",
  OA = "OA",
  OI = "OI",
  OM = "OM",
  OP = "OP",
  OR = "OR",
  ORVE = "ORVE",
  PE = "PE",
  PF = "PF",
  POLI = "POLI",
  PPT = "PPT",
  PS = "PS",
  RA = "RA",
  REGO = "REGO",
  RF = "RF",
  RFK = "RFK",
  RHFO = "RHFO",
  RKK = "RKK",
  ROL = "ROL",
  RONO = "RONO",
  RONP = "RONP",
  RT = "RT",
  RTK = "RTK",
  RTV = "RTV",
  SAD = "SAD",
  SFK = "SFK",
  SK = "SK",
  SP = "SP",
  SR = "SR",
  STKO = "STKO",
  SUD = "SUD",
  SYKH = "SYKH",
  TEPE = "TEPE",
  TH = "TH",
  TI = "TI",
  TK = "TK",
  TL = "TL",
  TLO = "TLO",
  TOLK = "TOLK",
  TP = "TP",
  TR = "TR",
  TRAN = "TRAN",
  TT = "TT",
  UTL = "UTL",
  VE = "VE",
  VIRT = "VIRT",
  VP = "VP",
  X = "X",
  YH = "YH",
  YM = "YM",
}

export type KontaktAdresseResponse = {
  adresseString: string;
  adresselinjer: string[];
};
