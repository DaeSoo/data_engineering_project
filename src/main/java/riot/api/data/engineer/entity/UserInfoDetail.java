package riot.api.data.engineer.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "user_info_detail")
@Setter
@Getter
public class UserInfoDetail extends BaseEntity {
    @Column(name="id")
    String id;
    @Column(name="account_id")
    String accountId;
    @Id
    @Column(name="puuid")
    String puuid;
    @Column(name="name")
    String name;
    @Column(name="revision_date")
    String revisionDate;
    @Column(name="summoner_level")
    String summonerLevel;
    @Column(name="profile_icon_id")
    String profileIconId;

    @Column(name = "api_key_id")
    @SerializedName("apiKeyId")
    private Long apiKeyId;
}
