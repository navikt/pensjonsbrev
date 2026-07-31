import { describe, expect, it } from "vitest";

import { type HashBoundValue, pickValueForCurrentHash } from "~/Brevredigering/LetterEditor/diff/diffQueryState";

describe("diffQueryState", () => {
  it("ignores stale diff responses from an older hash", () => {
    const response: HashBoundValue<string> = {
      value: "stale-diff",
      redigertBrevHash: "old-hash",
    };

    const active = pickValueForCurrentHash(response, "new-hash");
    expect(active).toBeUndefined();
  });

  it("returns diff only when response hash matches current saved hash", () => {
    const response: HashBoundValue<string> = {
      value: "fresh-diff",
      redigertBrevHash: "hash-1",
    };

    const active = pickValueForCurrentHash(response, "hash-1");
    expect(active).toBe("fresh-diff");
  });
});
