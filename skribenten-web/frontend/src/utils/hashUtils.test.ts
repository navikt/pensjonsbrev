import { describe, expect, it } from "vitest";

import { sha256Hash, truncatedSha256Hash } from "~/utils/hashUtils";

// Known SHA-256 hash of the empty string, for cross-checking correctness.
const EMPTY_STRING_SHA256 = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";

describe("sha256Hash", () => {
  it("returns a 64-character lowercase hex string", async () => {
    const result = await sha256Hash("hello");
    expect(result).toHaveLength(64);
    expect(result).toMatch(/^[0-9a-f]+$/);
  });

  it("is deterministic — same input produces same output", async () => {
    const a = await sha256Hash("some-saks-id");
    const b = await sha256Hash("some-saks-id");
    expect(a).toBe(b);
  });

  it("produces different hashes for different inputs", async () => {
    const a = await sha256Hash("saks-123");
    const b = await sha256Hash("saks-456");
    expect(a).not.toBe(b);
  });

  it("matches the known SHA-256 hash of the empty string", async () => {
    expect(await sha256Hash("")).toBe(EMPTY_STRING_SHA256);
  });
});

describe("truncatedSha256Hash", () => {
  it("returns 16 characters by default", async () => {
    const result = await truncatedSha256Hash("some-saks-id");
    expect(result).toHaveLength(16);
  });

  it("returns the requested number of characters", async () => {
    for (const length of [1, 8, 12, 20, 32, 64]) {
      const result = await truncatedSha256Hash("some-saks-id", length);
      expect(result).toHaveLength(length);
    }
  });

  it("output is a prefix of the full sha256Hash output", async () => {
    const input = "some-saks-id";
    const full = await sha256Hash(input);
    const truncated = await truncatedSha256Hash(input, 20);
    expect(full.startsWith(truncated)).toBe(true);
  });

  it("is deterministic — same input produces same output", async () => {
    const a = await truncatedSha256Hash("saks-789");
    const b = await truncatedSha256Hash("saks-789");
    expect(a).toBe(b);
  });

  it("produces different outputs for different inputs", async () => {
    const a = await truncatedSha256Hash("saks-123");
    const b = await truncatedSha256Hash("saks-456");
    expect(a).not.toBe(b);
  });

  it("output contains only lowercase hex characters", async () => {
    const result = await truncatedSha256Hash("saks-id", 32);
    expect(result).toMatch(/^[0-9a-f]+$/);
  });
});
