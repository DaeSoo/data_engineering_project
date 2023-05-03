package riot.api.data.engineer.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import riot.api.data.engineer.entity.UserInfo;

import java.util.List;

import static riot.api.data.engineer.entity.QUserInfo.userInfo;

@RequiredArgsConstructor
@Repository
public class UserInfoQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<UserInfo> findListByApiKeyId(Long apiKeyId) {
        return queryFactory.selectFrom(userInfo)
                .where(userInfo.apiKeyId.eq(apiKeyId))
                .fetch();
    }

}
