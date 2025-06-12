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
  NewLine,
  ParagraphBlock,
  TextContent,
  TITLE1,
  Title1Block,
  TITLE2,
  Title2Block,
  VariableValue,
} from "~/types/brevbakerTypes";
import { FontType, ITEM_LIST, LITERAL, NEW_LINE, PARAGRAPH, VARIABLE } from "~/types/brevbakerTypes";
import type { Nullable } from "~/types/Nullable";

import type { BlockContentIndex, Focus, ItemContentIndex, LetterEditorState, LiteralIndex } from "../model/state";

export function cleanseText(text: string): string {
  return text.replaceAll("<br>", "").replaceAll("&nbsp;", " ").replaceAll("\n", " ").replaceAll("\r", "");
}

export function isEditableContent(content: Content | undefined | null): boolean {
  return content != null && (content.type === VARIABLE || content.type === ITEM_LIST);
}

export function isBlockContentIndex(f: Focus | LiteralIndex): f is BlockContentIndex {
  return !isItemContentIndex(f);
}

export function isItemContentIndex(f: Focus | LiteralIndex): f is ItemContentIndex {
  return "itemIndex" in f && f.itemIndex !== undefined && "itemContentIndex" in f && f.itemContentIndex !== undefined;
}

export function isAtStartOfBlock(f: Focus, offset?: number): boolean {
  return (
    f.contentIndex === 0 &&
    (offset ?? f.cursorPosition) === 0 &&
    (isItemContentIndex(f) ? f.itemIndex === 0 && f.itemContentIndex === 0 : true)
  );
}

export function text<T extends TextContent | undefined>(
  content: T,
): string | (undefined extends T ? undefined : never) {
  if (content?.type === LITERAL) {
    return content.editedText ?? content.text;
  } else if (content?.type === VARIABLE || content?.type === NEW_LINE) {
    return content.text;
  } else {
    return undefined as undefined extends T ? undefined : never;
  }
}

export function fontTypeOf(content: TextContent): FontType {
  switch (content.type) {
    case "NEW_LINE":
      return FontType.PLAIN;
    case "LITERAL":
      return content.editedFontType ?? content.fontType;
    case "VARIABLE":
      return content.fontType;
  }
}

export function isNew(obj: Identifiable): boolean {
  return obj.id === null;
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

  const reverseSearchNonMatching = from.slice(0, atIndex).findLastIndex((c) => !predicate(c));
  const forwardSearchNonMatching = from.slice(atIndex + 1).findIndex((c) => !predicate(c));

  const startIndex = reverseSearchNonMatching >= 0 ? reverseSearchNonMatching + 1 : 0;
  const endIndex = forwardSearchNonMatching >= 0 ? atIndex + forwardSearchNonMatching : from.length - 1;

  return {
    startIndex,
    endIndex,
    count: endIndex - startIndex + 1,
  };
}

export function mergeLiteralsIfPossible<T extends Identifiable>(first: Draft<T>, second: Draft<T>): Draft<T[]> {
  if (
    isLiteral(first) &&
    isLiteral(second) &&
    !isFritekst(first) &&
    !isFritekst(second) &&
    fontTypeOf(first) === fontTypeOf(second)
  ) {
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

/**
 * Splits literal text at given offset by updating 'editedText' of the given literal and returning a new literal.
 *
 * @param literal the literal to update
 * @param offset the offset to split at
 * @returns a new literal
 */
export function splitLiteralAtOffset(literal: Draft<LiteralValue>, offset: number): LiteralValue {
  const origText = text(literal);
  const newText = cleanseText(origText.slice(0, Math.max(0, offset)));
  const nextText = cleanseText(origText.slice(Math.max(0, offset)));

  updateLiteralText(literal, newText);

  return newLiteral({ editedText: nextText, fontType: literal.fontType, editedFontType: literal.editedFontType });
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
  parentId?: Nullable<number>;
  content: Content[];
  deletedContent?: number[];
}): ParagraphBlock {
  return {
    type: PARAGRAPH,
    id: args.id ?? null,
    parentId: args.parentId ?? null,
    editable: true,
    deletedContent: args.deletedContent ?? [],
    content: args.content,
  };
}

export function newLiteral(args: {
  id?: Nullable<number>;
  parentId?: Nullable<number>;
  text?: string;
  editedText?: Nullable<string>;
  fontType?: Nullable<FontType>;
  // TODO: Gir ikke mening å sette editedFontType i nye literals.
  editedFontType?: Nullable<FontType>;
  tags?: ElementTags[];
}): LiteralValue {
  return {
    type: LITERAL,
    id: args.id ?? null,
    parentId: args.parentId ?? null,
    text: args.text ?? "",
    editedText: args.editedText ?? null,
    editedFontType: args.editedFontType ?? null,
    fontType: args.fontType ?? FontType.PLAIN,
    tags: args.tags ?? [],
  };
}

export const newVariable = (args: {
  id?: Nullable<number>;
  text: string;
  parentId?: Nullable<number>;
  fontType?: FontType;
}): VariableValue => {
  return {
    type: VARIABLE,
    id: args.id ?? null,
    parentId: args.parentId ?? null,
    text: args.text,
    fontType: args.fontType ?? FontType.PLAIN,
  };
};

export function newItem({ id, content }: { id?: Nullable<number>; content: TextContent[] }): Item {
  return {
    id: id ?? null,
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

export function createNewLine(): NewLine {
  return {
    id: null,
    parentId: null,
    type: NEW_LINE,
    text: "",
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
