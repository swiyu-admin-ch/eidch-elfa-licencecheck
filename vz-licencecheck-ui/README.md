LicenceCheck UI
===============

Node and Npm
------------

The project is configured to use a specific version of Node and Npm, which you can look up in the `.nvmrc` file.
We should always use LTS versions of _Node.js_, like `lts/jod` for example.

Make sure you have installed the [nvm-sh/nvm: Node Version Manager](https://github.com/nvm-sh/nvm?tab=readme-ov-file#git-install),
go to the root of the UI module, and type the `nvm use` command in the terminal. You should get something similar to
the following.

    $ nvm use
    Found '/home/dev/development/projects/eid/vz-licencecheck-scs/vz-licencecheck-ui/.nvmrc' with version <lts/jod>
    Now using node v22.22.0 (npm v10.9.4)


The local `node_modules` folder
-------------------------------

Make sure you have a clean installation of the project's dependencies.

    $ npm ci

This will clean the `node_modules` directory and install the dependencies looking at an existing `package-lock.json`
file. You can now use `npx` to run commands from the local `node_modules` package. For example run the following
command the get the version of the Angular CLI.

    $ `npx ng --version`
    21.2.1


Updating the dependencies
-------------------------

Check for the outdated dependencies.

    $ npm outdated

The desired version ranges of the dependencies are configured in the `package.json` file. You can update
the dependencies by running:

    $ npm upgrade --save

This will update the `package.json` file. To make sure everything is working fine, I usually remove all
files and folders that can be automatically generated, and make a fresh installation of the dependencies.

    $ rm -Rf .angular/cache/ coverage/ node_modules/ package-lock.json target/
    $ npm install

The dependencies are installed in the local `node_modules` package, and the `package-lock.json` is
regenerated.


Linting the project
-------------------

To lint the project files and automatically fix problems, you can do:

    $ npx ng lint --fix
    $ npm run format


Jest tests
----------

To quickly execute all Jest tests or the tests of given single suite, do:

    $ npx jest
    $ npx jest app.component

Note that you will get the same results when you replace `npx jest` with `npm test`.
If you want to enable code coverage do:

    $ npx jest --collectCoverage=true 
    $ npm test -- --collectCoverage=true

Using the Angular CLI, watch mode and coverage are enabled, and the output is verbose by default.
To watch all test files or a single one, and generate a coverage report, do:

    $ npx ng test --watch-all
    $ npx ng test --test-match='**/app.component.spec.ts'

To obtain the same output as the simple `npx jest` command with the Angular CLI you use one
of the following commands:

    $ npx ng test --verbose=false --watch=false --coverage=false
    $ npx ng test --configuration=production --coverage=false

To have the same output as the simple `ng test` command using `jest` directly, do:

    $ npx jest --verbose --watch --collectCoverage


Build
-----

Run `npx ng build` to build the project. The build artifacts will be stored in
the `target/classes/static` directory.


All-In-One with Maven
---------------------

Maven's `clean` lifecycle will try to delete the `target`, `coverage`, `node_modules`
and `.angular/cache` directories.

    $ ../mvnw clean

All `npm` commands are executed in the `generate-resources` phase of the Maven build.

    $ ../mvnw package

If the `node_modules` folder is missing a clean installation of the dependencies is done
by running the `npm ci` command.

If the build output directory is missing, that is the `target/classes/static` directory, then the
following is executed in sequence.

* lint the source files by running `npm run lint`
* run all Jest tests by running `npm run test:ci`
* do a production build by running `npm run build`

You can skip running the Jest tests with one of the following commands.

    $ ../mvnw -D skipTests compile
    $ ../mvnw -D skip.ui.tests compile


Development server
------------------

Run `npx ng serve --port 4201` or do a simple `npm start` to start the dev-server, and.
navigate to [http://localhost:4201/](http://localhost:4201/). The app will automatically
reload if you change any of the source files.


Generate the API code
---------------------

To generate or regenerate the frontend's API code do:

    npm run pregenerate:api
    npm run generate:api
    npm run postgenerate:api


Code scaffolding
----------------

Run `ng generate component component-name` to generate a new component.
You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.
