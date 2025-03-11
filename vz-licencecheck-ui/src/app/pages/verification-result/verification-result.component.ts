import {Component, OnDestroy, OnInit} from '@angular/core';
import {UseCaseService} from '@app/_services';
import {Router} from '@angular/router';
import {Subscription, switchMap, timer} from 'rxjs';
import {environment} from '@environments/environment';
import {DateUtils} from '@app/core/utils';

import {UseCaseResponse} from '@app/core/api/generated/model/use-case-response';
import {VerificationStatusResponse} from '@app/core/api/generated';
import {AttributeGroupResponse} from '@app/core/api/generated/model/attribute-group-response';
import {AttributeResponse} from '@app/core/api/generated/model/attribute-response';

@Component({
  selector: 'app-verification-result',
  templateUrl: './verification-result.component.html',
  styleUrls: ['./verification-result.component.scss']
})
export class VerificationResultComponent implements OnInit, OnDestroy {
  useCase: UseCaseResponse;
  verificationResponse: VerificationStatusResponse;
  navigationTimerSubscription: Subscription;

  constructor(public useCaseService: UseCaseService, private readonly router: Router) {}

  ngOnInit() {
    if (!this.useCaseService.getUseCase()) {
      this.close();
    } else {
      this.useCase = this.useCaseService.getUseCase();
    }

    if (!this.useCaseService.getVerificationResponse() && !this.useCaseService.isTimeout()) {
      this.close();
    }

    if (this.useCaseService.getVerificationResponse()) {
      this.verificationResponse = this.useCaseService.getVerificationResponse();
    }

    this.startNavigationTimer();
  }

  ngOnDestroy() {
    this.useCaseService.reset();
    this.navigationTimerSubscription?.unsubscribe();
  }

  getAttribute(key: string): string | undefined {
    const attribute = this.verificationResponse?.holderResponse?.attributes?.[key];
    return attribute || '---';
  }

  get statusStyle(): string {
    return this.isVcValid() ? 'verification-success' : 'verification-invalid';
  }

  base64image(key: string) {
    return 'data:image/png;base64,' + this.getAttribute(key);
  }

  getSortedAttributeGroups(): AttributeGroupResponse[] {
    return this.useCase.attributeGroups.sort((a, b) => a.order - b.order);
  }

  getSortedAttributesWithoutPhotoImage(attributeGroup: AttributeGroupResponse): AttributeResponse[] {
    return attributeGroup.attributes.filter(a => a.name !== 'photoImage').sort((a, b) => a.order - b.order);
  }

  isVcValid(): boolean {
    if (this.verificationResponse?.holderResponse?.attributes['dateOfExpiration'] !== undefined) {
      const dateOfExpiration = DateUtils.parseDate(
        this.verificationResponse?.holderResponse?.attributes?.dateOfExpiration
      );
      const now = new Date();
      now.setHours(0, 0, 0, 0);
      return dateOfExpiration && dateOfExpiration >= now;
    }
    return false;
  }

  displayVc(): boolean {
    return this.useCaseService.isVcValid();
  }

  displayTimeout(): boolean {
    return this.useCaseService.isTimeout();
  }

  displayRejected(): boolean {
    return this.useCaseService.isRejected();
  }

  displayInvalid(): boolean {
    return this.useCaseService.isVcInvalid();
  }

  displayGroup(group: AttributeGroupResponse) {
    return group.name !== 'photoImage';
  }

  close() {
    this.router.navigate(['/use-case']);
  }

  private startNavigationTimer(): void {
    this.navigationTimerSubscription = timer(environment.navigationDelay)
      .pipe(switchMap(() => this.router.navigate(['/use-case'])))
      .subscribe();
  }
}
