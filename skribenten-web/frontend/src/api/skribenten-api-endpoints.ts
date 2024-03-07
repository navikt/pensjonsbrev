// "When accessing a member from an await expression, the await expression has to be parenthesized, which is not readable."
// For the purpose of this file it is convenient to be able to access the data property of axios response as a one-liners.
/* eslint-disable unicorn/no-await-expression-member*/

import type { AxiosResponse } from "axios";
import axios, { AxiosError } from "axios";

import type {
  Avtaleland,
  BestillOgRedigerBrevResponse,
  FinnSamhandlerRequestDto,
  FinnSamhandlerResponseDto,
  HentSamhandlerAdresseRequestDto,
  HentSamhandlerAdresseResponseDto,
  HentSamhandlerRequestDto,
  HentsamhandlerResponseDto,
  KontaktAdresseResponse,
  LetterMetadata,
  OrderDoksysLetterRequest,
  OrderEblankettRequest,
  OrderExstreamLetterRequest,
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
  sakId: (sakId: string) => [...navnKeys.all, sakId] as const,
};

export const letterTemplatesKeys = {
  all: ["LETTER_TEMPLATES"] as const,
  sakTypeSearch: (search: { sakType: string; includeVedtak: boolean }) => [...letterTemplatesKeys.all, search] as const,
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

export const adresseKeys = {
  all: ["ADRESSE"],
  sakId: (sakId: string) => [...adresseKeys.all, sakId] as const,
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
  sakId: (sakId: string) => [...preferredLanguageKeys.all, sakId] as const,
};

export const getSak = {
  queryKey: saksnummerKeys.id,
  queryFn: async (sakId: string) => (await axios.get<SakDto>(`${SKRIBENTEN_API_BASE_PATH}/sak/${sakId}`)).data,
};

export const getNavn = {
  queryKey: navnKeys.sakId,
  queryFn: async (sakId: string) => (await axios.get<string>(`${SKRIBENTEN_API_BASE_PATH}/sak/${sakId}/navn`)).data,
};

export const getPreferredLanguage = {
  queryKey: preferredLanguageKeys.sakId,
  queryFn: async (sakId: string) =>
    (await axios.get<PreferredLanguage>(`${SKRIBENTEN_API_BASE_PATH}/sak/${sakId}/foretrukketSpraak`)).data,
};

export const getLetterTemplate = {
  queryKey: letterTemplatesKeys.sakTypeSearch,
  queryFn: async (sakType: string, search: { includeVedtak: boolean }) =>
    (await axios.get<LetterMetadata[]>(`${SKRIBENTEN_API_BASE_PATH}/lettertemplates/${sakType}`, { params: search }))
      .data,
};

export const getKontaktAdresse = {
  queryKey: adresseKeys.sakId,
  queryFn: async (sakId: string) =>
    (await axios.get<KontaktAdresseResponse>(`${SKRIBENTEN_API_BASE_PATH}/sak/${sakId}/adresse`)).data,
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

export const getAvtaleLand = {
  queryKey: avtalelandKeys.all,
  queryFn: async () => (await axios.get<Avtaleland[]>(`${SKRIBENTEN_API_BASE_PATH}/kodeverk/avtaleland`)).data,
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

export async function orderExstreamLetter(sakId: string, orderLetterRequest: OrderExstreamLetterRequest) {
  const response = await axios.post<BestillOgRedigerBrevResponse>(
    `${SKRIBENTEN_API_BASE_PATH}/sak/${sakId}/bestillBrev/exstream`,
    orderLetterRequest,
  );

  return convertBestillOgRedigerBrevResponse(response);
}

export async function orderDoksysLetter(sakId: string, orderLetterRequest: OrderDoksysLetterRequest) {
  const response = await axios.post<BestillOgRedigerBrevResponse>(
    `${SKRIBENTEN_API_BASE_PATH}/sak/${sakId}/bestillBrev/doksys`,
    orderLetterRequest,
  );

  return convertBestillOgRedigerBrevResponse(response);
}

export async function orderEblankett(sakId: string, orderLetterRequest: OrderEblankettRequest) {
  const response = await axios.post<BestillOgRedigerBrevResponse>(
    `${SKRIBENTEN_API_BASE_PATH}/sak/${sakId}/bestillBrev/exstream/eblankett`,
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
