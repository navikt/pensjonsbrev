import { TextField } from "@navikt/ds-react";
import { type ChangeEvent, useCallback } from "react";

import { useManagedLetterEditorContextV2 } from "./ManagedLetterEditorContextV2";

export const UnderskriftTextFieldV2 = ({
  of,
  error: externalError,
  controlled = false,
}: {
  of: "Saksbehandler" | "Attestant";
  error?: string;
  controlled?: boolean;
}) => {
  const { editorState, setEditorState } = useManagedLetterEditorContextV2();

  const signatur = editorState.redigertBrev.signatur;
  const value = of === "Saksbehandler" ? signatur.saksbehandlerNavn : signatur.attesterendeSaksbehandlerNavn;
  const update = useCallback(
    (e: ChangeEvent<HTMLInputElement>) => {
      const navn = e.target.value;
      setEditorState((state) => ({
        ...state,
        saveStatus: "DIRTY",
        redigertBrev: {
          ...state.redigertBrev,
          signatur:
            of === "Saksbehandler"
              ? { ...state.redigertBrev.signatur, saksbehandlerNavn: navn }
              : { ...state.redigertBrev.signatur, attesterendeSaksbehandlerNavn: navn },
        },
      }));
    },
    [of, setEditorState],
  );

  const error = controlled ? externalError : (value?.length ?? 0) > 0 ? undefined : "Underskrift må oppgis";

  return (
    <TextField autoComplete="on" error={error} label="Underskrift" onChange={update} size="small" value={value ?? ""} />
  );
};
