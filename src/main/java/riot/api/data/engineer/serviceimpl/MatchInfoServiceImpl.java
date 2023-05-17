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
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.KafkaInfo;
import riot.api.data.engineer.entity.MatchInfo;
import riot.api.data.engineer.entity.MyProducer;
import riot.api.data.engineer.entity.UserInfoDetail;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.api.ApiKey;
import riot.api.data.engineer.entity.api.ApiParams;
import riot.api.data.engineer.entity.matchdetail.Info;
import riot.api.data.engineer.entity.matchdetail.MatchDetail;
import riot.api.data.engineer.entity.matchdetail.MetaData;
import riot.api.data.engineer.repository.MatchInfoRepository;
import riot.api.data.engineer.service.ApiInfoService;
import riot.api.data.engineer.service.ApiKeyService;
import riot.api.data.engineer.service.ApiParamsService;
import riot.api.data.engineer.service.KafkaInfoService;
import riot.api.data.engineer.service.MatchInfoService;
import riot.api.data.engineer.service.UserInfoDetailService;
import riot.api.data.engineer.service.WebclientCallService;
import riot.api.data.engineer.utils.UtilManager;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
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
    private final KafkaInfoService kafkaInfoService;
    private final MyProducer myProducer;
    private final ExecutorService executorService;
    private final ApiParamsService apiParamsService;




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
    public void matchApiRequest(ApiKey apiKey, String apiName, String startDate, String endDate) {
        try{
            List<UserInfoDetail> userInfoDetailList = userInfoDetailService.userInfoDeatilList(apiKey.getApiKeyId());
            ApiInfo apiInfo = apiInfoService.findOneByName(apiName);
            List<ApiParams> apiParamsList = apiParamsService.getApiParamsList(apiInfo.getApiInfoId());
            Map<String, String> queryParams = setParams(apiParamsList, startDate, endDate);
            for (UserInfoDetail userInfoDetail : userInfoDetailList) {
                Map<String, String> pathValiable = new HashMap<>();
                pathValiable.put(apiName, userInfoDetail.getPuuid());
                WebClientDTO webClientDTO = new WebClientDTO(apiInfo.getApiScheme(), apiInfo.getApiHost(), apiInfo.getApiUrl(), pathValiable);
                List<String> response = webclientCallService.webclientQueryParamGet(webClientDTO, apiKey, apiName, queryParams);
                listToEntity(response, apiKey.getApiKeyId());
            /*
                RiotApi 1분 호출 limit을 맞추기 위한 Thread.sleep
             */
                Thread.sleep(1200);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.info("Exception : {}", e.getMessage());
        }
    }
    @Override
    public int createThread(String method, String startDate, String endDate){
        List<Callable<Integer>> tasks = new ArrayList<>();
        List<ApiKey> apiKeyList = apiKeyService.findList();
        ExecutorService executorService = Executors.newFixedThreadPool(apiKeyList.size());
        try{
            for(ApiKey apiKey : apiKeyList){
                Callable<Integer> task = () -> {
                    matchApiRequest(apiKey, method, startDate, endDate);
                    return 1;
                };
                tasks.add(task);
            }
            executorService.invokeAll(tasks);
            executorService.shutdown();
            return 2;
        } catch (Exception e) {
            log.info(e.getMessage());
            executorService.shutdown();
            return -1;
        }
    }

    @Override
    public List<MatchInfo> getMatchInfoList() {
        return matchInfoRepository.findAll();
    }


    @Override
    public int apiCallBatch(ApiInfo apiInfo, List<ApiKey> apiKeyList) {
        int apiKeyCount = apiKeyList.size();
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(apiKeyCount);
            List<MatchInfo> matchInfoList = matchInfoRepository.findAll();

            List<List<MatchInfo>> partionMatchInfoList = partitionList(matchInfoList, apiKeyCount);
            List<Callable<Integer>> tasks = new ArrayList<>();
            KafkaInfo kafkaInfo = kafkaInfoService.findOneByApiInfoId(apiInfo.getApiInfoId());


            for (int i = 0; i < apiKeyCount; i++) {
                List<MatchInfo> matchInfoPartition = partionMatchInfoList.get(i);
                ApiKey apiKey = apiKeyList.get(i);
                Callable<Integer> task = () -> {
                    apiCallRepeat(apiInfo, apiKey, matchInfoPartition, kafkaInfo);
                    return 1;
                };
                tasks.add(task);
            }
            executorService.invokeAll(tasks);
            executorService.shutdown();
            return 2;
        } catch (Exception e) {
            executorService.shutdownNow();
            log.info(e.getMessage());
            return -1;
        }
    }

    protected void apiCallRepeat(ApiInfo apiInfo, ApiKey apiKey, List<MatchInfo> matchInfos, KafkaInfo kafkaInfo) {
        Gson gson = new Gson();
        for (MatchInfo matchInfo : matchInfos) {
            WebClientDTO webClientDTO = new WebClientDTO(apiInfo.getApiScheme(), apiInfo.getApiHost(), apiInfo.getApiUrl());
            String response = webclientCallService.webclientGetWithMatchIdWithToken(webClientDTO, apiKey, matchInfo.getId());
            if (StringUtils.isEmpty(response)) {
                continue;
            } else {
                /**** 카프카 전송 ****/
                MatchDetail matchDetail = setMatchInfoDetail(response);
                String processedResponse = gson.toJson(matchDetail);

                myProducer.sendMessage(kafkaInfo, processedResponse);
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

        Info infoData = gson.fromJson(jsonObject.getAsJsonObject("info"), Info.class);
        matchDetail.setInfo(infoData);

        MetaData metaData = gson.fromJson(jsonObject.getAsJsonObject("metadata"), MetaData.class);
        matchDetail.setMetadata(metaData);

        return matchDetail;
    }

    private Info setInfo(String response) {
        Gson gson = new Gson();
        JsonObject jsonObject = new UtilManager().StringToJsonObject(response);

        Info infoData = gson.fromJson(jsonObject.getAsJsonObject("info"), Info.class);

        return infoData;
    }

    public MatchInfo jsonToEntity(String response, Long apiKeyId){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
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

    private static <T> List<List<T>> partitionList(List<T> list, int partitionSize) {
        List<List<T>> partitions = new ArrayList<>();
        int size = list.size();
        for (int i = 0; i < size; i += partitionSize) {
            int end = Math.min(size, i + partitionSize);
            partitions.add(list.subList(i, end));
        }
        return partitions;
    }

    public void listToEntity(List<String> response, Long apiKeyId) {
        for (String puuid : response) {
            MatchInfo matchInfo = new MatchInfo(puuid, apiKeyId);
            matchInfoSave(matchInfo);
        }
    }

    public Map<String, String> setParams(List<ApiParams> apiParamsList, String startDate, String endDate){
        Map<String, String> map = new HashMap<>();
        for(ApiParams apiParams : apiParamsList){
            if(Boolean.TRUE.equals(apiParams.getIsRequired())){
                map.put(apiParams.getApiKey(), apiParams.getApiValue());
                if(Boolean.TRUE.equals(apiParams.getDateParamRequired()) && "startTime".equals(apiParams.getApiKey())){
                    map.put(apiParams.getApiKey(), setStartDate(startDate));
                }else if(Boolean.TRUE.equals(apiParams.getDateParamRequired()) && "endTime".equals(apiParams.getApiKey())){
                    map.put(apiParams.getApiKey(),  setEndDate(endDate));
                }
            }
        }
        return map;
    }

    public String setStartDate(String startDate){
        return String.valueOf(LocalDateTime.of(LocalDate.parse(startDate),LocalTime.of(0,0,0)).toEpochSecond(ZoneOffset.UTC));
    }

    public String setEndDate(String endDate){
        return String.valueOf(LocalDateTime.of(LocalDate.parse(endDate),LocalTime.of(23,59,59)).toEpochSecond(ZoneOffset.UTC));
    }
}
