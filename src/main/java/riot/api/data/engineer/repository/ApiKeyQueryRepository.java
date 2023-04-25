package riot.api.data.engineer.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import riot.api.data.engineer.entity.api.ApiKey;
import riot.api.data.engineer.entity.api.QApiKey;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ApiKeyQueryRepository {
    private final JPAQueryFactory queryFactory;

    public ApiKey findOne(){
        QApiKey qApiKey = new QApiKey("apiKey");
        return queryFactory.selectFrom(qApiKey)
                .where(
                        qApiKey.useYn.eq(true)
                        .and(qApiKey.apiKeyId.eq(1L)))
                .fetchOne();
    }

    public List<ApiKey> findList() {
        QApiKey qApiKey = new QApiKey("apiKey");
        return queryFactory.selectFrom(qApiKey)
                .where(qApiKey.useYn.eq(true))
                .fetch();
    }
}
