package riot.api.data.engineer.service;

import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.api.ApiKey;

import java.util.List;

public interface WebclientCallService {

//    String webClientStringGet(WebClientDTO webClientDTO);
//    String webclientGet(WebClientDTO webClientDTO);

//    String webClientStringPathGet(WebClientDTO webClientDTO, List<String> path);
//    String webclientGetWithVersion(WebClientDTO webClientDTO, String version);

//    String webClientPathTokenGet(WebClientDTO webClientDTO, ApiKey apiKey ,List<String> path);


//    String webClientPathTokenGet(WebClientDTO webClientDTO, ApiKey apiKey);

//    String getWebClientToString(WebClientDTO webClientDTO);

//    String webclientGetWithParam(WebClient webClient, WebClientDTO webClientDTO);

//    String webclientGetWithTokenWithPageParam(WebClientDTO webClientDTO, ApiKey apikey);

//    String webclientGetWithMatchIdWithToken(WebClientDTO webClientDTO,ApiKey apiKey,String matchId);

//    String webclientQueryParamGet(WebClientDTO webClientDTO, ApiKey apikey, Map<String,String> queryParam);

//    List<String> webclientQueryParamGet(WebClientDTO webClientDTO, ApiKey apikey, String apiName, Map<String, String> queryParams);

//    List<String> webclientQueryParamGet(WebClientDTO webClientDTO, ApiKey apikey);


    String getWebClientToString(WebClientDTO webClientDTO, ApiKey apiKey);
    List<String> getWebClientToList(WebClientDTO webClientDTO, ApiKey apiKey);

}
