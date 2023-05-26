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
import riot.api.data.engineer.entity.champions.Champions;
import riot.api.data.engineer.entity.champions.Data;
import riot.api.data.engineer.service.ChampionsService;
import riot.api.data.engineer.utils.UtilManager;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChampionsServiceImpl implements ChampionsService {
    private final MyProducer myProducer;

    @Override
    public Champions setChampions(String response) {
        JsonObject jsonObject = new UtilManager().StringToJsonObject(response);

        Champions champions = new Champions();
        Gson gson = new Gson();

        champions.setVersion(String.valueOf(jsonObject.get("version")));
        champions.setType(String.valueOf(jsonObject.get("type")));
        champions.setFormat(String.valueOf(jsonObject.get("format")));
        JsonObject championData = jsonObject.getAsJsonObject("data");

        List<Data> dataList = new ArrayList<>();

        championData.keySet().forEach(key -> {
            String championName = key;
            JsonObject jsonObject2 = championData.getAsJsonObject(championName);
            Data data = gson.fromJson(jsonObject2, Data.class);
            dataList.add(data);
        });
        champions.setDataList(dataList);

        return champions;
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
                .webclient(webClient)
                .webClientDTO(webClientDTO)
                .build();

        return webClientCaller.getWebClientToString();
    }

    @Override
    public List<String> setPathVariableVersion(Version version) {
        List<String> pathVariable = new ArrayList<>();
        pathVariable.add(version.getVersion());
        return  pathVariable;
    }

    @Override
    public List<Data> sendKafkaMessage(KafkaInfo kafkaInfo, Champions champions) {
        List<Data> datalist = champions.getDataList();
        Gson gson = new Gson();

        for (Data data : datalist) {
            data.setVersion(champions.getVersion().replaceAll("\"", ""));
            String json = gson.toJson(data);
            myProducer.sendMessage(kafkaInfo, json);
        }

        return datalist;
    }


}
