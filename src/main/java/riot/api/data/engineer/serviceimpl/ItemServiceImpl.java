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
import riot.api.data.engineer.entity.items.Item;
import riot.api.data.engineer.entity.items.Items;
import riot.api.data.engineer.service.ItemService;
import riot.api.data.engineer.utils.UtilManager;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final MyProducer myProducer;
    @Override
    public Items setItems(String response) {
        JsonObject jsonObject = new UtilManager().StringToJsonObject(response);

        Items items = new Items();
        Gson gson = new Gson();

        items.setVersion(String.valueOf(jsonObject.get("version")));
        items.setType(String.valueOf(jsonObject.get("type")));

        JsonObject itemsData = jsonObject.getAsJsonObject("data");
        List<Item> itemList = new ArrayList<>();

        itemsData.keySet().forEach(key -> {
            JsonObject itemJson = itemsData.getAsJsonObject(key);
            Item item = gson.fromJson(itemJson,Item.class);
            item.setItemId(key);
            itemList.add(item);
        });
        items.setItemList(itemList);

        return items;
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
    public List<Item> sendKafkaMessage(KafkaInfo kafkaInfo, Items items) {
        Gson gson = new Gson();
        items.getItemList().forEach( item -> {
            item.setVersion(items.getVersion().replaceAll("\"",""));
            item.setDescription(item.getDescription().replaceAll("<[^>]+>",""));
            String json = gson.toJson(item);
            myProducer.sendMessage(kafkaInfo, json);
        });
        return items.getItemList();
    }

}
