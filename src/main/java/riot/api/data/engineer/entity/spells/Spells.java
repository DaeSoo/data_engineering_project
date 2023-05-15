package riot.api.data.engineer.entity.spells;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Spells {
    String type;
    String version;
    List<Spell> spellList;
}
