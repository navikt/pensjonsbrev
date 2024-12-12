import type { Draft } from "immer";

import type { BrevResponse } from "~/types/brev";
import type {
  Content,
  ElementTags,
  Identifiable,
  Item,
  ItemList,
  LiteralValue,
  ParagraphBlock,
  VariableValue,
} from "~/types/brevbakerTypes";
import type { Nullable } from "~/types/Nullable";

import { ITEM_LIST, LITERAL, PARAGRAPH, VARIABLE } from "../../../types/brevbakerTypes";
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

export function deleteElement(toDelete: Identifiable, verifyNotPresent: Identifiable[], deleted: Draft<number[]>) {
  if (
    toDelete.id !== null &&
    !deleted.includes(toDelete.id) &&
    !verifyNotPresent.map((b) => b.id).includes(toDelete.id)
  ) {
    deleted.push(toDelete.id);
  }
}

export function deleteElements(
  contentToDelete: Identifiable[],
  verifyNotPresent: Identifiable[],
  deleted: Draft<number[]>,
) {
  for (const toDelete of contentToDelete) {
    deleteElement(toDelete, verifyNotPresent, deleted);
  }
}

export function newParagraph(...content: Content[]): ParagraphBlock {
  return {
    type: PARAGRAPH,
    id: null,
    editable: true,
    deletedContent: [],
    content,
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
    text: args.text,
    editedText: args.editedText ?? null,
    tags: args.tags ?? [],
  };
}

export function newItem(text: string): Item {
  return { id: null, content: [newLiteral({ text })] };
}

export function newItemList(...items: Item[]): ItemList {
  return { id: null, type: "ITEM_LIST", items, deletedItems: [] };
}
