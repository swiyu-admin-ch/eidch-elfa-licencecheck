import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {PolicyService} from '@app/_services/policy.service';
import {CommonModule} from '@angular/common';
import {TranslateModule} from '@ngx-translate/core';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatButtonModule} from '@angular/material/button';
import {ObButtonModule} from '@oblique/oblique';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  standalone: true,
  imports: [CommonModule, TranslateModule, MatCheckboxModule, MatButtonModule, ObButtonModule]
})
export class HomeComponent implements OnInit {
  showMessage: boolean = false;
  policyGroup: string = 'policy-group';

  constructor(private readonly router: Router, private readonly policyService: PolicyService) {}

  ngOnInit() {
    if (this.isPolicyConfirmed()) {
      this.router.navigateByUrl('/use-case');
    }
  }

  isPolicyConfirmed(): boolean {
    return this.policyService.isPolicyConfirmed();
  }

  confirmPolicy() {
    if (this.policyService.isPolicyConfirmed()) {
      this.router.navigateByUrl('/use-case');
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
