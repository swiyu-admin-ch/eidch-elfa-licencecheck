import {Injectable, signal} from '@angular/core';
import {Observable, of} from 'rxjs';
import {catchError, tap} from 'rxjs/operators';
import {AppConfig, AppConfigApi} from '@app/core/api/generated';

@Injectable({
  providedIn: 'root'
})
export class AppConfigService {
  private readonly _appConfig = signal<AppConfig | undefined>(undefined);

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
}
