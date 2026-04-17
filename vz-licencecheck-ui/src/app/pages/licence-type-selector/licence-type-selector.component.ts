import {Component, inject, signal} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {ObAlertComponent, ObButtonDirective, ObSelectableDirective, ObSelectableModule} from '@oblique/oblique';
import {TranslatePipe} from '@ngx-translate/core';
import {VerificationStore} from '@app/_services/verification.store';
import {MatCard, MatCardContent} from '@angular/material/card';
import {RadioCardDirective} from '@app/_directives/radio-card.directive';
import {MatButton} from '@angular/material/button';
import {Router} from '@angular/router';

@Component({
  selector: 'app-licence-type-chooser',
  imports: [
    CommonModule,
    ObSelectableModule,
    TranslatePipe,
    MatCard,
    MatCardContent,
    NgOptimizedImage,
    RadioCardDirective,
    MatButton,
    ObButtonDirective,
    ObAlertComponent
  ],
  templateUrl: './licence-type-selector.component.html',
  styleUrl: './licence-type-selector.component.scss'
})
export class LicenceTypeSelectorComponent {
  private readonly store = inject(VerificationStore);
  private readonly router = inject(Router);
  private readonly nextPageUrl = '/use-case';

  readonly displayAlert = signal<boolean>(false);

  /**
   * @description
   * Stores the actually selected licence type, which may be `null`.
   * Clears the displayed alert if a licence type has been selected.
   *
   * @param selection the selected items.
   */
  protected doSelectLicenceType(selection: ObSelectableDirective<string>[]) {
    const selectedLicenceType = selection.length > 0 ? selection[0].value : null;
    this.store.setSelectedLicenceType(selectedLicenceType);

    if (this.selectionDone) {
      this.displayAlert.set(false);
    }
  }

  /**
   * @description
   * Navigate to the next page if a licence type has been selected,
   * or else display an alert message.
   */
  protected doContinue() {
    if (this.selectionDone()) {
      this.router.navigateByUrl(this.nextPageUrl);
    } else {
      this.displayAlert.set(true);
    }
  }

  protected isLicenceTypeSelected(licenceType: string) {
    return this.store.selectedLicenceType() === licenceType;
  }

  private readonly selectionDone = () => !!this.store.selectedLicenceType();
}
