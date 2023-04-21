package riot.api.data.engineer.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import riot.api.data.engineer.entity.api.ApiInfo;
import java.util.List;
import static riot.api.data.engineer.entity.api.QApiInfo.apiInfo;

@RequiredArgsConstructor
@Repository
public class ApiInfoRepository {
    private final JPAQueryFactory queryFactory;

    public List<ApiInfo> findByName(String apiName) {
        return queryFactory.selectFrom(apiInfo)
                .where(apiInfo.apiName.eq(apiName))
                .fetch();
    }
}
