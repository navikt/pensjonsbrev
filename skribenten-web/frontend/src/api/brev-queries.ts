/* eslint-disable unicorn/no-await-expression-member*/

import { useQuery } from "@tanstack/react-query";
import type { AxiosResponse } from "axios";
import axios from "axios";

import { SKRIBENTEN_API_BASE_PATH } from "~/api/skribenten-api-endpoints";
import type {
  BrevResponse,
  OppdaterBrevRequest,
  OpprettBrevRequest,
  ReservasjonResponse,
  SaksbehandlerValg,
} from "~/types/brev";
import type { EditedLetter, LetterModelSpecification } from "~/types/brevbakerTypes";

export const brevmalKeys = {
  all: ["BREVMAL"] as const,
  brevkode: (brevkode: string) => [...brevmalKeys.all, brevkode] as const,
  modelSpecification: (brevkode: string) => [...brevmalKeys.brevkode(brevkode), "MODEL_SPECIFICATION"],
};

export const brevKeys = {
  all: ["BREV"] as const,
  id: (brevId: number) => [...brevKeys.all, brevId] as const,
  reservasjon: (brevId: number) => [...brevKeys.id(brevId), "RESERVASJON"] as const,
};

export const getModelSpecification = {
  queryKey: brevmalKeys.modelSpecification,
  queryFn: async (brevkode: string) =>
    (await axios.get<LetterModelSpecification>(`${SKRIBENTEN_API_BASE_PATH}/brevmal/${brevkode}/modelSpecification`))
      .data,
};

export const getBrev = {
  queryKey: brevKeys.id,
  queryFn: async (saksId: string, brevId: number, reserver: boolean = true) =>
    (await axios.get<BrevResponse>(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev/${brevId}?reserver=${reserver}`))
      .data,
};

export async function createBrev(saksId: string, request: OpprettBrevRequest) {
  return (await axios.post<BrevResponse>(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev`, request)).data;
}

export async function updateBrev(saksId: string, brevId: string | number, request: OppdaterBrevRequest) {
  return (await axios.put<BrevResponse>(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev/${brevId}`, request)).data;
}

export async function hurtiglagreBrev(brevId: number, redigertBrev: EditedLetter) {
  return (await axios.put<BrevResponse>(`${SKRIBENTEN_API_BASE_PATH}/brev/${brevId}/redigertBrev`, redigertBrev)).data;
}

export async function hurtiglagreSaksbehandlerValg(brevId: number, saksbehandlerValg: SaksbehandlerValg) {
  return (
    await axios.put<BrevResponse>(`${SKRIBENTEN_API_BASE_PATH}/brev/${brevId}/saksbehandlerValg`, saksbehandlerValg)
  ).data;
}

export const oppdaterSignatur = async (args: { brevId: number | string; signatur: string }) =>
  (
    await axios.put<BrevResponse>(`${SKRIBENTEN_API_BASE_PATH}/brev/${args.brevId}/signatur`, args.signatur, {
      //sendes som form-data hvis man ikke setter content-type til application/json
      headers: { "Content-Type": "text/plain" },
    })
  ).data;

export const getBrevReservasjon = {
  querykey: brevKeys.reservasjon,
  queryFn: async (brevId: number) => {
    const response = await axios
      .get<ReservasjonResponse>(`${SKRIBENTEN_API_BASE_PATH}/brev/${brevId}/reservasjon`)
      .catch((error) => {
        if (error.response.status === 423) {
          return error.response as AxiosResponse<ReservasjonResponse>;
        } else {
          throw error;
        }
      });
    return response.data;
  },
};

export function useModelSpecification<T>(brevkode: string, select: (data: LetterModelSpecification) => T) {
  return useQuery({
    queryKey: getModelSpecification.queryKey(brevkode),
    queryFn: () => getModelSpecification.queryFn(brevkode),
    select,
  }).data;
}
