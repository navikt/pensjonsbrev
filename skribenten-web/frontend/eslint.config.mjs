import { fixupConfigRules, fixupPluginRules } from "@eslint/compat";
import react from "eslint-plugin-react";
import prettier from "eslint-plugin-prettier";
import typescriptEslint from "@typescript-eslint/eslint-plugin";
import testingLibrary from "eslint-plugin-testing-library";
import reactHooks from "eslint-plugin-react-hooks";
import simpleImportSort from "eslint-plugin-simple-import-sort";
import globals from "globals";
import tsParser from "@typescript-eslint/parser";
import path from "node:path";
import { fileURLToPath } from "node:url";
import js from "@eslint/js";
import { FlatCompat } from "@eslint/eslintrc";
import unicorn from "eslint-plugin-unicorn";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const compat = new FlatCompat({
  baseDirectory: __dirname,
  recommendedConfig: js.configs.recommended,
  allConfig: js.configs.all,
});

export default [
  {
    ignores: ["src/vite-env.d.ts", "dist", "**/vite.config.ts", "src/routeTree.gen.ts"],
  },
  ...fixupConfigRules(
    compat.extends(
      "eslint:recommended",
      "plugin:@typescript-eslint/recommended",
      "plugin:@tanstack/eslint-plugin-query/recommended",
      "prettier",
      "plugin:testing-library/react",
      "plugin:react-hooks/recommended",
    ),
  ),
  {
    files: ["**/*.ts", "**/*.tsx", "**/*.js", "**/*.jsx"],
    plugins: {
      react,
      prettier,
      unicorn,
      "@typescript-eslint": fixupPluginRules(typescriptEslint),
      "testing-library": fixupPluginRules(testingLibrary),
      "react-hooks": fixupPluginRules(reactHooks),
      "simple-import-sort": simpleImportSort,
    },

    languageOptions: {
      globals: {
        ...globals.browser,
      },

      parser: tsParser,
      ecmaVersion: "latest",
      sourceType: "module",

      parserOptions: {
        project: true,
        tsconfigRootDir: __dirname,
      },
    },

    rules: {
      "no-console": "error",
      "react/jsx-boolean-value": "error",
      "react/jsx-key": "error",
      "react/jsx-sort-props": "error",
      "prettier/prettier": ["error"],
      "import/prefer-default-export": "off",
      "simple-import-sort/imports": "error",
      "simple-import-sort/exports": "error",
      "testing-library/render-result-naming-convention": "off",
      "@tanstack/query/exhaustive-deps": "off",
      "@typescript-eslint/consistent-type-imports": ["warn"],
      "@typescript-eslint/switch-exhaustiveness-check": "error",
      "react/no-unused-prop-types": "error",
      "unicorn/filename-case": "off",
      "unicorn/no-null": "off",
      "unicorn/no-useless-undefined": "off",
      "unicorn/no-array-callback-reference": "off",
      "unicorn/prevent-abbreviations": "off",
      "unicorn/catch-error-name": "error",
      "unicorn/explicit-length-check": "error",
      "unicorn/import-style": "error",
      "unicorn/new-for-builtins": "error",
      "unicorn/prefer-default-parameters": "error",
      "unicorn/prefer-array-flat": "error",
      "unicorn/prefer-array-flat-map": "error",
      "unicorn/prefer-global-this": "error",
      "unicorn/prefer-regexp-test": "error",
      "unicorn/throw-new-error": "error",
    },
  },
];
