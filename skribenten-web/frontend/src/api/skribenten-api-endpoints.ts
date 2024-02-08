// "When accessing a member from an await expression, the await expression has to be parenthesized, which is not readable."
// For the purpose of this file it is convenient to be able to access the data property of axios response as a one-liners.
/* eslint-disable unicorn/no-await-expression-member*/

import type { AxiosResponse } from "axios";
import axios, { AxiosError } from "axios";

import type {
  BestillOgRedigerBrevResponse,
  FinnSamhandlerRequestDto,
  FinnSamhandlerResponseDto,
  HentSamhandlerAdresseRequestDto,
  HentSamhandlerAdresseResponseDto,
  HentSamhandlerRequestDto,
  HentsamhandlerResponseDto,
  KontaktAdresseResponse,
  LetterMetadata,
  OrderLetterRequest,
  PidRequest,
  PreferredLanguage,
  SakDto,
} from "~/types/apiTypes";
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
  eblanketter: () => [...letterTemplatesKeys.all, "E_BLANKETTER"] as const,
  sakTypeSearch: (search: { sakType: string; includeVedtak: boolean }) => [...letterTemplatesKeys.all, search] as const,
};

export const letterKeys = {
  all: ["LETTER"] as const,
  brevkode: (brevkode: string) => [...letterKeys.all, brevkode] as const,
};

export const favoritterKeys = {
  all: ["FAVORITTER"] as const,
};

export const adresseKeys = {
  all: ["ADRESSE"],
  pid: (pid: string) => [...adresseKeys.all, pid] as const,
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
  queryKey: letterTemplatesKeys.sakTypeSearch,
  queryFn: async (sakType: string, search: { includeVedtak: boolean }) =>
    (await axios.get<LetterMetadata[]>(`${SKRIBENTEN_API_BASE_PATH}/lettertemplates/${sakType}`, { params: search }))
      .data,
};

export const getEblanketter = {
  queryKey: letterTemplatesKeys.eblanketter(),
  queryFn: async () =>
    (await axios.get<LetterMetadata[]>(`${SKRIBENTEN_API_BASE_PATH}/lettertemplates/e-blanketter`)).data,
};

export const getKontaktAdresse = {
  queryKey: adresseKeys.pid,
  queryFn: async (pid: string) =>
    (
      await axios.post<PidRequest, AxiosResponse<KontaktAdresseResponse>>(
        `${SKRIBENTEN_API_BASE_PATH}/person/adresse`,
        {
          pid,
        },
      )
    ).data,
};

export const getFavoritter = {
  queryKey: favoritterKeys.all,
  queryFn: async () => (await axios.get<string[]>(`${SKRIBENTEN_API_BASE_PATH}/me/favourites`)).data,
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
    await axios.post<string>(`${SKRIBENTEN_API_BASE_PATH}/me/favourites`, id, {
      headers: { "Content-Type": "text/plain" },
    })
  ).data;
}

export async function deleteFavoritt(id: string) {
  return (await axios.delete<string>(`${SKRIBENTEN_API_BASE_PATH}/me/favourites`, { data: id })).data;
}

export async function orderLetter(orderLetterRequest: OrderLetterRequest) {
  const response = (
    await axios.post<BestillOgRedigerBrevResponse>(`${SKRIBENTEN_API_BASE_PATH}/bestillbrev`, orderLetterRequest)
  ).data;

  if (response.failureType) {
    throw new Error(response.failureType);
  }

  return response.url ?? "";
}

export async function finnSamhandler(request: FinnSamhandlerRequestDto) {
  return (await axios.post<FinnSamhandlerResponseDto>(`${SKRIBENTEN_API_BASE_PATH}/finnSamhandler`, request)).data;
}

export const hentSamhandler = {
  queryKey: samhandlerKeys.idTSSEkstern,
  queryFn: async (request: HentSamhandlerRequestDto) => {
    const response = (
      await axios.post<HentsamhandlerResponseDto>(`${SKRIBENTEN_API_BASE_PATH}/hentSamhandler`, request)
    ).data;

    if (response.failure) {
      throw new Error(response.failure);
    }

    return response.success;
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
      // TODO: generalize
      throw new AxiosError(response.data.failureType, "200", undefined, request, response);
    }

    return response.data.adresse;
  },
};
