package riot.api.data.engineer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import riot.api.data.engineer.entity.api.ApiParams;

public interface ApiParamsRepository extends JpaRepository<ApiParams, Long> {
}
