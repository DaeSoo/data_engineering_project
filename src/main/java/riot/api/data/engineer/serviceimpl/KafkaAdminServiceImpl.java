package riot.api.data.engineer.serviceimpl;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;
import riot.api.data.engineer.service.KafkaAdminService;

@Service
public class KafkaAdminServiceImpl implements KafkaAdminService {

    private final KafkaAdmin kafkaAdmin;

    public KafkaAdminServiceImpl(KafkaAdmin kafkaAdmin) {
        this.kafkaAdmin = kafkaAdmin;
    }

    // 토픽 생성
    @Override
    public void createTopic(String topicName, int partitions, short replicationFactor) {
        NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);
        kafkaAdmin.createOrModifyTopics(newTopic);
    }

}
