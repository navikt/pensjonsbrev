export type BlockContentIndex = { blockIndex: number; contentIndex: number };
export type ItemContentIndex = BlockContentIndex & {
  itemIndex: number;
  itemContentIndex: number;
};
export type LiteralIndex = BlockContentIndex | ItemContentIndex;
