package riot.api.data.engineer.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.UserInfo;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.api.ApiKey;
import riot.api.data.engineer.service.ApiInfoService;
import riot.api.data.engineer.service.ApiKeyService;
import riot.api.data.engineer.service.UserInfoService;
import riot.api.data.engineer.service.WebclientCallService;
import riot.api.data.engineer.webclient.WebClientConfig;

import java.lang.reflect.Method;
import java.util.*;


@Slf4j
@RestController
@RequestMapping(value = "/userinfo")
public class UserInfoController {
    private final ApiInfoService apiInfoService;
    private final ApiKeyService apiKeyService;
    private final UserInfoService userInfoService;
    private final WebclientCallService webclientCallService;

    public UserInfoController(ApiInfoService apiInfoService, WebClient webClient,
                              ApiKeyService apiKeyService,
                              UserInfoService userInfoService,
                              WebclientCallService webclientCallService)
    {
        this.apiInfoService = apiInfoService;
        this.apiKeyService = apiKeyService;
        this.userInfoService = userInfoService;
        this.webclientCallService = webclientCallService;
    }

//    @GetMapping("/test")


    @GetMapping("/user/entries")
    public List<UserInfo> getUserEntries(){
        String method = new Exception().getStackTrace()[0].getMethodName();


        String apiName = "/userinfo/entries";

        List<ApiInfo> apiInfoList = apiInfoService.findByName(apiName);
        List<ApiKey> apiKeyList = apiKeyService.findList();

        userInfoService.apiCallBatch(apiInfoList,apiKeyList);


        return null;


    }



}

