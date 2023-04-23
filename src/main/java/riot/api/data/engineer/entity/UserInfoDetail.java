package riot.api.data.engineer.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "user_info_detail")
@Setter
@Getter
public class UserInfoDetail {
    String id;
    String accountId;
    @Id
    String puuid;
    String name;
    String revisionDate;
    String SummonerLevel;
}
