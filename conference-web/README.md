# ConferenceWeb

This project was generated with [angular-cli](https://github.com/angular/angular-cli) version 1.0.0-beta.28.3.

## Angular CLI commands


### Check NG CLI version

```
$ ng version
angular-cli: 1.0.0-beta.28.3
node: 7.5.0
```
### Create a new project

Create the project without NPM (we use [Yarn](https://yarnpkg.com/)) or Git (already there)

```
$ ng new conference-web --prefix conf --skip-npm --skip-git
```

You can already install the needed dependencies with [Yarn](https://yarnpkg.com/) : 

```
$ yarn install
```

### Adding dependencies

We need to install some dependencies :

```
$ yarn add @angular/material --save
$ yarn add hammerjs --save
$ yarn add @types/hammerjs --save
$ yarn add bootstrap --save
```

We use Material Design. So we need to add it to the module app :

```
in app.module.ts
// MATERIAL DESIGN MODULES
import { MaterialModule } from '@angular/material';

@NgModule({
  imports: [
    MaterialModule.forRoot(),
  ],
```

### Generate the needed components 

```
$ ng generate component attendee
$ ng generate component login
$ ng generate component logout
$ ng generate component vote
$ ng generate component venue
$ ng generate component schedule
$ ng generate component scheduleDetail
$ ng generate component speaker
$ ng generate component speakerDetail
$ ng generate service auth
$ ng generate service authGuard
$ ng generate pipe noat
```

### Generate the code for the server APIs

First we install `swagger-codegen` so we can generate TypeScript code to access our server APIs 

```
$ brew install swagger-codegen
```

Now generate the TypeScript code for all our services. Make sure all the services are up and running with a swagger.json file available

```
$ swagger-codegen generate -i microservice-attendee/src/main/webapp/swagger.json -l typescript-angular2 -o conference-web/src/app/server
$ swagger-codegen generate -i microservice-rating/src/main/webapp/swagger.json -l typescript-angular2 -o conference-web/src/app/server
$ swagger-codegen generate -i microservice-schedule/src/main/webapp/swagger.json -l typescript-angular2 -o conference-web/src/app/server
$ swagger-codegen generate -i microservice-speaker/src/main/webapp/swagger.json -l typescript-angular2 -o conference-web/src/app/server
$ swagger-codegen generate -i microservice-talk/src/main/webapp/swagger.json -l typescript-angular2 -o conference-web/src/app/server
$ swagger-codegen generate -i microservice-venue/src/main/webapp/swagger.json -l typescript-angular2 -o conference-web/src/app/server
```


## Development server
Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive/pipe/service/class/module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `-prod` flag for a production build.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).
Before running the tests make sure you are serving the app via `ng serve`.

## Deploying to GitHub Pages

Run `ng github-pages:deploy` to deploy to GitHub Pages.

## Further help

To get more help on the `angular-cli` use `ng help` or go check out the [Angular-CLI README](https://github.com/angular/angular-cli/blob/master/README.md).
