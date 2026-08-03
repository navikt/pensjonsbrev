import { describe, expect, test } from "vitest";

import { countMissingFromTemplateBlocks } from "~/Brevredigering/LetterEditor/actions/common";

import { letter, paragraph, withMissingFromTemplate } from "../utils";

describe("countMissingFromTemplateBlocks", () => {
  test("returns 0 when no blocks are missing from template", () => {
    const state = letter(paragraph([]), paragraph([]));
    expect(countMissingFromTemplateBlocks(state.redigertBrev)).toBe(0);
  });

  test("counts blocks flagged as missing from template", () => {
    const state = letter(withMissingFromTemplate(paragraph([])), paragraph([]), withMissingFromTemplate(paragraph([])));
    expect(countMissingFromTemplateBlocks(state.redigertBrev)).toBe(2);
  });
});
