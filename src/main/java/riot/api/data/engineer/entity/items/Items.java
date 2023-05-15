package riot.api.data.engineer.entity.items;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Items {
    String type;
    String version;
    List<Item> itemList;
}
