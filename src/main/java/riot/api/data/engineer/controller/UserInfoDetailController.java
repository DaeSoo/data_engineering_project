package riot.api.data.engineer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.UserInfo;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.api.ApiKey;
import riot.api.data.engineer.repository.UserInfoRepository;
import riot.api.data.engineer.service.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/userinfo/detail")
public class UserInfoDetailController {
    private final ApiInfoService apiInfoService;
    private final ApiKeyService apiKeyService;
    private final WebclientCallService webclientCallService;
    private final UserInfoRepository userInfoRepository;
    private final UserInfoDetailService userInfoDetailService;

    public UserInfoDetailController(ApiInfoService apiInfoService, WebClient webClient,
                                ApiKeyService apiKeyService,
                                UserInfoRepository userInfoRepository,
                                WebclientCallService webclientCallService,
                                    UserInfoDetailService userInfoDetailService)
    {
        this.apiInfoService = apiInfoService;
        this.apiKeyService = apiKeyService;
        this.webclientCallService = webclientCallService;
        this.userInfoDetailService = userInfoDetailService;
        this.userInfoRepository = userInfoRepository;
    }

    @GetMapping("save")
    public List<UserInfo> getUserInfoDetail(){
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        ApiInfo apiInfo = apiInfoService.findOneByName(new Exception().getStackTrace()[0].getMethodName());
        List<ApiKey> apiKeyList = apiKeyService.findList();

        Iterator<UserInfo> iter = userInfoList.iterator();
        while(iter.hasNext()){
            UserInfo userInfo = iter.next();
            Map<String, String> queryParam = new HashMap<>();
            queryParam.put("summonerId" , userInfo.getSummonerId());
            WebClientDTO webClientDTO = new WebClientDTO(apiInfo.getApiScheme(),apiInfo.getApiHost(),apiInfo.getApiUrl(), queryParam);
            String response = webclientCallService.webclientQueryParamGet(webClientDTO, apiKeyList.get(0),queryParam);
            userInfoDetailService.UserInfoDetailSave(response);
            log.info("aaa", response);

        }

        return null;
    }
}
