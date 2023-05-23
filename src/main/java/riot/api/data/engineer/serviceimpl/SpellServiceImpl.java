package riot.api.data.engineer.serviceimpl;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import riot.api.data.engineer.entity.spells.Spell;
import riot.api.data.engineer.entity.spells.Spells;
import riot.api.data.engineer.service.SpellService;
import riot.api.data.engineer.utils.UtilManager;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpellServiceImpl implements SpellService {
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

}
