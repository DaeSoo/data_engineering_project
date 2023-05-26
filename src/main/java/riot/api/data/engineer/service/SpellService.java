package riot.api.data.engineer.service;

import org.springframework.web.reactive.function.client.WebClient;
import riot.api.data.engineer.entity.KafkaInfo;
import riot.api.data.engineer.entity.Version;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.spells.Spell;
import riot.api.data.engineer.entity.spells.Spells;

import java.util.List;

public interface SpellService {
    Spells setSpells(String response);

    List<String> setPathVariableVersion(Version version);

    String apiCall(WebClient webClient, ApiInfo apiInfo, List<String> pathVariable);

    List<Spell> sendKafkaMessage(KafkaInfo kafkaInfo, Spells spellList);
}
