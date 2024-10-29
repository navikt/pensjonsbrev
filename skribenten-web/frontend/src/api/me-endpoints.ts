// "When accessing a member from an await expression, the await expression has to be parenthesized, which is not readable."
// For the purpose of this file it is convenient to be able to access the data property of axios response as a one-liners.
/* eslint-disable unicorn/no-await-expression-member*/

import axios from "axios";

import type { FavorittSamhandlerMottaker } from "~/routes/saksnummer_/$saksId/brevvelger/-components/endreMottaker/Favoritter";
import type { Enhet } from "~/types/apiTypes";
import { SamhandlerTypeCode } from "~/types/apiTypes";

import { SKRIBENTEN_API_BASE_PATH } from "./skribenten-api-endpoints";

export const favoritterKeys = {
  all: ["FAVORITTER"] as const,
};

export const enheterKeys = {
  all: ["ENHETER"] as const,
};

export const getBrevFavoritter = {
  queryKey: favoritterKeys.all,
  queryFn: async () => (await axios.get<string[]>(`${SKRIBENTEN_API_BASE_PATH}/me/favourites`)).data,
};

export const getEnheter = {
  queryKey: enheterKeys.all,
  queryFn: async () => (await axios.get<Enhet[]>(`${SKRIBENTEN_API_BASE_PATH}/me/enheter`)).data,
};

export async function addBrevFavoritt(id: string) {
  return (
    await axios.post<string>(`${SKRIBENTEN_API_BASE_PATH}/me/favourites`, id, {
      headers: { "Content-Type": "text/plain" },
    })
  ).data;
}

export async function deleteBrevFavoritt(id: string) {
  return (await axios.delete<string>(`${SKRIBENTEN_API_BASE_PATH}/me/favourites`, { data: id })).data;
}

export async function getMottakerFavoritter() {
  //return (await axios.get<FavorittSamhandlerMottaker[]>(`${SKRIBENTEN_API_BASE_PATH}/me/mottaker-favoritter`)).data;

  return [
    { id: "1", navn: "Test Testerson", type: SamhandlerTypeCode.AA },
    { id: "2", navn: "Navn Navnesen", type: SamhandlerTypeCode.APOP },
    { id: "3", navn: "Favourite samhandler", type: SamhandlerTypeCode.FT },
    { id: "4", navn: "Donald Duck", type: SamhandlerTypeCode.PE },
    { id: "5", navn: "Duck Donald ", type: SamhandlerTypeCode.VIRT },
  ];
}

export async function addMottakerFavoritt() {
  return (await axios.patch<FavorittSamhandlerMottaker[]>(`${SKRIBENTEN_API_BASE_PATH}/me/mottaker-favoritter`)).data;
}

export async function deleteMottakerFavoritt(id: string) {
  return (await axios.delete<FavorittSamhandlerMottaker[]>(`${SKRIBENTEN_API_BASE_PATH}/me/mottaker-favoritter/${id}`))
    .data;
}
