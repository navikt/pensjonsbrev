import React from "react";

import { EditableText } from "~/Brevredigering/LetterEditor/components/ContentGroup";
import { Text } from "~/Brevredigering/LetterEditor/components/Text";
import type { LiteralValue, TextContent } from "~/types/brevbakerTypes";
import { LITERAL, NEW_LINE, VARIABLE } from "~/types/brevbakerTypes";

import type { TableCellIndex } from "../model/state";

type TableCellContentProps = {
  content: TextContent;
  tableCellIndex: TableCellIndex;
};

export function TableCellContent({ content, tableCellIndex }: TableCellContentProps) {
  switch (content.type) {
    case LITERAL:
      return <EditableText content={content as LiteralValue} literalIndex={tableCellIndex} />;

    case VARIABLE:
      return <Text content={content} literalIndex={tableCellIndex} />;

    case NEW_LINE:
      return <br />;
  }
}
