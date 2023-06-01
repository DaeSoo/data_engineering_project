package riot.api.data.engineer.serviceimpl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;
import riot.api.data.engineer.apiresult.ApiResult;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.UserInfo;
import riot.api.data.engineer.entity.WebClientCaller;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.api.ApiKey;
import riot.api.data.engineer.repository.UserInfoQueryRepository;
import riot.api.data.engineer.repository.UserInfoRepository;
import riot.api.data.engineer.service.UserInfoService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {
    private final WebClient webClient;
    private final UserInfoRepository userInfoRepository;
    private final UserInfoQueryRepository userInfoQueryRepository;

    @Override
    public ResponseEntity<ApiResult> createUserEntriesTasks(List<ApiInfo> apiInfoList, List<ApiKey> apiKeyList) {
        log.info("===== createUserEntriesTasks Start =====");
        int batchSize = apiKeyList.size();
        List<Callable<Integer>> tasks = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(batchSize);

        for(ApiInfo apiInfo : apiInfoList) {
            int page = 1;
            for (ApiKey apiKey : apiKeyList) {
                int finalPage = page;
                Callable<Integer> task = () -> {
                    userEntriesApiCall(apiInfo, apiKey, finalPage,batchSize);
                    return 200;
                };
                page++;
                tasks.add(task);
            }
        }
        try{
            executorService.invokeAll(tasks);
            executorService.shutdown();
        }
        catch (Exception e){
            executorService.shutdownNow();
            log.info("===== createUserEntriesTasks End =====");
            return new ResponseEntity<>(new ApiResult(500,e.getMessage(),null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("===== createUserEntriesTasks End =====");
        return new ResponseEntity<>(new ApiResult(200,"success",null),HttpStatus.OK);
    }

    public void userEntriesApiCall(ApiInfo apiInfo, ApiKey apiKey, int page, int batchSize){
        Gson gson = new Gson();
        int pageSum = page;
        try{
            while(true){
                /*** API 호출 세팅 ***/
                Map<String,String> paging = new HashMap<>();
                paging.put("page",String.valueOf(pageSum));
                WebClientCaller webClientCaller = buildWebClientCaller(paging,apiInfo);
                /*** API 호출 ***/
                String response = webClientCaller.getWebClientToString(apiKey);
                /*** String to userInfoList ***/
                List<UserInfo> userInfoList = gson.fromJson(response, new TypeToken<List<UserInfo>>(){}.getType());

                if(CollectionUtils.isEmpty(userInfoList)){
                    break;
                }
                else{
                    for (UserInfo userInfo : userInfoList) {
                        userInfo.setUpdateYn("N");
                        userInfo.setApiKeyId(apiKey.getApiKeyId());
                        userInfoRepository.save(userInfo);
                    }
                    pageSum += batchSize;
                }
                Thread.sleep(1200);
            }
        }
        catch (Exception e){
            log.error("=== ERROR ===");
            log.error(e.getMessage());
        }
    }

    @Override
    public List<UserInfo> findUserInfoListUpdateYnIsN(Long apiKey, String updateYn) {
        return userInfoQueryRepository.findListByApiKeyId(apiKey, updateYn);
    }

    @Override
    public List<UserInfo> findUserInfoListAll() {
        return userInfoRepository.findAll();
    }

    @Override
    @Transactional
    public ApiResult deleteAll(List<UserInfo> userInfoList) {
        try{
            userInfoRepository.deleteAll(userInfoList);
            return new ApiResult(200,"success",userInfoList.size());
        }catch (Exception e){
            return new ApiResult(500,e.getMessage(),userInfoList.size());
        }

    }

    private WebClientCaller buildWebClientCaller(Map<String, String> paging,ApiInfo apiInfo){

        WebClientDTO webClientDTO = WebClientDTO.builder()
                .scheme(apiInfo.getApiScheme())
                .host(apiInfo.getApiHost())
                .path(apiInfo.getApiUrl())
                .paging(paging)
                .build();

        return WebClientCaller.builder()
                .webClientDTO(webClientDTO)
                .webclient(webClient)
                .build();
    }
    @Override
    public UserInfo save(UserInfo userInfo) {
        return userInfoRepository.save(userInfo);
    }
}

