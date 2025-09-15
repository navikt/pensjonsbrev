import { ArrowRedoIcon, ArrowUndoIcon } from "@navikt/aksel-icons";
import { Button, HStack } from "@navikt/ds-react";
import React from "react";

type EditorUndoRedoProps = {
  undo: () => void;
  redo: () => void;
  canUndo: boolean;
  canRedo: boolean;
};

export const EditorUndoRedo: React.FC<EditorUndoRedoProps> = ({ undo, redo, canUndo, canRedo }) => (
  <>
    <HStack gap="2">
      <Button
        disabled={!canUndo}
        icon={<ArrowUndoIcon fontSize="1.5rem" title="Angre (Undo)" />}
        onClick={undo}
        size="small"
        title="Angre (Undo)"
        type="button"
        variant="tertiary-neutral"
      ></Button>
    </HStack>
    <HStack gap="2">
      <Button
        disabled={!canRedo}
        icon={<ArrowRedoIcon fontSize="1.5rem" title="Gjør om (Redo)" />}
        onClick={redo}
        size="small"
        title="Gjør om (Redo)"
        type="button"
        variant="tertiary-neutral"
      ></Button>
    </HStack>
  </>
);
