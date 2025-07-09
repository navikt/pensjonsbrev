import { newLiteral, newParagraph, newVariable } from "~/Brevredigering/LetterEditor/actions/common";
import { SpraakKode } from "~/types/apiTypes";
import type {
  BrevInfo,
  BrevResponse,
  BrevStatus,
  BrevType,
  Mottaker,
  NavAnsatt,
  NAVEnhet,
  SaksbehandlerValg,
} from "~/types/brev";
import { Distribusjonstype } from "~/types/brev";
import type {
  AnyBlock,
  Content,
  EditedLetter,
  Item,
  ItemList,
  LiteralValue,
  ParagraphBlock,
  Sakspart,
  Signatur,
  TextContent,
  Title1Block,
  Title2Block,
  VariableValue,
} from "~/types/brevbakerTypes";
import { FontType } from "~/types/brevbakerTypes";
import type { Nullable } from "~/types/Nullable";

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

export const nyRedigertBrev = ({
  title,
  sakspart,
  blocks,
  signatur = nySignatur({}),
  deletedBlocks,
}: {
  title?: string;
  sakspart?: Sakspart;
  blocks?: AnyBlock[];
  signatur?: Signatur;
  deletedBlocks?: number[];
}): EditedLetter => {
  return {
    title: title ?? "Information about application processing time",
    sakspart: sakspart ?? {
      gjelderNavn: "TRYGG ANBEFALING",
      gjelderFoedselsnummer: "21418744917",
      saksnummer: "22981081",
      dokumentDato: "25/09/2024",
    },
    blocks: blocks ?? [
      {
        id: 272_720_182,
        parentId: null,
        editable: true,
        content: [
          newLiteral({ id: 1_507_865_607, parentId: 272_720_182, text: "We received your application for " }),
          newVariable({ id: -726_051_414, parentId: 272_720_182, text: "alderspensjon" }),
          newLiteral({
            id: -711_242_333,
            parentId: 272_720_182,
            text: " from the Norwegian National Insurance Scheme on ",
          }),
          newVariable({ id: -694_080_035, parentId: 272_720_182, text: "24 July 2024" }),
          newLiteral({ id: -1_114_690_237, parentId: 272_720_182, text: "." }),
        ],
        deletedContent: [],
        type: "PARAGRAPH",
      },
      {
        id: 822_540_105,
        parentId: null,
        editable: true,
        content: [
          newLiteral({
            id: -1_114_690_237,
            parentId: 822_540_105,
            text: "Our processing time for this type of application is usually ",
          }),
          newVariable({ id: 1_834_595_758, parentId: 822_540_105, text: "10" }),
          newLiteral({ id: 1_838_606_639, parentId: 822_540_105, text: " weeks." }),
        ],
        deletedContent: [],
        type: "PARAGRAPH",
      },
    ],
    signatur: signatur,
    deletedBlocks: deletedBlocks ?? [],
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
  brevtype?: BrevType;
  status?: BrevStatus;
  distribusjonstype?: Distribusjonstype;
  mottaker?: Nullable<Mottaker>;
  avsenderEnhet?: Nullable<NAVEnhet>;
  spraak?: SpraakKode;
  journalpostId?: Nullable<number>;
  vedtaksId?: Nullable<number>;
}): BrevInfo => {
  return {
    id: args.id ?? 1,
    opprettetAv: args.opprettetAv ?? { id: "Z990297", navn: "Opp R. av" },
    opprettet: args.opprettet ?? "2024-09-25T06:21:46.033Z",
    sistredigertAv: args.sistredigertAv ?? { id: "Z990297", navn: "Sist R. av" },
    sistredigert: args.sistredigert ?? "2024-09-25T08:54:51.520Z",
    brevkode: args.brevkode ?? "INFORMASJON_OM_SAKSBEHANDLINGSTID",
    brevtittel: args.brevtittel ?? "Informasjon om saksbehandlingstid",
    brevtype: args.brevtype ?? "INFORMASJONSBREV",
    status: args.status ?? {
      type: "Kladd",
    },
    distribusjonstype: args.distribusjonstype ?? Distribusjonstype.SENTRALPRINT,
    mottaker: args.mottaker ?? null,
    avsenderEnhet: args.avsenderEnhet ?? null,
    spraak: args.spraak ?? SpraakKode.Engelsk,
    journalpostId: args.journalpostId ?? null,
    vedtaksId: args.vedtaksId ?? null,
  };
};

//TODO - kan heller bruke newLiteral fra common.ts
export const nyLiteral = (args: { id?: Nullable<number>; text?: string; editedText?: string }): LiteralValue => ({
  type: "LITERAL",
  id: args.id ?? null,
  parentId: null,
  text: args.text ?? "ny literal default text",
  editedText: args.editedText ?? args.text ?? "ny literal default edited-text",
  tags: [],
  fontType: FontType.PLAIN,
  editedFontType: null,
});

export const nyVariable = (args: { id?: Nullable<number>; name?: string; text?: string }): VariableValue => ({
  type: "VARIABLE",
  id: args.id ?? 1,
  parentId: null,
  name: args.name,
  text: args.text ?? "ny variable default text",
  fontType: FontType.PLAIN,
});

//TODO - kan heller bruke newItem fra common.ts
export const nyItem = (args: { id?: Nullable<number>; content?: TextContent[] }): Item => ({
  id: args.id ?? null,
  parentId: null,
  content: args.content ?? [nyVariable({})],
  deletedContent: [],
});

//TODO - kan heller bruke newItemList fra common.ts
export const nyItemList = (args: { id?: Nullable<number>; items?: Item[] }): ItemList => ({
  type: "ITEM_LIST",
  id: args.id ?? null,
  parentId: null,
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
  parentId: null,
  editable: args.editable ?? true,
  content: args.content ?? [nyVariable({})],
  deletedContent: [],
});

export const nyTitle2Block = (args: {
  id?: Nullable<number>;
  editable?: boolean;
  content?: TextContent[];
}): Title2Block => ({
  type: "TITLE2",
  id: args.id ?? null,
  parentId: null,
  editable: args.editable ?? true,
  content: args.content ?? [nyVariable({})],
  deletedContent: [],
});

export const nyParagraphBlock = (args: { id?: Nullable<number>; content?: Content[] }): ParagraphBlock =>
  newParagraph({
    id: args.id,
    content: args.content ?? [],
  });

export const nySignatur = (args: {
  hilsenTekst?: string;
  saksbehandlerRolleTekst?: string;
  saksbehandlerNavn?: string;
  attesterendeSaksbehandlerNavn?: string;
  navAvsenderEnhet?: string;
}): Signatur => ({
  hilsenTekst: args.hilsenTekst ?? "Yours sincerely",
  saksbehandlerRolleTekst: args.saksbehandlerRolleTekst ?? "Caseworker",
  saksbehandlerNavn: args.saksbehandlerNavn ?? "Sak S. Behandler",
  attesterendeSaksbehandlerNavn: args.attesterendeSaksbehandlerNavn ?? "Attest S. Behandler",
  navAvsenderEnhet: args.navAvsenderEnhet ?? "Nav Arbeid og ytelser Sørlandet",
});
