import { css } from "@emotion/react";
import { ArrowRightLeftIcon, PlusIcon, TrashIcon } from "@navikt/aksel-icons";
import { ActionMenu } from "@navikt/ds-react";
import React, { useRef, useState } from "react";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { applyAction } from "~/Brevredigering/LetterEditor/lib/actions";
import { type Cell as CellType, type ColumnSpec, type Table } from "~/types/brevbakerTypes";

import { hasHeaderContentCols } from "../actions/common";
import type { TableCellIndex } from "../model/state";
import { TableCellContent } from "./TableCellContent";
import TableContextMenu from "./TableContextMenu";

const tableStyles = css`
  width: 100%;
  table-layout: fixed;
  border-collapse: collapse;

  td,
  th {
    border: 1px solid #000;
    padding: 2mm;
    word-wrap: break-word;
  }

  th {
    background: var(--a-surface-subtle);
    font-weight: 400;
  }
`;

const selectedBackgroundStyle = css`
  background: var(--a-surface-info-subtle, #d0e7ff) !important;
`;

// TODO: render <ContentGroup> once that component
// can accept TableCellIndex (rowIndex/cellIndex)
const renderCellText = (cell: CellType, _: number, idx: TableCellIndex) =>
  cell.text.map((txt, i) => (
    <TableCellContent content={txt} key={i} tableCellIndex={{ ...idx, cellContentIndex: i }} />
  ));

const TableView: React.FC<{
  node: Table;
  blockIndex: number;
  contentIndex: number;
}> = ({ node, blockIndex, contentIndex }) => {
  const { setEditorState } = useEditor();
  const [menuAnchor, setMenuAnchor] = useState<{ x: number; y: number } | null>(null);
  const [highlight, setHighlight] = useState<{ row: number; col: number } | null>(null);

  const menuTargetRef = useRef<{ rowIndex: number; colIndex: number } | null>(null);

  const headerHasContent = hasHeaderContentCols(node.header?.colSpec);
  const hasHeader = node.header?.colSpec?.length > 0;
  const canPromoteHeader = !headerHasContent;

  const onlyOneCol = (node.header.colSpec?.length ?? 0) <= 1;

  const clickedRow = menuTargetRef.current?.rowIndex;
  const isHeaderCtx = clickedRow === -1;

  return (
    <>
      <table
        css={tableStyles}
        data-cy="letter-table"
        onContextMenu={(e) => {
          e.preventDefault();

          const cell = (e.target as HTMLElement).closest("td,th") as HTMLTableCellElement | null;
          if (!cell) return;
          const rowEl = cell.parentElement as HTMLTableRowElement;

          // Did our TableView render a thead?
          const hasRenderedThead = hasHeaderContentCols(node.header?.colSpec);

          // Is the clicked cell a header cell in our rendered table?
          const isHeaderCell = cell.tagName === "TH" || (hasRenderedThead && rowEl.parentElement?.tagName === "THEAD");

          const rowIndex = isHeaderCell ? -1 : rowEl.rowIndex - (hasRenderedThead ? 1 : 0);
          const colIndex = cell.cellIndex;

          setEditorState((prev) => ({
            ...prev,
            focus: {
              blockIndex,
              contentIndex,
              rowIndex,
              cellIndex: colIndex,
              cellContentIndex: 0,
            },
          }));
          menuTargetRef.current = { rowIndex, colIndex };
          setHighlight({ row: rowIndex, col: colIndex });
          setMenuAnchor({ x: e.clientX, y: e.clientY });
        }}
      >
        {hasHeader && (
          <thead>
            <tr>
              {node.header.colSpec.map((col: ColumnSpec, colIdx) => {
                const isHeaderHighlighted =
                  !!highlight && highlight.row === -1 && (highlight.col === -1 || highlight.col === colIdx);
                return (
                  <th
                    css={isHeaderHighlighted && selectedBackgroundStyle}
                    data-cy={`table-header-${colIdx}`}
                    key={colIdx}
                    scope="col"
                  >
                    {renderCellText(col.headerContent, colIdx, {
                      blockIndex,
                      contentIndex,
                      rowIndex: -1,
                      cellIndex: colIdx,
                      cellContentIndex: 0,
                    })}
                  </th>
                );
              })}
            </tr>
          </thead>
        )}
        <tbody>
          {node.rows.map((row, rowIdx) => {
            return (
              <tr data-cy={`table-row-${rowIdx}`} key={rowIdx}>
                {row.cells.map((cell, cellIdx) => {
                  const isHighlighted =
                    !!highlight && highlight.row === rowIdx && (highlight.col === -1 || highlight.col === cellIdx);
                  return (
                    <td
                      css={isHighlighted && selectedBackgroundStyle}
                      data-cy={`table-cell-${rowIdx}-${cellIdx}`}
                      key={cellIdx}
                    >
                      {renderCellText(cell, cellIdx, {
                        blockIndex,
                        contentIndex,
                        rowIndex: rowIdx,
                        cellIndex: cellIdx,
                        cellContentIndex: 0,
                      })}
                    </td>
                  );
                })}
              </tr>
            );
          })}
        </tbody>
      </table>

      <TableContextMenu
        anchor={menuAnchor}
        data-cy="table-action-menu"
        onClose={() => {
          setMenuAnchor(null);
          setHighlight(null);
          menuTargetRef.current = null;
        }}
      >
        {canPromoteHeader && typeof clickedRow === "number" && clickedRow >= 0 && (
          <ActionMenu.Item
            icon={<ArrowRightLeftIcon fontSize="1.25rem" />}
            onMouseEnter={() => setHighlight({ row: clickedRow, col: -1 })}
            onMouseLeave={() =>
              setHighlight(
                menuTargetRef.current
                  ? { row: menuTargetRef.current.rowIndex, col: menuTargetRef.current.colIndex }
                  : null,
              )
            }
            onSelect={() => {
              const rowIdx = menuTargetRef.current?.rowIndex ?? -1;
              if (rowIdx >= 0) {
                applyAction(Actions.promoteRowToHeader, setEditorState, blockIndex, contentIndex, rowIdx);
                menuTargetRef.current = null;
              }
            }}
          >
            Gjør rad til overskrift
          </ActionMenu.Item>
        )}
        {!canPromoteHeader && isHeaderCtx && (
          <ActionMenu.Item
            icon={<ArrowRightLeftIcon fontSize="1.25rem" />}
            onMouseEnter={() => setHighlight({ row: -1, col: -1 })}
            onMouseLeave={() =>
              setHighlight(
                menuTargetRef.current
                  ? { row: menuTargetRef.current.rowIndex, col: menuTargetRef.current.colIndex }
                  : null,
              )
            }
            onSelect={() => {
              applyAction(Actions.demoteHeaderToRow, setEditorState, blockIndex, contentIndex);
              menuTargetRef.current = null;
            }}
          >
            Gjør overskrift til vanlig tekst
          </ActionMenu.Item>
        )}
        {isHeaderCtx && <ActionMenu.Divider />}
        <ActionMenu.Item
          disabled={isHeaderCtx}
          icon={<PlusIcon fontSize="1.25rem" />}
          onSelect={() => applyAction(Actions.insertTableRowAbove, setEditorState)}
        >
          Sett inn rad over
        </ActionMenu.Item>
        <ActionMenu.Item
          icon={<PlusIcon fontSize="1.25rem" />}
          onSelect={() => applyAction(Actions.insertTableRowBelow, setEditorState)}
        >
          Sett inn rad under
        </ActionMenu.Item>
        <ActionMenu.Item
          icon={<PlusIcon fontSize="1.25rem" />}
          onSelect={() => applyAction(Actions.insertTableColumnLeft, setEditorState)}
        >
          Sett inn kolonne til venstre
        </ActionMenu.Item>
        <ActionMenu.Item
          icon={<PlusIcon fontSize="1.25rem" />}
          onSelect={() => applyAction(Actions.insertTableColumnRight, setEditorState)}
        >
          Sett inn kolonne til høyre
        </ActionMenu.Item>

        <ActionMenu.Divider />

        <ActionMenu.Item
          icon={<TrashIcon fontSize="1.25rem" />}
          onSelect={() => applyAction(Actions.removeTableRow, setEditorState)}
          variant="danger"
        >
          Slett rad
        </ActionMenu.Item>
        <ActionMenu.Item
          disabled={onlyOneCol}
          icon={<TrashIcon fontSize="1.25rem" />}
          onSelect={() => applyAction(Actions.removeTableColumn, setEditorState)}
          variant="danger"
        >
          Slett kolonne
        </ActionMenu.Item>
        <ActionMenu.Item
          icon={<TrashIcon fontSize="1.25rem" />}
          onSelect={() => applyAction(Actions.removeTable, setEditorState)}
          variant="danger"
        >
          Slett tabellen
        </ActionMenu.Item>
      </TableContextMenu>
    </>
  );
};

export default TableView;
