package riot.api.data.engineer.entity.spells;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Spell {
    @SerializedName("id")
    String id;
    @SerializedName("name")
    String name;
    @SerializedName("description")
    String description;
    @SerializedName("maxrank")
    Integer maxrank;
    @SerializedName("cooldownBurn")
    String cooldownBurn;
    @SerializedName("costBurn")
    String costBurn;
    @SerializedName("key")
    String key;
    @SerializedName("costType")
    String costType;
    @SerializedName("maxammo")
    Integer maxammo;
    @SerializedName("rangeBurn")
    String rangeBurn;
    @SerializedName("resource")
    String resource;
    @SerializedName("currentVersion")
    String version;

}

