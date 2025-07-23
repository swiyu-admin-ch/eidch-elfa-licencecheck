import {Injectable} from '@angular/core';
import {Status, UseCase, VerificationErrorResponseCode, VerificationState} from '@app/core/api/generated';

@Injectable({
  providedIn: 'root'
})
export class UseCaseService {
  private useCase: UseCase;
  private verificationState: VerificationState;

  private timeout = false;

  reset() {
    this.useCase = null;
    this.verificationState = null;
    this.timeout = false;
  }

  setUseCase(useCase: UseCase) {
    this.useCase = useCase;
  }

  setTimeout() {
    this.timeout = true;
  }

  getUseCase(): UseCase {
    return this.useCase;
  }

  setVerificationState(response: VerificationState) {
    this.verificationState = response;
  }

  getVerificationState(): VerificationState {
    return this.verificationState;
  }

  isVcValid(): boolean {
    return this.verificationState?.status === Status.Success;
  }

  isTimeout(): boolean {
    return this.timeout;
  }

  isRejected(): boolean {
    return (
      this.verificationState?.status === Status.Failed &&
      this.verificationState?.errorCode === VerificationErrorResponseCode.ClientRejected
    );
  }

  isVcInvalid(): boolean {
    return (
      this.verificationState?.status === Status.Failed &&
      Object.values(VerificationErrorResponseCode)
        .filter(err => VerificationErrorResponseCode.ClientRejected !== err) // Ignore, separate screen for ClientRejected
        .includes(this.verificationState?.errorCode)
    );
  }
}
