const { defineConfig, globalIgnores } = require("eslint/config");

const js = require("@eslint/js");

const { FlatCompat } = require("@eslint/eslintrc");

const compat = new FlatCompat({
  baseDirectory: __dirname,
  recommendedConfig: js.configs.recommended,
  allConfig: js.configs.all,
});

module.exports = defineConfig([
  {},
  globalIgnores(["projects/**/*"]),
  {
    files: ["**/*.ts"],

    languageOptions: {
      parserOptions: {
        project: ["tsconfig.json"],
        createDefaultProgram: true,
      },
    },

    extends: compat.extends(
      "plugin:@angular-eslint/recommended",
      "plugin:@angular-eslint/template/process-inline-templates",
      "plugin:prettier/recommended",
    ),

    rules: {
      "prettier/prettier": [
        "error",
        {
          endOfLine: "auto",
        },
      ],

      "@angular-eslint/directive-selector": [
        "error",
        {
          type: "attribute",
          prefix: "app",
          style: "camelCase",
        },
      ],

      "@angular-eslint/component-selector": [
        "error",
        {
          type: "element",
          prefix: "app",
          style: "kebab-case",
        },
      ],
    },
  },
  {
    files: ["**/*.spec.ts", "**/__mocks__/*.ts"],

    languageOptions: {
      parserOptions: {
        project: ["tsconfig.spec.json"],
        createDefaultProgram: true,
      },
    },

    extends: compat.extends(
      "plugin:@angular-eslint/recommended",
      "plugin:@angular-eslint/template/process-inline-templates",
      "plugin:prettier/recommended",
    ),

    rules: {
      "@angular-eslint/directive-selector": [
        "error",
        {
          type: "attribute",
          prefix: "app",
          style: "camelCase",
        },
      ],

      "@angular-eslint/component-selector": [
        "error",
        {
          type: "element",
          prefix: "app",
          style: "kebab-case",
        },
      ],
    },
  },
  {
    files: ["**/*.js"],

    languageOptions: {
      ecmaVersion: 2022,
      parserOptions: {},
    },
  },
  {
    files: ["**/*.html"],
    ignores: ["**/*inline-template-*.component.html"],

    extends: compat.extends("plugin:@angular-eslint/template/recommended", "plugin:prettier/recommended"),

    rules: {
      "prettier/prettier": [
        "error",
        {
          parser: "angular",
          endOfLine: "auto",
        },
      ],
    },
  },
  globalIgnores(["**/node_modules", "**/dist", "**/coverage", "**/target"]),
]);
