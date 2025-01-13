import { css } from "@emotion/react";
import { BulletListIcon } from "@navikt/aksel-icons";
import { Button } from "@navikt/ds-react";

import Actions from "../actions";
import type { CallbackReceiver } from "../lib/actions";
import { applyAction } from "../lib/actions";
import type { LetterEditorState } from "../model/state";
import { getCursorOffset } from "../services/caretUtils";

const EditorBulletList = (props: {
  editorState: LetterEditorState;
  setEditorState: CallbackReceiver<LetterEditorState>;
}) => {
  //TODO - bug - om du bare taster i vei i tastaturet mens du lager nye avsnitt, og trykker fortsett før lagring, vil focus være på et avsnitt som ikke eksister i blocks
  //dette fører til en error
  const block = props.editorState.redigertBrev.blocks[props.editorState.focus.blockIndex];
  const erAlleElementerIBlockenItemList = block.content.every((contentItem) => contentItem.type === "ITEM_LIST");

  return (
    <div>
      <Button
        css={css`
          width: 32px;
          height: 32px;

          /*
            TODO - style at knappen er aktiv etter ønsket design.
          */
          ${erAlleElementerIBlockenItemList &&
          css`
            background-color: #ccc;
            color: #000;
            border-color: #999;
            box-shadow: inset 0 0 5px rgb(0 0 0 / 20%);
          `}
        `}
        data-cy="editor-bullet-list"
        icon={<BulletListIcon fontSize="1.5rem" title="punktliste-ikon" />}
        onClick={() => {
          applyAction(Actions.toggleBulletList, props.setEditorState, props.editorState.focus);
          //setter fokuset tilbake til editor etter valg
          applyAction(Actions.cursorPosition, props.setEditorState, getCursorOffset());
        }}
        size="small"
        type="button"
        variant="tertiary-neutral"
      />
    </div>
  );
};

export default EditorBulletList;
