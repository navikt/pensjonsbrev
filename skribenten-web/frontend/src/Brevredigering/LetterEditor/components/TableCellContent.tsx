import {DiffText, EditableText} from "~/Brevredigering/LetterEditor/components/ContentGroup";
import { Text } from "~/Brevredigering/LetterEditor/components/Text";
import { LITERAL, type LiteralValue, NEW_LINE, type TextContent, VARIABLE } from "~/types/brevbakerTypes";

import { type TableCellIndex } from "../model/state";
import {useEditor} from "~/Brevredigering/LetterEditor/LetterEditor";
import React from "react";

type TableCellContentProps = {
  content: TextContent;
  tableCellIndex: TableCellIndex;
};

export function TableCellContent({ content, tableCellIndex }: TableCellContentProps) {
  const {showDiff} = useEditor();
  switch (content.type) {
    case LITERAL:
      if (showDiff) {
        return <DiffText content={content} literalIndex={tableCellIndex} />;
      } else {
        return <EditableText content={content} literalIndex={tableCellIndex} />;
      }

    case VARIABLE:
      return <Text content={content} literalIndex={tableCellIndex} />;

    case NEW_LINE:
      return <br />;
  }
}
