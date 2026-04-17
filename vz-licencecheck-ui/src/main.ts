import {
  ApplicationConfig,
  enableProdMode,
  importProvidersFrom,
  inject,
  LOCALE_ID,
  provideAppInitializer,
  provideZoneChangeDetection
} from '@angular/core';

import {environment} from '@environments/environment';
import {
  OB_BANNER,
  ObHttpApiInterceptor,
  ObMasterLayoutConfig,
  ObMasterLayoutModule,
  provideObliqueConfiguration,
  WINDOW
} from '@oblique/oblique';
import {AppConfigService} from '@app/core/app-config/app-config.service';
import {banner} from '@app/core/utils';
import {HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';
import {ErrorInterceptor} from '@app/_interceptors';
import {PolicyService} from '@app/_services/policy.service';
import {registerLocaleData} from '@angular/common';
import localeDECH from '@angular/common/locales/de-CH';
import localeFRCH from '@angular/common/locales/fr-CH';
import localeITCH from '@angular/common/locales/it-CH';
import localeENCH from '@angular/common/locales/en-CH';
import localeRM from '@angular/common/locales/rm';
import {AppComponent} from '@app/app.component';
import {bootstrapApplication, BrowserModule} from '@angular/platform-browser';
import {PreloadAllModules, provideRouter, withPreloading} from '@angular/router';
import {routes} from '@app/app.routes';
import {ApiModule, Configuration} from '@app/core/api/generated';

registerLocaleData(localeDECH);
registerLocaleData(localeFRCH);
registerLocaleData(localeITCH);
registerLocaleData(localeENCH);
registerLocaleData(localeRM);

if (environment.production) {
  enableProdMode();
}

const initializeMasterLayoutConfig = (config: ObMasterLayoutConfig) => {
  config.locale.locales = ['de-CH', 'fr-CH', 'it-CH', 'en-CH', 'rm'];
  config.homePageRoute = '/';
};

const appConfig: ApplicationConfig = {
  providers: [
    /* Initialize Services and/or run code on application initialization. */
    provideAppInitializer(() => {
      initializeMasterLayoutConfig(inject(ObMasterLayoutConfig));
      // initialization will not complete until the app-config is loaded
      return inject(AppConfigService).loadAppConfig();
    }),
    provideObliqueConfiguration({
      accessibilityStatement: {
        createdOn: new Date('2026-03-03'),
        conformity: 'none',
        applicationName: "Replace me with the application's name",
        applicationOperator:
          'Replace me with the name and address of the federal office that exploit this application, HTML is permitted',
        contact: [{email: ''}, {phone: ''}]
      }
    }),
    {
      provide: OB_BANNER,
      useFactory: (appConfigService: AppConfigService) => {
        return banner(appConfigService.appConfig?.environment);
      },
      deps: [AppConfigService]
    },
    {provide: LOCALE_ID, useValue: 'de-CH'},
    {provide: WINDOW, useValue: window},
    {provide: HTTP_INTERCEPTORS, useClass: ObHttpApiInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true},
    PolicyService,
    provideHttpClient(withInterceptorsFromDi()),
    provideRouter(routes, withPreloading(PreloadAllModules)),
    importProvidersFrom([
      ObMasterLayoutModule,
      BrowserModule,

      ApiModule.forRoot(
        () =>
          new Configuration({
            basePath: '' // use relative
          })
      )
    ])
  ]
};

bootstrapApplication(AppComponent, {
  ...appConfig,
  providers: [provideZoneChangeDetection(), ...appConfig.providers]
}).catch(err => console.error(err));
