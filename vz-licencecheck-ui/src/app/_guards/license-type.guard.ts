import {inject} from '@angular/core';
import {CanActivateFn, Router} from '@angular/router';
import {VerificationStore} from '@app/_services/verification.store';
import {AppConfigService} from '@app/core/app-config/app-config.service';

/**
 * @description
 * Redirects to the license type selection page if the mDL feature is enabled
 * and no license type has been selected yet.
 */
export const licenseTypeGuard: CanActivateFn = () => {
  const appConfigService = inject(AppConfigService);
  const store = inject(VerificationStore);
  const router = inject(Router);

  const isMdlEnabled = appConfigService.isMdlFeatureEnabled;
  const licenceType = store.selectedLicenceType();
  const licenceTypeSelectionRequired = isMdlEnabled && !licenceType;

  if (licenceTypeSelectionRequired) {
    router.navigate(['/licence-type']);
  }
  return !licenceTypeSelectionRequired;
};
