// TODO: Midlertidig mock til backend-API for del-automatiske brev er klart. Slett filen og alle referanser til den da.
// Aktiveres lokalt med `VITE_MOCK_DELAUTOMATISKE_BREV=true npm run dev`.
import { AxiosHeaders, type AxiosResponse } from "axios";

import {
  type BestillDelautomatiskBrevRequest,
  type BestillDelautomatiskBrevResponse,
  type LetterMetadata,
} from "~/types/apiTypes";

export const brukMockForDelautomatiskeBrev = import.meta.env.VITE_MOCK_DELAUTOMATISKE_BREV === "true";

export const MOCK_BREVKODE_SOM_FEILER = "MOCK_DELAUTO_FEILER";

const mockDelautomatiskeBrevmaler: LetterMetadata[] = [
  {
    id: "MOCK_DELAUTO_ETTEROPPGJOER",
    name: "Orientering om etteroppgjør av uføretrygd",
    brevkategori: "Informasjon",
    brevsystem: "EXSTREAM",
    dokumentkategoriCode: "IB",
    redigerbart: false,
    redigerbarBrevtittel: false,
    spraak: ["NB", "NN", "EN"],
    delautomatisk: true,
  },
  {
    id: "MOCK_DELAUTO_OPPHOER_BT",
    name: "Varsel om opphør av barnetillegg",
    brevkategori: "Varsel",
    brevsystem: "EXSTREAM",
    dokumentkategoriCode: "B",
    redigerbart: false,
    redigerbarBrevtittel: false,
    spraak: ["NB", "NN"],
    delautomatisk: true,
  },
  {
    id: "MOCK_DELAUTO_KVITTERING",
    name: "Bekreftelse på mottatt søknad",
    brevkategori: "Informasjon",
    brevsystem: "EXSTREAM",
    dokumentkategoriCode: "B",
    redigerbart: false,
    redigerbarBrevtittel: false,
    spraak: ["NB", "NN", "EN"],
    delautomatisk: true,
  },
  {
    id: MOCK_BREVKODE_SOM_FEILER,
    name: "Mock: del-automatisk brev som feiler ved bestilling",
    brevkategori: "Informasjon",
    brevsystem: "EXSTREAM",
    dokumentkategoriCode: "B",
    redigerbart: false,
    redigerbarBrevtittel: false,
    spraak: ["NB"],
    delautomatisk: true,
  },
];

export const leggTilMockBrevmaler = (brevmaler: LetterMetadata[]): LetterMetadata[] =>
  brukMockForDelautomatiskeBrev ? [...brevmaler, ...mockDelautomatiskeBrevmaler] : brevmaler;

export const leggTilMockBrevmalKoder = (brevmalKoder: string[]): string[] =>
  brukMockForDelautomatiskeBrev ? [...brevmalKoder, ...mockDelautomatiskeBrevmaler.map((b) => b.id)] : brevmalKoder;

export async function mockBestillDelautomatiskBrev(
  request: BestillDelautomatiskBrevRequest,
): Promise<AxiosResponse<BestillDelautomatiskBrevResponse>> {
  await new Promise((resolve) => setTimeout(resolve, 1000));

  const data: BestillDelautomatiskBrevResponse =
    request.brevkode === MOCK_BREVKODE_SOM_FEILER
      ? { failureType: "SKRIBENTEN_INTERNAL_ERROR", journalpostId: null }
      : { failureType: null, journalpostId: Math.floor(100_000_000 + Math.random() * 900_000_000) };

  return { data, status: 200, statusText: "OK", headers: {}, config: { headers: new AxiosHeaders() } };
}
