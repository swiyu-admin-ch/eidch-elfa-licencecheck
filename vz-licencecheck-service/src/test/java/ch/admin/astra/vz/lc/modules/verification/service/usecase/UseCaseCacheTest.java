package ch.admin.astra.vz.lc.modules.verification.service.usecase;

import ch.admin.astra.vz.lc.modules.verification.domain.usecase.UseCase;
import ch.admin.astra.vz.lc.modules.verification.exception.UseCaseNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations="classpath:application.yml")
@SpringBootTest(properties="spring.main.lazy-initialization=true")
class UseCaseCacheTest {

    private static final UUID EXISTING_USE_CASE = UUID.fromString("c2041c31-db6b-4cf1-871d-6a24d400159b");

    @Autowired
    private UseCaseCache fileService;

    @Test
    void testGetUseCases_existingFile_isReturned() {
        List<UseCase> useCaseList = fileService.getUseCases();
        assertNotNull(useCaseList);
        assertEquals(1, useCaseList.size());
        assertEquals(EXISTING_USE_CASE, useCaseList.getFirst().getId());
    }

    @Test
    void testGetUseCaseById_existingFile_isReturned() {
        UseCase useCase = fileService.getUseCaseById(EXISTING_USE_CASE);
        assertNotNull(useCase);
        assertEquals(EXISTING_USE_CASE, useCase.getId());
    }

    @Test
    void testGetUseCaseById_UseCaseNotFoundException() {
        UUID useCaseId = UUID.fromString("d9394767-6d8e-4d30-bcd5-1cecb39d8cd4");
        Assertions.assertThrows(UseCaseNotFoundException.class, () -> fileService.getUseCaseById(useCaseId));
    }
}