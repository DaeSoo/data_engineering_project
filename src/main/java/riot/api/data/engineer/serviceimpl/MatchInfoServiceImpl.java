package riot.api.data.engineer.serviceimpl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.*;
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
    private final MatchInfoQueryRepository matchInfoQueryRepository;
    private final WebClient webClient;


    @Override
    public ResponseEntity<ApiResult> createMatchInfoTasks(String method, String startDate, String endDate) {
        log.info("===== createMatchInfoTasks Start =====");
        List<Callable<Integer>> tasks = new ArrayList<>();
        List<ApiKey> apiKeyList = apiKeyService.findList();
        ExecutorService executorService = Executors.newFixedThreadPool(apiKeyList.size());
        try {
            for (ApiKey apiKey : apiKeyList) {
                Callable<Integer> task = () -> {
                    matchListApiCall(apiKey, method, startDate, endDate);
                    return 200;
                };
                tasks.add(task);
            }
            executorService.invokeAll(tasks);
            executorService.shutdown();
            log.info("===== createMatchInfoTasks End =====");
            return new ResponseEntity<>(new ApiResult(200, "success", null), HttpStatus.OK);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            executorService.shutdownNow();
            log.error("===== createMatchInfoTasks End =====");
            return new ResponseEntity<>(new ApiResult(500, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    public void matchListApiCall(ApiKey apiKey, String apiName, String startDate, String endDate) {

        try {
            List<UserInfoDetail> userInfoDetailList = userInfoDetailService.findUserInfoDetailListByApiKey(apiKey.getApiKeyId());
            ApiInfo apiInfo = apiInfoService.findOneByName(apiName);
            Map<String, String> queryParams = setParams(apiInfo.getApiParams(), startDate, endDate);

            for (UserInfoDetail userInfoDetail : userInfoDetailList) {

                List<String> pathVariableList = new ArrayList<>();
                pathVariableList.add(userInfoDetail.getPuuid());

                WebClientCaller webClientCaller = buildWebClientCaller(pathVariableList,queryParams,apiInfo);
                /*** 매치리스트 API 호출  ***/
                List<String> response = webClientCaller.getWebClientToList(apiKey);
                /*** String to MatchInfo ***/
                List<MatchInfo> matchInfoList = responseToEntity(response, apiKey.getApiKeyId());
                /*** matchInfo 저장 ***/
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
    public ResponseEntity<ApiResult> createMatchInfoDetailTasks(ApiInfo apiInfo, List<ApiKey> apiKeyList) {
        log.info("===== createMatchInfoDetailTasks Start =====");

        int apiKeyCount = apiKeyList.size();
        ExecutorService executorService = Executors.newFixedThreadPool(apiKeyCount);
        try {

            /*** 전체 데이터 find ***/
            List<List<MatchInfo>> partionMatchInfoList = partitionList(matchInfoQueryRepository.findMatchInfoByCollectCompleteYnIsFalse(),apiKeyCount);

            List<Callable<Integer>> tasks = new ArrayList<>();
            KafkaInfo kafkaInfo = kafkaInfoService.findOneByApiInfoId(apiInfo.getApiInfoId());

            for (int i = 0; i < apiKeyCount; i++) {
                List<MatchInfo> matchInfoPartition = partionMatchInfoList.get(i);
                ApiKey apiKey = apiKeyList.get(i);
                Callable<Integer> task = () -> {
                    matchDetailApiCall(apiInfo, apiKey, matchInfoPartition, kafkaInfo);
                    return 200;
                };
                tasks.add(task);
            }
            executorService.invokeAll(tasks);
            executorService.shutdown();

            log.info("===== MatchInfoDetailTasks End =====");
            return new ResponseEntity<>(new ApiResult(200, "success", null), HttpStatus.OK);
        } catch (Exception e) {
            executorService.shutdownNow();
            log.error(e.getMessage());
            log.error("===== MatchInfoDetailTasks End =====");
            return new ResponseEntity<>(new ApiResult(500, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public ApiResult deleteAllByCollectCompleteYn(String collectCompleteYn) {
        try{
            Optional<String> optionalCollectCompleteYn = Optional.ofNullable(collectCompleteYn);
            if(optionalCollectCompleteYn.isPresent()){
                if(optionalCollectCompleteYn.get().equals("true")){
                    matchInfoRepository.deleteMatchInfosByCollectCompleteYn(true);
                    return new ApiResult(200,"success",null);
                } else if (optionalCollectCompleteYn.get().equals("false")) {
                    matchInfoRepository.deleteMatchInfosByCollectCompleteYn(false);
                    return new ApiResult(200,"success",null);
                } else {
                    return new ApiResult(400,"파라미터 값이 올바르지 않습니다. (true/false만 허용)","입력된 collectCompleteYn : " + collectCompleteYn);
                }
            }
            else {
                matchInfoRepository.deleteAll();
                return new ApiResult(200,"success",null);
            }
        }catch (Exception e){
            return new ApiResult(500,e.getMessage(),null);
        }

    }

    protected void matchDetailApiCall(ApiInfo apiInfo, ApiKey apiKey, List<MatchInfo> matchInfos, KafkaInfo kafkaInfo) {
        Gson gson = new Gson();

        for (MatchInfo matchInfo : matchInfos) {
            try {
                List<String> pathVariableList = new ArrayList<>();
                pathVariableList.add(matchInfo.getId());

                WebClientCaller webClientCaller = buildWebClientCaller(pathVariableList,null,apiInfo);
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
                log.error("=== ERROR ===");
                log.error(" ApiKey : " + matchInfo.getApiKeyId());
                log.error("ID : " + matchInfo.getId());
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
        return String.valueOf(
                LocalDateTime.of(LocalDate.parse(endDate),
                LocalTime.of(23, 59, 59)).atZone(ZoneId.of("Asia/Seoul")).toEpochSecond());
    }

    private WebClientCaller buildWebClientCaller(List<String> pathVariableList,Map<String, String> queryParams,ApiInfo apiInfo){

        WebClientDTO webClientDTO = WebClientDTO.builder()
                .scheme(apiInfo.getApiScheme())
                .host(apiInfo.getApiHost())
                .path(apiInfo.getApiUrl())
                .pathVariable(pathVariableList)
                .queryParam(queryParams).build();

        return WebClientCaller.builder()
                .webClientDTO(webClientDTO)
                .webclient(webClient)
                .build();
    }
    @Override
    public List<MatchInfo> findMatchInfoList() {
        return matchInfoRepository.findAll();
    }

    @Override
    public MatchInfo matchInfoSave(MatchInfo matchInfo) {
        return matchInfoRepository.save(matchInfo);
    }
}
