package riot.api.data.engineer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import riot.api.data.engineer.entity.MinioInfo;

public interface MinioRepository extends JpaRepository<MinioInfo, Long> {
}
