import { TextField } from "@navikt/ds-react";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { bindActionWithCallback } from "~/Brevredigering/LetterEditor/lib/actions";
import { useManagedLetterEditorContext } from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";

// TODO: Burde ha et forhold til freeze, men for å oppnå det så må ansvaret for freeze håndteres i ManagedLetterEditorContext.
export const UnderskriftTextField = ({ of }: { of: "Saksbehandler" | "Attestant" }) => {
  const { editorState, setEditorState } = useManagedLetterEditorContext();

  const signatur = editorState.redigertBrev.signatur;
  const value = of === "Saksbehandler" ? signatur.saksbehandlerNavn : signatur.attesterendeSaksbehandlerNavn;
  const update = bindActionWithCallback(Actions.updateSignatur, setEditorState, of);

  return (
    <TextField
      autoComplete={"on"}
      error={(value?.length ?? 0) > 0 ? undefined : "Underskrift må oppgis"}
      label="Underskrift"
      onChange={(e) => update(e.target.value)}
      size="small"
      value={value ?? ""}
    />
  );
};
