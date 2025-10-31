export default {
  "*": "eslint",
  "*.{ts,tsx,cts,mts}": () => "tsc --noEmit",
  "*.{css,scss,sass,ts,tsx,js,jsx,cts,cjs,mts,mjs}": "stylelint",
};
