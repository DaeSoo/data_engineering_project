package riot.api.data.engineer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import riot.api.data.engineer.entity.Version;


public interface VersionRepository extends JpaRepository<Version,String> {

}
