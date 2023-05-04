package riot.api.data.engineer.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaAdminConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic setMatchTopic() {
//        return TopicBuilder.name("match")
//                .partitions(3)
//                .replicas(3)
//                .compact()
//                .build();
        return new NewTopic("match",3, (short)3).configs(Collections.singletonMap("group.id", "groupA"));
    }

    @Bean
    public NewTopic setRunes() {
        return TopicBuilder.name("runes")
                .partitions(3)
                .replicas(3)
                .compact()
                .build();
    }

    @Bean
    public NewTopic setChampions() {
        return TopicBuilder.name("champions")
                .partitions(3)
                .replicas(3)
                .compact()
                .build();
    }

    @Bean
    public NewTopic setItems() {
        return TopicBuilder.name("items")
                .partitions(3)
                .replicas(3)
                .compact()
                .build();
    }

    @Bean
    public NewTopic setSpells() {
        return TopicBuilder.name("spells")
                .partitions(3)
                .replicas(3)
                .compact()
                .build();
    }



}
