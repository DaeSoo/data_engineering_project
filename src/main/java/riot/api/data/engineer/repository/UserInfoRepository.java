package riot.api.data.engineer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import riot.api.data.engineer.entity.UserInfo;


public interface UserInfoRepository extends JpaRepository<UserInfo,String> {

}
