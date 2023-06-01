package riot.api.data.engineer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import riot.api.data.engineer.apiresult.ApiResult;
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
        List<ApiInfo> apiInfoList = apiInfoService.findByName(new Exception().getStackTrace()[0].getMethodName());
        List<ApiKey> apiKeyList = apiKeyService.findList();

        return userInfoService.createUserEntriesTasks(apiInfoList, apiKeyList);

    }

    @DeleteMapping("/user/entries")
    public ResponseEntity<ApiResult> userEntriesDeleteByUpdateYn(@RequestParam(required = false,name = "updateYn") String updateYn) {
        try{
            ApiResult apiResult = userInfoService.deleteAllByUpdateYn(updateYn);
            return new ResponseEntity<>(apiResult,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ApiResult(500,e.getMessage(),null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

