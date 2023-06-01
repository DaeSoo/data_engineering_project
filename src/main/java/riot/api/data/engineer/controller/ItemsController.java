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
import riot.api.data.engineer.entity.items.Item;
import riot.api.data.engineer.entity.items.Items;
import riot.api.data.engineer.service.ApiInfoService;
import riot.api.data.engineer.service.ItemService;
import riot.api.data.engineer.service.KafkaInfoService;
import riot.api.data.engineer.service.VersionService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/ddragon/items")
@RequiredArgsConstructor
public class ItemsController {

    private final ApiInfoService apiInfoService;
    private final VersionService versionService;
    private final KafkaInfoService kafkaInfoService;
    private final ItemService itemService;
    private final WebClient webClient;

    @GetMapping("")
    public ResponseEntity<ApiResult> getItems() {
        try {
            /** API 정보 조회 **/
            ApiInfo apiInfo = apiInfoService.findOneByName(new Exception().getStackTrace()[0].getMethodName());
            /** 최근 버전 조회 **/
            Version version = versionService.findOneByCurrentVersion();
            /** api 정보로 kafka 정보 조회 **/
            KafkaInfo kafkaInfo = kafkaInfoService.findOneByApiInfoId(apiInfo.getApiInfoId());
            /** pathVariable 세팅 **/
            List<String> pathVariable = itemService.setPathVariableVersion(version);
            /** API 호출 **/
            String response = itemService.apiCall(webClient, apiInfo, pathVariable);
            /** String to POJO **/
            Items itemList = itemService.setItems(response);
            /** 카프카 메세지 전송 **/
            List<Item> items = itemService.sendKafkaMessage(kafkaInfo, itemList);

            return new ResponseEntity<>(new ApiResult(200, "success", items), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResult(500, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

}
