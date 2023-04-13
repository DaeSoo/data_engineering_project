package riot.api.data.engineer.service;


import com.google.gson.JsonObject;
import riot.api.data.engineer.entity.Champions;

public interface CollectDragonService {

    Champions setChampions(JsonObject jsonObject);
}
