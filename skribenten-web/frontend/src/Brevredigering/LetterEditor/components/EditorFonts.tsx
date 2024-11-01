import { css } from "@emotion/react";
import { Button, Label } from "@navikt/ds-react";

import { FontType } from "~/types/brevbakerTypes";
import { getLiteralEditedFontTypeForBlock } from "~/utils/brevbakerUtils";

import Actions from "../actions";
import type { CallbackReceiver } from "../lib/actions";
import { applyAction } from "../lib/actions";
import type { LetterEditorState } from "../model/state";
import { getCursorOffset } from "../services/caretUtils";

const EditorFonts = (props: {
  editorState: LetterEditorState;
  setEditorState: CallbackReceiver<LetterEditorState>;
}) => {
  const activeFontType = getLiteralEditedFontTypeForBlock(props.editorState);

  return (
    <div>
      <FontButton
        active={activeFontType === FontType.BOLD}
        dataCy="fonttype-bold"
        onClick={() => {
          if (activeFontType === FontType.BOLD) {
            applyAction(Actions.switchFontType, props.setEditorState, props.editorState.focus, FontType.PLAIN);
          } else {
            applyAction(Actions.switchFontType, props.setEditorState, props.editorState.focus, FontType.BOLD);
          }

          //setter fokuset tilbake til editor etter valgt fonttype
          applyAction(Actions.cursorPosition, props.setEditorState, getCursorOffset());
        }}
        text={<Label>F</Label>}
      />
      <FontButton
        active={activeFontType === FontType.ITALIC}
        dataCy="fonttype-italic"
        onClick={() => {
          if (activeFontType === FontType.ITALIC) {
            applyAction(Actions.switchFontType, props.setEditorState, props.editorState.focus, FontType.PLAIN);
          } else {
            applyAction(Actions.switchFontType, props.setEditorState, props.editorState.focus, FontType.ITALIC);
          }

          //setter fokuset tilbake til editor etter valgt fonttype
          applyAction(Actions.cursorPosition, props.setEditorState, getCursorOffset());
        }}
        text={
          <Label
            css={css`
              font-style: italic;
            `}
          >
            K
          </Label>
        }
      />
    </div>
  );
};

export default EditorFonts;

const FontButton = (props: {
  active: boolean;
  onClick: () => void;
  text: React.ReactNode;
  disabled?: boolean;
  dataCy: string;
}) => {
  return (
    <Button
      css={css`
        color: var(--a-text-default);
        width: 32px;
        height: 32px;

        &:hover {
          color: var(--a-text-default);
        }

        /*
            TODO - style at knappen er aktiv etter ønsket design.
          */
        ${props.active &&
        css`
          background-color: #ccc;
          color: #000;
          border-color: #999;
          box-shadow: inset 0 0 5px rgb(0 0 0 / 20%);
        `}
      `}
      data-cy={props.dataCy}
      disabled={props.disabled}
      onClick={props.onClick}
      size="small"
      type="button"
      variant="tertiary"
    >
      {props.text}
    </Button>
  );
};
