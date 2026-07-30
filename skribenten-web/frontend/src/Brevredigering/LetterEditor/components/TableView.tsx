import { css } from "@emotion/react";
import { ArrowRightLeftIcon, PlusIcon, TrashIcon } from "@navikt/aksel-icons";
import { ActionMenu } from "@navikt/ds-react";
import React, { useState } from "react";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { DeletedCellContentAt, DeletedCellsAt, DeletedRowsAt } from "~/Brevredigering/LetterEditor/diff/DeletedMarkup";
import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { applyAction } from "~/Brevredigering/LetterEditor/lib/actions";
import { type Cell as CellType, type ColumnSpec, type Table } from "~/types/brevbakerTypes";

import { type TableCellIndex } from "../model/state";
import { isEmptyTableHeader } from "../model/utils";
import { TableCellContent } from "./TableCellContent";
import TableContextMenu from "./TableContextMenu";

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

const selectedBackgroundStyle = css`
  && {
    background: var(--ax-bg-accent-soft);
  }
`;

// TODO: render <ContentGroup> once that component
// can accept TableCellIndex (rowIndex/cellIndex)
const CellText = ({ cell, index }: { cell: CellType; index: TableCellIndex }) => (
  <>
    {cell.text.map((txt, i) => (
      <React.Fragment key={i}>
        <DeletedCellContentAt
          blockIndex={index.blockIndex}
          cellContentIndex={i}
          cellIndex={index.cellIndex}
          contentIndex={index.contentIndex}
          rowIndex={index.rowIndex}
        />
        <TableCellContent content={txt} tableCellIndex={{ ...index, cellContentIndex: i }} />
      </React.Fragment>
    ))}
    <DeletedCellContentAt
      blockIndex={index.blockIndex}
      cellContentIndex={cell.text.length}
      cellIndex={index.cellIndex}
      contentIndex={index.contentIndex}
      rowIndex={index.rowIndex}
      trailing
    />
  </>
);

const TableView: React.FC<{
  node: Table;
  blockIndex: number;
  contentIndex: number;
}> = ({ node, blockIndex, contentIndex }) => {
  const { setEditorState } = useEditor();
  const [menuAnchor, setMenuAnchor] = useState<{ x: number; y: number } | null>(null);
  const [highlight, setHighlight] = useState<{ row: number; col: number } | null>(null);

  const [menuTarget, setMenuTarget] = useState<{ rowIndex: number; colIndex: number } | null>(null);

  const headerHasContent = !isEmptyTableHeader(node.header);
  const canPromoteHeader = !headerHasContent;

  const headerColCount = node.header.colSpec.length;
  const onlyOneCol = headerColCount <= 1;

  const clickedRow = menuTarget?.rowIndex;
  const isHeaderCtx = clickedRow === -1;

  return (
    <>
      <table
        css={tableStyles}
        data-testid="letter-table"
        onContextMenu={(e) => {
          e.preventDefault();

          const cell = (e.target as HTMLElement).closest("td,th") as HTMLTableCellElement | null;
          if (!cell) return;
          const rowEl = cell.parentElement as HTMLTableRowElement;

          const isHeaderCell = cell.tagName === "TH" || rowEl.parentElement?.tagName === "THEAD";
          const rowIndex = isHeaderCell ? -1 : rowEl.rowIndex - 1;
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
          setMenuTarget({ rowIndex, colIndex });
          setHighlight({ row: rowIndex, col: colIndex });
          setMenuAnchor({ x: e.clientX, y: e.clientY });
        }}
      >
        <thead>
          <tr>
            {node.header.colSpec.map((col: ColumnSpec, colIndex) => {
              const isHeaderHighlighted =
                !!highlight && highlight.row === -1 && (highlight.col === -1 || highlight.col === colIndex);
              return (
                <React.Fragment key={colIndex}>
                  <DeletedCellsAt
                    asHeader
                    blockIndex={blockIndex}
                    cellIndex={colIndex}
                    contentIndex={contentIndex}
                    rowIndex={-1}
                  />
                  <th
                    css={isHeaderHighlighted && selectedBackgroundStyle}
                    data-testid={`table-header-${colIndex}`}
                    scope="col"
                  >
                    <CellText
                      cell={col.headerContent}
                      index={{
                        blockIndex,
                        contentIndex,
                        rowIndex: -1,
                        cellIndex: colIndex,
                        cellContentIndex: 0,
                      }}
                    />
                  </th>
                </React.Fragment>
              );
            })}
            <DeletedCellsAt
              asHeader
              blockIndex={blockIndex}
              cellIndex={node.header.colSpec.length}
              contentIndex={contentIndex}
              rowIndex={-1}
              trailing
            />
          </tr>
        </thead>
        <tbody>
          {node.rows.map((row, rowIndex) => {
            return (
              <React.Fragment key={rowIndex}>
                <DeletedRowsAt blockIndex={blockIndex} contentIndex={contentIndex} rowIndex={rowIndex} />
                <tr data-testid={`table-row-${rowIndex}`}>
                  {row.cells.map((cell, cellIndex) => {
                    const isHighlighted =
                      !!highlight &&
                      highlight.row === rowIndex &&
                      (highlight.col === -1 || highlight.col === cellIndex);
                    return (
                      <React.Fragment key={cellIndex}>
                        <DeletedCellsAt
                          blockIndex={blockIndex}
                          cellIndex={cellIndex}
                          contentIndex={contentIndex}
                          rowIndex={rowIndex}
                        />
                        <td
                          css={isHighlighted && selectedBackgroundStyle}
                          data-testid={`table-cell-${rowIndex}-${cellIndex}`}
                        >
                          <CellText
                            cell={cell}
                            index={{
                              blockIndex,
                              contentIndex,
                              rowIndex: rowIndex,
                              cellIndex: cellIndex,
                              cellContentIndex: 0,
                            }}
                          />
                        </td>
                      </React.Fragment>
                    );
                  })}
                  <DeletedCellsAt
                    blockIndex={blockIndex}
                    cellIndex={row.cells.length}
                    contentIndex={contentIndex}
                    rowIndex={rowIndex}
                    trailing
                  />
                </tr>
              </React.Fragment>
            );
          })}
          <DeletedRowsAt blockIndex={blockIndex} contentIndex={contentIndex} rowIndex={node.rows.length} trailing />
        </tbody>
      </table>

      <TableContextMenu
        anchor={menuAnchor}
        data-testid="table-action-menu"
        onClose={() => {
          setMenuAnchor(null);
          setHighlight(null);
          setMenuTarget(null);
        }}
      >
        {canPromoteHeader && typeof clickedRow === "number" && clickedRow >= 0 && (
          <ActionMenu.Item
            icon={<ArrowRightLeftIcon fontSize="1.25rem" />}
            onMouseEnter={() => setHighlight({ row: clickedRow, col: -1 })}
            onMouseLeave={() =>
              setHighlight(menuTarget ? { row: menuTarget.rowIndex, col: menuTarget.colIndex } : null)
            }
            onSelect={() => {
              const rowIndex = menuTarget?.rowIndex ?? -1;
              if (rowIndex >= 0) {
                applyAction(Actions.promoteRowToHeader, setEditorState, blockIndex, contentIndex, rowIndex);
                setMenuTarget(null);
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
              setHighlight(menuTarget ? { row: menuTarget.rowIndex, col: menuTarget.colIndex } : null)
            }
            onSelect={() => {
              applyAction(Actions.demoteHeaderToRow, setEditorState, blockIndex, contentIndex);
              setMenuTarget(null);
            }}
          >
            Gjør overskrift til vanlig tekst
          </ActionMenu.Item>
        )}
        {isHeaderCtx && headerHasContent && <ActionMenu.Divider />}
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
