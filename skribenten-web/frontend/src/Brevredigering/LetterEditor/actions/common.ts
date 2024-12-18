import type { Draft } from "immer";

import { MergeTarget } from "~/Brevredigering/LetterEditor/actions/merge";
import { updateLiteralText } from "~/Brevredigering/LetterEditor/actions/updateContentText";
import { isFritekst, isLiteral } from "~/Brevredigering/LetterEditor/model/utils";
import type { BrevResponse } from "~/types/brev";
import type {
  Content,
  ElementTags,
  Identifiable,
  Item,
  ItemList,
  LiteralValue,
  ParagraphBlock,
  TextContent,
  TITLE1,
  Title1Block,
  TITLE2,
  Title2Block,
  VariableValue,
} from "~/types/brevbakerTypes";
import { ITEM_LIST, LITERAL, PARAGRAPH, VARIABLE } from "~/types/brevbakerTypes";
import type { Nullable } from "~/types/Nullable";

import type { LetterEditorState } from "../model/state";

export function cleanseText(text: string): string {
  return text.replaceAll("<br>", "").replaceAll("&nbsp;", " ").replaceAll("\n", " ").replaceAll("\r", "");
}

export function isEditableContent(content: Content | undefined | null): boolean {
  return content != null && (content.type === VARIABLE || content.type === ITEM_LIST);
}

export function text<T extends LiteralValue | VariableValue | undefined>(
  content: T,
): string | (undefined extends T ? undefined : never) {
  if (content?.type === LITERAL) {
    return content.editedText ?? content.text;
  } else if (content?.type === VARIABLE) {
    return content.text;
  } else {
    return undefined as undefined extends T ? undefined : never;
  }
}

export function create(brev: BrevResponse): LetterEditorState {
  return {
    info: brev.info,
    redigertBrev: brev.redigertBrev,
    redigertBrevHash: brev.redigertBrevHash,
    isDirty: false,
    focus: { blockIndex: 0, contentIndex: 0 },
  };
}

// TODO: skriv en test som verifiserer at kun elementer som har matchende parentId til from blir lagt til i deleted.
export function removeElements<T extends Identifiable>(
  startIndex: number,
  count: number,
  from: { content: Draft<T[]>; deletedContent: Draft<number[]>; id: number | null },
): Draft<T[]> {
  const removedElements = from.content.splice(startIndex, count);
  for (const e of removedElements) deleteElement(e, from);
  return removedElements;
}

function deleteElement(
  toDelete: Identifiable,
  from: { content: Identifiable[]; deletedContent: Draft<number[]>; id: number | null },
) {
  if (
    toDelete.id !== null &&
    toDelete.parentId === from.id &&
    !from.deletedContent.includes(toDelete.id) &&
    !from.content.map((c) => c.id).includes(toDelete.id)
  ) {
    from.deletedContent.push(toDelete.id);
  }
}

export function addElements<T extends Identifiable, E extends T>(
  elements: E[],
  toIndex: number,
  to: Draft<T[]>,
  deleted: Draft<number[]>,
): void;
export function addElements<T extends Identifiable, E extends T>(
  elements: Draft<E[]>,
  toIndex: number,
  to: Draft<T[]>,
  deleted: Draft<number[]>,
): void {
  if (toIndex > 0) {
    const elementBeforeStartIndex = to[toIndex - 1];
    const firstElementToAdd = elements[0];

    to.splice(
      toIndex - 1,
      1,
      ...mergeLiteralsIfPossible(elementBeforeStartIndex, firstElementToAdd),
      ...elements.slice(1),
    );
  } else {
    to.splice(toIndex, 0, ...elements);
  }

  const presentIds = to.map((e) => e.id).filter((id) => id !== null) as number[];
  for (const id of presentIds) {
    const index = deleted.indexOf(id);
    if (index !== -1) {
      deleted.splice(index, 1);
    }
  }
}

export function findAdjoiningContent<T extends Content, S extends T>(
  atIndex: number,
  from: T[],
  predicate: (value: T) => value is S,
): { startIndex: number; endIndex: number; count: number } {
  if (from.length === 0) {
    return { startIndex: 0, endIndex: 0, count: 0 };
  }

  let countBefore = 0;
  for (let i = atIndex - 1; i >= 0; i--) {
    if (predicate(from[i])) {
      countBefore++;
    } else {
      break;
    }
  }
  let countAfter = 0;
  for (let i = atIndex + 1; i < from.length; i++) {
    if (predicate(from[i])) {
      countAfter++;
    } else {
      break;
    }
  }

  return {
    startIndex: atIndex - countBefore,
    endIndex: atIndex + countAfter,
    count: countBefore + 1 + countAfter,
  };
}

export function mergeLiteralsIfPossible<T extends Identifiable>(first: Draft<T>, second: Draft<T>): Draft<T[]> {
  // TODO: Legg til sjekk for `first.fontType === second.fontType`
  if (isLiteral(first) && isLiteral(second) && !isFritekst(first) && !isFritekst(second)) {
    const mergedText = text(first) + text(second);
    if (first.id !== null && second.id !== null) {
      return [first, second];
    } else if (first.id === null) {
      updateLiteralText(second, mergedText);
      return [second];
    } else {
      updateLiteralText(first, mergedText);
      return [first];
    }
  } else {
    return [first, second];
  }
}

export function newTitle(args: {
  id?: Nullable<number>;
  content: TextContent[];
  type: typeof TITLE1 | typeof TITLE2;
  deletedContent?: number[];
}): Title1Block | Title2Block {
  return {
    type: args.type,
    id: args.id ?? null,
    parentId: null,
    editable: true,
    deletedContent: args.deletedContent ?? [],
    content: args.content,
  };
}

export function newParagraph(args: {
  id?: Nullable<number>;
  content: Content[];
  deletedContent?: number[];
}): ParagraphBlock {
  return {
    type: PARAGRAPH,
    id: args.id ?? null,
    parentId: null,
    editable: true,
    deletedContent: args.deletedContent ?? [],
    content: args.content,
  };
}

export function newLiteral(args: {
  id?: Nullable<number>;
  text: string;
  editedText?: Nullable<string>;
  tags?: ElementTags[];
}): LiteralValue {
  return {
    type: LITERAL,
    id: args.id ?? null,
    parentId: null,
    text: args.text,
    editedText: args.editedText ?? null,
    tags: args.tags ?? [],
  };
}

export function newItem({ content }: { content: TextContent[] }): Item {
  return {
    id: null,
    parentId: null,
    content,
    deletedContent: [],
  };
}

export function newItemList(args: { id?: Nullable<number>; items: Item[]; deletedItems?: number[] }): ItemList {
  return {
    id: args.id ?? null,
    parentId: null,
    type: "ITEM_LIST",
    items: args.items,
    deletedItems: args.deletedItems ?? [],
  };
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
