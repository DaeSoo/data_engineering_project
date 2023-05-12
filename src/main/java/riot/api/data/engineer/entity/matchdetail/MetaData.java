package riot.api.data.engineer.entity.matchdetail;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class MetaData {
    @SerializedName("participants")
    private List<String> participants;
    @SerializedName("matchId")
    private String matchid;
    @SerializedName("dataVersion")
    private String dataversion;

}
