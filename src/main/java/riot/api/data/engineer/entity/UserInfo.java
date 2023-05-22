package riot.api.data.engineer.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "user_info")
@Setter
@Getter
public class UserInfo {
    @Column(name="summoner_id")
    @SerializedName("summonerId")
    private String summonerId;
    @SerializedName("hotStreak")
    @Column(name = "hot_streak")
    private boolean hotStreak;
    @Column(name="fresh_blood")
    @SerializedName("freshBlood")
    private boolean freshBlood;
    @Column(name="in_active")
    @SerializedName("inactive")
    private boolean inactive;
    @Column(name="veteran")
    @SerializedName("veteran")
    private boolean veteran;
    @Column(name="losses")
    @SerializedName("losses")
    private int losses;
    @Column(name="wins")
    @SerializedName("wins")
    private int wins;
    @Column(name="league_points")
    @SerializedName("leaguePoints")
    private int leaguePoints;
    @Id
    @Column(name="summoner_name")
    @SerializedName("summonerName")
    private String summonerName;
    @Column(name="rank")
    @SerializedName("rank")
    private String rank;
    @Column(name="tier")
    @SerializedName("tier")
    private String tier;
    @Column(name="queue_type")
    @SerializedName("queueType")
    private String queueType;
    @Column(name="league_id")
    @SerializedName("leagueId")
    private String leagueId;

    @Column(name="update_yn", columnDefinition = "N")
    @SerializedName("updateYn")
    private String updateYn;

    @Column(name = "api_key_id")
    @SerializedName("apiKeyId")
    private Long apiKeyId;

}
