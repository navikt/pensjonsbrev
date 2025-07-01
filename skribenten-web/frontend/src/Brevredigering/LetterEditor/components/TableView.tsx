import { css } from "@emotion/react";
import { Table as AkselTable } from "@navikt/ds-react";

import type { LiteralValue, Table as TableNode, TextContent, VariableValue } from "~/types/brevbakerTypes";

function renderText(textArr: TextContent[]) {
  return textArr
    .map((t) => {
      switch (t.type) {
        case "LITERAL":
          return (t as LiteralValue).editedText ?? t.text;
        case "VARIABLE":
          return (t as VariableValue).text;
        default:
          return "";
      }
    })
    .join("");
}

export const TableView = ({ node }: { node: TableNode }) => (
  <AkselTable
    css={css`
      td,
      th {
        border: 1px solid var(--a-gray-300);
      }
    `}
    size="medium"
  >
    <AkselTable.Header>
      <AkselTable.Row>
        {node.header.colSpec.map((col, idx) => (
          <AkselTable.ColumnHeader align={col.alignment === "RIGHT" ? "right" : "left"} key={idx}>
            {renderText(col.headerContent.text)}
          </AkselTable.ColumnHeader>
        ))}
      </AkselTable.Row>
    </AkselTable.Header>

    <AkselTable.Body>
      {node.rows.map((row, rIdx) => (
        <AkselTable.Row key={rIdx}>
          {row.cells.map((cell, cIdx) => (
            <AkselTable.DataCell key={cIdx}>{renderText(cell.text)}</AkselTable.DataCell>
          ))}
        </AkselTable.Row>
      ))}
    </AkselTable.Body>
  </AkselTable>
);
