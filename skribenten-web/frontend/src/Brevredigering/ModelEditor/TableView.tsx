import { css } from "@emotion/react";
import React from "react";

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

const TableView: React.FC<{
  node: Table;
  blockIndex: number;
  contentIndex: number;
}> = ({ node, blockIndex, contentIndex }) => (
  <table css={tableStyles}>
    <tbody>
      {node.rows.map((row, rowIndex) => (
        <tr key={rowIndex}>
          {row.cells.map((cell, cellIndex) => (
            <td key={cellIndex}>
              {cell.text
                .filter((textItem) => textItem.type === LITERAL)
                .map((literal, literalIndex) => (
                  <TableCellContent
                    key={literalIndex}
                    lit={literal}
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
      ))}
    </tbody>
  </table>
);

export default TableView;
