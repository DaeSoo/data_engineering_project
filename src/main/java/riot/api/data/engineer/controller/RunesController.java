package riot.api.data.engineer.controller;

import com.google.gson.JsonArray;
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
@RequestMapping(value = "/ddragon/runes")
public class RunesController {

    private final ApiInfoService apiInfoService;

    private final VersionService versionService;
    private final WebclientCallService webclientCallService;

    public RunesController(ApiInfoService apiInfoService, VersionService versionService, WebclientCallService webclientCallService) {
        this.apiInfoService = apiInfoService;
        this.versionService = versionService;
        this.webclientCallService = webclientCallService;
    }

    @GetMapping("/get")
    public JsonArray getRunes() {

        String apiName = "/ddragon/runes/get";

        ApiInfo apiInfo = apiInfoService.findOneByName(apiName);

        Version version = versionService.findOneByName();

        String response = webclientCallService.webclientGetWithVersion(new WebClientDTO(apiInfo.getApiScheme(), apiInfo.getApiHost(), apiInfo.getApiUrl()), version.getVersion());

        JsonArray jsonArray = new UtilManager().StringToJsonArray(response);

        return new UtilManager().StringToJsonArray(response);

    }

}
