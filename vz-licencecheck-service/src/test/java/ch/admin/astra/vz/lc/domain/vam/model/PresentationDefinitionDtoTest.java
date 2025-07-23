package ch.admin.astra.vz.lc.domain.vam.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static ch.admin.astra.vz.lc.domain.vam.model.PresentationDefinitionDto.*;
import static org.junit.jupiter.api.Assertions.*;

class PresentationDefinitionDtoTest {

    @Test
    void buildPresentationDefinitionDto() {
        String clientName = "clientName";
        String a1 = "a1";
        String a2 = "a2";

        PresentationDefinitionDto result = PresentationDefinitionDto.buildPresentationDefinitionDto(clientName, List.of(a1, a2));

        assertNotNull(result);
        assertDoesNotThrow(() -> UUID.fromString(result.getId()));
        assertEquals(clientName, result.getName());
        assertEquals(DEFAULT_PURPOSE, result.getPurpose());
        var descriptorDto = result.getInputDescriptors().getFirst();
        assertNotNull(descriptorDto);
        assertDoesNotThrow(() -> UUID.fromString(descriptorDto.id()));
        assertEquals(DEFAULT_NAME, descriptorDto.name());
        assertNotNull(descriptorDto.format());
        assertTrue(descriptorDto.format().containsKey(DEFAULT_FORMAT_KEY));
        assertEquals(DEFAULT_PROOF_TYPE, descriptorDto.format().get(DEFAULT_FORMAT_KEY).alg().getFirst());
        assertEquals(DEFAULT_PROOF_TYPE, descriptorDto.format().get(DEFAULT_FORMAT_KEY).keyBindingAlg().getFirst());
        var constraintDto = descriptorDto.constraints();
        assertNotNull(constraintDto);
        assertNotNull(constraintDto.fields().getFirst());
        assertNotNull(constraintDto.fields().getFirst().path().getFirst());
        assertEquals(DEFAULT_FORMATTED_PATH.formatted(a1), constraintDto.fields().getFirst().path().getFirst());
        assertEquals(DEFAULT_FORMATTED_PATH.formatted(a2), constraintDto.fields().get(1).path().getFirst());
    }
}