import { randomInt } from "node:crypto";

import type { ItemContentIndex } from "~/Brevredigering/LetterEditor/actions/model";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { SpraakKode } from "~/types/apiTypes";
import { Distribusjonstype } from "~/types/brev";
import type {
  AnyBlock,
  Content,
  Identifiable,
  Item,
  ItemList,
  LiteralValue,
  ParagraphBlock,
  TextContent,
  Title1Block,
  Title2Block,
  VariableValue,
} from "~/types/brevbakerTypes";
import { TITLE2 } from "~/types/brevbakerTypes";
import { ITEM_LIST, LITERAL, PARAGRAPH, TITLE1, VARIABLE } from "~/types/brevbakerTypes";

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

export function paragraph(...content: Content[]): ParagraphBlock {
  return {
    id: randomInt(1000),
    editable: true,
    type: PARAGRAPH,
    deletedContent: [],
    content,
  };
}

export function withDeleted<T extends AnyBlock>(block: T, deletedContent: number[]): T {
  return {
    ...block,
    deletedContent,
  };
}

export function title1(...content: TextContent[]): Title1Block {
  return {
    id: randomInt(1000),
    editable: true,
    type: TITLE1,
    deletedContent: [],
    content,
  };
}
export function title2(...content: TextContent[]): Title2Block {
  return {
    id: randomInt(1000),
    editable: true,
    type: TITLE2,
    deletedContent: [],
    content,
  };
}

export function literal(text: string, editedText: string | null = null): LiteralValue {
  return {
    id: randomInt(1000),
    type: LITERAL,
    text,
    editedText,
  };
}

export function variable(text: string): VariableValue {
  return {
    id: randomInt(1000),
    type: VARIABLE,
    text,
  };
}

export function itemList(...items: Item[]): ItemList {
  return {
    id: randomInt(1000),
    type: ITEM_LIST,
    items,
    deletedItems: [],
  };
}

export function item(...content: TextContent[]): Item {
  return { id: randomInt(1000), content };
}

export function asNew<T extends Identifiable>(c: T): T {
  return { ...c, id: null };
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
