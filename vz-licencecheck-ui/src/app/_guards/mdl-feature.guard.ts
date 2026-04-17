import {inject} from '@angular/core';
import {CanActivateFn, Router} from '@angular/router';
import {AppConfigService} from '@app/core/app-config/app-config.service';

export const mdlFeatureGuard: CanActivateFn = () => {
  const router = inject(Router);
  const isMdlEnabled = inject(AppConfigService).isMdlFeatureEnabled;

  if (!isMdlEnabled) {
    // If MDL is disabled, redirect to use-case page
    router.navigate(['/use-case']);
  }
  return isMdlEnabled;
};
