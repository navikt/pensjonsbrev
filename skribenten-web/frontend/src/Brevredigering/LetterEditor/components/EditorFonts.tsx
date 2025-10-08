import { css } from "@emotion/react";
import { Button, Label } from "@navikt/ds-react";
import type { ReactNode } from "react";
import React from "react";

import { fontTypeOf, isItemContentIndex, isTable } from "~/Brevredigering/LetterEditor/actions/common";
import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { isItemList, isTableCellIndex, isTextContent } from "~/Brevredigering/LetterEditor/model/utils";
import type { TextContent } from "~/types/brevbakerTypes";
import { FontType } from "~/types/brevbakerTypes";

import Actions from "../actions";
import { applyAction } from "../lib/actions";
import type { LetterEditorState } from "../model/state";

const getCurrentActiveFontTypeAtCursor = (editorState: LetterEditorState): FontType => {
  const block = editorState.redigertBrev.blocks[editorState.focus.blockIndex];
  const focus = editorState.focus;
  const blockContent = block?.content[editorState.focus.contentIndex];

  let textContent: TextContent | undefined = undefined;

  if (isTable(blockContent) && isTableCellIndex(focus)) {
    const cell =
      focus.rowIndex === -1
        ? blockContent.header.colSpec[focus.cellIndex]?.headerContent
        : blockContent.rows[focus.rowIndex]?.cells[focus.cellIndex];

    textContent = cell?.text?.at(focus.cellContentIndex);
  } else if (isItemList(blockContent) && isItemContentIndex(focus)) {
    textContent = blockContent?.items[focus.itemIndex]?.content[focus.itemContentIndex];
  } else if (isTextContent(blockContent)) {
    textContent = blockContent;
  }

  return isTextContent(textContent) ? fontTypeOf(textContent) : FontType.PLAIN;
};

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
          if (activeFontType === FontType.BOLD) {
            applyAction(Actions.switchFontType, setEditorState, editorState.focus, FontType.PLAIN);
          } else {
            applyAction(Actions.switchFontType, setEditorState, editorState.focus, FontType.BOLD);
          }
        }}
        text={<Label>F</Label>}
      />
      <FontButton
        active={activeFontType === FontType.ITALIC}
        dataCy="fonttype-italic"
        disabled={freeze || editorState.focus.blockIndex < 0}
        onClick={() => {
          if (activeFontType === FontType.ITALIC) {
            applyAction(Actions.switchFontType, setEditorState, editorState.focus, FontType.PLAIN);
          } else {
            applyAction(Actions.switchFontType, setEditorState, editorState.focus, FontType.ITALIC);
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
      variant="tertiary-neutral"
    >
      {props.text}
    </Button>
  );
};
