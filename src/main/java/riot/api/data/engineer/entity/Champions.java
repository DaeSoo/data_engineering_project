package riot.api.data.engineer.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Champions {
    private String type;
    private String version;
    private String format;
    private List<Data> dataList;
}
