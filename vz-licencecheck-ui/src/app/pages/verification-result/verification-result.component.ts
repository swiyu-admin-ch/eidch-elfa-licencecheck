import {Component, OnDestroy, OnInit} from '@angular/core';
import {UseCaseService} from '@app/_services';
import {Router} from '@angular/router';
import {Subscription, switchMap, timer} from 'rxjs';
import {environment} from '@environments/environment';
import {DateUtils} from '@app/core/utils';

import {Attribute, AttributeGroup, UseCase, VerificationState} from '@app/core/api/generated';
import {CommonModule} from '@angular/common';
import {TranslateModule} from '@ngx-translate/core';
import {MatIconModule} from '@angular/material/icon';
import {ObButtonModule, ObIconModule, ObStickyModule} from '@oblique/oblique';
import {MatDividerModule} from '@angular/material/divider';
import {MatButtonModule} from '@angular/material/button';

@Component({
  selector: 'app-verification-result',
  templateUrl: './verification-result.component.html',
  styleUrls: ['./verification-result.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    MatIconModule,
    MatButtonModule,
    MatDividerModule,
    ObButtonModule,
    ObIconModule,
    ObStickyModule
  ]
})
export class VerificationResultComponent implements OnInit, OnDestroy {
  useCase: UseCase;
  verificationState: VerificationState;
  navigationTimerSubscription: Subscription;

  constructor(public useCaseService: UseCaseService, private readonly router: Router) {}

  ngOnInit() {
    if (!this.useCaseService.getUseCase()) {
      this.close();
    } else {
      this.useCase = this.useCaseService.getUseCase();
    }

    if (!this.useCaseService.getVerificationState() && !this.useCaseService.isTimeout()) {
      this.close();
    }

    if (this.useCaseService.getVerificationState()) {
      this.verificationState = this.useCaseService.getVerificationState();
    }

    this.startNavigationTimer();
  }

  ngOnDestroy() {
    this.useCaseService.reset();
    this.navigationTimerSubscription?.unsubscribe();
  }

  getAttribute(key: string): string | undefined {
    const attribute = this.verificationState?.holderAttributes?.attributes?.[key];
    return attribute || '---';
  }

  get statusStyle(): string {
    return this.isVcValid() ? 'verification-success' : 'verification-invalid';
  }

  base64image(key: string) {
    return 'data:image/png;base64,' + this.getAttribute(key);
  }

  getSortedAttributeGroups(): AttributeGroup[] {
    return this.useCase.attributeGroups.sort((a: AttributeGroup, b: AttributeGroup) => a.order - b.order);
  }

  getSortedAttributesWithoutPhotoImage(attributeGroup: AttributeGroup): Attribute[] {
    return attributeGroup.attributes.filter(a => a.name !== 'photoImage').sort((a, b) => a.order - b.order);
  }

  isVcValid(): boolean {
    if (this.verificationState?.holderAttributes?.attributes['dateOfExpiration'] !== undefined) {
      const dateOfExpiration = DateUtils.parseDate(
        this.verificationState?.holderAttributes?.attributes?.dateOfExpiration
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

  displayGroup(group: AttributeGroup) {
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
