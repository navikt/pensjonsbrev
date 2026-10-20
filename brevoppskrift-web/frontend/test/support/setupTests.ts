import { vi } from "vitest";

// jsdom does not implement scrolling, and TanStack Router's scroll restoration
// calls `scrollTo` on every navigation - each call would log a "Not
// implemented" warning into the test output. Guarded because some test files
// opt into the `node` environment, where there is no `scrollTo` at all.
if ("scrollTo" in globalThis) {
  globalThis.scrollTo = vi.fn();
}
