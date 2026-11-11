import { type Draft } from "immer";

import { type ListBlockTypeV2, type ListBlockV2 } from "~/types/brevV2";

import { changeBlockType } from "../model/blockType";
import { insertAt, removeAt } from "../model/collection";
import { newItem, newList } from "../model/factories";
import { listAt, type TextContainer } from "../model/paths";
import { blockCollection, breakOutOfList, joinAdjacentLists, linearOf } from "../model/structure";
import { type Command, type EditorStateV2 } from "../state/editorState";
import { focusIn } from "./focus";

export const toggleList: Command<[type: ListBlockTypeV2]> = (draft, type) => {
  const doc = draft.redigertBrev;
  const path = draft.focus.path;
  const linear = linearOf(doc, draft.focus);

  switch (path.kind) {
    case "block": {
      const block = doc.blocks[path.block];
      if (block?.type !== "PARAGRAPH") return;
      const blocks = blockCollection(doc);
      removeAt(blocks, path.block);
      const item = newItem(block.content);
      insertAt(blocks, path.block, newList(type, [item]));
      joinWithNeighbours(draft, path.block, item, linear);
      return;
    }
    case "item": {
      const list = listAt(doc, path.block);
      const item = list?.items[path.item];
      if (!list || !item) return;
      if (list.type === type) {
        focusIn(draft, breakOutOfList(doc, path), linear);
      } else {
        changeBlockType<ListBlockV2>(list, type);
        joinWithNeighbours(draft, path.block, item, linear);
      }
      return;
    }
    case "title":
    case "header":
    case "cell":
      return;
  }
};

function joinWithNeighbours(draft: Draft<EditorStateV2>, block: number, focused: TextContainer, linear: number) {
  joinAdjacentLists(draft.redigertBrev, block + 1);
  joinAdjacentLists(draft.redigertBrev, block);
  focusIn(draft, focused, linear);
}
