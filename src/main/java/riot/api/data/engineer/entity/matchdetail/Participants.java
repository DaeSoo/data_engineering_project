package riot.api.data.engineer.entity.matchdetail;


import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class Participants {

    @SerializedName("pushPings")
    private Integer pushpings;
    @SerializedName("summonerId")
    private String summonerid;
    @SerializedName("nexusTakedowns")
    private Integer nexustakedowns;
    @SerializedName("summonerName")
    private String summonername;
    @SerializedName("nexusKills")
    private Integer nexuskills;
    @SerializedName("item6")
    private Integer item6;
    @SerializedName("enemyVisionPings")
    private Integer enemyvisionpings;
    @SerializedName("summoner1Casts")
    private Integer summoner1casts;
    @SerializedName("win")
    private Boolean win;
    @SerializedName("riotIdName")
    private String riotidname;
    @SerializedName("allInPings")
    private Integer allinpings;
    @SerializedName("dragonKills")
    private Integer dragonkills;
    @SerializedName("wardsKilled")
    private Integer wardskilled;
    @SerializedName("baronKills")
    private Integer baronkills;
    @SerializedName("magicDamageDealt")
    private Integer magicdamagedealt;
    @SerializedName("lane")
    private String lane;
    @SerializedName("trueDamageDealt")
    private Integer truedamagedealt;
    @SerializedName("timeCCingOthers")
    private Integer timeccingothers;
    @SerializedName("objectivesStolenAssists")
    private Integer objectivesstolenassists;
    @SerializedName("trueDamageDealtToChampions")
    private Integer truedamagedealttochampions;
    @SerializedName("detectorWardsPlaced")
    private Integer detectorwardsplaced;
    @SerializedName("trueDamageTaken")
    private Integer truedamagetaken;
    @SerializedName("totalMinionsKilled")
    private Integer totalminionskilled;
    @SerializedName("deaths")
    private Integer deaths;
    @SerializedName("dangerPings")
    private Integer dangerpings;
    @SerializedName("champExperience")
    private Integer champexperience;
    @SerializedName("spell4Casts")
    private Integer spell4casts;
    @SerializedName("kills")
    private Integer kills;
    @SerializedName("enemyMissingPings")
    private Integer enemymissingpings;
    @SerializedName("onMyWayPings")
    private Integer onmywaypings;
    @SerializedName("tripleKills")
    private Integer triplekills;
    @SerializedName("itemsPurchased")
    private Integer itemspurchased;
    @SerializedName("item5")
    private Integer item5;
    @SerializedName("visionWardsBoughtInGame")
    private Integer visionwardsboughtingame;
    @SerializedName("largestKillingSpree")
    private Integer largestkillingspree;
    @SerializedName("turretKills")
    private Integer turretkills;
    @SerializedName("spell3Casts")
    private Integer spell3casts;
    @SerializedName("champLevel")
    private Integer champlevel;
    @SerializedName("needVisionPings")
    private Integer needvisionpings;
    @SerializedName("championTransform")
    private Integer championtransform;
    @SerializedName("neutralMinionsKilled")
    private Integer neutralminionskilled;
    @SerializedName("quadraKills")
    private Integer quadrakills;
    @SerializedName("firstBloodKill")
    private Boolean firstbloodkill;
    @SerializedName("eligibleForProgression")
    private Boolean eligibleforprogression;
    @SerializedName("nexusLost")
    private Integer nexuslost;
    @SerializedName("firstBloodAssist")
    private Boolean firstbloodassist;
    @SerializedName("damageDealtToBuildings")
    private Integer damagedealttobuildings;
    @SerializedName("firstTowerKill")
    private Boolean firsttowerkill;
    @SerializedName("firstTowerAssist")
    private Boolean firsttowerassist;
    @SerializedName("item0")
    private Integer item0;
    @SerializedName("basicPings")
    private Integer basicpings;
    @SerializedName("perks")
    private Perks perks;
    @SerializedName("summonerLevel")
    private Integer summonerlevel;
    @SerializedName("item1")
    private Integer item1;
    @SerializedName("gameEndedInSurrender")
    private Boolean gameendedinsurrender;
    @SerializedName("damageDealtToObjectives")
    private Integer damagedealttoobjectives;
    @SerializedName("spell1Casts")
    private Integer spell1casts;
    @SerializedName("longestTimeSpentLiving")
    private Integer longesttimespentliving;
    @SerializedName("turretsLost")
    private Integer turretslost;
    @SerializedName("physicalDamageDealtToChampions")
    private Integer physicaldamagedealttochampions;
    @SerializedName("largestCriticalStrike")
    private Integer largestcriticalstrike;
    @SerializedName("championName")
    private String championname;
    @SerializedName("largestMultiKill")
    private Integer largestmultikill;
    @SerializedName("totalUnitsHealed")
    private Integer totalunitshealed;
    @SerializedName("damageSelfMitigated")
    private Integer damageselfmitigated;
    @SerializedName("totalEnemyJungleMinionsKilled")
    private Integer totalenemyjungleminionskilled;
    @SerializedName("physicalDamageDealt")
    private Integer physicaldamagedealt;
    @SerializedName("consumablesPurchased")
    private Integer consumablespurchased;
    @SerializedName("totalTimeCCDealt")
    private Integer totaltimeccdealt;
    @SerializedName("totalDamageDealt")
    private Integer totaldamagedealt;
    @SerializedName("role")
    private String role;
    @SerializedName("participantId")
    private Integer participantid;
    @SerializedName("profileIcon")
    private Integer profileicon;
    @SerializedName("magicDamageTaken")
    private Integer magicdamagetaken;
    @SerializedName("visionScore")
    private Integer visionscore;
    @SerializedName("baitPings")
    private Integer baitpings;
    @SerializedName("turretTakedowns")
    private Integer turrettakedowns;
    @SerializedName("pentaKills")
    private Integer pentakills;
    @SerializedName("visionClearedPings")
    private Integer visionclearedpings;
    @SerializedName("doubleKills")
    private Integer doublekills;
    @SerializedName("sightWardsBoughtInGame")
    private Integer sightwardsboughtingame;
    @SerializedName("riotIdTagline")
    private String riotidtagline;
    @SerializedName("item4")
    private Integer item4;
    @SerializedName("item2")
    private Integer item2;
    @SerializedName("objectivesStolen")
    private Integer objectivesstolen;
    @SerializedName("inhibitorTakedowns")
    private Integer inhibitortakedowns;
    @SerializedName("summoner2Id")
    private Integer summoner2id;
    @SerializedName("commandPings")
    private Integer commandpings;
    @SerializedName("goldSpent")
    private Integer goldspent;
    @SerializedName("magicDamageDealtToChampions")
    private Integer magicdamagedealttochampions;
    @SerializedName("spell2Casts")
    private Integer spell2casts;
    @SerializedName("assistMePings")
    private Integer assistmepings;
    @SerializedName("totalHealsOnTeammates")
    private Integer totalhealsonteammates;
    @SerializedName("individualPosition")
    private String individualposition;
    @SerializedName("totalDamageShieldedOnTeammates")
    private Integer totaldamageshieldedonteammates;
    @SerializedName("totalDamageDealtToChampions")
    private Integer totaldamagedealttochampions;
    @SerializedName("teamPosition")
    private String teamposition;
    @SerializedName("killingSprees")
    private Integer killingsprees;
    @SerializedName("physicalDamageTaken")
    private Integer physicaldamagetaken;
    @SerializedName("gameEndedInEarlySurrender")
    private Boolean gameendedinearlysurrender;
    @SerializedName("bountyLevel")
    private Integer bountylevel;
    @SerializedName("damageDealtToTurrets")
    private Integer damagedealttoturrets;
    @SerializedName("totalDamageTaken")
    private Integer totaldamagetaken;
    @SerializedName("totalAllyJungleMinionsKilled")
    private Integer totalallyjungleminionskilled;
    @SerializedName("assists")
    private Integer assists;
    @SerializedName("getBackPings")
    private Integer getbackpings;
    @SerializedName("championId")
    private Integer championid;
    @SerializedName("summoner1Id")
    private Integer summoner1id;
    @SerializedName("puuid")
    private String puuid;
    @SerializedName("totalHeal")
    private Integer totalheal;
    @SerializedName("inhibitorsLost")
    private Integer inhibitorslost;
    @SerializedName("summoner2Casts")
    private Integer summoner2casts;
    @SerializedName("inhibitorKills")
    private Integer inhibitorkills;
    @SerializedName("teamEarlySurrendered")
    private Boolean teamearlysurrendered;
    @SerializedName("item3")
    private Integer item3;
    @SerializedName("unrealKills")
    private Integer unrealkills;
    @SerializedName("teamId")
    private Integer teamid;
    @SerializedName("totalTimeSpentDead")
    private Integer totaltimespentdead;
    @SerializedName("holdPings")
    private Integer holdpings;
    @SerializedName("goldEarned")
    private Integer goldearned;
    @SerializedName("timePlayed")
    private Integer timeplayed;
    @SerializedName("wardsPlaced")
    private Integer wardsplaced;

    public static class Perks {
        @SerializedName("styles")
        private List<Styles> styles;
        @SerializedName("statPerks")
        private Statperks statperks;
    }

    public static class Styles {
        @SerializedName("style")
        private Integer style;
        @SerializedName("selections")
        private List<Selections> selections;
        @SerializedName("description")
        private String description;
    }

    public static class Selections {
        @SerializedName("var1")
        private Integer var1;
        @SerializedName("perk")
        private Integer perk;
        @SerializedName("var2")
        private Integer var2;
        @SerializedName("var3")
        private Integer var3;
    }

    public static class Statperks {
        @SerializedName("flex")
        private Integer flex;
        @SerializedName("offense")
        private Integer offense;
        @SerializedName("defense")
        private Integer defense;
    }
}
