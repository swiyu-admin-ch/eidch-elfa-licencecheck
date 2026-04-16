import {TestBed} from '@angular/core/testing';
import {of} from 'rxjs';

import {Failed, Idle, Polling, Succeeded, VerificationStore} from './verification.store';
import {
  Status,
  UseCase,
  VerificationBeginResponse,
  VerificationErrorResponseCode,
  VerificationState,
  VerifierApi
} from '@app/core/api/generated';

// --------- TEST HELPERS ---------

class VerifierApiMock {
  startVerificationProcess = jest.fn();
}

function future(days = 1): string {
  const d = new Date();
  d.setDate(d.getDate() + days);
  const date = d.toISOString().slice(0, 10);
  const [year, month, day] = date.split('-');
  return `${parseInt(day)}.${parseInt(month)}.${year}`;
}

function past(days = 1): string {
  const d = new Date();
  d.setDate(d.getDate() - days);
  const date = d.toISOString().slice(0, 10);
  const [year, month, day] = date.split('-');
  return `${parseInt(day)}.${parseInt(month)}.${year}`;
}

const mockUseCase: UseCase = {
  id: 'uc-1',
  title: 'driver-licence',
  order: 1,
  attributeGroups: []
};

const beginResponse: VerificationBeginResponse = {
  id: 'verif-1',
  qrCode: 'AAA',
  qrCodeFormat: 'png'
};

const successVerification = (dateOfExpiration: string): VerificationState => ({
  id: 'verif-1',
  status: Status.Success,
  errorCode: null,
  holderAttributes: {
    attributes: {
      dateOfExpiration
    }
  }
});

const failedVerification = (code: VerificationErrorResponseCode): VerificationState => ({
  id: 'verif-1',
  status: Status.Failed,
  errorCode: code,
  holderAttributes: {
    attributes: {}
  }
});

// --------- TESTS ---------

describe('VerificationStore', () => {
  let store: VerificationStore;
  let api: jest.Mocked<VerifierApiMock>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [VerificationStore, {provide: VerifierApi, useClass: VerifierApiMock}]
    });

    store = TestBed.inject(VerificationStore);
    api = TestBed.inject(VerifierApi) as any;

    jest.useFakeTimers(); // only needed if you test timers; safe to keep
  });

  afterEach(() => {
    jest.clearAllTimers();
    jest.useRealTimers();
  });

  it('starts in Idle', () => {
    expect(store.status()).toBe(Idle);
    expect(store.verification()).toBeNull();
    expect(store.useCase()).toBeNull();
  });

  it('setUseCase stores the use case', () => {
    store.setUseCase(mockUseCase);
    expect(store.useCase()).toEqual(mockUseCase);
  });

  it('beginVerification moves to Polling, sets verificationId and qr', async () => {
    // GIVEN
    store.setUseCase(mockUseCase);
    api.startVerificationProcess.mockReturnValue(of(beginResponse));

    // WHEN
    await store.beginVerification();

    // THEN
    expect(api.startVerificationProcess).toHaveBeenCalledWith({
      startVerification: {useCaseId: mockUseCase.id}
    });
    expect(store.status()).toBe(Polling);
    expect(store.qrDataUrl()).toContain('data:image/png;base64,AAA');
  });

  it('completeVerification -> Succeeded, isValid() true when not expired', () => {
    // GIVEN
    const v = successVerification(future(2));

    // WHEN
    store.completeVerification(v);

    // THEN
    expect(store.status()).toBe(Succeeded);
    expect(store.isValid()).toBe(true);
    expect(store.isInvalid()).toBe(false);
  });

  it('completeVerification -> Succeeded, but isValid() false when expired', () => {
    // GIVEN
    const v = successVerification(past(1));

    // WHEN
    store.completeVerification(v);

    // THEN
    expect(store.status()).toBe(Succeeded);
    expect(store.isValid()).toBe(false);
    expect(store.isInvalid()).toBe(false); // it’s still success, just expired attr
  });

  it('Failed + ClientRejected -> isRejected() true, isInvalid() false', () => {
    // GIVEN
    const v = failedVerification(VerificationErrorResponseCode.ClientRejected);

    // WHEN
    store.completeVerification(v);

    // THEN
    expect(store.status()).toBe(Failed);
    expect(store.isRejected()).toBe(true);
    expect(store.isPremature()).toBe(false);
    expect(store.isInvalid()).toBe(false);
  });

  it('Failed + JwtPremature -> isPremature() true', () => {
    // GIVEN
    const v = failedVerification(VerificationErrorResponseCode.JwtPremature);

    // WHEN
    store.completeVerification(v);

    // THEN
    expect(store.status()).toBe(Failed);
    expect(store.isPremature()).toBe(true);
    expect(store.isRejected()).toBe(false);
    expect(store.isInvalid()).toBe(false);
  });

  it('Failed + other error -> isInvalid() true', () => {
    // GIVEN
    const v = failedVerification(VerificationErrorResponseCode.InvalidFormat);

    // WHEN
    store.completeVerification(v);

    // THEN
    expect(store.status()).toBe(Failed);
    expect(store.isInvalid()).toBe(true);
    expect(store.isRejected()).toBe(false);
    expect(store.isPremature()).toBe(false);
  });

  it('reset() brings store back to Idle & clears data', async () => {
    store.setUseCase(mockUseCase);
    api.startVerificationProcess.mockReturnValue(of(beginResponse));
    await store.beginVerification();

    store.reset();

    expect(store.status()).toBe(Idle);
    expect(store.useCase()).toBeNull();
    expect(store.verification()).toBeNull();
    expect(store.qrDataUrl()).toBeNull();
  });
});
