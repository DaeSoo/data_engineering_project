package riot.api.data.engineer.serviceimpl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.UserInfo;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.api.ApiKey;
import riot.api.data.engineer.repository.UserInfoQueryRepository;
import riot.api.data.engineer.repository.UserInfoRepository;
import riot.api.data.engineer.service.UserInfoService;
import riot.api.data.engineer.service.WebclientCallService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {
    private final Executor executor;
    private final ExecutorService executorService;
    private final WebclientCallService webclientCallService;
    private final UserInfoRepository userInfoRepository;
    private final UserInfoQueryRepository userInfoQueryRepository;

    @Override
    public void apiCallBatch(List<ApiInfo> apiInfoList, List<ApiKey> apiKeyList) {
        int batchSize = apiKeyList.size();

        for (int i = 0; i < apiInfoList.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, apiInfoList.size());
            List<ApiInfo> batch = apiInfoList.subList(i, endIndex);
            for (int j = 0; j < batch.size(); j++) {
                ApiInfo apiInfo = batch.get(j);
                int page = 1;
                int finalJ = j;
                executor.execute(() -> {
                    apiCallRepeat(apiInfo,apiKeyList.get(finalJ),page);
                });
            }
        }
    }

    @Override
    public int apiCallBatchTest(List<ApiInfo> apiInfoList, List<ApiKey> apiKeyList) {
        int batchSize = apiKeyList.size();
        List<Callable<Integer>> tasks = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(batchSize);

        for (int i = 0; i < apiInfoList.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, apiInfoList.size());
            List<ApiInfo> batch = apiInfoList.subList(i, endIndex);
            try{
                for (int j = 0; j < batch.size(); j++) {
                    int finalJ1 = j;
                    Callable<Integer> task = () -> {
                        ApiInfo apiInfo = batch.get(finalJ1);
                        int page = 1;
                        int finalJ = finalJ1;
                        apiCallRepeat(apiInfo, apiKeyList.get(finalJ), page);
                        return 1;
                    };
                    tasks.add(task);
                }
                executorService.invokeAll(tasks);
                executorService.shutdown();
            }catch (Exception e){
                executorService.shutdownNow();
                return -1;
            }
        }
        return 2;
    }

    @Transactional
    protected void apiCallRepeat(ApiInfo apiInfo, ApiKey apiKey, int page){
        Gson gson = new Gson();

        while(true){
            Map<String,String> queryParam = new HashMap<>();
            queryParam.put("page",String.valueOf(page));
            WebClientDTO webClientDTO = new WebClientDTO(apiInfo.getApiScheme(),apiInfo.getApiHost(), apiInfo.getApiUrl(),queryParam);

            String response = webclientCallService.webclientGetWithTokenWithPageParam(webClientDTO,apiKey);
            List<UserInfo> userInfoList = gson.fromJson(response, new TypeToken<List<UserInfo>>(){}.getType());

            if(CollectionUtils.isEmpty(userInfoList)){
                break;
            }
            else{
                for (UserInfo userInfo : userInfoList) {
                    userInfo.setApiKeyId(apiKey.getApiKeyId());
                }
                userInfoRepository.saveAll(userInfoList);
                page++;
            }
            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<UserInfo> getUserInfoList(Long apiKey) {
        return userInfoQueryRepository.findListByApiKeyId(apiKey);
    }

    @Override
    public List<UserInfo> getUserInfoListAll() {
        return userInfoRepository.findAll();
    }

    @Override
    public void removeAll(List<UserInfo> userInfoList) {
        userInfoRepository.deleteAll(userInfoList);
    }
}

