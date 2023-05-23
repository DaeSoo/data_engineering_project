package riot.api.data.engineer.serviceimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import riot.api.data.engineer.apiresult.ApiResult;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.*;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.api.ApiKey;
import riot.api.data.engineer.entity.api.ApiParams;
import riot.api.data.engineer.entity.matchdetail.Info;
import riot.api.data.engineer.entity.matchdetail.MatchDetail;
import riot.api.data.engineer.entity.matchdetail.MetaData;
import riot.api.data.engineer.repository.MatchInfoQueryRepository;
import riot.api.data.engineer.repository.MatchInfoRepository;
import riot.api.data.engineer.service.*;
import riot.api.data.engineer.utils.EpochTimestampConverter;
import riot.api.data.engineer.utils.UtilManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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
    private final ApiKeyService apiKeyService;
    private final KafkaInfoService kafkaInfoService;
    private final MyProducer myProducer;
    private final ApiParamsService apiParamsService;
    private final MatchInfoQueryRepository matchInfoQueryRepository;
    private final WebClient webClient;

    @Override
    public MatchInfo matchInfoSave(MatchInfo matchInfo) {
        return matchInfoRepository.save(matchInfo);
    }

    /**
     * @param apiKey  ApiKey Entity
     * @param apiName ApiInfo apiName(method명)
     */
    @Override
    public void matchApiRequest(ApiKey apiKey, String apiName, String startDate, String endDate) {
        try {
            List<UserInfoDetail> userInfoDetailList = userInfoDetailService.userInfoDeatilList(apiKey.getApiKeyId());
            ApiInfo apiInfo = apiInfoService.findOneByName(apiName);
            List<ApiParams> apiParamsList = apiParamsService.getApiParamsList(apiInfo.getApiInfoId());
            Map<String, String> queryParams = setParams(apiParamsList, startDate, endDate);

            for (UserInfoDetail userInfoDetail : userInfoDetailList) {
                List<String> pathVariable = new ArrayList<>();
                pathVariable.add(userInfoDetail.getPuuid());
                WebClientDTO webClientDTO = WebClientDTO.builder()
                        .apiName(apiName)
                        .scheme(apiInfo.getApiScheme())
                        .host(apiInfo.getApiHost())
                        .path(apiInfo.getApiUrl())
                        .pathVariable(pathVariable)
                        .queryParam(queryParams).build();

                WebClientCaller webClientCaller = WebClientCaller.builder()
                        .webClientDTO(webClientDTO)
                        .webclient(webClient)
                        .build();

                List<String> response = webClientCaller.getWebClientToList(apiKey);
                List<MatchInfo> matchInfoList = responseToEntity(response, apiKey.getApiKeyId());

                for (MatchInfo matchInfo : matchInfoList) {
                    matchInfoRepository.save(matchInfo);
                }
                Thread.sleep(1200);
            }
        } catch (Exception e) {
            log.error(" === ERROR === ");
            log.error(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ApiResult> createThread(String method, String startDate, String endDate) {
        List<Callable<Integer>> tasks = new ArrayList<>();
        List<ApiKey> apiKeyList = apiKeyService.findList();
        ExecutorService executorService = Executors.newFixedThreadPool(apiKeyList.size());
        try {
            for (ApiKey apiKey : apiKeyList) {
                Callable<Integer> task = () -> {
                    matchApiRequest(apiKey, method, startDate, endDate);
                    return 200;
                };
                tasks.add(task);
            }
            executorService.invokeAll(tasks);
            executorService.shutdown();
            return new ResponseEntity<>(new ApiResult(200, "success", null), HttpStatus.OK);
        }
        catch (Exception e) {
            log.info(e.getMessage());
            executorService.shutdownNow();
            return new ResponseEntity<>(new ApiResult(500, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<MatchInfo> getMatchInfoList() {
        return matchInfoRepository.findAll();
    }


    @Override
    public ResponseEntity<ApiResult> apiCallBatch(ApiInfo apiInfo, List<ApiKey> apiKeyList) {
        int apiKeyCount = apiKeyList.size();
        ExecutorService executorService = Executors.newFixedThreadPool(apiKeyCount);

        try {
            List<MatchInfo> matchInfoList = matchInfoQueryRepository.findMatchInfoByCollectCompleteYn();

            List<List<MatchInfo>> partionMatchInfoList = partitionList(matchInfoList, apiKeyCount);
            List<Callable<Integer>> tasks = new ArrayList<>();
            KafkaInfo kafkaInfo = kafkaInfoService.findOneByApiInfoId(apiInfo.getApiInfoId());

            for (int i = 0; i < apiKeyCount; i++) {
                List<MatchInfo> matchInfoPartition = partionMatchInfoList.get(i);
                ApiKey apiKey = apiKeyList.get(i);
                Callable<Integer> task = () -> {
                    apiCallRepeat(apiInfo, apiKey, matchInfoPartition, kafkaInfo);
                    return 200;
                };
                tasks.add(task);
            }
            executorService.invokeAll(tasks);
            executorService.shutdown();
            return new ResponseEntity<>(new ApiResult(200, "success", null), HttpStatus.OK);
        } catch (Exception e) {
            executorService.shutdownNow();
            log.error(e.getMessage());
            return new ResponseEntity<>(new ApiResult(500, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    protected void apiCallRepeat(ApiInfo apiInfo, ApiKey apiKey, List<MatchInfo> matchInfos, KafkaInfo kafkaInfo) {
        Gson gson = new Gson();

        for (MatchInfo matchInfo : matchInfos) {
            try {
                List<String> path = new ArrayList<>();
                path.add(matchInfo.getId());
                WebClientDTO webClientDTO = WebClientDTO.builder().scheme(apiInfo.getApiScheme())
                        .host(apiInfo.getApiHost())
                        .path(apiInfo.getApiUrl())
                        .pathVariable(path).build();

                WebClientCaller webClientCaller = WebClientCaller.builder()
                        .webClientDTO(webClientDTO)
                        .webclient(webClient)
                        .build();
                String response = webClientCaller.getWebClientToString(apiKey);

                if (StringUtils.isEmpty(response)) {
                    continue;
                } else {
                    MatchDetail matchDetail = setMatchInfoDetail(response);
                    EpochTimestampConverter epochTimestampConverter = new EpochTimestampConverter(matchDetail.getInfo().getGameStartTimestamp());
                    matchDetail.setCollectDate(epochTimestampConverter.convertToDateString());
                    String processedResponse = gson.toJson(matchDetail);

                    myProducer.sendMessage(kafkaInfo, processedResponse);
                    matchInfo.setCollectCompleteYn(true);
                    matchInfoSave(matchInfo);
                }
                Thread.sleep(1200);
            } catch (Exception e) {
                log.info("=== ERROR ===");
                log.info(" ApiKey : " + matchInfo.getApiKeyId());
                log.info("ID : " + matchInfo.getId());
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

    private static <T> List<List<T>> partitionList(List<T> list, int apiKeyCount) {
        List<List<T>> partitions = new ArrayList<>();
        int size = list.size();
        int partitionSize = size / apiKeyCount;
        int remainder = size % apiKeyCount;
        int startIndex = 0;

        for (int i = 0; i < apiKeyCount; i++) {
            int endIndex = startIndex + partitionSize;
            if (remainder > 0) {
                endIndex++;
                remainder--;
            }
            endIndex = Math.min(endIndex, size);
            partitions.add(list.subList(startIndex, endIndex));
            startIndex = endIndex;
        }

        return partitions;
    }

    public List<MatchInfo> responseToEntity(List<String> response, Long apiKeyId) {
        List<MatchInfo> matchInfoList = new ArrayList<>();
        for (String id : response) {
            MatchInfo matchInfo = new MatchInfo(id, apiKeyId);
            matchInfoList.add(matchInfo);
        }
        return matchInfoList;
    }

    public Map<String, String> setParams(List<ApiParams> apiParamsList, String startDate, String endDate) {
        Map<String, String> map = new HashMap<>();
        for (ApiParams apiParams : apiParamsList) {
            if (apiParams.getIsRequired()) {
                map.put(apiParams.getApiKey(), apiParams.getApiValue());
                if (apiParams.getDateParamRequired() && "startTime".equals(apiParams.getApiKey())) {
                    map.put(apiParams.getApiKey(), setStartDate(startDate));
                } else if (apiParams.getDateParamRequired() && "endTime".equals(apiParams.getApiKey())) {
                    map.put(apiParams.getApiKey(), setEndDate(endDate));
                }
            }
        }
        return map;
    }

    public String setStartDate(String startDate) {
        return String.valueOf(LocalDateTime.of(LocalDate.parse(startDate), LocalTime.of(0, 0, 0)).atZone(ZoneId.of("Asia/Seoul")).toEpochSecond());
    }

    public String setEndDate(String endDate) {
        return String.valueOf(LocalDateTime.of(LocalDate.parse(endDate), LocalTime.of(23, 59, 59)).atZone(ZoneId.of("Asia/Seoul")).toEpochSecond());
    }
}
