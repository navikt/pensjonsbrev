import { TextField } from "@navikt/ds-react";
import { type ChangeEvent, useCallback } from "react";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { applyAction } from "~/Brevredigering/LetterEditor/lib/actions";
import { useManagedLetterEditorContext } from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";
import { isLetterDocument } from "~/types/brevbakerTypes";

export const UnderskriftTextField = ({
  of,
  error: externalError,
  controlled = false,
}: {
  of: "Saksbehandler" | "Attestant";
  error?: string;
  controlled?: boolean;
}) => {
  const { editorState, setEditorState } = useManagedLetterEditorContext();

  // The underskrift field is only rendered for a letter, never a vedlegg. Read signatur only when
  // the document is a letter (a vedlegg has none); the field is not rendered otherwise.
  const signatur = isLetterDocument(editorState.redigertBrev) ? editorState.redigertBrev.signatur : undefined;
  const value = signatur
    ? of === "Saksbehandler"
      ? signatur.saksbehandlerNavn
      : signatur.attesterendeSaksbehandlerNavn
    : undefined;
  const update = useCallback(
    (e: ChangeEvent<HTMLInputElement>) => applyAction(Actions.updateSignatur, setEditorState, of, e.target.value),
    [of, setEditorState],
  );

  const onFocus = useCallback(
    () =>
      applyAction(Actions.updateFocus, setEditorState, {
        blockIndex: -1,
        contentIndex: -1,
      }),
    [setEditorState],
  );

  const error = controlled ? externalError : (value?.length ?? 0) > 0 ? undefined : "Underskrift må oppgis";

  return (
    <TextField
      autoComplete="on"
      error={error}
      label="Underskrift"
      onChange={update}
      onFocus={onFocus}
      size="small"
      value={value ?? ""}
    />
  );
};
