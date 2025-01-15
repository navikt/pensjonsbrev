import type { Draft } from "immer";

import { text } from "~/Brevredigering/LetterEditor/actions/common";
import type {
  AnyBlock,
  Content,
  Identifiable,
  Item,
  ItemList,
  LiteralValue,
  TextContent,
  VariableValue,
} from "~/types/brevbakerTypes";
import { ElementTags, ITEM_LIST, LITERAL, NEW_LINE, VARIABLE } from "~/types/brevbakerTypes";

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
  }
}

export function isEmptyContentGroup(group: ContentGroup) {
  return group.content.length === 1 && isEmptyContent(group.content[0]);
}

export function isEmptyItem(item: Item): boolean {
  return item.content.length === 0 || (item.content.length === 1 && isEmptyContent(item.content[0]));
}

export function isEmptyContentList(content: Content[]) {
  return content.length === 0 || (content.length === 1 && isEmptyContent(content[0]));
}
export function isEmptyBlock(block: AnyBlock): boolean {
  return isEmptyContentList(block.content);
}
