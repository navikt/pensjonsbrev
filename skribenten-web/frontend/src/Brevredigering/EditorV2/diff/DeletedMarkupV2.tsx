import { css } from "@emotion/react";

import {
  type BlockV2,
  type CellV2,
  type ColumnSpecV2,
  type ItemV2,
  type ListBlockV2,
  type RowV2,
  type TableV2,
  type TextV2,
} from "~/types/brevV2";

import { type ContainerPath } from "../model/paths";
import { fontTypeOf, textOf } from "../model/text";
import { useDeletedV2 } from "./AttestantDiffContextV2";
import {
  deletedBlocksAt,
  deletedCellsAt,
  deletedColumnsAt,
  deletedContentAt,
  deletedItemsAt,
  deletedRowsAt,
} from "./diffModel";

const deletedTableStyle = css`
  width: 100%;
  table-layout: fixed;
  border-collapse: collapse;
  margin-block: var(--ax-space-28);

  td,
  th {
    border-bottom: 1px solid var(--ax-border-neutral);
    padding: 2mm;
    overflow-wrap: break-word;
    text-align: left;
  }
`;

const DeletedText = ({ text }: { text: TextV2 }) => {
  if (text.type === "NEW_LINE") return <br />;
  const fontType = fontTypeOf(text);
  return (
    <span
      className="attestant-diff-deleted"
      css={{
        ...(fontType === "BOLD" && { fontWeight: "bold" }),
        ...(fontType === "ITALIC" && { fontStyle: "italic" }),
      }}
      data-diff-deleted
    >
      {textOf(text)}
    </span>
  );
};

const DeletedTexts = ({ content }: { content: readonly TextV2[] }) =>
  content.map((text, index) => <DeletedText key={index} text={text} />);

const DeletedItems = ({ items }: { items: readonly ItemV2[] }) =>
  items.map((item, index) => (
    <li data-diff-deleted key={index}>
      <DeletedTexts content={item.content} />
    </li>
  ));

const DeletedColumns = ({ columns }: { columns: readonly ColumnSpecV2[] }) =>
  columns.map((column, index) => (
    <th data-diff-deleted key={index} scope="col">
      <DeletedTexts content={column.content} />
    </th>
  ));

const DeletedCells = ({ cells }: { cells: readonly CellV2[] }) =>
  cells.map((cell, index) => (
    <td data-diff-deleted key={index}>
      <DeletedTexts content={cell.content} />
    </td>
  ));

const DeletedRows = ({ rows }: { rows: readonly RowV2[] }) =>
  rows.map((row, index) => (
    <tr data-diff-deleted key={index}>
      <DeletedCells cells={row.cells} />
    </tr>
  ));

const DeletedList = ({ list }: { list: ListBlockV2 }) => {
  const ListTag = list.type === "NUMBERED_LIST" ? "ol" : "ul";
  return (
    <ListTag contentEditable={false} data-diff-deleted-block>
      <DeletedItems items={list.items} />
    </ListTag>
  );
};

const DeletedTable = ({ table }: { table: TableV2 }) => (
  <table contentEditable={false} css={deletedTableStyle} data-diff-deleted-block>
    <thead>
      <tr>
        <DeletedColumns columns={table.header.colSpec} />
      </tr>
    </thead>
    <tbody>
      <DeletedRows rows={table.rows} />
    </tbody>
  </table>
);

const DeletedBlock = ({ block }: { block: BlockV2 }) => {
  switch (block.type) {
    case "PARAGRAPH":
    case "TITLE2":
    case "TITLE3":
    case "TITLE4":
      return (
        <div className={block.type} contentEditable={false} data-diff-deleted-block>
          <DeletedTexts content={block.content} />
        </div>
      );
    case "ITEM_LIST":
    case "NUMBERED_LIST":
      return <DeletedList list={block} />;
    case "TABLE":
      return <DeletedTable table={block} />;
  }
};

type TrailingProps = { trailing?: boolean };

export const DeletedBlocksAtV2 = ({ block, trailing = false }: { block: number } & TrailingProps) =>
  useDeletedV2((diff) => deletedBlocksAt(diff, block, trailing)).map((deleted, index) => (
    <DeletedBlock block={deleted} key={index} />
  ));

export const DeletedContentAtV2 = ({
  path,
  content,
  trailing = false,
}: { path: ContainerPath; content: number } & TrailingProps) => (
  <DeletedTexts content={useDeletedV2((diff) => deletedContentAt(diff, path, content, trailing))} />
);

export const DeletedItemsAtV2 = ({
  block,
  item,
  trailing = false,
}: { block: number; item: number } & TrailingProps) => (
  <DeletedItems items={useDeletedV2((diff) => deletedItemsAt(diff, block, item, trailing))} />
);

export const DeletedColumnsAtV2 = ({
  block,
  column,
  trailing = false,
}: { block: number; column: number } & TrailingProps) => (
  <DeletedColumns columns={useDeletedV2((diff) => deletedColumnsAt(diff, block, column, trailing))} />
);

export const DeletedRowsAtV2 = ({ block, row, trailing = false }: { block: number; row: number } & TrailingProps) => (
  <DeletedRows rows={useDeletedV2((diff) => deletedRowsAt(diff, block, row, trailing))} />
);

export const DeletedCellsAtV2 = ({
  block,
  row,
  cell,
  trailing = false,
}: { block: number; row: number; cell: number } & TrailingProps) => (
  <DeletedCells cells={useDeletedV2((diff) => deletedCellsAt(diff, block, row, cell, trailing))} />
);
