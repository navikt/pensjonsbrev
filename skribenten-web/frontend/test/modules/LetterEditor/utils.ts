import { randomInt } from "node:crypto";

import { newLiteral, newVariable } from "~/Brevredigering/LetterEditor/actions/common";
import type { ItemContentIndex } from "~/Brevredigering/LetterEditor/actions/model";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { SpraakKode } from "~/types/apiTypes";
import { Distribusjonstype } from "~/types/brev";
import type {
  AnyBlock,
  Cell,
  Content,
  ElementTags,
  Identifiable,
  Item,
  ItemList,
  LiteralValue,
  NewLine,
  ParagraphBlock,
  Row,
  Table,
  TextContent,
  Title1Block,
  Title2Block,
  VariableValue,
} from "~/types/brevbakerTypes";
import { ITEM_LIST, NEW_LINE, PARAGRAPH, TABLE, TITLE1, TITLE2 } from "~/types/brevbakerTypes";
import type { Nullable } from "~/types/Nullable";

export function letter(...blocks: AnyBlock[]): LetterEditorState {
  return {
    isDirty: false,
    info: {
      id: 1,
      opprettetAv: { id: "Z993104", navn: "Zninitre Ennullfire" },
      opprettet: "2024-07-24T09:23:21.381Z",
      sistredigertAv: { id: "Z993104", navn: "Zninitre Ennullfire" },
      sistredigert: "2024-07-26T14:15:57.173Z",
      brevkode: "INFORMASJON_OM_SAKSBEHANDLINGSTID",
      brevtittel: "Informasjon om saksbehandlingstid",
      status: {
        type: "UnderRedigering",
        redigeresAv: { id: "Z993104", navn: "Zninitre Ennullfire" },
      },
      distribusjonstype: Distribusjonstype.SENTRALPRINT,
      mottaker: null,
      avsenderEnhet: null,
      spraak: SpraakKode.Bokmaal,
      journalpostId: null,
    },
    redigertBrev: {
      title: "tittel",
      sakspart: { gjelderNavn: "navn", gjelderFoedselsnummer: "123", saksnummer: "456", dokumentDato: "2022-01-01" },
      blocks: blocks,
      signatur: {
        hilsenTekst: "Mvh",
        navAvsenderEnhet: "enhet",
        saksbehandlerRolleTekst: "Saksbehandler",
        saksbehandlerNavn: "navn",
      },
      deletedBlocks: [],
    },
    redigertBrevHash: "hash1",
    focus: { blockIndex: 0, contentIndex: 0 },
  };
}

function randomId() {
  return randomInt(1_000_000);
}

export function paragraph(...content: Content[]): ParagraphBlock {
  const id = randomId();
  return {
    id,
    parentId: null,
    editable: true,
    type: PARAGRAPH,
    deletedContent: [],
    content: withParent(content, id),
  };
}

export function withDeleted<T extends AnyBlock>(block: T, deletedContent: number[]): T {
  return {
    ...block,
    deletedContent,
  };
}

export function title1(...content: TextContent[]): Title1Block {
  const id = randomId();
  return {
    id,
    parentId: null,
    editable: true,
    type: TITLE1,
    deletedContent: [],
    content: withParent(content, id),
  };
}
export function title2(...content: TextContent[]): Title2Block {
  const id = randomId();
  return {
    id,
    parentId: null,
    editable: true,
    type: TITLE2,
    deletedContent: [],
    content: withParent(content, id),
  };
}

export function literal(args: {
  id?: Nullable<number>;
  parentId?: Nullable<number>;
  text: string;
  editedText?: Nullable<string>;
  tags?: ElementTags[];
}): LiteralValue {
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
  return {
    id: randomId(),
    parentId: null,
    type: NEW_LINE,
    text: "",
  };
}

export function itemList(args: {
  id?: Nullable<number>;
  parentId?: Nullable<number>;
  items: Item[];
  deletedItems?: number[];
}): ItemList {
  const id = args.id ?? randomId();
  return {
    id: id,
    parentId: args.parentId ?? null,
    type: ITEM_LIST,
    items: withParent(args.items, id),
    deletedItems: args.deletedItems ?? [],
  };
}

export function item(...content: TextContent[]): Item {
  const id = randomId();
  return { id: id, parentId: null, content: withParent(content, id), deletedContent: [] };
}

export function table(headerCells: Cell[], rows: Row[]): Table {
  const tableId = randomId();
  const headerId = randomId();
  return {
    id: tableId,
    parentId: null,
    type: TABLE,
    rows: withParent(rows, tableId),
    header: {
      id: headerId,
      parentId: tableId,
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
  return {
    id,
    parentId: null,
    text: withParent(content, id),
  };
}
export function row(...cells: Cell[]): Row {
  const id = randomId();
  return {
    id,
    parentId: null,
    cells: withParent(cells, id),
  };
}

export function withParent<T extends Identifiable>(
  content: T[],
  parentId: number | null,
  replaceExisting: boolean = false,
): T[] {
  return content.map((c) => ({ ...c, parentId: replaceExisting ? parentId : (c.parentId ?? parentId) }));
}

export function asNew<T extends Identifiable>(c: T, keepParent: boolean = false): T {
  return { ...c, id: null, parentId: keepParent ? c.parentId : null };
}

export function select<T>(from: LetterEditorState, id: Partial<ItemContentIndex> & { blockIndex: number }): T {
  const block = from.redigertBrev.blocks[id.blockIndex];

  if (id.contentIndex == null) {
    return block as T;
  } else {
    const content = block?.content?.[id.contentIndex];

    if (id.itemIndex == null) {
      return content as T;
    } else {
      const item = (content as ItemList)?.items?.[id.itemIndex];

      return id.itemContentIndex == null ? (item as T) : (item?.content?.[id.itemContentIndex] as T);
    }
  }
}
