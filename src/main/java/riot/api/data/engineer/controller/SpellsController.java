package riot.api.data.engineer.controller;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import riot.api.data.engineer.apiresult.ApiResult;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.KafkaInfo;
import riot.api.data.engineer.entity.MyProducer;
import riot.api.data.engineer.entity.Version;
import riot.api.data.engineer.entity.WebClientCaller;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.spells.Spell;
import riot.api.data.engineer.entity.spells.Spells;
import riot.api.data.engineer.service.ApiInfoService;
import riot.api.data.engineer.service.KafkaInfoService;
import riot.api.data.engineer.service.SpellService;
import riot.api.data.engineer.service.VersionService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/ddragon/spells")
@RequiredArgsConstructor
public class SpellsController {

    private final ApiInfoService apiInfoService;
    private final VersionService versionService;
    private final KafkaInfoService kafkaInfoService;
    private final MyProducer myProducer;
    private final SpellService spellService;
    private final WebClient webClient;

    @GetMapping("/get")
    public ResponseEntity<ApiResult> getSpells() {
        try{
            String apiName = "/ddragon/spells/get";

            ApiInfo apiInfo = apiInfoService.findOneByName(apiName);
            Version version = versionService.findOneByCurrentVersion();
            KafkaInfo kafkaInfo = kafkaInfoService.findOneByApiInfoId(apiInfo.getApiInfoId());

            List<String> pathVariable = new ArrayList<>();
            pathVariable.add(version.getVersion());

            WebClientDTO webClientDTO = WebClientDTO.builder()
                    .scheme(apiInfo.getApiScheme())
                    .host(apiInfo.getApiHost())
                    .path(apiInfo.getApiUrl())
                    .pathVariable(pathVariable)
                    .build();

            WebClientCaller webClientCaller = WebClientCaller.builder()
                    .webClientDTO(webClientDTO)
                    .webclient(webClient)
                    .build();

            String response = webClientCaller.getWebClientToString();

            Spells spells = spellService.setSpells(response);

            Gson gson = new Gson();
            for (Spell spell : spells.getSpellList()) {
                spell.setVersion(spells.getVersion().replaceAll("\"",""));
                String json = gson.toJson(spell);
                myProducer.sendMessage(kafkaInfo, json);
            }

            return new ResponseEntity<>(new ApiResult(200, "success", spells), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResult(500, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
