import { css } from "@emotion/react";
import { Button, Label } from "@navikt/ds-react";

import { FontType } from "~/types/brevbakerTypes";
import { handleSwitchContent, handleSwitchTextContent, isItemContentIndex } from "~/utils/brevbakerUtils";

import Actions from "../actions";
import type { CallbackReceiver } from "../lib/actions";
import { applyAction } from "../lib/actions";
import type { LetterEditorState } from "../model/state";

const getCurrentActiveFontTypeAtCursor = (editorState: LetterEditorState): FontType => {
  const block = editorState.redigertBrev.blocks[editorState.focus.blockIndex];
  const theContentWeAreOn = block.content[editorState.focus.contentIndex];

  return handleSwitchContent({
    content: theContentWeAreOn,
    onLiteral: (literal) => literal.editedFontType ?? literal.fontType,
    onVariable: (variable) => variable.fontType,
    onNewLine: () => FontType.PLAIN,
    onItemList: (itemList) => {
      if (!isItemContentIndex(editorState.focus)) {
        return FontType.PLAIN;
      }
      const item = itemList.items[editorState.focus.itemIndex].content[editorState.focus.itemContentIndex];

      return handleSwitchTextContent({
        content: item,
        onLiteral: (literal) => literal.editedFontType ?? literal.fontType,
        onVariable: (variable) => variable.fontType,
        onNewLine: () => FontType.PLAIN,
      });
    },
  });
};

const EditorFonts = (props: {
  editorState: LetterEditorState;
  setEditorState: CallbackReceiver<LetterEditorState>;
}) => {
  const activeFontType = getCurrentActiveFontTypeAtCursor(props.editorState);

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
