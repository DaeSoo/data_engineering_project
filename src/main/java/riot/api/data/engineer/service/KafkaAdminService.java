package riot.api.data.engineer.service;

public interface KafkaAdminService {
    void createTopic(String topicName, int partitions, short replicationFactor);
}
