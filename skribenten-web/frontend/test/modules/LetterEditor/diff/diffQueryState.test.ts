import { describe, expect, it } from "vitest";

import {
  getSnapshotForHash,
  type HashBoundValue,
  pickValueForCurrentHash,
} from "~/Brevredigering/LetterEditor/diff/diffQueryState";

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

  it("returns hash-bound snapshot used by diff request", () => {
    const snapshots = new Map([
      ["hash-a", { text: "saved-a" }],
      ["hash-b", { text: "saved-b" }],
    ]);

    expect(getSnapshotForHash(snapshots, "hash-b")).toEqual({ text: "saved-b" });
    expect(getSnapshotForHash(snapshots, "missing")).toBeUndefined();
  });
});
