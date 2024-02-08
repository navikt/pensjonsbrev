import type { SakType } from "./apiTypes";
import { SpraakKode } from "./apiTypes";

export const SPRAAK_ENUM_TO_TEXT = {
  [SpraakKode.Bokmaal]: "Bokmål",
  [SpraakKode.Nynorsk]: "Nynorsk",
  [SpraakKode.Engelsk]: "Engelsk",
};

export const SAK_TYPE_TO_TEXT: Record<SakType, string> = {
  AFP: "AFP",
  AFP_PRIVAT: "AFP Privat",
  ALDER: "Alderspensjon",
  BARNEP: "Barnepensjon",
  FAM_PL: "Familiepleierytelse",
  GAM_YRK: "Gammel yrkesskade",
  GENRL: "Generell",
  GJENLEV: "Gjenlevendeytelse",
  GRBL: "Grunnblanketter",
  KRIGSP: "Krigspensjon",
  OMSORG: "Omsorgopptjening",
  UFOREP: "Uføretrygd",
};
