import axios from "axios";

import {
  type BrevResponse,
  type EditAttachment,
  type RedigerbartVedleggInfo,
  type RedigertVedleggRequest,
} from "~/types/brev";

import { SKRIBENTEN_API_BASE_PATH } from "./skribenten-api-endpoints";

const vedleggUrl = (saksId: string, brevId: number | string) =>
  `${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev/${brevId}/redigerbareVedlegg`;

export const redigerbareVedleggKeys = {
  all: ["redigerbareVedlegg"] as const,
  liste: (brevId: number | string) => [...redigerbareVedleggKeys.all, "liste", brevId] as const,
  vedlegg: (brevId: number | string, vedleggId: string) =>
    [...redigerbareVedleggKeys.all, "vedlegg", brevId, vedleggId] as const,
};

/**
 * Lightweight list of the letter's editable attachments (vedleggId + title). Used to build tabs.
 */
export const getRedigerbareVedlegg = {
  queryKey: redigerbareVedleggKeys.liste,
  queryFn: async (saksId: string, brevId: number | string) =>
    (await axios.get<RedigerbartVedleggInfo[]>(vedleggUrl(saksId, brevId))).data,
};

/**
 * Full editable content of one attachment. Backend falls back to the template attachment when
 * no saved override exists, so no edited-vs-original branching is needed here.
 */
export const getRedigerbartVedlegg = {
  queryKey: redigerbareVedleggKeys.vedlegg,
  queryFn: async (saksId: string, brevId: number | string, vedleggId: string) =>
    (await axios.get<EditAttachment>(`${vedleggUrl(saksId, brevId)}/${vedleggId}`)).data,
};

/**
 * Saves the edited attachment and returns the stored result, so the caller can refresh from the
 * response instead of assuming its local state matches what the backend persisted.
 */
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

/**
 * Deletes the stored edited override for an attachment, reverting it to the template attachment.
 * This is a reset, not a removal — the template attachment still exists afterwards.
 */
export const tilbakestillRedigerbartVedlegg = async (
  saksId: string,
  brevId: number | string,
  vedleggId: string,
): Promise<BrevResponse> => (await axios.delete<BrevResponse>(`${vedleggUrl(saksId, brevId)}/${vedleggId}`)).data;
