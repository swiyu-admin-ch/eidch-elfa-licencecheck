import {APP_INITIALIZER, ApplicationConfig, enableProdMode, importProvidersFrom, LOCALE_ID} from '@angular/core';

import {environment} from './environments/environment';
import {
  multiTranslateLoader,
  OB_BANNER,
  ObHttpApiInterceptor,
  ObHttpApiInterceptorConfig,
  ObIconModule,
  ObMasterLayoutConfig,
  ObMasterLayoutModule
} from '@oblique/oblique';
import {AppConfigService} from '@app/core/app-config/app-config.service';
import {banner} from '@app/core/utils';
import {HTTP_INTERCEPTORS, provideHttpClient} from '@angular/common/http';
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
import {ApiModule, Configuration} from '@app/core/api/generated';
import {PreloadAllModules, provideRouter, withPreloading} from '@angular/router';
import {routes} from '@app/app.routes';

if (environment.production) {
  enableProdMode();
}

const appConfig: ApplicationConfig = {
  providers: [
    /* Initialize Services and/or run code on application initialization. */
    {
      provide: APP_INITIALIZER,
      useFactory: (config: ObMasterLayoutConfig) => {
        return () => {
          config.locale.locales = ['de-CH', 'fr-CH', 'it-CH', 'en-CH', 'rm'];
          config.homePageRoute = '/use-case';
        };
      },
      deps: [ObMasterLayoutConfig, ObHttpApiInterceptorConfig],
      multi: true
    },
    // Oblique: show environment in top header
    {
      provide: OB_BANNER,
      useFactory: (appConfigService: AppConfigService) => {
        return banner(appConfigService.appConfig?.environment);
      },
      deps: [AppConfigService]
    },
    {provide: LOCALE_ID, useValue: 'de-CH'},
    {provide: HTTP_INTERCEPTORS, useClass: ObHttpApiInterceptor, multi: true},
    // provider used to create fake backend
    {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true},
    PolicyService,
    provideHttpClient(),
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

registerLocaleData(localeDECH);
registerLocaleData(localeFRCH);
registerLocaleData(localeITCH);
registerLocaleData(localeENCH);
registerLocaleData(localeRM);

bootstrapApplication(AppComponent, appConfig).catch(err => console.error(err));
