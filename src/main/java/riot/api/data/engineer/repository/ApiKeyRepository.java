package riot.api.data.engineer.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import riot.api.data.engineer.entity.api.ApiKey;
import riot.api.data.engineer.entity.api.QApiKey;

@RequiredArgsConstructor
@Repository
public class ApiKeyRepository {
    private final JPAQueryFactory queryFactory;

    public ApiKey findOne(){
        QApiKey qApiKey = new QApiKey("apiKey");
        return queryFactory.selectFrom(qApiKey)
                .where(
                        qApiKey.useYn.eq(true)
                        .and(qApiKey.apiKeyId.eq(1L)))
                .fetchOne();
    }
}
