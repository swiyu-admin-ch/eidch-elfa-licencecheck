import {Component, computed, inject, OnInit, signal} from '@angular/core';
import {Router} from '@angular/router';
import {CommonModule} from '@angular/common';
import {TranslateModule} from '@ngx-translate/core';
import {MatRadioModule} from '@angular/material/radio';
import {MatButtonModule} from '@angular/material/button';
import {ObButtonModule} from '@oblique/oblique';
import {VerificationStore} from '@app/_services/verification.store';
import {FormsModule} from '@angular/forms';
import {InputOption} from '@app/core/api/generated/model/input-option';

@Component({
  selector: 'app-input-selection',
  templateUrl: './input-selection.component.html',
  styleUrls: ['./input-selection.component.scss'],
  imports: [CommonModule, TranslateModule, MatRadioModule, MatButtonModule, ObButtonModule, FormsModule]
})
export class InputSelectionComponent implements OnInit {
  private readonly router = inject(Router);
  private readonly store = inject(VerificationStore);

  selectedOption = signal<string | null>(null);
  elementName = '';

  // Computed values for better performance - calculated once when dependencies change
  private readonly selectionElement = computed(() => {
    const useCase = this.store.useCase();
    return useCase?.inputElements?.find(el => el.type === 'selection') ?? null;
  });

  readonly options = computed((): InputOption[] => {
    return this.selectionElement()?.inputOptions ?? [];
  });

  get hasSelection(): boolean {
    return !!this.selectedOption();
  }

  ngOnInit(): void {
    this.elementName = this.selectionElement()?.name ?? '';

    // If no selection element exists, proceed directly to verification
    if (!this.selectionElement()) {
      this.proceedDirectly();
    }
  }

  onOptionChange(value: string): void {
    this.selectedOption.set(value);
  }

  goBack(): void {
    this.router.navigate(['/use-case']);
  }

  async startVerification(): Promise<void> {
    this.store.setSelectedCategory(this.selectedOption());
    await this.store.beginVerification();
    this.router.navigate(['/scan-qr-code']);
  }

  getOptionTranslationKey(key: string): string {
    return this.elementName ? `i18n.verifier.input-selection.${this.elementName}.${key}` : key;
  }

  getTitleTranslationKey(): string {
    return this.elementName ? `i18n.verifier.input-selection.${this.elementName}.title` : '';
  }

  private async proceedDirectly(): Promise<void> {
    await this.store.beginVerification();
    this.router.navigate(['/scan-qr-code']);
  }
}
