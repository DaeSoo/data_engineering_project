package riot.api.data.engineer.service;

import org.springframework.web.reactive.function.client.WebClient;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.api.ApiKey;

import java.util.Map;

public interface WebclientCallService {

    String webclientGet(WebClient webClient, WebClientDTO webClientDTO);

    String webclientGetWithToken(WebClientDTO webClientDTO, ApiKey apikey);

    String webclientQueryParamGet(WebClientDTO webClientDTO, ApiKey apikey, Map<String,String> queryParam);
}
