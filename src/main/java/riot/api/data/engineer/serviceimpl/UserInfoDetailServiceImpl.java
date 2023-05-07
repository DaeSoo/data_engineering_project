package riot.api.data.engineer.serviceimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.UserInfo;
import riot.api.data.engineer.entity.UserInfoDetail;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.api.ApiKey;
import riot.api.data.engineer.repository.UserInfoDetailQueryRepository;
import riot.api.data.engineer.repository.UserInfoDetailRepository;
import riot.api.data.engineer.service.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserInfoDetailServiceImpl implements UserInfoDetailService {
    private final UserInfoDetailRepository userInfoDetailRepository ;
    private final UserInfoService userInfoService ;
    private final WebclientCallService webclientCallService;
    private final ApiKeyService apiKeyService;
    private final ApiInfoService apiInfoService;
    private final UserInfoDetailQueryRepository userInfoDetailQueryRepository;

    private String pacakgeName = this.getClass().getName();


    @Override
    public UserInfoDetail userInfoDetailSave(UserInfoDetail userInfoDetail) {
            return userInfoDetailRepository.save(userInfoDetail);
    }

    @Override
    public UserInfoDetail jsonToEntity(String response, Long apiKeyId){
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            UserInfoDetail userInfoDetail = objectMapper.readValue(response, UserInfoDetail.class);
            userInfoDetail.setApiKeyId(apiKeyId);
            return userInfoDetail;
        } catch (JsonMappingException e) {
            log.info("jsonMapping Exception : {}", e.getMessage());
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            log.info("jsonProcessing Exception : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createTestMethod(String packageName, String methodName) {

    }


    /**
     * @param apiKey ApiKey Entity
     * @param apiName ApiInfo apiName(method명)
     */
    @Override
    public Boolean userInfoDetailApiRequest(ApiKey apiKey, String apiName) {
        boolean let = false;
        try{
            List<UserInfo> userInfoList = userInfoService.getUserInfoList(apiKey.getApiKeyId());
            ApiInfo apiInfo = apiInfoService.findOneByName(apiName);
            for (UserInfo userInfo : userInfoList) {
                Map<String, String> queryParam = new HashMap<>();
                queryParam.put("summonerId", userInfo.getSummonerId());
                WebClientDTO webClientDTO = new WebClientDTO(apiInfo.getApiScheme(), apiInfo.getApiHost(), apiInfo.getApiUrl(), queryParam);
                String response = webclientCallService.webclientQueryParamGet(webClientDTO, apiKey, queryParam);
                userInfoDetailSave(jsonToEntity(response, apiKey.getApiKeyId()));
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
        executorService.submit(() -> userInfoDetailApiRequest(apiKey, apiName));
    }


    @Override
    public List<UserInfoDetail> userInfoDeatilList() {
        return userInfoDetailRepository.findAll();
    }

    @Override
    public List<UserInfoDetail> userInfoDeatilList(Long apiKey) {
        return userInfoDetailQueryRepository.findListByApiKeyId(apiKey);
    }
}
