package riot.api.data.engineer.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import riot.api.data.engineer.entity.KafkaInfo;

import static riot.api.data.engineer.entity.QKafkaInfo.kafkaInfo;


@RequiredArgsConstructor
@Repository
public class KafkaInfoQueryRepository {
    private final JPAQueryFactory queryFactory;
    public KafkaInfo findByApiInfoId(Long apiInfoId){
        return queryFactory.selectFrom(kafkaInfo)
                .where(kafkaInfo.apiInfoId.eq(apiInfoId))
                .fetchOne();
    }
}
