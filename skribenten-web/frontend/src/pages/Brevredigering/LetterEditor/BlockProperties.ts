import type { AnyBlock } from "~/types/brevbakerTypes";

import type { BoundAction, CallbackReceiver } from "./lib/actions";
import type { CursorPosition, LetterEditorState } from "./model/state";

export type BlockProperties<T> = {
  block: T & AnyBlock;
  blockId: number;
  updateLetter: CallbackReceiver<LetterEditorState>;
  stealFocus: CursorPosition | undefined;
  focusStolen: BoundAction<[]>;
  onFocus: BoundAction<[]>;
};
