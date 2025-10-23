// "When accessing a member from an await expression, the await expression has to be parenthesized, which is not readable."
// For the purpose of this file it is convenient to be able to access the data property of axios response as a one-liners.

import axios from "axios";

import { CACHE_FOR } from "./cache";

const BFF_BASE_URL = "/bff/internal";

export type BaseUrls = {
  psak: string;
};

export type UserInfo = {
  name: string;
  navident: string;
};

export const getUserInfo = {
  queryKey: ["USER"],
  queryFn: async () => (await axios.get<UserInfo>(`${BFF_BASE_URL}/userInfo`)).data,
  staleTime: CACHE_FOR.aDay,
};

export const getBaseUrls = {
  queryKey: ["BASE_URLS"],
  queryFn: async () => (await axios.get<BaseUrls>(`${BFF_BASE_URL}/baseUrls`)).data,
  staleTime: CACHE_FOR.aDay,
};

export async function loggFeil(data: Feilmelding) {
  return await axios.post(`${BFF_BASE_URL}/logg`, data);
}

export type Feilmelding = {
  type: string;
  message: unknown;
  stack: unknown;
  status: number | undefined;
  jsonContent: unknown;
};
