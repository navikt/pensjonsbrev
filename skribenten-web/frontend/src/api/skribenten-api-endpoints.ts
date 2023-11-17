// "When accessing a member from an await expression, the await expression has to be parenthesized, which is not readable."
// For the purpose of this file it is convenient to be able to access the data property of axios response as a one-liners.
/* eslint-disable unicorn/no-await-expression-member*/

import axios from "axios";

import type { SakDto } from "../types/apiTypes";
const SKRIBENTEN_API_BASE_PATH = "/skribenten-backend";

export const saksnummerKeys = {
  all: ["SAK"] as const,
  id: (sakId: string) => [...saksnummerKeys.all, sakId] as const,
};

export const navnKeys = {
  all: ["NAVN"] as const,
  id: (fnr: string) => [...navnKeys.all, fnr] as const,
};

export const getSak = {
  queryKey: saksnummerKeys.id,
  queryFn: async (sakId: string) => (await axios.get<SakDto>(`${SKRIBENTEN_API_BASE_PATH}/pen/sak/${sakId}`)).data,
};

export const getNavn = {
  queryKey: navnKeys.id,
  queryFn: async (fnr: string) => (await axios.get<string>(`${SKRIBENTEN_API_BASE_PATH}/pdl/navn/${fnr}`)).data,
};
