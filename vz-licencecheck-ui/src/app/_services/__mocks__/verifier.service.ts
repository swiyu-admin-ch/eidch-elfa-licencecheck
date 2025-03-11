import {UseCaseResponse} from '@app/core/api/generated';

export const dummyUseCases: UseCaseResponse[] = [
  {id: '1', title: 'Title1', description: 'Desc1', order: 1, attributeGroups: []},
  {id: '2', title: 'Title2', description: 'Desc2', order: 2, attributeGroups: []}
];
export const mockGetUseCases = jest.fn().mockReturnValue(dummyUseCases);
export const mockStartVerificationProcess = jest.fn();
export const mockGetVerificationProcess = jest.fn();

const mock = jest.fn().mockImplementation(() => {
  return {
    getUseCases: mockGetUseCases,
    startVerificationProcess: mockStartVerificationProcess,
    getVerificationProcess: mockGetVerificationProcess
  };
});

export default mock;
