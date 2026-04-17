import {Component, computed, inject, OnDestroy} from '@angular/core';
import {Router} from '@angular/router';
import {environment} from '@environments/environment';

import {Attribute, AttributeGroup} from '@app/core/api/generated';
import {CommonModule} from '@angular/common';
import {TranslateModule} from '@ngx-translate/core';
import {MatIconModule} from '@angular/material/icon';
import {ObButtonModule, ObIconModule, ObStickyModule} from '@oblique/oblique';
import {MatDividerModule} from '@angular/material/divider';
import {MatButtonModule} from '@angular/material/button';
import {TimedOut, VerificationStore} from '@app/_services/verification.store';
import {DateUtils} from '@app/core/utils';

@Component({
  selector: 'app-verification-result',
  templateUrl: './verification-result.component.html',
  styleUrls: ['./verification-result.component.scss'],
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
export class VerificationResultComponent implements OnDestroy {
  protected readonly router = inject(Router);
  readonly store = inject(VerificationStore);

  readonly statusStyleClass = computed(() => (this.store.isValid() ? 'verification-success' : 'verification-invalid'));

  constructor() {
    // navigates back to use-cases after some time (15 minutes)
    setTimeout(() => this.router.navigate(['/use-case']), environment.navigationDelay);
  }

  ngOnDestroy() {
    this.store.reset();
  }

  getAttribute(key: string): string {
    return this.store.verification()?.holderAttributes?.attributes?.[key] ?? '---';
  }

  base64image(key: string): string {
    return 'data:image/png;base64,' + this.getAttribute(key);
  }

  getSortedAttributeGroups(): AttributeGroup[] {
    return [...(this.store.useCase()?.attributeGroups ?? [])].sort((a, b) => a.order - b.order);
  }

  getSortedAttributesWithoutPhotoImage(group: AttributeGroup): Attribute[] {
    return group.attributes.filter(a => a.name !== 'photoImage').sort((a, b) => a.order - b.order);
  }

  shouldDisplayGroupHeader(group: AttributeGroup) {
    return group.name !== 'photoImage';
  }

  protected readonly TimedOut = TimedOut;
  protected readonly DateUtils = DateUtils;
}
