package riot.api.data.engineer.serviceimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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


    /**
     * @param apiKey ApiKey Entity
     * @param apiName ApiInfo apiName(method명)
     */
    @Override
    public void userInfoDetailApiRequest(ApiKey apiKey, String apiName) {
        try{
            List<UserInfo> userInfoList = userInfoService.getUserInfoList(apiKey.getApiKeyId(), "N");
            ApiInfo apiInfo = apiInfoService.findOneByName(apiName);
            for (UserInfo userInfo : userInfoList) {
                List<String> pathVariable = new ArrayList<>();
                pathVariable.add(userInfo.getSummonerId());
                WebClientDTO webClientDTO = WebClientDTO.builder()
                        .scheme(apiInfo.getApiScheme())
                        .host(apiInfo.getApiHost())
                        .path(apiInfo.getApiUrl())
                        .pathVariable(pathVariable).build();

                WebClientCaller webClientCaller = WebClientCaller.builder()
                        .webclient(webClient)
                        .webClientDTO(webClientDTO)
                        .build();

                String response = webClientCaller.getWebClientToString(apiKey);

                userInfoDetailSave(jsonToEntity(response, apiKey.getApiKeyId()));
                /*
                 * UserInfoDetail Save가 됬을 경우
                 * userInfo의 setUpdateYn = Default 값이 N을 Y로 변경
                 */
                userInfo.setUpdateYn("Y");
                userInfoService.save(userInfo);
            /*
                RiotApi 1분 호출 limit을 맞추기 위한 Thread.sleep
             */try{
                    Thread.sleep(1200);
                }
                catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                    log.info("Exception : {}", e.getMessage());
                }
            }
        }catch (Exception e){
            log.error(" === ERROR === ");
            log.error(e.getMessage());
        }
    }
    @Override
    public ResponseEntity<ApiResult> createThread(String method){
        List<ApiKey> apiKeyList = apiKeyService.findList();
        List<Callable<Integer>> tasks = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(apiKeyList.size());

        try{
            for(ApiKey apiKey : apiKeyList){
                Callable<Integer> task = () -> {
                    userInfoDetailApiRequest(apiKey, method);
                    return 1;
                };
                tasks.add(task);
            }
            executorService.invokeAll(tasks);
            executorService.shutdown();
        }
        catch (Exception e){
            executorService.shutdownNow();
            return new ResponseEntity(new ApiResult(500, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(new ApiResult(200, "success", null), HttpStatus.OK);
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
