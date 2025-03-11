package ch.admin.astra.vz.lc.domain.vam.service;

import ch.admin.astra.vz.lc.domain.vam.api.VerifierAgentManagementApi;
import ch.admin.astra.vz.lc.domain.vam.exception.VAMException;
import ch.admin.astra.vz.lc.domain.vam.model.CreateVerificationManagementDto;
import ch.admin.astra.vz.lc.domain.vam.model.ManagementResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class VerifierAgentManagementImplTest {

    @InjectMocks
    private VerifierAgentManagementImpl verifierAgentManagement;

    @Mock
    private VerifierAgentManagementApi verifierAgentManagementApi;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Call<ManagementResponseDto> call;

    @Mock
    private Response<ManagementResponseDto> responseMock;

    @Test
    void createVerification_success() throws IOException {
        CreateVerificationManagementDto createVerificationManagementDto = CreateVerificationManagementDto.builder().build();
        Mockito.when(verifierAgentManagementApi.createVerification(createVerificationManagementDto)).thenReturn(call);

        ManagementResponseDto expected = ManagementResponseDto.builder().build();
        Mockito.when(call.execute()).thenReturn(Response.success(expected));

        ManagementResponseDto result = verifierAgentManagement.createVerification(createVerificationManagementDto);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void createVerification_exception() throws IOException {
        CreateVerificationManagementDto createVerificationManagementDto = CreateVerificationManagementDto.builder().build();
        Mockito.when(verifierAgentManagementApi.createVerification(createVerificationManagementDto)).thenReturn(call);
        Mockito.when(call.execute()).thenThrow(IOException.class);

        Assertions.assertThrows(VAMException.class, () -> verifierAgentManagement.createVerification(createVerificationManagementDto));
    }
}