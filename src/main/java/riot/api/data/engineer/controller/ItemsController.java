package riot.api.data.engineer.controller;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.KafkaInfo;
import riot.api.data.engineer.entity.MyProducer;
import riot.api.data.engineer.entity.Version;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.items.Items;
import riot.api.data.engineer.service.ApiInfoService;
import riot.api.data.engineer.service.ItemService;
import riot.api.data.engineer.service.KafkaInfoService;
import riot.api.data.engineer.service.VersionService;
import riot.api.data.engineer.service.WebclientCallService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/ddragon/items")
@RequiredArgsConstructor
public class ItemsController {

    private final ApiInfoService apiInfoService;
    private final VersionService versionService;
    private final WebclientCallService webclientCallService;
    private final KafkaInfoService kafkaInfoService;
    private final MyProducer myProducer;
    private final ItemService itemService;


    @GetMapping("/get")
    public String getItems() {

        String apiName = "/ddragon/items/get";

        ApiInfo apiInfo = apiInfoService.findOneByName(apiName);
        Version version = versionService.findOneByName();
        KafkaInfo kafkaInfo = kafkaInfoService.findOneByApiInfoId(apiInfo.getApiInfoId());
        List<String> pathVariable = new ArrayList<>();
        pathVariable.add(version.getVersion());

        String response = webclientCallService.getWebClientToString(WebClientDTO.builder().scheme(apiInfo.getApiScheme()).host(apiInfo.getApiHost()).path(apiInfo.getApiUrl()).pathVariable(pathVariable).build(), null);

        Items items = itemService.setItems(response);
        Gson gson = new Gson();
        items.getItemList().forEach( item -> {
            item.setVersion(items.getVersion().replaceAll("\"",""));
            item.setDescription(item.getDescription().replaceAll("<[^>]+>",""));
            String json = gson.toJson(item);
            myProducer.sendMessage(kafkaInfo, json);
        });

        if (!ObjectUtils.isEmpty(response)) {
            return response;
        } else {
            return null;
        }
    }

}
