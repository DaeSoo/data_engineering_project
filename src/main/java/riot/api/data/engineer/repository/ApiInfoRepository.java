package riot.api.data.engineer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import riot.api.data.engineer.entity.api.ApiInfo;

public interface ApiInfoRepository extends JpaRepository<ApiInfo, Long> {
    ApiInfo findApiInfoByApiName(String apiName);
}
