// "When accessing a member from an await expression, the await expression has to be parenthesized, which is not readable."
// For the purpose of this file it is convenient to be able to access the data property of axios response as a one-liners.
/* eslint-disable unicorn/no-await-expression-member*/

import axios from "axios";

import type { BrevInfo } from "~/types/brev";

import { SKRIBENTEN_API_BASE_PATH } from "./skribenten-api-endpoints";

export const hentAlleBrevForSak = {
  queryKey: ["hentAlleBrevForSak"],
  queryFn: async (saksId: string) => hentAlleBrevForSakFunction(saksId),
};

const hentAlleBrevForSakFunction = async (saksId: string) =>
  (await axios.get<BrevInfo[]>(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev`)).data;

export const lagPdfForBrev = async (saksId: string, brevId: string) => {
  return (
    await axios.post(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev/${brevId}/pdf`, undefined, {
      responseType: "blob",
      headers: { Accept: "application/pdf" },
    })
  ).data;
};

export const hentPdfForBrev = {
  queryKey: ["hentPdfForBrev"],
  queryFn: async (saksId: string, brevId: string) => hentPdfForBrevFunction(saksId, brevId),
};

export const hentPdfForBrevFunction = async (saksId: string, brevId: string) =>
  (
    await axios.get(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev/${brevId}/pdf`, {
      responseType: "blob",
      headers: { Accept: "application/pdf" },
    })
  ).data;

export const slettBrev = async (saksId: string, brevId: string) =>
  (await axios.delete(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev/${brevId}`)).data;
