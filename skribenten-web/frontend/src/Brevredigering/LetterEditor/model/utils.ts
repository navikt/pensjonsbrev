import { produce } from "immer";

import { text } from "~/Brevredigering/LetterEditor/actions/common";
import type { AnyBlock, Content, Item, TextContent } from "~/types/brevbakerTypes";
import { ITEM_LIST, LITERAL, VARIABLE } from "~/types/brevbakerTypes";

import { MergeTarget } from "../actions/merge";
import type { ContentGroup } from "./state";

export function isTextContent(content: Content): content is TextContent {
  return content?.type === LITERAL || content?.type === VARIABLE;
}

export function getMergeIds(sourceId: number, target: MergeTarget): [number, number] {
  switch (target) {
    case MergeTarget.PREVIOUS: {
      return [sourceId - 1, sourceId];
    }
    case MergeTarget.NEXT: {
      return [sourceId, sourceId + 1];
    }
  }
}

export function isEmptyContent(content: Content) {
  switch (content.type) {
    case VARIABLE:
    case LITERAL: {
      return text(content).trim().replaceAll("​", "").length === 0;
    }
    case ITEM_LIST: {
      return content.items.length === 1 && isEmptyItem(content.items[0]);
    }
  }
}

export function isEmptyContentGroup(group: ContentGroup) {
  return group.content.length === 1 && isEmptyContent(group.content[0]);
}

export function mergeContentArrays<T extends Content>(first: T[], second: T[]): T[];
export function mergeContentArrays(first: Content[], second: Content[]) {
  return produce(first, (draft) => {
    const lastContentOfFirst = draft[first.length - 1];
    const firstContentOfSecond = second[0];

    if (lastContentOfFirst.type === LITERAL && firstContentOfSecond.type === LITERAL) {
      // merge adjoining literals
      lastContentOfFirst.editedText =
        (lastContentOfFirst.editedText ?? lastContentOfFirst.text) +
        (firstContentOfSecond.editedText ?? firstContentOfSecond.text);
      draft.splice(first.length, 0, ...second.slice(1));
    } else {
      draft.splice(first.length, 0, ...second);
    }
  });
}

export function isEmptyItem(item: Item): boolean {
  return item.content.length === 0 || (item.content.length === 1 && isEmptyContent(item.content[0]));
}

export function isEmptyContentList(content: Content[]) {
  return content.length === 0 || (content.length === 1 && isEmptyContent(content[0]));
}
export function isEmptyBlock(block: AnyBlock): boolean {
  return isEmptyContentList(block.content);
}
