package riot.api.data.engineer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import riot.api.data.engineer.apiresult.ApiResult;
import riot.api.data.engineer.dto.MatchInfoDto;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.api.ApiKey;
import riot.api.data.engineer.service.ApiInfoService;
import riot.api.data.engineer.service.ApiKeyService;
import riot.api.data.engineer.service.MatchInfoService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("match")
@RequiredArgsConstructor
@Slf4j
public class MatchInfoController {

    private final MatchInfoService matchInfoService;
    private final ApiInfoService apiInfoService;
    private final ApiKeyService apiKeyService;

    @PostMapping("list")
    public ResponseEntity<ApiResult> getMatchList(@Valid @RequestBody MatchInfoDto matchInfoDto) {
        return matchInfoService.createMatchInfoTasks(new Exception().getStackTrace()[0].getMethodName(), matchInfoDto.getStartTime(), matchInfoDto.getEndTime());
    }

    @GetMapping("detail")
    public ResponseEntity<ApiResult> getMatchDetail(){

        ApiInfo apiInfo = apiInfoService.findOneByName(new Exception().getStackTrace()[0].getMethodName());
        List<ApiKey> apiKeyList = apiKeyService.findList();

        return matchInfoService.createMatchInfoDetailTasks(apiInfo, apiKeyList);

    }

    @DeleteMapping("list")
    public ResponseEntity<ApiResult> matchInfosDeleteByCollectCompleteYn(@RequestParam(required = false,name = "collectCompleteYn") String collectCompleteYn) {
        try{
            ApiResult apiResult = matchInfoService.deleteAllByCollectCompleteYn(collectCompleteYn);
            return new ResponseEntity<>(apiResult, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ApiResult(500,e.getMessage(),null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
