import {inject, Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {EMPTY, Observable} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {ObENotificationType, ObNotificationService} from '@oblique/oblique';
import {TranslateService} from '@ngx-translate/core';
import {ErrorResponse} from '@app/core/api/generated';

export interface I18nNotification {
  messageKey: string;
  titleKey: string;
}

export const defaultErrorTranslationKeys: I18nNotification = {
  titleKey: 'i18n.exception',
  messageKey: 'i18n.exception.default'
};

@Injectable({providedIn: 'root'})
export class ErrorInterceptor implements HttpInterceptor {
  private readonly notificationService = inject(ObNotificationService);
  private readonly translate = inject(TranslateService);

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      catchError((errorResponse: HttpErrorResponse) => {
        if (isErrorResponse(errorResponse.error)) {
          this.handleError(errorResponse.error);
        } else {
          this.handleUnknownError();
        }

        return EMPTY;
      })
    );
  }

  private handleError(error: ErrorResponse): void {
    this.notifyError(errorTranslationKeys(error?.errorType));
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

function isErrorResponse(error: any): error is ErrorResponse {
  return error?.errorType;
}

export function errorTranslationKeys(errorType: string): I18nNotification {
  if (errorType) {
    return {
      messageKey: `i18n.error.${errorType}`,
      titleKey: 'i18n.exception'
    };
  }
  return defaultErrorTranslationKeys;
}
