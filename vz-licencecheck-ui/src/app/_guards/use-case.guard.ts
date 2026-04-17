import {inject} from '@angular/core';
import {CanActivateFn, Router} from '@angular/router';
import {VerificationStore} from '@app/_services/verification.store';

export const useCaseGuard: CanActivateFn = () => {
  const store = inject(VerificationStore);
  const router = inject(Router);

  const useCase = store.useCase();

  if (!useCase) {
    // Redirect to use-case selection if no use case is available
    router.navigate(['/use-case']);
    return false;
  }

  return true;
};
