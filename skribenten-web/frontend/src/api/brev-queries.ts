import { useQuery } from "@tanstack/react-query";
import type { AxiosResponse } from "axios";
import axios from "axios";

import { SKRIBENTEN_API_BASE_PATH } from "~/api/skribenten-api-endpoints";
import type { LetterMetadata } from "~/types/apiTypes";
import type {
  BrevInfo,
  BrevResponse,
  OppdaterBrevRequest,
  OpprettBrevRequest,
  ReservasjonResponse,
} from "~/types/brev";
import type { EditedLetter, LetterModelSpecification } from "~/types/brevbakerTypes";
import type { P1Redigerbar } from "~/types/p1";

export const brevmetadataKeys = {
  all: ["BREVMETADATA"] as const,
};

export const getBrevmetadataQuery = {
  queryKey: brevmetadataKeys.all,
  queryFn: async () => (await axios.get<LetterMetadata[]>(`${SKRIBENTEN_API_BASE_PATH}/brevmal`)).data,
};

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

export const p1OverrideKeys = {
  all: ["P1_OVERRIDE"] as const,
  id: (saksId: string, brevId: number) => [...p1OverrideKeys.all, saksId, brevId] as const,
};

// GET: try to fetch existing override
export const getP1Override = async (saksId: string, brevId: number): Promise<P1Redigerbar | null> => {
  try {
    const res = await axios.get<P1Redigerbar>(
      `${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev/${brevId}/p1-overstyring`,
    );
    return res.data;
  } catch (error) {
    if (axios.isAxiosError(error) && error.response?.status === 404) {
      // No override exists yet
      return null;
    }
    throw error;
  }
};

// POST: create override from Pesys data (first time)
export const createP1Override = async (saksId: string, brevId: number): Promise<P1Redigerbar> => {
  const res = await axios.post<P1Redigerbar>(
    `${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev/${brevId}/p1-overstyring`,
    {}, // empty payload
  );
  return res.data;
};

// PUT: save edited override
export const saveP1Override = async (saksId: string, brevId: number, payload: P1Redigerbar): Promise<P1Redigerbar> => {
  const res = await axios.put<P1Redigerbar>(
    `${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev/${brevId}/p1-overstyring`,
    payload,
  );
  return res.data;
};

// Helper: ensure we always get a P1 override (GET or POST)
export const getOrCreateP1Override = async (saksId: string, brevId: number): Promise<P1Redigerbar> => {
  const existing = await getP1Override(saksId, brevId);
  if (existing) return existing;
  return createP1Override(saksId, brevId);
};
