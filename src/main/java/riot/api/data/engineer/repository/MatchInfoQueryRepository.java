package riot.api.data.engineer.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import riot.api.data.engineer.entity.MatchInfo;

import java.util.List;

import static riot.api.data.engineer.entity.QMatchInfo.matchInfo;

@RequiredArgsConstructor
@Repository
public class MatchInfoQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<MatchInfo> findMatchInfoByApiKeyId(Long apiKeyId) {
        return queryFactory.selectFrom(matchInfo)
                .where(matchInfo.apiKeyId.eq(apiKeyId))
                .fetch();
    }

    public List<MatchInfo> findMatchInfoByCollectCompleteYn() {
        return queryFactory.selectFrom(matchInfo)
                .where(matchInfo.collectCompleteYn.eq(false))
                .fetch();
    }
}
