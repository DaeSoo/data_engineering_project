package riot.api.data.engineer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @PostMapping("save")
    public int getMatchList(@Valid @RequestBody MatchInfoDto matchInfoDto) {
        return matchInfoService.createThread(new Exception().getStackTrace()[0].getMethodName(), matchInfoDto.getStartTime(), matchInfoDto.getEndTime());
    }

    @GetMapping("detail")
    public int getMatchDetail(){
        String apiName = "/match/detail";

        try{
            ApiInfo apiInfo = apiInfoService.findOneByName(apiName);
            List<ApiKey> apiKeyList = apiKeyService.findList();
            int response = matchInfoService.apiCallBatch(apiInfo, apiKeyList);
            return response;
        }
        catch (Exception e){
            log.info(e.getMessage());
            return -1;
        }
    }
}
