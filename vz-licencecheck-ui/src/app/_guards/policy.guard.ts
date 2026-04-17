import {inject, Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {PolicyService} from '@app/_services/policy.service';

@Injectable({
  providedIn: 'root'
})
export class PolicyGuard {
  private readonly policyService = inject(PolicyService);
  private readonly router = inject(Router);

  canActivate(): boolean {
    if (this.policyService.isPolicyConfirmed()) {
      return true;
    } else {
      this.router.navigateByUrl('/');
      return false;
    }
  }
}
