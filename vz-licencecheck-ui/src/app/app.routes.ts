import {Routes} from '@angular/router';
import {PolicyGuard} from '@app/_guards/policy.guard';
import {HomeComponent} from '@app/pages/home/home.component';
import {verificationResultGuard} from '@app/_guards/verification-result.guard';
import {scanQrCodeGuard} from '@app/_guards/scan-qr-code.guard';
import {mdlFeatureGuard} from '@app/_guards/mdl-feature.guard';
import {licenseTypeGuard} from '@app/_guards/license-type.guard';

export const routes: Routes = [
  {path: '', component: HomeComponent, pathMatch: 'full'},
  {
    path: 'licence-type',
    loadComponent: () =>
      import('./pages/licence-type-selector/licence-type-selector.component').then(m => m.LicenceTypeSelectorComponent),
    canActivate: [PolicyGuard, mdlFeatureGuard],
    pathMatch: 'full'
  },
  {
    path: 'use-case',
    loadComponent: () => import('./pages/use-case/use-case.component').then(m => m.UseCaseComponent),
    canActivate: [PolicyGuard, licenseTypeGuard],
    pathMatch: 'full'
  },
  {
    path: 'scan-qr-code',
    loadComponent: () => import('./pages/scan-qr-code/scan-qr-code.component').then(m => m.ScanQrCodeComponent),
    canActivate: [PolicyGuard, scanQrCodeGuard],
    pathMatch: 'full'
  },
  {
    path: 'verification-result',
    loadComponent: () =>
      import('./pages/verification-result/verification-result.component').then(m => m.VerificationResultComponent),
    canActivate: [PolicyGuard, verificationResultGuard],
    pathMatch: 'full'
  },
  {path: '**', redirectTo: ''}
];
