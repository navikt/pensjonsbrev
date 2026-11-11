import axios from "axios";

import { redigerbareVedleggKeys } from "~/api/redigerbareVedlegg-endpoints";
import { type Redigeringsflate } from "~/Brevredigering/LetterEditor/RedigeringsflateContext";
import { type AttachmentV2, type RedigertVedleggRequestV2 } from "~/types/brevV2";

import { SKRIBENTEN_API_BASE_PATH } from "./skribenten-api-endpoints";

const vedleggV2Url = (saksId: string, brevId: number, vedleggId: string, redigeringsflate: Redigeringsflate) => {
  const attestering = redigeringsflate === "attestant-redigering" ? "/attestering" : "";
  return `${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev/v2/${brevId}${attestering}/redigerbareVedlegg/${vedleggId}`;
};

export const redigerbareVedleggV2Keys = {
  vedlegg: (brevId: number, vedleggId: string, redigeringsflate: Redigeringsflate) =>
    [...redigerbareVedleggKeys.brev(brevId, redigeringsflate), "vedleggV2", vedleggId] as const,
};

export const getRedigerbartVedleggV2 = (
  saksId: string,
  brevId: number,
  vedleggId: string,
  redigeringsflate: Redigeringsflate,
) => ({
  queryKey: redigerbareVedleggV2Keys.vedlegg(brevId, vedleggId, redigeringsflate),
  queryFn: async ({ signal }: { signal: AbortSignal }) =>
    (await axios.get<AttachmentV2>(vedleggV2Url(saksId, brevId, vedleggId, redigeringsflate), { signal })).data,
});

export const lagreRedigerbartVedleggV2 = async (
  saksId: string,
  brevId: number,
  vedleggId: string,
  redigertVedlegg: AttachmentV2,
  redigeringsflate: Redigeringsflate,
): Promise<AttachmentV2> =>
  (
    await axios.put<AttachmentV2>(vedleggV2Url(saksId, brevId, vedleggId, redigeringsflate), {
      redigertVedlegg,
    } satisfies RedigertVedleggRequestV2)
  ).data;

export const tilbakestillRedigerbartVedleggV2 = async (
  saksId: string,
  brevId: number,
  vedleggId: string,
): Promise<AttachmentV2> =>
  (await axios.delete<AttachmentV2>(vedleggV2Url(saksId, brevId, vedleggId, "saksbehandler-redigering"))).data;
