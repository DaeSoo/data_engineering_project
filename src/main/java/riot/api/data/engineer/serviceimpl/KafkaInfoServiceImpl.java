package riot.api.data.engineer.serviceimpl;

import org.springframework.stereotype.Service;
import riot.api.data.engineer.entity.KafkaInfo;
import riot.api.data.engineer.repository.KafkaInfoQueryRepository;
import riot.api.data.engineer.service.KafkaInfoService;

@Service
public class KafkaInfoServiceImpl implements KafkaInfoService {
    private final KafkaInfoQueryRepository kafkaInfoQueryRepository;

    public KafkaInfoServiceImpl(KafkaInfoQueryRepository kafkaInfoQueryRepository) {
        this.kafkaInfoQueryRepository = kafkaInfoQueryRepository;
    }

    @Override
    public KafkaInfo findOneByApiInfoId(Long apiInfoId) {
        return kafkaInfoQueryRepository.findByApiInfoId(apiInfoId);
    }
}
