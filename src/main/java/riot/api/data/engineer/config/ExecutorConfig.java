package riot.api.data.engineer.config;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class ExecutorConfig {
    private final ThreadPoolTaskExecutor taskExecutor;

    public ExecutorConfig() {
        taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1);
        taskExecutor.setMaxPoolSize(1);
        taskExecutor.setQueueCapacity(25);
        taskExecutor.initialize();
    }

    public void execute(Runnable task) {
        taskExecutor.execute(task);
    }
}
