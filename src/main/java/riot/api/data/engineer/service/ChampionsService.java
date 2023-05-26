package riot.api.data.engineer.service;


import org.springframework.web.reactive.function.client.WebClient;
import riot.api.data.engineer.entity.KafkaInfo;
import riot.api.data.engineer.entity.Version;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.champions.Champions;
import riot.api.data.engineer.entity.champions.Data;

import java.util.List;

public interface ChampionsService {

    Champions setChampions(String response);

    String apiCall(WebClient webClient, ApiInfo apiInfo, List<String> pathVariable);

    List<String> setPathVariableVersion(Version version);

    List<Data> sendKafkaMessage(KafkaInfo kafkaInfo, Champions champions);
}
