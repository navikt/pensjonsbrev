import { type Action, withPatches } from "~/Brevredigering/LetterEditor/lib/actions";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";

import { updateLiteralText } from "./updateContentText";

export const updateTitle: Action<LetterEditorState, [title: string]> = withPatches((draft, title) => {
  if (draft.redigertBrev.title.text[0].type === "LITERAL") {
    // draft.redigertBrev.title.text[0].editedText = title;
    updateLiteralText(draft.redigertBrev.title.text[0], title);
    draft.saveStatus = "DIRTY";
  }
});
