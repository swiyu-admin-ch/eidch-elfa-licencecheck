import {Routes} from '@angular/router';
import {PolicyGuard} from '@app/_guards/policy.guard';
import {HomeComponent} from '@app/pages/home/home.component';

export const routes: Routes = [
  {path: '', component: HomeComponent, pathMatch: 'full'},
  {
    path: 'use-case',
    loadComponent: () => import('./pages/use-case/use-case.component').then(m => m.UseCaseComponent),
    canActivate: [PolicyGuard],
    pathMatch: 'full'
  },
  {
    path: 'scan-qr-code',
    loadComponent: () => import('./pages/scan-qr-code/scan-qr-code.component').then(m => m.ScanQrCodeComponent),
    canActivate: [PolicyGuard],
    pathMatch: 'full'
  },
  {
    path: 'verification-result',
    loadComponent: () =>
      import('./pages/verification-result/verification-result.component').then(m => m.VerificationResultComponent),
    canActivate: [PolicyGuard],
    pathMatch: 'full'
  },
  {path: '**', redirectTo: ''}
];
