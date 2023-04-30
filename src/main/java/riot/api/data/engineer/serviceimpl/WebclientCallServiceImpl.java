package riot.api.data.engineer.serviceimpl;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.api.ApiKey;
import riot.api.data.engineer.service.WebclientCallService;

import java.util.Optional;

@Service
public class WebclientCallServiceImpl implements WebclientCallService {
    private final WebClient webClient;

    public WebclientCallServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }


    @Override
    public String webclientGet(WebClientDTO webClientDTO) {
        return webClient.get().uri(uriBuilder -> uriBuilder
                .scheme(webClientDTO.getScheme())
                .host(webClientDTO.getHost())
                .path(webClientDTO.getPath())
                .build()).retrieve().bodyToMono(String.class).block();
    }

    @Override
    public String webclientGetWithVersion(WebClientDTO webClientDTO, String version) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme(webClientDTO.getScheme())
                        .host(webClientDTO.getHost())
                        .path(webClientDTO.getPath())
                        .build(version))
                .retrieve().bodyToMono(String.class).block();
    }


    @Override
    public String webclientGetWithParam(WebClient webClient, WebClientDTO webClientDTO) {
        return webClient.get().uri(uriBuilder -> uriBuilder
                .scheme(webClientDTO.getScheme())
                .host(webClientDTO.getHost())
                .path(webClientDTO.getPath())
                .queryParamIfPresent("page", Optional.ofNullable(webClientDTO.getQueryParam().get("page")))
                .build()).retrieve().bodyToMono(String.class).block();
    }

    @Override
    public String webclientGetWithTokenWithPageParam(WebClientDTO webClientDTO, ApiKey apikey) {
        return webClient.get().uri(uriBuilder -> uriBuilder
                .scheme(webClientDTO.getScheme())
                .host(webClientDTO.getHost())
                .path(webClientDTO.getPath())
                .queryParamIfPresent("page", Optional.ofNullable(webClientDTO.getQueryParam().get("page")))
                .build()).header(apikey.getKeyName(), apikey.getApiKey()).retrieve().bodyToMono(String.class).block();
    }

}
