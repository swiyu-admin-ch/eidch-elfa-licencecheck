import {Component, computed, inject, OnInit, Signal, ViewEncapsulation} from '@angular/core';
import {Router} from '@angular/router';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {InfoDialogComponent} from '@app/pages/use-case/info-dialog/info-dialog.component';
import {UseCase, VerifierApi} from '@app/core/api/generated';
import {UntilDestroy} from '@ngneat/until-destroy';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {TranslateModule} from '@ngx-translate/core';
import {MatCardModule} from '@angular/material/card';
import {MatIconModule} from '@angular/material/icon';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';
import {ObButtonModule, ObIconModule} from '@oblique/oblique';
import {MatButtonModule} from '@angular/material/button';
import {VerificationStore} from '@app/_services/verification.store';
import {toSignal} from '@angular/core/rxjs-interop';
import {map} from 'rxjs';
import {AppConfigService} from '@app/core/app-config/app-config.service';

@UntilDestroy()
@Component({
  selector: 'app-use-case',
  templateUrl: './use-case.component.html',
  styleUrls: ['./use-case.component.scss'],
  encapsulation: ViewEncapsulation.None,
  imports: [
    CommonModule,
    TranslateModule,
    MatCardModule,
    MatIconModule,
    MatFormFieldModule,
    MatSelectModule,
    ObButtonModule,
    ObIconModule,
    MatButtonModule,
    NgOptimizedImage
  ]
})
export class UseCaseComponent implements OnInit {
  private readonly verifierApi = inject(VerifierApi);
  private readonly store = inject(VerificationStore);
  private readonly router = inject(Router);
  private readonly dialog = inject(MatDialog);
  private readonly appConfigService = inject(AppConfigService);

  readonly isMdlEnabled = this.appConfigService.isMdlFeatureEnabled;

  useCases = toSignal(this.verifierApi.getUseCases().pipe(map(list => [...list].sort((a, b) => a.order - b.order))), {
    initialValue: []
  });

  // Filter use cases based on MDL feature flag
  availableUseCases: Signal<UseCase[]> = computed(() => {
    const cases = this.useCases();
    if (!this.isMdlEnabled) {
      return cases.filter(u => u.type === 'elfa');
    }
    return cases;
  });

  readonly selectedType = this.store.selectedLicenceType;

  filteredUseCases: Signal<UseCase[]> = computed(() => {
    return this.availableUseCases().filter(u => u.type === this.store.selectedLicenceType());
  });

  ngOnInit(): void {
    if (!this.isMdlEnabled) {
      // if mDL feature is not enabled, the selected licence type is fixed to 'elfa'
      this.store.setSelectedLicenceType('elfa');
    }
  }

  async createVerificationRequest(useCase: UseCase) {
    this.store.reset();
    this.store.setUseCase(useCase);

    await this.store.beginVerification();
    this.router.navigate(['/scan-qr-code']);
  }

  openInfoDialog(useCase: UseCase) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.maxWidth = window.innerWidth <= 600 ? '90vw' : '50vw';
    dialogConfig.maxHeight = '90vh';
    dialogConfig.autoFocus = false;
    dialogConfig.data = {item: useCase};

    this.dialog.open(InfoDialogComponent, dialogConfig);
  }

  /**
   * @description
   * Navigate back to the licence type selection page.
   */
  protected goBack() {
    this.router.navigateByUrl('/licence-type');
  }
}
