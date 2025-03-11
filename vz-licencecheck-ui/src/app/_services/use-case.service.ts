import {Injectable} from '@angular/core';
import {ErrorCode, UseCaseResponse, VerificationStatus, VerificationStatusResponse} from '@app/core/api/generated';

@Injectable({
  providedIn: 'root'
})
export class UseCaseService {
  private useCase: UseCaseResponse;
  private verificationResponse: VerificationStatusResponse;

  private timeout = false;

  reset() {
    this.useCase = null;
    this.verificationResponse = null;
    this.timeout = false;
  }

  setUseCase(useCase: UseCaseResponse) {
    this.useCase = useCase;
  }

  setTimeout() {
    this.timeout = true;
  }

  getUseCase(): UseCaseResponse {
    return this.useCase;
  }

  setVerificationResponse(response: VerificationStatusResponse) {
    this.verificationResponse = response;
  }

  getVerificationResponse(): VerificationStatusResponse {
    return this.verificationResponse;
  }

  isVcValid(): boolean {
    return this.verificationResponse?.status === VerificationStatus.Success;
  }

  isTimeout(): boolean {
    return this.timeout;
  }

  isRejected(): boolean {
    return (
      this.verificationResponse?.status === VerificationStatus.Failed &&
      this.verificationResponse?.errorCode === ErrorCode.ClientRejected
    );
  }

  isVcInvalid(): boolean {
    return (
      this.verificationResponse?.status === VerificationStatus.Failed &&
      Object.values(ErrorCode)
        .filter(err => ErrorCode.ClientRejected !== err) // Ignore, separate screen for ClientRejected
        .includes(this.verificationResponse?.errorCode)
    );
  }
}
