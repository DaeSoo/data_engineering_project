package riot.api.data.engineer.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@Builder
public class WebClientDTO {
    private static final String schemeHttps = "https";
    String scheme;
    String host;
    String path;
    String apiName;
    List<String> pathVariable;
    Map<String,String> queryParam;
    Map<String,String> paging;

}
