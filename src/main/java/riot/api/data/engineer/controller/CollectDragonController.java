package riot.api.data.engineer.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import riot.api.data.engineer.entity.Champions;
import riot.api.data.engineer.service.CollectDragonService;

@Slf4j
@RestController
@RequestMapping(value = "/collect/dragon")
public class CollectDragonController {

    private final CollectDragonService collectDragonService;
    private final WebClient webClient;

    public CollectDragonController(CollectDragonService collectDragonService,WebClient webClient){
        this.collectDragonService = collectDragonService;
        this.webClient = webClient;
    }

    @GetMapping("/excel/download")
    public void getExcelDownload() {
        log.info("hi");
    }

    @GetMapping("/test")
    public Mono<String> doTest() {
        return webClient.get()
                .uri("http://localhost:20000/collect/dragon/excel/download")
                .retrieve()
                .bodyToMono(String.class);
    }
    @GetMapping("/champions")
    public Champions getChampions() {
        String uri = "https://ddragon.leagueoflegends.com/cdn/13.7.1/data/ko_KR/champion.json";
        String response = webClient.get().uri(uri).retrieve().bodyToMono(String.class).block();
//        return client.get()
//                .uri()
//                .retrieve()
//                .bodyToFlux(Champions.class);

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
        Champions champions = collectDragonService.setChampions(jsonObject);
        return champions;
    }

}
