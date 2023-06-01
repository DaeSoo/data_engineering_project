package riot.api.data.engineer.serviceimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import riot.api.data.engineer.apiresult.ApiResult;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.UserInfo;
import riot.api.data.engineer.entity.UserInfoDetail;
import riot.api.data.engineer.entity.WebClientCaller;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.api.ApiKey;
import riot.api.data.engineer.repository.UserInfoDetailQueryRepository;
import riot.api.data.engineer.repository.UserInfoDetailRepository;
import riot.api.data.engineer.service.ApiInfoService;
import riot.api.data.engineer.service.ApiKeyService;
import riot.api.data.engineer.service.UserInfoDetailService;
import riot.api.data.engineer.service.UserInfoService;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserInfoDetailServiceImpl implements UserInfoDetailService {
    private final UserInfoDetailRepository userInfoDetailRepository ;
    private final UserInfoService userInfoService ;
    private final ApiKeyService apiKeyService;
    private final ApiInfoService apiInfoService;
    private final UserInfoDetailQueryRepository userInfoDetailQueryRepository;
    private final WebClient webClient;

    @Override
    public ResponseEntity<ApiResult> createUserInfoDetailTasks(String method){
        log.info("===== createUserInfoDetailTasks Start =====");
        List<ApiKey> apiKeyList = apiKeyService.findList();
        List<Callable<Integer>> tasks = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(apiKeyList.size());

        try{
            for(ApiKey apiKey : apiKeyList){
                Callable<Integer> task = () -> {
                    userInfoDetailApiCall(apiKey, method);
                    return 1;
                };
                tasks.add(task);
            }
            executorService.invokeAll(tasks);
            executorService.shutdown();
        }
        catch (Exception e){
            executorService.shutdownNow();
            log.error(e.getMessage());
            log.info("===== createUserInfoDetailTasks End =====");
            return new ResponseEntity(new ApiResult(500, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("===== createUserInfoDetailTasks End =====");
        return new ResponseEntity(new ApiResult(200, "success", null), HttpStatus.OK);
    }

    /**
     * @param apiKey ApiKey Entity
     * @param apiName ApiInfo apiName(method명)
     */
    @Override
    public void userInfoDetailApiCall(ApiKey apiKey, String apiName) {
        try{
            List<UserInfo> userInfoList = userInfoService.findUserInfoListUpdateYnIsN(apiKey.getApiKeyId(), "N");
            ApiInfo apiInfo = apiInfoService.findOneByName(apiName);

            for (UserInfo userInfo : userInfoList) {
                List<String> pathVariableList = new ArrayList<>();
                pathVariableList.add(userInfo.getSummonerId());
                /*** Api 호출 세팅**/
                WebClientCaller webClientCaller = buildWebClientCaller(pathVariableList,apiInfo);
                /*** API 호출 **/
                String response = webClientCaller.getWebClientToString(apiKey);
                /*** UserInfoDetail Save ***/
                userInfoDetailSave(jsonToEntity(response, apiKey.getApiKeyId()));
                /*** userInfo Update ***/
                userInfo.setUpdateYn("Y");
                userInfoService.save(userInfo);

                Thread.sleep(1200);

            }
        }catch (InterruptedException ie){
            log.error(" === ERROR === ");
            log.error(ie.getMessage());
            Thread.currentThread().interrupt();
        }
        catch (Exception e){
            log.error(" === ERROR === ");
            log.error(e.getMessage());
        }
    }

    @Override
    public List<UserInfoDetail> findUserInfoDetailList() {
        return userInfoDetailRepository.findAll();
    }

    @Override
    public List<UserInfoDetail> findUserInfoDetailListByApiKey(Long apiKey) {
        return userInfoDetailQueryRepository.findListByApiKeyId(apiKey);
    }

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

    private WebClientCaller buildWebClientCaller(List<String> pathVariableList, ApiInfo apiInfo){

        WebClientDTO webClientDTO = WebClientDTO.builder()
                .scheme(apiInfo.getApiScheme())
                .host(apiInfo.getApiHost())
                .path(apiInfo.getApiUrl())
                .pathVariable(pathVariableList)
                .build();

        return WebClientCaller.builder()
                .webClientDTO(webClientDTO)
                .webclient(webClient)
                .build();
    }

    @Override
    @Transactional
    public ApiResult deleteAll() {
        try{
            userInfoDetailRepository.deleteAll();
            return new ApiResult(200,"success",null);
        }catch (Exception e){
            return new ApiResult(500,e.getMessage(),null);
        }

    }
}
