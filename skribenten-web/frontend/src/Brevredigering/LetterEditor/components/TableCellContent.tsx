import React from "react";

import { EditableText } from "~/Brevredigering/LetterEditor/components/ContentGroup"; // if EditableText is exported there
import { Text } from "~/Brevredigering/LetterEditor/components/Text";
import type { LiteralValue, TextContent } from "~/types/brevbakerTypes";
import { LITERAL, NEW_LINE, VARIABLE } from "~/types/brevbakerTypes";

import type { LiteralIndex, TableCellIndex } from "../model/state";

type TableCellContentProps = {
  content: TextContent;
  tableCellIndex: TableCellIndex;
};

// we’re importing EditableText from ContentGroup. Because ContentGroup renders TableView, and TableView renders TableCellContent, this creates a circular dependency chain.
// ContentGroup -> TableView -> TableCellContent -> ContentGroup, if bundler can't handle it and
// EditableText is undefined at runtime, move EditableText into its own module (e.g. components/EditableText.tsx).

export function TableCellContent({ content, tableCellIndex }: TableCellContentProps) {
  switch (content.type) {
    case LITERAL: {
      // Keyboard/caret logic that still depends on itemIndex/itemContentIndex
      // will continue to work while we also keep rowIndex/cellIndex/cellContentIndex for future use.
      const litIndex: LiteralIndex = {
        blockIndex: tableCellIndex.blockIndex,
        contentIndex: tableCellIndex.contentIndex,
        itemIndex: tableCellIndex.rowIndex,
        itemContentIndex: tableCellIndex.cellIndex,
        rowIndex: tableCellIndex.rowIndex,
        cellIndex: tableCellIndex.cellIndex,
        cellContentIndex: tableCellIndex.cellContentIndex,
      };

      return <EditableText content={content as LiteralValue} literalIndex={litIndex} />;
    }

    case VARIABLE: {
      return (
        <Text
          content={content}
          literalIndex={{
            blockIndex: tableCellIndex.blockIndex,
            contentIndex: tableCellIndex.contentIndex,
          }}
        />
      );
    }

    case NEW_LINE: {
      return <br />;
    }
  }
}
