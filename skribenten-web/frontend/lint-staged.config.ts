export default {
  /*
   * Top entries run in parallell. We run `biome format` before any of the
   * other non-writing commands, to make sure we don't get concurrency
   * problems.
   */
  "!(*.{ts,tsx,cts,mts,css})": [
    "biome format", // reformats and writes to disk
    "biome check",
  ],
  "*.{ts,cts,mts}": [
    "biome format", // reformats and writes to disk
    "biome check",
    () => "tsc --noEmit", // use function to pick up tsconfig.json
  ],
  "*.tsx": [
    "biome format", // reformats and writes to disk
    "biome check",
    () => "tsc --noEmit", // use function to pick up tsconfig.json
    "stylelint",
  ],
  "*.css": [
    "biome format", // reformats and writes to disk
    "biome check",
    "stylelint",
  ],
};
