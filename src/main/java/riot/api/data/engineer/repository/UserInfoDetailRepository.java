package riot.api.data.engineer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import riot.api.data.engineer.entity.UserInfoDetail;

public interface UserInfoDetailRepository extends JpaRepository<UserInfoDetail, String> {
}
