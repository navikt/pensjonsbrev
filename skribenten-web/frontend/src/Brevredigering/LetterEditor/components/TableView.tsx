import { css } from "@emotion/react";
import { PlusIcon, TrashIcon } from "@navikt/aksel-icons";
import { ActionMenu } from "@navikt/ds-react";
import React, { useState } from "react";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { applyAction } from "~/Brevredigering/LetterEditor/lib/actions";
import { type Cell as CellType, type ColumnSpec, LITERAL, type Table } from "~/types/brevbakerTypes";

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

type CellIndexes = {
  blockIndex: number;
  contentIndex: number;
  itemIndex: number;
  itemContentIndex: number;
};

const renderCellText = (cell: CellType, _: number, idx: CellIndexes) =>
  cell.text
    .filter((txt) => txt.type === LITERAL)
    .map((lit, i) => <TableCellContent key={i} lit={lit} litIndex={idx} />);

const TableView: React.FC<{
  node: Table;
  blockIndex: number;
  contentIndex: number;
}> = ({ node, blockIndex, contentIndex }) => {
  const { editorState, setEditorState } = useEditor();
  const { tableSelection } = editorState;

  const [menuAnchor, setMenuAnchor] = useState<{ x: number; y: number } | null>(null);

  return (
    <>
      <table
        css={tableStyles}
        onContextMenu={(e) => {
          e.preventDefault();

          const cell = (e.target as HTMLElement).closest("td,th");
          if (!cell) return;

          const rowIndex = (cell.parentElement as HTMLTableRowElement).rowIndex - 1;
          const colIndex = (cell as HTMLTableCellElement).cellIndex;

          setEditorState((prev) => ({
            ...prev,
            contextMenuCell: { blockIndex, contentIndex, rowIndex, colIndex },
          }));
          setMenuAnchor({ x: e.clientX, y: e.clientY });
        }}
      >
        <thead>
          <tr>
            {node.header.colSpec.map((col: ColumnSpec, colIdx) => (
              <th key={colIdx}>
                {renderCellText(col.headerContent, colIdx, {
                  blockIndex,
                  contentIndex,
                  itemIndex: -1,
                  itemContentIndex: colIdx,
                })}
              </th>
            ))}
          </tr>
        </thead>

        <tbody>
          {node.rows.map((row, rowIdx) => {
            const isRowSelected =
              tableSelection &&
              tableSelection.blockIndex === blockIndex &&
              tableSelection.contentIndex === contentIndex &&
              tableSelection.rowIndex === rowIdx;

            return (
              <tr css={isRowSelected && selectedBackgroundStyle} key={rowIdx}>
                {row.cells.map((cell, cellIdx) => {
                  const isCellSelected =
                    editorState.contextMenuCell &&
                    editorState.contextMenuCell.blockIndex === blockIndex &&
                    editorState.contextMenuCell.contentIndex === contentIndex &&
                    editorState.contextMenuCell.rowIndex === rowIdx &&
                    editorState.contextMenuCell.colIndex === cellIdx;

                  return (
                    <td css={isCellSelected && selectedBackgroundStyle} key={cellIdx}>
                      {renderCellText(cell, cellIdx, {
                        blockIndex,
                        contentIndex,
                        itemIndex: rowIdx,
                        itemContentIndex: cellIdx,
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
        onClose={() => {
          setMenuAnchor(null);
          setEditorState((prev) => ({ ...prev, contextMenuCell: undefined }));
        }}
      >
        <ActionMenu.Item
          icon={<PlusIcon aria-hidden fontSize="1.25rem" />}
          onSelect={() => applyAction(Actions.insertTableRowAbove, setEditorState)}
        >
          Sett inn rad over
        </ActionMenu.Item>
        <ActionMenu.Item
          icon={<PlusIcon aria-hidden fontSize="1.25rem" />}
          onSelect={() => applyAction(Actions.insertTableRowBelow, setEditorState)}
        >
          Sett inn rad under
        </ActionMenu.Item>
        <ActionMenu.Item
          icon={<PlusIcon aria-hidden fontSize="1.25rem" />}
          onSelect={() => applyAction(Actions.insertTableColumnLeft, setEditorState)}
        >
          Sett inn kolonne til venstre
        </ActionMenu.Item>
        <ActionMenu.Item
          icon={<PlusIcon aria-hidden fontSize="1.25rem" />}
          onSelect={() => applyAction(Actions.insertTableColumnRight, setEditorState)}
        >
          Sett inn kolonne til høyre
        </ActionMenu.Item>

        <ActionMenu.Divider />

        <ActionMenu.Item
          icon={<TrashIcon aria-hidden fontSize="1.25rem" />}
          onSelect={() => applyAction(Actions.removeTableRow, setEditorState)}
          variant="danger"
        >
          Slett rad
        </ActionMenu.Item>
        <ActionMenu.Item
          icon={<TrashIcon aria-hidden fontSize="1.25rem" />}
          onSelect={() => applyAction(Actions.removeTableColumn, setEditorState)}
          variant="danger"
        >
          Slett kolonne
        </ActionMenu.Item>
        <ActionMenu.Item
          icon={<TrashIcon aria-hidden fontSize="1.25rem" />}
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
