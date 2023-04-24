package riot.api.data.engineer.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import riot.api.data.engineer.entity.Version;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.service.ApiInfoService;
import riot.api.data.engineer.service.VersionService;
import riot.api.data.engineer.utils.UtilManager;

@Slf4j
@RestController
@RequestMapping(value = "/ddragon/runes")
public class RunesController {

    private WebClient webClient;
    private final ApiInfoService apiInfoService;

    private final VersionService versionService;

    public RunesController(WebClient webClient, ApiInfoService apiInfoService, VersionService versionService){
        this.webClient = webClient;
        this.apiInfoService = apiInfoService;
        this.versionService = versionService;
    }

    @GetMapping("/get")
    public JsonObject getRunes() {

        String apiName = "/ddragon/runes/get";

        ApiInfo apiInfo = apiInfoService.findOneByName(apiName);

        String uri = new UtilManager().getStringConcat(apiInfo.getApiPrefix(),apiInfo.getApiUrl());

        Version version = versionService.findOneByName();

        if(!StringUtils.isEmpty(uri) && !StringUtils.isEmpty(version.getVersion())){
            String response = webClient.get().uri(uri,version.getVersion()).retrieve().bodyToMono(String.class).block();
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response, JsonObject.class);

            return jsonObject;

        }else{
            return null;
        }
    }

}
