package riot.api.data.engineer.controller;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.KafkaInfo;
import riot.api.data.engineer.entity.MyProducer;
import riot.api.data.engineer.entity.Version;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.runes.RuneList;
import riot.api.data.engineer.service.*;

@Slf4j
@RestController
@RequestMapping(value = "/ddragon/runes")
@RequiredArgsConstructor
public class RunesController {

    private final ApiInfoService apiInfoService;

    private final VersionService versionService;
    private final WebclientCallService webclientCallService;
    private final KafkaInfoService kafkaInfoService;
    private final MyProducer myProducer;
    private final RuneService runeService;

    @GetMapping("/get")
    public String getRunes() {

        String apiName = "/ddragon/runes/get";

        ApiInfo apiInfo = apiInfoService.findOneByName(apiName);
        Version version = versionService.findOneByName();
        KafkaInfo kafkaInfo = kafkaInfoService.findOneByApiInfoId(apiInfo.getApiInfoId());

        String response = webclientCallService.webclientGetWithVersion(new WebClientDTO(apiInfo.getApiScheme(), apiInfo.getApiHost(), apiInfo.getApiUrl()), version.getVersion());

        RuneList runeList = runeService.setRuneList(response);
        Gson gson = new Gson();
        runeList.getRuneList().forEach(rune -> {
            String json = gson.toJson(rune);
            myProducer.sendMessage(kafkaInfo, json);
        });

        return response;
    }

}
