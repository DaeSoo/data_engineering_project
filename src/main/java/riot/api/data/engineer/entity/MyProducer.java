package riot.api.data.engineer.entity;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MyProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public MyProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topicName,KafkaInfo kafkaInfo,String message) {
        kafkaTemplate.send(topicName,kafkaInfo.getApiInfoId().toString(),message);
    }

    public void sendMessage(KafkaInfo kafkaInfo,String message) {
        kafkaTemplate.send(kafkaInfo.getTopicName(),kafkaInfo.getApiInfoId().toString(),message);
    }

}