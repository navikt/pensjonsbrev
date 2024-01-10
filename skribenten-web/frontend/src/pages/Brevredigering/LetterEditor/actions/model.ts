export type BlockContentIndex = { blockIndex: number; contentIndex: number };
export type ItemContentIndex = {
  blockIndex: number;
  contentIndex: number;
  itemIndex: number;
  itemContentIndex: number;
};
export type ContentIndex = BlockContentIndex | ItemContentIndex;

export enum MergeTarget {
  PREVIOUS = "PREVIOUS",
  NEXT = "NEXT",
}
