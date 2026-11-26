import { describe, expect, test } from "vitest";

import { countMissingInRenderedBlocks } from "~/Brevredigering/LetterEditor/actions/common";
import { letter, paragraph, withMissingInRendered } from "~test/support/letterEditorTestUtils";

describe("countMissingInRenderedBlocks", () => {
  test("returns 0 when no blocks are missing in rendered", () => {
    const state = letter(paragraph([]), paragraph([]));
    expect(countMissingInRenderedBlocks(state.redigertBrev)).toBe(0);
  });

  test("counts blocks flagged as missing in rendered", () => {
    const state = letter(withMissingInRendered(paragraph([])), paragraph([]), withMissingInRendered(paragraph([])));
    expect(countMissingInRenderedBlocks(state.redigertBrev)).toBe(2);
  });
});
