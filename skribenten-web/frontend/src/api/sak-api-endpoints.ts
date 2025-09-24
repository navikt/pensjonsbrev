// "When accessing a member from an await expression, the await expression has to be parenthesized, which is not readable."
// For the purpose of this file it is convenient to be able to access the data property of axios response as a one-liners.

import axios from "axios";

import type {
  BestillBrevResponse,
  BrevInfo,
  BrevResponse,
  DelvisOppdaterBrevRequest,
  DelvisOppdaterBrevResponse,
  OppdaterBrevRequest,
} from "~/types/brev";

import { SKRIBENTEN_API_BASE_PATH } from "./skribenten-api-endpoints";

export const hentAlleBrevForSak = {
  queryKey: (sakId: string) => ["hentAlleBrevForSak", sakId],
  queryFn: async (saksId: string) => hentAlleBrevForSakFunction(saksId),
};

const hentAlleBrevForSakFunction = async (saksId: string) =>
  (await axios.get<BrevInfo[]>(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev`)).data;

export const hentPdfForBrev = {
  queryKey: (brevId: number) => ["hentPdfForBrev", brevId],
  queryFn: async (saksId: string, brevId: number) => hentPdfForBrevFunction(saksId, brevId),
};

const hentPdfForBrevFunction = async (saksId: string, brevId: string | number) =>
  (
    await axios
      .get<Blob>(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev/${brevId}/pdf`, {
        responseType: "blob",
        headers: { Accept: "application/pdf" },
      })
      .catch((error) => {
        if (error?.response?.status === 404) {
          return null;
        } else {
          throw error;
        }
      })
  )?.data ?? null;

export const delvisOppdaterBrev = async (saksId: string, brevId: string | number, body: DelvisOppdaterBrevRequest) =>
  (await axios.patch<DelvisOppdaterBrevResponse>(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev/${brevId}`, body))
    .data;

export const slettBrev = async (saksId: string, brevId: string | number) =>
  (await axios.delete<void>(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev/${brevId}`)).data;

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

export const fjernOverstyrtMottaker = async (argz: { saksId: string; brevId: string | number }) => {
  return (await axios.delete(`${SKRIBENTEN_API_BASE_PATH}/sak/${argz.saksId}/brev/${argz.brevId}/mottaker`)).data;
};

export const attesterBrev = async (args: {
  saksId: string;
  brevId: string | number;
  frigiReservasjon?: boolean;
  request: OppdaterBrevRequest;
}) => {
  const frigiReservasjon = args.frigiReservasjon ?? true;

  return (
    await axios.put<BrevResponse>(
      `${SKRIBENTEN_API_BASE_PATH}/sak/${args.saksId}/brev/${args.brevId}/attestering?frigiReservasjon=${frigiReservasjon}`,
      {
        saksbehandlerValg: args.request.saksbehandlerValg,
        redigertBrev: args.request.redigertBrev,
      },
    )
  ).data;
};
