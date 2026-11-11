import { type ListBlockV2, type TextBlockV2 } from "~/types/brevV2";

import { isFromTemplate } from "./collection";

export function changeBlockType<B extends TextBlockV2 | ListBlockV2>(block: B, type: B["type"]): void {
  if (block.type === type) return;
  if (isFromTemplate(block) && !block.originalType) {
    block.originalType = block.type;
  }
  block.type = type;
  if (block.originalType === type) {
    block.originalType = null;
  }
}
