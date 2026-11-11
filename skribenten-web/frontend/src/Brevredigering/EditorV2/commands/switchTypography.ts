import { type TextBlockTypeV2 } from "~/types/brevV2";

import { changeBlockType } from "../model/blockType";
import { isTextBlock } from "../model/paths";
import { type Command } from "../state/editorState";

export const switchTypography: Command<[type: TextBlockTypeV2]> = (draft, type) => {
  const path = draft.focus.path;
  if (path.kind !== "block") return;
  const block = draft.redigertBrev.blocks[path.block];
  if (isTextBlock(block)) changeBlockType(block, type);
};
