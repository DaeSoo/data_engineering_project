package riot.api.data.engineer.serviceimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import riot.api.data.engineer.dto.MatchInfoParam;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.MatchInfo;
import riot.api.data.engineer.entity.UserInfoDetail;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.api.ApiKey;
import riot.api.data.engineer.repository.MatchInfoRepository;
import riot.api.data.engineer.service.*;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchInfoServiceImpl implements MatchInfoService {
    private final MatchInfoRepository matchInfoRepository;
    private final UserInfoDetailService userInfoDetailService;
    private final ApiInfoService apiInfoService;
    private final WebclientCallService webclientCallService;
    private final ApiKeyService apiKeyService;

    @Override
    public MatchInfo matchInfoSave(MatchInfo matchInfo){
        return matchInfoRepository.save(matchInfo);
    }

    /**
     *
     * @param apiKey ApiKey Entity
     * @param apiName ApiInfo apiName(method명)
     */
    @Override
    public Boolean matchApiRequest(ApiKey apiKey, String apiName) {
        boolean let = false;
        MatchInfoParam matchInfoParam = setDateParam();
        try{
            List<UserInfoDetail> userInfoDetailList = userInfoDetailService.userInfoDeatilList(apiKey.getApiKeyId());
            ApiInfo apiInfo = apiInfoService.findOneByName(apiName);
            for (UserInfoDetail userInfoDetail : userInfoDetailList) {
                Map<String, String> queryParam = new HashMap<>();
                queryParam.put(apiName, userInfoDetail.getPuuid());
                WebClientDTO webClientDTO = new WebClientDTO(apiInfo.getApiScheme(), apiInfo.getApiHost(), apiInfo.getApiUrl(), queryParam);
                List<String> response = webclientCallService.webclientQueryParamGet(webClientDTO, apiKey, queryParam, apiName, matchInfoParam);
                listToEntity(response, apiKey.getApiKeyId());
            /*
                RiotApi 1분 호출 limit을 맞추기 위한 Thread.sleep
             */
                Thread.sleep(1500);
            }
            let = true;
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            log.info("Exception : {}", e.getMessage());
        }
        return let;
    }
    @Override
    public void createThread(String method){
        List<ApiKey> apiKeyList = apiKeyService.findList();
        ExecutorService executorService = Executors.newFixedThreadPool(apiKeyList.size());
        for(ApiKey apiKey : apiKeyList){
            createSubmit(executorService, apiKey, method);
        }
        executorService.shutdown();
    }

    public void createSubmit(ExecutorService executorService, ApiKey apiKey, String apiName){
        executorService.submit(() -> matchApiRequest(apiKey, apiName));
    }
    public MatchInfo jsonToEntity(String response, Long apiKeyId){
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            MatchInfo matchInfo = objectMapper.readValue(response, MatchInfo.class);
            matchInfo.setApiKeyId(apiKeyId);
            return matchInfo;
        } catch (JsonMappingException e) {
            log.info("jsonMapping Exception : {}", e.getMessage());
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            log.info("jsonProcessing Exception : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void listToEntity(List<String> response, Long apiKeyId){
        for(String puuid : response){
            MatchInfo matchInfo = new MatchInfo(puuid, apiKeyId);
            matchInfoSave(matchInfo);
        }
    }

    public MatchInfoParam setDateParam(){
        return new MatchInfoParam(LocalDateTime.now().minusDays(1).with(LocalTime.of(0, 0, 0)).toEpochSecond(ZoneOffset.UTC),
                LocalDateTime.now().minusDays(1).with(LocalTime.of(23, 59, 59)).toEpochSecond(ZoneOffset.UTC),
                "ranked",
                0,
                100
        );
    }
}
