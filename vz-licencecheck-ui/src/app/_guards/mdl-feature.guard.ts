import {inject} from '@angular/core';
import {CanActivateFn, Router} from '@angular/router';
import {AppConfigService} from '@app/core/app-config/app-config.service';

export const mdlFeatureGuard: CanActivateFn = () => {
  const router = inject(Router);
  const appConfigService = inject(AppConfigService);

  // Simple direct access to the config property
  const isMdlEnabled = appConfigService.appConfig?.featureFlags?.enableMdl ?? false;

  if (!isMdlEnabled) {
    // If MDL is disabled, redirect to use-case page
    router.navigate(['/use-case']);
    return false;
  }

  return true;
};
