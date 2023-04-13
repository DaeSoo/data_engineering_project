package riot.api.data.engineer.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Info {
    @JsonProperty("difficulty")
    public int difficulty;
    @JsonProperty("magic")
    public int magic;
    @JsonProperty("defense")
    public int defense;
    @JsonProperty("attack")
    public int attack;
}
