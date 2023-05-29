package riot.api.data.engineer.entity.champions;

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
    @SerializedName("currentVersion")
    private String version;

}
