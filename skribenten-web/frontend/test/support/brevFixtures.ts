import { SpraakKode } from "~/types/apiTypes";
import {
  type BrevInfo,
  type BrevResponse,
  type BrevStatus,
  type BrevType,
  Distribusjonstype,
  type Mottaker,
  type NAVEnhet,
  type NavAnsatt,
  type SaksbehandlerValg,
} from "~/types/brev";
import { type EditedLetter, type Signatur } from "~/types/brevbakerTypes";
import { type Nullable } from "~/types/Nullable";

import { editedLetter } from "./letterEditorTestUtils";

export const brevResponse = ({
  info = brevInfo({}),
  redigertBrev = editedLetter({}),
  redigertBrevHash = "redigertBrevHash",
  saksbehandlerValg = {},
  valgteVedlegg = null,
}: {
  info?: BrevInfo;
  redigertBrev?: EditedLetter;
  redigertBrevHash?: string;
  saksbehandlerValg?: SaksbehandlerValg;
  valgteVedlegg?: BrevResponse["valgteVedlegg"];
} = {}): BrevResponse => ({
  info,
  redigertBrev,
  redigertBrevHash,
  saksbehandlerValg,
  propertyUsage: null,
  valgteVedlegg,
});

export const brevInfo = (args: {
  id?: number;
  opprettetAv?: NavAnsatt;
  opprettet?: string;
  sistredigertAv?: NavAnsatt;
  sistredigert?: string;
  brevkode?: string;
  brevtittel?: string;
  brevtype?: BrevType;
  status?: BrevStatus;
  distribusjonstype?: Distribusjonstype;
  mottaker?: Nullable<Mottaker>;
  avsenderEnhet?: NAVEnhet;
  spraak?: SpraakKode;
  journalpostId?: Nullable<number>;
  vedtaksId?: Nullable<number>;
  saksId?: number;
}): BrevInfo => ({
  id: args.id ?? 1,
  opprettetAv: args.opprettetAv ?? { id: "Z990297", navn: "Opp R. av" },
  opprettet: args.opprettet ?? "2024-09-25T06:21:46.033Z",
  sistredigertAv: args.sistredigertAv ?? { id: "Z990297", navn: "Sist R. av" },
  sistredigert: args.sistredigert ?? "2024-09-25T08:54:51.520Z",
  brevkode: args.brevkode ?? "INFORMASJON_OM_SAKSBEHANDLINGSTID",
  brevtittel: args.brevtittel ?? "Informasjon om saksbehandlingstid",
  brevtype: args.brevtype ?? "INFORMASJONSBREV",
  status: args.status ?? { type: "Kladd" },
  distribusjonstype: args.distribusjonstype ?? Distribusjonstype.SENTRALPRINT,
  mottaker: args.mottaker ?? null,
  avsenderEnhet: args.avsenderEnhet ?? { enhetNr: "0001", navn: "NAV Familie- og pensjonsytelser" },
  spraak: args.spraak ?? SpraakKode.Engelsk,
  journalpostId: args.journalpostId ?? null,
  vedtaksId: args.vedtaksId ?? null,
  saksId: args.saksId ?? 22981081,
});

export const signatur = (
  args: {
    hilsenTekst?: string;
    saksbehandlerNavn?: string;
    attesterendeSaksbehandlerNavn?: string;
    navAvsenderEnhet?: string;
  } = {},
): Signatur => ({
  hilsenTekst: args.hilsenTekst ?? "Sincerely",
  saksbehandlerNavn: args.saksbehandlerNavn ?? "Sak S. Behandler",
  attesterendeSaksbehandlerNavn: args.attesterendeSaksbehandlerNavn ?? "Attest S. Behandler",
  navAvsenderEnhet: args.navAvsenderEnhet ?? "Nav Arbeid og ytelser Sørlandet",
});
