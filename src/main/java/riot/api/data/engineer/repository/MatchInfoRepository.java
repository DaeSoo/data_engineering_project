package riot.api.data.engineer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import riot.api.data.engineer.entity.MatchInfo;

@Repository
public interface MatchInfoRepository extends JpaRepository<MatchInfo, String> {
}
