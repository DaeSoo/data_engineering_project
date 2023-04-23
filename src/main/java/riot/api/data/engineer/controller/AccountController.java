package riot.api.data.engineer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;



@Slf4j
@RestController
@RequestMapping(value = "/account")
public class AccountController {
    private final WebClient webClient;
    public AccountController()
    {
        this.webClient = WebClient.create("https://kr.api.riotgames.com");
    }

    @GetMapping("/test")
    public Mono<String> doTest() throws InterruptedException {
        int i = 1;
        for (i = 1; i < 10; i++) {
            int finalI = i;
            Mono<String> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/lol/league/v4/entries/{queue}/{tier}/{division}").queryParam("page", finalI)
                            .build("RANKED_SOLO_5x5","DIAMOND","IV")

                    )

                    .retrieve()
                    .bodyToMono(String.class);
            response.subscribe(value -> log.info(value));
            if(i > 101){
                        webClient.get()
                        .uri(uriBuilder -> uriBuilder.path("/lol/league/v4/entries/{queue}/{tier}/{division}")
                                .build("RANKED_SOLO_5x5","PLATINUM","IV"))
                        .retrieve()
                        .onStatus(
                                HttpStatus.INTERNAL_SERVER_ERROR::equals,
                                res -> res.bodyToMono(String.class).map(Exception::new))
                        .bodyToMono(String.class);

            }
            log.info("page : " + i);
            Thread.sleep(1000);
        }
        return null;
    }



}

