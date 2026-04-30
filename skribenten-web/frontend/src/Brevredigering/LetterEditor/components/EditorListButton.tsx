import { BulletListIcon, NumberListIcon } from "@navikt/aksel-icons";
import { Button, Tooltip } from "@navikt/ds-react";

import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { ListType } from "~/types/brevbakerTypes";

import Actions from "../actions";
import { applyAction } from "../lib/actions";
import { getCursorOffset } from "../services/caretUtils";
import { tooltipText } from "../utils";

type EditorListButtonProps = {
  listType: ListType;
};

const EditorListButton = ({ listType }: EditorListButtonProps) => {
  const { editorState, freeze, setEditorState } = useEditor();
  const currentContent =
    editorState.redigertBrev.blocks[editorState.focus.blockIndex]?.content[editorState.focus.contentIndex];
  const focusedMatchesListType = currentContent?.type === "ITEM_LIST" && currentContent.listType === listType;

  const action = listType === ListType.NUMMERERT_LISTE ? Actions.toggleNumberList : Actions.toggleBulletList;
  const icon =
    listType === ListType.NUMMERERT_LISTE ? (
      <NumberListIcon fontSize="1.5rem" title="nummerert-liste-ikon" />
    ) : (
      <BulletListIcon fontSize="1.5rem" title="punktliste-ikon" />
    );
  const dataCy = listType === ListType.NUMMERERT_LISTE ? "editor-number-list" : "editor-bullet-list";
  const tooltip = listType === ListType.NUMMERERT_LISTE ? tooltipText.numberList : tooltipText.bulletList;

  return (
    <Tooltip content={tooltip}>
      <Button
        color="text-neutral"
        data-cy={dataCy}
        disabled={freeze || editorState.focus.blockIndex < 0}
        icon={icon}
        onClick={() => {
          applyAction(action, setEditorState, editorState.focus);
          //setter fokuset tilbake til editor etter valg
          applyAction(Actions.cursorPosition, setEditorState, getCursorOffset());
        }}
        size="small"
        type="button"
        variant={focusedMatchesListType ? "primary-neutral" : "tertiary-neutral"}
      />
    </Tooltip>
  );
};

export default EditorListButton;
