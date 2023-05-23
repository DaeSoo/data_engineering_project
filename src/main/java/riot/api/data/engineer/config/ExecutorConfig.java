package riot.api.data.engineer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorConfig {

    @Bean
    public ExecutorService executorService() {
        // 스레드 풀의 크기를 10으로 설정
        return Executors.newFixedThreadPool(10);
    }
}
