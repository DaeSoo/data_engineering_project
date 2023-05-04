package riot.api.data.engineer.service;

import riot.api.data.engineer.entity.KafkaInfo;

public interface KafkaInfoService {
    KafkaInfo findOneByApiInfoId(Long apiInfoId);
}
