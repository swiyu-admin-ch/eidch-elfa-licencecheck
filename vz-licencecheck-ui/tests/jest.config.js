const { compilerOptions } = require("../tsconfig");

module.exports = {
  roots: ["<rootDir>/src"],
  preset: "jest-preset-angular",
  setupFilesAfterEnv: ["<rootDir>/tests/setupJest.ts"],
  moduleNameMapper: {
    ...(compilerOptions.paths
      ? Object.entries(compilerOptions.paths).reduce((acc, [key, [path]]) => {
          acc[key.replace("/*", "/(.*)$")] = `<rootDir>/${path.replace("/*", "/$1")}`;
          return acc;
        }, {})
      : {}),
    "^package.json$": "<rootDir>/package.json",
  },
  transformIgnorePatterns: ["<rootDir>/node_modules/(?!(.*\\.mjs)|date-fns)"],
  coverageDirectory: "<rootDir>/coverage/sonarQube",
  testPathIgnorePatterns: ["/node_modules/", "/dist/"],
  testResultsProcessor: "jest-sonar-reporter",
  collectCoverage: true,
  collectCoverageFrom: ["<rootDir>/src/**/*.ts", "!<rootDir>/src/app/core/api/generated/**"],
};
