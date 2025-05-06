package ch.admin.astra.vz.lc.domain.vam.service;

import ch.admin.astra.vz.lc.domain.qrcode.exception.ImageHandlingException;
import ch.admin.astra.vz.lc.domain.vam.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Base64;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Mock Implementation class for the {@link VerifierAgentManagementClient} interface to test locally.
 */
@RequiredArgsConstructor
public class MockVerifierAgentManagementImpl implements VerifierAgentManagementClient {

    private final Boolean failResponse;
    private final ErrorCodeDto errorCode;

    private final Random random = new Random();

    @Override
    public ManagementResponseDto createVerification(CreateVerificationManagementDto createVerificationManagementDto) {
        return buildSuccessResponse(UUID.fromString("534a8d81-081f-4f01-9e37-38856c8b06e4"), VerificationState.PENDING);
    }

    @Override
    public ManagementResponseDto getVerificationStatus(UUID verificationId) {
        int i = random.nextInt(3);

        if (Boolean.TRUE.equals(failResponse)) {
            return buildFailedResponse(verificationId, errorCode);
        }

        return buildSuccessResponse(verificationId, i == 1 ? VerificationState.SUCCESS : VerificationState.PENDING);
    }

    private ManagementResponseDto buildSuccessResponse(UUID verificationId, VerificationState status) {
        return ManagementResponseDto.builder()
            .id(verificationId)
            .state(status)
            .verificationUrl("https://www.eid.admin.ch/")
            .walletResponse(ResponseDataDto.builder()
                .errorCode(null)
                .credentialSubjectData(Map.ofEntries(
                    new SimpleEntry<>("photoImage", getPhotoImageAsString()),
                    new SimpleEntry<>("categoryCode", "B"),
                    new SimpleEntry<>("categoryIcon", getCategoryImageAsString()),
                    new SimpleEntry<>("categoryRestrictions", "10.01 Handschaltung"),
                    new SimpleEntry<>("restrictionsA", "05.05 Fahren nur mit Beifahrer, der im Besitz eines Führerausweises sein muss"),
                    new SimpleEntry<>("restrictionsB", "05.04 Fahren nur mit Beifahrer"),
                    new SimpleEntry<>("firstName", "Seraina Manuela"),
                    new SimpleEntry<>("lastName", "Muster"),
                    new SimpleEntry<>("dateOfExpiration", "31.12.2025"),
                        new SimpleEntry<>("hometown", "Bern"),
                    new SimpleEntry<>("faberPin", "123456789"),
                    new SimpleEntry<>("licenceNumber", "123456789001"),
                    new SimpleEntry<>("dateOfBirth", "01.01.2000"),
                    new SimpleEntry<>("issuerEntity", "BE"),
                        new SimpleEntry<>("issuerEntityDate", "01.01.2023"),
                        new SimpleEntry<>("signatureImage", getSampleSignatureImageAsString()),
                        new SimpleEntry<>("policeQRImage", getSamplePoliceQRImageAsString())))
                .build())
            .build();
    }

    @SuppressWarnings("java:S1144")
    private ManagementResponseDto buildFailedResponse(UUID verificationId, ErrorCodeDto errorCode) {
        return ManagementResponseDto.builder()
            .id(verificationId)
            .state(VerificationState.FAILED)
            .verificationUrl("https://www.eid.admin.ch/")
            .walletResponse(ResponseDataDto.builder()
                .errorCode(errorCode)
                .credentialSubjectData(Map.of()).build())
            .build();
    }

    private String getPhotoImageAsString() {
        try {
            return Base64.getEncoder()
                .encodeToString(new ClassPathResource("sample/sampleProfilePicture.png").getContentAsByteArray());
        } catch (IOException e) {
            throw new ImageHandlingException(e.getMessage(), e);
        }
    }

    private String getCategoryImageAsString() {
        try {
            return Base64.getEncoder()
                .encodeToString(new ClassPathResource("sample/kat_b.png").getContentAsByteArray());
        } catch (IOException e) {
            throw new ImageHandlingException(e.getMessage(), e);
        }
    }

    private String getSampleSignatureImageAsString() {
        try {
            return Base64.getEncoder()
                    .encodeToString(new ClassPathResource("sample/sampleSignature.png").getContentAsByteArray());
        } catch (IOException e) {
            throw new ImageHandlingException(e.getMessage(), e);
        }
    }

    private String getSamplePoliceQRImageAsString() {
        try {
            return Base64.getEncoder()
                    .encodeToString(new ClassPathResource("sample/samplePoliceQRImage.png").getContentAsByteArray());
        } catch (IOException e) {
            throw new ImageHandlingException(e.getMessage(), e);
        }
    }
}
