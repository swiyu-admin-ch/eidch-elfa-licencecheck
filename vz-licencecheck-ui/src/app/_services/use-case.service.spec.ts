import {TestBed} from '@angular/core/testing';
import {UseCaseService} from './use-case.service';
import {Status, UseCase, VerificationState} from '@app/core/api/generated';

describe('UseCaseService', () => {
  let useCaseService: UseCaseService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    useCaseService = TestBed.inject(UseCaseService);
  });

  describe('setUseCase', () => {
    it('should set the use case', () => {
      const useCase: UseCase = {
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

  describe('setVerificationState', () => {
    it('should set the verification response', () => {
      const response: VerificationState = {id: '1', status: Status.Success};
      useCaseService.setVerificationState(response);
      expect(useCaseService.getVerificationState()).toEqual(response);
    });
  });
});
