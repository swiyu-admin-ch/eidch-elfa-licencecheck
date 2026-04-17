import {Component, computed, inject, OnInit, Signal, signal, ViewEncapsulation} from '@angular/core';
import {Router} from '@angular/router';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {InfoDialogComponent} from '@app/pages/use-case/info-dialog/info-dialog.component';
import {UseCase, VerifierApi} from '@app/core/api/generated';
import {UntilDestroy} from '@ngneat/until-destroy';
import {CommonModule} from '@angular/common';
import {TranslateModule} from '@ngx-translate/core';
import {MatCardModule} from '@angular/material/card';
import {MatIconModule} from '@angular/material/icon';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSelectChange, MatSelectModule} from '@angular/material/select';
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
    MatButtonModule
  ]
})
export class UseCaseComponent implements OnInit {
  private readonly verifierApi = inject(VerifierApi);
  private readonly store = inject(VerificationStore);
  private readonly router = inject(Router);
  private readonly dialog = inject(MatDialog);
  private readonly appConfigService = inject(AppConfigService);

  isMdlEnabled = false;

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

  availableTypes: Signal<string[]> = computed(() => {
    return Array.from(
      new Set(
        this.availableUseCases()
          .map(u => u.type)
          .filter(Boolean)
      )
    );
  });

  selectedType = signal<string | null>(null);

  filteredUseCases: Signal<UseCase[]> = computed(() => {
    return this.selectedType() ? this.availableUseCases().filter(u => u.type === this.selectedType()) : [];
  });

  ngOnInit(): void {
    // Load app configuration to get feature flags - set the flag once
    this.appConfigService.loadAppConfig().subscribe(config => {
      this.isMdlEnabled = config?.featureFlags?.enableMdl ?? false;

      // Set initial selectedType based on MDL state
      if (!this.isMdlEnabled) {
        this.selectedType.set('elfa');
      }
    });
  }

  onTypeChange(event: MatSelectChange) {
    this.selectedType.set(event.value || null);
  }

  async createVerificationRequest(useCase: UseCase) {
    this.store.reset();
    this.store.setUseCase(useCase);

    // Check if MDL is enabled, this is the mdl-validity-single-category use case, and it has selection input elements
    const hasSelectionInput = useCase.inputElements?.some(element => element.type === 'selection');

    if (this.isMdlEnabled && hasSelectionInput) {
      // Navigate to input selection page
      this.router.navigate(['/input-selection']);
    } else {
      // For other use cases, or when MDL is disabled, or no selection input, proceed directly to verification
      await this.store.beginVerification();
      this.router.navigate(['/scan-qr-code']);
    }
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
