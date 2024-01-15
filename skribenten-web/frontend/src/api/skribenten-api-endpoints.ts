// "When accessing a member from an await expression, the await expression has to be parenthesized, which is not readable."
// For the purpose of this file it is convenient to be able to access the data property of axios response as a one-liners.
/* eslint-disable unicorn/no-await-expression-member*/

import type { AxiosResponse } from "axios";
import axios from "axios";

import type { LetterTemplatesResponse, PidRequest, PreferredLanguage, SakDto } from "~/types/apiTypes";
import type { RedigerbarTemplateDescription, RenderedLetter } from "~/types/brevbakerTypes";
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
  pid: (pid: string) => [...navnKeys.all, pid] as const,
};

export const letterTemplatesKeys = {
  all: ["LETTER_TEMPLATES"] as const,
  id: (sakType: string) => [...letterTemplatesKeys.all, sakType] as const,
};

export const letterKeys = {
  all: ["LETTER"] as const,
  brevkode: (brevkode: string) => [...letterKeys.all, brevkode] as const,
};

export const favoritterKeys = {
  all: ["FAVORITTER"] as const,
};

export const preferredLanguageKeys = {
  all: ["PREFERRED_LANGUAGE"] as const,
  pid: (pid: string) => [...preferredLanguageKeys.all, pid] as const,
};

export const getSak = {
  queryKey: saksnummerKeys.id,
  queryFn: async (sakId: string) => (await axios.get<SakDto>(`${SKRIBENTEN_API_BASE_PATH}/pen/sak/${sakId}`)).data,
};

export const getNavn = {
  queryKey: navnKeys.pid,
  queryFn: async (pid: string) =>
    (await axios.post<PidRequest, AxiosResponse<string>>(`${SKRIBENTEN_API_BASE_PATH}/person/navn`, { pid })).data,
};

export const getPreferredLanguage = {
  queryKey: preferredLanguageKeys.pid,
  queryFn: async (pid: string) =>
    (
      await axios.post<PidRequest, AxiosResponse<PreferredLanguage>>(
        `${SKRIBENTEN_API_BASE_PATH}/person/foretrukketSpraak`,
        {
          pid,
        },
      )
    ).data,
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

export const getTemplate = {
  queryKey: letterKeys.brevkode,
  queryFn: async (brevkode: string) =>
    (await axios.get<RedigerbarTemplateDescription>(`${SKRIBENTEN_API_BASE_PATH}/template/${brevkode}`)).data,
};

export async function renderLetter(letterId: string, request: unknown) {
  return (await axios.post<RenderedLetter>(`${SKRIBENTEN_API_BASE_PATH}/letter/${letterId}`, request)).data;
}

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
