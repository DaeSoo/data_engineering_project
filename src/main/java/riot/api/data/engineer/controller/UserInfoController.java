package riot.api.data.engineer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import riot.api.data.engineer.entity.UserInfo;
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
    public int getUserEntries() {
        String apiName = "/userinfo/entries";
        List<ApiInfo> apiInfoList = apiInfoService.findByName(apiName);
        List<ApiKey> apiKeyList = apiKeyService.findList();

        int response = userInfoService.apiCallBatchTest(apiInfoList, apiKeyList);

        return response;

    }

    @PutMapping("/user/entries/remove")
    public String userEntriesRemove() {
        try{
            List<UserInfo> userInfoList = userInfoService.getUserInfoListAll();
            if(CollectionUtils.isEmpty(userInfoList)){
                return "fail";
            }
            userInfoService.removeAll(userInfoList);
            return "success";
        }catch (Exception e){
            return "fail";
        }
    }
}

