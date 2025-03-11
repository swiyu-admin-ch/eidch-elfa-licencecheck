import {defineConfig} from 'cypress';

export default defineConfig({
  numTestsKeptInMemory: 0, // prevents memory related crashes when run locally not in headless mode
  e2e: {
    watchForFileChanges: false, // test does not run automatically on every file change
    defaultCommandTimeout: 5000,
    retries: 2,
    trashAssetsBeforeRuns: false,
    setupNodeEvents(on, config) {
      // see https://stackoverflow.com/a/52077306
      on('task', {
        log (message) {
          console.log(message)
          return null
        }
      })
      switch (config.env.stage) {
        case 'local':
          config.baseUrl = 'http://127.0.0.1:4201';
          break;
        case 'dev':
          config.baseUrl = 'https://astra-vz-licencecheck-d.apps.p-szb-ros-shrd-npr-01.cloud.admin.ch/';
          break;
        default:
          // default is Openshift DEV, da die Pipline keine Parameter erlaubt (siehe Kommentar https://jira.bit.admin.ch/browse/EWJTOOL-1708)
          config.baseUrl = 'https://astra-vz-licencecheck-d.apps.p-szb-ros-shrd-npr-01.cloud.admin.ch/';
      }
      config.specPattern = 'cypress/e2e/*.cy.ts'
      console.log("running cypress tests against: " + config.baseUrl);
      return config;
    },
    video: false,
  }
});
