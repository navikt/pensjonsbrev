import { TableIcon } from "@navikt/aksel-icons";
import { Button, Dropdown, HStack } from "@navikt/ds-react";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { applyAction } from "~/Brevredigering/LetterEditor/lib/actions";
import { TABLE } from "~/types/brevbakerTypes";

const EditorTableTools = () => {
  const { editorState, setEditorState } = useEditor();
  const { focus } = editorState;

  const currentBlock = editorState.redigertBrev.blocks[focus.blockIndex];
  const currentContent = currentBlock?.type === "PARAGRAPH" ? currentBlock.content[focus.contentIndex] : undefined;

  const isTableSelected = currentContent?.type === TABLE;

  return (
    <HStack gap="2">
      <Button
        icon={<TableIcon fontSize="1.5rem" title="Sett inn 2x2 tabell" />}
        onClick={(e) => {
          e.preventDefault();
          applyAction(Actions.insertTable, setEditorState, editorState.focus, 2, 2);
        }}
        size="small"
        type="button"
        variant="tertiary"
      />

      {isTableSelected && (
        <Dropdown>
          <Dropdown.Toggle type="button">Tabell</Dropdown.Toggle>
          <Dropdown.Menu>
            <Dropdown.Menu.List>
              <Dropdown.Menu.List.Item
                onClick={(e) => {
                  e.preventDefault();
                  applyAction(Actions.addTableRow, setEditorState, focus.blockIndex, focus.contentIndex);
                }}
              >
                Legg til rad
              </Dropdown.Menu.List.Item>
              <Dropdown.Menu.List.Item
                onClick={(e) => {
                  e.preventDefault();
                  applyAction(Actions.addTableColumn, setEditorState, focus.blockIndex, focus.contentIndex);
                }}
              >
                Legg til kolonne
              </Dropdown.Menu.List.Item>
            </Dropdown.Menu.List>
          </Dropdown.Menu>
        </Dropdown>
      )}
    </HStack>
  );
};

export default EditorTableTools;
