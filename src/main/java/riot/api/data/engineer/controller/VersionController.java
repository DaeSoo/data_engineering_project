package riot.api.data.engineer.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.Version;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.service.ApiInfoService;
import riot.api.data.engineer.service.VersionService;
import riot.api.data.engineer.service.WebclientCallService;
import riot.api.data.engineer.utils.UtilManager;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/ddragon/version")
public class VersionController {
    private final ApiInfoService apiInfoService;
    private final WebclientCallService webclientCallService;
    private final VersionService versionService;

    public VersionController(ApiInfoService apiInfoService,
                             WebclientCallService webclientCallService, VersionService versionService) {
        this.apiInfoService = apiInfoService;
        this.webclientCallService = webclientCallService;
        this.versionService = versionService;
    }

    @GetMapping("/get")
    public List<Version> getVersion() {
        /** yml에서 관리하는 것으로 변경 예정**/
        String apiName = "/ddragon/version/get";

        ApiInfo apiInfo = apiInfoService.findOneByName(apiName);

        String response = webclientCallService.webclientGet(new WebClientDTO(apiInfo.getApiScheme(), apiInfo.getApiHost(), apiInfo.getApiUrl()));

        JsonArray jsonArrayResponse = new UtilManager().StringToJsonArray(response);

        List<Version> versionList = new ArrayList<>();

        for (JsonElement jsonElement : jsonArrayResponse) {
            String versionString = jsonElement.getAsString();
            boolean currentVersionYn = versionString.equals(jsonArrayResponse.get(0).getAsString());
            Version version = new Version(versionString, currentVersionYn);
            versionList.add(version);
        }

        try {
            versionService.save(versionList);
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return versionList;
    }

    @GetMapping("/get/current/version")
    public Version getCurrentVersion() {
        return versionService.findOneByName();
    }


}