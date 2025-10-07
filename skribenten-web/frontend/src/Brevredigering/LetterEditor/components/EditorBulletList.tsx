import { css } from "@emotion/react";
import { BulletListIcon } from "@navikt/aksel-icons";
import { Button } from "@navikt/ds-react";

import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";

import Actions from "../actions";
import { applyAction } from "../lib/actions";
import { getCursorOffset } from "../services/caretUtils";

const EditorBulletList = () => {
  const { editorState, freeze, setEditorState } = useEditor();
  const focusedIsItemList =
    editorState.redigertBrev.blocks[editorState.focus.blockIndex]?.content[editorState.focus.contentIndex]?.type ===
    "ITEM_LIST";

  return (
    <Button
      css={css`
        width: 32px;
        height: 32px;

        ${focusedIsItemList &&
        css`
          background-color: #23262a;
          color: #fff;
          border-color: #999;
          box-shadow: inset 0 0 5px rgb(0 0 0 / 20%);
        `}
      `}
      data-cy="editor-bullet-list"
      disabled={freeze || editorState.focus.blockIndex < 0}
      icon={<BulletListIcon fontSize="1.5rem" title="punktliste-ikon" />}
      onClick={() => {
        applyAction(Actions.toggleBulletList, setEditorState, editorState.focus);
        //setter fokuset tilbake til editor etter valg
        applyAction(Actions.cursorPosition, setEditorState, getCursorOffset());
      }}
      size="small"
      type="button"
      variant="tertiary-neutral"
    />
  );
};

export default EditorBulletList;
