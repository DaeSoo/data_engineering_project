package riot.api.data.engineer.service;


import org.springframework.web.reactive.function.client.WebClient;
import riot.api.data.engineer.entity.KafkaInfo;
import riot.api.data.engineer.entity.Version;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.items.Item;
import riot.api.data.engineer.entity.items.Items;

import java.util.List;

public interface ItemService {
    Items setItems(String response);

    List<String> setPathVariableVersion(Version version);

    String apiCall(WebClient webClient, ApiInfo apiInfo, List<String> pathVariable);

    List<Item> sendKafkaMessage(KafkaInfo kafkaInfo, Items items);
}
