package riot.api.data.engineer.serviceimpl;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import riot.api.data.engineer.entity.Champions;
import riot.api.data.engineer.entity.Data;
import riot.api.data.engineer.service.CollectDragonService;

import java.util.ArrayList;
import java.util.List;

@Service
public class CollectDragonServiceImpl implements CollectDragonService {
    @Override
    public Champions setChampions(JsonObject jsonObject){
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
}
