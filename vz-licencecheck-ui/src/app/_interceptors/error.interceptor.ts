import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {EMPTY, Observable} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {ObENotificationType, ObNotificationService} from '@oblique/oblique';
import {ErrorResponse} from '@app/core/api/generated';
import {TranslateService} from '@ngx-translate/core';

export interface I18nNotification {
  messageKey: string;
  titleKey: string;
}

export const defaultErrorTranslationKeys: I18nNotification = {
  titleKey: 'i18n.exception',
  messageKey: 'i18n.exception.default'
};

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(
    private readonly notificationService: ObNotificationService,
    private readonly translate: TranslateService
  ) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      catchError((errorResponse: ErrorResponse) => {
        if (isErrorResponse(errorResponse)) {
          this.handleHttpError(errorResponse);
        } else {
          this.handleUnknownError();
        }

        return EMPTY;
      })
    );
  }

  private handleHttpError(error: HttpErrorResponse): void {
    this.notifyError(httpErrorTranslationKeys(error.error));
  }

  private handleUnknownError(): void {
    this.notifyError(defaultErrorTranslationKeys);
  }

  private notifyError(i18n: I18nNotification): void {
    let title = this.translate.instant(i18n.titleKey);
    let message = this.translate.instant(i18n.messageKey);
    if (!title) {
      title = this.translate.instant(defaultErrorTranslationKeys.titleKey);
    }
    if (!message) {
      message = this.translate.instant(defaultErrorTranslationKeys.messageKey);
    }
    this.notificationService.error({
      message,
      title,
      type: ObENotificationType.ERROR,
      timeout: 10000,
      sticky: false
    });
  }
}

function isErrorResponse(error: any): error is HttpErrorResponse {
  return error?.status;
}

export function httpErrorTranslationKeys(error: ErrorResponse): I18nNotification {
  if (!error) {
    return defaultErrorTranslationKeys;
  }
  return {
    titleKey: 'i18n.exception',
    messageKey: `i18n.exception.${error.errorCode}`
  };
}
