package riot.api.data.engineer.serviceimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import riot.api.data.engineer.dto.FileDto;
import riot.api.data.engineer.dto.MatchInfoParam;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.*;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.api.ApiKey;
import riot.api.data.engineer.entity.matchdetail.Info;
import riot.api.data.engineer.entity.matchdetail.MatchDetail;
import riot.api.data.engineer.entity.matchdetail.MetaData;
import riot.api.data.engineer.repository.MatchInfoQueryRepository;
import riot.api.data.engineer.repository.MatchInfoRepository;
import riot.api.data.engineer.service.*;
import riot.api.data.engineer.utils.UtilManager;


import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchInfoServiceImpl implements MatchInfoService {
    private final MatchInfoRepository matchInfoRepository;
    private final UserInfoDetailService userInfoDetailService;
    private final ApiInfoService apiInfoService;
    private final WebclientCallService webclientCallService;
    private final ApiKeyService apiKeyService;
    private final MatchInfoQueryRepository matchInfoQueryRepository;
    private final Executor executor;
    private final KafkaInfoService kafkaInfoService;
    private final MyProducer myProducer;
    private final ExecutorService executorService;
    private final MinioService minioService;




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

    @Override
    public List<MatchInfo> getMatchInfoList() {
        return matchInfoRepository.findAll();
    }


    @Override
    public void apiCallBatch(ApiInfo apiInfo, List<ApiKey> apiKeyList) {
        try{
            ExecutorService executorService = Executors.newFixedThreadPool(apiKeyList.size());
            KafkaInfo kafkaInfo = kafkaInfoService.findOneByApiInfoId(apiInfo.getApiInfoId());
            for(ApiKey apiKey : apiKeyList){
                Runnable task = () -> {
                    List<MatchInfo> matchInfos = matchInfoQueryRepository.findListByApiKeyId(apiKey.getApiKeyId());
                    apiCallRepeat(apiInfo, apiKey, matchInfos, kafkaInfo);
                };
                executorService.submit(task);
            }
            executorService.shutdown();
        }catch (Exception e){
            executorService.shutdownNow();
            log.info(e.getMessage());
        }
    }

    @Override
    public int apiCallBatchTest(ApiInfo apiInfo, List<ApiKey> apiKeyList) {
        try{
            List<Callable<Integer>> tasks = new ArrayList<>();

            ExecutorService executorService = Executors.newFixedThreadPool(apiKeyList.size());
            KafkaInfo kafkaInfo = kafkaInfoService.findOneByApiInfoId(apiInfo.getApiInfoId());
            for(ApiKey apiKey : apiKeyList){
                Callable<Integer> task = () -> {
                    List<MatchInfo> matchInfos = matchInfoQueryRepository.findListByApiKeyId(apiKey.getApiKeyId());
                    apiCallRepeatTest(apiInfo, apiKey, matchInfos, kafkaInfo);
                    return 1;
                };
                tasks.add(task);
            }
            executorService.invokeAll(tasks);
            executorService.shutdown();
            return 2;
        }catch (Exception e){
            executorService.shutdownNow();
            log.info(e.getMessage());
            return -1;
        }

    }


    protected void apiCallRepeat(ApiInfo apiInfo, ApiKey apiKey,List<MatchInfo> matchInfos,KafkaInfo kafkaInfo){

        for(MatchInfo matchInfo : matchInfos){
            WebClientDTO webClientDTO = new WebClientDTO(apiInfo.getApiScheme(),apiInfo.getApiHost(), apiInfo.getApiUrl());
            String response = webclientCallService.webclientGetWithMatchIdWithToken(webClientDTO,apiKey,matchInfo.getId());
            if(StringUtils.isEmpty(response)){
                continue;
            }
            else{
                /**** 원천 데이터 전송 ****/
                minioService.save(minioService.uploadInputStream(matchInfo.getId(), response));
                /**** 카프카 전송 ****/
                myProducer.sendMessage(kafkaInfo,response);
            }
            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected void apiCallRepeatTest(ApiInfo apiInfo, ApiKey apiKey,List<MatchInfo> matchInfos,KafkaInfo kafkaInfo){
        Gson gson = new Gson();
        for(MatchInfo matchInfo : matchInfos){
            WebClientDTO webClientDTO = new WebClientDTO(apiInfo.getApiScheme(),apiInfo.getApiHost(), apiInfo.getApiUrl());
            String response = webclientCallService.webclientGetWithMatchIdWithToken(webClientDTO,apiKey,matchInfo.getId());
            if(StringUtils.isEmpty(response)){
                continue;
            }
            else{
                /**** 카프카 전송 ****/
                MatchDetail matchDetail = setMatchInfoDetail(response);
                String processedResponse = gson.toJson(matchDetail);

                myProducer.sendMessage(kafkaInfo,processedResponse);
            }
            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private MatchDetail setMatchInfoDetail(String response) {
        Gson gson = new Gson();
        JsonObject jsonObject = new UtilManager().StringToJsonObject(response);
        MatchDetail matchDetail = new MatchDetail();

        Info infoData = gson.fromJson(jsonObject.getAsJsonObject("info"),Info.class);
        matchDetail.setInfo(infoData);

        MetaData metaData = gson.fromJson(jsonObject.getAsJsonObject("metadata"),MetaData.class);
        matchDetail.setMetadata(metaData);

        return matchDetail;
    }

    private Info setInfo(String response) {
        Gson gson = new Gson();
        JsonObject jsonObject = new UtilManager().StringToJsonObject(response);

        Info infoData = gson.fromJson(jsonObject.getAsJsonObject("info"),Info.class);

        return infoData;
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
