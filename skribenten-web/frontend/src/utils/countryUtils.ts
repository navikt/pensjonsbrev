import type { Nullable } from "~/types/Nullable";

type Land = {
  kode: string;
  navn: string;
};
/**
 * Hent landnavn basert på landkode
 */
export const getCountryNameByKode = (kode: Nullable<string>, landData: Land[] | undefined): Nullable<string> => {
  if (!landData) return kode;
  const country = landData.find((land) => land.kode === kode);
  return country ? country.navn : kode;
};
