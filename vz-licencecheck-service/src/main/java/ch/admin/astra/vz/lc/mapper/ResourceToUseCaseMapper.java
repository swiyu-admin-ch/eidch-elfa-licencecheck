package ch.admin.astra.vz.lc.mapper;

import ch.admin.astra.vz.lc.domain.verifier.exception.FileMappingException;
import ch.admin.astra.vz.lc.domain.verifier.model.UseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ResourceToUseCaseMapper {

    private final ObjectMapper objectMapper;

    public UseCase map(Resource resource) {
        try {
            return objectMapper.readValue(resource.getInputStream(), UseCase.class);
        } catch (IOException e) {
            throw new FileMappingException(e, resource.getFilename());
        }
    }
}
