import { css } from "@emotion/react";
import { PlusIcon, TrashIcon } from "@navikt/aksel-icons";
import { ActionMenu } from "@navikt/ds-react";
import React, { useState } from "react";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { applyAction } from "~/Brevredigering/LetterEditor/lib/actions";
import { type Cell as CellType, type ColumnSpec, type Table } from "~/types/brevbakerTypes";

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
  background: var(--a-surface-info-subtle, #d0e7ff);
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

  return (
    <>
      <table
        css={tableStyles}
        data-cy="letter-table"
        onContextMenu={(e) => {
          e.preventDefault();

          const cell = (e.target as HTMLElement).closest("td,th");
          if (!cell) return;

          const rowIndex = (cell.parentElement as HTMLTableRowElement).rowIndex - 1;
          const colIndex = (cell as HTMLTableCellElement).cellIndex;

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
          setHighlight({ row: rowIndex, col: colIndex });
          setMenuAnchor({ x: e.clientX, y: e.clientY });
        }}
      >
        <thead>
          <tr>
            {node.header.colSpec.map((col: ColumnSpec, colIdx) => (
              <th data-cy={`table-header-${colIdx}`} key={colIdx}>
                {renderCellText(col.headerContent, colIdx, {
                  blockIndex,
                  contentIndex,
                  rowIndex: -1,
                  cellIndex: colIdx,
                  cellContentIndex: 0,
                })}
              </th>
            ))}
          </tr>
        </thead>

        <tbody>
          {node.rows.map((row, rowIdx) => {
            return (
              <tr data-cy={`table-row-${rowIdx}`} key={rowIdx}>
                {row.cells.map((cell, cellIdx) => {
                  const isHighLight = highlight && highlight.row === rowIdx && highlight.col === cellIdx;
                  return (
                    <td
                      css={isHighLight && selectedBackgroundStyle}
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
        }}
      >
        <ActionMenu.Item
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
