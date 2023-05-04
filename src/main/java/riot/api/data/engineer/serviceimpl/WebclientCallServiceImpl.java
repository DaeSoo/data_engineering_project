package riot.api.data.engineer.serviceimpl;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import riot.api.data.engineer.dto.MatchInfoParam;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.api.ApiKey;
import riot.api.data.engineer.service.WebclientCallService;

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
    public String webclientQueryParamGet(WebClientDTO webClientDTO, ApiKey apikey, Map<String,String> queryParam) {
            return webClient.get().uri(uriBuilder -> uriBuilder
                    .scheme(webClientDTO.getScheme())
                    .host(webClientDTO.getHost())
                    .path(webClientDTO.getPath())
                    .queryParamIfPresent("page", Optional.ofNullable(webClientDTO.getQueryParam().get("page")))
                    .build(queryParam.get("summonerId"))).header(apikey.getKeyName(), apikey.getApiKey()).retrieve().bodyToMono(String.class).block();
    }

    @Override
    public List<String> webclientQueryParamGet(WebClientDTO webClientDTO, ApiKey apikey, Map<String,String> queryParam, String apiName, MatchInfoParam matchInfoParam) {
        return webClient.get().uri(uriBuilder -> uriBuilder
                .scheme(webClientDTO.getScheme())
                .host(webClientDTO.getHost())
                .path(webClientDTO.getPath())
//                .queryParam("startTime" ,matchInfoParam.getStartTime())
//                .queryParam("endTime", matchInfoParam.getEndTime())
                .queryParam("type", matchInfoParam.getType())
                .queryParam("count", matchInfoParam.getCount())
                .queryParam("start", matchInfoParam.getStart())
                .queryParamIfPresent("page", Optional.ofNullable(webClientDTO.getQueryParam().get("page")))
                .build(queryParam.get(apiName))).header(apikey.getKeyName(), apikey.getApiKey()).retrieve().bodyToMono(List.class).block();
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
