import { ArrowDownIcon, ArrowUpIcon, FilesIcon, TrashIcon } from "@navikt/aksel-icons";
import { Button, HStack } from "@navikt/ds-react";
import { type ReactNode } from "react";

import { useEditor } from "./EditorContext";

/** Flytt/kopier/slett-knapper for ett element i en liste. Skjules helt for lesere. */
export function RowActions({
  what,
  index,
  count,
  onMove,
  onDuplicate,
  onDelete,
  extra,
  canDelete = true,
}: {
  what: string;
  index: number;
  count: number;
  onMove: (delta: -1 | 1) => void;
  onDuplicate?: () => void;
  onDelete: () => void;
  extra?: ReactNode;
  /** Settes til false for blokker som beskytter låst kildeinnhold. */
  canDelete?: boolean;
}) {
  const { canEditStructure: canEdit } = useEditor();
  if (!canEdit) {
    return null;
  }
  return (
    <HStack gap="space-4" wrap>
      <Button
        disabled={index === 0}
        icon={<ArrowUpIcon aria-hidden />}
        onClick={() => onMove(-1)}
        size="xsmall"
        title={`Flytt ${what} opp`}
        variant="tertiary"
      />
      <Button
        disabled={index >= count - 1}
        icon={<ArrowDownIcon aria-hidden />}
        onClick={() => onMove(1)}
        size="xsmall"
        title={`Flytt ${what} ned`}
        variant="tertiary"
      />
      {onDuplicate ? (
        <Button
          icon={<FilesIcon aria-hidden />}
          onClick={onDuplicate}
          size="xsmall"
          title={`Kopier ${what}`}
          variant="tertiary"
        />
      ) : null}
      {extra}
      {canDelete ? (
        <Button
          data-color="danger"
          icon={<TrashIcon aria-hidden />}
          onClick={onDelete}
          size="xsmall"
          title={`Slett ${what}`}
          variant="tertiary"
        />
      ) : null}
    </HStack>
  );
}
