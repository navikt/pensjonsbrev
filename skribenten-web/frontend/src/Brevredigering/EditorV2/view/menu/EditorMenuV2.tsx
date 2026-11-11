import {
  ArrowCirclepathReverseIcon,
  BulletListIcon,
  ExclamationmarkTriangleFillIcon,
  NumberListIcon,
  TableIcon,
} from "@navikt/aksel-icons";
import { BodyShort, Box, Button, HStack, Select, Tooltip } from "@navikt/ds-react";
import { format, isToday } from "date-fns";
import { type ReactNode, useState } from "react";

import { EditorUndoRedo } from "~/Brevredigering/LetterEditor/components/EditorUndoRedo";
import InsertTableDialog from "~/Brevredigering/LetterEditor/components/InsertTableDialog";
import { useRedigeringsflate } from "~/Brevredigering/LetterEditor/RedigeringsflateContext";
import { tooltipText } from "~/Brevredigering/LetterEditor/utils";
import { VerticalDivider } from "~/components/Divider";
import { type FontTypeV2, type ListBlockTypeV2 } from "~/types/brevV2";
import { formatTime } from "~/utils/dateUtils";

import { switchFontType } from "../../commands/switchFontType";
import { switchTypography } from "../../commands/switchTypography";
import { insertTable } from "../../commands/table";
import { toggleList } from "../../commands/toggleList";
import { type Caret, isListBlock, isTextBlock, resolveText } from "../../model/paths";
import { fontTypeOf } from "../../model/text";
import { canRedo, canUndo } from "../../state/history";
import { useEditorV2 } from "../EditorContextV2";
import { useMenuDispatch } from "../useMenuDispatch";
import { TYPOGRAPHY, typographyOf } from "./typography";

function FontButton({
  fontType,
  label,
  testId,
  tooltip,
}: {
  fontType: FontTypeV2;
  label: ReactNode;
  testId: string;
  tooltip: string;
}) {
  const { state, freeze } = useEditorV2();
  const run = useMenuDispatch();
  const text = resolveText(state.redigertBrev, state.focus);
  const active = !!text && fontTypeOf(text) === fontType;
  return (
    <Tooltip content={tooltip}>
      <Button
        color="text-neutral"
        data-testid={testId}
        disabled={freeze || state.focus.path.kind === "title"}
        onClick={() => run(switchFontType, fontType)}
        size="small"
        type="button"
        variant={active ? "primary-neutral" : "tertiary-neutral"}
      >
        {label}
      </Button>
    </Tooltip>
  );
}

const LIST_BUTTONS: Record<ListBlockTypeV2, { label: string; testId: string; tooltip: string; icon: ReactNode }> = {
  ITEM_LIST: {
    label: "Punktliste",
    testId: "bullet-list-button",
    tooltip: tooltipText.bulletList,
    icon: <BulletListIcon fontSize="1.5rem" title="punktliste-ikon" />,
  },
  NUMBERED_LIST: {
    label: "Nummerert liste",
    testId: "number-list-button",
    tooltip: tooltipText.numberList,
    icon: <NumberListIcon fontSize="1.5rem" title="nummerert-liste-ikon" />,
  },
};

function ListButton({ type }: { type: ListBlockTypeV2 }) {
  const { state, freeze } = useEditorV2();
  const run = useMenuDispatch();
  const { path } = state.focus;
  const block = path.kind === "item" ? state.redigertBrev.blocks[path.block] : undefined;
  const active = isListBlock(block) && block.type === type;
  const { label, testId, tooltip, icon } = LIST_BUTTONS[type];
  return (
    <Tooltip content={tooltip}>
      <Button
        aria-label={label}
        color="text-neutral"
        data-testid={testId}
        disabled={freeze || path.kind === "title"}
        icon={icon}
        onClick={() => run(toggleList, type)}
        size="small"
        type="button"
        variant={active ? "primary-neutral" : "tertiary-neutral"}
      />
    </Tooltip>
  );
}

function TableButton() {
  const { state, freeze, dispatch } = useEditorV2();
  const [openedAt, setOpenedAt] = useState<Caret | null>(null);
  return (
    <>
      <Tooltip content={tooltipText.table}>
        <Button
          aria-label={tooltipText.table}
          data-color="neutral"
          data-testid="toolbar-table-button"
          disabled={freeze || state.focus.path.kind === "title"}
          icon={<TableIcon fontSize="1.5rem" title="Sett inn tabell" />}
          onClick={() => setOpenedAt(state.focus)}
          size="small"
          type="button"
          variant="tertiary"
        />
      </Tooltip>
      <InsertTableDialog
        onCancel={() => setOpenedAt(null)}
        onInsert={(columns, rows) => {
          if (openedAt) dispatch(insertTable, openedAt, rows, columns);
          setOpenedAt(null);
        }}
        open={openedAt !== null}
      />
    </>
  );
}

function SelectTypography() {
  const { state, freeze } = useEditorV2();
  const run = useMenuDispatch();
  const { path } = state.focus;
  const block = path.kind === "block" ? state.redigertBrev.blocks[path.block] : undefined;
  return (
    <Select
      data-testid="typography-select"
      disabled={freeze || path.kind === "title"}
      hideLabel
      label="Tekst stil"
      onChange={(event) => {
        const type = typographyOf(event.target.value);
        if (type) run(switchTypography, type);
      }}
      readOnly={!isTextBlock(block)}
      size="small"
      value={isTextBlock(block) ? block.type : "PARAGRAPH"}
    >
      {TYPOGRAPHY.map(({ type, label }) => (
        <option key={type} value={type}>
          {label}
        </option>
      ))}
    </Select>
  );
}

function LagringStatus() {
  const { error, state, freeze } = useEditorV2();
  if (freeze || state.saveStatus === "SAVE_PENDING") return <BodyShort size="small">Lagrer...</BodyShort>;
  if (error) {
    const sist = state.info.sistredigert;
    const tidspunkt = isToday(sist) ? formatTime(sist) : format(sist, "dd.MM.yyyy HH:mm");
    return (
      <HStack gap="space-4">
        <ExclamationmarkTriangleFillIcon color="#FF9100" fontSize="1.5rem" title="error-ikon" />
        {`Klarte ikke lagre. Sist lagret ${tidspunkt}`}
      </HStack>
    );
  }
  return state.saveStatus === "SAVED" ? <BodyShort size="small">Lagret</BodyShort> : null;
}

export function EditorMenuV2({ onTilbakestill }: { onTilbakestill?: () => void }) {
  const { state, freeze, undo, redo } = useEditorV2();
  const redigeringsflate = useRedigeringsflate();
  return (
    <Box
      asChild
      background="default"
      borderColor="neutral-subtle"
      borderWidth="0 0 1 0"
      flexShrink="0"
      minHeight="48px"
      width="100%"
    >
      <HStack align="center" gap="space-4" justify="space-between" paddingInline="space-8">
        <HStack align="center" gap="space-8">
          <EditorUndoRedo
            canRedo={!freeze && canRedo(state.history)}
            canUndo={!freeze && canUndo(state.history)}
            redo={redo}
            undo={undo}
          />
          <VerticalDivider />
          <FontButton
            fontType="BOLD"
            label={<BodyShort weight="semibold">F</BodyShort>}
            testId="fonttype-bold-button"
            tooltip={tooltipText.bold}
          />
          <FontButton
            fontType="ITALIC"
            label={
              <BodyShort css={{ fontStyle: "italic" }} weight="semibold">
                K
              </BodyShort>
            }
            testId="fonttype-italic-button"
            tooltip={tooltipText.italic}
          />
          <VerticalDivider />
          <ListButton type="ITEM_LIST" />
          <ListButton type="NUMBERED_LIST" />
          <VerticalDivider />
          <TableButton />
          <VerticalDivider />
          <SelectTypography />
        </HStack>
        <HStack align="center" gap="space-16">
          <LagringStatus />
          {onTilbakestill && redigeringsflate === "saksbehandler-redigering" && (
            <Tooltip content={tooltipText.tilbakestill}>
              <Button
                aria-label="Tilbakestill mal"
                data-color="danger"
                data-testid="tilbakestill-mal-button"
                icon={<ArrowCirclepathReverseIcon fontSize="1.5rem" title="Tilbakestill mal" />}
                onClick={onTilbakestill}
                size="small"
                type="button"
                variant="primary"
              />
            </Tooltip>
          )}
        </HStack>
      </HStack>
    </Box>
  );
}
