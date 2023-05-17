package riot.api.data.engineer.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import riot.api.data.engineer.entity.api.ApiParams;

import java.util.List;

import static riot.api.data.engineer.entity.api.QApiParams.apiParams;

@RequiredArgsConstructor
@Repository
public class ApiParamsQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<ApiParams> findByApiInfoId(Long apiInfoId){
        return queryFactory.selectFrom(apiParams)
                .where(apiParams.apiInfoId.eq(apiInfoId))
                .fetch();
    }
}
