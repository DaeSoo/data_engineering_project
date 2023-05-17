package riot.api.data.engineer.serviceimpl;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.api.ApiKey;
import riot.api.data.engineer.service.WebclientCallService;

import java.net.URI;
import java.util.List;
import java.util.Map;
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
        return webClient.get().uri(uriBuilder -> uriBuilder
                        .scheme(webClientDTO.getScheme())
                        .host(webClientDTO.getHost())
                        .path(webClientDTO.getPath())
                        .build(version))
                .retrieve().bodyToMono(String.class).block();
    }

    @Override
    public String webclientGetWithMatchIdWithToken(WebClientDTO webClientDTO, ApiKey apiKey, String matchId) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme(webClientDTO.getScheme())
                        .host(webClientDTO.getHost())
                        .path(webClientDTO.getPath())
                        .build(matchId))
                .header(apiKey.getKeyName(), apiKey.getApiKey())
                .retrieve().bodyToMono(String.class).block();
    }

    @Override
    public String webclientQueryParamGet(WebClientDTO webClientDTO, ApiKey apikey, Map<String, String> queryParam) {
        return webClient.get().uri(uriBuilder -> uriBuilder
                .scheme(webClientDTO.getScheme())
                .host(webClientDTO.getHost())
                .path(webClientDTO.getPath())
                .queryParamIfPresent("page", Optional.ofNullable(webClientDTO.getQueryParam().get("page")))
                .build(queryParam.get("summonerId"))).header(apikey.getKeyName(), apikey.getApiKey()).retrieve().bodyToMono(String.class).block();
    }

    @Override
    public List webclientQueryParamGet(WebClientDTO webClientDTO, ApiKey apikey, String apiName, Map<String, String> queryParams) {
        return webClient.get().uri(createApiListUri(webClientDTO,queryParams, apiName)).header(apikey.getKeyName(), apikey.getApiKey()).retrieve().bodyToMono(List.class).block();
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

    public URI createApiListUri(WebClientDTO webClientDTO, Map<String, String> queryParams, String apiName){
        UriBuilder uriBuilder = getUriBuilder(webClientDTO);
        if(queryParams.size() != 0){
            for(Map.Entry<String, String> entry : queryParams.entrySet()){
                uriBuilder.queryParam(entry.getKey(), entry.getValue());
            }
            return uriBuilder.queryParamIfPresent("page", Optional.ofNullable(webClientDTO.getQueryParam().get("page")))
                    .build(webClientDTO.getQueryParam().get(apiName));
        }else {
            return uriBuilder.build();
        }
    }
    public UriBuilder getUriBuilder(WebClientDTO webClientDTO){
        return UriComponentsBuilder.newInstance()
                .scheme(webClientDTO.getScheme())
                .host(webClientDTO.getHost())
                .path(webClientDTO.getPath());
    }
}
