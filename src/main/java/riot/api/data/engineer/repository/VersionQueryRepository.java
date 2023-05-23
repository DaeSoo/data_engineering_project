package riot.api.data.engineer.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import riot.api.data.engineer.entity.QVersion;
import riot.api.data.engineer.entity.Version;

@RequiredArgsConstructor
@Repository
public class VersionQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Version findOneByCurrentVersion(){
        QVersion qVersion = new QVersion("qVersion");
        return queryFactory.selectFrom(qVersion)
                .where(
                        qVersion.currentVersionYn.eq(true))
                .fetchOne();

    }


}
