import React from "react";

import type { LiteralIndex } from "~/Brevredigering/LetterEditor/model/state";
import type { LiteralValue } from "~/types/brevbakerTypes";

import { EditableText } from "../LetterEditor/components/ContentGroup";

export const TableCellContent: React.FC<{
  lit: LiteralValue;
  litIndex: LiteralIndex;
}> = ({ lit, litIndex }) => <EditableText content={lit} literalIndex={litIndex} />;
