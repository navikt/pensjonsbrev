import { produce } from "immer";

import type { Action } from "~/Brevredigering/LetterEditor/lib/actions";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";

export const updateSaksbehandlerSignatur: Action<LetterEditorState, [signatur: string]> = produce((draft, signatur) => {
  draft.redigertBrev.signatur.saksbehandlerNavn = signatur;
  draft.isDirty = true;
});
