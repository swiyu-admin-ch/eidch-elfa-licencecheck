package ch.admin.astra.vz.lc.domain.verifier;

import ch.admin.astra.vz.lc.domain.verifier.exception.UseCaseNotFoundException;
import ch.admin.astra.vz.lc.domain.verifier.model.UseCase;

import java.util.List;
import java.util.UUID;

/**
 * Cache that contains all existing use cases as {@link UseCase} instances from the /resources/use-cases folder
 */
public interface UseCaseCache {

    /**
     * Loads all {@link UseCase} instances from the cache.
     *
     * @return List of all {@link UseCase} instances
     */
    List<UseCase> getUseCases();

    /**
     * Loads a {@link UseCase} from the cache and returns it.
     *
     * @param useCaseId identifier for {@link UseCase}
     * @return UseCase with the given useCaseId
     * @throws UseCaseNotFoundException when the use case with the given useCaseId cannot be found
     */
    UseCase getUseCaseById(UUID useCaseId);
}
