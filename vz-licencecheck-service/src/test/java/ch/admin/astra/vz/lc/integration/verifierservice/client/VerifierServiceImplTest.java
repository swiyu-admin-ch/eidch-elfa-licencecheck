package ch.admin.astra.vz.lc.integration.verifierservice.client;

import ch.admin.astra.vz.lc.integration.verifierservice.client.model.CreateVerificationManagementDto;
import ch.admin.astra.vz.lc.integration.verifierservice.client.model.ManagementResponseDto;
import ch.admin.astra.vz.lc.integration.verifierservice.exception.VerifierException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VerifierServiceImplTest  {

    @InjectMocks
    private VerifierServiceImpl verifierService;
    @Mock
    private VerifierApi verifierApi;

    @Test
    void createVerification_success() {
        CreateVerificationManagementDto dto = CreateVerificationManagementDto.builder().build();
        ManagementResponseDto expected = ManagementResponseDto.builder().build();

        when(verifierApi.createVerification(dto)).thenReturn(expected);

        ManagementResponseDto result = verifierService.createVerification(dto);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void createVerification_exception() {
        CreateVerificationManagementDto dto = CreateVerificationManagementDto.builder().build();

        when(verifierApi.createVerification(dto))
                .thenThrow(new VerifierException(HttpStatus.BAD_GATEWAY, "error", false));

        Assertions.assertThrows(VerifierException.class, () -> verifierService.createVerification(dto));
    }
}