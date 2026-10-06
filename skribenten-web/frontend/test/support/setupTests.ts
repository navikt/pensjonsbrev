import { webcrypto } from "node:crypto";

import { enablePatches } from "immer";

// In the vmThreads pool, globals come from jsdom, whose crypto lacks `subtle`. Use Node's Web Crypto instead.
if (!globalThis.crypto?.subtle) {
  Object.defineProperty(globalThis, "crypto", { value: webcrypto, configurable: true });
}

// This ensures immer's patch support is enabled for all Vitest tests.
enablePatches();

// jsdom does not implement window.scrollTo and logs "Not implemented" whenever it is called.
globalThis.scrollTo = () => {};
