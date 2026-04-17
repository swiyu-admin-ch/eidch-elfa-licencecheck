import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {PolicyService} from '@app/_services/policy.service';
import {CommonModule} from '@angular/common';
import {TranslateModule} from '@ngx-translate/core';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatButtonModule} from '@angular/material/button';
import {ObButtonModule} from '@oblique/oblique';
import {AppConfigService} from '@app/core/app-config/app-config.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  imports: [CommonModule, TranslateModule, MatCheckboxModule, MatButtonModule, ObButtonModule]
})
export class HomeComponent implements OnInit {
  readonly titleKey: string;
  showMessage: boolean = false;
  policyGroup: string = 'policy-group';

  private readonly nextPageUrl: string;

  constructor(
    private readonly router: Router,
    private readonly policyService: PolicyService,
    private readonly appConfigService: AppConfigService
  ) {
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

  checkPolicy(checked: boolean) {
    if (checked) {
      this.policyService.setPolicyConfirmed(true);
      this.showMessage = false;
      this.policyGroup = 'policy-group';
    } else {
      this.policyService.setPolicyConfirmed(false);
    }
  }
}
