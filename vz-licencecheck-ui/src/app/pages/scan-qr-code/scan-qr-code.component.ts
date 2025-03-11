import {Component, OnDestroy, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {UseCaseService} from '@app/_services';
import {environment} from '@environments/environment';
import {ObHttpApiInterceptorConfig} from '@oblique/oblique';
import {interval, startWith, Subscription, switchMap} from 'rxjs';
import {TimerService} from '@app/_services/timer.service';
import {
  VerificationBeginResponseDto,
  VerificationStatus,
  VerificationStatusResponse,
  VerifierApi
} from '@app/core/api/generated';
import {UntilDestroy, untilDestroyed} from '@ngneat/until-destroy';
import {UseCaseResponse} from '@app/core/api/generated/model/use-case-response';

@UntilDestroy()
@Component({
  selector: 'app-scan-qr-code',
  templateUrl: './scan-qr-code.component.html',
  styleUrls: ['./scan-qr-code.component.scss']
})
export class ScanQrCodeComponent implements OnInit, OnDestroy {
  useCase: UseCaseResponse;
  response: VerificationBeginResponseDto;

  intervalSubscription: Subscription;

  constructor(
    private readonly verifierApi: VerifierApi,
    private readonly router: Router,
    private readonly obHttpApiInterceptorConfig: ObHttpApiInterceptorConfig,
    public useCaseService: UseCaseService,
    private readonly timerService: TimerService
  ) {}

  ngOnInit() {
    // deactivate spinner
    this.obHttpApiInterceptorConfig.api.spinner = false;

    this.useCase = this.useCaseService.getUseCase();
    if (this.useCase) {
      this.verifierApi
        .startVerificationProcess({startVerificationRequest: {useCaseId: this.useCase.id}})
        .pipe(untilDestroyed(this))
        .subscribe(result => {
          this.response = result;
          this.startPolling();
          this.timerService.startTimeoutTimer();
          this.timerService.subscribeToTimer({
            next: () => {
              this.useCaseService.setTimeout();
              this.goToVerification();
            }
          });
        });
    } else {
      this.close();
    }
  }

  ngOnDestroy(): void {
    this.obHttpApiInterceptorConfig.api.spinner = true;
    this.intervalSubscription?.unsubscribe();
    this.timerService.stopTimer();
  }

  private startPolling() {
    this.intervalSubscription = interval(environment.pollingInterval)
      .pipe(
        startWith(0),
        switchMap(() => this.verifierApi.getVerificationProcess({verificationId: this.response.id}))
      )
      .subscribe({
        next: this.handleResponse(),
        error: this.handleError()
      });
  }

  handleResponse() {
    return (response: VerificationStatusResponse) => {
      if (response.status !== VerificationStatus.Pending) {
        this.timerService.stopTimer();
        this.useCaseService.setVerificationResponse(response);
        this.goToVerification();
      }
    };
  }

  handleError() {
    return (error: VerificationStatusResponse) => {
      if (error.status !== VerificationStatus.Pending) {
        this.close();
      }
    };
  }
  goToVerification() {
    this.router.navigate(['/verification-result']);
  }

  close() {
    this.useCaseService.setUseCase(null);
    this.router.navigate(['/use-case']);
  }

  get base64image() {
    return 'data:image/' + this.response.qrCodeFormat + ';base64,' + this.response.qrCode;
  }
}
