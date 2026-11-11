import { type LetterCommand } from "../state/editorState";

export const updateSignatur: LetterCommand<[of: "Saksbehandler" | "Attestant", signatur: string]> = (
  draft,
  of,
  signatur,
) => {
  switch (of) {
    case "Saksbehandler":
      draft.redigertBrev.signatur.saksbehandlerNavn = signatur;
      return;
    case "Attestant":
      draft.redigertBrev.signatur.attesterendeSaksbehandlerNavn = signatur;
      return;
  }
};
