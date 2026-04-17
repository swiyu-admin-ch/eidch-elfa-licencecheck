// This guard prevents access to the scan QR code page if no use-case is selected (i.e. on browser refresh)
import {CanActivateFn, Router} from '@angular/router';
import {inject} from '@angular/core';
import {Idle, VerificationStore} from '@app/_services/verification.store';

export const scanQrCodeGuard: CanActivateFn = () => {
  const store = inject(VerificationStore);
  const router = inject(Router);

  if (store.status() === Idle) {
    router.navigate(['/use-case']);
    return false;
  }
  return true;
};
