/* eslint-disable unicorn/no-await-expression-member*/

import { useQuery } from "@tanstack/react-query";
import axios from "axios";

import { SKRIBENTEN_API_BASE_PATH } from "~/api/skribenten-api-endpoints";
import type { BrevResponse, OppdaterBrevRequest, OpprettBrevRequest } from "~/types/brev";
import type { LetterModelSpecification } from "~/types/brevbakerTypes";

export const brevmalKeys = {
  all: ["BREVMAL"] as const,
  brevkode: (brevkode: string) => [...brevmalKeys.all, brevkode] as const,
  modelSpecification: (brevkode: string) => [...brevmalKeys.brevkode(brevkode), "MODEL_SPECIFICATION"],
};

export const brevKeys = {
  all: ["BREV"] as const,
  id: (brevId: number) => [...brevKeys.all, brevId] as const,
};

export const getModelSpecification = {
  queryKey: brevmalKeys.modelSpecification,
  queryFn: async (brevkode: string) =>
    (await axios.get<LetterModelSpecification>(`${SKRIBENTEN_API_BASE_PATH}/brevmal/${brevkode}/modelSpecification`))
      .data,
};

export const getBrev = {
  queryKey: brevKeys.id,
  queryFn: async (saksId: string, brevId: number) => {
    await new Promise((resolve) => setTimeout(resolve, 10_000));
    return (await axios.get<BrevResponse>(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev/${brevId}`)).data;
  },
};

export async function createBrev(saksId: string, request: OpprettBrevRequest) {
  return (await axios.post<BrevResponse>(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev`, request)).data;
}

export async function updateBrev(saksId: string, brevId: number, request: OppdaterBrevRequest) {
  return (await axios.put<BrevResponse>(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev/${brevId}`, request)).data;
}

export function useModelSpecification<T>(brevkode: string, select: (data: LetterModelSpecification) => T) {
  return useQuery({
    queryKey: getModelSpecification.queryKey(brevkode),
    queryFn: () => getModelSpecification.queryFn(brevkode),
    select,
  }).data;
}
