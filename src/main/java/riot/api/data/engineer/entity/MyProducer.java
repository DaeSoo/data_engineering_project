package riot.api.data.engineer.entity;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MyProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public MyProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(KafkaInfo kafkaInfo,String message) {
        kafkaTemplate.send(kafkaInfo.getTopicName(), UUID.randomUUID().toString() ,message);
    }

}