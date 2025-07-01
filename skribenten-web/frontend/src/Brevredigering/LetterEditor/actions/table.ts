import { produce } from "immer";

import { PARAGRAPH, TABLE } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { Focus, LetterEditorState } from "../model/state";
import { newTable, pushCol, pushRow } from "../model/tableHelpers";

export const insertTable: Action<LetterEditorState, [focus: Focus, rows?: number, cols?: number]> = produce(
  (draft, focus, rows = 2, cols = 2) => {
    const block = draft.redigertBrev.blocks[focus.blockIndex];
    if (block.type !== PARAGRAPH) return;

    block.content.splice(focus.contentIndex + 1, 0, newTable(rows, cols));
    draft.focus = {
      blockIndex: focus.blockIndex,
      contentIndex: focus.contentIndex + 1,
    };
    draft.isDirty = true;
  },
);

export const addTableRow: Action<LetterEditorState, [blockIdx: number, contentIdx: number]> = produce(
  (draft, blockIdx, contentIdx) => {
    const content = draft.redigertBrev.blocks[blockIdx].content[contentIdx];
    if (content?.type !== TABLE) return;

    pushRow(content);
    draft.isDirty = true;
  },
);

export const addTableColumn: Action<LetterEditorState, [blockIdx: number, contentIdx: number]> = produce(
  (draft, blockIdx, contentIdx) => {
    const content = draft.redigertBrev.blocks[blockIdx].content[contentIdx];
    if (content?.type !== TABLE) return;

    pushCol(content);
    draft.isDirty = true;
  },
);
