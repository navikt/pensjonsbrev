import { css } from "@emotion/react";

import { text as textOf } from "~/Brevredigering/LetterEditor/actions/common";
import {
  useDeletedBlocks,
  useDeletedCellContent,
  useDeletedCells,
  useDeletedContent,
  useDeletedItemContent,
  useDeletedItems,
  useDeletedRows,
} from "~/Brevredigering/LetterEditor/diff/AttestantDiffContext";
import { effectiveListType } from "~/Brevredigering/LetterEditor/model/utils";
import {
  type AnyBlock,
  type Cell,
  type Content,
  type Item,
  ListType,
  type Row,
  type TextContent,
} from "~/types/brevbakerTypes";

/**
 * Read-only rendering of markup that the saksbehandler removed entirely. The nodes only exist to be
 * looked at: they are never part of `editorState.redigertBrev`, are not editable, and are skipped by
 * the caret utilities since they carry no `contenteditable`.
 */

const deletedTextStyle = css`
  color: var(--ax-text-danger);
  background: var(--ax-bg-danger-moderate-pressedA);
  border-radius: 2px;
  font-weight: 600;
  text-decoration-line: line-through;
  user-select: none;
`;

const deletedBlockStyle = css`
  ${deletedTextStyle};
  background: none;
  text-decoration-line: none;
`;

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

const DeletedText = ({ content }: { content: TextContent }) => {
  if (content.type === "NEW_LINE") return <br />;
  return (
    <span css={deletedTextStyle} data-diff-deleted>
      {textOf(content)}
    </span>
  );
};

/** Inline text content of a deleted item or table cell. */
export const DeletedTextNodes = ({ content }: { content: TextContent[] }) => {
  if (content.length === 0) return null;
  return content.map((node, index) => <DeletedText content={node} key={index} />);
};

const DeletedTable = ({ table }: { table: Extract<Content, { type: "TABLE" }> }) => (
  <table css={deletedTableStyle} data-diff-deleted>
    <thead>
      <tr>
        {table.header.colSpec.map((col, colIndex) => (
          <th key={colIndex} scope="col">
            <DeletedTextNodes content={col.headerContent.text} />
          </th>
        ))}
      </tr>
    </thead>
    <tbody>
      <DeletedRows rows={table.rows} />
    </tbody>
  </table>
);

const DeletedContentNode = ({ content }: { content: Content }) => {
  switch (content.type) {
    case "LITERAL":
    case "VARIABLE":
    case "NEW_LINE": {
      return <DeletedText content={content} />;
    }
    case "ITEM_LIST": {
      const ListTag = effectiveListType(content) === ListType.PUNKTLISTE ? "ul" : "ol";
      return (
        <ListTag data-diff-deleted>
          <DeletedItems items={content.items} />
        </ListTag>
      );
    }
    case "TABLE": {
      return <DeletedTable table={content} />;
    }
    default: {
      return null;
    }
  }
};

/** Paragraph content that was removed entirely from a still-existing block. */
export const DeletedContentNodes = ({ content }: { content: Content[] }) => {
  if (content.length === 0) return null;
  return content.map((node, index) => <DeletedContentNode content={node} key={index} />);
};

/** Items that were removed entirely from a still-existing item list. */
export const DeletedItems = ({ items }: { items: Item[] }) => {
  if (items.length === 0) return null;
  return items.map((item, index) => (
    <li data-diff-deleted key={index}>
      <DeletedTextNodes content={item.content} />
    </li>
  ));
};

/** Rows that were removed entirely from a still-existing table. */
export const DeletedRows = ({ rows }: { rows: Row[] }) => {
  if (rows.length === 0) return null;
  return rows.map((row, index) => (
    <tr data-diff-deleted key={index}>
      <DeletedCells cells={row.cells} />
    </tr>
  ));
};

/** Cells that were removed entirely from a still-existing row. */
export const DeletedCells = ({ cells }: { cells: Cell[] }) => {
  if (cells.length === 0) return null;
  return cells.map((cell, index) => (
    <td data-diff-deleted key={index}>
      <DeletedTextNodes content={cell.text} />
    </td>
  ));
};

/** Blocks that were removed entirely from the letter. */
export const DeletedBlocks = ({ blocks }: { blocks: AnyBlock[] }) => {
  if (blocks.length === 0) return null;
  return blocks.map((block, index) => (
    <div className={block.type} contentEditable={false} css={deletedBlockStyle} data-diff-deleted-block key={index}>
      <DeletedContentNodes content={block.content} />
    </div>
  ));
};

/**
 * Connectors that look up the deleted markup for a unified position in the active diff.
 * `trailing` collects everything from the given index and onwards, and is used once after the last
 * surviving sibling so deletions at the very end are not dropped.
 */

type TrailingProps = { trailing?: boolean };

export const DeletedBlocksAt = ({ blockIndex, trailing = false }: { blockIndex: number } & TrailingProps) => (
  <DeletedBlocks blocks={useDeletedBlocks(blockIndex, trailing)} />
);

export const DeletedContentAt = ({
  blockIndex,
  contentIndex,
  trailing = false,
}: { blockIndex: number; contentIndex: number } & TrailingProps) => (
  <DeletedContentNodes content={useDeletedContent(blockIndex, contentIndex, trailing)} />
);

export const DeletedItemsAt = ({
  blockIndex,
  contentIndex,
  itemIndex,
  trailing = false,
}: { blockIndex: number; contentIndex: number; itemIndex: number } & TrailingProps) => (
  <DeletedItems items={useDeletedItems(blockIndex, contentIndex, itemIndex, trailing)} />
);

export const DeletedItemContentAt = ({
  blockIndex,
  contentIndex,
  itemIndex,
  itemContentIndex,
  trailing = false,
}: { blockIndex: number; contentIndex: number; itemIndex: number; itemContentIndex: number } & TrailingProps) => (
  <DeletedTextNodes content={useDeletedItemContent(blockIndex, contentIndex, itemIndex, itemContentIndex, trailing)} />
);

export const DeletedRowsAt = ({
  blockIndex,
  contentIndex,
  rowIndex,
  trailing = false,
}: { blockIndex: number; contentIndex: number; rowIndex: number } & TrailingProps) => (
  <DeletedRows rows={useDeletedRows(blockIndex, contentIndex, rowIndex, trailing)} />
);

export const DeletedCellsAt = ({
  blockIndex,
  contentIndex,
  rowIndex,
  cellIndex,
  trailing = false,
}: { blockIndex: number; contentIndex: number; rowIndex: number; cellIndex: number } & TrailingProps) => (
  <DeletedCells cells={useDeletedCells(blockIndex, contentIndex, rowIndex, cellIndex, trailing)} />
);

export const DeletedCellContentAt = ({
  blockIndex,
  contentIndex,
  rowIndex,
  cellIndex,
  cellContentIndex,
  trailing = false,
}: {
  blockIndex: number;
  contentIndex: number;
  rowIndex: number;
  cellIndex: number;
  cellContentIndex: number;
} & TrailingProps) => (
  <DeletedTextNodes
    content={useDeletedCellContent(blockIndex, contentIndex, rowIndex, cellIndex, cellContentIndex, trailing)}
  />
);
