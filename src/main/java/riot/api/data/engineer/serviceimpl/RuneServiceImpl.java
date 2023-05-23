package riot.api.data.engineer.serviceimpl;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import riot.api.data.engineer.entity.runes.Rune;
import riot.api.data.engineer.entity.runes.RuneList;
import riot.api.data.engineer.service.RuneService;
import riot.api.data.engineer.utils.UtilManager;

import java.util.ArrayList;
import java.util.List;

@Service
public class RuneServiceImpl implements RuneService {

    @Override
    public RuneList setRuneList(String response) {
        Gson gson = new Gson();

        RuneList runeList = new RuneList();
        List<Rune> runes = new ArrayList<>();

        JsonArray jsonArray = new UtilManager().StringToJsonArray(response);
        jsonArray.forEach(jsonElement -> {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Rune rune = gson.fromJson(jsonObject, Rune.class);
            rune.getSlots().forEach(slot -> {
                slot.getRunes().forEach(runeDetail -> {
                    runeDetail.setShortDesc(runeDetail.getShortDesc().replaceAll("<[^>]+>",""));
                    runeDetail.setLongDesc(runeDetail.getLongDesc().replaceAll("<[^>]+>",""));
                });
            });
            runes.add(rune);
        });
        runeList.setRuneList(runes);
        return runeList;
    }

}
