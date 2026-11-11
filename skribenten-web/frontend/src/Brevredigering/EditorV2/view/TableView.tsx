import { css } from "@emotion/react";
import { ArrowRightLeftIcon, PlusIcon, TrashIcon } from "@navikt/aksel-icons";
import { ActionMenu } from "@navikt/ds-react";
import { Fragment, type MouseEvent, useState } from "react";

import TableContextMenu from "~/Brevredigering/LetterEditor/components/TableContextMenu";
import { type TableV2 } from "~/types/brevV2";

import { placeCaret } from "../caret/domCaret";
import {
  demoteHeaderToRow,
  insertTableColumnLeft,
  insertTableColumnRight,
  insertTableRowAbove,
  insertTableRowBelow,
  promoteRowToHeader,
  removeTable,
  removeTableColumn,
  removeTableRow,
} from "../commands/table";
import { moveFocus } from "../commands/updateText";
import { useAttestantDiffV2 } from "../diff/AttestantDiffContextV2";
import { DeletedRowsAtV2 } from "../diff/DeletedMarkupV2";
import { type Caret, caret } from "../model/paths";
import { isEmptyContent } from "../model/text";
import { type Command } from "../state/editorState";
import { useEditorV2 } from "./EditorContextV2";
import { TextContainerView } from "./text/TextContainerView";

const tableStyles = css`
  width: 100%;
  table-layout: fixed;
  border-collapse: collapse;
  margin-block: var(--ax-space-28);

  td,
  th {
    border-bottom: 1px solid var(--ax-border-neutral);
    padding: 2mm;
    overflow-wrap: break-word;
    font-weight: var(--ax-font-weight-regular);
    font-size: var(--ax-font-size-medium);
  }

  th {
    border-bottom: 2px solid var(--ax-border-focus);
    background: var(--ax-bg-accent-moderate);
    font-weight: var(--ax-font-weight-bold);
    text-align: left;
  }

  tbody > tr:nth-of-type(even) {
    background: var(--ax-bg-neutral-moderate);
  }

  tbody > tr:nth-of-type(odd) {
    background: var(--ax-bg-default);
  }
`;

const selectedStyle = css`
  && {
    background: var(--ax-bg-accent-soft);
  }
`;

type Target = { row: number; column: number };

function caretAt(block: number, { row, column }: Target): Caret {
  return caret(row < 0 ? { kind: "header", block, column } : { kind: "cell", block, row, cell: column });
}

function focusEmptyCell(event: MouseEvent<HTMLTableElement>) {
  if (!(event.target instanceof HTMLTableCellElement) || event.button !== 0) return;
  const cell = event.target;
  if (cell.contains(document.activeElement) || (cell.textContent ?? "").replaceAll("\u200B", "") !== "") return;
  const literal = cell.querySelector<HTMLElement>("[contenteditable=true]");
  if (literal) placeCaret(literal, 0);
}

function targetOf(event: MouseEvent<HTMLTableElement>): Target | undefined {
  const cell = event.target instanceof Element ? event.target.closest("td,th") : null;
  if (!(cell instanceof HTMLTableCellElement) || !(cell.parentElement instanceof HTMLTableRowElement)) return undefined;
  const header = cell.tagName === "TH";
  return { row: header ? -1 : cell.parentElement.rowIndex - 1, column: cell.cellIndex };
}

export function TableView({ table, block }: { table: TableV2; block: number }) {
  const { dispatch } = useEditorV2();
  const { diffHash, disableDiffMode } = useAttestantDiffV2();
  const [anchor, setAnchor] = useState<{ x: number; y: number } | null>(null);
  const [target, setTarget] = useState<Target | null>(null);
  const [highlight, setHighlight] = useState<Target | null>(null);

  const headerIsEmpty = table.header.colSpec.every((column) => isEmptyContent(column.content));
  const onHeader = target?.row === -1;
  const highlighted = (row: number, column: number) =>
    !!highlight && highlight.row === row && (highlight.column === -1 || highlight.column === column);

  const close = () => {
    setAnchor(null);
    setTarget(null);
    setHighlight(null);
  };

  const run = (command: Command<[at: Caret]>) => () => {
    if (target) dispatch(command, caretAt(block, target));
  };

  return (
    <>
      {/* biome-ignore lint/a11y/useKeyWithClickEvents: kun museklikk utenfor tekst; tastatur når cellene via Tab og piltaster */}
      <table
        css={tableStyles}
        data-testid="letter-table"
        onClick={focusEmptyCell}
        onContextMenu={(event) => {
          event.preventDefault();
          if (diffHash !== undefined) {
            disableDiffMode();
            return;
          }
          const clicked = targetOf(event);
          if (!clicked) return;
          dispatch(moveFocus, caretAt(block, clicked));
          setTarget(clicked);
          setHighlight(clicked);
          setAnchor({ x: event.clientX, y: event.clientY });
        }}
      >
        <thead>
          <tr>
            {table.header.colSpec.map((column, index) => (
              <th
                css={highlighted(-1, index) && selectedStyle}
                data-testid={`table-header-${index}`}
                key={index}
                scope="col"
              >
                <TextContainerView content={column.content} path={{ kind: "header", block, column: index }} />
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {table.rows.map((row, rowIndex) => (
            <Fragment key={rowIndex}>
              <DeletedRowsAtV2 block={block} row={rowIndex} />
              <tr data-testid={`table-row-${rowIndex}`}>
                {row.cells.map((cell, cellIndex) => (
                  <td
                    css={highlighted(rowIndex, cellIndex) && selectedStyle}
                    data-testid={`table-cell-${rowIndex}-${cellIndex}`}
                    key={cellIndex}
                  >
                    <TextContainerView
                      content={cell.content}
                      path={{ kind: "cell", block, row: rowIndex, cell: cellIndex }}
                    />
                  </td>
                ))}
              </tr>
            </Fragment>
          ))}
          <DeletedRowsAtV2 block={block} row={table.rows.length} trailing />
        </tbody>
      </table>

      <TableContextMenu anchor={anchor} onClose={close}>
        {headerIsEmpty && target && target.row >= 0 && (
          <ActionMenu.Item
            icon={<ArrowRightLeftIcon fontSize="1.25rem" />}
            onMouseEnter={() => setHighlight({ row: target.row, column: -1 })}
            onMouseLeave={() => setHighlight(target)}
            onSelect={run(promoteRowToHeader)}
          >
            Gjør rad til overskrift
          </ActionMenu.Item>
        )}
        {!headerIsEmpty && onHeader && (
          <ActionMenu.Item
            icon={<ArrowRightLeftIcon fontSize="1.25rem" />}
            onMouseEnter={() => setHighlight({ row: -1, column: -1 })}
            onMouseLeave={() => setHighlight(target)}
            onSelect={run(demoteHeaderToRow)}
          >
            Gjør overskrift til vanlig tekst
          </ActionMenu.Item>
        )}
        {onHeader && !headerIsEmpty && <ActionMenu.Divider />}
        <ActionMenu.Item disabled={onHeader} icon={<PlusIcon fontSize="1.25rem" />} onSelect={run(insertTableRowAbove)}>
          Sett inn rad over
        </ActionMenu.Item>
        <ActionMenu.Item icon={<PlusIcon fontSize="1.25rem" />} onSelect={run(insertTableRowBelow)}>
          Sett inn rad under
        </ActionMenu.Item>
        <ActionMenu.Item icon={<PlusIcon fontSize="1.25rem" />} onSelect={run(insertTableColumnLeft)}>
          Sett inn kolonne til venstre
        </ActionMenu.Item>
        <ActionMenu.Item icon={<PlusIcon fontSize="1.25rem" />} onSelect={run(insertTableColumnRight)}>
          Sett inn kolonne til høyre
        </ActionMenu.Item>
        <ActionMenu.Divider />
        <ActionMenu.Item icon={<TrashIcon fontSize="1.25rem" />} onSelect={run(removeTableRow)} variant="danger">
          Slett rad
        </ActionMenu.Item>
        <ActionMenu.Item
          disabled={table.header.colSpec.length <= 1}
          icon={<TrashIcon fontSize="1.25rem" />}
          onSelect={run(removeTableColumn)}
          variant="danger"
        >
          Slett kolonne
        </ActionMenu.Item>
        <ActionMenu.Item icon={<TrashIcon fontSize="1.25rem" />} onSelect={run(removeTable)} variant="danger">
          Slett tabellen
        </ActionMenu.Item>
      </TableContextMenu>
    </>
  );
}
