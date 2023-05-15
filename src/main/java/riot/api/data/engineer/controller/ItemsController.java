package riot.api.data.engineer.controller;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
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
import riot.api.data.engineer.service.*;

@Slf4j
@RestController
@RequestMapping(value = "/ddragon/items")
public class ItemsController {

    private final ApiInfoService apiInfoService;
    private final VersionService versionService;
    private final WebclientCallService webclientCallService;
    private final KafkaInfoService kafkaInfoService;
    private final MyProducer myProducer;
    private final ItemService itemService;

    public ItemsController(ApiInfoService apiInfoService, VersionService versionService, WebclientCallService webclientCallService, KafkaTemplate<String, String> kafkaTemplate, KafkaInfoService kafkaInfoService, MyProducer myProducer, ItemService itemService) {
        this.apiInfoService = apiInfoService;
        this.versionService = versionService;
        this.webclientCallService = webclientCallService;
        this.kafkaInfoService = kafkaInfoService;
        this.myProducer = myProducer;
        this.itemService = itemService;
    }

    @GetMapping("/get")
    public String getItems() {

        String apiName = "/ddragon/items/get";

        ApiInfo apiInfo = apiInfoService.findOneByName(apiName);
        Version version = versionService.findOneByName();
        KafkaInfo kafkaInfo = kafkaInfoService.findOneByApiInfoId(apiInfo.getApiInfoId());

        String response = webclientCallService.webclientGetWithVersion(new WebClientDTO(apiInfo.getApiScheme(), apiInfo.getApiHost(), apiInfo.getApiUrl()), version.getVersion());

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
