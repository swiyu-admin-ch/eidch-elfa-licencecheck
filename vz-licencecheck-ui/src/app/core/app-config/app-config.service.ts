import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {catchError, tap} from 'rxjs/operators';
import {AppConfig, AppConfigApi} from '@app/core/api/generated';

@Injectable({
  providedIn: 'root'
})
export class AppConfigService {
  private readonly _appConfig$ = new BehaviorSubject<AppConfig | undefined>(undefined);

  constructor(private readonly appConfigApi: AppConfigApi) {}

  loadAppConfig(): Observable<AppConfig | undefined> {
    return this.appConfigApi.getConfiguration().pipe(
      tap(config => {
        this._appConfig$.next(config);
      }),
      catchError(e => {
        console.error('failed to load app config', e);
        return of(undefined);
      })
    );
  }

  get appConfig(): AppConfig | undefined {
    return this._appConfig$.getValue();
  }
}
