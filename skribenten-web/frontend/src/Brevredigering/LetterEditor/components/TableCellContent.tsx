import React from "react";

import { EditableText } from "~/Brevredigering/LetterEditor/components/ContentGroup";
import { Text } from "~/Brevredigering/LetterEditor/components/Text";
import type { LiteralValue, TextContent } from "~/types/brevbakerTypes";
import { LITERAL, NEW_LINE, VARIABLE } from "~/types/brevbakerTypes";

import type { LiteralIndex, TableCellIndex } from "../model/state";

type TableCellContentProps = {
  content: TextContent;
  tableCellIndex: TableCellIndex;
};

export function TableCellContent({ content, tableCellIndex }: TableCellContentProps) {
  const literalIndex: LiteralIndex = {
    blockIndex: tableCellIndex.blockIndex,
    contentIndex: tableCellIndex.contentIndex,
    rowIndex: tableCellIndex.rowIndex,
    cellIndex: tableCellIndex.cellIndex,
    cellContentIndex: tableCellIndex.cellContentIndex,
  };

  switch (content.type) {
    case LITERAL:
      return <EditableText content={content as LiteralValue} literalIndex={literalIndex} />;

    case VARIABLE:
      return <Text content={content} literalIndex={literalIndex} />;

    case NEW_LINE:
      return <br />;
  }
}
