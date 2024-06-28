// "When accessing a member from an await expression, the await expression has to be parenthesized, which is not readable."
// For the purpose of this file it is convenient to be able to access the data property of axios response as a one-liners.
/* eslint-disable unicorn/no-await-expression-member*/

import type { AxiosResponse } from "axios";
import axios, { AxiosError } from "axios";

import type { FinnSamhandlerRequest } from "~/types/apiTypes";
import {
  type Avtaleland,
  type BestillOgRedigerBrevResponse,
  type Enhet,
  type FinnSamhandlerRequestDto,
  type FinnSamhandlerResponseDto,
  type HentSamhandlerAdresseRequestDto,
  type HentSamhandlerAdresseResponseDto,
  type HentSamhandlerRequestDto,
  type HentsamhandlerResponseDto,
  type KontaktAdresseResponse,
  type OrderDoksysLetterRequest,
  type OrderEblankettRequest,
  type OrderExstreamLetterRequest,
  type PreferredLanguage,
  type SakContextDto,
} from "~/types/apiTypes";

export const SKRIBENTEN_API_BASE_PATH = "/bff/skribenten-backend";

axios.interceptors.response.use(undefined, (error) => {
  if (error.response.status === 401) {
    window.location.assign(error.response.headers.location);
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

export const getSakContext = {
  queryKey: saksnummerKeys.id,
  queryFn: async (saksId: string, vedtaksId: string | undefined) =>
    (await axios.get<SakContextDto>(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}`, { params: { vedtaksId } })).data,
  staleTime: 5000,
};

export const getNavn = {
  queryKey: navnKeys.saksId,
  queryFn: async (saksId: string) => (await axios.get<string>(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/navn`)).data,
};

export const getPreferredLanguage = {
  queryKey: preferredLanguageKeys.saksId,
  queryFn: async (saksId: string) =>
    (await axios.get<PreferredLanguage>(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/foretrukketSpraak`)).data,
};

export const getKontaktAdresse = {
  queryKey: adresseKeys.saksId,
  queryFn: async (saksId: string) =>
    (await axios.get<KontaktAdresseResponse>(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/adresse`)).data,
};

export const getFavoritter = {
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

export const getSamhandlerQuery = {
  queryKey: ["getSamhandler"],
  queryFn: async (request: FinnSamhandlerRequest) => finnSamhandler2(request),
};

export async function finnSamhandler2(request: FinnSamhandlerRequest): Promise<FinnSamhandlerResponseDto> {
  return {
    samhandlere: [
      {
        navn: "AETAT ARBEIDSDIREKTORATET",
        samhandlerType: "AK",
        offentligId: "00870309732",
        idType: "ORG",
        idTSSEkstern: "80000425013",
      },
      {
        navn: "AETAT FYLLINGSDALEN",
        samhandlerType: "AK",
        offentligId: "00974742888",
        idType: "ORG",
        idTSSEkstern: "80000425436",
      },
      {
        navn: "AETAT FØRDE",
        samhandlerType: "AK",
        offentligId: "00982026636",
        idType: "ORG",
        idTSSEkstern: "80000415016",
      },
      {
        navn: "AETAT SKI",
        samhandlerType: "AK",
        offentligId: "00976818741",
        idType: "ORG",
        idTSSEkstern: "80000407290",
      },
      {
        navn: "ARBEIDSKONTORET PÅ JØRPELAND",
        samhandlerType: "AK",
        offentligId: "00871345422",
        idType: "ORG",
        idTSSEkstern: "80000408122",
      },
    ],
    failureType: null,
  };

  //return (await axios.post<FinnSamhandlerResponseDto>(`${SKRIBENTEN_API_BASE_PATH}/finnSamhandler`, request)).data;
}

export async function finnSamhandler(request: FinnSamhandlerRequestDto) {
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

export const hentSamhandlerAdresse = {
  queryKey: samhandlerAdresseKeys.idTSSEkstern,
  queryFn: async (request: HentSamhandlerAdresseRequestDto) => {
    const response = await axios.post<HentSamhandlerAdresseResponseDto>(
      `${SKRIBENTEN_API_BASE_PATH}/hentSamhandlerAdresse`,
      request,
    );

    if (response.data.failureType) {
      throw convertResponseToAxiosError({ message: response.data.failureType, response });
    }

    return response.data.adresse;
  },
};

function convertResponseToAxiosError({ message, response }: { message: string; response: AxiosResponse }) {
  return new AxiosError(message, undefined, undefined, undefined, response);
}
