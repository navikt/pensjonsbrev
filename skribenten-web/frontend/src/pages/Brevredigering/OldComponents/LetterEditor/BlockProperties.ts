import type { AnyBlock } from "~/types/brevbakerTypes";

import type { BoundAction, CallbackReceiver } from "./lib/actions";
import type { CursorPosition, LetterEditorState } from "./model/state";

export interface BlockProperties<T extends AnyBlock> {
  block: T;
  blockId: number;
  updateLetter: CallbackReceiver<LetterEditorState>;
  blockStealFocus: CursorPosition | undefined;
  blockFocusStolen: BoundAction<[]>;
  onFocus: BoundAction<[]>;
}
