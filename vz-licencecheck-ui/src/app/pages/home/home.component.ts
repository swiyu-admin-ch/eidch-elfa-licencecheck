import {Component, inject, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {PolicyService} from '@app/_services/policy.service';

import {TranslateModule} from '@ngx-translate/core';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatButtonModule} from '@angular/material/button';
import {ObButtonModule} from '@oblique/oblique';
import {AppConfigService} from '@app/core/app-config/app-config.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  imports: [TranslateModule, MatCheckboxModule, MatButtonModule, ObButtonModule]
})
export class HomeComponent implements OnInit {
  private readonly router = inject(Router);
  private readonly policyService = inject(PolicyService);
  private readonly appConfigService = inject(AppConfigService);

  readonly titleKey: string;
  showMessage: boolean = false;
  policyGroup: string = 'policy-group';

  private readonly nextPageUrl: string;

  constructor() {
    if (this.appConfigService.isMdlFeatureEnabled) {
      this.titleKey = 'i18n.verifier.home.title';
      this.nextPageUrl = '/licence-type';
    } else {
      this.titleKey = 'i18n.verifier.home.title.old';
      this.nextPageUrl = '/use-case';
    }
  }

  ngOnInit() {
    if (this.isPolicyConfirmed()) {
      this.router.navigateByUrl(this.nextPageUrl);
    }
  }

  isPolicyConfirmed(): boolean {
    return this.policyService.isPolicyConfirmed();
  }

  confirmPolicy() {
    if (this.isPolicyConfirmed()) {
      this.router.navigateByUrl(this.nextPageUrl);
    } else {
      this.showMessage = true;
      this.policyGroup = 'policy-group-error';
    }
  }

  checkPolicy(confirmed: boolean) {
    this.policyService.setPolicyConfirmed(confirmed);
    if (confirmed) {
      this.showMessage = false;
      this.policyGroup = 'policy-group';
    }
  }
}
