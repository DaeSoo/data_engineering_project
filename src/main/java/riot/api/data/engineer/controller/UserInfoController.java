package riot.api.data.engineer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import riot.api.data.engineer.apiresult.ApiResult;
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
    public ResponseEntity<ApiResult> getUserEntries() throws InterruptedException {
        String apiName = "/userinfo/entries";
        List<ApiInfo> apiInfoList = apiInfoService.findByName(apiName);
        List<ApiKey> apiKeyList = apiKeyService.findList();

        ResponseEntity<ApiResult> response = userInfoService.apiCallBatch(apiInfoList, apiKeyList);

        return response;

    }

    @DeleteMapping("/user/entries")
    public ResponseEntity<ApiResult> userEntriesRemove() {
        try{
            List<UserInfo> userInfoList = userInfoService.getUserInfoListAll();
            if(CollectionUtils.isEmpty(userInfoList)){
                return new ResponseEntity(new ApiResult(404,"List is Empty",null), HttpStatus.BAD_REQUEST);
            }
            ApiResult apiResult = userInfoService.removeAll(userInfoList);
            return new ResponseEntity<>(apiResult,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(new ApiResult(500,e.getMessage(),null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

