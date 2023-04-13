package riot.api.data.engineer.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Stats {
    @JsonProperty("attackspeed")
    public double attackspeed;
    @JsonProperty("attackspeedperlevel")
    public double attackspeedperlevel;
    @JsonProperty("attackdamageperlevel")
    public int attackdamageperlevel;
    @JsonProperty("attackdamage")
    public int attackdamage;
    @JsonProperty("critperlevel")
    public int critperlevel;
    @JsonProperty("crit")
    public int crit;
    @JsonProperty("mpregenperlevel")
    public int mpregenperlevel;
    @JsonProperty("mpregen")
    public int mpregen;
    @JsonProperty("hpregenperlevel")
    public int hpregenperlevel;
    @JsonProperty("hpregen")
    public int hpregen;
    @JsonProperty("attackrange")
    public int attackrange;
    @JsonProperty("spellblockperlevel")
    public double spellblockperlevel;
    @JsonProperty("spellblock")
    public int spellblock;
    @JsonProperty("armorperlevel")
    public double armorperlevel;
    @JsonProperty("armor")
    public int armor;
    @JsonProperty("movespeed")
    public int movespeed;
    @JsonProperty("mpperlevel")
    public int mpperlevel;
    @JsonProperty("mp")
    public int mp;
    @JsonProperty("hpperlevel")
    public int hpperlevel;
    @JsonProperty("hp")
    public int hp;
}
