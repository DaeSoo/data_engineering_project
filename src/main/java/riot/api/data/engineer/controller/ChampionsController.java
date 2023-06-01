package riot.api.data.engineer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import riot.api.data.engineer.apiresult.ApiResult;
import riot.api.data.engineer.entity.KafkaInfo;
import riot.api.data.engineer.entity.Version;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.champions.Champions;
import riot.api.data.engineer.entity.champions.Data;
import riot.api.data.engineer.service.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/ddragon/champions")
@RequiredArgsConstructor
public class ChampionsController {

    private final ChampionsService championsService;
    private final ApiInfoService apiInfoService;
    private final VersionService versionService;
    private final KafkaInfoService kafkaInfoService;
    private final WebClient webClient;

    @GetMapping("")
    public ResponseEntity<ApiResult> getChampions() {


        try {
            /** api 정보 조회 **/
            ApiInfo apiInfo = apiInfoService.findOneByName(new Exception().getStackTrace()[0].getMethodName());
            /** 버전 조회 **/
            Version version = versionService.findOneByCurrentVersion();
            /** api 정보로 kafka 정보 조회 **/
            KafkaInfo kafkaInfo = kafkaInfoService.findOneByApiInfoId(apiInfo.getApiInfoId());
            /** API pathVariable 세팅 **/
            List<String> pathVariable = championsService.setPathVariableVersion(version);
            /** API 호출 **/
            String response = championsService.apiCall(webClient, apiInfo, pathVariable);
            /** String to POJO **/
            Champions champions = championsService.setChampions(response);
            /** 카프카 메세지 전송 **/
            List<Data> dataList = championsService.sendKafkaMessage(kafkaInfo, champions);

            return new ResponseEntity<>(new ApiResult(200, "success", dataList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResult(500, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
