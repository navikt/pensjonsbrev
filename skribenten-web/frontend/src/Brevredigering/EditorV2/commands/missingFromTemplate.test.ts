import { describe, expect, test } from "vitest";

import {
  item,
  list,
  literal,
  missingFromTemplate,
  paragraph,
  paragraphWith,
  stateV2,
  textBlockAt,
} from "~test/support/editorV2TestUtils";

import { caret, TITLE } from "../model/paths";
import { countMissingFromTemplate } from "../model/structure";
import { run } from "../state/editorState";
import { keepMissingFromTemplate, removeMissingFromTemplate } from "./missingFromTemplate";

describe("removeMissingFromTemplate", () => {
  test("removes the block, records its id and focuses the next block", () => {
    const flagged = missingFromTemplate(paragraph(literal("avsnitt utenfor mal")));
    const other = paragraph(literal("resten av brevet"));
    const result = run(stateV2(flagged, other), removeMissingFromTemplate, 0);

    expect(result.redigertBrev.blocks).toEqual([other]);
    expect(result.redigertBrev.deletedBlocks).toEqual([flagged.id]);
    expect(result.saveStatus).toBe("DIRTY");
    expect(result.focus).toEqual(caret({ kind: "block", block: 0 }));
  });

  test("does not record a new block in deletedBlocks", () => {
    const flagged = missingFromTemplate(paragraphWith({ id: null, content: [literal("nytt avsnitt")] }));
    const other = paragraph(literal("resten av brevet"));
    const result = run(stateV2(flagged, other), removeMissingFromTemplate, 0);

    expect(result.redigertBrev.blocks).toEqual([other]);
    expect(result.redigertBrev.deletedBlocks).toEqual([]);
  });

  test("focuses the previous block when removing the last block", () => {
    const result = run(
      stateV2(list(item(literal("a")), item(literal("b"))), missingFromTemplate(paragraph(literal("x")))),
      removeMissingFromTemplate,
      1,
    );
    expect(result.focus).toEqual(caret({ kind: "item", block: 0, item: 0 }));
  });

  test("focuses the title when no blocks remain", () => {
    const result = run(stateV2(missingFromTemplate(paragraph(literal("x")))), removeMissingFromTemplate, 0);
    expect(result.redigertBrev.blocks).toEqual([]);
    expect(result.focus).toEqual(caret(TITLE));
  });

  test("joins lists that become adjacent", () => {
    const result = run(
      stateV2(list(item(literal("a"))), missingFromTemplate(paragraph(literal("x"))), list(item(literal("b")))),
      removeMissingFromTemplate,
      1,
    );
    expect(result.redigertBrev.blocks).toHaveLength(1);
  });

  test("ignores unknown index", () => {
    const state = stateV2(paragraph(literal("x")));
    expect(run(state, removeMissingFromTemplate, 3)).toBe(state);
  });
});

describe("keepMissingFromTemplate", () => {
  test("clears id and flag, keeping the content", () => {
    const flagged = missingFromTemplate(paragraph(literal("behold meg")));
    const result = run(stateV2(flagged), keepMissingFromTemplate, 0);
    const block = textBlockAt(result, 0);

    expect(block.id).toBeNull();
    expect(block.missingFromTemplate).toBe(false);
    expect(block.content).toEqual(flagged.content);
    expect(result.saveStatus).toBe("DIRTY");
  });
});

describe("countMissingFromTemplate", () => {
  test("returns 0 when no blocks are missing from template", () => {
    expect(countMissingFromTemplate(stateV2(paragraph(), paragraph()).redigertBrev)).toBe(0);
  });

  test("counts flagged blocks", () => {
    const state = stateV2(missingFromTemplate(paragraph()), paragraph(), missingFromTemplate(list(item())));
    expect(countMissingFromTemplate(state.redigertBrev)).toBe(2);
  });
});
