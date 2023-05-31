package riot.api.data.engineer.serviceimpl;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
import riot.api.data.engineer.entity.runes.Rune;
import riot.api.data.engineer.entity.runes.RuneList;
import riot.api.data.engineer.service.RuneService;
import riot.api.data.engineer.utils.UtilManager;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RuneServiceImpl implements RuneService {
    private final MyProducer myProducer;

    @Override
    public RuneList setRuneList(String response) {
        Gson gson = new Gson();

        RuneList runeList = new RuneList();
        List<Rune> runes = new ArrayList<>();

        JsonArray jsonArray = new UtilManager().StringToJsonArray(response);
        jsonArray.forEach(jsonElement -> {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Rune rune = gson.fromJson(jsonObject, Rune.class);
            rune.getSlots().forEach(slot -> slot.getRunes().forEach(runeDetail -> {
                runeDetail.setShortDesc(runeDetail.getShortDesc().replaceAll("<[^>]+>",""));
                runeDetail.setLongDesc(runeDetail.getLongDesc().replaceAll("<[^>]+>",""));
            }));
            runes.add(rune);
        });
        runeList.setRuneList(runes);
        return runeList;
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
    public List<Rune> sendKafkaMessage(KafkaInfo kafkaInfo, RuneList runeList,Version version) {
        Gson gson = new Gson();
        runeList.getRuneList().forEach(rune -> {
            rune.setVersion(version.getVersion().replaceAll("\"", ""));
            String json = gson.toJson(rune);
            myProducer.sendMessage(kafkaInfo, json);
        });
        return runeList.getRuneList();
    }

}
