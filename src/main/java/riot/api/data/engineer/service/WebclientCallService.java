package riot.api.data.engineer.service;

import org.springframework.web.reactive.function.client.WebClient;
import riot.api.data.engineer.dto.MatchInfoParam;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.api.ApiKey;

import java.util.List;
import java.util.Map;

public interface WebclientCallService {

    String webclientGet(WebClientDTO webClientDTO);
    String webclientGetWithVersion(WebClientDTO webClientDTO, String version);

    String webclientGetWithParam(WebClient webClient, WebClientDTO webClientDTO);

    String webclientGetWithTokenWithPageParam(WebClientDTO webClientDTO, ApiKey apikey);

    String webclientQueryParamGet(WebClientDTO webClientDTO, ApiKey apikey, Map<String,String> queryParam);

    List<String> webclientQueryParamGet(WebClientDTO webClientDTO, ApiKey apikey, Map<String,String> queryParam, String apiName, MatchInfoParam matchInfoParam);
}
