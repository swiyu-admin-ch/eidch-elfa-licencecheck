// File: vz-licencecheck-service/src/test/java/ch/admin/astra/vz/lc/integration/verifiermanagement/client/VerifierServiceImplTest.java
package ch.admin.astra.vz.lc.integration.verifiermanagement.client;

import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.CreateVerificationManagementDto;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.ManagementResponseDto;
import ch.admin.astra.vz.lc.integration.verifiermanagement.exception.VerifierException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.when;

@TestPropertySource(locations={"classpath:application.yml", "classpath:application-ref.yml"})
@ActiveProfiles(value = "ref")
@SpringBootTest(properties="spring.main.lazy-initialization=true")
class VerifierServiceImplTest  {

    @Autowired
    private VerifierServiceClient verifierService;

    @MockitoBean
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