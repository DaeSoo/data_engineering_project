package riot.api.data.engineer.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "kafka_info")
@Getter
@Setter
public class KafkaInfo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "topic_name")
    String topicName;
    @Column(name = "partition")
    Integer partition;
    @Column(name = "replicas")
    Integer replicas;
    @Column(name = "api_info_id")
    Long apiInfoId;


}
