package ch.admin.astra.vz.lc.modules.verification.service.usecase;

import ch.admin.astra.vz.lc.modules.verification.domain.usecase.UseCase;
import ch.admin.astra.vz.lc.modules.verification.exception.FileStorageException;
import ch.admin.astra.vz.lc.modules.verification.mapper.ResourceToUseCaseMapper;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class UseCaseCacheConfiguration {

    private final ResourceToUseCaseMapper resourceToUseCaseMapper;

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_APPLICATION)
    public UseCaseCache getUseCaseCache(@Value("${verifier.use-case-folderPath}") String folderPath) {
        List<Resource> useCaseResourceList = findAllFilesInDirectory(folderPath);
        ConcurrentMap<UUID, UseCase> useCaseCache = mapToUseCases(useCaseResourceList);
        return new UseCaseCache(useCaseCache);
    }

    @NotNull
    private List<Resource> findAllFilesInDirectory(String folderPath) {
        List<Resource> useCaseResourceList = new ArrayList<>();

        ClassLoader cl = this.getClass().getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);

        try {
            Resource[] useCaseResources = resolver.getResources("classpath:" + folderPath + "/*.json");
            Collections.addAll(useCaseResourceList, useCaseResources);
        }
        catch (IOException e) {
            throw new FileStorageException(e);
        }

        return useCaseResourceList;
    }


    private ConcurrentMap<UUID, UseCase> mapToUseCases(List<Resource> resourceList) {
        return resourceList.stream()
                .map(resourceToUseCaseMapper::map)
                .collect(Collectors.toConcurrentMap(UseCase::getId, useCase -> useCase));
    }
}