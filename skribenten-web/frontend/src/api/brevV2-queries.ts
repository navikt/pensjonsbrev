import axios from "axios";

import { SKRIBENTEN_API_BASE_PATH } from "~/api/skribenten-api-endpoints";
import { type OpprettBrevRequest, type ValgteVedleggRequest } from "~/types/brev";
import {
  type BrevResponseV2,
  type DiffResponseV2,
  type LetterV2,
  type OppdaterAttesteringRequestV2,
  type OppdaterBrevRequestV2,
} from "~/types/brevV2";

const brevV2Url = (saksId: string | number, brevId?: number) =>
  `${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev/v2${brevId === undefined ? "" : `/${brevId}`}`;

export const brevV2Keys = {
  all: ["BREV_V2"] as const,
  id: (brevId: number) => [...brevV2Keys.all, brevId] as const,
  attestering: (brevId: number) => [...brevV2Keys.all, "ATTESTERING", brevId] as const,
  diff: (brevId: number, redigertBrevHash: string, split: boolean) =>
    [...brevV2Keys.all, "DIFF", brevId, redigertBrevHash, split] as const,
};

export const getBrevV2 = (saksId: string | number, brevId: number, reserver: boolean = true) => ({
  queryKey: brevV2Keys.id(brevId),
  queryFn: async () => (await axios.get<BrevResponseV2>(`${brevV2Url(saksId, brevId)}?reserver=${reserver}`)).data,
});

export const getBrevAttesteringV2 = (saksId: string | number, brevId: number, reserver: boolean = true) => ({
  queryKey: brevV2Keys.attestering(brevId),
  queryFn: async () =>
    (await axios.get<BrevResponseV2>(`${brevV2Url(saksId, brevId)}/attestering?reserver=${reserver}`)).data,
});

export async function createBrevV2(saksId: string | number, request: OpprettBrevRequest) {
  return (await axios.post<BrevResponseV2>(brevV2Url(saksId), request)).data;
}

export async function oppdaterBrevV2(args: {
  saksId: string | number;
  brevId: number;
  request: OppdaterBrevRequestV2;
  frigiReservasjon: boolean;
}) {
  return (
    await axios.put<BrevResponseV2>(
      `${brevV2Url(args.saksId, args.brevId)}?frigiReservasjon=${args.frigiReservasjon}`,
      { saksbehandlerValg: args.request.saksbehandlerValg, redigertBrev: args.request.redigertBrev },
    )
  ).data;
}

export async function oppdaterRedigertBrevV2(args: {
  saksId: string | number;
  brevId: number;
  redigertBrev: LetterV2;
  frigiReservasjon: boolean;
}) {
  return (
    await axios.put<BrevResponseV2>(
      `${brevV2Url(args.saksId, args.brevId)}/redigertBrev?frigiReservasjon=${args.frigiReservasjon}`,
      args.redigertBrev,
    )
  ).data;
}

export async function oppdaterValgteVedleggV2(saksId: string | number, brevId: number, body: ValgteVedleggRequest) {
  return (await axios.put<BrevResponseV2>(`${brevV2Url(saksId, brevId)}/valgteVedlegg`, body)).data;
}

export async function tilbakestillBrevV2(saksId: string | number, brevId: number) {
  return (await axios.post<BrevResponseV2>(`${brevV2Url(saksId, brevId)}/tilbakestill`)).data;
}

export const getBrevDiffV2 = (
  saksId: string | number,
  brevId: number,
  redigertBrevHash: string,
  redigertBrev: LetterV2,
  split: boolean = false,
) => ({
  queryKey: brevV2Keys.diff(brevId, redigertBrevHash, split),
  queryFn: async () =>
    (await axios.post<DiffResponseV2>(`${brevV2Url(saksId, brevId)}/diff?split=${split}`, redigertBrev)).data,
});

export async function attesterBrevV2(args: {
  saksId: string | number;
  brevId: number;
  request: OppdaterAttesteringRequestV2;
  frigiReservasjon: boolean;
}) {
  return (
    await axios.put<BrevResponseV2>(
      `${brevV2Url(args.saksId, args.brevId)}/attestering?frigiReservasjon=${args.frigiReservasjon}`,
      { redigertBrev: args.request.redigertBrev },
    )
  ).data;
}

export async function lagreAttestertBrevtekstV2(args: {
  saksId: string | number;
  brevId: number;
  redigertBrev: LetterV2;
  frigiReservasjon: boolean;
}) {
  return (
    await axios.put<BrevResponseV2>(
      `${brevV2Url(args.saksId, args.brevId)}/attestering/redigertBrev?frigiReservasjon=${args.frigiReservasjon}`,
      args.redigertBrev,
    )
  ).data;
}
