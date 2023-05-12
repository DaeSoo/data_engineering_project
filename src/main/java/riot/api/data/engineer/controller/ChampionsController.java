package riot.api.data.engineer.controller;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.KafkaInfo;
import riot.api.data.engineer.entity.MyProducer;
import riot.api.data.engineer.entity.Version;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.champions.Champions;
import riot.api.data.engineer.entity.champions.Data;
import riot.api.data.engineer.service.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/ddragon/champions")
public class ChampionsController {

    private final ChampionsService championsService;
    private final WebclientCallService webclientCallService;
    private final ApiInfoService apiInfoService;
    private final VersionService versionService;
    private final KafkaInfoService kafkaInfoService;
    private final MyProducer myProducer;

    public ChampionsController(ChampionsService championsService, WebclientCallService webclientCallService, ApiInfoService apiInfoService, VersionService versionService, KafkaInfoService kafkaInfoService, MyProducer myProducer) {
        this.championsService = championsService;
        this.kafkaInfoService = kafkaInfoService;
        this.myProducer = myProducer;
        this.webclientCallService = webclientCallService;
        this.apiInfoService = apiInfoService;
        this.versionService = versionService;
    }

    @GetMapping("/get")
    public String getChampions() {

        String apiName = "/ddragon/champions/get";

        ApiInfo apiInfo = apiInfoService.findOneByName(apiName);
        Version version = versionService.findOneByName();
        KafkaInfo kafkaInfo = kafkaInfoService.findOneByApiInfoId(apiInfo.getApiInfoId());

        String response = webclientCallService.webclientGetWithVersion(new WebClientDTO(apiInfo.getApiScheme(), apiInfo.getApiHost(), apiInfo.getApiUrl()), version.getVersion());

        Champions champions = championsService.setChampions(response);
        List<Data> datalist = champions.getDataList();
        Gson gson = new Gson();

        for (Data data : datalist) {
            String json = gson.toJson(data);
            myProducer.sendMessage(kafkaInfo, json);
        }
        String json = gson.toJson(champions);

        if (!ObjectUtils.isEmpty(champions)) {
            return json;
        } else {
            return null;
        }
    }

}
