package riot.api.data.engineer.entity.champions;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Image {
    @JsonProperty("h")
    public int h;
    @JsonProperty("w")
    public int w;
    @JsonProperty("y")
    public int y;
    @JsonProperty("x")
    public int x;
    @JsonProperty("group")
    public String group;
    @JsonProperty("sprite")
    public String sprite;
    @JsonProperty("full")
    public String full;
}
