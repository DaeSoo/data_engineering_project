package riot.api.data.engineer.serviceimpl;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.Champions;
import riot.api.data.engineer.entity.Data;
import riot.api.data.engineer.entity.Version;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.service.ChampionsService;
import riot.api.data.engineer.service.WebclientCallService;
import riot.api.data.engineer.utils.UtilManager;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChampionsServiceImpl implements ChampionsService {
    private final WebclientCallService webclientCallService;

    public ChampionsServiceImpl(WebclientCallService webclientCallService) {
        this.webclientCallService = webclientCallService;
    }

    @Override
    public Champions setChampions(String response){
        JsonObject jsonObject = new UtilManager().StringToJsonObject(response);

        Champions champions = new Champions();
        Gson gson = new Gson();

        champions.setVersion(String.valueOf(jsonObject.get("version")));
        champions.setType(String.valueOf(jsonObject.get("type")));
        champions.setFormat(String.valueOf(jsonObject.get("format")));
        JsonObject championData = jsonObject.getAsJsonObject("data");

        List<Data> dataList = new ArrayList<>();
        championData.keySet().forEach(key ->{
            String championName = key;
            JsonObject jsonObject2 = championData.getAsJsonObject(championName);
            Data data = gson.fromJson(jsonObject2, Data.class);
            dataList.add(data);
        });
        champions.setDataList(dataList);

        return champions;
    }

    @Override
    public String getChampionsInfo(ApiInfo apiInfo, Version version) {
        WebClientDTO webClientDTO = new WebClientDTO(apiInfo.getApiScheme(),apiInfo.getApiHost(),apiInfo.getApiUrl());

        return webclientCallService.webclientGetWithVersion(webClientDTO,version.getVersion());
    }
}
