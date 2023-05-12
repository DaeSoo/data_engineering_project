package riot.api.data.engineer.entity.matchdetail;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class Info {
    @SerializedName("gameEndTimestamp")
    Long gameEndTimestamp;
    @SerializedName("gameId")
    Long gameId;
    @SerializedName("gameMode")
    String gameMode;
    @SerializedName("gameName")
    String gameName;
    @SerializedName("gameStartTimestamp")
    Long gameStartTimestamp;
    @SerializedName("gameType")
    String gameType;
    @SerializedName("gameVersion")
    String gameVersion;
    @SerializedName("mapId")
    Integer mapId;
    @SerializedName("participants")
    List<Participants> participants;
    @SerializedName("platformId")
    String platformId;
    @SerializedName("queueId")
    Integer queueId;
    @SerializedName("teams")
    List<Teams> teams;
    @SerializedName("tournamentCode")
    String tournamentCode;
}
