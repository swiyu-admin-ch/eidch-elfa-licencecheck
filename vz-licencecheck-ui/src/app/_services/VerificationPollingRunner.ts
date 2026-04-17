import {firstValueFrom, interval, Subscription, take} from 'rxjs';
import {Polling, Starting, VerificationStore} from '@app/_services/verification.store';
import {Status, VerifierApi} from '@app/core/api/generated';
import {environment} from '@environments/environment';

export class VerificationPollingRunner {
  private pollSub: Subscription | null = null;
  private timeoutHandle: any = null;

  constructor(
    private readonly store: VerificationStore,
    private readonly api: VerifierApi
  ) {}

  start(verificationId: string) {
    this.stop(); // clean up old timers first

    this.pollSub = interval(environment.pollingInterval).subscribe(async () => {
      const result = await firstValueFrom(this.api.getVerificationProcess({verificationId}).pipe(take(1)));

      if (result.status !== Status.Pending) {
        this.store.completeVerification(result);
        this.stop();
      }
    });

    this.timeoutHandle = setTimeout(() => {
      const status = this.store.status();
      if (status === Polling || status === Starting) {
        this.store.markTimedOut();
        this.stop();
      }
    }, environment.pollingTimeoutTime);
  }

  stop() {
    this.pollSub?.unsubscribe();
    this.pollSub = null;

    if (this.timeoutHandle) {
      clearTimeout(this.timeoutHandle);
      this.timeoutHandle = null;
    }
  }
}
