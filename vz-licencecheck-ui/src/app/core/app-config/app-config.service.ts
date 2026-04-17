import {computed, Injectable, signal} from '@angular/core';
import {Observable, of} from 'rxjs';
import {catchError, tap} from 'rxjs/operators';
import {AppConfig, AppConfigApi, FeatureFlags} from '@app/core/api/generated';

@Injectable({
  providedIn: 'root'
})
export class AppConfigService {
  private readonly _appConfig = signal<AppConfig | undefined>(undefined);
  private readonly _featureFlags = computed<FeatureFlags | undefined>(() => this._appConfig()?.featureFlags);

  constructor(private readonly appConfigApi: AppConfigApi) {}

  loadAppConfig(): Observable<AppConfig | undefined> {
    return this.appConfigApi.getConfiguration().pipe(
      tap(config => {
        this._appConfig.set(config);
      }),
      catchError(e => {
        console.error('failed to load app config', e);
        return of(undefined);
      })
    );
  }

  get appConfig(): AppConfig | undefined {
    return this._appConfig();
  }

  /**
   * Indicates whether mDL feature is enabled.
   */
  get isMdlFeatureEnabled(): boolean {
    return this._featureFlags()?.enableMdl ?? false;
  }
}
