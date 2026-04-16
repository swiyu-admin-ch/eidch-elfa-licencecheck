import {CanActivateFn, Router} from '@angular/router';
import {inject} from '@angular/core';
import {TimedOut, VerificationStore} from '@app/_services/verification.store';

// This guard prevents access to the verification result page if there is no verification in progress
export const verificationResultGuard: CanActivateFn = () => {
  const store = inject(VerificationStore);
  const router = inject(Router);

  const hasVerification = !!store.verification();
  const timedOut = store.status() === TimedOut;

  if (!hasVerification && !timedOut) {
    router.navigate(['/use-case']);
    return false;
  }
  return true;
};
