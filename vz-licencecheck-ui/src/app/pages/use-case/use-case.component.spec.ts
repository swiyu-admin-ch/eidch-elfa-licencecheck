import {ComponentFixture, TestBed} from '@angular/core/testing';
import {UseCaseComponent} from './use-case.component';
import {TranslateModule} from '@ngx-translate/core';
import {provideHttpClientTesting} from '@angular/common/http/testing';
import {MatDialog} from '@angular/material/dialog';
import {provideHttpClient} from '@angular/common/http';

describe('UseCaseSelectorComponent', () => {
  let component: UseCaseComponent;
  let fixture: ComponentFixture<UseCaseComponent>;
  let dialogMock: any;

  beforeEach(async () => {
    dialogMock = {close: () => {}};

    await TestBed.configureTestingModule({
      imports: [UseCaseComponent, TranslateModule.forRoot()],
      providers: [provideHttpClient(), provideHttpClientTesting(), {provide: MatDialog, useValue: dialogMock}]
    }).compileComponents();

    fixture = TestBed.createComponent(UseCaseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
