package riot.api.data.engineer.serviceimpl;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import riot.api.data.engineer.entity.items.Item;
import riot.api.data.engineer.entity.items.Items;
import riot.api.data.engineer.service.ItemService;
import riot.api.data.engineer.utils.UtilManager;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
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
            String spellName = key;
            JsonObject itemJson = itemsData.getAsJsonObject(spellName);
            Item item = gson.fromJson(itemJson,Item.class);
            itemList.add(item);
        });
        items.setItemList(itemList);

        return items;
    }

}
