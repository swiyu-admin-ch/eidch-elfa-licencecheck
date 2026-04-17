import {computed, inject, Injectable, signal} from '@angular/core';
import {firstValueFrom, take} from 'rxjs';
import {
  Status,
  UseCase,
  VerificationBeginResponse,
  VerificationErrorResponseCode,
  VerificationState,
  VerifierApi
} from '@app/core/api/generated';
import {DateUtils} from '@app/core/utils';
import {VerificationPollingRunner} from '@app/_services/VerificationPollingRunner';
import {QrCodeUtil} from '@app/core/utils/qr-code-util';

enum VerificationFlowStatus {
  Idle = 'idle',
  Starting = 'starting',
  Polling = 'polling',
  Succeeded = 'succeeded',
  Failed = 'failed',
  TimedOut = 'timedOut'
}

export const {Idle, Succeeded, TimedOut, Polling, Starting, Failed} = VerificationFlowStatus;

@Injectable({providedIn: 'root'})
export class VerificationStore {
  private readonly api = inject(VerifierApi);
  private readonly pollingRunner = new VerificationPollingRunner(this, this.api);

  // --- STATE (signals)
  private readonly _selectedLicenceType = signal<string | null>(null);
  private readonly _useCase = signal<UseCase | null>(null);
  private readonly _verification = signal<VerificationState | null>(null);
  private readonly _verificationId = signal<string | null>(null);
  private readonly _beginResponse = signal<VerificationBeginResponse | null>(null);
  private readonly _status = signal<VerificationFlowStatus>(Idle);
  private readonly _errorCode = signal<VerificationErrorResponseCode | null>(null);

  // --- SELECTORS / COMPUTEDS
  readonly selectedLicenceType = this._selectedLicenceType.asReadonly();
  readonly useCase = this._useCase.asReadonly();
  readonly verification = this._verification.asReadonly();
  readonly status = this._status.asReadonly();

  readonly isValid = computed(() => {
    const v = this._verification();
    return v?.status === Status.Success && this.isDateOfExpirationValid(v);
  });

  readonly isPremature = computed(
    () => this._status() === Failed && this._errorCode() === VerificationErrorResponseCode.JwtPremature
  );

  readonly isRejected = computed(
    () => this._status() === Failed && this._errorCode() === VerificationErrorResponseCode.ClientRejected
  );

  readonly isInvalid = computed(() => {
    if (this._status() !== Failed) return false;
    const e = this._errorCode();
    return (
      e != null &&
      e !== VerificationErrorResponseCode.ClientRejected &&
      e !== VerificationErrorResponseCode.JwtPremature
    );
  });

  readonly isTerminal = computed(() => {
    return [Succeeded, Failed, TimedOut].includes(this._status());
  });

  readonly qrDataUrl = computed<string | null>(() => {
    const r = this._beginResponse();
    if (!r) return null;
    const sanitizedQrCode = QrCodeUtil.sanitize(r.qrCode, r.qrCodeFormat);
    if (!sanitizedQrCode) return null;
    return `data:image/${r.qrCodeFormat};base64,${sanitizedQrCode}`;
  });

  // --- COMMANDS (public API)
  reset() {
    this.pollingRunner.stop();

    this._status.set(Idle);
    this._useCase.set(null);
    this._verification.set(null);
    this._verificationId.set(null);
    this._beginResponse.set(null);
    this._errorCode.set(null);
  }

  setSelectedLicenceType(licenceType: string | null): void {
    this._selectedLicenceType.set(licenceType);
  }

  setUseCase(useCase: UseCase | null) {
    this._useCase.set(useCase);
  }

  async beginVerification() {
    if (!this._useCase()) return;

    this._status.set(Starting);
    const res = await firstValueFrom(
      this.api.startVerificationProcess({startVerification: {useCaseId: this.useCase().id}}).pipe(take(1))
    );

    this._beginResponse.set(res);
    this._verificationId.set(res.id);
    this._status.set(Polling);

    this.pollingRunner.start(res.id);
  }

  completeVerification(result: VerificationState) {
    this._verification.set(result);
    this._errorCode.set(result.errorCode ?? null);
    this._status.set(result.status === Status.Success ? Succeeded : Failed);
  }

  markTimedOut() {
    this._status.set(TimedOut);
  }

  // --- HELPERS
  private isDateOfExpirationValid(v: VerificationState | null | undefined): boolean {
    const expirationStr = v?.holderAttributes?.attributes?.dateOfExpiration;
    if (!expirationStr) return true;
    const dateOfExpiration = DateUtils.parseDate(expirationStr);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    return !!dateOfExpiration && dateOfExpiration >= today;
  }
}
