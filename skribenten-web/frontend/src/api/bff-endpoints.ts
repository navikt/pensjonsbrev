// "When accessing a member from an await expression, the await expression has to be parenthesized, which is not readable."
// For the purpose of this file it is convenient to be able to access the data property of axios response as a one-liners.
/* eslint-disable unicorn/no-await-expression-member*/

import axios from "axios";

import { CACHE_FOR } from "./cache";

const BFF_BASE_URL = "/bff/internal";

export const getUserInfo = {
  queryKey: ["USER"],
  queryFn: async () => (await axios.get<UserInfo>(`${BFF_BASE_URL}/userInfo`)).data,
  staleTime: CACHE_FOR.aDay,
};

export type UserInfo = {
  name: string;
  navident: string;
  rolle: BrukerRoller;
};

export enum BrukerRoller {
  Saksbehandler = "Saksbehandler",
  Attestant = "Attestant",
}
