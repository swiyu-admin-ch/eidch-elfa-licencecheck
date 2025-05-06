package ch.admin.astra.vz.lc.controller;

import ch.admin.astra.vz.lc.api.StartVerificationDto;
import ch.admin.astra.vz.lc.api.UseCaseDto;
import ch.admin.astra.vz.lc.api.VerificationStateDto;
import ch.admin.astra.vz.lc.domain.verifier.VerificationService;
import ch.admin.astra.vz.lc.domain.verifier.model.VerificationBeginResponseDto;
import ch.admin.astra.vz.lc.logging.LoggingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/verification")
@Tag(name = "Verifier", description = "Handles verification of electronic learner drivers licenses")
class VerificationController {

    private final VerificationService verificationService;
    private final LoggingService loggingService;

    @Operation(summary = "Return a list of possible use-cases", description = "Retrieves a list of possible use-cases.")
    @GetMapping(value = "/use-cases")
    public ResponseEntity<List<UseCaseDto>> getUseCases() {
        loggingService.initGetUseCases();

        List<UseCaseDto> response = verificationService.getUseCases();
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(24, TimeUnit.HOURS))
                .body(response);
    }

    @Operation(summary = "Start a new verification process", description = "This endpoint starts a new verification process for a given use-case.")
    @PostMapping(value = "/verify")
    public VerificationBeginResponseDto startVerificationProcess(@Valid @RequestBody() StartVerificationDto request) {
        loggingService.initStartVerification(request.useCaseId());
        return verificationService.createVerification(request.useCaseId());
    }

    @Operation(summary = "Get the current status of a previously initiated verification process.", description = "This endpoint can be used to poll the status and result of a previously initiated verification process.")
    @GetMapping(value = "/verify/{verificationId}")
    public VerificationStateDto getVerificationProcess(@Valid @PathVariable UUID verificationId) {
        loggingService.initGetVerificationProcessStatus(verificationId);

        return verificationService.getVerificationStatus(verificationId);
    }
}
