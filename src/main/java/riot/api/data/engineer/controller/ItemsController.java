package riot.api.data.engineer.controller;

import com.google.gson.JsonObject;
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
import riot.api.data.engineer.service.ApiInfoService;
import riot.api.data.engineer.service.KafkaInfoService;
import riot.api.data.engineer.service.VersionService;
import riot.api.data.engineer.service.WebclientCallService;
import riot.api.data.engineer.utils.UtilManager;

@Slf4j
@RestController
@RequestMapping(value = "/ddragon/items")
public class ItemsController {

    private final ApiInfoService apiInfoService;
    private final VersionService versionService;
    private final WebclientCallService webclientCallService;
    private final KafkaInfoService kafkaInfoService;
    private final MyProducer myProducer;

    public ItemsController(ApiInfoService apiInfoService, VersionService versionService, WebclientCallService webclientCallService, KafkaTemplate<String,String> kafkaTemplate, KafkaInfoService kafkaInfoService, MyProducer myProducer){
        this.apiInfoService = apiInfoService;
        this.versionService = versionService;
        this.webclientCallService = webclientCallService;
        this.kafkaInfoService = kafkaInfoService;
        this.myProducer = myProducer;
    }

    @GetMapping("/get")
    public String getItems() {

        String apiName = "/ddragon/items/get";

        ApiInfo apiInfo = apiInfoService.findOneByName(apiName);
        Version version = versionService.findOneByName();
        KafkaInfo kafkaInfo = kafkaInfoService.findOneByApiInfoId(apiInfo.getApiInfoId());

        String response = webclientCallService.webclientGetWithVersion(new WebClientDTO(apiInfo.getApiScheme(),apiInfo.getApiHost(),apiInfo.getApiUrl()),version.getVersion());

        myProducer.sendMessage(kafkaInfo,response);

        if(!ObjectUtils.isEmpty(response)){
            return "success";
        }
        else{
            return null;
        }
    }

}
