import { css } from "@emotion/react";
import { Button } from "@navikt/ds-react";
import type { ReactNode } from "react";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";

import { applyAction } from "../lib/actions";

export const EditorMenu = () => {
  const { editorState, setEditorState } = useEditor();
  const activeTypography = editorState.renderedLetter.editedLetter.blocks[editorState.focus.blockIndex].type;

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
        dataCy="TITLE1-BUTTON"
        isActive={activeTypography === "TITLE1"}
        onClick={() => applyAction(Actions.switchTypography, setEditorState, editorState.focus.blockIndex, "TITLE1")}
      >
        Overskrift 1
      </SelectTypographyButton>
      <SelectTypographyButton
        dataCy="TITLE2-BUTTON"
        isActive={activeTypography === "TITLE2"}
        onClick={() => applyAction(Actions.switchTypography, setEditorState, editorState.focus.blockIndex, "TITLE2")}
      >
        Overskrift 2
      </SelectTypographyButton>
      <SelectTypographyButton
        dataCy="PARAGRAPH-BUTTON"
        isActive={activeTypography === "PARAGRAPH"}
        onClick={() => applyAction(Actions.switchTypography, setEditorState, editorState.focus.blockIndex, "PARAGRAPH")}
      >
        Normal
      </SelectTypographyButton>
    </div>
  );
};

function SelectTypographyButton({
  dataCy,
  isActive,
  children,
  onClick,
}: {
  dataCy: string;
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
      data-cy={dataCy}
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
