import type {
  BrevInfo,
  BrevResponse,
  BrevStatus,
  Mottaker,
  NavAnsatt,
  NAVEnhet,
  SaksbehandlerValg,
} from "~/types/brev";
import type { AnyBlock, EditedLetter, Sakspart, Signatur } from "~/types/brevbakerTypes";
import type { Nullable } from "~/types/Nullable";

import { SpraakKode } from "../../src/types/apiTypes";
import { Distribusjonstype } from "../../src/types/brev";

export const nyBrevResponse = ({
  info = nyBrevInfo({}),
  redigertBrev = nyRedigertBrev({}),
  redigertBrevHash = "redigertBrevHash",
  saksbehandlerValg = {},
}: {
  info?: BrevInfo;
  redigertBrev?: EditedLetter;
  redigertBrevHash?: string;
  saksbehandlerValg?: SaksbehandlerValg;
}): BrevResponse => {
  return {
    info: info,
    redigertBrev: redigertBrev,
    redigertBrevHash: redigertBrevHash,
    saksbehandlerValg: saksbehandlerValg,
  };
};

export const nyRedigertBrev = (args: {
  title?: string;
  sakspart?: Sakspart;
  blocks?: AnyBlock[];
  signatur?: Signatur;
  deletedBlocks?: number[];
}): EditedLetter => {
  return {
    title: args.title ?? "Information about application processing time",
    sakspart: args.sakspart ?? {
      gjelderNavn: "TRYGG ANBEFALING",
      gjelderFoedselsnummer: "21418744917",
      saksnummer: "22981081",
      dokumentDato: "25/09/2024",
    },
    blocks: args.blocks ?? [
      {
        id: 272_720_182,
        editable: true,
        content: [
          {
            id: 1_507_865_607,
            text: "We received your application for ",
            editedText: null,
            type: "LITERAL",
          },
          {
            id: -726_051_414,
            text: "alderspensjon",
            type: "VARIABLE",
          },
          {
            id: -711_242_333,
            text: " from the Norwegian National Insurance Scheme on ",
            editedText: null,
            type: "LITERAL",
          },
          {
            id: -694_080_035,
            text: "24 July 2024",
            type: "VARIABLE",
          },
          {
            id: 46,
            text: ".",
            editedText: null,
            type: "LITERAL",
          },
        ],
        deletedContent: [],
        type: "PARAGRAPH",
      },
      {
        id: 822_540_105,
        editable: true,
        content: [
          {
            id: -1_114_690_237,
            text: "Our processing time for this type of application is usually ",
            editedText: null,
            type: "LITERAL",
          },
          {
            id: 1_834_595_758,
            text: "10",
            type: "VARIABLE",
          },
          {
            id: 1_838_606_639,
            text: " weeks.",
            editedText: null,
            type: "LITERAL",
          },
        ],
        deletedContent: [],
        type: "PARAGRAPH",
      },
    ],
    signatur: {
      hilsenTekst: args.signatur?.hilsenTekst ?? "Yours sincerely",
      saksbehandlerRolleTekst: args.signatur?.saksbehandlerRolleTekst ?? "Caseworker",
      saksbehandlerNavn: args.signatur?.saksbehandlerNavn ?? "Sak S. Behandler",
      attesterendeSaksbehandlerNavn: args.signatur?.attesterendeSaksbehandlerNavn ?? "Attest S. Behandler",
      navAvsenderEnhet: args.signatur?.navAvsenderEnhet ?? "NAV Arbeid og ytelser Sørlandet",
    },
    deletedBlocks: [],
  };
};

export const nyBrevInfo = (args: {
  id?: number;
  opprettetAv?: NavAnsatt;
  opprettet?: string;
  sistredigertAv?: NavAnsatt;
  sistredigert?: string;
  brevkode?: string;
  brevtittel?: string;
  status?: BrevStatus;
  distribusjonstype?: Distribusjonstype;
  mottaker?: Nullable<Mottaker>;
  avsenderEnhet?: Nullable<NAVEnhet>;
  spraak?: SpraakKode;
}): BrevInfo => {
  return {
    id: args.id ?? 1,
    opprettetAv: args.opprettetAv ?? { id: "Z990297", navn: "Opp R. av" },
    opprettet: args.opprettet ?? "2024-09-25T06:21:46.033Z",
    sistredigertAv: args.sistredigertAv ?? { id: "Z990297", navn: "Sist R. av" },
    sistredigert: args.sistredigert ?? "2024-09-25T08:54:51.520Z",
    brevkode: args.brevkode ?? "INFORMASJON_OM_SAKSBEHANDLINGSTID",
    brevtittel: args.brevtittel ?? "Informasjon om saksbehandlingstid",
    status: args.status ?? {
      type: "Kladd",
    },
    distribusjonstype: args.distribusjonstype ?? Distribusjonstype.SENTRALPRINT,
    mottaker: args.mottaker ?? null,
    avsenderEnhet: args.avsenderEnhet ?? null,
    spraak: args.spraak ?? SpraakKode.Engelsk,
  };
};
