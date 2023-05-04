package riot.api.data.engineer.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import riot.api.data.engineer.entity.UserInfoDetail;

import java.util.List;

import static riot.api.data.engineer.entity.QUserInfoDetail.userInfoDetail;


@Repository
@RequiredArgsConstructor
public class UserInfoDetailQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<UserInfoDetail> findListByApiKeyId(Long apiKeyId) {
        return queryFactory.selectFrom(userInfoDetail)
                .where(userInfoDetail.apiKeyId.eq(apiKeyId))
                .fetch();
    }
}
