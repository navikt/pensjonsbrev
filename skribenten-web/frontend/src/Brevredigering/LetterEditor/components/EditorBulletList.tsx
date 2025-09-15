import { css } from "@emotion/react";
import { BulletListIcon } from "@navikt/aksel-icons";
import { Button } from "@navikt/ds-react";

import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";

import Actions from "../actions";
import { applyAction } from "../lib/actions";
import { getCursorOffset } from "../services/caretUtils";

const EditorBulletList = () => {
  const { editorState, freeze, setEditorState } = useEditor();
  // Alexander: litt usikker på hva som menes med todo under her.
  //TODO - bug - om du bare taster i vei i tastaturet mens du lager nye avsnitt, og trykker fortsett før lagring, vil focus være på et avsnitt som ikke eksister i blocks
  //dette fører til en error
  const block = editorState.redigertBrev.blocks[editorState.focus.blockIndex];
  const erAlleElementerIBlockenItemList =
    block?.content.every((contentItem) => contentItem.type === "ITEM_LIST") ?? true;

  return (
    <Button
      css={css`
        width: 32px;
        height: 32px;

        ${erAlleElementerIBlockenItemList &&
        css`
          background-color: #23262a;
          color: #fff;
          border-color: #999;
          box-shadow: inset 0 0 5px rgb(0 0 0 / 20%);
        `}
      `}
      data-cy="editor-bullet-list"
      disabled={freeze}
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
