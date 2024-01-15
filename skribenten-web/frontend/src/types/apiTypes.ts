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
