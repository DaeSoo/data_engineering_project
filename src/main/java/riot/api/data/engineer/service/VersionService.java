package riot.api.data.engineer.service;

import org.springframework.web.reactive.function.client.WebClient;
import riot.api.data.engineer.entity.Version;
import riot.api.data.engineer.entity.api.ApiInfo;

import java.util.List;

public interface VersionService {
    Version findOneByCurrentVersion();

    void save(List<Version> versionList);

    List<Version> getVersionList(String response);

    String apiCall(WebClient webClient, ApiInfo apiInfo);
}
