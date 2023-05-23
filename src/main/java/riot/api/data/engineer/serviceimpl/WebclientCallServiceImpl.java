package riot.api.data.engineer.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
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
@Slf4j
public class WebclientCallServiceImpl implements WebclientCallService {
    private final WebClient webClient;

    public WebclientCallServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }


//    @Override
//    public String webClientStringGet(WebClientDTO webClientDTO) {
//        return webClient.get().uri(getUriBuilder(webClientDTO).build()).retrieve().bodyToMono(String.class).block();
//    }
//
//    @Override
//    public String webClientStringPathGet(WebClientDTO webClientDTO, List<String> path) {
//        return webClient.get().uri(getUriBuilder(webClientDTO).build(path)).retrieve().bodyToMono(String.class).block();
//    }

//    @Override
//    public String webclientGet(WebClientDTO webClientDTO) {
//        return webClient.get().uri(uriBuilder -> uriBuilder
//                .scheme(webClientDTO.getScheme())
//                .host(webClientDTO.getHost())
//                .path(webClientDTO.getPath())
//                .build()).retrieve().bodyToMono(String.class).block();
//    }

//    @Override
//    public String webclientGetWithVersion(WebClientDTO webClientDTO, String version) {
//        return webClient.get().uri(uriBuilder -> uriBuilder
//                        .scheme(webClientDTO.getScheme())
//                        .host(webClientDTO.getHost())
//                        .path(webClientDTO.getPath())
//                        .build(version))
//                .retrieve().bodyToMono(String.class).block();
//    }


//    @Override
//    public String getWebClientToString(WebClientDTO webClientDTO){
//        UriBuilder builder =  createMapToParam(webClientDTO, getUriBuilder(webClientDTO));
//        if(CollectionUtils.isNotEmpty(webClientDTO.getPathVariable())){
//            return webClient.get().uri(builder.build(webClientDTO.getPathVariable())).retrieve().bodyToMono(String.class).block();
//        }
//        return webClient.get().uri(builder.build()).retrieve().bodyToMono(String.class).block();
//    }

    @Override
    public String getWebClientToString(WebClientDTO webClientDTO, ApiKey apiKey){
        UriBuilder builder = createMapToParam(webClientDTO, getUriBuilder(webClientDTO));
        URI uri = createURI(builder, webClientDTO.getPathVariable());
        return craeteHeader(uri,apiKey).retrieve().bodyToMono(String.class).block();
//        if(ObjectUtils.isNotEmpty(apiKey)){
//            return webClient.get().uri(uri).header(apiKey.getKeyName(), apiKey.getApiKey()).retrieve().bodyToMono(String.class).block();
//        }
//        return webClient.get().uri(uri).retrieve().bodyToMono(String.class).block();
    }

    @Override
    public List getWebClientToList(WebClientDTO webClientDTO, ApiKey apiKey){
        UriBuilder builder =  createMapToParam(webClientDTO, getUriBuilder(webClientDTO));
        URI uri = createURI(builder, webClientDTO.getPathVariable());
        return craeteHeader(uri,apiKey).retrieve().bodyToMono(List.class).block();
//        if(ObjectUtils.isNotEmpty(apiKey)){
//            return webClient.get().uri(uri).header(apiKey.getKeyName(), apiKey.getApiKey()).retrieve().bodyToMono(List.class).block();
//        }
//        return webClient.get().uri(builder.build()).retrieve().bodyToMono(List.class).block();
    }

    public URI createURI(UriBuilder uriBuilder, List<String> pathVariable){
        if(CollectionUtils.isNotEmpty(pathVariable)){
            return uriBuilder.build(pathVariable.toArray(new String[0]));
        }else{
            return uriBuilder.build();
        }
    }

    public WebClient.RequestHeadersSpec<?> craeteHeader(URI uri, ApiKey apiKey){
        if(ObjectUtils.isNotEmpty(apiKey)){
            return webClient.get().uri(uri).header(apiKey.getKeyName(), apiKey.getApiKey());
        }else {
            return webClient.get().uri(uri);
        }
    }


//    @Override
//    public String webClientPathTokenGet(WebClientDTO webClientDTO, ApiKey apiKey) {
//        return webClient.get().uri(getUriBuilder(webClientDTO).build(webClientDTO.getPathVariable()))
//                .header(apiKey.getKeyName(), apiKey.getApiKey()).retrieve().bodyToMono(String.class).block();
//    }


//    @Override
//    public String webClientPathTokenGet(WebClientDTO webClientDTO, ApiKey apiKey) {
//        return webClient.get().uri(getUriBuilder(webClientDTO).build())
//                .header(apiKey.getKeyName(), apiKey.getApiKey()).retrieve().bodyToMono(String.class).block();
//    }

//    @Override
//    public String webclientGetWithMatchIdWithToken(WebClientDTO webClientDTO, ApiKey apiKey, String matchId) {
//        return webClient.get().uri(uriBuilder -> uriBuilder
//                        .scheme(webClientDTO.getScheme())
//                        .host(webClientDTO.getHost())
//                        .path(webClientDTO.getPath())
//                        .build(matchId))
//                .header(apiKey.getKeyName(), apiKey.getApiKey())
//                .retrieve().bodyToMono(String.class).block();
//    }

//    @Override
//    public String webclientQueryParamGet(WebClientDTO webClientDTO, ApiKey apikey, Map<String, String> queryParam) {
//        return webClient.get().uri(uriBuilder -> uriBuilder
//                .scheme(webClientDTO.getScheme())
//                .host(webClientDTO.getHost())
//                .path(webClientDTO.getPath())
//                .queryParamIfPresent("page", Optional.ofNullable(webClientDTO.getQueryParam().get("page")))
//                .build(queryParam.get("summonerId"))).header(apikey.getKeyName(), apikey.getApiKey()).retrieve().bodyToMono(String.class).block();
//    }

//    @Override
//    public List webclientQueryParamGet(WebClientDTO webClientDTO, ApiKey apikey) {
//        return webClient.get().uri(createApiListUri(webClientDTO)).header(apikey.getKeyName(), apikey.getApiKey()).retrieve().bodyToMono(List.class).block();
//    }
//    @Override
//    public String webclientGetWithParam(WebClient webClient, WebClientDTO webClientDTO) {
//        return webClient.get().uri(uriBuilder -> uriBuilder
//                .scheme(webClientDTO.getScheme())
//                .host(webClientDTO.getHost())
//                .path(webClientDTO.getPath())
//                .queryParamIfPresent("page", Optional.ofNullable(webClientDTO.getQueryParam().get("page")))
//                .build()).retrieve().bodyToMono(String.class).block();
//    }

//    @Override
//    public String webclientGetWithTokenWithPageParam(WebClientDTO webClientDTO, ApiKey apikey) {
//        return webClient.get().uri(uriBuilder -> uriBuilder
//                .scheme(webClientDTO.getScheme())
//                .host(webClientDTO.getHost())
//                .path(webClientDTO.getPath())
//                .queryParamIfPresent("page", Optional.ofNullable(webClientDTO.getQueryParam().get("page")))
//                .build()).header(apikey.getKeyName(), apikey.getApiKey()).retrieve().bodyToMono(String.class).block();
//    }

    public UriBuilder createMapToParam(WebClientDTO webClientDTO, UriBuilder uriBuilder){
        if(MapUtils.isNotEmpty(webClientDTO.getQueryParam())){
            for(Map.Entry<String, String> entry : webClientDTO.getQueryParam().entrySet()){
                uriBuilder.queryParam(entry.getKey(), entry.getValue());
            }
        }
        if(MapUtils.isNotEmpty(webClientDTO.getPaging())){
            uriBuilder.queryParamIfPresent("page", Optional.ofNullable(webClientDTO.getQueryParam().get("page"))).build();
        }
        return uriBuilder;
    }



    public UriBuilder getUriBuilder(WebClientDTO webClientDTO){
        return UriComponentsBuilder.newInstance()
                .scheme(webClientDTO.getScheme())
                .host(webClientDTO.getHost())
                .path(webClientDTO.getPath());
    }

}
