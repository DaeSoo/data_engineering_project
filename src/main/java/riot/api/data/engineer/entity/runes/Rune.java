package riot.api.data.engineer.entity.runes;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Rune {
    String id;
    String key;
    String name;
    List<Slots> slots;
}
