import { css } from "@emotion/react";
import { Button, Label } from "@navikt/ds-react";
import type { ReactNode } from "react";

import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { FontType } from "~/types/brevbakerTypes";

import Actions from "../actions";
import { getCurrentActiveFontTypeAtCursor } from "../actions/switchFontType";
import { applyAction } from "../lib/actions";

const EditorFonts = () => {
  const { editorState, freeze, setEditorState } = useEditor();
  const activeFontType = getCurrentActiveFontTypeAtCursor(editorState);

  return (
    <>
      <FontButton
        active={activeFontType === FontType.BOLD}
        dataCy="fonttype-bold"
        disabled={freeze || editorState.focus.blockIndex < 0}
        onClick={() => {
          applyAction(Actions.switchFontType, setEditorState, FontType.BOLD);
        }}
        text={<Label>F</Label>}
      />
      <FontButton
        active={activeFontType === FontType.ITALIC}
        dataCy="fonttype-italic"
        disabled={freeze || editorState.focus.blockIndex < 0}
        onClick={() => {
          applyAction(Actions.switchFontType, setEditorState, FontType.ITALIC);
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
    </>
  );
};

export default EditorFonts;

const FontButton = (props: {
  active: boolean;
  onClick: () => void;
  text: ReactNode;
  disabled?: boolean;
  dataCy: string;
}) => {
  return (
    <Button
      css={css`
        color: var(--ax-text-neutral);
        width: 32px;
        height: 32px;

        ${props.active &&
        css`
          background-color: #23262a;
          color: #fff;
          border-color: #999;
          box-shadow: inset 0 0 5px rgb(0 0 0 / 20%);
        `}
      `}
      data-cy={props.dataCy}
      disabled={props.disabled}
      onClick={props.onClick}
      size="small"
      type="button"
      variant="tertiary-neutral"
    >
      {props.text}
    </Button>
  );
};
