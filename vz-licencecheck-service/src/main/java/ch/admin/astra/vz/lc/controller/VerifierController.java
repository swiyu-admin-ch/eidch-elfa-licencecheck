package ch.admin.astra.vz.lc.controller;

import ch.admin.astra.vz.lc.api.ErrorResponse;
import ch.admin.astra.vz.lc.api.StartVerificationRequest;
import ch.admin.astra.vz.lc.api.UseCaseResponse;
import ch.admin.astra.vz.lc.api.VerificationStatusResponse;
import ch.admin.astra.vz.lc.domain.vam.model.ManagementResponseDto;
import ch.admin.astra.vz.lc.domain.verifier.VerifierService;
import ch.admin.astra.vz.lc.domain.verifier.model.VerificationBeginResponseDto;
import ch.admin.astra.vz.lc.logging.LoggingService;
import ch.admin.astra.vz.lc.mapper.UseCaseMapper;
import ch.admin.astra.vz.lc.mapper.VerificationMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequestMapping("/api/v1/verifier")
@Tag(name = "Verifier", description = "Handles verification of electronic learner drivers licenses")
class VerifierController {

    private final VerifierService verifierService;
    private final UseCaseMapper useCaseMapper;
    private final VerificationMapper verificationMapper;
    private final LoggingService loggingService;

    @Operation(summary = "Return a list of possible use-cases", description = "Retrieves a list of possible use-cases.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(description = "error occurred - see status code and problem object for more information.",
        content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping(value = "/use-cases")
    public ResponseEntity<List<UseCaseResponse>> getUseCases() {
        loggingService.initGetUseCases();

        List<UseCaseResponse> response = useCaseMapper.map(verifierService.getUseCases());
        return ResponseEntity.ok()
            .cacheControl(CacheControl.maxAge(24, TimeUnit.HOURS))
            .body(response);
    }

    @Operation(summary = "Start a new verification process", description = "This endpoint starts a new verification process for a given use-case.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(description = "error occurred - see status code and problem object for more information.",
        content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping(value = "/verify")
    public ResponseEntity<VerificationBeginResponseDto> startVerificationProcess(@Parameter(in = ParameterIn.DEFAULT, required = true, schema = @Schema(implementation = StartVerificationRequest.class)) @Valid @RequestBody() StartVerificationRequest request) {
        loggingService.initStartVerification(request.getUseCaseId());

        VerificationBeginResponseDto responseDto = verifierService.createVerification(request.getUseCaseId());

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Get the current status of a previously initiated verification process.", description = "This endpoint can be used to poll the status and result of a previously initiated verification process.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(description = "error occurred - see status code and problem object for more information.",
        content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping(value = "/verify/{verificationId}")
    public ResponseEntity<VerificationStatusResponse> getVerificationProcess(@Parameter(in = ParameterIn.DEFAULT, required = true, schema = @Schema(implementation = String.class)) @Valid @PathVariable UUID verificationId) {
        loggingService.initGetVerificationProcessStatus(verificationId);

        ManagementResponseDto response = verifierService.getVerificationStatus(verificationId);
        return ResponseEntity.ok(verificationMapper.map(response));
    }
}
