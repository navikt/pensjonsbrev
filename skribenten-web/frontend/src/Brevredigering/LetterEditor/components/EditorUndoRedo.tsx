import { ArrowRedoIcon, ArrowUndoIcon } from "@navikt/aksel-icons";
import { Button, HStack, Tooltip } from "@navikt/ds-react";
import type React from "react";

import { tooltipText } from "../utils";

type EditorUndoRedoProps = {
  undo: () => void;
  redo: () => void;
  canUndo: boolean;
  canRedo: boolean;
};

export const EditorUndoRedo: React.FC<EditorUndoRedoProps> = ({ undo, redo, canUndo, canRedo }) => (
  <>
    <HStack gap="space-8">
      <Tooltip content={tooltipText.undo}>
        <Button
          data-color="neutral"
          disabled={!canUndo}
          icon={<ArrowUndoIcon fontSize="1.5rem" title="Angre" />}
          onClick={undo}
          size="small"
          type="button"
          variant="tertiary"
        />
      </Tooltip>
    </HStack>
    <HStack gap="space-8">
      <Tooltip content={tooltipText.redo}>
        <Button
          data-color="neutral"
          disabled={!canRedo}
          icon={<ArrowRedoIcon fontSize="1.5rem" title="Gjør om" />}
          onClick={redo}
          size="small"
          type="button"
          variant="tertiary"
        />
      </Tooltip>
    </HStack>
  </>
);
