package ch.admin.astra.vz.lc.modules.verification.service.usecase;

import ch.admin.astra.vz.lc.junit.VZIntegrationTest;
import ch.admin.astra.vz.lc.modules.verification.domain.usecase.UseCase;
import ch.admin.astra.vz.lc.modules.verification.exception.UseCaseNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@VZIntegrationTest
class UseCaseCacheTestIT {

    private static final UUID EXISTING_USE_CASE = UUID.fromString("c2041c31-db6b-4cf1-871d-6a24d400159b");

    @Autowired
    private UseCaseCache useCaseCache;

    @Test
    void testGetUseCases_existingFile_isReturned() {
        List<UseCase> useCaseList = useCaseCache.getUseCases();
        assertThat(useCaseList).isNotNull().hasSize(1);
        assertThat(useCaseList.getFirst().getId()).isEqualTo(EXISTING_USE_CASE);
    }

    @Test
    void testGetUseCaseById_existingFile_isReturned() {
        UseCase useCase = useCaseCache.getUseCaseById(EXISTING_USE_CASE);
        assertThat(useCase).isNotNull();
        assertThat(useCase.getId()).isEqualTo(EXISTING_USE_CASE);
    }

    @Test
    void testGetUseCaseById_UseCaseNotFoundException() {
        UUID useCaseId = UUID.fromString("d9394767-6d8e-4d30-bcd5-1cecb39d8cd4");
        assertThatExceptionOfType(UseCaseNotFoundException.class)
            .isThrownBy(() -> useCaseCache.getUseCaseById(useCaseId));
    }
}