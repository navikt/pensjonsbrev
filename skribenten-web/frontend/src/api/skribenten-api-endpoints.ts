// "When accessing a member from an await expression, the await expression has to be parenthesized, which is not readable."
// For the purpose of this file it is convenient to be able to access the data property of axios response as a one-liners.

import type { AxiosResponse } from "axios";
import axios, { AxiosError } from "axios";

import type {
  Avtaleland,
  BestillOgRedigerBrevResponse,
  Enhet,
  FinnSamhandlerRequestDto,
  FinnSamhandlerResponseDto,
  HentSamhandlerAdresseResponseDto,
  HentSamhandlerRequestDto,
  HentsamhandlerResponseDto,
  KontaktAdresseResponse,
  OrderDoksysLetterRequest,
  OrderEblankettRequest,
  OrderExstreamLetterRequest,
  PreferredLanguage,
  SakContextDto,
} from "~/types/apiTypes";
import type { AttestForbiddenReason } from "~/utils/parseAttest403";
import { parseAttest403 } from "~/utils/parseAttest403";

export const SKRIBENTEN_API_BASE_PATH = "/bff/skribenten-backend";

axios.interceptors.response.use(undefined, (error) => {
  if (error.response.status === 401) {
    globalThis.location.assign(error.response.headers.location);
  } else if (error.response?.status === 403) {
    (error as AxiosError & { forbidReason?: AttestForbiddenReason }).forbidReason = parseAttest403(error.response.data);
  }
  return Promise.reject(error);
});

/**
 * Anbefalt lesing for react-query key factory pattern: https://tkdodo.eu/blog/effective-react-query-keys
 */

export const saksnummerKeys = {
  all: ["SAK"] as const,
  id: (saksId: string, vedtaksId: string | undefined) => [...saksnummerKeys.all, saksId, vedtaksId] as const,
};

export const navnKeys = {
  all: ["NAVN"] as const,
  saksId: (saksId: string) => [...navnKeys.all, saksId] as const,
};

export const letterKeys = {
  all: ["LETTER"] as const,
  brevkode: (brevkode: string) => [...letterKeys.all, brevkode] as const,
};

export const favoritterKeys = {
  all: ["FAVORITTER"] as const,
};

export const avtalelandKeys = {
  all: ["AVTALE_LAND"] as const,
};

export const enheterKeys = {
  all: ["ENHETER"] as const,
};

export const adresseKeys = {
  all: ["ADRESSE"],
  saksId: (saksId: string) => [...adresseKeys.all, saksId] as const,
};

export const samhandlerKeys = {
  all: ["SAMHANDLER"],
  idTSSEkstern: (idTSSEkstern: string) => [...samhandlerKeys.all, idTSSEkstern] as const,
};

export const samhandlerAdresseKeys = {
  all: ["SAMHANDLER_ADRESSE"],
  idTSSEkstern: (idTSSEkstern: string) => [...samhandlerAdresseKeys.all, idTSSEkstern] as const,
};

export const preferredLanguageKeys = {
  all: ["PREFERRED_LANGUAGE"] as const,
  saksId: (saksId: string) => [...preferredLanguageKeys.all, saksId] as const,
};

export const orderLetterKeys = {
  all: ["ORDER_LETTER"],
  brevsystem: (brevsystem: string) => [...orderLetterKeys.all, brevsystem] as const,
};

export const getSakContextQuery = (saksId: string, vedtaksId: string | undefined) => ({
  queryKey: saksnummerKeys.id(saksId, vedtaksId),
  queryFn: async () =>
    (await axios.get<SakContextDto>(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}`, { params: { vedtaksId } })).data,
  staleTime: 5000,
});

export const getNavnQuery = (saksId: string) => ({
  queryKey: navnKeys.saksId(saksId),
  queryFn: async () => (await axios.get<string>(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/navn`)).data,
});

export const getPreferredLanguageQuery = (saksId: string) => ({
  queryKey: preferredLanguageKeys.saksId(saksId),
  queryFn: async () =>
    (await axios.get<PreferredLanguage>(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/foretrukketSpraak`)).data,
});

export const getKontaktAdresseQuery = (saksId: string) => ({
  queryKey: adresseKeys.saksId(saksId),
  queryFn: async () =>
    (await axios.get<KontaktAdresseResponse>(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/adresse`)).data,
});

export const getFavoritterQuery = {
  queryKey: favoritterKeys.all,
  queryFn: async () => (await axios.get<string[]>(`${SKRIBENTEN_API_BASE_PATH}/me/favourites`)).data,
};

export const getAvtaleLand = {
  queryKey: avtalelandKeys.all,
  queryFn: async () => (await axios.get<Avtaleland[]>(`${SKRIBENTEN_API_BASE_PATH}/kodeverk/avtaleland`)).data,
};

export const getEnheter = {
  queryKey: enheterKeys.all,
  queryFn: async () => (await axios.get<Enhet[]>(`${SKRIBENTEN_API_BASE_PATH}/me/enheter`)).data,
};

export async function addFavoritt(id: string) {
  return (
    await axios.post<string>(`${SKRIBENTEN_API_BASE_PATH}/me/favourites`, id, {
      headers: { "Content-Type": "text/plain" },
    })
  ).data;
}

export async function deleteFavoritt(id: string) {
  return (await axios.delete<string>(`${SKRIBENTEN_API_BASE_PATH}/me/favourites`, { data: id })).data;
}

export async function orderExstreamLetter(saksId: string, orderLetterRequest: OrderExstreamLetterRequest) {
  const response = await axios.post<BestillOgRedigerBrevResponse>(
    `${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/bestillBrev/exstream`,
    orderLetterRequest,
  );

  return convertBestillOgRedigerBrevResponse(response);
}

export async function orderDoksysLetter(saksId: string, orderLetterRequest: OrderDoksysLetterRequest) {
  const response = await axios.post<BestillOgRedigerBrevResponse>(
    `${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/bestillBrev/doksys`,
    orderLetterRequest,
  );

  return convertBestillOgRedigerBrevResponse(response);
}

export async function orderEblankett(saksId: string, orderLetterRequest: OrderEblankettRequest) {
  const response = await axios.post<BestillOgRedigerBrevResponse>(
    `${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/bestillBrev/exstream/eblankett`,
    orderLetterRequest,
  );

  return convertBestillOgRedigerBrevResponse(response);
}

function convertBestillOgRedigerBrevResponse(response: AxiosResponse<BestillOgRedigerBrevResponse>) {
  if (response.data.failureType) {
    throw convertResponseToAxiosError({ message: response.data.failureType, response });
  }
  const url = response.data.url;
  if (!url) {
    throw convertResponseToAxiosError({ message: "Responsen mangler url for redigering", response });
  }

  return url;
}

export async function finnSamhandler(request: FinnSamhandlerRequestDto): Promise<FinnSamhandlerResponseDto> {
  return (await axios.post<FinnSamhandlerResponseDto>(`${SKRIBENTEN_API_BASE_PATH}/finnSamhandler`, request)).data;
}

export const hentSamhandler = {
  queryKey: samhandlerKeys.idTSSEkstern,
  queryFn: async (request: HentSamhandlerRequestDto) => {
    const response = await axios.post<HentsamhandlerResponseDto>(`${SKRIBENTEN_API_BASE_PATH}/hentSamhandler`, request);

    if (response.data.failure) {
      throw convertResponseToAxiosError({ message: response.data.failure, response });
    }

    return response.data.success;
  },
};

export const hentSamhandlerAdresseQuery = (idTSSEkstern: string) => ({
  queryKey: samhandlerAdresseKeys.idTSSEkstern(idTSSEkstern),
  queryFn: async () => {
    const response = await axios.post<HentSamhandlerAdresseResponseDto>(
      `${SKRIBENTEN_API_BASE_PATH}/hentSamhandlerAdresse`,
      { idTSSEkstern },
    );

    if (response.data.failureType) {
      throw convertResponseToAxiosError({ message: response.data.failureType, response });
    }

    return response.data.adresse;
  },
});

export const hentLandForManuellUtfyllingAvAdresse = {
  queryKey: ["LANDKODER_OG_NAVN"],
  queryFn: async () =>
    (await axios.get<Array<{ kode: string; navn: string }>>(`${SKRIBENTEN_API_BASE_PATH}/land`)).data,
};

function convertResponseToAxiosError({ message, response }: { message: string; response: AxiosResponse }) {
  return new AxiosError(message, undefined, undefined, undefined, response);
}
