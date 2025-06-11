import { css } from "@emotion/react";
import { Button, Label } from "@navikt/ds-react";
import type { ReactNode } from "react";
import React from "react";

import { fontTypeOf, isItemContentIndex } from "~/Brevredigering/LetterEditor/actions/common";
import { isItemList, isTextContent } from "~/Brevredigering/LetterEditor/model/utils";
import { FontType, TABLE } from "~/types/brevbakerTypes";

import Actions from "../actions";
import type { CallbackReceiver } from "../lib/actions";
import { applyAction } from "../lib/actions";
import type { LetterEditorState } from "../model/state";

const getCurrentActiveFontTypeAtCursor = (editorState: LetterEditorState): FontType => {
  const block = editorState.redigertBrev.blocks[editorState.focus.blockIndex];
  const cur = editorState.focus;
  if (block.type === TABLE) {
    if (isItemContentIndex(cur) && block.rows[cur.contentIndex] && block.rows[cur.contentIndex].cells[cur.itemIndex]) {
      const cell = block.rows[cur.contentIndex].cells[cur.itemIndex];
      const literal = cell.text[cur.itemContentIndex];
      return isTextContent(literal) ? fontTypeOf(literal) : FontType.PLAIN;
    }
    return FontType.PLAIN;
  }
  const blockContent = block.content[editorState.focus.contentIndex];
  const textContent =
    isItemContentIndex(editorState.focus) && isItemList(blockContent)
      ? blockContent.items[editorState.focus.itemIndex]?.content[editorState.focus.itemContentIndex]
      : blockContent;

  if (isTextContent(textContent)) {
    return fontTypeOf(textContent);
  } else {
    return FontType.PLAIN;
  }
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
  text: ReactNode;
  disabled?: boolean;
  dataCy: string;
}) => {
  return (
    <Button
      css={css`
        color: var(--a-text-default);
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
      variant="tertiary"
    >
      {props.text}
    </Button>
  );
};
