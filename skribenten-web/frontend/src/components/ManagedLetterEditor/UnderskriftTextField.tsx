import { TextField } from "@navikt/ds-react";
import type { ChangeEvent } from "react";
import { useCallback } from "react";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { applyAction } from "~/Brevredigering/LetterEditor/lib/actions";
import { useManagedLetterEditorContext } from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";

export const UnderskriftTextField = ({ of }: { of: "Saksbehandler" | "Attestant" }) => {
  const { editorState, setEditorState } = useManagedLetterEditorContext();

  const signatur = editorState.redigertBrev.signatur;
  const value = of === "Saksbehandler" ? signatur.saksbehandlerNavn : signatur.attesterendeSaksbehandlerNavn;
  const update = useCallback(
    (e: ChangeEvent<HTMLInputElement>) => applyAction(Actions.updateSignatur, setEditorState, of, e.target.value),
    [of, setEditorState],
  );

  const onFocus = useCallback(
    () => applyAction(Actions.updateFocus, setEditorState, { blockIndex: -1, contentIndex: -1 }),
    [setEditorState],
  );

  return (
    <TextField
      autoComplete="on"
      error={(value?.length ?? 0) > 0 ? undefined : "Underskrift må oppgis"}
      label="Underskrift"
      onChange={update}
      onFocus={onFocus}
      size="small"
      value={value ?? ""}
    />
  );
};
