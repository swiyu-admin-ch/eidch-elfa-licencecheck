package ch.admin.astra.vz.lc.domain.verifier;

import ch.admin.astra.vz.lc.domain.vam.model.ManagementResponseDto;
import ch.admin.astra.vz.lc.domain.verifier.model.UseCase;
import ch.admin.astra.vz.lc.domain.verifier.model.VerificationBeginResponseDto;

import java.util.List;
import java.util.UUID;

public interface VerifierService {

    List<UseCase> getUseCases();

    VerificationBeginResponseDto createVerification(UUID useCaseId);

    ManagementResponseDto getVerificationStatus(UUID verificationId);
}
