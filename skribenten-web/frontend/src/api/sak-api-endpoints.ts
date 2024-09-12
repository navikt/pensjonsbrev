// "When accessing a member from an await expression, the await expression has to be parenthesized, which is not readable."
// For the purpose of this file it is convenient to be able to access the data property of axios response as a one-liners.
/* eslint-disable unicorn/no-await-expression-member*/

import axios from "axios";

import type {
  BestillBrevResponse,
  BrevInfo,
  DelvisOppdaterBrevRequest,
  DelvisOppdaterBrevResponse,
} from "~/types/brev";

import { SKRIBENTEN_API_BASE_PATH } from "./skribenten-api-endpoints";

export const hentAlleBrevForSak = {
  queryKey: (sakId: string) => ["hentAlleBrevForSak", sakId],
  queryFn: async (saksId: string) => hentAlleBrevForSakFunction(saksId),
};

const hentAlleBrevForSakFunction = async (saksId: string) =>
  (await axios.get<BrevInfo[]>(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev`)).data;

export const hentPdfForBrev = {
  queryKey: (brevId: string) => ["hentPdfForBrev", brevId],
  queryFn: async (saksId: string, brevId: string) => hentPdfForBrevFunction(saksId, brevId),
};

export const hentPdfForBrevFunction = async (saksId: string, brevId: string | number) =>
  (
    await axios.get<Blob>(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev/${brevId}/pdf`, {
      responseType: "blob",
      headers: { Accept: "application/pdf" },
    })
  ).data;

export const delvisOppdaterBrev = async (argz: DelvisOppdaterBrevRequest) =>
  (
    await axios.patch<DelvisOppdaterBrevResponse>(
      `${SKRIBENTEN_API_BASE_PATH}/sak/${argz.sakId}/brev/${argz.brevId}`,
      argz,
    )
  ).data;

export const slettBrev = async (saksId: string, brevId: string) =>
  (await axios.delete(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev/${brevId}`)).data;

export const sendBrev = async (saksId: string, brevId: string | number): Promise<BestillBrevResponse> =>
  (await axios.post<BestillBrevResponse>(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev/${brevId}/pdf/send`)).data;

export const hentPdfForJournalpostQuery = {
  queryKey: (sakId: string, journalpostId: string) => ["pdfForJournalpost", sakId, journalpostId],
  queryFn: async (sakId: string, journalpostId: string | number) =>
    hentPdfForJournalpost({ sakId: sakId, journalpostId: journalpostId }),
};

export const hentPdfForJournalpost = async (argz: { sakId: string; journalpostId: string | number }) =>
  (
    await axios.get<Blob>(`${SKRIBENTEN_API_BASE_PATH}/sak/${argz.sakId}/pdf/${argz.journalpostId}`, {
      responseType: "blob",
      headers: { Accept: "application/pdf" },
    })
  ).data;
