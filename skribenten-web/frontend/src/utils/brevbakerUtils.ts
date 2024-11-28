import type { Draft } from "immer";

import type { BlockContentIndex, ItemContentIndex, LiteralIndex } from "~/Brevredigering/LetterEditor/actions/model";
import type { Focus } from "~/Brevredigering/LetterEditor/model/state";
import type { Content, ItemList, LiteralValue, TextContent, VariableValue } from "~/types/brevbakerTypes";

export const handleSwitchContent = <T, U, V>(args: {
  content: Content;
  onLiteral: (literal: Draft<LiteralValue>) => T;
  onVariable: (variable: Draft<VariableValue>) => U;
  onItemList: (itemList: Draft<ItemList>) => V;
}) => {
  switch (args.content.type) {
    case "LITERAL": {
      return args.onLiteral(args.content);
    }
    case "VARIABLE": {
      return args.onVariable(args.content);
    }
    case "ITEM_LIST": {
      return args.onItemList(args.content);
    }
  }
};

export const handleSwitchTextContent = <T>(args: {
  content: TextContent;
  onLiteral: (literal: Draft<LiteralValue>) => T;
  onVariable: (variable: Draft<VariableValue>) => T;
}) => {
  switch (args.content.type) {
    case "LITERAL": {
      return args.onLiteral(args.content);
    }
    case "VARIABLE": {
      return args.onVariable(args.content);
    }
  }
};

export const isBlockContentIndex = (f: Focus | LiteralIndex): f is BlockContentIndex => !isItemContentIndex(f);

export const isItemContentIndex = (f: Focus | LiteralIndex): f is ItemContentIndex =>
  "itemIndex" in f && "itemContentIndex" in f;
