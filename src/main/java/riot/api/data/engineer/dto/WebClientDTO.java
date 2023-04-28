package riot.api.data.engineer.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Setter
@Getter
public class WebClientDTO {
    private static final String schemeHttps = "https";
    String scheme;
    String host;
    String path;

//    List<Map<String,Object>> queryParam;

    Map<String,String> queryParam;

    public WebClientDTO(String host,String path,Map<String,String> queryParam){
        this.scheme = schemeHttps;
        this.host = host;
        this.path = path;
        this.queryParam = queryParam;

    }

}
