package riot.api.data.engineer.serviceimpl;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.KafkaInfo;
import riot.api.data.engineer.entity.MyProducer;
import riot.api.data.engineer.entity.Version;
import riot.api.data.engineer.entity.WebClientCaller;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.spells.Spell;
import riot.api.data.engineer.entity.spells.Spells;
import riot.api.data.engineer.service.SpellService;
import riot.api.data.engineer.utils.UtilManager;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpellServiceImpl implements SpellService {
    private final MyProducer myProducer;
    @Override
    public Spells setSpells(String response) {
        JsonObject jsonObject = new UtilManager().StringToJsonObject(response);

        Spells spells = new Spells();
        Gson gson = new Gson();

        spells.setVersion(String.valueOf(jsonObject.get("version")));
        spells.setType(String.valueOf(jsonObject.get("type")));

        JsonObject spellsData = jsonObject.getAsJsonObject("data");
        List<Spell> spellList = new ArrayList<>();

        spellsData.keySet().forEach( key -> {
            String spellName = key;
            JsonObject spellJson = spellsData.getAsJsonObject(spellName);
            Spell spell = gson.fromJson(spellJson,Spell.class);
            spellList.add(spell);
        });
        spells.setSpellList(spellList);

        return spells;
    }

    @Override
    public List<String> setPathVariableVersion(Version version) {
        List<String> pathVariable = new ArrayList<>();
        pathVariable.add(version.getVersion());
        return pathVariable;
    }

    @Override
    public String apiCall(WebClient webClient, ApiInfo apiInfo, List<String> pathVariable) {
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
        return webClientCaller.getWebClientToString();
    }

    @Override
    public List<Spell> sendKafkaMessage(KafkaInfo kafkaInfo, Spells spellList) {
        Gson gson = new Gson();
        for (Spell spell : spellList.getSpellList()) {
            spell.setVersion(spellList.getVersion().replaceAll("\"",""));
            String json = gson.toJson(spell);
            myProducer.sendMessage(kafkaInfo, json);
        }
        return spellList.getSpellList();
    }

}
