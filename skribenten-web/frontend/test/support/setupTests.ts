import { enablePatches } from "immer";

// This ensures immer's patch support is enabled for all Vitest tests.
enablePatches();

// jsdom does not implement window.scrollTo and logs "Not implemented" whenever it is called.
globalThis.scrollTo = () => {};
