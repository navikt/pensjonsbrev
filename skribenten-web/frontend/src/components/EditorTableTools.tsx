import { TableIcon } from "@navikt/aksel-icons";
import { Button, HStack } from "@navikt/ds-react";
import { useState } from "react";

import Actions from "~/Brevredigering/LetterEditor/actions";
import InsertTableDialog from "~/Brevredigering/LetterEditor/components/InsertTableDialog";
import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { applyAction } from "~/Brevredigering/LetterEditor/lib/actions";
import type { Focus } from "~/Brevredigering/LetterEditor/model/state";

const EditorTableTools = () => {
  const { editorState, setEditorState } = useEditor();
  const [isInsertTableDialogOpen, setIsInsertTableDialogOpen] = useState(false);
  const [focusAtOpen, setFocusAtOpen] = useState<Focus | null>(null);

  return (
    <>
      <HStack gap="2">
        <Button
          data-cy="toolbar-table-btn"
          icon={<TableIcon fontSize="1.5rem" title="Sett inn tabell" />}
          onClick={() => {
            setFocusAtOpen(editorState.focus);
            setIsInsertTableDialogOpen(true);
          }}
          size="small"
          type="button"
          variant="tertiary-neutral"
        />
      </HStack>

      <InsertTableDialog
        onCancel={() => setIsInsertTableDialogOpen(false)}
        onInsert={(columnCount, rowCount) => {
          const focus = focusAtOpen ?? editorState.focus;
          applyAction(Actions.insertTable, setEditorState, focus, rowCount, columnCount);
          setIsInsertTableDialogOpen(false);
        }}
        open={isInsertTableDialogOpen}
      />
    </>
  );
};

export default EditorTableTools;
