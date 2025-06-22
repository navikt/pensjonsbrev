import { css } from "@emotion/react";
import React from "react";

import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { LITERAL, type Table } from "~/types/brevbakerTypes";

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
`;

const selectedRowStyles = css`
  background: var(--a-surface-info-subtle, #d0e7ff); /* light blue */
`;

const TableView: React.FC<{
  node: Table;
  blockIndex: number;
  contentIndex: number;
}> = ({ node, blockIndex, contentIndex }) => {
  const { editorState } = useEditor();
  const { tableSelection } = editorState;

  return (
    <table css={tableStyles}>
      <tbody>
        {node.rows.map((row, rowIndex) => {
          const isSelected =
            tableSelection &&
            tableSelection.blockIndex === blockIndex &&
            tableSelection.contentIndex === contentIndex &&
            tableSelection.rowIndex === rowIndex;

          return (
            <tr css={isSelected && selectedRowStyles} key={rowIndex}>
              {row.cells.map((cell, cellIndex) => (
                <td key={cellIndex}>
                  {cell.text
                    .filter((textContent) => textContent.type === LITERAL)
                    .map((litValue, litIdx) => (
                      <TableCellContent
                        key={litIdx}
                        lit={litValue}
                        litIndex={{
                          blockIndex,
                          contentIndex,
                          itemIndex: rowIndex,
                          itemContentIndex: cellIndex,
                        }}
                      />
                    ))}
                </td>
              ))}
            </tr>
          );
        })}
      </tbody>
    </table>
  );
};

export default TableView;
