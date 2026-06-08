import { type ManueltAdressertTil } from "./brev";
import { type Nullable } from "./Nullable";
import type * as generated from "./skribenten-api";

export type SakDto = generated.Fagsak;
export type SakContextDto = generated.SakContext;
export type BrukerStatusDto = generated.BrukerStatus;
export type LetterMetadata = generated.Brevmal;
export type PreferredLanguage = generated.KontaktinfoResponse;

export type BrevSystem = generated.BrevSystem;
export const BrevSystem = {
  Exstream: "EXSTREAM",
  Brevbaker: "BREVBAKER",
} as const;

export type SpraakKode = generated.SpraakKode;
export const SpraakKode = {
  Bokmaal: "NB",
  Engelsk: "EN",
  Nynorsk: "NN",
  Fransk: "FR",
  NordSamisk: "SE",
} as const;

export type OrderExstreamLetterRequest = generated.BestillExstreamBrevRequest;
export type OrderEblankettRequest = generated.BestillEblankettRequest;
export type BestillOgRedigerBrevResponse = generated.BestillOgRedigerBrevResponse;
export type FailureType = generated.FailureType;

export type FinnSamhandlerRequestDto = generated.FinnSamhandlerRequestDto;
export type FinnSamhandlerResponseDto = generated.FinnSamhandlerResponseDto;

export type HentSamhandlerRequestDto = {
  idTSSEkstern: string;
  hentDetaljert: boolean;
};

export type HentSamhandlerAdresseResponseDto = {
  adresse: Adresse;
  failureType?: string;
};

/* 
  vi har '2' typer adresser vi kan få. Denne, og KontaktAdresseResponse. Dette formatet brukes for samhandler / manuell, mens KontaktAdresseResponse brukes ved getKontaktAdresse
  lage en type som omfatter begge - og rename denne? Eventuelt forholde oss til kun 1 adresseformat. 
*/
export type Adresse = {
  navn: string;
  linje1: Nullable<string>;
  linje2: Nullable<string>;
  linje3: Nullable<string>;
  postnr: Nullable<string>;
  poststed: Nullable<string>;
  land: Nullable<string>;
  manueltAdressertTil: ManueltAdressertTil;
};

export type HentSamhandlerAdresseRequestDto = {
  idTSSEkstern: string;
};

export type HentsamhandlerResponseDto = {
  success: Samhandler;
  failure: null | string;
};

export type Samhandler = generated.NoNavPensjonBrevSkribentenRoutesSamhandlerDtoFinnSamhandlerResponseDtoSamhandler;

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
  type: AdresseType;
};

export type AdresseType =
  | "MATRIKKELADRESSE"
  | "POSTADRESSE_I_FRITT_FORMAT"
  | "POSTBOKSADRESSE"
  | "REGOPPSLAG_ADRESSE"
  | "UKJENT_BOSTED"
  | "UTENLANDSK_ADRESSE"
  | "UTENLANDSK_ADRESSE_I_FRITT_FORMAT"
  | "VEGADRESSE"
  | "VERGE_PERSON_POSTADRESSE"
  | "VERGE_SAMHANDLER_POSTADRESSE";

export type Avtaleland = {
  navn: string;
  kode: string;
};

export type Enhet = {
  id: string;
  navn: string;
};
