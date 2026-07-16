import { randomInt } from "node:crypto";

import { create, newItem, newLiteral, newParagraph, newVariable } from "~/Brevredigering/LetterEditor/actions/common";
import { type LetterEditorState, type LiteralIndex } from "~/Brevredigering/LetterEditor/model/state";
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
import {
  type AnyBlock,
  type Cell,
  type Content,
  type EditedLetter,
  type ElementTags,
  type Identifiable,
  type Item,
  type ItemList,
  ListType,
  type LiteralValue,
  type NewLine,
  type ParagraphBlock,
  type Row,
  type Sakspart,
  type Signatur,
  type Table,
  type TextContent,
  type Title,
  type Title1Block,
  type Title2Block,
  type Title3Block,
  type VariableValue,
} from "~/types/brevbakerTypes";
import { type Nullable } from "~/types/Nullable";

// ─── API-level fixtures ───────────────────────────────────────────────────────

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
  signatur = defaultSignatur(),
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
  signatur,
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

function defaultSignatur(): Signatur {
  return {
    hilsenTekst: "Sincerely",
    saksbehandlerNavn: "Sak S. Behandler",
    attesterendeSaksbehandlerNavn: "Attest S. Behandler",
    navAvsenderEnhet: "Nav Arbeid og ytelser Sørlandet",
  };
}

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

// ─── LetterEditorState factory ────────────────────────────────────────────────

/**
 * Builds a minimal LetterEditorState with default info and signatur.
 * Delegates to `create` and `brevResponse`/`editedLetter` so all invariants
 * (normalizeTableSeparators etc.) are applied consistently.
 */
export function letter(...blocks: AnyBlock[]): LetterEditorState {
  return create(brevResponse({ redigertBrev: editedLetter({ blocks }) }));
}

// ─── Block builders ───────────────────────────────────────────────────────────

export type ParagraphArgs = {
  id?: number;
  content: Content[];
  deletedContent?: number[];
};

export function paragraph(content?: Content[] | ParagraphArgs): ParagraphBlock {
  if (Array.isArray(content) || content === undefined) {
    const id = randomId();
    return newParagraph({ id, content: withParent(content ?? [], id) });
  } else {
    const id = content.id ?? null;
    return newParagraph({
      id,
      content: withParent(content.content, id),
      deletedContent: content.deletedContent,
    });
  }
}

export function withDeleted<T extends AnyBlock>(block: T, deletedContent: number[]): T {
  return { ...block, deletedContent };
}

export function withMissingFromTemplate<T extends AnyBlock>(block: T, missingFromTemplate = true): T {
  return { ...block, missingFromTemplate };
}

type TitleArgs = { id?: Nullable<number>; content: TextContent[] };

function makeTitle<T extends "TITLE1" | "TITLE2" | "TITLE3">(
  type: T,
  contentOrArgs: TextContent | TitleArgs,
  rest: TextContent[],
): {
  id: Nullable<number>;
  parentId: null;
  editable: boolean;
  type: T;
  deletedContent: number[];
  content: TextContent[];
  missingFromTemplate: boolean;
} {
  if ("content" in contentOrArgs && !("text" in contentOrArgs)) {
    const args = contentOrArgs as TitleArgs;
    const id = args.id ?? randomId();
    return {
      id,
      parentId: null,
      editable: true,
      type,
      deletedContent: [],
      content: withParent(args.content, id),
      missingFromTemplate: false,
    };
  }
  const id = randomId();
  const content = [contentOrArgs as TextContent, ...rest];
  return {
    id,
    parentId: null,
    editable: true,
    type,
    deletedContent: [],
    content: withParent(content, id),
    missingFromTemplate: false,
  };
}

export function title1(contentOrArgs: TextContent | TitleArgs, ...rest: TextContent[]): Title1Block {
  return makeTitle("TITLE1", contentOrArgs, rest);
}

export function title2(contentOrArgs: TextContent | TitleArgs, ...rest: TextContent[]): Title2Block {
  return makeTitle("TITLE2", contentOrArgs, rest);
}

export function title3(contentOrArgs: TextContent | TitleArgs, ...rest: TextContent[]): Title3Block {
  return makeTitle("TITLE3", contentOrArgs, rest);
}

// ─── Content builders ─────────────────────────────────────────────────────────

/**
 * Creates a literal with `editedText: null` (unedited template element).
 * Pass an explicit `editedText` to simulate user-edited text.
 */
export function literal(
  args:
    | {
        id?: Nullable<number>;
        parentId?: Nullable<number>;
        text: string;
        editedText?: Nullable<string>;
        tags?: ElementTags[];
      }
    | string,
): LiteralValue {
  if (typeof args === "string") {
    args = { text: args };
  }
  return newLiteral({
    id: args.id ?? randomId(),
    parentId: args.parentId ?? null,
    text: args.text,
    editedText: args.editedText ?? null,
    tags: args.tags ?? [],
  });
}

export function variable(text: string): VariableValue {
  return newVariable({ id: randomId(), text });
}

export function newLine(): NewLine {
  return { id: randomId(), parentId: null, type: "NEW_LINE", text: "", fontType: "PLAIN" };
}

export function itemList(args: {
  id?: Nullable<number>;
  parentId?: Nullable<number>;
  listType?: ListType;
  editedListType?: ListType;
  items: Item[];
  deletedItems?: number[];
}): ItemList {
  const id = args.id ?? randomId();
  return {
    id,
    parentId: args.parentId ?? null,
    type: "ITEM_LIST",
    listType: args.listType ?? ListType.PUNKTLISTE,
    editedListType: args.editedListType,
    items: withParent(args.items, id),
    deletedItems: args.deletedItems ?? [],
  };
}

export function item(
  contentOrArgs: TextContent | { id?: Nullable<number>; content?: TextContent[] },
  ...rest: TextContent[]
): Item {
  if ("content" in contentOrArgs && !("text" in contentOrArgs)) {
    const args = contentOrArgs as { id?: Nullable<number>; content?: TextContent[] };
    const id = args.id ?? randomId();
    return newItem({ id, content: withParent(args.content ?? [], id) });
  }
  const id = randomId();
  const content = [contentOrArgs as TextContent, ...rest];
  return newItem({ id, content: withParent(content, id) });
}

// ─── Table builders ───────────────────────────────────────────────────────────

export function table(headerCells: Cell[], rows: Row[]): Table {
  if (rows.some((r) => r.cells.length !== headerCells.length)) {
    throw new Error("All rows must have the same number of cells as there are header cells");
  }
  const tableId = randomId();
  const headerId = randomId();
  return {
    id: tableId,
    parentId: null,
    type: "TABLE",
    rows: withParent(rows, tableId),
    header: {
      id: headerId,
      parentId: tableId,
      deletedColSpecs: [],
      colSpec: headerCells.map((c) => {
        const colSpecId = randomId();
        return {
          id: colSpecId,
          parentId: headerId,
          headerContent: { ...c, parentId: colSpecId },
          alignment: "LEFT",
          span: 1,
        };
      }),
    },
    deletedRows: [],
  };
}

export function cell(...content: TextContent[]): Cell {
  const id = randomId();
  return { id, parentId: null, text: withParent(content, id), deletedContent: [] };
}

export function row(...cells: Cell[]): Row {
  const id = randomId();
  return { id, parentId: null, cells: withParent(cells, id), deletedCells: [] };
}

// ─── Utilities ────────────────────────────────────────────────────────────────

export function withParent<T extends Identifiable>(
  content: T[],
  parentId: number | null,
  replaceExisting: boolean = false,
): T[] {
  return content.map((c) => ({
    ...c,
    parentId: replaceExisting ? parentId : (c.parentId ?? parentId),
  }));
}

export function asNew<T extends Identifiable>(c: T, keepParent: boolean = false): T {
  return { ...c, id: null, parentId: keepParent ? c.parentId : null };
}

export function select<T>(from: LetterEditorState, id: Partial<LiteralIndex> & { blockIndex: number }): T {
  const block = from.redigertBrev.blocks[id.blockIndex];

  if (id.contentIndex == null) {
    return block as T;
  } else {
    const content = block?.content?.[id.contentIndex];

    if ("itemIndex" in id && id.itemIndex != null) {
      const it = (content as ItemList)?.items?.[id.itemIndex];
      return id.itemContentIndex == null ? (it as T) : (it?.content?.[id.itemContentIndex] as T);
    } else if ("rowIndex" in id && id.rowIndex != null) {
      if (id.rowIndex === -1) throw new Error("select not implemented for header selection");

      const r = (content as Table)?.rows?.[id.rowIndex];
      if (id.cellIndex == null) {
        return r as T;
      } else {
        const c = r?.cells?.[id.cellIndex];
        return id.cellContentIndex == null ? (c as T) : (c?.text?.[id.cellContentIndex] as T);
      }
    } else {
      return content as T;
    }
  }
}

// ─── Internal helpers ─────────────────────────────────────────────────────────

function randomId() {
  return randomInt(1_000_000);
}
