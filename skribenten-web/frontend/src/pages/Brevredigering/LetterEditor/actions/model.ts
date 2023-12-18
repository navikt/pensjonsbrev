export type BlockContentId = { blockId: number; contentId: number };
export type ItemContentId = { blockId: number; contentId: number; itemId: number; itemContentId: number };
export type ContentId = BlockContentId | ItemContentId;

export enum MergeTarget {
  PREVIOUS = "PREVIOUS",
  NEXT = "NEXT",
}
