package riot.api.data.engineer.service;

import org.springframework.web.reactive.function.client.WebClient;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.api.ApiKey;

public interface WebclientCallService {

    String webclientGet(WebClientDTO webClientDTO);
    String webclientGetWithVersion(WebClientDTO webClientDTO, String version);

    String webclientGetWithParam(WebClient webClient, WebClientDTO webClientDTO);

    String webclientGetWithTokenWithPageParam(WebClientDTO webClientDTO, ApiKey apikey);
}
