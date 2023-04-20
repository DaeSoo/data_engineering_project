package riot.api.data.engineer.entity.api;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
public class ApiKey {
    @Id
    private Long id;

    private String apiKey;

    private Boolean useYn;
}
