import { newLiteral, newVariable } from "~/Brevredigering/LetterEditor/actions/common";
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
import { type AnyBlock, type EditedLetter, type Sakspart, type Signatur, type Title } from "~/types/brevbakerTypes";
import { type Nullable } from "~/types/Nullable";

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

export const editedLetter = ({
  title,
  sakspart,
  blocks,
  signatur: letterSignatur = signatur(),
  deletedBlocks,
}: {
  title?: Title;
  sakspart?: Sakspart;
  blocks?: AnyBlock[];
  signatur?: Signatur;
  deletedBlocks?: number[];
} = {}): EditedLetter => ({
  title: title ?? {
    text: [newLiteral({ text: "Information about application processing time" })],
    deletedContent: [],
  },
  sakspart: sakspart ?? {
    gjelderNavn: "TRYGG ANBEFALING",
    gjelderFoedselsnummer: "21418744917",
    saksnummer: "22981081",
    dokumentDato: "2024-09-25",
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
      missingFromTemplate: false,
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
      missingFromTemplate: false,
      type: "PARAGRAPH",
    },
  ],
  signatur: letterSignatur,
  deletedBlocks: deletedBlocks ?? [],
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
