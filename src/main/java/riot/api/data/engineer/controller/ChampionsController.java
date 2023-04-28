package riot.api.data.engineer.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import riot.api.data.engineer.entity.Champions;
import riot.api.data.engineer.entity.Version;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.service.ApiInfoService;
import riot.api.data.engineer.service.CollectDragonService;
import riot.api.data.engineer.service.VersionService;
import riot.api.data.engineer.utils.UtilManager;

@Slf4j
@RestController
@RequestMapping(value = "/ddragon/champions")
public class ChampionsController {

    private final CollectDragonService collectDragonService;
    private WebClient webClient;
    private final ApiInfoService apiInfoService;

    private final VersionService versionService;

    public ChampionsController(CollectDragonService collectDragonService, WebClient webClient, ApiInfoService apiInfoService, VersionService versionService){
        this.collectDragonService = collectDragonService;
        this.webClient = webClient;
        this.apiInfoService = apiInfoService;
        this.versionService = versionService;
    }

    @GetMapping("/get")
    public Champions getChampions() {

        String apiName = "/ddragon/champions/get";

        ApiInfo apiInfo = apiInfoService.findOneByName(apiName);

        String uri = new UtilManager().getStringConcat(apiInfo.getApiHost(),apiInfo.getApiUrl());

        Version version = versionService.findOneByName();

        if(!StringUtils.isEmpty(uri) && !StringUtils.isEmpty(version.getVersion())){
            String response = webClient.get().uri(uri,version.getVersion()).retrieve().bodyToMono(String.class).block();
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
            Champions champions = collectDragonService.setChampions(jsonObject);

            return champions;
        }else{
            return null;
        }
    }

}
