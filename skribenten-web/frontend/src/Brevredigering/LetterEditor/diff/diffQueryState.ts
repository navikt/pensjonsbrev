import { type Content, type EditedLetter, type Identifiable, type TextContent } from "~/types/brevbakerTypes";

export type HashBoundValue<T> = {
  value: T;
  redigertBrevHash: string;
};

const identity = ({ id, parentId }: Identifiable) => [id ?? null, parentId ?? null];

const textStructure = (content: TextContent) => [content.type, ...identity(content)];

const contentStructure = (content: Content): unknown => {
  switch (content.type) {
    case "ITEM_LIST":
      return [
        content.type,
        ...identity(content),
        content.editedListType ?? content.listType,
        content.items.map((item) => [...identity(item), item.content.map(textStructure)]),
      ];
    case "TABLE":
      return [
        content.type,
        ...identity(content),
        [
          ...identity(content.header),
          content.header.colSpec.map((column) => [
            ...identity(column),
            column.span,
            column.alignment,
            [...identity(column.headerContent), column.headerContent.text.map(textStructure)],
          ]),
        ],
        content.rows.map((row) => [
          ...identity(row),
          row.cells.map((cell) => [...identity(cell), cell.text.map(textStructure)]),
        ]),
      ];
    default:
      return textStructure(content);
  }
};

export function letterStructureSignature(letter: EditedLetter): string {
  return JSON.stringify(
    letter.blocks.map((block) => [
      block.type,
      ...identity(block),
      block.missingFromTemplate,
      block.content.map(contentStructure),
    ]),
  );
}

export function getSnapshotForHash<T>(
  snapshotsByHash: ReadonlyMap<string, T>,
  redigertBrevHash: string,
): T | undefined {
  return snapshotsByHash.get(redigertBrevHash);
}

export function pickValueForCurrentHash<T>(
  response: HashBoundValue<T> | undefined,
  currentSavedHash: string,
): T | undefined {
  if (!response) return undefined;
  if (response.redigertBrevHash !== currentSavedHash) return undefined;
  return response.value;
}

export function shouldRenderDiffMarkers<T>({
  visDiff,
  currentSavedHash,
  invalidatedDiffHashes,
  diff,
}: {
  visDiff: boolean;
  currentSavedHash: string;
  invalidatedDiffHashes: ReadonlySet<string>;
  diff: T | undefined;
}): boolean {
  return visDiff && diff !== undefined && !invalidatedDiffHashes.has(currentSavedHash);
}
