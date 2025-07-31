import { produce } from "immer";

import type { Action } from "~/Brevredigering/LetterEditor/lib/actions";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";

export const updateSignatur: Action<LetterEditorState, [of: "Saksbehandler" | "Attestant", signatur: string]> = produce(
  (draft, of, signatur) => {
    if (of === "Saksbehandler") {
      draft.redigertBrev.signatur.saksbehandlerNavn = signatur;
      draft.isDirty = true;
    } else if (of === "Attestant") {
      draft.redigertBrev.signatur.attesterendeSaksbehandlerNavn = signatur;
      draft.isDirty = true;
    }
  },
);
