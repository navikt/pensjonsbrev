import { css } from "@emotion/react";
import { ChevronRightIcon } from "@navikt/aksel-icons";
import * as CM from "@radix-ui/react-context-menu";
import React from "react";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { applyAction } from "~/Brevredigering/LetterEditor/lib/actions";
import { type Cell as CellType, type ColumnSpec, LITERAL, type Table } from "~/types/brevbakerTypes";

import DeleteCellsSubMenu from "./DeleteCellsSubMenu";
import { TableCellContent } from "./TableCellContent";

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
    background: var(--a-gray-100);
    font-weight: 600;
  }
`;

const selectedRowStyles = css`
  background: var(--a-surface-info-subtle, #d0e7ff);
`;

const cellSelectedStyles = css`
  background: var(--a-surface-info-subtle, #d0e7ff);
`;

const menuContentStyles = css`
  background: var(--a-surface-default);
  border: 1px solid var(--a-border-subtle);
  border-radius: 6px;
  padding: 0.25rem;
  box-shadow: 0 2px 6px rgba(0 0 0 / 0.15);
  display: flex;
  flex-direction: column;
  min-width: 220px;
`;

const menuItemStyles = css`
  font-size: 1rem;
  line-height: 1.375rem;
  padding: 4px 12px;
  border-radius: 6px;
  cursor: default;
  user-select: none;
  display: flex;
  align-items: center;
  gap: 0.5rem;

  &[data-highlighted] {
    background: var(--a-surface-action-subtle);
    color: var(--a-text-default);
  }

  &:focus {
    outline: none;
    box-shadow: none;
  }

  &[data-disabled] {
    opacity: 0.45;
    cursor: not-allowed;
  }
`;

const menuSeparatorStyles = css`
  height: 1px;
  margin: 4px 0;
  background: var(--a-border-subtle);
`;

type CellIndexes = {
  blockIndex: number;
  contentIndex: number;
  itemIndex: number;
  itemContentIndex: number;
};

const renderCellText = (cell: CellType, idx: number, indexes: CellIndexes) =>
  cell.text
    .filter((t) => t.type === LITERAL)
    .map((lit, litIdx) => <TableCellContent key={litIdx} lit={lit} litIndex={indexes} />);

const TableView: React.FC<{
  node: Table;
  blockIndex: number;
  contentIndex: number;
}> = ({ node, blockIndex, contentIndex }) => {
  const { editorState, setEditorState } = useEditor();
  const { tableSelection } = editorState;

  return (
    <CM.Root
      onOpenChange={(open) => {
        if (!open) {
          setEditorState((prev) => ({ ...prev, contextMenuCell: undefined }));
        }
      }}
    >
      <CM.Trigger asChild>
        <table
          css={tableStyles}
          onContextMenu={(e) => {
            const cell = (e.target as HTMLElement).closest("td,th");
            if (!cell) return;

            const rowIndex = (cell.parentElement as HTMLTableRowElement).rowIndex - 1;
            const colIndex = (cell as HTMLTableCellElement).cellIndex;

            setEditorState((prev) => ({
              ...prev,
              contextMenuCell: { blockIndex, contentIndex, rowIndex, colIndex },
            }));
          }}
        >
          <thead>
            <tr>
              {node.header.colSpec.map((col: ColumnSpec, colIdx: number) => (
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
              const isSelected =
                tableSelection &&
                tableSelection.blockIndex === blockIndex &&
                tableSelection.contentIndex === contentIndex &&
                tableSelection.rowIndex === rowIdx;

              return (
                <tr css={isSelected && selectedRowStyles} key={rowIdx}>
                  {row.cells.map((cell, cellIdx) => {
                    const isCellSelected =
                      editorState.contextMenuCell &&
                      editorState.contextMenuCell.blockIndex === blockIndex &&
                      editorState.contextMenuCell.contentIndex === contentIndex &&
                      editorState.contextMenuCell.rowIndex === rowIdx &&
                      editorState.contextMenuCell.colIndex === cellIdx;

                    return (
                      <td css={isCellSelected && cellSelectedStyles} key={cellIdx}>
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
      </CM.Trigger>

      <CM.Portal>
        <CM.Content css={menuContentStyles}>
          <CM.Item css={menuItemStyles} onSelect={() => applyAction(Actions.insertTableColumnLeft, setEditorState)}>
            Sett inn kolonne til venstre
          </CM.Item>
          <CM.Item css={menuItemStyles} onSelect={() => applyAction(Actions.insertTableColumnRight, setEditorState)}>
            Sett inn kolonne til høyre
          </CM.Item>

          <CM.Item css={menuItemStyles} onSelect={() => applyAction(Actions.insertTableRowAbove, setEditorState)}>
            Sett inn rad over
          </CM.Item>
          <CM.Item css={menuItemStyles} onSelect={() => applyAction(Actions.insertTableRowBelow, setEditorState)}>
            Sett inn rad under
          </CM.Item>
          <CM.Separator css={menuSeparatorStyles} />

          <CM.Sub>
            <CM.SubTrigger css={menuItemStyles}>
              Slett&nbsp;celler
              <ChevronRightIcon
                aria-hidden
                css={css`
                  margin-left: auto;
                `}
                fontSize="1rem"
              />
            </CM.SubTrigger>

            <CM.SubContent css={menuContentStyles} sideOffset={4}>
              <DeleteCellsSubMenu
                onDelete={(choice) => {
                  if (choice === "row") applyAction(Actions.removeTableRow, setEditorState);
                  if (choice === "col") applyAction(Actions.removeTableColumn, setEditorState);
                  if (choice === "table") applyAction(Actions.removeTable, setEditorState);
                }}
              />
            </CM.SubContent>
          </CM.Sub>
        </CM.Content>
      </CM.Portal>
    </CM.Root>
  );
};

export default TableView;
