import { randomInt } from "node:crypto";

import type { ItemContentIndex } from "~/Brevredigering/LetterEditor/actions/model";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
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
  VariableValue,
} from "~/types/brevbakerTypes";
import { ITEM_LIST, LITERAL, PARAGRAPH, TITLE1, VARIABLE } from "~/types/brevbakerTypes";

export function letter(...blocks: AnyBlock[]): LetterEditorState {
  return {
    renderedLetter: {
      editedLetter: {
        blocks: blocks,
        deletedBlocks: [],
      },
      title: "tittel",
      sakspart: { gjelderNavn: "navn", gjelderFoedselsnummer: "123", saksnummer: "456", dokumentDato: "2022-01-01" },
      signatur: {
        hilsenTekst: "Mvh",
        navAvsenderEnhet: "enhet",
        saksbehandlerRolleTekst: "Saksbehandler",
        saksbehandlerNavn: "navn",
      },
    },
    focus: { blockIndex: 0, contentIndex: 0 },
  };
}

export function paragraph(...content: Content[]): ParagraphBlock {
  return {
    id: randomInt(1000),
    editable: true,
    type: PARAGRAPH,
    content,
  };
}

export function title(...content: TextContent[]): Title1Block {
  return {
    id: randomInt(1000),
    editable: true,
    type: TITLE1,
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
  };
}

export function item(...content: TextContent[]): Item {
  return { id: randomInt(1000), content };
}

export function asNew<T extends Identifiable>(c: T): T {
  return { ...c, id: null };
}

export function select<T>(from: LetterEditorState, id: Partial<ItemContentIndex> & { blockIndex: number }): T {
  const block = from.renderedLetter.editedLetter.blocks[id.blockIndex];

  if (id.contentIndex == null) {
    return block as T;
  } else {
    const content = block.content[id.contentIndex];

    if (id.itemIndex == null) {
      return content as T;
    } else {
      const item = (content as ItemList).items[id.itemIndex];

      return id.itemContentIndex == null ? (item as T) : (item.content[id.itemContentIndex] as T);
    }
  }
}
