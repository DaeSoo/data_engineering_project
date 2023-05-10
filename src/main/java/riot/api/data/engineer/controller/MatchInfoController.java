package riot.api.data.engineer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.api.ApiKey;
import riot.api.data.engineer.service.ApiInfoService;
import riot.api.data.engineer.service.ApiKeyService;
import riot.api.data.engineer.service.MatchInfoService;

import java.util.List;

@RestController
@RequestMapping("match")
@RequiredArgsConstructor
@Slf4j
public class MatchInfoController {

    private final MatchInfoService matchInfoService;
    private final ApiInfoService apiInfoService;
    private final ApiKeyService apiKeyService;

    @GetMapping("save")
    public String getMatchList(){
        matchInfoService.createThread(new Exception().getStackTrace()[0].getMethodName());

        return "success";
    }

    @GetMapping("detail")
    public String getMatchDetail(){
        String apiName = "/match/detail";

        try{
            ApiInfo apiInfo = apiInfoService.findOneByName(apiName);
            List<ApiKey> apiKeyList = apiKeyService.findList();
            matchInfoService.apiCallBatch(apiInfo, apiKeyList);
        }
        catch (Exception e){
            log.info(e.getMessage());
        }

        return "success";
    }

    @GetMapping("detailTest")
    public int getMatchDetailTest(){
        String apiName = "/match/detail";

        try{
            ApiInfo apiInfo = apiInfoService.findOneByName(apiName);
            List<ApiKey> apiKeyList = apiKeyService.findList();
            int response = matchInfoService.apiCallBatchTest(apiInfo, apiKeyList);
            return response;
        }
        catch (Exception e){
            log.info(e.getMessage());
            return -1;
        }
    }
}
