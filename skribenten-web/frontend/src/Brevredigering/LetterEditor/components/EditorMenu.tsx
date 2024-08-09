import { css } from "@emotion/react";
import { CheckmarkCircleFillIcon } from "@navikt/aksel-icons";
import { Button, HStack, Loader } from "@navikt/ds-react";
import { format, isToday } from "date-fns";
import type { ReactNode } from "react";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { isTextContent } from "~/Brevredigering/LetterEditor/model/utils";
import { formatTime } from "~/utils/dateUtils";

import { applyAction } from "../lib/actions";

export const EditorMenu = () => {
  const { freeze, editorState, setEditorState } = useEditor();
  const activeTypography = editorState.redigertBrev.blocks[editorState.focus.blockIndex]?.type;
  const changeableContent = isTextContent(
    editorState.redigertBrev.blocks[editorState.focus.blockIndex].content[editorState.focus.contentIndex],
  );

  return (
    <div
      css={css`
        border-bottom: 1px solid var(--a-gray-200);
        background: var(--a-blue-50);
        padding: var(--a-spacing-3) var(--a-spacing-4);
        display: flex;
        gap: var(--a-spacing-2);
        align-self: stretch;
      `}
    >
      <SelectTypographyButton
        dataCy="TITLE1-BUTTON"
        enabled={activeTypography !== "TITLE1" && changeableContent}
        onClick={() => applyAction(Actions.switchTypography, setEditorState, editorState.focus, "TITLE1")}
      >
        Overskrift 1
      </SelectTypographyButton>
      <SelectTypographyButton
        dataCy="TITLE2-BUTTON"
        enabled={activeTypography !== "TITLE2" && changeableContent}
        onClick={() => applyAction(Actions.switchTypography, setEditorState, editorState.focus, "TITLE2")}
      >
        Overskrift 2
      </SelectTypographyButton>
      <SelectTypographyButton
        dataCy="PARAGRAPH-BUTTON"
        enabled={activeTypography !== "PARAGRAPH" && changeableContent}
        onClick={() => applyAction(Actions.switchTypography, setEditorState, editorState.focus, "PARAGRAPH")}
      >
        Normal
      </SelectTypographyButton>
      <LagretTidspunkt datetime={editorState.info.sistredigert} freeze={freeze} isDirty={editorState.isDirty} />
    </div>
  );
};

function LagretTidspunkt({ freeze, datetime, isDirty }: { freeze: boolean; datetime: string; isDirty: boolean }) {
  if (freeze) {
    return (
      <HStack gap="1">
        <Loader title="Lagrer..." />
        Lagrer...
      </HStack>
    );
  } else {
    const tekst = isToday(datetime)
      ? `Lagret kl ${formatTime(datetime)}`
      : `Lagret ${format(datetime, "dd.MM.yyyy HH:mm")}`;

    const ikon = isDirty ? null : <CheckmarkCircleFillIcon color="#007C2E" fontSize="1.5rem" title="vellykket-ikon" />;

    return (
      <HStack gap="1">
        {ikon}
        {tekst}
      </HStack>
    );
  }
}

function SelectTypographyButton({
  dataCy,
  enabled,
  children,
  onClick,
}: {
  dataCy: string;
  enabled: boolean;
  children: ReactNode;
  onClick: () => void;
}) {
  return (
    <Button
      css={
        !enabled &&
        css`
          color: var(--a-text-on-action);
          background-color: var(--a-surface-action-active);
        `
      }
      data-cy={dataCy}
      disabled={!enabled}
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
