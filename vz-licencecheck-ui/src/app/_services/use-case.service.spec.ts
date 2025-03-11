import {TestBed} from '@angular/core/testing';
import {UseCaseService} from './use-case.service';
import {UseCaseResponse, VerificationStatus, VerificationStatusResponse} from '@app/core/api/generated';

describe('UseCaseService', () => {
  let useCaseService: UseCaseService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    useCaseService = TestBed.inject(UseCaseService);
  });

  describe('setUseCase', () => {
    it('should set the use case', () => {
      const useCase: UseCaseResponse = {
        id: '1',
        title: 'Test',
        description: 'Description',
        order: 1,
        attributeGroups: []
      };
      useCaseService.setUseCase(useCase);
      expect(useCaseService.getUseCase()).toEqual(useCase);
    });
  });

  describe('setVerificationResponse', () => {
    it('should set the verification response', () => {
      const response: VerificationStatusResponse = {id: '1', status: VerificationStatus.Success};
      useCaseService.setVerificationResponse(response);
      expect(useCaseService.getVerificationResponse()).toEqual(response);
    });
  });
});
