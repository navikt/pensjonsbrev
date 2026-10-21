export default {
  /*
   * Top entries run in parallell. We run `biome format` before any of the other
   * non-writing commands, to make sure we don't get concurrency problems.
   */
  "!(*.{ts,tsx,cts,mts,css})": [
    "biome format --write --no-errors-on-unmatched", // format and write to disk
    "biome check --no-errors-on-unmatched",
  ],
  "*.{ts,tsx,cts,mts}": [
    "biome format --write --no-errors-on-unmatched",
    "biome check --no-errors-on-unmatched",
    () => "npm run check-types",
    (files: string[]) => {
      const tsxFiles = files.filter((file) => file.endsWith(".tsx"));
      return tsxFiles.length
        ? [`stylelint ${tsxFiles.map((file) => JSON.stringify(file)).join(" ")}`]
        : [];
    },
  ],
  "*.css": [
    "biome format --write --no-errors-on-unmatched", // format and write to disk
    "biome check --no-errors-on-unmatched",
    "stylelint",
  ],
};
