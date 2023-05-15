package riot.api.data.engineer.entity.items;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class Item {
    String name;
    String description;
    String colloq;
    String plaintext;
    List<String> from;
    List<String> into;
    Gold gold;
    List<String> tags;
    Integer depth;
    Stats stats;
    String version;
}
