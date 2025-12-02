import { css } from "@emotion/react";
import { ExclamationmarkTriangleFillIcon } from "@navikt/aksel-icons";
import { BodyShort, HStack, Select } from "@navikt/ds-react";
import { format, isToday } from "date-fns";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { isTextContent } from "~/Brevredigering/LetterEditor/model/utils";
import { VerticalDivider } from "~/components/Divider";
import EditorTableTools from "~/components/EditorTableTools";
import { formatTime } from "~/utils/dateUtils";

import { applyAction } from "../lib/actions";
import { getCursorOffset } from "../services/caretUtils";
import type { Typography } from "../utils";
import { TypographyToText } from "../utils";
import EditorBulletList from "./EditorBulletList";
import EditorFonts from "./EditorFonts";
import { EditorUndoRedo } from "./EditorUndoRedo";

const SelectTypography = () => {
  const { editorState, freeze, setEditorState } = useEditor();
  const changeableContent = isTextContent(
    editorState.redigertBrev.blocks[editorState.focus.blockIndex]?.content?.[editorState.focus.contentIndex],
  );

  return (
    <Select
      data-cy="typography-select"
      disabled={freeze || editorState.focus.blockIndex < 0}
      hideLabel
      label="Tekst stil"
      onChange={(e) => {
        applyAction(Actions.switchTypography, setEditorState, e.target.value as Typography);
        //setter fokuset tilbake til editor etter valgt tekststil
        applyAction(Actions.cursorPosition, setEditorState, getCursorOffset());
      }}
      readOnly={!changeableContent}
      size="small"
      value={editorState.redigertBrev.blocks[editorState.focus.blockIndex]?.type}
    >
      {Object.entries(TypographyToText).map(([key, value]) => (
        <option key={key} value={key}>
          {value}
        </option>
      ))}
    </Select>
  );
};

type EditorMenuProps = {
  undo: () => void;
  redo: () => void;
  canUndo: boolean;
  canRedo: boolean;
};

export const EditorMenu = ({ undo, redo, canUndo, canRedo }: EditorMenuProps) => {
  return (
    <div
      css={css`
        border-bottom: 1px solid var(--ax-neutral-300);
        background: var(--ax-bg-default);
        padding-inline: var(--ax-space-16);
        min-height: 48px;
        display: flex;
        gap: var(--ax-space-4);
        align-self: stretch;
        align-items: center;
        justify-content: space-between;
      `}
    >
      <HStack align="center" gap="4" margin-block="2">
        <EditorUndoRedo canRedo={canRedo} canUndo={canUndo} redo={redo} undo={undo} />
        <VerticalDivider />
        <EditorFonts />
        <VerticalDivider />
        <EditorBulletList />
        <VerticalDivider />
        <EditorTableTools />
        <VerticalDivider />
        <SelectTypography />
      </HStack>

      <LagringStatus />
    </div>
  );
};

const LagringStatus = () => {
  const { error, editorState, freeze } = useEditor();
  if (freeze || editorState.saveStatus === "SAVE_PENDING") {
    return (
      <HStack gap="1">
        <BodyShort size="small">Lagrer...</BodyShort>
      </HStack>
    );
  } else if (error) {
    const tekst = isToday(editorState.info.sistredigert)
      ? `Klarte ikke lagre. Sist lagret ${formatTime(editorState.info.sistredigert)}`
      : `Klarte ikke lagre. Sist lagret ${format(editorState.info.sistredigert, "dd.MM.yyyy HH:mm")}`;

    return (
      <HStack gap="1">
        <ExclamationmarkTriangleFillIcon color="#FF9100" fontSize="1.5rem" title="error-ikon" />
        {tekst}
      </HStack>
    );
  } else if (editorState.saveStatus === "SAVED") {
    return (
      <HStack gap="1">
        <BodyShort size="small">Lagret</BodyShort>
      </HStack>
    );
  }
};
