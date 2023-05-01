package riot.api.data.engineer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.Champions;
import riot.api.data.engineer.entity.Version;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.service.ApiInfoService;
import riot.api.data.engineer.service.ChampionsService;
import riot.api.data.engineer.service.VersionService;
import riot.api.data.engineer.service.WebclientCallService;

@Slf4j
@RestController
@RequestMapping(value = "/ddragon/champions")
public class ChampionsController {

    private final ChampionsService championsService;
    private WebClient webClient;

    private final WebclientCallService webclientCallService;
    private final ApiInfoService apiInfoService;

    private final VersionService versionService;

    public ChampionsController(ChampionsService championsService, WebClient webClient, WebclientCallService webclientCallService, ApiInfoService apiInfoService, VersionService versionService){
        this.championsService = championsService;
        this.webClient = webClient;
        this.webclientCallService = webclientCallService;
        this.apiInfoService = apiInfoService;
        this.versionService = versionService;
    }

    @GetMapping("/get")
    public Champions getChampions() {

        String apiName = "/ddragon/champions/get";

        ApiInfo apiInfo = apiInfoService.findOneByName(apiName);

        Version version = versionService.findOneByName();

        String response = webclientCallService.webclientGetWithVersion(new WebClientDTO(apiInfo.getApiScheme(),apiInfo.getApiHost(),apiInfo.getApiUrl()),version.getVersion());

        Champions champions = championsService.setChampions(response);

       if(!ObjectUtils.isEmpty(champions)){
           return champions;
        }
       else{
            return null;
        }
    }

}
