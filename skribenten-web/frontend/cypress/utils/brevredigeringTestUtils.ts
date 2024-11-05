import type {
  BrevInfo,
  BrevResponse,
  BrevStatus,
  Mottaker,
  NavAnsatt,
  NAVEnhet,
  SaksbehandlerValg,
} from "~/types/brev";
import type {
  Content,
  Item,
  ItemList,
  ParagraphBlock,
  TextContent,
  Title1Block,
  VariableValue,
} from "~/types/brevbakerTypes";
import {
  type AnyBlock,
  type EditedLetter,
  type LiteralValue,
  type Sakspart,
  type Signatur,
} from "~/types/brevbakerTypes";
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
      navAvsenderEnhet: args.signatur?.navAvsenderEnhet ?? "Nav Arbeid og ytelser Sørlandet",
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
  journalpostId?: Nullable<number>;
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
    journalpostId: args.journalpostId ?? null,
  };
};

//TODO - kan heller bruke newLiteral fra common.ts
export const nyLiteral = (args: { id?: Nullable<number>; text?: string }): LiteralValue => ({
  type: "LITERAL",
  id: args.id ?? null,
  text: args.text ?? "ny literal default text",
  editedText: args.text ?? "ny literal default edited-text",
});

export const nyVariable = (args: { id?: Nullable<number>; name?: string; text?: string }): VariableValue => ({
  type: "VARIABLE",
  id: args.id ?? 1,
  name: args.name,
  text: args.text ?? "ny variable default text",
});

//TODO - kan heller bruke newItem fra common.ts
export const nyItem = (args: { id?: Nullable<number>; content?: TextContent[] }): Item => ({
  id: args.id ?? null,
  content: args.content ?? [nyVariable({})],
});

//TODO - kan heller bruke newItemList fra common.ts
export const nyItemList = (args: { id?: Nullable<number>; items?: Item[] }): ItemList => ({
  type: "ITEM_LIST",
  id: args.id ?? null,
  items: args.items ?? [nyItem({})],
  deletedItems: [],
});

export const nyTitle1Block = (args: {
  id?: Nullable<number>;
  editable?: boolean;
  content?: TextContent[];
}): Title1Block => ({
  type: "TITLE1",
  id: args.id ?? null,
  editable: args.editable ?? true,
  content: args.content ?? [nyVariable({})],
  deletedContent: [],
});

//TODO - kan heller bruke newParagraph fra common.ts
export const nyParagraphBlock = (args: {
  id?: Nullable<number>;
  editable?: boolean;
  content?: Content[];
}): ParagraphBlock => ({
  type: "PARAGRAPH",
  id: args.id ?? null,
  editable: args.editable ?? true,
  content: args.content ?? [nyVariable({})],
  deletedContent: [],
});
