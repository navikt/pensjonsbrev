import axios from "axios";

import { type EditAttachment, type RedigerbartVedleggInfo, type RedigertVedleggRequest } from "~/types/brev";
import { type Redigeringsflate } from "~/utils/editorTracking";

import { SKRIBENTEN_API_BASE_PATH } from "./skribenten-api-endpoints";

const vedleggUrl = (saksId: string, brevId: number | string, redigeringsflate: Redigeringsflate) => {
  const attestering = redigeringsflate === "attestant-redigering" ? "/attestering" : "";
  return `${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev/${brevId}${attestering}/redigerbareVedlegg`;
};

export const redigerbareVedleggKeys = {
  all: ["redigerbareVedlegg"] as const,
  liste: (brevId: number | string, redigeringsflate: Redigeringsflate) =>
    [...redigerbareVedleggKeys.all, redigeringsflate, "liste", brevId] as const,
  vedlegg: (brevId: number | string, vedleggId: string, redigeringsflate: Redigeringsflate) =>
    [...redigerbareVedleggKeys.all, redigeringsflate, "vedlegg", brevId, vedleggId] as const,
};

export const getRedigerbareVedlegg = {
  queryKey: redigerbareVedleggKeys.liste,
  queryFn: async (saksId: string, brevId: number | string, redigeringsflate: Redigeringsflate) =>
    (await axios.get<RedigerbartVedleggInfo[]>(vedleggUrl(saksId, brevId, redigeringsflate))).data,
};

export const getRedigerbartVedlegg = {
  queryKey: redigerbareVedleggKeys.vedlegg,
  queryFn: async (saksId: string, brevId: number | string, vedleggId: string, redigeringsflate: Redigeringsflate) =>
    (await axios.get<EditAttachment>(`${vedleggUrl(saksId, brevId, redigeringsflate)}/${vedleggId}`)).data,
};

export const lagreRedigerbartVedlegg = async (
  saksId: string,
  brevId: number | string,
  vedleggId: string,
  redigertVedlegg: EditAttachment,
  redigeringsflate: Redigeringsflate,
): Promise<EditAttachment> =>
  (
    await axios.put<EditAttachment>(`${vedleggUrl(saksId, brevId, redigeringsflate)}/${vedleggId}`, {
      redigertVedlegg,
    } satisfies RedigertVedleggRequest)
  ).data;

export const tilbakestillRedigerbartVedlegg = async (
  saksId: string,
  brevId: number | string,
  vedleggId: string,
): Promise<EditAttachment> =>
  (await axios.delete<EditAttachment>(`${vedleggUrl(saksId, brevId, "saksbehandler-redigering")}/${vedleggId}`)).data;
