package riot.api.data.engineer.entity.runes;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Slots {
    @SerializedName("runes")
    List<RuneDetail> runes;
}
