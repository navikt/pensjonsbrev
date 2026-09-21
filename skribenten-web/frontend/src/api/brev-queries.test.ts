import { describe, expect, it } from "vitest";

import { brevDiffKeys } from "~/api/brev-queries";

describe("brevDiffKeys", () => {
  it("produces different keys for different hashes", () => {
    const keyA = brevDiffKeys.id(123, "hash-a");
    const keyB = brevDiffKeys.id(123, "hash-b");
    expect(keyA).not.toEqual(keyB);
  });

  it("produces different keys for different brevIds", () => {
    const key1 = brevDiffKeys.id(1, "same-hash");
    const key2 = brevDiffKeys.id(2, "same-hash");
    expect(key1).not.toEqual(key2);
  });
});
