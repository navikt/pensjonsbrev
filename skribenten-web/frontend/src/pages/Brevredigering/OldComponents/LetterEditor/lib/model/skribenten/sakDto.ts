export interface SakDto {
  readonly sakId: number;
  readonly foedselsnr: string;
  readonly foedselsdato: string;
  readonly sakType: SakType;
}

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
