const IGNORED_UNICORN_RULES = {
  "unicorn/filename-case": "off",
  "unicorn/no-null": "off",
  "unicorn/no-useless-undefined": "off",
  "unicorn/no-array-callback-reference": "off",
};

// eslint-disable-next-line unicorn/prefer-module,no-undef
module.exports = {
  parser: "@typescript-eslint/parser",
  env: {
    browser: true,
    es2022: true,
  },
  extends: ["eslint:recommended", "plugin:@typescript-eslint/recommended", "prettier", "plugin:unicorn/recommended", "plugin:testing-library/react"],
  overrides: [],
  parserOptions: {
    project: true,
    tsconfigRootDir: __dirname,
    ecmaVersion: "latest",
    sourceType: "module",
  },
  plugins: ["react", "prettier", "@typescript-eslint", "testing-library", "simple-import-sort"],
  rules: {
    "no-console": "error",
    "react/jsx-boolean-value": "error",
    "react/jsx-key": "error",
    "react/jsx-sort-props": "error",
    "prettier/prettier": ["error"],
    "import/prefer-default-export": "off",
    "simple-import-sort/imports": "error",
    "simple-import-sort/exports": "error",
    "@typescript-eslint/consistent-type-imports": ["warn"],
    "@typescript-eslint/switch-exhaustiveness-check": "error",
    ...IGNORED_UNICORN_RULES,
  },
};
