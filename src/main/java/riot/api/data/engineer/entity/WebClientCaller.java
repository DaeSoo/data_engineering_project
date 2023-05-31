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
    private static final String PAGE = "page";

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

        Optional<Map<String,String>> queryParams = Optional.ofNullable(webClientDTO.getQueryParam());
        Optional<Map<String,String>> paging = Optional.ofNullable(webClientDTO.getPaging());

        if(queryParams.isPresent()){
            for(Map.Entry<String, String> entry : queryParams.get().entrySet()){
                uriBuilder.queryParam(entry.getKey(), entry.getValue());
            }
        }
        if(paging.isPresent()){
            uriBuilder.queryParamIfPresent(PAGE, Optional.ofNullable(paging.get().get(PAGE))).build();
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
