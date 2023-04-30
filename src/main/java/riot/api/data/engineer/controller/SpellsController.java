package riot.api.data.engineer.controller;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.Version;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.service.ApiInfoService;
import riot.api.data.engineer.service.VersionService;
import riot.api.data.engineer.service.WebclientCallService;
import riot.api.data.engineer.utils.UtilManager;

@Slf4j
@RestController
@RequestMapping(value = "/ddragon/spells")
public class SpellsController {

    private final ApiInfoService apiInfoService;
    private final VersionService versionService;
    private final WebclientCallService webclientCallService;

    public SpellsController(ApiInfoService apiInfoService, VersionService versionService, WebclientCallService webclientCallService) {
        this.apiInfoService = apiInfoService;
        this.versionService = versionService;
        this.webclientCallService = webclientCallService;
    }

    @GetMapping("/get")
    public JsonObject getSpells() {

        String apiName = "/ddragon/spells/get";

        ApiInfo apiInfo = apiInfoService.findOneByName(apiName);
        Version version = versionService.findOneByName();

        String response = webclientCallService.webclientGetWithVersion(new WebClientDTO(apiInfo.getApiScheme(), apiInfo.getApiHost(), apiInfo.getApiUrl()), version.getVersion());
        return new UtilManager().StringToJsonObject(response);
    }

}
