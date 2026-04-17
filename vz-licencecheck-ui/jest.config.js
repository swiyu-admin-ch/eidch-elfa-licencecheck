const { createCjsPreset } = require("jest-preset-angular/build/presets/create-cjs-preset");

module.exports = {
  ...createCjsPreset(),

  roots: ["<rootDir>/src"],
  setupFilesAfterEnv: ["<rootDir>/tests/setupJest.ts"],

  moduleNameMapper: {
    "@app/(.*)": "<rootDir>/src/app/$1",
    "@environments/(.*)": "<rootDir>/src/environments/$1",
  },

  testResultsProcessor: "jest-sonar-reporter",
  testPathIgnorePatterns: ["/node_modules/", "/dist/"],

  coverageDirectory: "<rootDir>/coverage/sonarQube",
  collectCoverageFrom: ["<rootDir>/src/**/*.ts", "!<rootDir>/src/app/core/api/generated/**"],
  coveragePathIgnorePatterns: ["<rootDir>/src/main.ts", "<rootDir>/src/polyfills.ts"],
};
