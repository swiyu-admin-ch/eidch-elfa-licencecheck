import {Component, effect, inject} from '@angular/core';
import {Router} from '@angular/router';
import {ObButtonModule} from '@oblique/oblique';
import {UntilDestroy} from '@ngneat/until-destroy';
import {CommonModule} from '@angular/common';
import {TranslateModule} from '@ngx-translate/core';
import {MatButtonModule} from '@angular/material/button';
import {VerificationStore} from '@app/_services/verification.store';

@UntilDestroy()
@Component({
  selector: 'app-scan-qr-code',
  templateUrl: './scan-qr-code.component.html',
  styleUrls: ['./scan-qr-code.component.scss'],
  standalone: true,
  imports: [CommonModule, TranslateModule, MatButtonModule, ObButtonModule]
})
export class ScanQrCodeComponent {
  private readonly router = inject(Router);
  readonly store = inject(VerificationStore);

  constructor() {
    // navigate to result when terminal -> succeeded, failed or timedOut
    effect(() => {
      if (this.store.isTerminal()) {
        this.router.navigate(['/verification-result']);
      }
    });
  }

  close() {
    this.store.reset();
    this.router.navigate(['/use-case']);
  }
}
