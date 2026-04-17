import {Routes} from '@angular/router';
import {PolicyGuard} from '@app/_guards/policy.guard';
import {HomeComponent} from '@app/pages/home/home.component';
import {verificationResultGuard} from '@app/_guards/verification-result.guard';
import {scanQrCodeGuard} from '@app/_guards/scan-qr-code.guard';
import {useCaseGuard} from '@app/_guards/use-case.guard';
import {mdlFeatureGuard} from '@app/_guards/mdl-feature.guard';

export const routes: Routes = [
  {path: '', component: HomeComponent, pathMatch: 'full'},
  {
    path: 'use-case',
    loadComponent: () => import('./pages/use-case/use-case.component').then(m => m.UseCaseComponent),
    canActivate: [PolicyGuard],
    pathMatch: 'full'
  },
  {
    path: 'input-selection',
    loadComponent: () =>
      import('./pages/input-selection/input-selection.component').then(m => m.InputSelectionComponent),
    canActivate: [PolicyGuard, useCaseGuard, mdlFeatureGuard],
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
