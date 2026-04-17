import {
  ApplicationConfig,
  enableProdMode,
  importProvidersFrom,
  LOCALE_ID,
  inject,
  provideAppInitializer
} from '@angular/core';

import {environment} from '@environments/environment';
import {
  multiTranslateLoader,
  OB_BANNER,
  ObHttpApiInterceptor,
  ObIconModule,
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
import {CommonModule, registerLocaleData} from '@angular/common';
import localeDECH from '@angular/common/locales/de-CH';
import localeFRCH from '@angular/common/locales/fr-CH';
import localeITCH from '@angular/common/locales/it-CH';
import localeENCH from '@angular/common/locales/en-CH';
import localeRM from '@angular/common/locales/rm';
import {AppComponent} from '@app/app.component';
import {bootstrapApplication} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {TranslateModule} from '@ngx-translate/core';
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

const appConfig: ApplicationConfig = {
  providers: [
    /* Initialize Services and/or run code on application initialization. */
    provideAppInitializer(() => {
      const initializerFn = ((config: ObMasterLayoutConfig) => {
        return () => {
          config.locale.locales = ['de-CH', 'fr-CH', 'it-CH', 'en-CH', 'rm'];
          config.homePageRoute = '/use-case';
        };
      })(inject(ObMasterLayoutConfig));
      return initializerFn();
    }),
    provideObliqueConfiguration({
      accessibilityStatement: {
        applicationName: "Replace me with the application's name",
        applicationOperator:
          'Replace me with the name and address of the federal office that exploit this application, HTML is permitted',
        contact: {/* at least 1 email or phone number has to be provided */ emails: [''], phones: ['']}
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
      CommonModule,
      BrowserAnimationsModule,
      ObMasterLayoutModule,
      ObIconModule.forRoot(),
      TranslateModule.forRoot(multiTranslateLoader()),
      ApiModule.forRoot(
        () =>
          new Configuration({
            basePath: '' // use relative
          })
      )
    ])
  ]
};

bootstrapApplication(AppComponent, appConfig).catch(err => console.error(err));
