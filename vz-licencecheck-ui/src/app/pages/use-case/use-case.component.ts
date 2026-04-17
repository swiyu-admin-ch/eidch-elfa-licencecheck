import {Component, inject, ViewEncapsulation} from '@angular/core';
import {Router} from '@angular/router';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {InfoDialogComponent} from '@app/pages/use-case/info-dialog/info-dialog.component';
import {UseCase, VerifierApi} from '@app/core/api/generated';
import {UntilDestroy} from '@ngneat/until-destroy';
import {CommonModule} from '@angular/common';
import {TranslateModule} from '@ngx-translate/core';
import {MatCardModule} from '@angular/material/card';
import {MatIconModule} from '@angular/material/icon';
import {ObButtonModule, ObIconModule} from '@oblique/oblique';
import {MatButtonModule} from '@angular/material/button';
import {VerificationStore} from '@app/_services/verification.store';
import {toSignal} from '@angular/core/rxjs-interop';
import {map} from 'rxjs';

@UntilDestroy()
@Component({
  selector: 'app-use-case',
  templateUrl: './use-case.component.html',
  styleUrls: ['./use-case.component.scss'],
  encapsulation: ViewEncapsulation.None,
  imports: [CommonModule, TranslateModule, MatCardModule, MatButtonModule, MatIconModule, ObButtonModule, ObIconModule]
})
export class UseCaseComponent {
  private readonly verifierApi = inject(VerifierApi);
  private readonly store = inject(VerificationStore);
  private readonly router = inject(Router);
  private readonly dialog = inject(MatDialog);

  useCases = toSignal(this.verifierApi.getUseCases().pipe(map(list => [...list].sort((a, b) => a.order - b.order))), {
    initialValue: []
  });

  async createVerificationRequest(useCase: UseCase) {
    this.store.reset();
    this.store.setUseCase(useCase);
    await this.store.beginVerification();
    this.router.navigate(['/scan-qr-code']);
  }
  openInfoDialog(useCase: UseCase) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.maxWidth = window.innerWidth <= 600 ? '90vw' : '35vw';
    dialogConfig.maxHeight = '90vh';
    dialogConfig.autoFocus = false;
    dialogConfig.data = {item: useCase};

    this.dialog.open(InfoDialogComponent, dialogConfig);
  }
}
