package riot.api.data.engineer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.api.ApiKey;
import riot.api.data.engineer.service.ApiInfoService;
import riot.api.data.engineer.service.ApiKeyService;
import riot.api.data.engineer.service.UserInfoService;

import java.util.List;


@Slf4j
@RestController
@RequestMapping(value = "/userinfo")
public class UserInfoController {
    private final ApiInfoService apiInfoService;
    private final ApiKeyService apiKeyService;
    private final UserInfoService userInfoService;

    public UserInfoController(ApiInfoService apiInfoService,
                              ApiKeyService apiKeyService,
                              UserInfoService userInfoService) {
        this.apiInfoService = apiInfoService;
        this.apiKeyService = apiKeyService;
        this.userInfoService = userInfoService;
    }


    @GetMapping("/user/entries")
    public String getUserEntries() {
        String apiName = "/userinfo/entries";
        List<ApiInfo> apiInfoList = apiInfoService.findByName(apiName);
        List<ApiKey> apiKeyList = apiKeyService.findList();

        userInfoService.apiCallBatch(apiInfoList, apiKeyList);

        return "success";

    }


}

