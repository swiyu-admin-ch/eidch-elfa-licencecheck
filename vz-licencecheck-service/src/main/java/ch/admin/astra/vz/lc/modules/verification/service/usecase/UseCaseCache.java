package ch.admin.astra.vz.lc.modules.verification.service.usecase;

import ch.admin.astra.vz.lc.modules.verification.domain.usecase.UseCase;
import ch.admin.astra.vz.lc.modules.verification.exception.UseCaseNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

/**
 * Cache that contains all existing use cases as {@link UseCase} instances from the /resources/use-cases folder
 */
@RequiredArgsConstructor
@Slf4j
public class UseCaseCache {

    private final ConcurrentMap<UUID, UseCase> useCaseCache;

    /**
     * Loads all {@link UseCase} instances from the cache.
     *
     * @return List of all {@link UseCase} instances
     */
    public List<UseCase> getUseCases() {
        return useCaseCache.values().stream().toList();
    }

    /**
     * Loads a {@link UseCase} from the cache and returns it.
     *
     * @param useCaseId identifier for {@link UseCase}
     * @return UseCase with the given useCaseId
     * @throws UseCaseNotFoundException when the use case with the given useCaseId cannot be found
     */
    public UseCase getUseCaseById(UUID useCaseId) {
        log.debug("Get UseCase by ID");
        if (!useCaseCache.containsKey(useCaseId)) {
            log.error("UseCase not found");
            throw new UseCaseNotFoundException(useCaseId);
        }
        return useCaseCache.get(useCaseId);
    }
}
