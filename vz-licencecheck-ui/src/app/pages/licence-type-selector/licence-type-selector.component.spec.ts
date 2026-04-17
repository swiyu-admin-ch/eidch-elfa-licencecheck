import {ComponentFixture, TestBed} from '@angular/core/testing';

import {LicenceTypeSelectorComponent} from './licence-type-selector.component';
import {VerifierApi} from '@app/core/api/generated';
import {DebugElement, Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {WINDOW} from '@oblique/oblique';
import {TranslateModule} from '@ngx-translate/core';
import {By} from '@angular/platform-browser';
import {VerificationStore} from '@app/_services/verification.store';

@Injectable()
class VerifierApiMock {}

describe('LicenceTypeSelectorComponent', () => {
  let fixture: ComponentFixture<LicenceTypeSelectorComponent>;
  let component: LicenceTypeSelectorComponent;
  let continueButton: DebugElement;

  let routerStub: {navigateByUrl: any};
  let storeStub: {selectedLicenceType: any; setSelectedLicenceType: any};

  beforeEach(async () => {
    routerStub = {
      navigateByUrl: jest.fn()
    };

    storeStub = {
      selectedLicenceType: jest.fn(),
      setSelectedLicenceType: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [LicenceTypeSelectorComponent, TranslateModule.forRoot()],
      providers: [
        {provide: VerificationStore, useValue: storeStub},
        {provide: Router, useValue: routerStub},
        {provide: VerifierApi, useClass: VerifierApiMock},
        {provide: WINDOW, useValue: window}
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LicenceTypeSelectorComponent);
    component = fixture.componentInstance;
    continueButton = fixture.debugElement.query(By.css('#continue-button'));
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('when selection done', () => {
    it('should navigate to /use-case if continue button clicked', () => {
      // given
      storeStub.selectedLicenceType.mockReturnValue('elfa');
      // when
      continueButton.nativeElement.click();
      // then
      expect(routerStub.navigateByUrl).toHaveBeenCalledWith('/use-case');
    });

    it('should clear any alert message if present', () => {
      let alertElement: DebugElement;

      // given an alert is displayed
      component.displayAlert.set(true);
      fixture.detectChanges();
      alertElement = fixture.debugElement.query(By.css('ob-alert'));
      expect(alertElement).toBeTruthy();
      // when
      const mdlSelection = fixture.debugElement.query(By.css('mat-card[value="mdl"]'));
      mdlSelection.nativeElement.click();
      fixture.detectChanges();
      // then
      alertElement = fixture.debugElement.query(By.css('ob-alert'));
      expect(alertElement).toBeFalsy();
      expect(component.displayAlert()).toBe(false);
    });
  });

  describe('when no selection done and continue button clicked', () => {
    beforeEach(async () => {
      storeStub.selectedLicenceType.mockReturnValue(null);
    });

    it('should not navigate', () => {
      // when
      continueButton.nativeElement.click();
      // then
      expect(routerStub.navigateByUrl).toHaveBeenCalledTimes(0);
    });

    it('should display an alert', () => {
      // when
      continueButton.nativeElement.click();
      fixture.detectChanges();
      // then
      const alertElement = fixture.debugElement.query(By.css('ob-alert'));
      expect(alertElement).toBeTruthy();
    });
  });
});
