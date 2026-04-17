import {ComponentFixture, TestBed} from '@angular/core/testing';
import {AppComponent} from './app.component';
import {AppConfigService} from '@app/core/app-config/app-config.service';
import {provideRouter} from '@angular/router';
import {provideObliqueTestingConfiguration} from '@oblique/oblique';

describe('AppComponent', () => {
  let fixture: ComponentFixture<AppComponent>;
  let component: AppComponent;

  let mockAppConfigService: {appConfig: any};

  beforeEach(async () => {
    mockAppConfigService = {
      appConfig: {
        version: '1.2.0',
        environment: 'LOCAL'
      }
    };

    await TestBed.configureTestingModule({
      providers: [
        provideObliqueTestingConfiguration(),
        provideRouter([]),
        {provide: AppConfigService, useValue: mockAppConfigService}
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it('should have current year as new Date().getFullYear()', () => {
    const currentYear = new Date().getFullYear();
    fixture.detectChanges();
    expect(component.currentYear).toEqual(currentYear);
  });

  it('should set banner according to the environment', () => {
    const env = 'LOCAL';
    fixture.detectChanges();
    expect(component.banner.text).toBe(env);
  });
});
