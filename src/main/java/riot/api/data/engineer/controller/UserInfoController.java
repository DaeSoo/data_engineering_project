package riot.api.data.engineer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
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
@RequiredArgsConstructor
public class UserInfoController {
    private final ApiInfoService apiInfoService;
    private final ApiKeyService apiKeyService;
    private final UserInfoService userInfoService;

    @GetMapping("/user/entries")
    public int getUserEntries() throws InterruptedException {
        String apiName = "/userinfo/entries";
        List<ApiInfo> apiInfoList = apiInfoService.findByName(apiName);
        List<ApiKey> apiKeyList = apiKeyService.findList();

        int response = userInfoService.apiCallBatch(apiInfoList, apiKeyList);

        return response;

    }

    @DeleteMapping("/user/entries/remove")
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

