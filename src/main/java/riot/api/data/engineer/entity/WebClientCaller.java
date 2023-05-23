package riot.api.data.engineer.entity;

import lombok.Builder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.api.ApiKey;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Builder
public class WebClientCaller {
    private static final String page = "page";

    private WebClient webclient;
    private WebClientDTO webClientDTO;

    public String getWebClientToString(ApiKey apiKey) {
        UriBuilder builder = createMapToParam();
        URI uri = createURI(builder, webClientDTO.getPathVariable());
        return createHeader(uri, apiKey).retrieve().bodyToMono(String.class).block();
    }

    public String getWebClientToString() {
        UriBuilder builder = createMapToParam();
        URI uri = createURI(builder, webClientDTO.getPathVariable());
        return createHeader(uri).retrieve().bodyToMono(String.class).block();
    }

    public List getWebClientToList(ApiKey apiKey) {
        UriBuilder builder = createMapToParam();
        URI uri = createURI(builder, webClientDTO.getPathVariable());
        return createHeader(uri, apiKey).retrieve().bodyToMono(List.class).block();
    }

    public List getWebClientToList() {
        UriBuilder builder = createMapToParam();
        URI uri = createURI(builder, webClientDTO.getPathVariable());
        return createHeader(uri).retrieve().bodyToMono(List.class).block();
    }

    private URI createURI(UriBuilder uriBuilder, List<String> pathVariable){
        if(CollectionUtils.isNotEmpty(pathVariable)){
            return uriBuilder.build(pathVariable.toArray(new String[0]));
        }else{
            return uriBuilder.build();
        }
    }

    private WebClient.RequestHeadersSpec<?> createHeader(URI uri){
            return webclient.get().uri(uri);
    }

    private WebClient.RequestHeadersSpec<?> createHeader(URI uri, ApiKey apiKey){
            return webclient.get().uri(uri).header(apiKey.getKeyName(), apiKey.getApiKey());
    }

    private UriBuilder createMapToParam(){

        UriBuilder uriBuilder = getUriBuilder();

        if(MapUtils.isNotEmpty(webClientDTO.getQueryParam())){
            for(Map.Entry<String, String> entry : webClientDTO.getQueryParam().entrySet()){
                uriBuilder.queryParam(entry.getKey(), entry.getValue());
            }
        }
        if(MapUtils.isNotEmpty(webClientDTO.getPaging())){
            uriBuilder.queryParamIfPresent(page, Optional.ofNullable(webClientDTO.getQueryParam().get(page))).build();
        }
        return uriBuilder;
    }

    private UriBuilder getUriBuilder(){
        return UriComponentsBuilder.newInstance()
                .scheme(webClientDTO.getScheme())
                .host(webClientDTO.getHost())
                .path(webClientDTO.getPath());
    }
}
