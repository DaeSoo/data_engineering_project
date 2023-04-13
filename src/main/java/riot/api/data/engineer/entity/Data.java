package riot.api.data.engineer.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Data {
    @SerializedName("stats")
    private Stats stats;
    @SerializedName("partype")
    private String partype;
    @SerializedName("tags")
    private List<String> tags;
    @SerializedName("image")
    private Image image;
    @SerializedName("info")
    private Info info;
    @SerializedName("blurb")
    private String blurb;
    @SerializedName("title")
    private String title;
    @SerializedName("name")
    private String name;
    @SerializedName("key")
    private String key;
    @SerializedName("id")
    private String id;
    @SerializedName("version")
    private String version;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Stats {
        @SerializedName("attackspeed")
        private double attackspeed;
        @SerializedName("attackspeedperlevel")
        private double attackspeedperlevel;
        @SerializedName("attackdamageperlevel")
        private int attackdamageperlevel;
        @SerializedName("attackdamage")
        private int attackdamage;
        @SerializedName("critperlevel")
        private int critperlevel;
        @SerializedName("crit")
        private int crit;
        @SerializedName("mpregenperlevel")
        private int mpregenperlevel;
        @SerializedName("mpregen")
        private int mpregen;
        @SerializedName("hpregenperlevel")
        private int hpregenperlevel;
        @SerializedName("hpregen")
        private int hpregen;
        @SerializedName("attackrange")
        private int attackrange;
        @SerializedName("spellblockperlevel")
        private double spellblockperlevel;
        @SerializedName("spellblock")
        private int spellblock;
        @SerializedName("armorperlevel")
        private double armorperlevel;
        @SerializedName("armor")
        private int armor;
        @SerializedName("movespeed")
        private int movespeed;
        @SerializedName("mpperlevel")
        private int mpperlevel;
        @SerializedName("mp")
        private int mp;
        @SerializedName("hpperlevel")
        private int hpperlevel;
        @SerializedName("hp")
        private int hp;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Image {
        @SerializedName("h")
        private int h;
        @SerializedName("w")
        private int w;
        @SerializedName("y")
        private int y;
        @SerializedName("x")
        private int x;
        @SerializedName("group")
        private String group;
        @SerializedName("sprite")
        private String sprite;
        @SerializedName("full")
        private String full;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Info {
        @SerializedName("difficulty")
        private int difficulty;
        @SerializedName("magic")
        private int magic;
        @SerializedName("defense")
        private int defense;
        @SerializedName("attack")
        private int attack;

        
    }
}
