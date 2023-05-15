package riot.api.data.engineer.controller;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.KafkaInfo;
import riot.api.data.engineer.entity.MyProducer;
import riot.api.data.engineer.entity.Version;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.champions.Data;
import riot.api.data.engineer.entity.spells.Spell;
import riot.api.data.engineer.entity.spells.Spells;
import riot.api.data.engineer.service.*;

@Slf4j
@RestController
@RequestMapping(value = "/ddragon/spells")
public class SpellsController {

    private final ApiInfoService apiInfoService;
    private final VersionService versionService;
    private final WebclientCallService webclientCallService;
    private final KafkaInfoService kafkaInfoService;
    private final MyProducer myProducer;
    private final SpellService spellService;

    public SpellsController(ApiInfoService apiInfoService, VersionService versionService, WebclientCallService webclientCallService, KafkaInfoService kafkaInfoService, MyProducer myProducer, SpellService spellService) {
        this.apiInfoService = apiInfoService;
        this.versionService = versionService;
        this.webclientCallService = webclientCallService;
        this.kafkaInfoService = kafkaInfoService;
        this.myProducer = myProducer;
        this.spellService = spellService;
    }

    @GetMapping("/get")
    public String getSpells() {

        String apiName = "/ddragon/spells/get";

        ApiInfo apiInfo = apiInfoService.findOneByName(apiName);
        Version version = versionService.findOneByName();
        KafkaInfo kafkaInfo = kafkaInfoService.findOneByApiInfoId(apiInfo.getApiInfoId());

        String response = webclientCallService.webclientGetWithVersion(new WebClientDTO(apiInfo.getApiScheme(), apiInfo.getApiHost(), apiInfo.getApiUrl()), version.getVersion());

        Spells spells = spellService.setSpells(response);

        Gson gson = new Gson();
        for (Spell spell : spells.getSpellList()) {
            String json = gson.toJson(spell);
            myProducer.sendMessage(kafkaInfo, json);
        }

        return "success";
    }

}
