import { css } from "@emotion/react";
import { Button } from "@navikt/ds-react";
import type { ReactNode } from "react";

import Actions from "~/pages/Brevredigering/LetterEditor/actions";
import { useEditor } from "~/pages/Brevredigering/LetterEditor/LetterEditor";

import { applyAction } from "../lib/actions";

export const EditorMenu = () => {
  const { editorState, setEditorState } = useEditor();
  const activeTypography = editorState.editedLetter.letter.blocks[editorState.focus.blockIndex].type;

  return (
    <div
      css={css`
        border-bottom: 1px solid var(--a-border-divider);
        background: var(--a-blue-50);
        padding: var(--a-spacing-3) var(--a-spacing-4);
        display: flex;
        gap: var(--a-spacing-2);
        align-self: stretch;
      `}
    >
      <SelectTypographyButton
        isActive={activeTypography === "TITLE1"}
        onClick={() => applyAction(Actions.switchTypography, setEditorState, editorState.focus.blockIndex, "TITLE1")}
      >
        Overskift 1
      </SelectTypographyButton>
      <SelectTypographyButton
        isActive={activeTypography === "TITLE2"}
        onClick={() => applyAction(Actions.switchTypography, setEditorState, editorState.focus.blockIndex, "TITLE2")}
      >
        Overskrift 2
      </SelectTypographyButton>
      <SelectTypographyButton
        isActive={activeTypography === "PARAGRAPH"}
        onClick={() => applyAction(Actions.switchTypography, setEditorState, editorState.focus.blockIndex, "PARAGRAPH")}
      >
        Normal
      </SelectTypographyButton>
    </div>
  );
};

function SelectTypographyButton({
  isActive,
  children,
  onClick,
}: {
  isActive: boolean;
  children: ReactNode;
  onClick: () => void;
}) {
  return (
    <Button
      css={
        isActive &&
        css`
          color: var(--a-text-on-action);
          background-color: var(--a-surface-action-active);
        `
      }
      disabled={isActive}
      // Use mouseDown instead of onClick to prevent the cursor from losing focus
      onMouseDown={(event) => {
        event.preventDefault();
        onClick();
      }}
      size="xsmall"
      type="button"
      variant="secondary-neutral"
    >
      {children}
    </Button>
  );
}
