import {ComponentFixture, TestBed} from '@angular/core/testing';
import {AppComponent} from './app.component';
import {AppConfigService} from '@app/core/app-config/app-config.service';
import {TranslateLoader, TranslateModule, TranslateNoOpLoader} from '@ngx-translate/core';
import {provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';
import {provideHttpClientTesting} from '@angular/common/http/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {provideRouter} from '@angular/router';
import {ObMasterLayoutModule, WINDOW} from '@oblique/oblique';
import {MatIconTestingModule} from '@angular/material/icon/testing';

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
      imports: [
        AppComponent,
        TranslateModule.forRoot({
          loader: {provide: TranslateLoader, useClass: TranslateNoOpLoader}
        }),
        ObMasterLayoutModule,
        MatIconTestingModule
      ],
      providers: [
        provideRouter([]),
        provideNoopAnimations(),
        provideHttpClient(withInterceptorsFromDi()),
        provideHttpClientTesting(),
        {provide: AppConfigService, useValue: mockAppConfigService},
        {provide: WINDOW, useValue: window}
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
