import type { Draft } from "immer";

import type { Content, ItemList, LiteralValue, NewLine, TextContent, VariableValue } from "~/types/brevbakerTypes";

export const handleSwitchContent = <T, U, V, W>(args: {
  content: Content;
  onLiteral: (literal: Draft<LiteralValue>) => T;
  onVariable: (variable: Draft<VariableValue>) => U;
  onItemList: (itemList: Draft<ItemList>) => V;
  onNewLine: (newLine: Draft<NewLine>) => W;
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
    case "NEW_LINE": {
      return args.onNewLine(args.content);
    }
    case "TABLE": {
      // TABLE content is handled elsewhere (e.g. see switchFontType for direct table handling)
      return undefined;
    }
  }
};

export const handleSwitchTextContent = <T>(args: {
  content: TextContent;
  onLiteral: (literal: Draft<LiteralValue>) => T;
  onVariable: (variable: Draft<VariableValue>) => T;
  onNewLine: (newLine: Draft<NewLine>) => T;
}) => {
  switch (args.content.type) {
    case "LITERAL": {
      return args.onLiteral(args.content);
    }
    case "VARIABLE": {
      return args.onVariable(args.content);
    }
    case "NEW_LINE": {
      return args.onNewLine(args.content);
    }
  }
};
