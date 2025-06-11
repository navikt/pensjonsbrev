import { css } from "@emotion/react";
import React from "react";

import type { Table } from "~/types/brevbakerTypes";

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

const TableView: React.FC<{ node: Table; blockIndex: number }> = ({ node, blockIndex }) => (
  <table css={tableStyles}>
    <tbody>
      {node.rows.map((row, rIdx) => (
        <tr key={rIdx}>
          {row.cells.map((cell, cIdx) => (
            <td key={cIdx}>
              {cell.text.map((lit, idx) => (
                <TableCellContent
                  key={idx}
                  lit={lit}
                  litIndex={{
                    blockIndex,
                    contentIndex: rIdx,
                    itemIndex: cIdx,
                    itemContentIndex: idx,
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
