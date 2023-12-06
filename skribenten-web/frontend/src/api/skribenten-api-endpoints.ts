// "When accessing a member from an await expression, the await expression has to be parenthesized, which is not readable."
// For the purpose of this file it is convenient to be able to access the data property of axios response as a one-liners.
/* eslint-disable unicorn/no-await-expression-member*/

import axios from "axios";

import type { LetterTemplatesResponse, PreferredLanguage, SakDto } from "../types/apiTypes";
const SKRIBENTEN_API_BASE_PATH = "/skribenten-backend";

/**
 * Anbefalt lesing for react-query key factory pattern: https://tkdodo.eu/blog/effective-react-query-keys
 */

export const saksnummerKeys = {
  all: ["SAK"] as const,
  id: (sakId: string) => [...saksnummerKeys.all, sakId] as const,
};

export const navnKeys = {
  all: ["NAVN"] as const,
  id: (fnr: string) => [...navnKeys.all, fnr] as const,
};

export const letterTemplatesKeys = {
  all: ["LETTER_TEMPLATES"] as const,
  id: (sakType: string) => [...letterTemplatesKeys.all, sakType] as const,
};

export const favoritterKeys = {
  all: ["FAVORITTER"] as const,
};

export const preferredLanguageKeys = {
  all: ["PREFERRED_LANGUAGE"] as const,
  fnr: (fnr: string) => [...preferredLanguageKeys.all, fnr] as const,
};

export const getSak = {
  queryKey: saksnummerKeys.id,
  queryFn: async (sakId: string) => (await axios.get<SakDto>(`${SKRIBENTEN_API_BASE_PATH}/pen/sak/${sakId}`)).data,
};

export const getNavn = {
  queryKey: navnKeys.id,
  queryFn: async (fnr: string) => (await axios.get<string>(`${SKRIBENTEN_API_BASE_PATH}/pdl/navn/${fnr}`)).data,
};

export const getPreferredLanguage = {
  queryKey: preferredLanguageKeys.fnr,
  queryFn: async (fnr: string) =>
    (await axios.get<PreferredLanguage>(`${SKRIBENTEN_API_BASE_PATH}/foretrukketSpraak/${fnr}`)).data,
};

export const getLetterTemplate = {
  queryKey: letterTemplatesKeys.id,
  queryFn: async (sakType: string) =>
    (await axios.get<LetterTemplatesResponse>(`${SKRIBENTEN_API_BASE_PATH}/lettertemplates/${sakType}`)).data,
};

export const getFavoritter = {
  queryKey: favoritterKeys.all,
  queryFn: async () => (await axios.get<string[]>(`${SKRIBENTEN_API_BASE_PATH}/favourites`)).data,
};

export async function addFavoritt(id: string) {
  return (
    await axios.post<string>(`${SKRIBENTEN_API_BASE_PATH}/favourites`, id, {
      headers: { "Content-Type": "text/plain" },
    })
  ).data;
}

export async function deleteFavoritt(id: string) {
  return (await axios.delete<string>(`${SKRIBENTEN_API_BASE_PATH}/favourites`, { data: id })).data;
}
