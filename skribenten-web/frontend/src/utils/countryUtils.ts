import type { Nullable } from "~/types/Nullable";

type Land = {
  kode: string;
  navn: string;
};
/**
 * Hent landnavn basert på landkode
 */
export const getCountryNameByKode = (kode: Nullable<string>, landData: Land[]): Nullable<string> => {
  return landData.find((land) => land.kode === kode)?.navn ?? kode;
};
