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
import riot.api.data.engineer.entity.spells.Spell;
import riot.api.data.engineer.entity.spells.Spells;
import riot.api.data.engineer.service.ApiInfoService;
import riot.api.data.engineer.service.KafkaInfoService;
import riot.api.data.engineer.service.SpellService;
import riot.api.data.engineer.service.VersionService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/ddragon/spells")
@RequiredArgsConstructor
public class SpellsController {

    private final ApiInfoService apiInfoService;
    private final VersionService versionService;
    private final KafkaInfoService kafkaInfoService;
    private final SpellService spellService;
    private final WebClient webClient;

    @GetMapping("")
    public ResponseEntity<ApiResult> getSpells() {
        try {
            /** API 정보 조회 **/
            ApiInfo apiInfo = apiInfoService.findOneByName(new Exception().getStackTrace()[0].getMethodName());
            /** 버전 조회 **/
            Version version = versionService.findOneByCurrentVersion();
            /** 카프카 정보 조회 **/
            KafkaInfo kafkaInfo = kafkaInfoService.findOneByApiInfoId(apiInfo.getApiInfoId());

            List<String> pathVariable = spellService.setPathVariableVersion(version);

            String response = spellService.apiCall(webClient, apiInfo, pathVariable);

            Spells spellList = spellService.setSpells(response);

            List<Spell> spells = spellService.sendKafkaMessage(kafkaInfo, spellList);

            return new ResponseEntity<>(new ApiResult(200, "success", spells), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResult(500, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
