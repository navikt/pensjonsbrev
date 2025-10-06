import { type Action, withPatches } from "~/Brevredigering/LetterEditor/lib/actions";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";

export const updateSignatur: Action<LetterEditorState, [of: "Saksbehandler" | "Attestant", signatur: string]> =
  withPatches((draft, of, signatur) => {
    if (of === "Saksbehandler") {
      draft.redigertBrev.signatur.saksbehandlerNavn = signatur;
      draft.saveStatus = "DIRTY";
    } else if (of === "Attestant") {
      draft.redigertBrev.signatur.attesterendeSaksbehandlerNavn = signatur;
      draft.saveStatus = "DIRTY";
    }
  });
