import type { Draft } from "immer";

import type {
  AnyBlock,
  Content,
  Identifiable,
  Item,
  ItemList,
  LiteralValue,
  ParagraphBlock,
  TextContent,
  VariableValue,
} from "~/types/brevbakerTypes";

import { text } from "../../../Brevredigering/LetterEditor/actions/common";
import { ElementTags, ITEM_LIST, LITERAL, NEW_LINE, PARAGRAPH, TABLE, VARIABLE } from "../../../types/brevbakerTypes";
import type { ContentGroup } from "./state";

export function isTextContent(obj: Draft<Identifiable | null | undefined>): obj is Draft<TextContent>;
export function isTextContent(obj: Content | null | undefined): obj is TextContent;
export function isTextContent(obj: Identifiable | null | undefined): obj is TextContent {
  return isLiteral(obj) || isVariable(obj);
}

export function isLiteral(obj: Draft<Identifiable | null | undefined>): obj is Draft<LiteralValue>;
export function isLiteral(obj: Identifiable | null | undefined): obj is LiteralValue {
  return !!obj && "type" in obj && obj.type === LITERAL;
}

export function isVariable(obj: Draft<Identifiable | null | undefined>): obj is Draft<VariableValue>;
export function isVariable(obj: Identifiable | null | undefined): obj is VariableValue {
  return !!obj && "type" in obj && obj.type === VARIABLE;
}

export function isItemList(obj: Draft<Identifiable | null | undefined>): obj is Draft<ItemList>;
export function isItemList(obj: Identifiable | null | undefined): obj is ItemList {
  return !!obj && "type" in obj && obj.type === ITEM_LIST;
}

export function isFritekst(literal: LiteralValue): boolean {
  return literal.tags.includes(ElementTags.FRITEKST);
}

export function isEmptyContent(content: Content) {
  switch (content.type) {
    case VARIABLE:
    case NEW_LINE:
    case LITERAL: {
      return text(content).trim().replaceAll("​", "").length === 0;
    }
    case ITEM_LIST: {
      return content.items.length === 1 && isEmptyItem(content.items[0]);
    }
    case TABLE: {
      // A table counts as “non-empty” if it has at least one row / cell.
      return content.rows.length === 0;
    }
  }
}

export function isEmptyContentGroup(group: ContentGroup) {
  return group.content.length === 1 && isEmptyContent(group.content[0]);
}

export function isEmptyItem(item: Item): boolean {
  return item.content.length === 0 || (item.content.length === 1 && !!isEmptyContent(item.content[0]));
}

export function isEmptyContentList(content: Content[] | undefined | null): boolean {
  if (!Array.isArray(content)) return true;
  return content.length === 0 || (content.length === 1 && !!isEmptyContent(content[0]));
}

export function isEmptyBlock(block: AnyBlock): boolean {
  if ("content" in block && Array.isArray(block.content)) {
    return isEmptyContentList(block.content);
  }
  /* TABLE (and any future block with rows/cells) is never “empty”
     for the purposes of split/merge. */
  return false;
}

export function isParagraph(block: AnyBlock | undefined | null): block is ParagraphBlock {
  return block?.type === PARAGRAPH;
}
