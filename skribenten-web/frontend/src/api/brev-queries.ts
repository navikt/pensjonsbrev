import { useQuery } from "@tanstack/react-query";
import type { AxiosResponse } from "axios";
import axios from "axios";

import { SKRIBENTEN_API_BASE_PATH } from "~/api/skribenten-api-endpoints";
import type {
  BrevInfo,
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

export const getModelSpecification = {
  queryKey: brevmalKeys.modelSpecification,
  queryFn: async (brevkode: string) =>
    (await axios.get<LetterModelSpecification>(`${SKRIBENTEN_API_BASE_PATH}/brevmal/${brevkode}/modelSpecification`))
      .data,
};

export const brevKeys = {
  all: ["BREV"] as const,
  id: (brevId: number) => [...brevKeys.all, brevId] as const,
  reservasjon: (brevId: number) => [...brevKeys.id(brevId), "RESERVASJON"] as const,
};

export const getBrev = {
  queryKey: brevKeys.id,
  queryFn: async (saksId: string, brevId: number, reserver: boolean = true) =>
    (await axios.get<BrevResponse>(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev/${brevId}?reserver=${reserver}`))
      .data,
};

export const attesteringBrevKeys = {
  all: ["BREV", "ATTESTERING"] as const,
  id: (brevId: number) => [...attesteringBrevKeys.all, brevId] as const,
  reservasjon: (brevId: number) => [...attesteringBrevKeys.id(brevId), "RESERVASJON"] as const,
};

export const getBrevAttesteringQuery = (saksId: string, brevId: number, reserver: boolean = true) => ({
  queryKey: attesteringBrevKeys.id(brevId),
  queryFn: async () =>
    (
      await axios.get<BrevResponse>(
        `${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev/${brevId}/attestering?reserver=${reserver}`,
      )
    ).data,
});

export const brevInfoKeys = {
  all: ["BREV_META"] as const,
  id: (brevId: number) => [...brevInfoKeys.all, brevId] as const,
};

export const getBrevInfoQuery = (brevId: number) => ({
  queryKey: brevInfoKeys.id(brevId),
  queryFn: async () => (await axios.get<BrevInfo>(`${SKRIBENTEN_API_BASE_PATH}/brev/${brevId}/info`)).data,
});

export async function createBrev(saksId: string, request: OpprettBrevRequest) {
  return (await axios.post<BrevResponse>(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev`, request)).data;
}

export async function oppdaterBrev(args: {
  saksId: number;
  brevId: number;
  request: OppdaterBrevRequest;
  frigiReservasjon?: boolean;
}) {
  const frigiReservasjon = args.frigiReservasjon ?? true;
  return (
    await axios.put<BrevResponse>(
      `${SKRIBENTEN_API_BASE_PATH}/sak/${args.saksId}/brev/${args.brevId}?frigiReservasjon=${frigiReservasjon}`,
      {
        saksbehandlerValg: args.request.saksbehandlerValg,
        redigertBrev: args.request.redigertBrev,
      },
    )
  ).data;
}

export async function oppdaterBrevtekst(brevId: number, redigertBrev: EditedLetter, frigiReservasjon?: boolean) {
  return (
    await axios.put<BrevResponse>(
      `${SKRIBENTEN_API_BASE_PATH}/brev/${brevId}/redigertBrev?frigiReservasjon=${frigiReservasjon === true}`,
      redigertBrev,
    )
  ).data;
}

export async function oppdaterSaksbehandlerValg(brevId: number, saksbehandlerValg: SaksbehandlerValg) {
  return (
    await axios.put<BrevResponse>(`${SKRIBENTEN_API_BASE_PATH}/brev/${brevId}/saksbehandlerValg`, saksbehandlerValg)
  ).data;
}

export const oppdaterAttestantSignatur = async (brevId: number | string, signatur: string) =>
  (
    await axios.put<BrevResponse>(`${SKRIBENTEN_API_BASE_PATH}/brev/${brevId}/attestant`, signatur, {
      //sendes som form-data hvis man ikke setter content-type til text/plain
      headers: { "Content-Type": "text/plain" },
    })
  ).data;

export async function tilbakestillBrev(brevId: number) {
  return (await axios.post<BrevResponse>(`${SKRIBENTEN_API_BASE_PATH}/brev/${brevId}/tilbakestill`)).data;
}

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
  const { status, data, error } = useQuery({
    queryKey: getModelSpecification.queryKey(brevkode),
    queryFn: () => getModelSpecification.queryFn(brevkode),
    select,
  });

  return { status, specification: data, error };
}
