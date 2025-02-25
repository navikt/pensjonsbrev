import type { Nullable } from "~/types/Nullable";

type Land = {
  kode: string;
  navn: string;
};
/**
 * Hent landnavn basert på landkode
 */
export const getCountryNameByKode = (kode: Nullable<string>, landData: Land[]): string => {
  if (!Array.isArray(landData)) {
    return kode || "";
  }

  if (landData) {
    const country = landData.find((land) => land.kode === kode);
    return country ? country.navn : kode || "";
  }

  return kode || "";
};
