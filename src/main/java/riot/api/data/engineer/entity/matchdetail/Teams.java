package riot.api.data.engineer.entity.matchdetail;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class Teams {

    @SerializedName("win")
    private Boolean win;
    @SerializedName("teamId")
    private Integer teamid;
    @SerializedName("objectives")
    private Objectives objectives;
    @SerializedName("bans")
    private List<Bans> bans;

    public static class Objectives {
        @SerializedName("tower")
        private Tower tower;
        @SerializedName("riftHerald")
        private Riftherald riftherald;
        @SerializedName("inhibitor")
        private Inhibitor inhibitor;
        @SerializedName("dragon")
        private Dragon dragon;
        @SerializedName("champion")
        private Champion champion;
        @SerializedName("baron")
        private Baron baron;
    }

    public static class Bans{
        @SerializedName("championId")
        private Integer championid;
        @SerializedName("pickTurn")
        private Integer pickturn;
    }

    public static class Tower {
        @SerializedName("kills")
        private Integer kills;
        @SerializedName("first")
        private Boolean first;
    }

    public static class Riftherald {
        @SerializedName("kills")
        private Integer kills;
        @SerializedName("first")
        private Boolean first;
    }

    public static class Inhibitor {
        @SerializedName("kills")
        private Integer kills;
        @SerializedName("first")
        private Boolean first;
    }

    public static class Dragon {
        @SerializedName("kills")
        private Integer kills;
        @SerializedName("first")
        private Boolean first;
    }

    public static class Champion {
        @SerializedName("kills")
        private Integer kills;
        @SerializedName("first")
        private Boolean first;
    }

    public static class Baron {
        @SerializedName("kills")
        private Integer kills;
        @SerializedName("first")
        private Boolean first;
    }
}
