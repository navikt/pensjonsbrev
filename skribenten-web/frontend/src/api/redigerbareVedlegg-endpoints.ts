import axios from "axios";

import { type EditAttachment, type RedigerbartVedleggInfo, type RedigertVedleggRequest } from "~/types/brev";

import { SKRIBENTEN_API_BASE_PATH } from "./skribenten-api-endpoints";

const vedleggUrl = (saksId: string, brevId: number | string) =>
  `${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev/${brevId}/redigerbareVedlegg`;

export const redigerbareVedleggKeys = {
  all: ["redigerbareVedlegg"] as const,
  liste: (brevId: number | string) => [...redigerbareVedleggKeys.all, "liste", brevId] as const,
  vedlegg: (brevId: number | string, vedleggId: string) =>
    [...redigerbareVedleggKeys.all, "vedlegg", brevId, vedleggId] as const,
};

export const getRedigerbareVedlegg = {
  queryKey: redigerbareVedleggKeys.liste,
  queryFn: async (saksId: string, brevId: number | string) =>
    (await axios.get<RedigerbartVedleggInfo[]>(vedleggUrl(saksId, brevId))).data,
};

export const getRedigerbartVedlegg = {
  queryKey: redigerbareVedleggKeys.vedlegg,
  queryFn: async (saksId: string, brevId: number | string, vedleggId: string) =>
    (await axios.get<EditAttachment>(`${vedleggUrl(saksId, brevId)}/${vedleggId}`)).data,
};

export const lagreRedigerbartVedlegg = async (
  saksId: string,
  brevId: number | string,
  vedleggId: string,
  redigertVedlegg: EditAttachment,
): Promise<EditAttachment> =>
  (
    await axios.put<EditAttachment>(`${vedleggUrl(saksId, brevId)}/${vedleggId}`, {
      redigertVedlegg,
    } satisfies RedigertVedleggRequest)
  ).data;

export const tilbakestillRedigerbartVedlegg = async (
  saksId: string,
  brevId: number | string,
  vedleggId: string,
): Promise<EditAttachment> => (await axios.delete<EditAttachment>(`${vedleggUrl(saksId, brevId)}/${vedleggId}`)).data;
